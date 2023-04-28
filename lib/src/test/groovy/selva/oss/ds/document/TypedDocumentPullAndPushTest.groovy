package selva.oss.ds.document

import selva.oss.ds.document.datatype.DataTypeConfig;
import selva.oss.ds.document.datatype.TypedValue;

import spock.lang.Specification

class TypedDocumentPullAndPushTest extends Specification {

    private enum Field {
        stringField, longField, integerField, floatField
    }

    public static class Pojo {
        public String stringField;
        public Long longField;
        public Integer integerField;
        public Float floatField;

        public <T> void set(Field field, T value) {
            switch (field) {
                case Field.stringField: this.stringField = value; break;
                case Field.longField: this.longField = value; break;
                case Field.integerField: this.integerField = value; break;
                case Field.floatField: this.floatField = value; break;
                default: throw new IllegalStateException("Field is not defined in pojo.");
            }
        }
    }

    private enum WhichDoc {
        ThisDoc, NewDoc, Both
    }

    private static TypedDocument initialisingWithFieldsConfig() {
        return new TypedDocument(Field.class, new FieldsConfig().add(Field.stringField, DataTypeConfig.createString()).add(Field.longField, DataTypeConfig.createLong()).add(Field.integerField, DataTypeConfig.createInteger()).add(Field.floatField, DataTypeConfig.createFloat()));
    }

    def "verifying push"() {
        setup:
        final TypedDocument<Field> typedDocument = initialisingWithFieldsConfig();
        data.each(entry -> typedDocument.setValue(entry.key, entry.value));

        when:
        Store store = Store.create(new Pojo());
        typedDocument.push(store);

        then:
        notThrown(StoreGetOps.FieldDoesNotExistException)
        data.each(entry -> {
            assert store.getTypedValue(entry.key.toString()).isEqual(new TypedValue(typedDocument.getSure(entry.key)))
        })

        where:
        data << [[[key:Field.stringField, value:"dkjdkfjk"], [key:Field.longField, value:83483L], [key:Field.integerField, value:38383], [key:Field.floatField, value:343.44F]],
        [[key:Field.stringField, value:"dfeiekek"], [key:Field.longField, value:74848L], [key:Field.integerField, value:33734], [key:Field.floatField, value:793.14F]],
        [[key:Field.stringField, value:"qweoojrm"], [key:Field.longField, value:66593L], [key:Field.integerField, value:16388], [key:Field.floatField, value:923.04F]]]
    }

    def "verifying pull"() {
        setup:
        final TypedDocument<Field> typedDocument = initialisingWithFieldsConfig();
        final Pojo pojo = new Pojo();
        data.each(entry -> typedDocument.setValue(entry.key, entry.value));
        newData.each(entry -> pojo.set(entry.key, entry.value));

        when:
        Store store = Store.create(pojo);
        typedDocument.pull(store);

        then:
        notThrown(StoreGetOps.FieldDoesNotExistException)
        newData.each(entry -> {
            assert store.getTypedValue(entry.key.toString()).isEqual(new TypedValue(typedDocument.getSure(entry.key)))
        })

        where:
        data << [[[key:Field.stringField, value:"dkjdkfjk"], [key:Field.longField, value:83483L], [key:Field.integerField, value:38383], [key:Field.floatField, value:343.44F]],
        [[key:Field.stringField, value:"dfeiekek"], [key:Field.longField, value:74848L], [key:Field.integerField, value:33734], [key:Field.floatField, value:793.14F]],
        [[key:Field.stringField, value:"qweoojrm"], [key:Field.longField, value:66593L], [key:Field.integerField, value:16388], [key:Field.floatField, value:923.04F]]]
        newData << [[[key:Field.stringField, value:"dkjfieii"], [key:Field.integerField, value:17983], [key:Field.floatField, value:78811.0292F]],
        [[key:Field.stringField, value:"aayyrjrj"], [key:Field.integerField, value:119987], [key:Field.floatField, value:3387289.9884373F]],
        [[key:Field.stringField, value:"vaqlmaalll"], [key:Field.integerField, value:9819177], [key:Field.floatField, value:78844442.8829292F]]]
    }

}
