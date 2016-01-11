package barqsoft.footballscores.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tyler McCraw on 1/10/16.
 */
public class ScoreModel implements Parcelable {

    private String homeName;
    private String awayName;
    private String homeGoals;
    private String awayGoals;
    private String date;
    private String matchId;

    public ScoreModel(String homeName, String awayName, String homeGoals, String awayGoals, String date, String matchId) {
        this.homeName = homeName;
        this.awayName = awayName;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.date = date;
        this.matchId = matchId;
    }

    protected ScoreModel(Parcel in) {
        homeName = in.readString();
        awayName = in.readString();
        homeGoals = in.readString();
        awayGoals = in.readString();
        date = in.readString();
        matchId = in.readString();
    }

    public static final Creator<ScoreModel> CREATOR = new Creator<ScoreModel>() {
        @Override
        public ScoreModel createFromParcel(Parcel in) {
            return new ScoreModel(in);
        }

        @Override
        public ScoreModel[] newArray(int size) {
            return new ScoreModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(homeName);
        dest.writeString(awayName);
        dest.writeString(homeGoals);
        dest.writeString(awayGoals);
        dest.writeString(date);
        dest.writeString(matchId);
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getAwayName() {
        return awayName;
    }

    public void setAwayName(String awayName) {
        this.awayName = awayName;
    }

    public String getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(String homeGoals) {
        this.homeGoals = homeGoals;
    }

    public String getAwayGoals() {
        return awayGoals;
    }

    public void setAwayGoals(String awayGoals) {
        this.awayGoals = awayGoals;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }
}
