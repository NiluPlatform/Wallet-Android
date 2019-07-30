package tech.nilu.wallet.model;

/**
 * Created by mnemati on 1/4/18.
 */

public class CustomException extends RuntimeException {

    public static String WALLET_NO_LOGIN_ERROR = "wallet.no.login.err";

    private String code;

    public CustomException(String code) {
        super(code);
        this.code = code;
    }

    public CustomException(String code, Throwable cause) {
        super(code, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
