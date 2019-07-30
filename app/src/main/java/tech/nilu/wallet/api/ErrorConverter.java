package tech.nilu.wallet.api;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import tech.nilu.wallet.api.model.BasicResponse;

@Singleton
public class ErrorConverter {
    private final Converter<ResponseBody, BasicResponse> converter;

    @Inject
    public ErrorConverter(Converter<ResponseBody, BasicResponse> converter) {
        this.converter = converter;
    }

    public Throwable getError(ResponseBody body) {
        try {
            BasicResponse error = converter.convert(body);
            return new Throwable(error.getError());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Throwable("Operation Failed!");
    }
}
