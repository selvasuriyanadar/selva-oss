package selva.oss.ds.query;

import static selva.oss.lang.Commons.*;
import static selva.oss.lang.operation.DataOrientedOps.produceByAll;
import static selva.oss.lang.operation.DataOrientedOps.ProduceOperation;

import selva.oss.ds.condition.ConditionBase;
import selva.oss.ds.condition.MultiBooleanCondition;
import selva.oss.ds.condition.SimpleBooleanCondition;
import selva.oss.ds.condition.ComparativeCondition;
import selva.oss.ds.condition.Condition;

public class QueryLogic {

    public String produceQuery(ConditionBase condition) {
        return Condition.produceByBaseConditionType(condition.getCondition(),
                () -> produceQueryByBooleanConditionType(condition),
                () -> produceQueryByComparativeConditionType(condition));
    }

    private String produceQueryByBooleanConditionType(ConditionBase condition) {
        return Condition.produceByBooleanConditionType(condition.getCondition(),
                () -> produceQueryByMultiBooleanConditionType(condition),
                () -> produceQueryBySimpleBooleanConditionType(condition));
    }

    private String produceQueryByMultiBooleanConditionType(ConditionBase condition) {
        return Condition.produceByMultiBooleanConditionType(condition.getCondition(),
                () -> produceByAll((resultOld, resultNew) -> (resultOld + " and " + resultNew),
                                   (ProduceOperation<String>[]) ((MultiBooleanCondition) condition).getSubConditions().stream()
                                   .map(c -> (ProduceOperation<String>) (() -> produceQuery(c))).toArray()),
                () -> produceByAll((resultOld, resultNew) -> (resultOld + " or " + resultNew),
                                   (ProduceOperation<String>[]) ((MultiBooleanCondition) condition).getSubConditions().stream()
                                   .map(c -> (ProduceOperation<String>) (() -> produceQuery(c))).toArray()));
    }

    private String produceQueryBySimpleBooleanConditionType(ConditionBase condition) {
        return Condition.produceBySimpleBooleanConditionType(condition.getCondition(),
                () -> "not " + produceQuery(((SimpleBooleanCondition) condition).getSubCondition()));
    }

    private String produceQueryByComparativeConditionType(ConditionBase condition) {
        return Condition.produceByComparativeConditionType(condition.getCondition(),
                () -> produceQueryByRelativeComparativeConditionType(condition),
                () -> produceQueryBySimpleComparativeConditionType(condition));
    }

    private String produceQueryByRelativeComparativeConditionType(ConditionBase condition) {
        return Condition.produceByRelativeComparativeConditionType(condition.getCondition(),
                () -> ((ComparativeCondition) condition).getLhsFieldOrValue().getAsString() + " = " + ((ComparativeCondition) condition).getRhsFieldOrValue().getAsString(),
                () -> ((ComparativeCondition) condition).getLhsFieldOrValue().getAsString() + " != " + ((ComparativeCondition) condition).getRhsFieldOrValue().getAsString());
    }

    private String produceQueryBySimpleComparativeConditionType(ConditionBase condition) {
        return Condition.produceBySimpleComparativeConditionType(condition.getCondition(),
                () -> ((ComparativeCondition) condition).getLhsFieldOrValue().getAsString() + " > " + ((ComparativeCondition) condition).getRhsFieldOrValue().getAsString(),
                () -> ((ComparativeCondition) condition).getLhsFieldOrValue().getAsString() + " >= " + ((ComparativeCondition) condition).getRhsFieldOrValue().getAsString(),
                () -> ((ComparativeCondition) condition).getLhsFieldOrValue().getAsString() + " < " + ((ComparativeCondition) condition).getRhsFieldOrValue().getAsString(),
                () -> ((ComparativeCondition) condition).getLhsFieldOrValue().getAsString() + " <= " + ((ComparativeCondition) condition).getRhsFieldOrValue().getAsString());
    }

}
