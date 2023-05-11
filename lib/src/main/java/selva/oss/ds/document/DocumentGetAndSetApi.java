package selva.oss.ds.document;

import static selva.oss.lang.Commons.*;
import selva.oss.ds.datatype.DataTypeConfig;
import selva.oss.ds.value.TypedValue;

import java.util.*;

interface DocumentGetAndSetApi<T> {

    public Optional<Object> getOptional(T field);

    public Object getSure(T field);

    public void setValue(T field, Object value);

    public void setNullable(T field, Object value);

}

interface DocumentGetAndSetOps extends DocumentBaseApi, DocumentStore<String> {

    default Optional<Object> getOptional(String field) {
        return fetchTypedValue(DocumentParamsStateValidator.createWithValidField(field)).flatMap(d -> Optional.of(d.copyValue()));
    }

    default Object getSure(String field) {
        return fetchTypedValueSure(DocumentParamsStateValidator.createWithValidField(field)).copyValue();
    }

    default void setValue(String field, Object value) {
        put(DocumentParamsStateValidator.createWithValidFieldAndValue(field, value));
    }

    default void setNullable(String field, Object value) {
        if (value == null) {
            return;
        }
        setValue(field, value);
    }

}
