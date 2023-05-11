package selva.oss.ds.document;

import static selva.oss.lang.Commons.*;
import selva.oss.ds.datatype.DataTypeConfig;
import selva.oss.ds.value.TypedValue;

import java.util.*;

public interface DocumentStore<T> extends Document<T>, Store<T> {

    public static DocumentStore<String> create() {
        return new DocumentImpl();
    }

}
