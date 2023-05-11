package selva.oss.ds.condition;

import static selva.oss.lang.Commons.*;

import selva.oss.ds.fieldorvalue.FieldOrValue;
import selva.oss.ds.fieldorvalue.FieldOrValueLogic;

public class LesserThanCondition extends RelativeComparativeCondition {

    public LesserThanCondition(FieldOrValue lhsFieldOrValue, FieldOrValue rhsFieldOrValue) {
        super(lhsFieldOrValue, rhsFieldOrValue);
    }

    @Override
    public Condition getCondition() {
        return Condition.LesserThan;
    }

}
