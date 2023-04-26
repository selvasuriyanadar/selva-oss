package selva.oss.ds.document.datatype;

import static selva.oss.lang.CommonValidation.*;

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

    public static class UnexpectedDataTypeType extends RuntimeException {
    }

    public interface ApplyOperation {
        public void apply();
    }

    public static void applyByDataTypeType(DataType dataType, ApplyOperation applyOperationForSimpleDataType, ApplyOperation applyOperationForCollectionDataType) {
        produceByDataTypeType(dataType,
                () -> { applyOperationForSimpleDataType.apply(); return new Nothing(); },
                () -> { applyOperationForCollectionDataType.apply(); return new Nothing(); });
    }

    public interface ProduceOperation<T> {
        public T produce();
    }

    public static <T> T produceByDataTypeType(DataType dataType, ProduceOperation<T> produceOperationForSimpleDataType, ProduceOperation<T> produceOperationForCollectionDataType) {
        if (isSimpleDataType(dataType)) {
            return produceOperationForSimpleDataType.produce();
        }
        else if (isCollectionDataType(dataType)) {
            return produceOperationForCollectionDataType.produce();
        }
        else {
            throw new UnexpectedDataTypeType();
        }
    }

    public static <T> T produceByCollectionDataTypeType(DataType dataType, ProduceOperation<T> produceOperationForCollectionDataTypeList) {
        if (dataType == DataType.List) {
            return produceOperationForCollectionDataTypeList.produce();
        }
        else {
            throw new UnexpectedDataTypeType();
        }
    }

    public static <T> Optional<T> catchUnexpectedDataTypeType(ProduceOperation<Optional<T>> produceOperation) {
        try {
            return produceOperation.produce();
        }
        catch (UnexpectedDataTypeType e) {
            return Optional.empty();
        }
    }

    public static <T> T produceByAnyDataTypeType(
            ProduceOperation<Optional<T>> produceOperationForSimpleDataType,
            ProduceOperation<Optional<T>> produceOperationForCollectionDataType) {
        return catchUnexpectedDataTypeType(produceOperationForSimpleDataType)
            .orElseGet(() -> catchUnexpectedDataTypeType(produceOperationForCollectionDataType)
                    .orElseThrow(() -> new UnexpectedDataTypeType()));
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
                                        .orElseThrow(() -> new UnexpectedDataTypeType())))))));
    }

    public static <T> T produceByAnyCollectionDataTypeType(
            ProduceOperation<Optional<T>> produceOperationForCollectionDataTypeList) {
        return produceOperationForCollectionDataTypeList.produce()
            .orElseThrow(() -> new UnexpectedDataTypeType());
    }

    public interface ReduceOperation<T> {
        public T reduce(T finalResult, T thisResult);
    }

    public static <T> T produceByAllDataTypeType(ReduceOperation<T> reduceOperation,
            ProduceOperation<T> produceOperationForSimpleDataType,
            ProduceOperation<T> produceOperationForCollectionDataType) {
        return reduceOperation.reduce(produceOperationForSimpleDataType.produce(),
                produceOperationForCollectionDataType.produce());
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
