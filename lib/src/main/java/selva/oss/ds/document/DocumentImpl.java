package selva.oss.ds.document;

import static selva.oss.lang.CommonValidation.*;
import selva.oss.ds.document.datatype.DataTypeConfig;
import selva.oss.ds.document.datatype.TypedValue;

import java.util.*;

/* The Values and Configs are never expected to be null and they are always expected to be valid
 */

class DocumentImpl implements DocumentBaseImpl {

    private Map<String, DataTypeConfig> dataTypeConfigs = new HashMap<>();
    private Map<String, TypedValue> data = new HashMap<>();

    public boolean containsField(String field) {
        return dataTypeConfigs.containsKey(field);
    }

    public boolean containsValue(String field) {
        return data.containsKey(field);
    }

    public Optional<TypedValue> fetchTypedValue(String field) {
        if (!containsValue(field)) {
            return Optional.empty();
        }
        return Optional.of(data.get(field));
    }

    public TypedValue fetchTypedValueSure(String field) {
        if (!containsValue(field)) {
            throw new DocumentBase.ValueDoesNotExistException();
        }
        return data.get(field);
    }

    public Optional<DataTypeConfig> fetchConfig(String field) {
        if (!containsField(field)) {
            return Optional.empty();
        }
        return Optional.of(dataTypeConfigs.get(field));
    }

    public DataTypeConfig fetchConfigSure(String field) {
        if (!containsField(field)) {
            throw new DocumentBase.ConfigDoesNotExistException();
        }
        return dataTypeConfigs.get(field);
    }

    public void putTypedValue(String field, TypedValue typedValue) {
        data.put(field, typedValue);
    }

    public void putConfig(String field, DataTypeConfig dataTypeConfig) {
        dataTypeConfigs.put(field, dataTypeConfig);
    }

}
