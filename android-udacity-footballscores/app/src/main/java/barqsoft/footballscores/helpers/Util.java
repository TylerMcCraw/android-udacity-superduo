package barqsoft.footballscores.helpers;

import android.content.Context;

import barqsoft.footballscores.R;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Util {

    // This set of league codes is for the 2015/2016 season. In fall of 2016, they will need to
    // be updated. Feel free to use the codes
    public static final int BUNDESLIGA = 351;
    public static final int CHAMPIONS_LEAGUE = 362;
    public static final int BUNDESLIGA1 = 394;
    public static final int BUNDESLIGA2 = 395;
    public static final int LIGUE1 = 396;
    public static final int LIGUE2 = 397;
    public static final int PREMIER_LEAGUE = 398;
    public static final int PRIMERA_DIVISION = 399;
    public static final int SEGUNDA_DIVISION = 400;
    public static final int SERIE_A = 401;
    public static final int PRIMERA_LIGA = 402;
    public static final int BUNDESLIGA3 = 403;
    public static final int EREDIVISIE = 404;

    public static String getLeague(Context context, int league_num) {
        switch (league_num) {
            case SERIE_A:
                return context.getString(R.string.league_name_serie_a);
            case PREMIER_LEAGUE:
                return context.getString(R.string.league_name_premier_league);
            case CHAMPIONS_LEAGUE:
                return context.getString(R.string.league_name_uefa_champions);
            case PRIMERA_DIVISION:
                return context.getString(R.string.league_name_primera_division);
            case BUNDESLIGA:
                return context.getString(R.string.league_name_bundesliga);
            default:
                return context.getString(R.string.unknown_league);
        }
    }

    public static String getMatchDay(Context context, int match_day, int league_num) {
        if (league_num == CHAMPIONS_LEAGUE) {
            if (match_day <= 6) {
                return context.getString(R.string.match_day_group_stages);
            } else if (match_day == 7 || match_day == 8) {
                return context.getString(R.string.match_day_first_knockout);
            } else if (match_day == 9 || match_day == 10) {
                return context.getString(R.string.match_day_quarterfinal);
            } else if (match_day == 11 || match_day == 12) {
                return context.getString(R.string.match_day_semifinal);
            } else {
                return context.getString(R.string.match_day_final);
            }
        } else {
            return context.getString(R.string.match_day_matchday) + String.valueOf(match_day);
        }
    }

    public static String getScores(int home_goals, int awaygoals) {
        if (home_goals < 0 || awaygoals < 0) {
            return " - ";
        } else {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName(Context context, String teamname) {
        final String teamNameArsenal = context.getString(R.string.team_name_arsenal);
        final String teamNameManchester = context.getString(R.string.team_name_manchester_united);
        final String teamNameSwanseaCity = context.getString(R.string.team_name_swansea_city);
        final String teamNameLeicesterCity = context.getString(R.string.team_name_leicester_city);
        final String teamNameEvertonFC = context.getString(R.string.team_name_everton_fc);
        final String teamNameWestHamUnitedFC = context.getString(R.string.team_name_west_ham_united);
        final String teamNameTottenhamHotspurFC = context.getString(R.string.team_name_tottenham_hotspur);
        final String teamNameWestBromwichAlbion = context.getString(R.string.team_name_west_bromwich_albion);
        final String teamNameSunderlandAFC = context.getString(R.string.team_name_sunderland_afc);
        final String teamNameStokeCityFC = context.getString(R.string.team_name_stoke_city);

        if (teamname == null) {
            return R.drawable.no_icon;
        }
        //This is the set of icons that are currently in the app. Feel free to find and add more
            //as you go.
        if (teamNameArsenal.equals(teamname)) {
            return R.drawable.arsenal;
        } else if (teamNameManchester.equals(teamname)) {
            return R.drawable.manchester_united;
        } else if (teamNameSwanseaCity.equals(teamname)) {
            return R.drawable.swansea_city_afc;
        } else if (teamNameLeicesterCity.equals(teamname)) {
            return R.drawable.leicester_city_fc_hd_logo;
        } else if (teamNameEvertonFC.equals(teamname)) {
            return R.drawable.everton_fc_logo1;
        } else if (teamNameWestHamUnitedFC.equals(teamname)) {
            return R.drawable.west_ham;
        } else if (teamNameTottenhamHotspurFC.equals(teamname)) {
            return R.drawable.tottenham_hotspur;
        } else if (teamNameWestBromwichAlbion.equals(teamname)) {
            return R.drawable.west_bromwich_albion_hd_logo;
        } else if (teamNameSunderlandAFC.equals(teamname)) {
            return R.drawable.sunderland;
        } else if (teamNameStokeCityFC.equals(teamname)) {
            return R.drawable.stoke_city;
        } else {
            return R.drawable.no_icon;
        }
    }
}
