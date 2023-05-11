package selva.oss.lang;

import java.util.*;
import java.util.stream.Stream;

public class Commons {

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
            NullNotExpected, UnexpectedType, CaseNotHandled, MinimumCollectionEntriesExpected
        }

        private CommonValidationFailedState commonValidationFailedState;

        public CommonValidationFailedException(CommonValidationFailedState commonValidationFailedState) {
            super(commonValidationFailedState.toString());
            this.commonValidationFailedState = commonValidationFailedState;
        }

        public CommonValidationFailedException(CommonValidationFailedState commonValidationFailedState, String message) {
            super(message);
            this.commonValidationFailedState = commonValidationFailedState;
        }

        public CommonValidationFailedState getCommonValidationFailedState() {
            return this.commonValidationFailedState;
        }

    }

    public static void validateNotNull(Object value) {
        if (value == null) {
            throw new CommonValidationFailedException(CommonValidationFailedException.CommonValidationFailedState.NullNotExpected);
        }
    }

    public static void validateType(Class valueType, Object value) {
        validateNotNull(valueType);

        if (!valueType.isInstance(value)) {
            throw new CommonValidationFailedException(CommonValidationFailedException.CommonValidationFailedState.UnexpectedType);
        }
    }

    public static <T extends Enum<T>> CommonValidationFailedException throwCaseNotHandledException(T notHandledCase) {
        validateNotNull(notHandledCase);

        return new CommonValidationFailedException(CommonValidationFailedException.CommonValidationFailedState.CaseNotHandled,
                "Case " + notHandledCase.toString() + " is not handled.");
    }

    public static void validateMinimumCollectionEntries(int size, Collection collection) {
        validateNotNull(collection);

        if (!(collection.size() >= size)) {
            throw new CommonValidationFailedException(CommonValidationFailedException.CommonValidationFailedState.MinimumCollectionEntriesExpected);
        }
    }

    public static class HasNotBeenImplementedException extends RuntimeException {
    }

}
