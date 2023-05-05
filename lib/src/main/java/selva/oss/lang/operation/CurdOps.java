package selva.oss.lang.operation;

import static selva.oss.lang.Commons.*;

public class CurdOps {

    public interface InsertingRecord {
        public int insert();
    }

    public static class RecordNotInsertedException extends RuntimeException {
    }

    private static int throwIfRecordNotInserted(InsertingRecord insertingRecord) {
        int id = insertingRecord.insert();
        if (id < 0) {
            throw new RecordNotInsertedException();
        }
        return id;
    }

    public static OpsResult<Integer> insert(InsertingRecord insertingRecord, String errorMessage) {
        return ExceptionHandler.toOpsResult(() -> {
            validateNotNull(insertingRecord);

            return throwIfRecordNotInserted(insertingRecord);
        }, errorMessage);
    }

}
