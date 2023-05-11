package selva.oss.ds.field;

import static selva.oss.lang.Commons.*;

public interface Field extends Equable<Field> {

    public String getFieldAsString();

    default boolean isEqual(Field field) {
        validateNotNull(field);

        return getFieldAsString().equals(field.getFieldAsString());
    }

}
