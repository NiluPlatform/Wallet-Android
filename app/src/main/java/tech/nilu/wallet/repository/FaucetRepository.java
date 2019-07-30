package tech.nilu.wallet.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.nilu.wallet.api.ErrorConverter;
import tech.nilu.wallet.api.FaucetApi;
import tech.nilu.wallet.api.MetaApi;
import tech.nilu.wallet.api.model.faucet.FaucetRequest;
import tech.nilu.wallet.api.model.faucet.FaucetResponse;
import tech.nilu.wallet.api.model.meta.AppIdResponse;
import tech.nilu.wallet.model.CustomException;
import tech.nilu.wallet.model.LiveResponse;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.util.MyKeyStore;
import tech.nilu.wallet.util.SecurityUtils;
import tech.nilu.wallet.util.SharedPreferencesUtils;

@Singleton
public class FaucetRepository {
    private final FaucetApi api;
    private final MetaApi metaApi;
    private final MyKeyStore keyStore;
    private final ErrorConverter errorConverter;
    private final SharedPreferencesUtils sharedPreferencesUtils;

    @Inject
    public FaucetRepository(FaucetApi faucetApi, MetaApi metaApi, MyKeyStore keyStore, ErrorConverter errorConverter, SharedPreferencesUtils sharedPreferencesUtils) {
        this.api = faucetApi;
        this.metaApi = metaApi;
        this.keyStore = keyStore;
        this.errorConverter = errorConverter;
        this.sharedPreferencesUtils = sharedPreferencesUtils;
    }

    public LiveData<LiveResponse<FaucetResponse>> getFaucet(String type, String address) {
        MutableLiveData<LiveResponse<FaucetResponse>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        String encryptedValue = sharedPreferencesUtils.getPreferenceString(MyKeyStore.IDENTIFIER);
        String identifier = encryptedValue == null ? null : keyStore.decryptString(MyKeyStore.NILU_ALIAS, encryptedValue);

        SecureRandom sr = new SecureRandom();
        int amount = sr.nextInt(5) + 1;
        int[] randoms = new int[]{
                sr.nextInt(10),
                sr.nextInt(10),
                sr.nextInt(10)
        };
        int randomsChecksum = (randoms[0] + randoms[1] + randoms[2]) % 10;
        int checksum = (randoms[0] + randoms[1] + randoms[2] + randomsChecksum + amount) % 10;
        String date = new SimpleDateFormat("yyMMddHHmm", Locale.getDefault()).format(new Date());
        String message = String.valueOf(randoms[0]) + String.valueOf(randoms[1]) + String.valueOf(randoms[2]) + randomsChecksum + "0" + String.valueOf(amount) + checksum + date + identifier;
        Log.d("FaucetRepository", "getFaucet: " + message);
        String hmac = SecurityUtils.createHmac(message);
        String magic = String.format(Locale.getDefault(), "%s%s%s%s0%s%s%s%s",
                String.valueOf(randoms[0]),
                String.valueOf(randoms[1]),
                String.valueOf(randoms[2]),
                String.valueOf(randomsChecksum),
                String.valueOf(amount),
                String.valueOf(checksum),
                date,
                hmac);

        FaucetRequest req = new FaucetRequest(identifier, magic);
        api.getFaucet(type, address, req).enqueue(new Callback<FaucetResponse>() {
            @Override
            public void onResponse(Call<FaucetResponse> call, Response<FaucetResponse> response) {
                if (response.isSuccessful())
                    result.setValue(LiveResponse.of(response.body()));
                else
                    result.setValue(LiveResponse.of(new CustomException(response.errorBody().toString())));
            }

            @Override
            public void onFailure(Call<FaucetResponse> call, Throwable t) {
                result.setValue(LiveResponse.of(t));
            }
        });

        return result;
    }

    public LiveData<LiveResponse<AppIdResponse>> getAppId() {
        MutableLiveData<LiveResponse<AppIdResponse>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        String code = SecurityUtils.getCode();
        String encrypted = SecurityUtils.encrypt(code);
        metaApi.getAppId(code, encrypted).enqueue(new Callback<AppIdResponse>() {
            @Override
            public void onResponse(Call<AppIdResponse> call, Response<AppIdResponse> response) {
                if (response.isSuccessful())
                    result.setValue(LiveResponse.of(response.body()));
                else
                    result.setValue(LiveResponse.of(errorConverter.getError(response.errorBody())));
            }

            @Override
            public void onFailure(Call<AppIdResponse> call, Throwable t) {
                result.setValue(LiveResponse.of(t));
            }
        });

        return result;
    }
}
