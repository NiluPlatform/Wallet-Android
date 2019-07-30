# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

-printmapping mapping.txt

-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

-keep class tech.nilu.wallet.api.model.** { *; }
-keep class tech.nilu.wallet.db.entity.** { *; }
-keep class tech.nilu.wallet.model.** { *; }

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**

# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8

-dontwarn com.squareup.okhttp.**

-dontwarn dagger.android.**

-dontwarn com.fasterxml.jackson.databind.**
-keep class org.** { *; }
-keep class com.fasterxml.jackson.core.** { *; }
-keep interface com.fasterxml.jackson.core { *; }
-keep public class * extends com.fasterxml.jackson.core.*
-keep class com.fasterxml.jackson.databind.introspect.VisibilityChecker$Std.
-keep class com.fasterxml.jackson.databind.ObjectMapper.
-keep class com.fasterxml.jackson.databind.** { *; }
-keep class com.fasterxml.jackson.databind.introspect.VisibilityChecker${ *; }
-keep interface com.fasterxml.jackson.databind { *; }
-keep public class * extends com.fasterxml.jackson.databind.*
-keep class com.fasterxml.jackson.annotation.** { *; }
-keep interface com.fasterxml.jackson.annotation.** { *; }
-keep class org.spongycastle.**