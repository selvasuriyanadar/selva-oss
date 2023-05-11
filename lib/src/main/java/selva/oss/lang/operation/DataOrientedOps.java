package selva.oss.lang.operation;

import static selva.oss.lang.Commons.*;

import java.util.*;

public class DataOrientedOps {

    public interface ProduceOperation<T> {
        public T produce();
    }

    public static class UnexpectedType extends RuntimeException {
    }

    public static <T> Optional<T> catchUnexpectedType(ProduceOperation<Optional<T>> produceOperation) {
        try {
            return produceOperation.produce();
        }
        catch (UnexpectedType e) {
            return Optional.empty();
        }
    }

    public interface ReduceOperation<T> {
        public T reduce(T finalResult, T thisResult);
    }

    public static class NothingToProduceException extends RuntimeException {
    }

    public static <T> T produceByAll(ReduceOperation<T> reduceOperation,
            ProduceOperation<T>... produceOperations) {
        if (produceOperations.length == 0) {
            throw new NothingToProduceException();
        }
        if (produceOperations.length == 1) {
            return produceOperations[0].produce();
        }
        T result = reduceOperation.reduce(produceOperations[0].produce(),
                produceOperations[1].produce());
        for (int i = 2; i < produceOperations.length; i++) {
            result = reduceOperation.reduce(result,
                    produceOperations[i].produce());
        }
        return result;
    }

    public interface ApplyOperation {
        public void apply();
    }

}
