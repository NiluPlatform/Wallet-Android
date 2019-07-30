package tech.nilu.wallet.api.model.token;

import java.util.Arrays;

public class AddressInfoResponse {
    private final String address;
    private final EthInfo eth;
    private final TokenInfo tokenInfo;
    private final BalanceInfo[] tokens;
    private final Error error;

    public AddressInfoResponse(String address, EthInfo ETH, TokenInfo tokenInfo, BalanceInfo[] tokens, Error error) {
        this.address = address;
        this.eth = ETH;
        this.tokenInfo = tokenInfo;
        this.tokens = tokens;
        this.error = error;
    }

    public String getAddress() {
        return address;
    }

    public EthInfo getEth() {
        return eth;
    }

    public TokenInfo getTokenInfo() {
        return tokenInfo;
    }

    public BalanceInfo[] getTokens() {
        return tokens;
    }

    public Error getError() {
        return error;
    }

    @Override
    public String toString() {
        return "AddressInfoResponse{" +
                "address='" + address + '\'' +
                ", eth=" + eth +
                ", tokenInfo=" + tokenInfo +
                ", tokens=" + Arrays.toString(tokens) +
                ", error=" + error +
                '}';
    }

    public class Error {
        private final int code;
        private final String message;

        public Error(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return "Error{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}
