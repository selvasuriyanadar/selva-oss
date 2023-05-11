package selva.oss.ds.document

import selva.oss.ds.condition.EqualsCondition;
import selva.oss.ds.condition.NotEqualsCondition;
import selva.oss.ds.condition.AndCondition;
import selva.oss.ds.condition.ConditionBase;
import selva.oss.ds.query.QueryLogic;
import selva.oss.ds.datatype.DataTypeConfig;
import selva.oss.ds.value.TypedValue;
import selva.oss.ds.field.FieldsConfig;
import selva.oss.ds.field.FieldConfig;
import selva.oss.ds.field.StringField;
import selva.oss.ds.fieldorvalue.FieldOrValue;

import spock.lang.Specification
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

class QueryTest extends Specification {

    private enum Field {
        stringField, longField, integerField, floatField, anotherLongField, anotherIntegerField
    }

    public static FieldsConfig getFieldsConfig() {
        return new FieldsConfig().add(Field.stringField, DataTypeConfig.createString()).add(Field.longField, DataTypeConfig.createLong()).add(Field.integerField, DataTypeConfig.createInteger()).add(Field.floatField, DataTypeConfig.createFloat()).add(Field.anotherLongField, DataTypeConfig.createLong()).add(Field.anotherIntegerField, DataTypeConfig.createInteger());
    }

    def "produce query assertion"() {
        setup:

        when:
        final String queryResult = QueryLogic.produceQuery(condition);
        System.out.println(queryResult);

        then:
        query.equals(queryResult)

        where:
        condition << [(ConditionBase) new EqualsCondition(new FieldOrValue(new FieldConfig(new StringField("name"), DataTypeConfig.createString())), new FieldOrValue(new FieldConfig(new StringField("location"), DataTypeConfig.createString()))),
            (ConditionBase) new AndCondition(Arrays.asList(new EqualsCondition(new FieldOrValue(new FieldConfig(new StringField("name"), DataTypeConfig.createString())), new FieldOrValue(new FieldConfig(new StringField("location"), DataTypeConfig.createString()))),
                new NotEqualsCondition(new FieldOrValue(new FieldConfig(new StringField("address"), DataTypeConfig.createString())), new FieldOrValue(new TypedValue("12 south street"))))),
            (ConditionBase) new EqualsCondition(new FieldOrValue(new FieldConfig(new StringField("long"), DataTypeConfig.createLong())), new FieldOrValue(new TypedValue(56L))),
            (ConditionBase) new EqualsCondition(new FieldOrValue(new FieldConfig(new StringField("float"), DataTypeConfig.createFloat())), new FieldOrValue(new TypedValue(56.57F))),
            (ConditionBase) new EqualsCondition(new FieldOrValue(new FieldConfig(new StringField("integer"), DataTypeConfig.createInteger())), new FieldOrValue(new TypedValue(56))),
            (ConditionBase) new EqualsCondition(new FieldOrValue(new FieldConfig(new StringField("double"), DataTypeConfig.createDouble())), new FieldOrValue(new TypedValue(56.57D)))]

        query << ["(name = location)",
            "((name = location) and (address != '12 south street'))",
            "(long = 56)",
            "(float = 56.57)",
            "(integer = 56)",
            "(double = 56.57)"]
    }

    def "produce from json"() {
        setup:
        final JsonObject jsonObject = new JsonParser().parse(json);
        final FieldsConfig fieldsConfig = getFieldsConfig();

        when:
        final String queryResult = QueryLogic.produceQuery(jsonObject, fieldsConfig);
        System.out.println(queryResult);

        then:
        query.equals(queryResult)

        where:
        json << [
        """
            {
                "condition": "And", "subConditions" : [
                    { "condition": "Equals", "lhsField": "stringField", "rhsValue": "Hello, World!" },
                    { "condition": "NotEquals", "lhsValue": 3.3334, "rhsField": "floatField" },
                    { "condition": "GreaterThan", "lhsField": "longField", "rhsField": "anotherLongField" },
                    { "condition": "GreaterThanOrEquals", "lhsField": "integerField", "rhsValue": 5 },
                    { "condition": "LesserThan", "lhsField": "floatField", "rhsValue": 33.24505 },
                    { "condition": "LesserThanOrEquals", "lhsField": "anotherIntegerField", "rhsField": "integerField" }
                ]
            }
        """,
        """
            {
                "condition": "And", "subConditions" : [
                    { "condition": "Equals", "lhsField": "stringField", "rhsValue": "Hello, World!" },
                    {
                        "condition": "Or", "subConditions": [
                            { "condition": "NotEquals", "lhsValue": 3.3334, "rhsField": "floatField" },
                            {
                                "condition": "Not", "subCondition": {
                                    "condition": "Equals", "lhsValue": "Hai! How are you?", "rhsField": "stringField"
                                }
                            }
                        ]
                    },
                    { "condition": "GreaterThan", "lhsField": "longField", "rhsField": "anotherLongField" },
                    { "condition": "GreaterThanOrEquals", "lhsField": "integerField", "rhsValue": 5 },
                    { "condition": "LesserThan", "lhsField": "floatField", "rhsValue": 33.24505 },
                    { "condition": "LesserThanOrEquals", "lhsField": "anotherIntegerField", "rhsField": "integerField" }
                ]
            }
        """
        ]

        query << [
            "((stringField = 'Hello, World!') and (3.3334 != floatField) and (longField > anotherLongField) and (integerField >= 5) and (floatField < 33.24505) and (anotherIntegerField <= integerField))",
            "((stringField = 'Hello, World!') and ((3.3334 != floatField) or (not ('Hai! How are you?' = stringField))) and (longField > anotherLongField) and (integerField >= 5) and (floatField < 33.24505) and (anotherIntegerField <= integerField))"
        ]
    }

}
