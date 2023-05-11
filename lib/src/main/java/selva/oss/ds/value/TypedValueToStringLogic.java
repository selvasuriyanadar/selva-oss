package selva.oss.ds.value;

import static selva.oss.lang.Commons.*;
import static selva.oss.ds.datatype.DataType.EnumParams;
import static selva.oss.ds.datatype.DataType.CollectionParams;

import selva.oss.lang.Collections;
import selva.oss.ds.datatype.DataType;
import selva.oss.ds.datatype.DataTypeConfig;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class TypedValueToStringLogic {

    public static String toString(DataTypeConfig dataTypeConfig, Object value) {
        return DataType.produceByDataTypeType(dataTypeConfig.getDataType(),
                () -> toStringBySimpleDataType(dataTypeConfig, value),
                () -> toStringByCollectionDataType(dataTypeConfig, value));
    }

    private static String toStringByCollectionDataType(DataTypeConfig dataTypeConfig, Object value) {
        return DataType.produceByCollectionDataTypeType(dataTypeConfig.getDataType(),
                () -> { return "[" + String.join(", ", (List<String>) (((List) value).stream().map(v -> toStringBySimpleDataType(dataTypeConfig.getElementDataTypeConfig(), v)).collect(Collectors.toList()))) + "]"; });
    }

    private static String toStringBySimpleDataType(DataTypeConfig dataTypeConfig, Object value) {
        return DataType.produceBySimpleDataTypeType(dataTypeConfig.getDataType(),
                () -> "'" + value.toString() + "'",
                () -> value.toString(),
                () -> value.toString(),
                () -> value.toString(),
                () -> value.toString(),
                () -> "'" + value.toString() + "'",
                () -> "'" + value.toString() + "'");
    }

}
