package selva.oss.ds.condition;

import static selva.oss.lang.operation.DataOrientedOps.ProduceOperation;
import static selva.oss.lang.operation.DataOrientedOps.UnexpectedType;
import static selva.oss.lang.operation.DataOrientedOps.catchUnexpectedType;
import static selva.oss.lang.operation.DataOrientedOps.ReduceOperation;
import static selva.oss.lang.operation.DataOrientedOps.produceByAll;
import static selva.oss.lang.Commons.*;

import java.util.*;
import java.util.stream.Stream;

public enum Condition {

    And(AndCondition.class), Or(OrCondition.class), Not(NotCondition.class), Equals(EqualsCondition.class), NotEquals(NotEqualsCondition.class), GreaterThan(GreaterThanCondition.class), GreaterThanOrEquals(GreaterThanOrEqualsCondition.class), LesserThan(LesserThanCondition.class), LesserThanOrEquals(LesserThanOrEqualsCondition.class);

    private Class<? extends ConditionBase> conditionClass;

    Condition(Class<? extends ConditionBase> conditionClass) {
        this.conditionClass = conditionClass;
    }

    public Class<? extends ConditionBase> getConditionClass() {
        return this.conditionClass;
    }

    // Logic

    public static boolean isBooleanCondition(Condition condition) {
        return Arrays.asList(Condition.And, Condition.Or, Condition.Not).contains(condition);
    }

    public static boolean isMultiBooleanCondition(Condition condition) {
        return Arrays.asList(Condition.And, Condition.Or).contains(condition);
    }

    public static boolean isSimpleBooleanCondition(Condition condition) {
        return Arrays.asList(Condition.Not).contains(condition);
    }

    public static boolean isComparativeCondition(Condition condition) {
        return Arrays.asList(Condition.Equals, Condition.NotEquals, Condition.GreaterThan, Condition.GreaterThanOrEquals, Condition.LesserThan, Condition.LesserThanOrEquals).contains(condition);
    }

    public static boolean isRelativeComparativeCondition(Condition condition) {
        return Arrays.asList(Condition.Equals, Condition.NotEquals).contains(condition);
    }

    public static boolean isSimpleComparativeCondition(Condition condition) {
        return Arrays.asList(Condition.GreaterThan, Condition.GreaterThanOrEquals, Condition.LesserThan, Condition.LesserThanOrEquals).contains(condition);
    }

    public static <T> T produceByBaseConditionType(Condition condition, ProduceOperation<T> produceOperationForBooleanCondition, ProduceOperation<T> produceOperationForComparativeCondition) {
        if (isBooleanCondition(condition)) {
            return produceOperationForBooleanCondition.produce();
        }
        else if (isComparativeCondition(condition)) {
            return produceOperationForComparativeCondition.produce();
        }
        else {
            throw new UnexpectedType();
        }
    }

    public static <T> T produceByBooleanConditionType(Condition condition, ProduceOperation<T> produceOperationForMultiBooleanCondition, ProduceOperation<T> produceOperationForSimpleBooleanCondition) {
        if (isMultiBooleanCondition(condition)) {
            return produceOperationForMultiBooleanCondition.produce();
        }
        else if (isSimpleBooleanCondition(condition)) {
            return produceOperationForSimpleBooleanCondition.produce();
        }
        else {
            throw new UnexpectedType();
        }
    }

    public static <T> T produceByMultiBooleanConditionType(Condition condition, ProduceOperation<T> produceOperationForAndCondition, ProduceOperation<T> produceOperationForOrCondition) {
        return switch (condition) {
            case And -> produceOperationForAndCondition.produce();
            case Or -> produceOperationForOrCondition.produce();
            default -> throw new UnexpectedType();
        };
    }

    public static <T> T produceBySimpleBooleanConditionType(Condition condition, ProduceOperation<T> produceOperationForNotCondition) {
        return switch (condition) {
            case Not -> produceOperationForNotCondition.produce();
            default -> throw new UnexpectedType();
        };
    }

    public static <T> T produceByComparativeConditionType(Condition condition, ProduceOperation<T> produceOperationForRelativeComparativeCondition, ProduceOperation<T> produceOperationForSimpleComparativeCondition) {
        if (isRelativeComparativeCondition(condition)) {
            return produceOperationForRelativeComparativeCondition.produce();
        }
        else if (isSimpleComparativeCondition(condition)) {
            return produceOperationForSimpleComparativeCondition.produce();
        }
        else {
            throw new UnexpectedType();
        }
    }

    public static <T> T produceByRelativeComparativeConditionType(Condition condition, ProduceOperation<T> produceOperationForEqualsCondition, ProduceOperation<T> produceOperationForNotEqualsCondition) {
        return switch (condition) {
            case Equals -> produceOperationForEqualsCondition.produce();
            case NotEquals -> produceOperationForNotEqualsCondition.produce();
            default -> throw new UnexpectedType();
        };
    }

    public static <T> T produceBySimpleComparativeConditionType(Condition condition, ProduceOperation<T> produceOperationForGreaterThanCondition, ProduceOperation<T> produceOperationForGreaterThanOrEqualsCondition, ProduceOperation<T> produceOperationForLesserThanCondition, ProduceOperation<T> produceOperationForLesserThanOrEqualsCondition) {
        return switch (condition) {
            case GreaterThan -> produceOperationForGreaterThanCondition.produce();
            case GreaterThanOrEquals -> produceOperationForGreaterThanOrEqualsCondition.produce();
            case LesserThan -> produceOperationForLesserThanCondition.produce();
            case LesserThanOrEquals -> produceOperationForLesserThanOrEqualsCondition.produce();
            default -> throw new UnexpectedType();
        };
    }

}
