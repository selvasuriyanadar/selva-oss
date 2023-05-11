package selva.oss.ds.query;

import static selva.oss.lang.Commons.*;
import static selva.oss.lang.operation.DataOrientedOps.produceByAll;
import static selva.oss.lang.operation.DataOrientedOps.ProduceOperation;
import selva.oss.lang.GsonUtils;

import selva.oss.ds.condition.*;
import selva.oss.ds.datatype.DataType;
import selva.oss.ds.datatype.DataTypeConfig;
import selva.oss.ds.field.FieldConfig;
import selva.oss.ds.field.FieldsConfig;
import selva.oss.ds.field.Field;
import selva.oss.ds.field.StringField;
import selva.oss.ds.value.TypedValue;
import selva.oss.ds.fieldorvalue.FieldOrValue;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

import java.util.*;

public class QueryInferFromJsonLogic {

    public enum JsonField {
        condition, lhsField, rhsField, lhsValue, rhsValue, subCondition, subConditions
    }

    public static class CouldNotParseConditionException extends RuntimeException {
    }

    private static Condition parseCondition(JsonObject jsonObject) {
        JsonElement condition = jsonObject.get(JsonField.condition.toString());
        if (condition == null) {
            throw new CouldNotParseConditionException();
        }

        try {
            return Condition.valueOf(condition.getAsString());
        }
        catch (ClassCastException | IllegalStateException | NullPointerException | IllegalArgumentException e) {
            throw new CouldNotParseConditionException();
        }
    }

    public static class CouldNotParseFieldException extends RuntimeException {
    }

    private static Field parseField(String key, JsonObject jsonObject) {
        JsonElement field = jsonObject.get(key);
        if (field == null) {
            throw new CouldNotParseFieldException();
        }

        try {
            return new StringField(field.getAsString());
        }
        catch (ClassCastException | IllegalStateException e) {
            throw new CouldNotParseFieldException();
        }
    }

    private static FieldConfig parseFieldConfig(String key, JsonObject jsonObject, DataTypeConfig dataTypeConfig) {
        JsonElement field = jsonObject.get(key);
        if (field == null) {
            throw new CouldNotParseFieldException();
        }

        try {
            return new FieldConfig(new StringField(field.getAsString()), dataTypeConfig);
        }
        catch (ClassCastException | IllegalStateException e) {
            throw new CouldNotParseFieldException();
        }
    }

    public static class CouldNotParseValueException extends RuntimeException {
    }

    private static TypedValue parseValue(String key, JsonObject jsonObject, DataTypeConfig dataTypeConfig) {
        JsonElement value = jsonObject.get(key);
        if (value == null) {
            throw new CouldNotParseValueException();
        }

        return new TypedValue(dataTypeConfig, value);
    }

    public static class ComparativeConditionData {

        public static class LhsFieldOrValueDoesNotExistException extends RuntimeException {
        }

        public static class RhsFieldOrValueDoesNotExistException extends RuntimeException {
        }

        private FieldOrValue lhsField;
        private FieldOrValue rhsField;

        public ComparativeConditionData(JsonObject jsonObject, FieldsConfig fieldsConfig) {
            ComparativeConditionInferredType comparativeConditionInferredType = new ComparativeConditionInferredType(jsonObject, fieldsConfig);
            this.lhsField = parseLhsFieldOrValue(jsonObject, comparativeConditionInferredType);
            this.rhsField = parseRhsFieldOrValue(jsonObject, comparativeConditionInferredType);
        }

        public FieldOrValue getLhsField() {
            return this.lhsField;
        }

        public FieldOrValue getRhsField() {
            return this.rhsField;
        }

        private FieldOrValue parseLhsFieldOrValue(JsonObject jsonObject, ComparativeConditionInferredType comparativeConditionInferredType) {
            if (GsonUtils.doesFieldExist(JsonField.lhsField.toString(), jsonObject)) {
                return new FieldOrValue(QueryInferFromJsonLogic.parseFieldConfig(JsonField.lhsField.toString(), jsonObject, comparativeConditionInferredType.getLhsDataTypeConfig()));
            }
            else if (GsonUtils.doesFieldExist(JsonField.lhsValue.toString(), jsonObject)) {
                return new FieldOrValue(QueryInferFromJsonLogic.parseValue(JsonField.lhsValue.toString(), jsonObject, comparativeConditionInferredType.getLhsDataTypeConfig()));
            }
            throw new LhsFieldOrValueDoesNotExistException();
        }

        private FieldOrValue parseRhsFieldOrValue(JsonObject jsonObject, ComparativeConditionInferredType comparativeConditionInferredType) {
            if (GsonUtils.doesFieldExist(JsonField.rhsField.toString(), jsonObject)) {
                return new FieldOrValue(QueryInferFromJsonLogic.parseFieldConfig(JsonField.rhsField.toString(), jsonObject, comparativeConditionInferredType.getRhsDataTypeConfig()));
            }
            else if (GsonUtils.doesFieldExist(JsonField.rhsValue.toString(), jsonObject)) {
                return new FieldOrValue(QueryInferFromJsonLogic.parseValue(JsonField.rhsValue.toString(), jsonObject, comparativeConditionInferredType.getRhsDataTypeConfig()));
            }
            throw new RhsFieldOrValueDoesNotExistException();
        }

    }

    private static class ComparativeConditionInferredType {

        public static class ComparativeConditionMustHaveAtleastOneFieldException extends RuntimeException {
        }

        private DataTypeConfig lhsDataTypeConfig;
        private DataTypeConfig rhsDataTypeConfig;

        public ComparativeConditionInferredType(JsonObject jsonObject, FieldsConfig fieldsConfig) {
            if (GsonUtils.doesFieldExist(JsonField.lhsField.toString(), jsonObject)) {
                setLhsDataTypeConfig(fieldsConfig.fetchSure(QueryInferFromJsonLogic.parseField(JsonField.lhsField.toString(), jsonObject)).getDataTypeConfig());
            }
            else if (GsonUtils.doesFieldExist(JsonField.rhsField.toString(), jsonObject)) {
                setRhsDataTypeConfig(fieldsConfig.fetchSure(QueryInferFromJsonLogic.parseField(JsonField.rhsField.toString(), jsonObject)).getDataTypeConfig());
            }
            else {
                throw new ComparativeConditionMustHaveAtleastOneFieldException();
            }
        }

        private void setLhsDataTypeConfig(DataTypeConfig lhsDataTypeConfig) {
            this.lhsDataTypeConfig = lhsDataTypeConfig;
        }

        private void setRhsDataTypeConfig(DataTypeConfig rhsDataTypeConfig) {
            this.rhsDataTypeConfig = rhsDataTypeConfig;
        }

        private DataTypeConfig getDataTypeConfig() {
            if (this.lhsDataTypeConfig != null) {
                return this.lhsDataTypeConfig;
            }
            else {
                return this.rhsDataTypeConfig;
            }
        }

        public DataTypeConfig getRhsDataTypeConfig() {
            if (this.rhsDataTypeConfig != null) {
                return this.rhsDataTypeConfig;
            }
            return getDataTypeConfig();
        }

        public DataTypeConfig getLhsDataTypeConfig() {
            if (this.lhsDataTypeConfig != null) {
                return this.lhsDataTypeConfig;
            }
            return getDataTypeConfig();
        }

    }

    public static ConditionBase inferCondition(JsonObject jsonObject, FieldsConfig fieldsConfig) {
        Condition condition = parseCondition(jsonObject);
        return inferCondition(condition, jsonObject, fieldsConfig);
    }

    public static ConditionBase inferCondition(Condition condition, JsonObject jsonObject, FieldsConfig fieldsConfig) {
        return Condition.produceByBaseConditionType(condition,
                () -> inferConditionByBooleanConditionType(condition, jsonObject, fieldsConfig),
                () -> inferConditionByComparativeConditionType(condition, jsonObject, fieldsConfig));
    }

    private static ConditionBase inferConditionByBooleanConditionType(Condition condition, JsonObject jsonObject, FieldsConfig fieldsConfig) {
        return Condition.produceByBooleanConditionType(condition,
                () -> inferConditionByMultiBooleanConditionType(condition, jsonObject, fieldsConfig),
                () -> inferConditionBySimpleBooleanConditionType(condition, jsonObject, fieldsConfig));
    }

    private static ConditionBase inferConditionByMultiBooleanConditionType(Condition condition, JsonObject jsonObject, FieldsConfig fieldsConfig) {
        return Condition.produceByMultiBooleanConditionType(condition,
                () -> produceByAll((resultOld, resultNew) -> new AndCondition(Arrays.asList(resultOld, resultNew)),
                                   (resultNew, andCondition) -> andCondition.add(resultNew),
                                   ((ProduceOperation<ConditionBase>[]) GsonUtils.fetchJsonObjects(JsonField.subConditions.toString(), jsonObject)
                                    .map(c -> ((ProduceOperation<ConditionBase>) (() -> inferCondition(c, fieldsConfig)))).toArray(ProduceOperation[]::new))),
                () -> produceByAll((resultOld, resultNew) -> new OrCondition(Arrays.asList(resultOld, resultNew)),
                                   (resultNew, orCondition) -> orCondition.add(resultNew),
                                   ((ProduceOperation<ConditionBase>[]) GsonUtils.fetchJsonObjects(JsonField.subConditions.toString(), jsonObject)
                                    .map(c -> ((ProduceOperation<ConditionBase>) (() -> inferCondition(c, fieldsConfig)))).toArray(ProduceOperation[]::new))));
    }

    private static ConditionBase inferConditionBySimpleBooleanConditionType(Condition condition, JsonObject jsonObject, FieldsConfig fieldsConfig) {
        return Condition.produceBySimpleBooleanConditionType(condition,
                () -> new NotCondition(inferCondition(GsonUtils.fetchJsonObject(JsonField.subCondition.toString(), jsonObject), fieldsConfig)));
    }

    private static ConditionBase inferConditionByComparativeConditionType(Condition condition, JsonObject jsonObject, FieldsConfig fieldsConfig) {
        return Condition.produceByComparativeConditionType(condition,
                () -> inferConditionBySimpleComparativeConditionType(condition, jsonObject, fieldsConfig),
                () -> inferConditionByRelativeComparativeConditionType(condition, jsonObject, fieldsConfig));
    }

    private static ConditionBase inferConditionBySimpleComparativeConditionType(Condition condition, JsonObject jsonObject, FieldsConfig fieldsConfig) {
        return Condition.produceBySimpleComparativeConditionType(condition,
                () -> { ComparativeConditionData comparativeConditionData = new ComparativeConditionData(jsonObject, fieldsConfig); return new EqualsCondition(comparativeConditionData.getLhsField(), comparativeConditionData.getRhsField()); },
                () -> { ComparativeConditionData comparativeConditionData = new ComparativeConditionData(jsonObject, fieldsConfig); return new NotEqualsCondition(comparativeConditionData.getLhsField(), comparativeConditionData.getRhsField()); });
    }

    private static ConditionBase inferConditionByRelativeComparativeConditionType(Condition condition, JsonObject jsonObject, FieldsConfig fieldsConfig) {
        return Condition.produceByRelativeComparativeConditionType(condition,
                () -> { ComparativeConditionData comparativeConditionData = new ComparativeConditionData(jsonObject, fieldsConfig); return new GreaterThanCondition(comparativeConditionData.getLhsField(), comparativeConditionData.getRhsField()); },
                () -> { ComparativeConditionData comparativeConditionData = new ComparativeConditionData(jsonObject, fieldsConfig); return new GreaterThanOrEqualsCondition(comparativeConditionData.getLhsField(), comparativeConditionData.getRhsField()); },
                () -> { ComparativeConditionData comparativeConditionData = new ComparativeConditionData(jsonObject, fieldsConfig); return new LesserThanCondition(comparativeConditionData.getLhsField(), comparativeConditionData.getRhsField()); },
                () -> { ComparativeConditionData comparativeConditionData = new ComparativeConditionData(jsonObject, fieldsConfig); return new LesserThanOrEqualsCondition(comparativeConditionData.getLhsField(), comparativeConditionData.getRhsField()); });
    }

}
