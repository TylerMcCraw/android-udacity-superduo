package barqsoft.footballscores.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import barqsoft.footballscores.R;
import barqsoft.footballscores.data.DatabaseContract;
import barqsoft.footballscores.helpers.Util;

/**
 * Created by yehya khaled on 3/2/2015.
 */
public class ScoresFetchService extends IntentService {
    public static final String LOG_TAG = "ScoresFetchService";

    public ScoresFetchService() {
        super("ScoresFetchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        getData(getApplicationContext().getString(R.string.timeframe_next_two_days));
        getData(getApplicationContext().getString(R.string.timeframe_past_two_days));

        return;
    }

    private void getData(String timeFrame) {
        //Creating fetch URL
        final String BASE_URL = getApplicationContext().getString(R.string.api_football_fixtures_url); //Base URL
        final String QUERY_TIME_FRAME = getApplicationContext().getString(R.string.api_timeframe_query); //Time Frame parameter to determine days
        //final String QUERY_MATCH_DAY = "matchday";

        Uri fetch_build = Uri.parse(BASE_URL).buildUpon().
                appendQueryParameter(QUERY_TIME_FRAME, timeFrame).build();
        //Log.v(LOG_TAG, "The url we are looking at is: "+fetch_build.toString()); //log spam
        HttpURLConnection m_connection = null;
        BufferedReader reader = null;
        String JSON_data = null;
        //Opening Connection
        try {
            URL fetch = new URL(fetch_build.toString());
            m_connection = (HttpURLConnection) fetch.openConnection();
            m_connection.setRequestMethod(getApplicationContext().getString(R.string.api_requestmethod_get));
            m_connection.addRequestProperty(getApplicationContext().getString(R.string.api_requestprop_x_auth_token), getString(R.string.api_key));
            m_connection.connect();

            // Read the input stream into a String
            InputStream inputStream = m_connection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            JSON_data = buffer.toString();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception here" + e.getMessage()); //NON-NLS
        } finally {
            if (m_connection != null) {
                m_connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error Closing Stream"); //NON-NLS
                }
            }
        }
        try {
            if (JSON_data != null) {
                //This bit is to check if the data contains any matches. If not, we call processJson on the dummy data
                JSONArray matches = new JSONObject(JSON_data).getJSONArray(getApplicationContext().getString(R.string.api_fixtures));
                if (matches.length() == 0) {
                    //if there is no data, call the function on dummy data
                    //this is expected behavior during the off season.
                    processJSONdata(getString(R.string.dummy_data), getApplicationContext(), false);
                    return;
                }


                processJSONdata(JSON_data, getApplicationContext(), true);
            } else {
                //Could not Connect
                Log.d(LOG_TAG, "Could not connect to server."); //NON-NLS
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    private void processJSONdata(String JSONdata, Context mContext, boolean isReal) {
        //JSON data
        final String SEASON_LINK = mContext.getString(R.string.api_url_soccerseasons);
        final String MATCH_LINK = mContext.getString(R.string.api_url_fixtures);
        final String FIXTURES = mContext.getString(R.string.api_fixtures);
        final String LINKS = mContext.getString(R.string.api_links);
        final String SOCCER_SEASON = mContext.getString(R.string.api_soccerseason);
        final String SELF = mContext.getString(R.string.api_self);
        final String MATCH_DATE = mContext.getString(R.string.api_date);
        final String HOME_TEAM = mContext.getString(R.string.api_hometeamname);
        final String AWAY_TEAM = mContext.getString(R.string.api_awayteamname);
        final String RESULT = mContext.getString(R.string.api_result);
        final String HOME_GOALS = mContext.getString(R.string.api_goalshometeam);
        final String AWAY_GOALS = mContext.getString(R.string.api_goalsawayteam);
        final String MATCH_DAY = mContext.getString(R.string.api_matchday);

        //Match data
        String League = null;
        String mDate = null;
        String mTime = null;
        String Home = null;
        String Away = null;
        String Home_goals = null;
        String Away_goals = null;
        String match_id = null;
        String match_day = null;

        try {
            JSONArray matches = new JSONObject(JSONdata).getJSONArray(FIXTURES);

            //ContentValues to be inserted
            Vector<ContentValues> values = new Vector<ContentValues>(matches.length());
            for (int i = 0; i < matches.length(); i++) {

                JSONObject match_data = matches.getJSONObject(i);
                League = match_data.getJSONObject(LINKS).getJSONObject(SOCCER_SEASON).
                        getString(mContext.getString(R.string.api_href));
                League = League.replace(SEASON_LINK, "");
                //This if statement controls which leagues we're interested in the data from.
                //add leagues here in order to have them be added to the DB.
                // If you are finding no data in the app, check that this contains all the leagues.
                // If it doesn't, that can cause an empty DB, bypassing the dummy data routine.
                if (League.equals(Integer.toString(Util.PREMIER_LEAGUE)) ||
                        League.equals(Integer.toString(Util.SERIE_A)) ||
                        League.equals(Integer.toString(Util.BUNDESLIGA1)) ||
                        League.equals(Integer.toString(Util.BUNDESLIGA2)) ||
                        League.equals(Integer.toString(Util.PRIMERA_DIVISION))) {
                    match_id = match_data.getJSONObject(LINKS).getJSONObject(SELF).
                            getString(mContext.getString(R.string.api_href));
                    match_id = match_id.replace(MATCH_LINK, "");
                    if (!isReal) {
                        //This if statement changes the match ID of the dummy data so that it all goes into the database
                        match_id = match_id + Integer.toString(i);
                    }

                    mDate = match_data.getString(MATCH_DATE);
                    mTime = mDate.substring(mDate.indexOf("T") + 1, mDate.indexOf("Z")); //NON-NLS
                    mDate = mDate.substring(0, mDate.indexOf("T")); //NON-NLS
                    SimpleDateFormat match_date = new SimpleDateFormat(mContext.getString(R.string.date_format_hms));
                    match_date.setTimeZone(TimeZone.getTimeZone(mContext.getString(R.string.timezone_utc)));
                    try {
                        Date parseddate = match_date.parse(mDate + mTime);
                        SimpleDateFormat new_date = new SimpleDateFormat(mContext.getString(R.string.date_format_hm));
                        new_date.setTimeZone(TimeZone.getDefault());
                        mDate = new_date.format(parseddate);
                        mTime = mDate.substring(mDate.indexOf(":") + 1);
                        mDate = mDate.substring(0, mDate.indexOf(":"));

                        if (!isReal) {
                            //This if statement changes the dummy data's date to match our current date range.
                            Date fragmentdate = new Date(System.currentTimeMillis() + ((i - 2) * 86400000));
                            SimpleDateFormat mformat = new SimpleDateFormat(getString(R.string.date_format));
                            mDate = mformat.format(fragmentdate);
                        }
                    } catch (Exception e) {
                        Log.d(LOG_TAG, "error here!"); //NON-NLS
                        Log.e(LOG_TAG, e.getMessage());
                    }
                    Home = match_data.getString(HOME_TEAM);
                    Away = match_data.getString(AWAY_TEAM);
                    Home_goals = match_data.getJSONObject(RESULT).getString(HOME_GOALS);
                    Away_goals = match_data.getJSONObject(RESULT).getString(AWAY_GOALS);
                    match_day = match_data.getString(MATCH_DAY);
                    ContentValues match_values = new ContentValues();
                    match_values.put(DatabaseContract.ScoresTable.MATCH_ID, match_id);
                    match_values.put(DatabaseContract.ScoresTable.DATE_COL, mDate);
                    match_values.put(DatabaseContract.ScoresTable.TIME_COL, mTime);
                    match_values.put(DatabaseContract.ScoresTable.HOME_COL, Home);
                    match_values.put(DatabaseContract.ScoresTable.AWAY_COL, Away);
                    match_values.put(DatabaseContract.ScoresTable.HOME_GOALS_COL, Home_goals);
                    match_values.put(DatabaseContract.ScoresTable.AWAY_GOALS_COL, Away_goals);
                    match_values.put(DatabaseContract.ScoresTable.LEAGUE_COL, League);
                    match_values.put(DatabaseContract.ScoresTable.MATCH_DAY, match_day);

                    values.add(match_values);
                }
            }
            int inserted_data = 0;
            ContentValues[] insert_data = new ContentValues[values.size()];
            values.toArray(insert_data);
            inserted_data = mContext.getContentResolver().bulkInsert(
                    DatabaseContract.BASE_CONTENT_URI, insert_data);

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }
}

