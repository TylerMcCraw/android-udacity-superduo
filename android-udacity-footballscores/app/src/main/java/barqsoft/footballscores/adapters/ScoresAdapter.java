package barqsoft.footballscores.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import barqsoft.footballscores.R;
import barqsoft.footballscores.helpers.Util;
import barqsoft.footballscores.helpers.ViewHolder;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ScoresAdapter extends CursorAdapter {
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DATE = 1;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;
    public double detailMatchId = 0;
    private String FOOTBALL_SCORES_HASHTAG = mContext.getString(R.string.football_scores_hashtag);

    public ScoresAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View mItem = LayoutInflater.from(context).inflate(R.layout.scores_list_item, parent, false);
        ViewHolder mHolder = new ViewHolder(mItem);
        mItem.setTag(mHolder);
        return mItem;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder mHolder = (ViewHolder) view.getTag();
        mHolder.getHomeName().setText(cursor.getString(COL_HOME));
        mHolder.getAwayName().setText(cursor.getString(COL_AWAY));
        mHolder.getDate().setText(cursor.getString(COL_MATCHTIME));
        mHolder.getScore().setText(Util.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS)));
        mHolder.setMatchId(cursor.getDouble(COL_ID));
        mHolder.getHomeCrest().setImageResource(Util.getTeamCrestByTeamName(mContext,
                cursor.getString(COL_HOME)));
        mHolder.getAwayCrest().setImageResource(Util.getTeamCrestByTeamName(mContext,
                cursor.getString(COL_AWAY)
        ));
        LayoutInflater vi = (LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.detail_fragment, null);
        ViewGroup container = (ViewGroup) view.findViewById(R.id.details_fragment_container);
        if (mHolder.getMatchId() == detailMatchId) {
            container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT));
            TextView match_day = (TextView) v.findViewById(R.id.matchday_textview);
            match_day.setText(Util.getMatchDay(mContext, cursor.getInt(COL_MATCHDAY),
                    cursor.getInt(COL_LEAGUE)));
            TextView league = (TextView) v.findViewById(R.id.league_textview);
            league.setText(Util.getLeague(context, cursor.getInt(COL_LEAGUE)));
            Button share_button = (Button) v.findViewById(R.id.share_button);
            share_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add Share Action
                    context.startActivity(createShareForecastIntent(mHolder.getHomeName().getText() + " "
                            + mHolder.getScore().getText() + " " + mHolder.getAwayName().getText() + " "));
                }
            });
        } else {
            container.removeAllViews();
        }

    }

    public Intent createShareForecastIntent(String ShareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shareIntent.setType(mContext.getString(R.string.share_intent_type));
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + FOOTBALL_SCORES_HASHTAG);
        return shareIntent;
    }
}
