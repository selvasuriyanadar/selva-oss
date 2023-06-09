package selva.oss.ds.value;

import static selva.oss.lang.Commons.*;
import selva.oss.ds.datatype.DataType;
import selva.oss.ds.datatype.DataTypeConfig;

import com.google.gson.JsonElement;

import java.util.*;
import java.util.stream.Stream;

public class TypedValue implements Equable<TypedValue>, Hashable {

    private Object value;
    private DataTypeConfig dataTypeConfig;

    public TypedValue(DataTypeConfig dataTypeConfig, JsonElement value) {
        validateNotNull(dataTypeConfig);
        validateNotNull(value);

        this.dataTypeConfig = dataTypeConfig;
        this.value = TypedValueConvertFromGsonElementLogic.convert(dataTypeConfig, value);
    }

    public TypedValue(Object value) {
        TypedValueLogic.validateValueExistDynamic(value);

        this.dataTypeConfig = TypedValueLogic.inferDataTypeDynamic(value);
        this.value = value;
    }

    public TypedValue(DataTypeConfig dataTypeConfig, Object value) {
        validateNotNull(dataTypeConfig);
        validateNotNull(value);
        TypedValueLogic.validateValue(dataTypeConfig, value);

        this.dataTypeConfig = dataTypeConfig;
        this.value = value;
    }

    public DataTypeConfig getDataTypeConfig() {
        return this.dataTypeConfig;
    }

    private Object getValue() {
        return this.value;
    }

    public Object copyValue() {
        return TypedValueLogic.copy(getDataTypeConfig(), getValue());
    }

    public static class TypeDoesNotMatchException extends RuntimeException {
    }

    public void validateTypeMatches(TypedValue typedValue) {
        validateNotNull(typedValue);

        validateTypeMatches(typedValue.getDataTypeConfig());
    }

    public void validateTypeMatches(DataTypeConfig dataTypeConfig) {
        if (!doesTypeMatch(dataTypeConfig)) {
            throw new TypeDoesNotMatchException();
        }
    }

    public boolean doesTypeMatch(DataTypeConfig dataTypeConfig) {
        validateNotNull(dataTypeConfig);

        return getDataTypeConfig().equals(dataTypeConfig);
    }

    public String getValueAsString() {
        return TypedValueToStringLogic.toString(getDataTypeConfig(), getValue());
    }

    @Override
    public boolean isEqual(TypedValue typedValue) {
        validateNotNull(typedValue);

        return doesTypeMatch(typedValue.getDataTypeConfig())
            && TypedValueLogic.isEqual(getDataTypeConfig(), getValue(), typedValue.getValue());
    }

    @Override
    public Stream<Object> getSimplifiedDataView() {
        return Stream.concat(TypedValueLogic.getSimplifiedDataView(getDataTypeConfig(), getValue()), getDataTypeConfig().getSimplifiedDataView());
    }

    @Override
    public int hashCode() {
        return produceHash(this);
    }

    @Override
    public boolean equals(Object value) {
        return checkIfEqual(TypedValue.class, this, Optional.ofNullable(value));
    }

}
