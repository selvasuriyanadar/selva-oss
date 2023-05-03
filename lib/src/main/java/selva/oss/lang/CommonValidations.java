package selva.oss.lang;

import java.util.*;
import java.util.stream.Stream;

public class CommonValidations {

    public static class NullNotExpectedException extends RuntimeException {
        public NullNotExpectedException(String message) {
            super(message);
        }
    }

    public static void validateNotNull(Object value, String fieldName) {
        if (value != null) {
            return;
        }

        throw new NullNotExpectedException(fieldName + " is required.");
    }

    public static class InvalidStateException extends RuntimeException {
        public InvalidStateException(String message) {
            super(message);
        }
    }

}
