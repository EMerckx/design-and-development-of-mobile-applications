package be.ugent.oomt.labo4.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import be.ugent.oomt.labo4.contentprovider.database.DatabaseContract;
import be.ugent.oomt.labo4.contentprovider.database.DbHelper;


/**
 * Created by elias on 04/11/13.
 */
public class MessageProvider extends ContentProvider {

	// database
    private SQLiteDatabase sqlDB;

    // used for the UriMatcher
    private static final int MESSAGES = 10;
    private static final int MESSAGE_ID = 20;
    private static final String MESSAGES_PATH = "messages";
    private static final int CONTACTS = 30;
    private static final int CONTACT_ID = 40;
    private static final String CONTACTS_PATH = "contacts";

    // unique namespace for contentprovider (provider name)
    private static final String AUTHORITY = "be.ugent.oomt.labo4.contentprovider.MessageProvider";
    public static final Uri MESSAGES_CONTENT_URL = Uri.parse("content://" + AUTHORITY + "/" + MESSAGES_PATH);
    public static final Uri CONTACTS_CONTENT_URL = Uri.parse("content://" + AUTHORITY + "/" + CONTACTS_PATH);

    public static final String CONTENT_MESSAGES_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/messages";
    public static final String CONTENT_MESSAGES_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/message";
    public static final String CONTENT_CONTACTS_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/contacts";
    public static final String CONTENT_CONTACTS_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/contact";

    private static final UriMatcher sURIMatcher;
    static {
        sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sURIMatcher.addURI(AUTHORITY, MESSAGES_PATH, MESSAGES);
        sURIMatcher.addURI(AUTHORITY, MESSAGES_PATH + "/#", MESSAGE_ID);
        sURIMatcher.addURI(AUTHORITY, CONTACTS_PATH, CONTACTS);
        sURIMatcher.addURI(AUTHORITY, CONTACTS_PATH + "/#", CONTACT_ID);
    }

    @Override
    public boolean onCreate() {
        DbHelper dbHelper = new DbHelper(getContext());
        sqlDB = dbHelper.getWritableDatabase();
        return sqlDB != null;
    }

    public static void addTestData(Context context) {
        ContentValues values = new ContentValues();
        String contact = "test@ugent.be";

        // status update
        values.put(DatabaseContract.Contact.COLUMN_NAME_CONTACT, contact);
        values.put(DatabaseContract.Contact.COLUMN_NAME_STATE, "test state");
        context.getContentResolver().insert(MessageProvider.CONTACTS_CONTENT_URL, values);
        // message
        values = new ContentValues();
        values.put(DatabaseContract.Message.COLUMN_NAME_CONTACT, contact);
        values.put(DatabaseContract.Message.COLUMN_NAME_MESSAGE, "This is a test message.");
        context.getContentResolver().insert(MessageProvider.MESSAGES_CONTENT_URL, values);
        // 2nd message
        values = new ContentValues();
        values.put(DatabaseContract.Message.COLUMN_NAME_CONTACT, contact);
        values.put(DatabaseContract.Message.COLUMN_NAME_MESSAGE, "This is a second test message.");
        context.getContentResolver().insert(MessageProvider.MESSAGES_CONTENT_URL, values);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {

    	//String groupBy = DatabaseContract.Message._ID;
        // Using SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case MESSAGES:
                // Set the table
                queryBuilder.setTables(DatabaseContract.Message.TABLE_NAME);
                break;
            case MESSAGE_ID:
                // Set the table
                queryBuilder.setTables(DatabaseContract.Message.TABLE_NAME);
                // adding the ID to the original query
                queryBuilder.appendWhere(DatabaseContract.Message._ID + "=" + uri.getLastPathSegment());
                break;
            case CONTACTS:
                queryBuilder.setTables(DatabaseContract.Contact.TABLE_NAME);
                break;
            case CONTACT_ID:
                queryBuilder.setTables(DatabaseContract.Contact.TABLE_NAME);
                queryBuilder.appendWhere(DatabaseContract.Contact._ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(sqlDB, projection, selection, selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        long id;
        Uri returnUri;
        switch (uriType) {
            case MESSAGES:
                id = sqlDB.replace(DatabaseContract.Message.TABLE_NAME, null, values);
                returnUri = ContentUris.withAppendedId(MESSAGES_CONTENT_URL, id);
                break;
            case CONTACTS:
                id = sqlDB.replace(DatabaseContract.Contact.TABLE_NAME, null, values);
                returnUri = ContentUris.withAppendedId(CONTACTS_CONTENT_URL, id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        int rowsDeleted;
        String id;
        switch (uriType) {
            case MESSAGES:
                rowsDeleted = sqlDB.delete(DatabaseContract.Message.TABLE_NAME, selection, selectionArgs);
                break;
            case MESSAGE_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(DatabaseContract.Message.TABLE_NAME, DatabaseContract.Message._ID + "=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(DatabaseContract.Message.TABLE_NAME, DatabaseContract.Message._ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            case CONTACTS:
                rowsDeleted = sqlDB.delete(DatabaseContract.Contact.TABLE_NAME, selection, selectionArgs);
                break;
            case CONTACT_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(DatabaseContract.Contact.TABLE_NAME, DatabaseContract.Contact._ID + "=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(DatabaseContract.Contact.TABLE_NAME, DatabaseContract.Contact._ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        int rowsUpdated;
        String id;
        switch (uriType) {
            case MESSAGES:
                rowsUpdated = sqlDB.update(DatabaseContract.Message.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MESSAGE_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(DatabaseContract.Message.TABLE_NAME, values, DatabaseContract.Message._ID + "=" + id, null);
                } else {
                    rowsUpdated = sqlDB.update(DatabaseContract.Message.TABLE_NAME, values, DatabaseContract.Message._ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            case CONTACTS:
                rowsUpdated = sqlDB.update(DatabaseContract.Contact.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CONTACT_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(DatabaseContract.Contact.TABLE_NAME, values, DatabaseContract.Contact._ID + "=" + id, null);
                } else {
                    rowsUpdated = sqlDB.update(DatabaseContract.Contact.TABLE_NAME, values, DatabaseContract.Contact._ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    public String getType(Uri uri) {
        int uriType = sURIMatcher.match(uri);
        String type;
        switch (uriType) {
            case MESSAGES:
                type = CONTENT_MESSAGES_TYPE;
                break;
            case MESSAGE_ID:
                type = CONTENT_MESSAGES_ITEM_TYPE;
                break;
            case CONTACTS:
                type = CONTENT_CONTACTS_TYPE;
                break;
            case CONTACT_ID:
                type = CONTENT_CONTACTS_ITEM_TYPE;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return type;
    }
}
