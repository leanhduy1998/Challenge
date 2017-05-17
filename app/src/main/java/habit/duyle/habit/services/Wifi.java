package habit.duyle.habit.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import habit.duyle.habit.Activities.MainActivity;

/**
 * Created by leanh on 3/29/2017.
 */

public class Wifi {
    public static boolean wifiIsAvailable(){
        ConnectivityManager connManager = (ConnectivityManager) MainActivity.getMainActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            return true;
        }
        return false;
    }
}
