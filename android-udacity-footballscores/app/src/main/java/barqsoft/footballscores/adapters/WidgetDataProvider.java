package barqsoft.footballscores.adapters;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import barqsoft.footballscores.R;
import barqsoft.footballscores.activities.MainActivity;
import barqsoft.footballscores.data.ScoreModel;
import barqsoft.footballscores.helpers.Util;
import barqsoft.footballscores.receivers.WidgetProvider;
import barqsoft.footballscores.services.ScoresFetchService;

public class WidgetDataProvider implements RemoteViewsFactory {

    private static final String LOG_TAG = "WidgetDataProvider"; //NON-NLS
    private static final int mCount = 10;
    private ArrayList<ScoreModel> mWidgetItems = new ArrayList<>();
    private Context mContext;
    private int mAppWidgetId;
    // 86400000 ms = 24 hours
    private int updateFrequency = 86400000;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

        remoteView.setTextViewText(R.id.home_name, mWidgetItems.get(position).getHomeName());
        remoteView.setTextViewText(R.id.away_name, mWidgetItems.get(position).getAwayName());
        remoteView.setTextViewText(R.id.data_textview, mWidgetItems.get(position).getDate());
        if (mWidgetItems.get(position).getHomeGoals() != null &&
                mWidgetItems.get(position).getAwayGoals() != null) {
            remoteView.setTextViewText(R.id.score_textview,
                    Util.getScores(Integer.parseInt(mWidgetItems.get(position).getHomeGoals()),
                            Integer.parseInt(mWidgetItems.get(position).getAwayGoals())));
        }
        remoteView.setImageViewResource(R.id.home_crest, Util.getTeamCrestByTeamName(mContext, mWidgetItems.get(position).getHomeName()));
        remoteView.setImageViewResource(R.id.away_crest, Util.getTeamCrestByTeamName(mContext, mWidgetItems.get(position).getAwayName()));

        Intent appIntent = new Intent(mContext, MainActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(mContext, 0, appIntent, 0);
        remoteView.setOnClickPendingIntent(R.id.item_container, appPendingIntent);

        return remoteView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {
    }

    private void updateScores() {
        Intent service_start = new Intent(mContext, ScoresFetchService.class);
        mContext.startService(service_start);
    }

    private void initData() {
        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.

        // TODO: The following needs to be moved to a common util class so that
        // TODO:   both ScoresFetchService and this implementation are consolidated.
        // TODO: For the future, we could set up the WidgetService and a CursorLoader to load this data (I think).
        fetchDataFromAPI(mContext.getString(R.string.timeframe_next_two_days));
        fetchDataFromAPI(mContext.getString(R.string.timeframe_past_two_days));
    }

    private void fetchDataFromAPI(String timeFrame) {
        //Creating fetch URL
        final String BASE_URL = mContext.getString(R.string.api_football_fixtures_url); //Base URL
        final String QUERY_TIME_FRAME = mContext.getString(R.string.api_timeframe_query); //Time Frame parameter to determine days

        Uri fetch_build = Uri.parse(BASE_URL).buildUpon().
                appendQueryParameter(QUERY_TIME_FRAME, timeFrame).build();
        HttpURLConnection m_connection = null;
        BufferedReader reader = null;
        String JSON_data = null;
        //Opening Connection
        try {
            URL fetch = new URL(fetch_build.toString());
            m_connection = (HttpURLConnection) fetch.openConnection();
            m_connection.setRequestMethod(mContext.getString(R.string.api_requestmethod_get));
            m_connection.addRequestProperty(mContext.getString(R.string.api_requestprop_x_auth_token), mContext.getString(R.string.api_key));
            m_connection.connect();

            Log.d(LOG_TAG, "fetchDataFromAPI: URL: " + fetch.toString()); //NON-NLS

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
                JSONArray matches = new JSONObject(JSON_data).getJSONArray(mContext.getString(R.string.api_fixtures));
                if (matches.length() == 0) {
                    //if there is no data, call the function on dummy data
                    //this is expected behavior during the off season.
                    processJSONdata(mContext.getString(R.string.dummy_data), mContext.getApplicationContext(), false);
                    return;
                }


                processJSONdata(JSON_data, mContext.getApplicationContext(), true);
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
        String leagueStr = null;
        String dateStr = null;
        String timeStr = null;
        String homeStr = null;
        String awayStr = null;
        String homeGoalsStr = null;
        String awayGoalsStr = null;
        String matchIdStr = null;
        String matchDayStr = null;

        try {
            JSONArray matches = new JSONObject(JSONdata).getJSONArray(FIXTURES);

            //ContentValues to be inserted
            for (int i = 0; i < matches.length(); i++) {
                JSONObject match_data = matches.getJSONObject(i);
                leagueStr = match_data.getJSONObject(LINKS).getJSONObject(SOCCER_SEASON).
                        getString(mContext.getString(R.string.api_href));
                leagueStr = leagueStr.replace(SEASON_LINK, "");
                //This if statement controls which leagues we're interested in the data from.
                //add leagues here in order to have them be added to the DB.
                // If you are finding no data in the app, check that this contains all the leagues.
                // If it doesn't, that can cause an empty DB, bypassing the dummy data routine.
                if (leagueStr.equals(Integer.toString(Util.PREMIER_LEAGUE)) ||
                        leagueStr.equals(Integer.toString(Util.SERIE_A)) ||
                        leagueStr.equals(Integer.toString(Util.BUNDESLIGA1)) ||
                        leagueStr.equals(Integer.toString(Util.BUNDESLIGA2)) ||
                        leagueStr.equals(Integer.toString(Util.PRIMERA_DIVISION))) {
                    matchIdStr = match_data.getJSONObject(LINKS).getJSONObject(SELF).getString(mContext.getString(R.string.api_href));
                    matchIdStr = matchIdStr.replace(MATCH_LINK, "");
                    if (!isReal) {
                        //This if statement changes the match ID of the dummy data so that it all goes into the database
                        matchIdStr = matchIdStr + Integer.toString(i);
                    }

                    dateStr = match_data.getString(MATCH_DATE);
                    timeStr = dateStr.substring(dateStr.indexOf("T") + 1, dateStr.indexOf("Z")); //NON-NLS
                    dateStr = dateStr.substring(0, dateStr.indexOf("T")); //NON-NLS
                    SimpleDateFormat match_date = new SimpleDateFormat(mContext.getString(R.string.date_format_hms));
                    match_date.setTimeZone(TimeZone.getTimeZone(mContext.getString(R.string.timezone_utc)));
                    try {
                        Date parseddate = match_date.parse(dateStr + timeStr);
                        SimpleDateFormat new_date = new SimpleDateFormat(mContext.getString(R.string.date_format_hm));
                        new_date.setTimeZone(TimeZone.getDefault());
                        dateStr = new_date.format(parseddate);
                        timeStr = dateStr.substring(dateStr.indexOf(":") + 1);
                        dateStr = dateStr.substring(0, dateStr.indexOf(":"));

                        if (!isReal) {
                            //This if statement changes the dummy data's date to match our current date range.
                            Date fragmentdate = new Date(System.currentTimeMillis() + ((i - 2) * 86400000));
                            SimpleDateFormat mformat = new SimpleDateFormat(mContext.getString(R.string.date_format));
                            dateStr = mformat.format(fragmentdate);
                        }
                    } catch (Exception e) {
                        Log.d(LOG_TAG, "error here!"); //NON-NLS
                        Log.e(LOG_TAG, e.getMessage());
                    }
                    homeStr = match_data.getString(HOME_TEAM);
                    awayStr = match_data.getString(AWAY_TEAM);
                    homeGoalsStr = match_data.getJSONObject(RESULT).getString(HOME_GOALS);
                    awayGoalsStr = match_data.getJSONObject(RESULT).getString(AWAY_GOALS);
                    matchDayStr = match_data.getString(MATCH_DAY);

                    mWidgetItems.add(new ScoreModel(homeStr, awayStr, homeGoalsStr,
                            awayGoalsStr, dateStr, matchIdStr));
                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }
}