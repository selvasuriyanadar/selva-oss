package selva.oss.ds.condition;

import static selva.oss.lang.Commons.*;

import java.util.*;

public class OrCondition extends MultiBooleanCondition {

    public OrCondition(List<ConditionBase> conditions) {
        super(conditions);
    }

    @Override
    public Condition getCondition() {
        return Condition.Or;
    }

}
