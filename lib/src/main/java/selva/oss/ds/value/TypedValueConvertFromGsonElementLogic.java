package selva.oss.ds.value;

import static selva.oss.lang.Commons.*;
import static selva.oss.ds.datatype.DataType.EnumParams;
import static selva.oss.ds.datatype.DataType.CollectionParams;

import selva.oss.lang.Collections;
import selva.oss.lang.GsonUtils;
import selva.oss.ds.datatype.DataType;
import selva.oss.ds.datatype.DataTypeConfig;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class TypedValueConvertFromGsonElementLogic {

    public static Object convert(DataTypeConfig dataTypeConfig, JsonElement value) {
        return DataType.produceByDataTypeType(dataTypeConfig.getDataType(),
                () -> convertBySimpleDataType(dataTypeConfig, value),
                () -> convertByCollectionDataType(dataTypeConfig, value));
    }

    private static Object convertByCollectionDataType(DataTypeConfig dataTypeConfig, JsonElement value) {
        if (!value.isJsonArray()) {
            throw new GsonUtils.JsonArrayExpectedException();
        }

        return DataType.produceByCollectionDataTypeType(dataTypeConfig.getDataType(),
                () -> Collections.streamIterator(value.getAsJsonArray().iterator()).map(v -> convertBySimpleDataType(dataTypeConfig.getElementDataTypeConfig(), v)).collect(Collectors.toList()));
    }

    public static class CouldNotParseValueException extends RuntimeException {
    }

    private static Object convertBySimpleDataType(DataTypeConfig dataTypeConfig, JsonElement value) {
        try {
            return DataType.produceBySimpleDataTypeType(dataTypeConfig.getDataType(),
                    () -> value.getAsString(),
                    () -> value.getAsLong(),
                    () -> value.getAsInt(),
                    () -> value.getAsFloat(),
                    () -> value.getAsDouble(),
                    () -> value.getAsBoolean(),
                    () -> Enum.valueOf(dataTypeConfig.getDataTypeClass(), value.getAsString()));
        }
        catch (ClassCastException | IllegalStateException | NullPointerException | IllegalArgumentException e) {
            throw new CouldNotParseValueException();
        }
    }

}
