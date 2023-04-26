package selva.oss.lang.reflection;

import static selva.oss.lang.CommonValidation.*;

import java.util.*;
import java.lang.reflect.Field;

public class ReflectiveData {

    private Object object;
    private Class pojoClass;

    public ReflectiveData(Object object) {
        validateNotNull(object);

        this.object = object;
        this.pojoClass = object.getClass();
    }

    public boolean containsField(String field) {
        try {
            this.pojoClass.getDeclaredField(field);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    public static class FieldNotFoundException extends RuntimeException {
    }

    public Field getDeclaredField(String field) {
        try {
            return this.pojoClass.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            throw new FieldNotFoundException();
        }
    }

    public static class CouldNotAccessFieldException extends RuntimeException {
    }

    public Object get(String field) {
        try {
            return getDeclaredField(field).get(this.object);
        } catch (IllegalAccessException e) {
            throw new CouldNotAccessFieldException();
        }
    }

    public void set(String field, Object value) {
        try {
            getDeclaredField(field).set(this.object, value);
        } catch (IllegalAccessException e) {
            throw new CouldNotAccessFieldException();
        }
    }

}
