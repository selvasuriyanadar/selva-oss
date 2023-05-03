package selva.oss.ds.document;

import static selva.oss.lang.Commons.*;
import selva.oss.ds.document.datatype.DataTypeConfig;
import selva.oss.ds.document.datatype.TypedValue;

import java.util.*;
import java.util.stream.Stream;

public class TypedDocument<T extends Enum<T>> implements Document<T> {

    private DocumentStore<String> document = DocumentStore.create();
    private Class<T> keyClass;

    private TypedDocument(Class<T> keyClass) {
        validateNotNull(keyClass);

        this.keyClass = keyClass;
    }

    public TypedDocument(Class<T> keyClass, FieldsConfig<T> fieldsConfig) {
        this(keyClass);
        validateNotNull(fieldsConfig);

        initialiseFromFieldsConfig(fieldsConfig);
    }

    public <U extends Enum<U>> TypedDocument(Class<T> keyClass, TypedDocument<U> typedDocument) {
        this(keyClass);
        validateNotNull(typedDocument);

        initialiseFromStore(typedDocument.getDocument());
    }

    public <U extends Enum<U>> TypedDocument(Class<T> keyClass, Store<String> store) {
        this(keyClass);
        validateNotNull(store);

        initialiseFromStore(store);
    }

    private void initialiseFromFieldsConfig(FieldsConfig<T> fieldsConfig) {
        streamExpectedFields().forEach(field -> { getDocument().defineFieldType(fieldsConfig.fetchSure(field)); });
    }

    private void initialiseFromStore(Store<String> store) {
        streamExpectedFields().forEach(field -> { getDocument().defineFieldType(field.toString(), store); });
        pull(store);
    }

    private DocumentStore<String> getDocument() {
        return this.document;
    }

    public boolean isPresent(T field) {
        validateNotNull(field);

        return getDocument().isPresent(field.toString());
    }

    public boolean isEqual(TypedDocument<T> inData, T field) {
        validateNotNull(inData);
        validateNotNull(field);

        return getDocument().isEqual(inData.getDocument(), field.toString());
    }

    public boolean diffAndCheckIfEdited(TypedDocument<T> inData, T field) {
        validateNotNull(inData);
        validateNotNull(field);

        return getDocument().diffAndCheckIfEdited(inData.getDocument(), field.toString());
    }

    public <D> Optional<D> getOptional(T field) {
        validateNotNull(field);

        return (Optional<D>) getDocument().getOptional(field.toString());
    }

    public <D> D getSure(T field) {
        validateNotNull(field);

        return (D) getDocument().getSure(field.toString());
    }

    public void setValue(T field, Object value) {
        validateNotNull(field);

        getDocument().setValue(field.toString(), value);
    }

    public void setNullable(T field, Object value) {
        validateNotNull(field);

        getDocument().setNullable(field.toString(), value);
    }

    private Stream<T> streamExpectedFields() {
        return Arrays.asList(keyClass.getEnumConstants()).stream();
    }

    public <U extends Enum<U>> TypedDocument<U> mask(Class<U> newKeyClass) {
        return new TypedDocument<U>(newKeyClass, this);
    }

    public <U extends Enum<U>> TypedDocument<T> merge(TypedDocument<U> typedDocument) {
        validateNotNull(typedDocument);

        typedDocument.streamExpectedFields().filter(field -> getDocument().isFieldPresent(field.toString())).forEach(field -> { getDocument().setTypedValueOptional(field.toString(), typedDocument.getDocument()); });
        return this;
    }

    public TypedDocument<T> pull(Store<String> store) {
        validateNotNull(store);

        streamExpectedFields().forEach(field -> { store.verifyFieldExist(field.toString()); getDocument().setTypedValueOptional(field.toString(), store); });
        return this;
    }

    public Store<String> push(Store<String> store) {
        validateNotNull(store);

        streamExpectedFields().forEach(field -> { store.verifyFieldExist(field.toString()); store.setTypedValueOptional(field.toString(), getDocument()); });
        return store;
    }

}
