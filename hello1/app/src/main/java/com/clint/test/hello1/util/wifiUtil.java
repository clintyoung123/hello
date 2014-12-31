package com.clint.test.hello1.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.util.Log;

/**
 * Created by administrator on 12/15/14.
 */
public class wifiUtil {
    public static boolean hasInternetConnection(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected())
        {
            return true;
        }
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected())
        {
            return true;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected())
        {
            return true;
        }
        return false;
    }

    public static void logWifiConfig(WifiConfiguration config)
    {
        // Log details of wifi config
        Log.d("WifiPreference", "-----  SSID -----" + config.SSID);
        Log.d("WifiPreference", "PASSWORD:" + config.preSharedKey);
        Log.d("WifiPreference", "- ALLOWED ALGORITHMS -");
        Log.d("WifiPreference", "LEAP:" + config.allowedAuthAlgorithms.get(WifiConfiguration.AuthAlgorithm.LEAP));
        Log.d("WifiPreference", "OPEN:" + config.allowedAuthAlgorithms.get(WifiConfiguration.AuthAlgorithm.OPEN));
        Log.d("WifiPreference", "SHARED:" + config.allowedAuthAlgorithms.get(WifiConfiguration.AuthAlgorithm.SHARED));
        Log.d("WifiPreference", "- GROUP CIPHERS -");
        Log.d("WifiPreference", "CCMP:" + config.allowedGroupCiphers.get(WifiConfiguration.GroupCipher.CCMP));
        Log.d("WifiPreference", "TKIP:" + config.allowedGroupCiphers.get(WifiConfiguration.GroupCipher.TKIP));
        Log.d("WifiPreference", "WEP104:" + config.allowedGroupCiphers.get(WifiConfiguration.GroupCipher.WEP104));
        Log.d("WifiPreference", "WEP40:" + config.allowedGroupCiphers.get(WifiConfiguration.GroupCipher.WEP40));
        Log.d("WifiPreference", "- KEYMGMT -");
        Log.d("WifiPreference", "IEEE8021X:" + config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X));
        Log.d("WifiPreference", "NONE:" + config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.NONE));
        Log.d("WifiPreference", "WPA_EAP:" + config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP));
        Log.d("WifiPreference", "WPA_PSK:" + config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK));
        Log.d("WifiPreference", "- PairWiseCipher -");
        Log.d("WifiPreference", "CCMP:" + config.allowedPairwiseCiphers.get(WifiConfiguration.PairwiseCipher.CCMP));
        Log.d("WifiPreference", "NONE:" + config.allowedPairwiseCiphers.get(WifiConfiguration.PairwiseCipher.NONE));
        Log.d("WifiPreference", "TKIP:" + config.allowedPairwiseCiphers.get(WifiConfiguration.PairwiseCipher.TKIP));
        Log.d("WifiPreference", "- Protocols -");
        Log.d("WifiPreference", "RSN:" + config.allowedProtocols.get(WifiConfiguration.Protocol.RSN));
        Log.d("WifiPreference", "WPA:" + config.allowedProtocols.get(WifiConfiguration.Protocol.WPA));
        Log.d("WifiPreference", "- WEP Key Strings -");
        String[] wepKeys = config.wepKeys;
        Log.d("WifiPreference", "WEP KEY 0:" + wepKeys[0]);
        Log.d("WifiPreference", "WEP KEY 1:" + wepKeys[1]);
        Log.d("WifiPreference", "WEP KEY 2:" + wepKeys[2]);
        Log.d("WifiPreference", "WEP KEY 3:" + wepKeys[3]);

    }
}
