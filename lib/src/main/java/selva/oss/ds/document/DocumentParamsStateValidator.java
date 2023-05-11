package selva.oss.ds.document;

import static selva.oss.lang.Commons.*;
import selva.oss.ds.datatype.DataTypeConfig;
import selva.oss.ds.value.TypedValue;

import java.util.*;

class DocumentLogic {

    public static class InvalidDocumentFieldException extends RuntimeException {
    }

    public static void validateField(DocumentBase document, String field) {
        if (!document.containsField(field)) {
            throw new InvalidDocumentFieldException();
        }
    }

    public static class FieldVerificationFailedTypeDoesNotMatchException extends RuntimeException {
    }

    public static void verifyFieldTypeMatches(DocumentBase document, String field, DataTypeConfig dataTypeConfig) {
        if (!document.fetchConfigSure(field).equals(dataTypeConfig)) {
            throw new FieldVerificationFailedTypeDoesNotMatchException();
        }
    }

    public static void validateTypeMatches(DocumentBase document, String field, TypedValue value) {
        value.validateTypeMatches(document.fetchConfigSure(field));
    }

    public static class FieldAlreadyDefinedException extends RuntimeException {
    }

    public static void validateIfNewField(DocumentBase document, String field) {
        if (document.containsField(field)) {
            throw new FieldAlreadyDefinedException();
        }
    }

}

public class DocumentParamsStateValidator {

    public interface Validation<T> {
        public T validate(DocumentBase document);
    }

    public static class DataDoesNotExist extends RuntimeException {
    }

    public static <T> T validateNullableValidation(DocumentBase document, Validation<T> validation) {
        validateNotNull(document);
        if (validation == null) {
            throw new DataDoesNotExist();
        }
        return validation.validate(document);
    }

    private Validation<String> validFieldValidation;
    private Validation<String> newFieldValidation;
    private Validation<TypedValue> validTypedValueValidation;

    private Validation<String> fieldValidation;
    private Validation<DataTypeConfig> dataTypeConfigValidation;
    private Validation<Object> valueValidation;

    public static DocumentParamsStateValidator createWithValidField(String validField) {
        return new DocumentParamsStateValidator().setValidField(validField);
    }

    public static DocumentParamsStateValidator createWithField(String field) {
        return new DocumentParamsStateValidator().setField(field);
    }

    public static DocumentParamsStateValidator createWithNewFieldAndDataTypeConfig(String newField, DataTypeConfig dataTypeConfig) {
        return new DocumentParamsStateValidator().setNewField(newField).setDataTypeConfig(dataTypeConfig);
    }

    public static DocumentParamsStateValidator createWithValidFieldAndValidTypedValue(String validField, TypedValue validTypedValue) {
        return new DocumentParamsStateValidator().setValidField(validField).setValidTypedValue(validTypedValue);
    }

    public static DocumentParamsStateValidator createWithValidFieldAndDataTypeConfig(String validField, DataTypeConfig dataTypeConfig) {
        return new DocumentParamsStateValidator().setValidField(validField).setDataTypeConfig(dataTypeConfig);
    }

    public static DocumentParamsStateValidator createWithValidFieldAndValue(String validField, Object value) {
        return new DocumentParamsStateValidator().setValidField(validField).setValue(value);
    }

    private DocumentParamsStateValidator setValidField(String validField) {
        this.validFieldValidation = document -> {
            validateNotNull(validField);
            DocumentLogic.validateField(document, validField);
            return validField;
        };
        return this;
    }

    public String getValidField(DocumentBase document) {
        return validateNullableValidation(document, this.validFieldValidation);
    }

    private DocumentParamsStateValidator setNewField(String newField) {
        this.newFieldValidation = document -> {
            validateNotNull(newField);
            DocumentLogic.validateIfNewField(document, newField);
            return newField;
        };
        return this;
    }

    public String getNewField(DocumentBase document) {
        return validateNullableValidation(document, this.newFieldValidation);
    }

    private DocumentParamsStateValidator setValidTypedValue(TypedValue validTypedValue) {
        this.validTypedValueValidation = document -> {
            validateNotNull(validTypedValue);
            DocumentLogic.validateTypeMatches(document, getValidField(document), validTypedValue);
            return validTypedValue;
        };
        return this;
    }

    public TypedValue getValidTypedValue(DocumentBase document) {
        return validateNullableValidation(document, this.validTypedValueValidation);
    }

    private DocumentParamsStateValidator setField(String field) {
        this.fieldValidation = document -> {
            validateNotNull(field);
            return field;
        };
        return this;
    }

    public String getField(DocumentBase document) {
        return validateNullableValidation(document, this.fieldValidation);
    }

    private DocumentParamsStateValidator setDataTypeConfig(DataTypeConfig dataTypeConfig) {
        this.dataTypeConfigValidation = document -> {
            validateNotNull(dataTypeConfig);
            return dataTypeConfig;
        };
        return this;
    }

    public DataTypeConfig getDataTypeConfig(DocumentBase document) {
        return validateNullableValidation(document, this.dataTypeConfigValidation);
    }

    private DocumentParamsStateValidator setValue(Object value) {
        this.valueValidation = document -> {
            validateNotNull(value);
            return value;
        };
        return this;
    }

    public Object getValue(DocumentBase document) {
        return validateNullableValidation(document, this.valueValidation);
    }

}
