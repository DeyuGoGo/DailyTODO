package go.deyu.dailytodo.model;

import io.realm.Realm;
import io.realm.RealmMigration;

/**
 * Created by huangeyu on 15/11/2.
 */
public class MessageRMMigration implements RealmMigration {
    @Override
    public long execute(Realm realm, long version) {
        if (version == 0) {
            version++;
        }
        return version;
    }
}
