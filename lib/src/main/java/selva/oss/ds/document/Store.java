package selva.oss.ds.document;

import static selva.oss.lang.CommonValidation.*;
import selva.oss.ds.document.datatype.DataTypeConfig;
import selva.oss.ds.document.datatype.TypedValue;

import java.util.*;

public interface Store<T> extends StoreGetApi<T>, StoreSetApi<T> {

    public static Store<String> create(Object object) {
        return new PojoDocumentImpl(object);
    }

    public static Store<String> create(Class pojoClass) {
        return new PojoDocumentImpl(pojoClass);
    }

}
