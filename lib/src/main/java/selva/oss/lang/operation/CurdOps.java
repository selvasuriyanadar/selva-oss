package selva.oss.lang.operation;

import static selva.oss.lang.Commons.*;

public class CurdOps {

    public interface InsertingRecord {
        public int insert();
    }

    public static class RecordNotInsertedException extends RuntimeException {
    }

    private static void throwIfRecordNotInserted(InsertingRecord insertingRecord) {
        if (insertingRecord.insert() < 0) {
            throw new RecordNotInsertedException();
        }
    }

    public static OpsResult insert(InsertingRecord insertingRecord, String errorMessage) {
        return ExceptionHandler.toOpsResult(() -> {
            validateNotNull(insertingRecord);

            throwIfRecordNotInserted(insertingRecord);
        }, errorMessage);
    }

}
