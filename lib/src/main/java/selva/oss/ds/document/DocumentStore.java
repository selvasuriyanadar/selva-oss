package selva.oss.ds.document;

import static selva.oss.lang.CommonValidation.*;
import selva.oss.ds.document.datatype.DataTypeConfig;
import selva.oss.ds.document.datatype.TypedValue;

import java.util.*;

public interface DocumentStore<T> extends Document<T>, Store<T> {

    public static DocumentStore<String> create() {
        return new DocumentImpl();
    }

}
