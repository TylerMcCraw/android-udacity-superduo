package barqsoft.footballscores.helpers;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import barqsoft.footballscores.R;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ViewHolder {
    private TextView homeName;
    private TextView awayName;
    private TextView score;
    private TextView date;
    private ImageView homeCrest;
    private ImageView awayCrest;
    private double matchId;

    public ViewHolder(View view) {
        homeName = (TextView) view.findViewById(R.id.home_name);
        awayName = (TextView) view.findViewById(R.id.away_name);
        score = (TextView) view.findViewById(R.id.score_textview);
        date = (TextView) view.findViewById(R.id.data_textview);
        homeCrest = (ImageView) view.findViewById(R.id.home_crest);
        awayCrest = (ImageView) view.findViewById(R.id.away_crest);
    }

    public TextView getHomeName() {
        return homeName;
    }

    public TextView getAwayName() {
        return awayName;
    }

    public TextView getScore() {
        return score;
    }

    public TextView getDate() {
        return date;
    }

    public ImageView getHomeCrest() {
        return homeCrest;
    }

    public ImageView getAwayCrest() {
        return awayCrest;
    }

    public double getMatchId() {
        return matchId;
    }

    public void setMatchId(double matchId) {
        this.matchId = matchId;
    }
}
