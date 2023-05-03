package selva.oss.ds.document;

import static selva.oss.lang.Commons.*;
import selva.oss.ds.document.datatype.DataTypeConfig;
import selva.oss.ds.document.datatype.TypedValue;

import java.util.*;

interface DocumentBaseApi {

    public boolean containsValue(DocumentParamsStateValidator documentParamsStateValidator);

    public boolean containsField(DocumentParamsStateValidator documentParamsStateValidator);

    public TypedValue fetchTypedValueSure(DocumentParamsStateValidator documentParamsStateValidator);

    public Optional<TypedValue> fetchTypedValue(DocumentParamsStateValidator documentParamsStateValidator);

    public DataTypeConfig fetchConfigSure(DocumentParamsStateValidator documentParamsStateValidator);

    public void put(DocumentParamsStateValidator documentParamsStateValidator);

    public void putTypedValue(DocumentParamsStateValidator documentParamsStateValidator);

    public void putConfig(DocumentParamsStateValidator documentParamsStateValidator);

}

interface DocumentBaseImpl extends DocumentBase, DocumentBaseApi, DocumentGetAndSetOps, DocumentExistanceAndEqualityOps, StoreSetOps, StoreGetOps {

    default boolean containsValue(DocumentParamsStateValidator documentParamsStateValidator) {
        final String field = documentParamsStateValidator.getValidField(this);

        return containsValue(field);
    }

    default boolean containsField(DocumentParamsStateValidator documentParamsStateValidator) {
        final String field = documentParamsStateValidator.getField(this);

        return containsField(field);
    }

    default TypedValue fetchTypedValueSure(DocumentParamsStateValidator documentParamsStateValidator) {
        final String field = documentParamsStateValidator.getValidField(this);

        return fetchTypedValueSure(field);
    }

    default Optional<TypedValue> fetchTypedValue(DocumentParamsStateValidator documentParamsStateValidator) {
        final String field = documentParamsStateValidator.getValidField(this);

        return fetchTypedValue(field);
    }

    default DataTypeConfig fetchConfigSure(DocumentParamsStateValidator documentParamsStateValidator) {
        final String field = documentParamsStateValidator.getValidField(this);

        return fetchConfigSure(field);
    }

    default void put(DocumentParamsStateValidator documentParamsStateValidator) {
        final String field = documentParamsStateValidator.getValidField(this);
        final Object value = documentParamsStateValidator.getValue(this);

        putTypedValue(field, new TypedValue(fetchConfigSure(field), value));
    }

    default void putTypedValue(DocumentParamsStateValidator documentParamsStateValidator) {
        final String field = documentParamsStateValidator.getValidField(this);
        final TypedValue value = documentParamsStateValidator.getValidTypedValue(this);

        putTypedValue(field, value);
    }

    default void putConfig(DocumentParamsStateValidator documentParamsStateValidator) {
        final String field = documentParamsStateValidator.getNewField(this);
        final DataTypeConfig dataTypeConfig = documentParamsStateValidator.getDataTypeConfig(this);

        putConfig(field, dataTypeConfig);
    }

}

interface DocumentBase {

    public boolean containsField(String field);

    public boolean containsValue(String field);

    public Optional<TypedValue> fetchTypedValue(String field);

    public static class ValueDoesNotExistException extends RuntimeException {
    }

    public TypedValue fetchTypedValueSure(String field) throws ValueDoesNotExistException;

    public Optional<DataTypeConfig> fetchConfig(String field);

    public static class ConfigDoesNotExistException extends RuntimeException {
    }

    public DataTypeConfig fetchConfigSure(String field) throws ConfigDoesNotExistException;

    public void putTypedValue(String field, TypedValue typedValue);

    public void putConfig(String field, DataTypeConfig dataTypeConfig);

}
