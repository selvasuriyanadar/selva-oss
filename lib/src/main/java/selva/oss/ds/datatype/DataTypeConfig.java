package selva.oss.ds.datatype;

import static selva.oss.lang.Commons.*;
import selva.oss.ds.datatype.DataTypeConfigLogic;

import java.util.*;
import java.util.stream.Stream;

public interface DataTypeConfig extends Equable<DataTypeConfig>, Hashable {

    public static class DataTypeNotConfiguredException extends RuntimeException {
    }

    default DataType getDataType() {
        throw new DataTypeNotConfiguredException();
    }

    public static class DataTypeClassNotConfiguredException extends RuntimeException {
    }

    default Class getDataTypeClass() {
        throw new DataTypeClassNotConfiguredException();
    }

    public static class ElementDataTypeNotConfiguredException extends RuntimeException {
    }

    default DataTypeConfig getElementDataTypeConfig() {
        throw new ElementDataTypeNotConfiguredException();
    }

    public static DataTypeConfig createString() {
        return new SimpleDataTypeConfig(DataType.String);
    }

    public static DataTypeConfig createLong() {
        return new SimpleDataTypeConfig(DataType.Long);
    }

    public static DataTypeConfig createInteger() {
        return new SimpleDataTypeConfig(DataType.Integer);
    }

    public static DataTypeConfig createFloat() {
        return new SimpleDataTypeConfig(DataType.Float);
    }

    public static DataTypeConfig createDouble() {
        return new SimpleDataTypeConfig(DataType.Double);
    }

    public static DataTypeConfig createBoolean() {
        return new SimpleDataTypeConfig(DataType.Boolean);
    }

    public static DataTypeConfig createEnum(Class dataTypeClass) {
        return new MustOverrideSimpleDataTypeConfig(DataType.Enum, dataTypeClass);
    }

    public static DataTypeConfig createList(DataTypeConfig elementDataTypeConfig) {
        return new CollectionDataTypeConfig(DataType.List, elementDataTypeConfig);
    }

}

class SimpleDataTypeConfig implements DataTypeConfig {

    private DataType dataType;

    public SimpleDataTypeConfig(DataType dataType) {
        validateNotNull(dataType);
        DataTypeConfigLogic.validateSimpleDataType(dataType);

        this.dataType = dataType;
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public Class getDataTypeClass() {
        return this.dataType.getDataTypeClass();
    }

    @Override
    public boolean isEqual(DataTypeConfig dataTypeConfig) {
        if (!(dataTypeConfig instanceof SimpleDataTypeConfig)) {
            return false;
        }
        return getDataType().equals(dataTypeConfig.getDataType());
    }

    @Override
    public Stream<Object> getSimplifiedDataView() {
        return Stream.of(getDataType());
    }

    @Override
    public int hashCode() {
        return produceHash(this);
    }

    @Override
    public boolean equals(Object value) {
        return checkIfEqual(DataTypeConfig.class, this, Optional.ofNullable(value));
    }

}

class MustOverrideSimpleDataTypeConfig implements DataTypeConfig {

    private DataType dataType;
    private Class overridingDataTypeClass;

    public MustOverrideSimpleDataTypeConfig(DataType dataType, Class dataTypeClass) {
        validateNotNull(dataType);
        validateNotNull(dataTypeClass);
        DataTypeConfigLogic.validateMustOverrideSimpleDataType(dataType);
        DataTypeConfigLogic.validateCanDataTypeClassOverride(dataType, dataTypeClass);

        this.dataType = dataType;
        this.overridingDataTypeClass = dataTypeClass;
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public Class getDataTypeClass() {
        return this.overridingDataTypeClass;
    }

    @Override
    public boolean isEqual(DataTypeConfig dataTypeConfig) {
        if (!(dataTypeConfig instanceof MustOverrideSimpleDataTypeConfig)) {
            return false;
        }
        return getDataType().equals(dataTypeConfig.getDataType())
            && getDataTypeClass().equals(dataTypeConfig.getDataTypeClass());
    }

    @Override
    public Stream<Object> getSimplifiedDataView() {
        return Stream.of(getDataType(), getDataTypeClass());
    }

    @Override
    public int hashCode() {
        return produceHash(this);
    }

    @Override
    public boolean equals(Object value) {
        return checkIfEqual(DataTypeConfig.class, this, Optional.ofNullable(value));
    }

}

class CollectionDataTypeConfig implements DataTypeConfig {

    private DataType dataType;
    private DataTypeConfig elementDataTypeConfig;

    public CollectionDataTypeConfig(DataType dataType, DataTypeConfig elementDataTypeConfig) {
        validateNotNull(dataType);
        validateNotNull(elementDataTypeConfig);
        DataTypeConfigLogic.validateCollectionDataType(dataType);
        DataTypeConfigLogic.validateElementDataTypeConfig(elementDataTypeConfig);

        this.dataType = dataType;
        this.elementDataTypeConfig = elementDataTypeConfig;
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public Class getDataTypeClass() {
        return this.dataType.getDataTypeClass();
    }

    public DataTypeConfig getElementDataTypeConfig() {
        return this.elementDataTypeConfig;
    }

    @Override
    public boolean isEqual(DataTypeConfig dataTypeConfig) {
        if (!(dataTypeConfig instanceof CollectionDataTypeConfig)) {
            return false;
        }
        return getDataType().equals(dataTypeConfig.getDataType())
            && getElementDataTypeConfig().equals(dataTypeConfig.getElementDataTypeConfig());
    }

    @Override
    public Stream<Object> getSimplifiedDataView() {
        return Stream.concat(Stream.of(getDataType()), getElementDataTypeConfig().getSimplifiedDataView());
    }

    @Override
    public int hashCode() {
        return produceHash(this);
    }

    @Override
    public boolean equals(Object value) {
        return checkIfEqual(DataTypeConfig.class, this, Optional.ofNullable(value));
    }

}
