package tech.nilu.wallet.model;

/**
 * Created by mnemati on 11/30/17.
 */

public class LiveResponse<T> {
    private T data;
    private CustomException exception;
    private LiveResponseStatus liveStatus;


    public LiveResponse(T data, LiveResponseStatus status) {
        this.data = data;
        this.liveStatus = status;
    }

    public LiveResponse(CustomException t) {
        this.exception = t;
        this.liveStatus = LiveResponseStatus.FAILED;
    }

    public T getData() {
        return data;
    }

    public LiveResponseStatus getLiveStatus() {
        return liveStatus;
    }

    public CustomException toCustomException() {
        return exception;
    }


    public static <T> LiveResponse<T> of(T data, LiveResponseStatus status) {
        return new LiveResponse<T>(data, status);
    }

    public static <T> LiveResponse<T> of(T data) {
        return new LiveResponse<T>(data, LiveResponseStatus.SUCCEED);
    }

    public static <T> LiveResponse<T> of(LiveResponseStatus status) {
        return new LiveResponse<T>(null, status);
    }

    public static <T> LiveResponse<T> of(Throwable t) {
        if (t instanceof CustomException)
            return new LiveResponse<T>((CustomException) t);
        return new LiveResponse<T>(new CustomException(t.getMessage(), t));
    }

}
