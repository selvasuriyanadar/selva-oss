package selva.oss.ds.condition;

import static selva.oss.lang.Commons.*;

import java.util.*;

public abstract class MultiBooleanCondition extends BooleanCondition {

    protected List<ConditionBase> condition;

    public MultiBooleanCondition(List<ConditionBase> condition) {
        validateMinimumCollectionEntries(2, condition);

        this.condition = condition;
    }

    public List<ConditionBase> getSubConditions() {
        return new ArrayList<>(this.condition);
    }

}
