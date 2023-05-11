package selva.oss.ds.datatype;

import static selva.oss.lang.Commons.*;

import java.util.*;
import java.util.stream.Stream;

public class DataTypeConfigLogic {

    public static class NotANumericalDataTypeException extends RuntimeException {
    }

    public static void validateIsNumericalType(DataTypeConfig dataTypeConfig) {
        if (!DataType.isNumericalDataType(dataTypeConfig.getDataType())) {
            throw new NotANumericalDataTypeException();
        }
    }

    public static class NotASimpleDataTypeException extends RuntimeException {
    }

    public static void validateSimpleDataType(DataType dataType) {
        if (!DataType.isSimpleDataType(dataType)) {
            throw new NotASimpleDataTypeException();
        }
    }

    public static class NotAMustOverrideSimpleDataTypeException extends RuntimeException {
    }

    public static void validateMustOverrideSimpleDataType(DataType dataType) {
        validateSimpleDataType(dataType);
        if (!DataType.mustOverrideDataTypeClass(dataType)) {
            throw new NotAMustOverrideSimpleDataTypeException();
        }
    }

    public static class DataTypeClassCannotOverrideException extends RuntimeException {
    }

    public static void validateCanDataTypeClassOverride(DataType dataType, Class dataTypeClass) {
        if (!DataType.canDataTypeClassOverride(dataType, dataTypeClass)) {
            throw new DataTypeClassCannotOverrideException();
        }
    }

    public static class NotACollectionDataTypeException extends RuntimeException {
    }

    public static void validateCollectionDataType(DataType dataType) {
        if (!DataType.isCollectionDataType(dataType)) {
            throw new NotACollectionDataTypeException();
        }
    }

    public static class InvalidElementDataTypeConfigException extends RuntimeException {
    }

    public static void validateElementDataTypeConfig(DataTypeConfig elementDataTypeConfig) {
        if (!DataType.isSimpleDataType(elementDataTypeConfig.getDataType())) {
            throw new InvalidElementDataTypeConfigException();
        }
    }

}
