package ru.whereiswifi;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity {

	private DB db;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        
        db = DB.inst(this);
        List<String> values = new ArrayList<String>();
        
        for(ContentValues v : db.listSpots()){
        	values.add(v.getAsString("ssid"));
        }
        
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
        
        
        // register receiver    
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(new WifiReceiver(), intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}

