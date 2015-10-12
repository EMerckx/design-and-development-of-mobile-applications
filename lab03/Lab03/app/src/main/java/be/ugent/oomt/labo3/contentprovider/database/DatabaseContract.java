package be.ugent.oomt.labo3.contentprovider.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by elias on 04/11/13.
 */
public abstract class DatabaseContract {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "labo3.db";

    public static void onCreate(SQLiteDatabase db) {
        Contact.onCreate(db);
        Message.onCreate(db);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(Message.class.getName(), String.format("Upgrading database from version %d to %d, which will destroy all old data", oldVersion, newVersion));
        Message.onUpgrade(db, oldVersion, newVersion);
        Contact.onUpgrade(db, oldVersion, newVersion);
    }

    public static abstract class Contact implements BaseColumns {

        // Database table
        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_NAME_CONTACT = _ID;
        public static final String COLUMN_NAME_STATE = "state";
        public static final String COLUMN_NAME_LAST_UPDATE = "last_update";

        // Database creation SQL statement
        private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
                COLUMN_NAME_CONTACT + " TEXT PRIMARY KEY, " +
                COLUMN_NAME_STATE + " TEXT NOT NULL, " +
                COLUMN_NAME_LAST_UPDATE + " DATETIME DEFAULT CURRENT_TIMESTAMP " +
                " )";

        public static void onCreate(SQLiteDatabase database) {
            database.execSQL(DATABASE_CREATE);
        }

        public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(database);
        }
    }

    /* Inner class that defines the table contents */
    public static abstract class Message implements BaseColumns {

        // Database table
        public static final String TABLE_NAME = "messages";
        public static final String COLUMN_NAME_CONTACT = "contact_ref";
        public static final String COLUMN_NAME_MESSAGE = "message";
        public static final String COLUMN_NAME_DATE = "date";
        
        // Database creation SQL statement
        private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME_CONTACT + " TEXT NOT NULL, " +
                    COLUMN_NAME_MESSAGE + " TEXT, " +
                    COLUMN_NAME_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    " FOREIGN KEY (" + COLUMN_NAME_CONTACT + ") REFERENCES " + Contact.TABLE_NAME + " (" + Contact._ID + ")" +
                " )";

        public static void onCreate(SQLiteDatabase database) {
            database.execSQL(DATABASE_CREATE);
        }

        public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(database);
        }
    }
}
