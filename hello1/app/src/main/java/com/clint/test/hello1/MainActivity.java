package com.clint.test.hello1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clint.test.hello1.util.wifiUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends Activity {

    final android.content.Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        TextView currentState=(TextView)findViewById(R.id.textView1);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(),"Reading Wifi Setting...", Toast.LENGTH_SHORT).show();

            currentState.setText("Reading Saved Wifi Settings");

            readWifiConfig();
            return true;
        } else if (id == R.id.action_wifiState){

            currentState.setText("Assure Wifi Connectivity");
            //return this.assureWIFI();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//region UI actions
    public void showMyListActivity(View view) {
        Intent intent = new Intent(this, MyListActivity.class);
        startActivity(intent);
    }


    public void showMyYesNoListActivity(View view) {
        Intent intent = new Intent(this, MyYesNoListActivity.class);
        startActivity(intent);
    }

//endregion

//region Event callbacks
//    @Override
//    public void onReceive(Context context, final Intent intent) {
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//
//        if (wifi.isConnected()) {
//            // Your code here
//        }
//    }

    /**
     * Register Wifi Connectivity Receiver to alert connectivity state
     */
    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            if(currentNetworkInfo.isConnected()){
                Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_LONG).show();
            }
        }
    };

//endregion


    /**
     * Reset Wifi if wifi connection is down.ß
     * @return
     */
    public boolean assureWIFI(){
        if (!wifiUtil.hasInternetConnection(context)) {
            // Alert to current wifi state and prompt to reset or cancel
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("Current Wifi State is OFF: Try Connect");
            builder1.setCancelable(true);
            builder1.setPositiveButton(getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // try connect know Wifi
                            enableNetwork();
                            // release dialog
                            dialog.cancel();

                        }
                    });
            builder1.setNegativeButton(getString(R.string.no),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        return true;
    }



    /**
     * enable Wifi network
     * // http://stackoverflow.com/questions/8818290/how-to-connect-to-a-specific-wifi-network-in-android-programmatically
     * @return
     */
    public boolean enableNetwork()
    {

        String networkSSID = "discovery";
        String pass = "DD4FCA9568";
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", networkSSID);
        //wifiConfig.preSharedKey = String.format("\"%s\"", pass);

        wifiConfig.status = WifiConfiguration.Status.DISABLED;
        wifiConfig.hiddenSSID = true;
        wifiConfig.priority = 40;
        wifiConfig.wepKeys[0] = String.format("\"%s\"", pass);
        wifiConfig.wepTxKeyIndex = 0;

        //wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);  //for public network
        //wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        //wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);


        WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);

        int netId = wifiManager.addNetwork(wifiConfig);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();

                break;
            }
        }

//        //remember id and enable
//        int netId = wifiManager.addNetwork(wifiConfig);
//        if (netId !=-1){
//            wifiManager.disconnect();
//            wifiManager.enableNetwork(netId, true);
//            wifiManager.reconnect();
//        }


        // loop the current network in case add failure
        //        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        //        for( WifiConfiguration i : list ) {
        //            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
        //                wifiManager.disconnect();
        //                wifiManager.enableNetwork(i.networkId, true);
        //                wifiManager.reconnect();
        //
        //                break;
        //            }
        //        }
        return true;
    }

    public void readWifiConfig()
    {
        ListView listView = (ListView)findViewById(R.id.listView1);
        final ArrayList<String> wifiList = new ArrayList<String>();



        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> items = wifi.getConfiguredNetworks();
        int n = items.size();
        Log.d("WifiPreference", "NO OF CONFIG " + n );
        //Iterator<WifiConfiguration> iter =  item.iterator();


        for(int i = 0;i<n ;i++) {
            WifiConfiguration config = items.get(i);
            wifiUtil.logWifiConfig(config);

            // Show in window
            wifiList.add(config.SSID + "\nStatus:" + config.status + "\n" +config.toString());
            //wifiList.add(config.SSID + "Status:" + config.status + "\n" +config.toString());

        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, wifiList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(1000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                wifiList.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
            }

        });
    }


    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
