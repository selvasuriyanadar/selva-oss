package selva.oss.ds.condition;

import static selva.oss.lang.Commons.*;

import java.util.*;

public class OrCondition extends MultiBooleanCondition {

    public OrCondition(List<ConditionBase> condition) {
        super(condition);
    }

    @Override
    public Condition getCondition() {
        return Condition.Or;
    }

}
