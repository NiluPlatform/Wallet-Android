package tech.nilu.wallet.api;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ResponseHandler {
    @Inject
    public ResponseHandler() {

    }

    public <T> void run(Context context, Callback<T> successCallback, T response) {

    }

    public interface Callback<T> {
        void call(T response);
    }
}
