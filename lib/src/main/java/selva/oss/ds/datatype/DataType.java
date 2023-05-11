package selva.oss.ds.datatype;

import static selva.oss.lang.operation.DataOrientedOps.ProduceOperation;
import static selva.oss.lang.operation.DataOrientedOps.UnexpectedType;
import static selva.oss.lang.operation.DataOrientedOps.catchUnexpectedType;
import static selva.oss.lang.operation.DataOrientedOps.ReduceOperation;
import static selva.oss.lang.operation.DataOrientedOps.produceByAll;
import static selva.oss.lang.operation.DataOrientedOps.ApplyOperation;
import static selva.oss.lang.Commons.*;

import java.util.*;
import java.util.stream.Stream;

public enum DataType {

    String(String.class), Long(Long.class), Integer(Integer.class), Float(Float.class), Double(Double.class), Boolean(Boolean.class), Enum(Enum.class), List(List.class);

    private Class dataTypeClass;

    DataType(Class dataTypeClass) {
        this.dataTypeClass = dataTypeClass;
    }

    public Class getDataTypeClass() {
        return this.dataTypeClass;
    }

    // Logic

    public static boolean isStringDataType(DataType dataType) {
        return Arrays.asList(DataType.String).contains(dataType);
    }

    public static boolean isNumericalDataType(DataType dataType) {
        return Arrays.asList(DataType.Long, DataType.Integer, DataType.Float, DataType.Double).contains(dataType);
    }

    public static boolean canDataTypeClassOverride(DataType dataType, Class dataTypeClass) {
        return dataType.getDataTypeClass().isAssignableFrom(dataTypeClass);
    }

    public static boolean mustOverrideDataTypeClass(DataType dataType) {
        return Arrays.asList(DataType.Enum).contains(dataType);
    }

    public static boolean isSimpleDataType(DataType dataType) {
        return Arrays.asList(DataType.String, DataType.Long, DataType.Integer, DataType.Float, DataType.Double, DataType.Boolean, DataType.Enum).contains(dataType);
    }

    public static boolean isCollectionDataType(DataType dataType) {
        return Arrays.asList(DataType.List).contains(dataType);
    }

    public static <T> T produceByDataTypeType(DataType dataType, ProduceOperation<T> produceOperationForSimpleDataType, ProduceOperation<T> produceOperationForCollectionDataType) {
        if (isSimpleDataType(dataType)) {
            return produceOperationForSimpleDataType.produce();
        }
        else if (isCollectionDataType(dataType)) {
            return produceOperationForCollectionDataType.produce();
        }
        else {
            throw new UnexpectedType();
        }
    }

    public static <T> T produceBySimpleDataTypeType(DataType dataType,
            ProduceOperation<T> produceOperationForSimpleDataTypeString,
            ProduceOperation<T> produceOperationForSimpleDataTypeLong,
            ProduceOperation<T> produceOperationForSimpleDataTypeInteger,
            ProduceOperation<T> produceOperationForSimpleDataTypeFloat,
            ProduceOperation<T> produceOperationForSimpleDataTypeDouble,
            ProduceOperation<T> produceOperationForSimpleDataTypeBoolean,
            ProduceOperation<T> produceOperationForSimpleDataTypeEnum) {
        return switch (dataType) {
            case String -> produceOperationForSimpleDataTypeString.produce();
            case Long -> produceOperationForSimpleDataTypeLong.produce();
            case Integer -> produceOperationForSimpleDataTypeInteger.produce();
            case Float -> produceOperationForSimpleDataTypeFloat.produce();
            case Double -> produceOperationForSimpleDataTypeDouble.produce();
            case Boolean -> produceOperationForSimpleDataTypeBoolean.produce();
            case Enum -> produceOperationForSimpleDataTypeEnum.produce();
            default -> throw new UnexpectedType();
        };
    }

    public static <T> T produceByCollectionDataTypeType(DataType dataType, ProduceOperation<T> produceOperationForCollectionDataTypeList) {
        return switch (dataType) {
            case List -> produceOperationForCollectionDataTypeList.produce();
            default -> throw new UnexpectedType();
        };
    }

    public static <T> T produceByAnyDataTypeType(
            ProduceOperation<Optional<T>> produceOperationForSimpleDataType,
            ProduceOperation<Optional<T>> produceOperationForCollectionDataType) {
        return catchUnexpectedType(produceOperationForSimpleDataType)
            .orElseGet(() -> catchUnexpectedType(produceOperationForCollectionDataType)
                    .orElseThrow(() -> new UnexpectedType()));
    }

    public static <T> T produceByAnySimpleDataTypeType(
            ProduceOperation<Optional<T>> produceOperationForSimpleDataTypeString,
            ProduceOperation<Optional<T>> produceOperationForSimpleDataTypeLong,
            ProduceOperation<Optional<T>> produceOperationForSimpleDataTypeInteger,
            ProduceOperation<Optional<T>> produceOperationForSimpleDataTypeFloat,
            ProduceOperation<Optional<T>> produceOperationForSimpleDataTypeDouble,
            ProduceOperation<Optional<T>> produceOperationForSimpleDataTypeBoolean,
            ProduceOperation<Optional<T>> produceOperationForSimpleDataTypeEnum) {
        return produceOperationForSimpleDataTypeString.produce()
            .orElseGet(() -> produceOperationForSimpleDataTypeLong.produce()
                    .orElseGet(() -> produceOperationForSimpleDataTypeInteger.produce()
                        .orElseGet(() -> produceOperationForSimpleDataTypeFloat.produce()
                            .orElseGet(() -> produceOperationForSimpleDataTypeDouble.produce()
                                .orElseGet(() -> produceOperationForSimpleDataTypeBoolean.produce()
                                    .orElseGet(() -> produceOperationForSimpleDataTypeEnum.produce()
                                        .orElseThrow(() -> new UnexpectedType())))))));
    }

    public static <T> T produceByAnyCollectionDataTypeType(
            ProduceOperation<Optional<T>> produceOperationForCollectionDataTypeList) {
        return produceOperationForCollectionDataTypeList.produce()
            .orElseThrow(() -> new UnexpectedType());
    }

    public static <T> T produceByAllDataTypeType(ReduceOperation<T> reduceOperation,
            ProduceOperation<T> produceOperationForSimpleDataType,
            ProduceOperation<T> produceOperationForCollectionDataType) {
        return produceByAll(reduceOperation, produceOperationForSimpleDataType, produceOperationForCollectionDataType);
    }

    public static void applyByDataTypeType(DataType dataType, ApplyOperation applyOperationForSimpleDataType, ApplyOperation applyOperationForCollectionDataType) {
        produceByDataTypeType(dataType,
                () -> { applyOperationForSimpleDataType.apply(); return new Nothing(); },
                () -> { applyOperationForCollectionDataType.apply(); return new Nothing(); });
    }

    public static class EnumParams {
        public Class dataTypeClass;

        public EnumParams(Class dataTypeClass) {
            this.dataTypeClass = dataTypeClass;
        }
    }

    public static class CollectionParams {
        public DataTypeConfig elementDataTypeConfig;

        public CollectionParams(DataTypeConfig elementDataTypeConfig) {
            this.elementDataTypeConfig = elementDataTypeConfig;
        }
    }

    public static DataTypeConfig inferDataTypeConfigForSimpleDataType(
            ProduceOperation<Optional<Nothing>> produceOperationForSimpleDataTypeString,
            ProduceOperation<Optional<Nothing>> produceOperationForSimpleDataTypeLong,
            ProduceOperation<Optional<Nothing>> produceOperationForSimpleDataTypeInteger,
            ProduceOperation<Optional<Nothing>> produceOperationForSimpleDataTypeFloat,
            ProduceOperation<Optional<Nothing>> produceOperationForSimpleDataTypeDouble,
            ProduceOperation<Optional<Nothing>> produceOperationForSimpleDataTypeBoolean,
            ProduceOperation<Optional<EnumParams>> produceOperationForSimpleDataTypeEnum) {
        return produceByAnySimpleDataTypeType(
                () -> produceOperationForSimpleDataTypeString.produce().flatMap(d -> Optional.of(DataTypeConfig.createString())),
                () -> produceOperationForSimpleDataTypeLong.produce().flatMap(d -> Optional.of(DataTypeConfig.createLong())),
                () -> produceOperationForSimpleDataTypeInteger.produce().flatMap(d -> Optional.of(DataTypeConfig.createInteger())),
                () -> produceOperationForSimpleDataTypeFloat.produce().flatMap(d -> Optional.of(DataTypeConfig.createFloat())),
                () -> produceOperationForSimpleDataTypeDouble.produce().flatMap(d -> Optional.of(DataTypeConfig.createDouble())),
                () -> produceOperationForSimpleDataTypeBoolean.produce().flatMap(d -> Optional.of(DataTypeConfig.createBoolean())),
                () -> produceOperationForSimpleDataTypeEnum.produce().flatMap(enumParams -> Optional.of(DataTypeConfig.createEnum(enumParams.dataTypeClass))));
    }

    public static DataTypeConfig inferDataTypeConfigForCollectionDataType(
            ProduceOperation<Optional<CollectionParams>> produceOperationForCollectionDataTypeList) {
        return produceByAnyCollectionDataTypeType(
                () -> produceOperationForCollectionDataTypeList.produce().flatMap(collectionParams -> Optional.of(DataTypeConfig.createList(collectionParams.elementDataTypeConfig))));
    }

}
