package selva.oss.lang.operation;

import static selva.oss.lang.Commons.*;
import selva.oss.lang.CommonValidations;

public class ExceptionHandler {

    public interface Operation {
        public void operate();
    }

    public static OpsResult toOpsResult(Operation operation, String errorMessage) {
        try {
            validateNotNull(operation);
            validateNotNull(errorMessage);

            operation.operate();
            return OpsResult.success();
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
