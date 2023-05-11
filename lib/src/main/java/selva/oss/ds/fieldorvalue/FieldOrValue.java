package selva.oss.ds.fieldorvalue;

import static selva.oss.lang.Commons.*;

import selva.oss.ds.value.TypedValue;
import selva.oss.ds.field.FieldConfig;
import selva.oss.ds.datatype.DataTypeConfig;

enum Type {
    Field, Value;
}

public class FieldOrValue {

    private FieldConfig field;
    private TypedValue value;

    public FieldOrValue(FieldConfig field) {
        validateNotNull(field);

        this.field = field;
    }

    public FieldOrValue(TypedValue value) {
        validateNotNull(value);

        this.value = value;
    }

    public static class TypeExpectedToBeEitherFieldOrValueException extends RuntimeException {
    }

    public Type getType() {
        if (field != null) {
            return Type.Field;
        }
        if (value != null) {
            return Type.Value;
        }
        throw new TypeExpectedToBeEitherFieldOrValueException();
    }

    public DataTypeConfig getDataTypeConfig() {
        if (field != null) {
            return field.getDataTypeConfig();
        }
        if (value != null) {
            return value.getDataTypeConfig();
        }
        throw new TypeExpectedToBeEitherFieldOrValueException();
    }

    public String getAsString() {
        if (field != null) {
            return field.getFieldAsString();
        }
        if (value != null) {
            return value.getValueAsString();
        }
        throw new TypeExpectedToBeEitherFieldOrValueException();
    }

}
