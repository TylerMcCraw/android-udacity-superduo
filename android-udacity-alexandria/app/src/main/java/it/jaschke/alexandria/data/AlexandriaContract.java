package it.jaschke.alexandria.data;

/**
 * Created by saj on 22/12/14.
 */

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class AlexandriaContract {

    public static final String CONTENT_AUTHORITY = "it.jaschke.alexandria"; //NON-NLS

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY); //NON-NLS

    public static final String PATH_BOOKS = "books"; //NON-NLS
    public static final String PATH_AUTHORS = "authors"; //NON-NLS
    public static final String PATH_CATEGORIES = "categories"; //NON-NLS

    public static final String PATH_FULLBOOK = "fullbook"; //NON-NLS

    public static final class BookEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOOKS).build();

        public static final Uri FULL_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FULLBOOK).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS; //NON-NLS
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS; //NON-NLS

        public static final String TABLE_NAME = "books"; //NON-NLS

        public static final String TITLE = "title"; //NON-NLS

        public static final String IMAGE_URL = "imgurl"; //NON-NLS

        public static final String SUBTITLE = "subtitle"; //NON-NLS

        public static final String DESC = "description"; //NON-NLS

        public static Uri buildBookUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildFullBookUri(long id) {
            return ContentUris.withAppendedId(FULL_CONTENT_URI, id);
        }

    }

    public static final class AuthorEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_AUTHORS).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_AUTHORS; //NON-NLS
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_AUTHORS; //NON-NLS

        public static final String TABLE_NAME = "authors"; //NON-NLS

        public static final String AUTHOR = "author"; //NON-NLS

        public static Uri buildAuthorUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class CategoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORIES).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORIES; //NON-NLS
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORIES; //NON-NLS

        public static final String TABLE_NAME = "categories"; //NON-NLS

        public static final String CATEGORY = "category"; //NON-NLS

        public static Uri buildCategoryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}