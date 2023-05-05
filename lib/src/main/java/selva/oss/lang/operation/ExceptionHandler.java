package selva.oss.lang.operation;

import static selva.oss.lang.Commons.*;
import selva.oss.lang.CommonValidations;

public class ExceptionHandler {

    public interface Operation<T> {
        public T operate();
    }

    public static <T> OpsResult<T> toOpsResult(Operation<T> operation, String errorMessage) {
        try {
            validateNotNull(operation);
            validateNotNull(errorMessage);

            return OpsResult.success(operation.operate());
        }
        catch (CommonValidations.NullNotExpectedException | CommonValidations.InvalidStateException e) {
            return OpsResult.error(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return OpsResult.error(errorMessage);
        }
    }

}
