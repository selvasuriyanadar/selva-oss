package selva.oss.ds.condition;

import static selva.oss.lang.Commons.*;

import selva.oss.ds.fieldorvalue.FieldOrValue;
import selva.oss.ds.fieldorvalue.FieldOrValueLogic;
import selva.oss.ds.datatype.DataTypeConfigLogic;

public abstract class RelativeComparativeCondition extends ComparativeCondition {

    public RelativeComparativeCondition(FieldOrValue lhsFieldOrValue, FieldOrValue rhsFieldOrValue) {
        super(lhsFieldOrValue, rhsFieldOrValue);
        DataTypeConfigLogic.validateIsNumericalType(lhsFieldOrValue.getDataTypeConfig());
        DataTypeConfigLogic.validateIsNumericalType(rhsFieldOrValue.getDataTypeConfig());
    }

}

