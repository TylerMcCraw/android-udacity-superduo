package barqsoft.footballscores.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by yehya khaled on 2/25/2015.
 */
public class ScoresProvider extends ContentProvider {
    private static ScoresDBHelper mOpenHelper;
    private static final int MATCHES = 100;
    private static final int MATCHES_WITH_LEAGUE = 101;
    private static final int MATCHES_WITH_ID = 102;
    private static final int MATCHES_WITH_DATE = 103;
    private UriMatcher mUriMatcher = buildUriMatcher();
    private static final String SCORES_BY_LEAGUE = DatabaseContract.ScoresTable.LEAGUE_COL + " = ?"; //NON-NLS
    private static final String SCORES_BY_DATE = DatabaseContract.ScoresTable.DATE_COL + " LIKE ?"; //NON-NLS
    private static final String SCORES_BY_ID = DatabaseContract.ScoresTable.MATCH_ID + " = ?"; //NON-NLS


    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.BASE_CONTENT_URI.toString();
        matcher.addURI(authority, null, MATCHES);
        matcher.addURI(authority, DatabaseContract.ScoresTable.LEAGUE_COL, MATCHES_WITH_LEAGUE);
        matcher.addURI(authority, DatabaseContract.ScoresTable.ID_COL, MATCHES_WITH_ID);
        matcher.addURI(authority, DatabaseContract.ScoresTable.DATE_COL, MATCHES_WITH_DATE);
        return matcher;
    }

    private int match_uri(Uri uri) {
        String link = uri.toString();
        {
            if (link.contentEquals(DatabaseContract.BASE_CONTENT_URI.toString())) {
                return MATCHES;
            } else if (link.contentEquals(DatabaseContract.ScoresTable.buildScoreWithDate().toString())) {
                return MATCHES_WITH_DATE;
            } else if (link.contentEquals(DatabaseContract.ScoresTable.buildScoreWithId().toString())) {
                return MATCHES_WITH_ID;
            } else if (link.contentEquals(DatabaseContract.ScoresTable.buildScoreWithLeague().toString())) {
                return MATCHES_WITH_LEAGUE;
            }
        }
        return -1;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ScoresDBHelper(getContext());
        return false;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case MATCHES:
                return DatabaseContract.ScoresTable.CONTENT_TYPE;
            case MATCHES_WITH_LEAGUE:
                return DatabaseContract.ScoresTable.CONTENT_TYPE;
            case MATCHES_WITH_ID:
                return DatabaseContract.ScoresTable.CONTENT_ITEM_TYPE;
            case MATCHES_WITH_DATE:
                return DatabaseContract.ScoresTable.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri :" + uri); //NON-NLS
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        int match = match_uri(uri);
        switch (match) {
            case MATCHES:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.SCORES_TABLE,
                        projection, null, null, null, null, sortOrder);
                break;
            case MATCHES_WITH_DATE:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.SCORES_TABLE,
                        projection, SCORES_BY_DATE, selectionArgs, null, null, sortOrder);
                break;
            case MATCHES_WITH_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.SCORES_TABLE,
                        projection, SCORES_BY_ID, selectionArgs, null, null, sortOrder);
                break;
            case MATCHES_WITH_LEAGUE:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.SCORES_TABLE,
                        projection, SCORES_BY_LEAGUE, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri); //NON-NLS
        }
        // getContext().getContentResolver() could be null
        if (getContext() != null && getContext().getContentResolver() != null) {
            retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return retCursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        return null;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        switch (match_uri(uri)) {
            case MATCHES:
                db.beginTransaction();
                int returncount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(DatabaseContract.SCORES_TABLE, null, value,
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1) {
                            returncount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                // getContext().getContentResolver() could be null
                if (getContext() != null && getContext().getContentResolver() != null) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return returncount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }
}
