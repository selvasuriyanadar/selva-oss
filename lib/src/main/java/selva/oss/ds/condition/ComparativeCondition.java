package selva.oss.ds.condition;

import static selva.oss.lang.Commons.*;

import selva.oss.ds.fieldorvalue.FieldOrValue;
import selva.oss.ds.fieldorvalue.FieldOrValueLogic;

public abstract class ComparativeCondition extends ConditionBase {

    protected FieldOrValue lhsFieldOrValue;
    protected FieldOrValue rhsFieldOrValue;

    public ComparativeCondition(FieldOrValue lhsFieldOrValue, FieldOrValue rhsFieldOrValue) {
        validateNotNull(lhsFieldOrValue);
        validateNotNull(rhsFieldOrValue);
        FieldOrValueLogic.validateBothAreNotValues(lhsFieldOrValue, rhsFieldOrValue);
        FieldOrValueLogic.validateDataTypeMatch(lhsFieldOrValue, rhsFieldOrValue);

        this.lhsFieldOrValue = lhsFieldOrValue;
        this.rhsFieldOrValue = rhsFieldOrValue;
    }

    public FieldOrValue getLhsFieldOrValue() {
        return this.lhsFieldOrValue;
    }

    public FieldOrValue getRhsFieldOrValue() {
        return this.rhsFieldOrValue;
    }

}
