package selva.oss.ds.field;

import static selva.oss.lang.Commons.*;
import selva.oss.ds.datatype.DataTypeConfig;
import selva.oss.ds.value.TypedValue;

import java.util.*;
import java.util.stream.Stream;

public class FieldsConfig<T> {

    private Map<T, FieldConfig> configs = new HashMap<>();

    public FieldsConfig add(T field, DataTypeConfig dataTypeConfig) {
        validateNotNull(field);

        configs.put(field, new FieldConfig(field, dataTypeConfig));
        return this;
    }

    public Stream<FieldConfig> streamConfigs() {
        return configs.values().stream();
    }

    public static class ConfigDoesNotExistException extends RuntimeException {
    }

    public FieldConfig fetchSure(T field) {
        if (!configs.containsKey(field)) {
            throw new ConfigDoesNotExistException();
        }
        return configs.get(field);
    }

}
