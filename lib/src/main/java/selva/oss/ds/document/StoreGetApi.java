package selva.oss.ds.document;

import static selva.oss.lang.CommonValidation.*;
import selva.oss.ds.document.datatype.DataTypeConfig;
import selva.oss.ds.document.datatype.TypedValue;

import java.util.*;

interface StoreGetApi<T> {

    public TypedValue getTypedValue(T field);

    public Optional<TypedValue> getTypedValueOptional(String field);

    public DataTypeConfig getConfig(String field);

}

interface StoreGetOps extends DocumentBaseApi, DocumentStore<String> {

    default TypedValue getTypedValue(String field) {
        return fetchTypedValueSure(DocumentParamsStateValidator.createWithValidField(field));
    }

    default Optional<TypedValue> getTypedValueOptional(String field) {
        return fetchTypedValue(DocumentParamsStateValidator.createWithValidField(field));
    }

    default DataTypeConfig getConfig(String field) {
        return fetchConfigSure(DocumentParamsStateValidator.createWithValidField(field));
    }

}
