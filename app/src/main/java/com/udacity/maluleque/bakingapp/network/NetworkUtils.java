package com.udacity.maluleque.bakingapp.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

public class NetworkUtils {
    /*
     *
     * This method checks if mobile has internet connection
     *
     * @param context   Android Context to access preferences and resources
     * */
    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network netInfo = cm.getActiveNetwork();

        if (netInfo == null) {
            return false;
        }

        NetworkCapabilities networkCapabilities =
                cm.getNetworkCapabilities(netInfo);

        return networkCapabilities != null
                && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }
}
