package selva.oss.lang;

import java.util.*;
import java.util.stream.Stream;

public class CommonValidation {

    public static class Nothing {

        public static Optional<Nothing> of(boolean bool) {
            return bool ? Optional.of(new Nothing()) : Optional.empty();
        }

    }

    public interface Equable<T> {
        public boolean isEqual(T value);
    }

    public interface Hashable {
        public Stream<Object> getSimplifiedDataView();
    }

    public static <T> boolean checkIfEqual(Class<T> type, Equable<T> thisValue, Optional<Object> newValue) {
        if (!newValue.isPresent()) {
            return false;
        }
        if (thisValue == newValue.get()) {
            return true;
        }
        if (!type.isInstance(newValue.get())) {
            return false;
        }
        return thisValue.isEqual((T) newValue.get());
    }

    public static int produceHash(Hashable hashable) {
        return Objects.hash(hashable.getSimplifiedDataView().toArray());
    }

    public static class CommonValidationFailedException extends RuntimeException {

        public enum CommonValidationFailedState {
            NullNotExpected, UnexpectedType
        }

        public CommonValidationFailedException(CommonValidationFailedState commonValidationFailedState) {
            super(commonValidationFailedState.toString());
        }

    }

    public static void validateNotNull(Object value) {
        if (value == null) {
            throw new CommonValidationFailedException(CommonValidationFailedException.CommonValidationFailedState.NullNotExpected);
        }
    }

    public static void validateType(Class valueType, Object value) {
        if (!valueType.isInstance(value)) {
            throw new CommonValidationFailedException(CommonValidationFailedException.CommonValidationFailedState.UnexpectedType);
        }
    }

    public static class HasNotBeenImplementedException extends RuntimeException {
    }

}
