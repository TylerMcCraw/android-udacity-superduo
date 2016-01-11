package it.jaschke.alexandria.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by saj on 22/12/14.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "alexandria.db"; //NON-NLS

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_BOOK_TABLE = "CREATE TABLE " + AlexandriaContract.BookEntry.TABLE_NAME + " (" + //NON-NLS
                AlexandriaContract.BookEntry._ID + " INTEGER PRIMARY KEY," + //NON-NLS
                AlexandriaContract.BookEntry.TITLE + " TEXT NOT NULL," + //NON-NLS
                AlexandriaContract.BookEntry.SUBTITLE + " TEXT ," + //NON-NLS
                AlexandriaContract.BookEntry.DESC + " TEXT ," + //NON-NLS
                AlexandriaContract.BookEntry.IMAGE_URL + " TEXT, " + //NON-NLS
                "UNIQUE (" + AlexandriaContract.BookEntry._ID + ") ON CONFLICT IGNORE)"; //NON-NLS NON-NLS

        final String SQL_CREATE_AUTHOR_TABLE = "CREATE TABLE " + AlexandriaContract.AuthorEntry.TABLE_NAME + " (" + //NON-NLS
                AlexandriaContract.AuthorEntry._ID + " INTEGER," + //NON-NLS
                AlexandriaContract.AuthorEntry.AUTHOR + " TEXT," + //NON-NLS
                " FOREIGN KEY (" + AlexandriaContract.AuthorEntry._ID + ") REFERENCES " + //NON-NLS NON-NLS
                AlexandriaContract.BookEntry.TABLE_NAME + " (" + AlexandriaContract.BookEntry._ID + "))";

        final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + AlexandriaContract.CategoryEntry.TABLE_NAME + " (" + //NON-NLS
                AlexandriaContract.CategoryEntry._ID + " INTEGER," + //NON-NLS
                AlexandriaContract.CategoryEntry.CATEGORY + " TEXT," + //NON-NLS
                " FOREIGN KEY (" + AlexandriaContract.CategoryEntry._ID + ") REFERENCES " + //NON-NLS
                AlexandriaContract.BookEntry.TABLE_NAME + " (" + AlexandriaContract.BookEntry._ID + "))";


        Log.d("sql-statements", SQL_CREATE_BOOK_TABLE); //NON-NLS
        Log.d("sql-statements", SQL_CREATE_AUTHOR_TABLE); //NON-NLS
        Log.d("sql-statements", SQL_CREATE_CATEGORY_TABLE); //NON-NLS

        db.execSQL(SQL_CREATE_BOOK_TABLE);
        db.execSQL(SQL_CREATE_AUTHOR_TABLE);
        db.execSQL(SQL_CREATE_CATEGORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: handle onUpdgrade gracefully
    }
}
