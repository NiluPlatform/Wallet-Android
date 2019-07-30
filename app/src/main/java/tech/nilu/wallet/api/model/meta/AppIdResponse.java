package tech.nilu.wallet.api.model.meta;

import com.google.gson.annotations.SerializedName;

import tech.nilu.wallet.api.model.BasicResponse;

public class AppIdResponse extends BasicResponse {
    @SerializedName("user_app_id")
    private final String appId;

    public AppIdResponse(String appId) {
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }

    @Override
    public String toString() {
        return "AppIdResponse{" +
                "appId='" + appId + '\'' +
                '}';
    }
}
