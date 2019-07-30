package tech.nilu.wallet.api.model;

import android.text.TextUtils;

import java.io.Serializable;

public class BasicResponse implements Serializable {
    protected int status = 200;
    protected String error;
    protected String message;

    public BasicResponse() {

    }

    public BasicResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
        if (!TextUtils.isEmpty(error) && status == -1)
            status = 500;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccessful() {
        return this.status == 200 || this.status == 0;
    }
}
