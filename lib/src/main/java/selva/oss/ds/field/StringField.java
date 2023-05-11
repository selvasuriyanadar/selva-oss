package selva.oss.ds.field;

import static selva.oss.lang.Commons.*;

import java.util.*;

public class StringField implements Field {

    private String field;

    public StringField(String field) {
        validateNotNull(field);

        this.field = field;
    }

    public String getFieldAsString() {
        return this.field.toString();
    }

    @Override
    public int hashCode() {
        return getFieldAsString().hashCode();
    }

    @Override
    public boolean equals(Object value) {
        return checkIfEqual(Field.class, this, Optional.ofNullable(value));
    }

}
