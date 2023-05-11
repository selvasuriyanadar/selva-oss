package selva.oss.ds.condition;

import static selva.oss.lang.Commons.*;

public abstract class SimpleBooleanCondition extends BooleanCondition {

    protected ConditionBase condition;

    public SimpleBooleanCondition(ConditionBase condition) {
        validateNotNull(condition);

        this.condition = condition;
    }

    public ConditionBase getSubCondition() {
        return this.condition;
    }

}
