package selva.oss.ds.condition;

import static selva.oss.lang.Commons.*;

import selva.oss.ds.fieldorvalue.FieldOrValue;
import selva.oss.ds.fieldorvalue.FieldOrValueLogic;

public class GreaterThanCondition extends RelativeComparativeCondition {

    public GreaterThanCondition(FieldOrValue lhsFieldOrValue, FieldOrValue rhsFieldOrValue) {
        super(lhsFieldOrValue, rhsFieldOrValue);
    }

    @Override
    public Condition getCondition() {
        return Condition.GreaterThan;
    }

}
