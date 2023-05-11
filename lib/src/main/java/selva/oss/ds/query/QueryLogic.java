package selva.oss.ds.query;

import static selva.oss.lang.Commons.*;
import static selva.oss.lang.operation.DataOrientedOps.produceByAll;
import static selva.oss.lang.operation.DataOrientedOps.ProduceOperation;

import selva.oss.ds.condition.ConditionBase;
import selva.oss.ds.condition.MultiBooleanCondition;
import selva.oss.ds.condition.SimpleBooleanCondition;
import selva.oss.ds.condition.ComparativeCondition;
import selva.oss.ds.condition.Condition;

import java.util.*;

public class QueryLogic {

    public static String produceQuery(ConditionBase condition) {
        return Condition.produceByBaseConditionType(condition.getCondition(),
                () -> produceQueryByBooleanConditionType(condition),
                () -> produceQueryByComparativeConditionType(condition));
    }

    private static String produceQueryByBooleanConditionType(ConditionBase condition) {
        return Condition.produceByBooleanConditionType(condition.getCondition(),
                () -> produceQueryByMultiBooleanConditionType(condition),
                () -> produceQueryBySimpleBooleanConditionType(condition));
    }

    private static String produceQueryByMultiBooleanConditionType(ConditionBase condition) {
        return Condition.produceByMultiBooleanConditionType(condition.getCondition(),
                () -> "(" + produceByAll((resultOld, resultNew) -> (resultOld + " and " + resultNew),
                                   (ProduceOperation<String>[]) (((MultiBooleanCondition) condition).getSubConditions().stream()
                                   .map(c -> ((ProduceOperation<String>) (() -> produceQuery(c)))).toArray(ProduceOperation[]::new))) + ")",
                () -> "(" + produceByAll((resultOld, resultNew) -> (resultOld + " or " + resultNew),
                                   (ProduceOperation<String>[]) (((MultiBooleanCondition) condition).getSubConditions().stream()
                                   .map(c -> ((ProduceOperation<String>) (() -> produceQuery(c)))).toArray(ProduceOperation[]::new))) + ")");
    }

    private static String produceQueryBySimpleBooleanConditionType(ConditionBase condition) {
        return Condition.produceBySimpleBooleanConditionType(condition.getCondition(),
                () -> "(" + "not " + produceQuery(((SimpleBooleanCondition) condition).getSubCondition()) + ")");
    }

    private static String produceQueryByComparativeConditionType(ConditionBase condition) {
        return Condition.produceByComparativeConditionType(condition.getCondition(),
                () -> produceQueryBySimpleComparativeConditionType(condition),
                () -> produceQueryByRelativeComparativeConditionType(condition));
    }

    private static String produceQueryBySimpleComparativeConditionType(ConditionBase condition) {
        return Condition.produceBySimpleComparativeConditionType(condition.getCondition(),
                () -> "(" + ((ComparativeCondition) condition).getLhsFieldOrValue().getAsString() + " = " + ((ComparativeCondition) condition).getRhsFieldOrValue().getAsString() + ")",
                () -> "(" + ((ComparativeCondition) condition).getLhsFieldOrValue().getAsString() + " != " + ((ComparativeCondition) condition).getRhsFieldOrValue().getAsString() + ")");
    }

    private static String produceQueryByRelativeComparativeConditionType(ConditionBase condition) {
        return Condition.produceByRelativeComparativeConditionType(condition.getCondition(),
                () -> "(" + ((ComparativeCondition) condition).getLhsFieldOrValue().getAsString() + " > " + ((ComparativeCondition) condition).getRhsFieldOrValue().getAsString() + ")",
                () -> "(" + ((ComparativeCondition) condition).getLhsFieldOrValue().getAsString() + " >= " + ((ComparativeCondition) condition).getRhsFieldOrValue().getAsString() + ")",
                () -> "(" + ((ComparativeCondition) condition).getLhsFieldOrValue().getAsString() + " < " + ((ComparativeCondition) condition).getRhsFieldOrValue().getAsString() + ")",
                () -> "(" + ((ComparativeCondition) condition).getLhsFieldOrValue().getAsString() + " <= " + ((ComparativeCondition) condition).getRhsFieldOrValue().getAsString() + ")");
    }

}
