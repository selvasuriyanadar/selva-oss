package selva.oss.ds.document;

import static selva.oss.lang.CommonValidation.*;
import selva.oss.ds.document.datatype.DataTypeConfig;
import selva.oss.ds.document.datatype.TypedValue;

import java.util.*;

interface StoreSetApi<T> {

    public void defineFieldType(T field, DataTypeConfig dataTypeConfig);

    public void defineFieldType(FieldConfig fieldConfig);

    public void defineFieldType(T field, Store<T> inData);

    public void setTypedValueOptional(T field, Optional<TypedValue> typedValue);

    public void setTypedValueOptional(T field, Store<T> inData);

}

interface StoreSetOps extends DocumentBaseApi, DocumentStore<String> {

    default void defineFieldType(String field, DataTypeConfig dataTypeConfig) {
        putConfig(DocumentParamsStateValidator.createWithNewFieldAndDataTypeConfig(field, dataTypeConfig));
    }

    default void defineFieldType(FieldConfig fieldConfig) {
        validateNotNull(fieldConfig);

        defineFieldType(fieldConfig.getFieldAsString(), fieldConfig.getDataTypeConfig());
    }

    default void defineFieldType(String field, Store<String> inData) {
        validateNotNull(inData);

        defineFieldType(field, inData.getConfig(field));
    }

    default void setTypedValueOptional(String field, Optional<TypedValue> typedValue) {
        if (!typedValue.isPresent()) {
            return;
        }

        putTypedValue(DocumentParamsStateValidator.createWithValidFieldAndValidTypedValue(field, typedValue.get()));
    }

    default void setTypedValueOptional(String field, Store<String> inData) {
        validateNotNull(inData);

        setTypedValueOptional(field, inData.getTypedValueOptional(field));
    }

}
