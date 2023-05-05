package selva.oss.lang.operation;

public class OpsResult<T> {

    public enum Status {
        Success, Error
    }

    private Status status;
    private T data;
    private String errorMessage;

    private OpsResult(Status status) {
        this.status = status;
    }

    private OpsResult(Status status, T data) {
        this.status = status;
        this.data = data;
    }

    private OpsResult(Status status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public Status getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public T getData() {
        return data;
    }

    public static <T> OpsResult<T> success() {
        return new OpsResult(Status.Success);
    }

    public static <T> OpsResult<T> success(T data) {
        return new OpsResult(Status.Success, data);
    }

    public static <T> OpsResult<T> error(String errorMessage) {
        return new OpsResult(Status.Error, errorMessage);
    }

}
