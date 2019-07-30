//
// Created by root on 11/18/18.
//

#include <jni.h>
#include <string>
#include <sstream>
#include <android/log.h>

static const char *kTAG = "nilu";
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, kTAG, __VA_ARGS__))
#define LOGW(...) ((void)__android_log_print(ANDROID_LOG_WARN, kTAG, __VA_ARGS__))
#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR, kTAG, __VA_ARGS__))

typedef struct tick_context {
    JavaVM *javaVM;
    jclass jniHelperClz;
    jobject jniHelperObj;
    jclass mainActivityClz;
    jobject mainActivityObj;
    pthread_mutex_t lock;
    int done;
} TickContext;
TickContext g_ctx;

typedef struct build_args {
    const char *board;
    const char *brand;
    const char *cpuABI;
    const char *device;
    const char *manufacturer;
    const char *model;
    const char *product;
    const char *serial;
} BuildArgs;
BuildArgs buildArgs;

template<class T>
inline std::string to_string(const T &t) {
    std::stringstream ss;
    ss << t;
    return ss.str();
}

int hashCode(const char *s) {
    int hash = 0;
    for (int i = 0; i < strlen(s); i++) {
        hash = 31 * hash + s[i];
    }
    return hash;
}

const char *digits(long val, int digits) {
    long hi = 1L << (digits * 4);
    std::stringstream ss;
    ss << std::hex << (hi | (val & (hi - 1)));
    std::string result = ss.str();
    return result.substr(1, result.length()).c_str();
}

std::string getUUID(long mostSigBits, long leastSigBits) {
    return std::string(digits(mostSigBits >> 32, 8)) +
           std::string(digits(mostSigBits >> 16, 4)) +
           std::string(digits(mostSigBits, 4)) +
           std::string(digits(leastSigBits >> 48, 4)) +
           std::string(digits(leastSigBits, 12));
}

const char *getStaticProperty(JNIEnv *env, const char *methodId) {
    jmethodID staticFunc = env->GetStaticMethodID(g_ctx.jniHelperClz, methodId,
                                                  "()Ljava/lang/String;");
    if (!staticFunc) {
        LOGE("Failed to retrieve %s methodID @ line %d", methodId, __LINE__);
        return NULL;
    }
    jstring buildProperty = (jstring) env->CallStaticObjectMethod(g_ctx.jniHelperClz, staticFunc);
    const char *property = env->GetStringUTFChars(buildProperty, NULL);
    if (!property) {
        LOGE("Unable to get property @ line %d", __LINE__);
        return NULL;
    }
    LOGI("%s", property);
    return property;
}

void queryRuntimeInfo(JNIEnv *env, jobject instance) {
    memset(&buildArgs, 0, sizeof(buildArgs));
    buildArgs.board = getStaticProperty(env, "getBuildBoard");
    buildArgs.brand = getStaticProperty(env, "getBuildBrand");
    buildArgs.cpuABI = getStaticProperty(env, "getBuildCpuABI");
    buildArgs.device = getStaticProperty(env, "getBuildDevice");
    buildArgs.manufacturer = getStaticProperty(env, "getBuildManufacturer");
    buildArgs.model = getStaticProperty(env, "getBuildModel");
    buildArgs.product = getStaticProperty(env, "getBuildProduct");
    buildArgs.serial = getStaticProperty(env, "getBuildSerial");
}

jstring getPseudoID(JNIEnv *env) {
    std::string board(buildArgs.board);
    std::string brand(buildArgs.brand);
    std::string cpuABI(buildArgs.cpuABI);
    std::string device(buildArgs.device);
    std::string manufacturer(buildArgs.manufacturer);
    std::string model(buildArgs.model);
    std::string product(buildArgs.product);

    std::string m_szDevIDShort = "35" +
                                 to_string(board.length() % 10) +
                                 to_string(brand.length() % 10) +
                                 to_string(cpuABI.length() % 10) +
                                 to_string(device.length() % 10) +
                                 to_string(manufacturer.length() % 10) +
                                 to_string(model.length() % 10) +
                                 to_string(product.length() % 10);
    std::string uuid = getUUID(hashCode(m_szDevIDShort.c_str()), hashCode(buildArgs.serial));
    while (uuid.length() < 32)
        uuid = "f" + uuid;
    return env->NewStringUTF(uuid.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_tech_nilu_wallet_util_SecurityUtils_getPseudoID(JNIEnv *env, jobject thiz) {
    return getPseudoID(env);
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    memset(&g_ctx, 0, sizeof(g_ctx));

    g_ctx.javaVM = vm;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR; // JNI version not supported.
    }

    jclass clz = env->FindClass("tech/nilu/wallet/JniHandler");
    g_ctx.jniHelperClz = (jclass) env->NewGlobalRef(clz);

    jmethodID jniHelperCtor = env->GetMethodID(g_ctx.jniHelperClz, "<init>", "()V");
    jobject handler = env->NewObject(g_ctx.jniHelperClz, jniHelperCtor);
    g_ctx.jniHelperObj = env->NewGlobalRef(handler);
    queryRuntimeInfo(env, g_ctx.jniHelperObj);

    g_ctx.done = 0;
    g_ctx.mainActivityObj = NULL;
    return JNI_VERSION_1_6;
}
