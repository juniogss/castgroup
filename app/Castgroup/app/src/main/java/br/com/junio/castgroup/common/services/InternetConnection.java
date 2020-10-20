package br.com.junio.castgroup.common.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

public class InternetConnection {

    public static boolean isConnected(Context context) {
        if (context == null) return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null)
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                    return true;
                else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                    return true;
                else return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
        }
        return false;
    }
}
