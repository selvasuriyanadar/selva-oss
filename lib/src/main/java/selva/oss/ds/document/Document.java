package selva.oss.ds.document;

import static selva.oss.lang.CommonValidation.*;
import selva.oss.ds.document.datatype.DataTypeConfig;
import selva.oss.ds.document.datatype.TypedValue;

import java.util.*;

public interface Document<T> extends DocumentGetAndSetApi<T>, DocumentExistanceAndEqualityApi<T> {

}
