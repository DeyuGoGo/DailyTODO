package go.deyu.dailytodo.dbh;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import go.deyu.dailytodo.data.NotificationMessage;

/**
 * Created by huangeyu on 15/5/20.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "message.db";
    private static final int DATABASE_VERSION = 3;
    private final String LOG_NAME = getClass().getName();

    private Dao<NotificationMessage, Integer> thingDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, NotificationMessage.class);
        } catch (SQLException e) {
            Log.e(LOG_NAME, "Could not create new table for Thing", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, NotificationMessage.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            Log.e(LOG_NAME, "Could not upgrade the table for Thing", e);
        }
    }

    public Dao<NotificationMessage, Integer> getNotificationMessageDao() throws SQLException {
        if (thingDao == null) {
            thingDao = getDao(NotificationMessage.class);
        }
        return thingDao;
    }

}
