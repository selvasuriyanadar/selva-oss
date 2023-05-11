package selva.oss.ds.fieldorvalue;

import static selva.oss.lang.Commons.*;

public class FieldOrValueLogic {

    public static class BothFieldOrValuesCannotBeOfTypeValue extends RuntimeException {
    }

    public static void validateBothAreNotValues(FieldOrValue lhsFieldOrValue, FieldOrValue rhsFieldOrValue) {
        if (lhsFieldOrValue.getType() == Type.Value && rhsFieldOrValue.getType() == Type.Value) {
            throw new BothFieldOrValuesCannotBeOfTypeValue();
        }
    }

    public static class BothFieldOrValuesMustHaveTheSameDataType extends RuntimeException {
    }

    public static void validateDataTypeMatch(FieldOrValue lhsFieldOrValue, FieldOrValue rhsFieldOrValue) {
        if (!lhsFieldOrValue.getDataTypeConfig().isEqual(rhsFieldOrValue.getDataTypeConfig())) {
            throw new BothFieldOrValuesMustHaveTheSameDataType();
        }
    }

}
