package tech.nilu.wallet.injection;

import android.content.Context;

import java.lang.annotation.Annotation;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tech.nilu.wallet.R;
import tech.nilu.wallet.api.Ether1Api;
import tech.nilu.wallet.api.EthplorerApi;
import tech.nilu.wallet.api.FaucetApi;
import tech.nilu.wallet.api.MetaApi;
import tech.nilu.wallet.api.NiluApi;
import tech.nilu.wallet.api.PirlApi;
import tech.nilu.wallet.api.TokenApi;
import tech.nilu.wallet.api.model.BasicResponse;

@Module
public class WebServiceModule {
    @Provides
    @Singleton
    Retrofit.Builder provideRetrofitBuilder(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient);
    }

    @Provides
    @Singleton
    @Inject
    Retrofit provideRetrofit(Retrofit.Builder builder, Context context) {
        return builder.baseUrl(context.getString(R.string.nilu_explorer)).build();
    }

    @Provides
    @Singleton
    @Inject
    Converter<ResponseBody, BasicResponse> provideErrorConverter(Retrofit retrofit) {
        return retrofit.responseBodyConverter(BasicResponse.class, new Annotation[0]);
    }

    @Provides
    @Singleton
    @Inject
    FaucetApi provideFaucetApi(Retrofit retrofit) {
        return retrofit.create(FaucetApi.class);
    }

    @Provides
    @Singleton
    @Inject
    NiluApi provideNiluApi(Retrofit retrofit) {
        return retrofit.create(NiluApi.class);
    }

    @Provides
    @Singleton
    @Inject
    EthplorerApi provideEthplorerApi(Retrofit.Builder builder, Context context) {
        return builder.baseUrl(context.getString(R.string.ethplorer_explorer))
                .build()
                .create(EthplorerApi.class);
    }

    @Provides
    @Singleton
    MetaApi provideMetaApi(Retrofit.Builder builder, Context context) {
        return builder.baseUrl(context.getString(R.string.meta_api))
                .build()
                .create(MetaApi.class);
    }

    @Provides
    @Singleton
    PirlApi providePirlApi(Retrofit.Builder builder, Context context) {
        return builder.baseUrl(context.getString(R.string.pirl_explorer))
                .build()
                .create(PirlApi.class);
    }

    @Provides
    @Singleton
    Ether1Api provideEther1Api(Retrofit.Builder builder, Context context) {
        return builder.baseUrl(context.getString(R.string.ether1_explorer))
                .build()
                .create(Ether1Api.class);
    }

    @Provides
    @Singleton
    TokenApi provideTokenApi(Retrofit.Builder builder, Context context) {
        return builder.baseUrl(context.getString(R.string.ethplorer_explorer))
                .build()
                .create(TokenApi.class);
    }
}
