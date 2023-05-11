package selva.oss.ds.field;

import static selva.oss.lang.Commons.*;
import selva.oss.ds.datatype.DataTypeConfig;
import selva.oss.ds.value.TypedValue;

import java.util.*;
import java.util.stream.Stream;

public class FieldConfig {

    private Field field;
    private DataTypeConfig dataTypeConfig;

    public FieldConfig(Field field, DataTypeConfig dataTypeConfig) {
        validateNotNull(field);
        validateNotNull(dataTypeConfig);

        this.field = field;
        this.dataTypeConfig = dataTypeConfig;
    }

    public Field getField() {
        return this.field;
    }

    public String getFieldAsString() {
        return this.field.getFieldAsString();
    }

    public DataTypeConfig getDataTypeConfig() {
        return this.dataTypeConfig;
    }

}
