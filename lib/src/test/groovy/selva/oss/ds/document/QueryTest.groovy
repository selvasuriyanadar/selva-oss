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

class QueryTest extends Specification {

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

}
