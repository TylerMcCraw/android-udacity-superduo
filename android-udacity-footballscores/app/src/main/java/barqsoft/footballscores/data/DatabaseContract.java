package barqsoft.footballscores.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by yehya khaled on 2/25/2015.
 */
public class DatabaseContract {
    public static final String SCORES_TABLE = "scores_table"; //NON-NLS

    //URI data
    public static final String CONTENT_AUTHORITY = "barqsoft.footballscores"; //NON-NLS
    public static final String PATH = "scores"; //NON-NLS
    public static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY); //NON-NLS

    public static final class ScoresTable implements BaseColumns {
        //Table data
        public static final String ID_COL = "id";
        public static final String LEAGUE_COL = "league"; //NON-NLS
        public static final String DATE_COL = "date"; //NON-NLS
        public static final String TIME_COL = "time"; //NON-NLS
        public static final String HOME_COL = "home"; //NON-NLS
        public static final String AWAY_COL = "away"; //NON-NLS
        public static final String HOME_GOALS_COL = "home_goals"; //NON-NLS
        public static final String AWAY_GOALS_COL = "away_goals"; //NON-NLS
        public static final String MATCH_ID = "match_id"; //NON-NLS
        public static final String MATCH_DAY = "match_day"; //NON-NLS

        //Types
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;

        public static Uri buildScoreWithLeague() {
            return BASE_CONTENT_URI.buildUpon().appendPath(LEAGUE_COL).build();
        }

        public static Uri buildScoreWithId() {
            return BASE_CONTENT_URI.buildUpon().appendPath(ID_COL).build(); //NON-NLS
        }

        public static Uri buildScoreWithDate() {
            return BASE_CONTENT_URI.buildUpon().appendPath(DATE_COL).build();
        }
    }
}
