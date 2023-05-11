package selva.oss.ds.field;

import static selva.oss.lang.Commons.*;
import selva.oss.ds.datatype.DataTypeConfig;
import selva.oss.ds.datatype.TypedValue;

import java.util.*;
import java.util.stream.Stream;

public class FieldConfig<T> {

    private T field;
    private DataTypeConfig dataTypeConfig;

    public FieldConfig(T field, DataTypeConfig dataTypeConfig) {
        validateNotNull(field);
        validateNotNull(dataTypeConfig);

        this.field = field;
        this.dataTypeConfig = dataTypeConfig;
    }

    public T getField() {
        return this.field;
    }

    public String getFieldAsString() {
        return this.field.toString();
    }

    public DataTypeConfig getDataTypeConfig() {
        return this.dataTypeConfig;
    }

}
