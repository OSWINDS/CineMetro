package cinemetroproject.cinemetro.util;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

/**
 * Extends SQLiteOpenHelper and is responsible for creating the tables of the db,
 * updating the db and deleting the db.
 */
public class dbHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "CineMetroDB";

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;

        // SQL statement to create station table
        query = "CREATE TABLE station ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "description TEXT )" +
                "route_id INTEGER" +
                "colour INTEGER";

        // create station table
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older station table if existed
        db.execSQL("DROP TABLE IF EXISTS station");

        // create fresh station table
        this.onCreate(db);
    }
}
