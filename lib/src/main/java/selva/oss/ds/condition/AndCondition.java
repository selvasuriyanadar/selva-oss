package selva.oss.ds.condition;

import java.util.*;

public class AndCondition extends MultiBooleanCondition {

    public AndCondition(List<ConditionBase> condition) {
        super(condition);
    }

    @Override
    public Condition getCondition() {
        return Condition.And;
    }

}
