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

    public interface InitialReduceOperation<T, R> {
        public R reduce(T oldValue, T newValue);
    }

    public interface Accumulator<T, R> {
        public void accumulate(T value, R result);
    }

    private static class Result<R> {
        public Optional<R> result = Optional.empty();
    }

    public static <T, R> R produceByAll(InitialReduceOperation<T, R> reduceOperation, Accumulator<T, R> accumulator,
            ProduceOperation<T>... produceOperations) {
        Result<R> result = new Result<R>();
        produceByAll((oldValue, newValue) -> {
            if (!result.result.isPresent()) {
                result.result = Optional.of(reduceOperation.reduce(oldValue, newValue));
                return newValue;
            }
            else {
                accumulator.accumulate(newValue, result.result.get());
                return newValue;
            }
        }, produceOperations);

        if (!result.result.isPresent()) {
            throw new NothingToProduceException();
        }
        return result.result.get();
    }

    public interface ApplyOperation {
        public void apply();
    }

}
