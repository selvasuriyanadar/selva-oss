package selva.oss.ds.document

import selva.oss.ds.datatype.DataTypeConfig;
import selva.oss.ds.value.TypedValue;
import selva.oss.ds.field.FieldsConfig;

import spock.lang.Specification

class TypedDocumentTest extends Specification {

    private enum Field {
        stringField, longField, integerField, floatField, enumField, listField
    }

    private enum TestEnum {
        CAT, DOG, PARROT, PEACOCK
    }

    public static TypedDocument initialisingWithFieldsConfig() {
        return new TypedDocument(Field.class, new FieldsConfig().add(Field.stringField, DataTypeConfig.createString()).add(Field.longField, DataTypeConfig.createLong()).add(Field.integerField, DataTypeConfig.createInteger()).add(Field.floatField, DataTypeConfig.createFloat()).add(Field.enumField, DataTypeConfig.createEnum(TestEnum.class)).add(Field.listField, DataTypeConfig.createList(DataTypeConfig.createInteger())));
    }

    def "initialising with FieldsConfig"() {
        when:
        initialisingWithFieldsConfig();

        then:
        notThrown(Exception)
    }

    def "setting fields"() {
        setup:
        final TypedDocument typedDocument = initialisingWithFieldsConfig();

        when:
        typedDocument.setValue(field, value);

        then:
        notThrown(Exception)

        where:
        field | value
        Field.stringField | "dkjdkfjk"
        Field.longField | 83483L
        Field.integerField | 38383
        Field.floatField | 343.44F
        Field.enumField | TestEnum.DOG
        Field.listField | [1,3,4,5]
        Field.listField | []
    }

    def "setting fields nullable"() {
        setup:
        final TypedDocument typedDocument = initialisingWithFieldsConfig();

        when:
        typedDocument.setNullable(field, valueNullable);
        def returnedValue = typedDocument.getOptional(field);

        then:
        notThrown(Exception)
        returnedValue != null
        valueNullable == null? !returnedValue.isPresent() : returnedValue.isPresent()

        where:
        field | valueNullable
        Field.stringField | "dkjdkfjk"
        Field.stringField | null
        Field.longField | 83483L
        Field.longField | null
        Field.integerField | 38383
        Field.integerField | null
        Field.floatField | 343.44F
        Field.floatField | null
        Field.enumField | TestEnum.DOG
        Field.enumField | null
        Field.listField | [1,3,4,5]
        Field.listField | null
        Field.listField | []
    }

    def "getting fields sure"() {
        setup:
        final TypedDocument typedDocument = initialisingWithFieldsConfig();

        when:
        typedDocument.setValue(field, value);
        def returnedValue = typedDocument.getSure(field);

        then:
        notThrown(Exception)
        returnedValue != null;

        where:
        field | value
        Field.stringField | "dkjdkfjk"
        Field.longField | 83483L
        Field.integerField | 38383
        Field.floatField | 343.44F
        Field.enumField | TestEnum.DOG
        Field.listField | [1,3,4,5]
        Field.listField | []
    }

    def "optionally getting fields"() {
        setup:
        final TypedDocument typedDocument = initialisingWithFieldsConfig();

        when:
        if (valueOpt.isPresent()) {
            typedDocument.setValue(field, valueOpt.get());
        }
        def returnedValue = typedDocument.getOptional(field);

        then:
        notThrown(Exception)
        returnedValue != null
        !valueOpt.isPresent()? !returnedValue.isPresent() : returnedValue.isPresent()

        where:
        field | valueOpt
        Field.stringField | Optional.of("dkdfk")
        Field.stringField | Optional.empty()
        Field.longField | Optional.of(3434L)
        Field.longField | Optional.empty()
        Field.integerField | Optional.of(3434)
        Field.integerField | Optional.empty()
        Field.floatField | Optional.of(343.33F)
        Field.floatField | Optional.empty()
        Field.enumField | Optional.of(TestEnum.DOG)
        Field.enumField | Optional.empty()
        Field.listField | Optional.of([1,3,4,5])
        Field.listField | Optional.empty()
        Field.listField | Optional.of([])
    }

}
