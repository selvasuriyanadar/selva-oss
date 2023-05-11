package selva.oss.ds.condition;

import static selva.oss.lang.Commons.*;

public class NotCondition extends SimpleBooleanCondition {

    public NotCondition(ConditionBase condition) {
        super(condition);
    }

    @Override
    public Condition getCondition() {
        return Condition.Not;
    }

}
