package au.com.roadhouse.rxdbflow.sql.language;

import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.Model;

public interface RxTransformable<T extends Model> {
    RxWhere<T> groupBy(NameAlias... nameAliases);

    RxWhere<T> groupBy(IProperty... properties);

    RxWhere<T> orderBy(NameAlias nameAlias, boolean ascending);

    RxWhere<T> orderBy(IProperty property, boolean ascending);

    RxWhere<T> orderBy(OrderBy orderBy);

    RxWhere<T> limit(int count);

    RxWhere<T> offset(int offset);

    RxWhere<T> having(SQLCondition... conditions);
}
