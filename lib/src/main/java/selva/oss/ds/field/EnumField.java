package selva.oss.ds.field;

import static selva.oss.lang.Commons.*;

import java.util.*;

public class EnumField<E extends Enum<E>> implements Field {

    private E field;

    public EnumField(E field) {
        validateNotNull(field);

        this.field = field;
    }

    public String getFieldAsString() {
        return this.field.toString();
    }

    @Override
    public boolean isEqual(Field field) {
        if (!(field instanceof EnumField)) {
            return false;
        }
        return this.field.equals(((EnumField<E>)field).field);
    }

    @Override
    public int hashCode() {
        return this.field.hashCode();
    }

    @Override
    public boolean equals(Object value) {
        return checkIfEqual(Field.class, this, Optional.ofNullable(value));
    }

}
