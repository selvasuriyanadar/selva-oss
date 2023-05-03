package selva.oss.lang.operation;

public class OpsResult {

    public enum Status {
        Success, Error
    }

    private Status status;
    private String errorMessage;

    private OpsResult(Status status) {
        this.status = status;
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

    public static OpsResult success() {
        return new OpsResult(Status.Success);
    }

    public static OpsResult error(String errorMessage) {
        return new OpsResult(Status.Error, errorMessage);
    }

}
