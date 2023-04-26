package selva.oss.ds.document;

import static selva.oss.lang.CommonValidation.*;
import selva.oss.lang.reflection.ReflectiveData;
import selva.oss.ds.document.datatype.DataTypeConfig;
import selva.oss.ds.document.datatype.TypedValue;
import selva.oss.ds.document.datatype.TypedValueLogic;

import java.util.*;

/* The Values and Configs are never expected to be null and they are always expected to be valid
 */

class PojoDocumentImpl implements DocumentBaseImpl {

    private ReflectiveData reflectiveData;

    public PojoDocumentImpl(Object object) {
        validateNotNull(object);

        this.reflectiveData = new ReflectiveData(object);
    }

    public boolean containsField(String field) {
        return this.reflectiveData.containsField(field);
    }

    public boolean containsValue(String field) {
        return this.reflectiveData.get(field) != null;
    }

    public Optional<TypedValue> fetchTypedValue(String field) {
        if (!containsValue(field)) {
            return Optional.empty();
        }
        return Optional.of(new TypedValue(fetchConfigSure(field), this.reflectiveData.get(field)));
    }

    public static class ValueDoesNotExistException extends RuntimeException {
    }

    public TypedValue fetchTypedValueSure(String field) {
        if (!containsValue(field)) {
            throw new ValueDoesNotExistException();
        }
        return new TypedValue(fetchConfigSure(field), this.reflectiveData.get(field));
    }

    public Optional<DataTypeConfig> fetchConfig(String field) {
        if (!containsField(field)) {
            return Optional.empty();
        }
        return Optional.of(TypedValueLogic.inferDataTypeStatic(this.reflectiveData.getDeclaredField(field)));
    }

    public static class ConfigDoesNotExistException extends RuntimeException {
    }

    public DataTypeConfig fetchConfigSure(String field) {
        if (!containsField(field)) {
            throw new ConfigDoesNotExistException();
        }
        return TypedValueLogic.inferDataTypeStatic(this.reflectiveData.getDeclaredField(field));
    }

    public void putTypedValue(String field, TypedValue typedValue) {
        this.reflectiveData.set(field, typedValue.copyValue());
    }

    public static class CannotDefineFieldException extends RuntimeException {
    }

    public void putConfig(String field, DataTypeConfig dataTypeConfig) {
        throw new CannotDefineFieldException();
    }

}
