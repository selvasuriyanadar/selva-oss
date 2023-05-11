package selva.oss.ds.document;

import static selva.oss.lang.Commons.*;
import selva.oss.ds.datatype.DataTypeConfig;
import selva.oss.ds.value.TypedValue;

import java.util.*;

interface StoreGetApi<T> {

    public TypedValue getTypedValue(T field);

    public Optional<TypedValue> getTypedValueOptional(String field);

    public DataTypeConfig getConfig(String field);

    public boolean isFieldPresent(String field);

    public void verifyFieldExist(String field);

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

    default boolean isFieldPresent(String field) {
        return containsField(DocumentParamsStateValidator.createWithField(field));
    }

    public static class FieldDoesNotExistException extends RuntimeException {
    }

    default void verifyFieldExist(String field) {
        if (!isFieldPresent(field)) {
            throw new FieldDoesNotExistException();
        }
    }

}
