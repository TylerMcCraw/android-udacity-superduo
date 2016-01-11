package it.jaschke.alexandria.helpers;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Tyler McCraw on 1/8/16.
 */
public class Util {
    public static boolean isOnline(Context context)
    {
        try
        {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
