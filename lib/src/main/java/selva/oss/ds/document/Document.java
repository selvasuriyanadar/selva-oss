package selva.oss.ds.document;

import static selva.oss.lang.Commons.*;
import selva.oss.ds.datatype.DataTypeConfig;
import selva.oss.ds.datatype.TypedValue;

import java.util.*;

public interface Document<T> extends DocumentGetAndSetApi<T>, DocumentExistanceAndEqualityApi<T> {

}
