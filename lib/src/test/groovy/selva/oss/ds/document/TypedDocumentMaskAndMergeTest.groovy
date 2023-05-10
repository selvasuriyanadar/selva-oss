package selva.oss.ds.document

import selva.oss.ds.datatype.DataTypeConfig;
import selva.oss.ds.datatype.TypedValue;

import spock.lang.*

class TypedDocumentMaskAndMergeTest extends Specification {

    private enum Field {
        stringField, longField, integerField, floatField, enumField, listField
    }

    private enum TestEnum {
        CAT, DOG, PARROT, PEACOCK
    }

    private enum MaskField1 {
        stringField, integerField, floatField, enumField, listField
    }

    private enum MaskField2 {
        stringField, longField, floatField, enumField, listField
    }

    private enum WhichDoc {
        ThisDoc, NewDoc, Both
    }

    private static TypedDocument initialisingWithFieldsConfig() {
        return new TypedDocument(Field.class, new FieldsConfig().add(Field.stringField, DataTypeConfig.createString()).add(Field.longField, DataTypeConfig.createLong()).add(Field.integerField, DataTypeConfig.createInteger()).add(Field.floatField, DataTypeConfig.createFloat()).add(Field.enumField, DataTypeConfig.createEnum(TestEnum.class)).add(Field.listField, DataTypeConfig.createList(DataTypeConfig.createInteger())));
    }

    private static <T extends Enum<T>, U extends Enum<U>> Optional<T> convertField(Class<T> fieldType, U field) {
        try {
            return Optional.of(Enum.valueOf(fieldType, field.toString()))
        }
        catch (IllegalArgumentException e) {
            return Optional.empty()
        }
    }

    @Shared _data = [[[key:Field.stringField, value:"dkjdkfjk"], [key:Field.longField, value:83483L], [key:Field.integerField, value:38383], [key:Field.floatField, value:343.44F], [key:Field.enumField, value:TestEnum.PARROT], [key:Field.listField, value:[3,4,2,1]], [key:Field.listField, value:[]]],
    [[key:Field.stringField, value:"dfeiekek"], [key:Field.longField, value:74848L], [key:Field.integerField, value:33734], [key:Field.floatField, value:793.14F], [key:Field.enumField, value:TestEnum.DOG], [key:Field.listField, value:[3,9,3,1]], [key:Field.listField, value:[]]],
    [[key:Field.stringField, value:"qweoojrm"], [key:Field.longField, value:66593L], [key:Field.integerField, value:16388], [key:Field.floatField, value:923.04F], [key:Field.enumField, value:TestEnum.CAT], [key:Field.listField, value:[0,4,0,1]], [key:Field.listField, value:[]]],
    [[key:Field.stringField, value:"uijppslll"], [key:Field.longField, value:82992L], [key:Field.integerField, value:223343], [key:Field.floatField, value:23.3998F], [key:Field.enumField, value:TestEnum.PEACOCK], [key:Field.listField, value:[3,9,3,1]], [key:Field.listField, value:[]]]]

    def "verifying mask"() {
        setup:
        final TypedDocument<Field> typedDocument = initialisingWithFieldsConfig();
        data.each(entry -> typedDocument.setValue(entry.key, entry.value));

        when:
        def TypedDocument typedDocumentMasked = new TypedDocument(typeClass, typedDocument)

        then:
        notThrown(DocumentBase.ConfigDoesNotExistException)
        data.each(entry -> {
            if (!convertField(typeClass, entry.key).isPresent()) {
                return;
            }
            assert typedDocumentMasked.isPresent(convertField(typeClass, entry.key).get())
            assert typedDocument.isEqual(typedDocumentMasked, entry.key)
        })

        where:
        data << _data
        typeClass << [MaskField1.class, MaskField2.class, MaskField1.class, MaskField2.class]
    }

    def "verifying merge"() {
        setup:
        final TypedDocument<Field> typedDocument = initialisingWithFieldsConfig();
        data.each(entry -> typedDocument.setValue(entry.key, entry.value));
        final TypedDocument typedDocumentMasked = new TypedDocument(typeClass, typedDocument)
        newData.each(entry -> typedDocumentMasked.setValue(entry.key, entry.value));

        when:
        typedDocument.merge(typedDocumentMasked)

        then:
        notThrown(DocumentLogic.InvalidDocumentFieldException)
        newData.each(entry -> { 
            if (!convertField(Field.class, entry.key).isPresent()) {
                return;
            }

            assert typedDocument.isPresent(convertField(Field.class, entry.key).get())
            assert typedDocumentMasked.isEqual(typedDocument, entry.key)
        })

        where:
        data << _data
        newData << [[[key:MaskField1.stringField, value:"dkjfieii"], [key:MaskField1.integerField, value:17983], [key:MaskField1.floatField, value:78811.0292F], [key:MaskField1.enumField, value:TestEnum.CAT], [key:MaskField1.listField, value:[3,9,1,1,7]], [key:Field.listField, value:[]]],
        [[key:MaskField2.stringField, value:"aayyrjrj"], [key:MaskField2.longField, value:119987L], [key:MaskField2.floatField, value:3387289.9884373F], [key:MaskField2.enumField, value:TestEnum.DOG], [key:MaskField2.listField, value:[2,2,9,8,7]], [key:Field.listField, value:[]]],
        [[key:MaskField1.stringField, value:"vaqlmaalll"], [key:MaskField1.integerField, value:9819177], [key:MaskField1.floatField, value:78844442.8829292F], [key:MaskField1.enumField, value:TestEnum.PARROT], [key:MaskField1.listField, value:[1,8,9,1,7]], [key:Field.listField, value:[]]],
        [[key:MaskField2.stringField, value:"iyyuddhehe"], [key:MaskField2.longField, value:9819177L], [key:MaskField2.floatField, value:111000.8829292F], [key:MaskField2.enumField, value:TestEnum.PEACOCK], [key:MaskField2.listField, value:[3,4,2,9,7]], [key:Field.listField, value:[]]]]
        typeClass << [MaskField1.class, MaskField2.class, MaskField1.class, MaskField2.class]
    }

}
