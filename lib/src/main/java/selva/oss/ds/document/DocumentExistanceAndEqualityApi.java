package selva.oss.ds.document;

import static selva.oss.lang.CommonValidation.*;
import selva.oss.ds.document.datatype.DataTypeConfig;
import selva.oss.ds.document.datatype.TypedValue;

import java.util.*;

interface DocumentExistanceAndEqualityApi<T> {

    public boolean isPresent(T field);

    default boolean isEqual(DocumentStore<T> inData, T field) {
        throw new HasNotBeenImplementedException();
    }

    default boolean diffAndCheckIfEdited(DocumentStore<T> inData, T field) {
        throw new HasNotBeenImplementedException();
    }

}

interface DocumentExistanceAndEqualityOps extends DocumentBaseApi, DocumentStore<String> {

    default boolean isPresent(String field) {
        return containsValue(DocumentParamsStateValidator.createWithValidField(field));
    }

    default boolean isEqual(DocumentStore<String> inData, String field) {
        validateNotNull(inData);
        validateNotNull(field);

        if (!isPresent(field) || !inData.isPresent(field)) {
            return false;
        }
        return getTypedValue(field).equals(inData.getTypedValue(field));
    }

    default boolean diffAndCheckIfEdited(DocumentStore<String> inData, String field) {
        validateNotNull(inData);
        validateNotNull(field);

        return ((!isPresent(field) && inData.isPresent(field)) || (isPresent(field) && inData.isPresent(field) && !getTypedValue(field).equals(inData.getTypedValue(field))));
    }

}