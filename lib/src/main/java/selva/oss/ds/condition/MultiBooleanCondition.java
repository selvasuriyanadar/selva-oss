package selva.oss.ds.condition;

import static selva.oss.lang.Commons.*;

import java.util.*;

public abstract class MultiBooleanCondition extends BooleanCondition {

    protected List<ConditionBase> conditions;

    public MultiBooleanCondition(List<ConditionBase> conditions) {
        validateMinimumCollectionEntries(2, conditions);

        this.conditions = new ArrayList<>(conditions);
    }

    public void add(ConditionBase condition) {
        this.conditions.add(condition);
    }

    public List<ConditionBase> getSubConditions() {
        return new ArrayList<>(this.conditions);
    }

}
