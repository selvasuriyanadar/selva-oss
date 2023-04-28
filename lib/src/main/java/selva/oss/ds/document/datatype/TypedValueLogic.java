package selva.oss.ds.document.datatype;

import static selva.oss.lang.CommonValidation.*;
import selva.oss.lang.Collections;
import static selva.oss.ds.document.datatype.DataType.EnumParams;
import static selva.oss.ds.document.datatype.DataType.CollectionParams;

import java.util.*;
import java.util.stream.Stream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;

public class TypedValueLogic {

    public static void validateValue(DataTypeConfig dataTypeConfig, Object value) {
        DataType.applyByDataTypeType(dataTypeConfig.getDataType(),
                () -> validateType(dataTypeConfig.getDataTypeClass(), value),
                () -> {
                    validateType(dataTypeConfig.getDataTypeClass(), value);
                    for (Object colElement : (Collection) value) {
                        validateNotNull(colElement);
                        validateType(dataTypeConfig.getElementDataTypeConfig().getDataTypeClass(), colElement);
                    }
                });
    }

    public static boolean isEqual(DataTypeConfig dataTypeConfig, Object value1, Object value2) {
        return DataType.produceByDataTypeType(dataTypeConfig.getDataType(),
                () -> { return value1.equals(value2); },
                () -> { return Collections.equals((Collection) value1, (Collection) value2); });
    }

    public static Stream<Object> getSimplifiedDataView(DataTypeConfig dataTypeConfig, Object value) {
        return DataType.produceByDataTypeType(dataTypeConfig.getDataType(),
                () -> { return Stream.of(value); },
                () -> { return ((Collection) value).stream(); });
    }

    public static Object copy(DataTypeConfig dataTypeConfig, Object value) {
        return DataType.produceByDataTypeType(dataTypeConfig.getDataType(),
                () -> { return value; },
                () -> {
                    return DataType.produceByCollectionDataTypeType(dataTypeConfig.getDataType(),
                            () -> { return new ArrayList((List) value); });
                });
    }

    public static class DynamicValueDoesNotExist extends RuntimeException {
    }

    public static void validateValueExistDynamic(Object value) {
        if (!doesValueExistDynamic(value)) {
            throw new DynamicValueDoesNotExist();
        }
    }

    public static Boolean doesValueExistDynamic(Object value) {
        if (value == null) {
            return false;
        }

        return DataType.produceByAllDataTypeType((finalResult, thisResult) -> (finalResult && thisResult),
                () -> (true),
                () -> (value instanceof Collection) ? (!((Collection) value).isEmpty()) : true);
    }

    private static DataTypeConfig inferSimpleDataTypeByClass(Class typeClass) {
        return DataType.inferDataTypeConfigForSimpleDataType(
                () -> Nothing.of(DataType.String.getDataTypeClass().equals(typeClass)),
                () -> Nothing.of(DataType.Long.getDataTypeClass().equals(typeClass)),
                () -> Nothing.of(DataType.Integer.getDataTypeClass().equals(typeClass)),
                () -> Nothing.of(DataType.Float.getDataTypeClass().equals(typeClass)),
                () -> Nothing.of(DataType.Double.getDataTypeClass().equals(typeClass)),
                () -> Nothing.of(DataType.Boolean.getDataTypeClass().equals(typeClass)),
                () -> DataType.canDataTypeClassOverride(DataType.Enum, typeClass) ? Optional.of(new EnumParams(typeClass)) : Optional.empty());
    }

    public static class TypeClassNotFoundException extends RuntimeException {
    }

    private static DataTypeConfig inferSimpleDataTypeByType(Type type) {
        return DataType.inferDataTypeConfigForSimpleDataType(
                () -> Nothing.of(DataType.String.getDataTypeClass().getName().equals(type.getTypeName())),
                () -> Nothing.of(DataType.Long.getDataTypeClass().getName().equals(type.getTypeName())),
                () -> Nothing.of(DataType.Integer.getDataTypeClass().getName().equals(type.getTypeName())),
                () -> Nothing.of(DataType.Float.getDataTypeClass().getName().equals(type.getTypeName())),
                () -> Nothing.of(DataType.Double.getDataTypeClass().getName().equals(type.getTypeName())),
                () -> Nothing.of(DataType.Boolean.getDataTypeClass().getName().equals(type.getTypeName())),
                () -> {
                    try {
                        return DataType.canDataTypeClassOverride(DataType.Enum, Class.forName(type.getTypeName())) ? Optional.of(new EnumParams(Class.forName(type.getTypeName()))) : Optional.empty();
                    }
                    catch (ClassNotFoundException e) {
                        throw new TypeClassNotFoundException();
                    }
                });
    }

    private static DataTypeConfig inferSimpleDataTypeStatic(Field field) {
        return inferSimpleDataTypeByClass(field.getType());
    }

    private static DataTypeConfig inferCollectionDataTypeStatic(Field field) {
        return DataType.inferDataTypeConfigForCollectionDataType(
                () -> DataType.canDataTypeClassOverride(DataType.List, field.getType()) ? Optional.of(new CollectionParams(inferSimpleDataTypeByType(((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]))) : Optional.empty());
    }

    public static DataTypeConfig inferDataTypeStatic(Field field) {
        return DataType.produceByAnyDataTypeType(
                () -> Optional.of(inferSimpleDataTypeStatic(field)),
                () -> Optional.of(inferCollectionDataTypeStatic(field)));
    }

    private static DataTypeConfig inferSimpleDataTypeDynamic(Object value) {
        return inferSimpleDataTypeByClass(value.getClass());
    }

    private static DataTypeConfig inferCollectionDataTypeDynamic(Object value) {
        return DataType.inferDataTypeConfigForCollectionDataType(
                () -> DataType.canDataTypeClassOverride(DataType.List, value.getClass()) ? Optional.of(new CollectionParams(inferSimpleDataTypeDynamic(((List) value).get(0)))) : Optional.empty());
    }

    public static DataTypeConfig inferDataTypeDynamic(Object value) {
        return DataType.produceByAnyDataTypeType(
                () -> Optional.of(inferSimpleDataTypeDynamic(value)),
                () -> Optional.of(inferCollectionDataTypeDynamic(value)));
    }

}
