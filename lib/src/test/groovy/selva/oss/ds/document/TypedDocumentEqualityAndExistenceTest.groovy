package selva.oss.ds.document

import selva.oss.ds.datatype.DataTypeConfig;
import selva.oss.ds.datatype.TypedValue;
import selva.oss.ds.field.FieldsConfig;

import spock.lang.Specification

class TypedDocumentEqualityAndExistenceTest extends Specification {

    private enum Field {
        stringField, longField, integerField, floatField, enumField, listField
    }

    private enum TestEnum {
        CAT, DOG, PARROT, PEACOCK
    }

    private enum WhichDoc {
        ThisDoc, NewDoc, Both
    }

    public static TypedDocument initialisingWithFieldsConfig() {
        return new TypedDocument(Field.class, new FieldsConfig().add(Field.stringField, DataTypeConfig.createString()).add(Field.longField, DataTypeConfig.createLong()).add(Field.integerField, DataTypeConfig.createInteger()).add(Field.floatField, DataTypeConfig.createFloat()).add(Field.enumField, DataTypeConfig.createEnum(TestEnum.class)).add(Field.listField, DataTypeConfig.createList(DataTypeConfig.createInteger())));
    }

    def "asserting isPresent"() {
        setup:
        final TypedDocument typedDocument = initialisingWithFieldsConfig();

        when:
        typedDocument.setNullable(field, valueNullable);

        then:
        valueNullable == null? !typedDocument.isPresent(field) : typedDocument.isPresent(field)

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

    def "asserting isEqual"() {
        setup:
        final TypedDocument typedDocument1 = initialisingWithFieldsConfig();
        final TypedDocument typedDocument2 = initialisingWithFieldsConfig();

        when:
        typedDocument1.setValue(field, value);
        typedDocument2.setValue(field, otherValue.orElse(value));

        then:
        !otherValue.isPresent()? typedDocument1.isEqual(typedDocument2, field) : !typedDocument1.isEqual(typedDocument2, field)

        where:
        field | value | otherValue
        Field.stringField | "dkjdkfjk" | Optional.of("dfkdfkdfkj")
        Field.stringField | "dkjdkfjk" | Optional.empty()
        Field.longField | 83483L | Optional.of(484849L)
        Field.longField | 83483L | Optional.empty()
        Field.integerField | 38383 | Optional.of(343455)
        Field.integerField | 38383 | Optional.empty()
        Field.floatField | 343.44F | Optional.of(989.33F)
        Field.floatField | 343.44F | Optional.empty()
        Field.enumField | TestEnum.DOG | Optional.of(TestEnum.CAT)
        Field.enumField | TestEnum.DOG | Optional.empty()
        Field.listField | [1,3,4,5] | Optional.of([4,5,2,3,1,1])
        Field.listField | [1,3,4,5] | Optional.empty()
        Field.listField | [] |  Optional.of([4,5,2,3,1,1])
        Field.listField | [1,3] |  Optional.of([])
        Field.listField | [] |  Optional.empty()
    }

    def "asserting isEqual with nullable values"() {
        setup:
        final TypedDocument typedDocument1 = initialisingWithFieldsConfig();
        final TypedDocument typedDocument2 = initialisingWithFieldsConfig();

        when:
        if (whichDoc == WhichDoc.Both) {
            typedDocument1.setNullable(field, valueNullable);
            typedDocument2.setNullable(field, valueNullable);
        }
        if (whichDoc == WhichDoc.ThisDoc) {
            typedDocument1.setNullable(field, valueNullable);
        }
        if (whichDoc == WhichDoc.NewDoc) {
            typedDocument2.setNullable(field, valueNullable);
        }

        then:
        (valueNullable == null || whichDoc != WhichDoc.Both)? !typedDocument1.isEqual(typedDocument2, field) : typedDocument1.isEqual(typedDocument2, field)

        where:
        field | valueNullable | whichDoc
        Field.stringField | "dkjdkfjk" | WhichDoc.ThisDoc
        Field.stringField | null | WhichDoc.NewDoc
        Field.stringField | null | WhichDoc.Both
        Field.longField | 83483L | WhichDoc.ThisDoc
        Field.longField | null | WhichDoc.Both
        Field.longField | null | WhichDoc.ThisDoc
        Field.integerField | 38383 | WhichDoc.NewDoc
        Field.integerField | null | WhichDoc.Both
        Field.integerField | null | WhichDoc.ThisDoc
        Field.floatField | 343.44F | WhichDoc.Both
        Field.floatField | 343.44F | WhichDoc.NewDoc
        Field.floatField | null | WhichDoc.Both
        Field.enumField | TestEnum.DOG | WhichDoc.Both
        Field.enumField | TestEnum.CAT | WhichDoc.NewDoc
        Field.enumField | null | WhichDoc.ThisDoc
        Field.listField | [1,3,4,5] | WhichDoc.ThisDoc
        Field.listField | [1,3,4,5] | WhichDoc.NewDoc
        Field.listField | null | WhichDoc.NewDoc
        Field.listField | [] | WhichDoc.NewDoc
        Field.listField | [] | WhichDoc.ThisDoc
    }

    def "asserting diffAndCheckIfEqual"() {
        setup:
        final TypedDocument typedDocument1 = initialisingWithFieldsConfig();
        final TypedDocument typedDocument2 = initialisingWithFieldsConfig();

        when:
        typedDocument1.setValue(field, value);
        typedDocument2.setValue(field, otherValue.orElse(value));

        then:
        otherValue.isPresent()? typedDocument1.diffAndCheckIfEdited(typedDocument2, field) : !typedDocument1.diffAndCheckIfEdited(typedDocument2, field)

        where:
        field | value | otherValue
        Field.stringField | "dkjdkfjk" | Optional.of("dfkdfkdfkj")
        Field.stringField | "dkjdkfjk" | Optional.empty()
        Field.longField | 83483L | Optional.of(484849L)
        Field.longField | 83483L | Optional.empty()
        Field.integerField | 38383 | Optional.of(343455)
        Field.integerField | 38383 | Optional.empty()
        Field.floatField | 343.44F | Optional.of(989.33F)
        Field.floatField | 343.44F | Optional.empty()
        Field.enumField | TestEnum.DOG | Optional.of(TestEnum.CAT)
        Field.enumField | TestEnum.DOG | Optional.empty()
        Field.listField | [1,3,4,5] | Optional.of([4,5,2,3,1,1])
        Field.listField | [1,3,4,5] | Optional.empty()
        Field.listField | [] |  Optional.of([4,5,2,3,1,1])
        Field.listField | [1,3] |  Optional.of([])
        Field.listField | [] |  Optional.empty()
    }

    def "asserting diffAndCheckIfEqual with nullable values"() {
        setup:
        final TypedDocument typedDocument1 = initialisingWithFieldsConfig();
        final TypedDocument typedDocument2 = initialisingWithFieldsConfig();

        when:
        if (whichDoc == WhichDoc.Both) {
            typedDocument1.setNullable(field, valueNullable);
            typedDocument2.setNullable(field, valueNullable);
        }
        if (whichDoc == WhichDoc.ThisDoc) {
            typedDocument1.setNullable(field, valueNullable);
        }
        if (whichDoc == WhichDoc.NewDoc) {
            typedDocument2.setNullable(field, valueNullable);
        }

        then:
        (valueNullable == null)? !typedDocument1.diffAndCheckIfEdited(typedDocument2, field) : true
        (whichDoc == WhichDoc.ThisDoc)? !typedDocument1.diffAndCheckIfEdited(typedDocument2, field) : true
        (valueNullable != null && whichDoc == WhichDoc.NewDoc)? typedDocument1.diffAndCheckIfEdited(typedDocument2, field) : true
        (valueNullable != null && whichDoc == WhichDoc.Both)? !typedDocument1.diffAndCheckIfEdited(typedDocument2, field) : true

        where:
        field | valueNullable | whichDoc
        Field.stringField | "dkjdkfjk" | WhichDoc.ThisDoc
        Field.stringField | null | WhichDoc.NewDoc
        Field.stringField | null | WhichDoc.Both
        Field.longField | 83483L | WhichDoc.ThisDoc
        Field.longField | null | WhichDoc.Both
        Field.longField | null | WhichDoc.ThisDoc
        Field.integerField | 38383 | WhichDoc.NewDoc
        Field.integerField | null | WhichDoc.Both
        Field.integerField | null | WhichDoc.ThisDoc
        Field.floatField | 343.44F | WhichDoc.Both
        Field.floatField | 343.44F | WhichDoc.NewDoc
        Field.floatField | null | WhichDoc.Both
        Field.enumField | TestEnum.DOG | WhichDoc.ThisDoc
        Field.enumField | TestEnum.PEACOCK | WhichDoc.Both
        Field.enumField | null | WhichDoc.NewDoc
        Field.listField | [1,3,4,5] | WhichDoc.ThisDoc
        Field.listField | [1,3,4,5] | WhichDoc.NewDoc
        Field.listField | null | WhichDoc.NewDoc
        Field.listField | [] | WhichDoc.NewDoc
        Field.listField | [] | WhichDoc.ThisDoc
    }

}
