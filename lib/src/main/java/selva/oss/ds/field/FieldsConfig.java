package selva.oss.ds.field;

import static selva.oss.lang.Commons.*;
import selva.oss.ds.datatype.DataTypeConfig;
import selva.oss.ds.value.TypedValue;

import java.util.*;
import java.util.stream.Stream;

public class FieldsConfig {

    private Map<Field, FieldConfig> configs = new HashMap<>();

    public FieldsConfig add(String field, DataTypeConfig dataTypeConfig) {
        return add(new StringField(field), dataTypeConfig);
    }

    public <E extends Enum<E>> FieldsConfig add(E field, DataTypeConfig dataTypeConfig) {
        return add(new EnumField<E>(field), dataTypeConfig);
    }

    public FieldsConfig add(Field field, DataTypeConfig dataTypeConfig) {
        validateNotNull(field);

        configs.put(field, new FieldConfig(field, dataTypeConfig));
        return this;
    }

    public Stream<FieldConfig> streamConfigs() {
        return configs.values().stream();
    }

    public static class ConfigDoesNotExistException extends RuntimeException {
    }

    public FieldConfig fetchSure(Field field) {
        if (!configs.containsKey(field)) {
            throw new ConfigDoesNotExistException();
        }
        return configs.get(field);
    }

}
