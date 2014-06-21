package com.firefightershelp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DispatcherActivity extends Activity {

    public final int port = 7950;


    private WifiP2pManager wifiManager;
    private WifiP2pManager.Channel wifichannel;
    private BroadcastReceiver wifiServerReceiver;
    private final String TAG="Dispatcher Activity";
    private IntentFilter wifiServerReceiverIntentFilter;
    // Declare Variables
    private ListView listview;
    List<ParseObject> ob;
    private TasksAdapter adapter;
    private List<Task> taskslist;
    private TextView welcomeText;
    private Context thisContext;
    private String eventId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisContext = this;
        setContentView(R.layout.activity_dispatcher);
       /* wifiManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        wifichannel = wifiManager.initialize(this, getMainLooper(), null);
        wifiServerReceiver = new WiFiServerBroadcastReceiver(wifiManager, wifichannel, this);

        wifiServerReceiverIntentFilter = new IntentFilter();;
        wifiServerReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        wifiServerReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        wifiServerReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        wifiServerReceiverIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);*/
        //Block auto opening keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //Custom Action bar
        try {
            LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            final View customActionBarView = inflater.inflate(
                    R.layout.actionbar_customlayout, null);
            final android.app.ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setCustomView(customActionBarView);
            final int actionBarColor = getResources().getColor(R.color.action_bar);
            actionBar.setBackgroundDrawable(new ColorDrawable(actionBarColor));
            actionBar.setDisplayUseLogoEnabled(false);
            ImageView newTask = (ImageView) customActionBarView.findViewById(R.id.newTask);
           newTask.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
        List<Task> tasksList = new ArrayList<Task>();

        welcomeText = (TextView) findViewById(R.id.welcome);
        welcomeText.setText("Welcome "+MyApplication.user+"!");
        Intent intent = getIntent();
        eventId = intent.getStringExtra("objectId");
        new RemoteDataTask().execute();
    }
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Parse.initialize(thisContext, "pAwMknVW1OcjKXYK7Lpj5Jibigfzio8kb2CdBScO", "YRL8bqthghkC5TsIPdybWGTIzuwab2i808JiSeUZ");
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            taskslist = new ArrayList<Task>();
            try {
                // Get posts by users current user is following
                /*ParseQuery<ParseObject> eventsquery = new ParseQuery<ParseObject>("Event");
                eventsquery.whereMatches("objectId", eventId);*/
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Task");
                query.whereMatches("event", eventId);
                /*query.orderByDescending("updatedAt");
                query.include("User");*/
                query.addAscendingOrder("status");
                query.addDescendingOrder("priority");
                /*followingPostsQuery.whereMatchesKeyInQuery("User", "toUser", followingQuery);
                followingPostsQuery.whereExists("text");*/
               /* // Get posts by current user
                ParseQuery<ParseObject> userPostQuery = new ParseQuery<ParseObject>("Post");
                userPostQuery.whereEqualTo("User", ParseUser.getCurrentUser());
                userPostQuery.whereExists("text");
                // Combine and order queries
                List<ParseQuery<ParseObject>> queryList = Arrays.asList(userPostQuery, followingPostsQuery);
                ParseQuery<ParseObject> query = ParseQuery.or(queryList);
                query.orderByDescending("updatedAt");
                query.include("User");*/
                ob = query.find();
                for (ParseObject tasks : ob) {
                        final Task task = new Task();
                        task.setTaskName(tasks.getString("name"));
                        task.setTaskId(tasks.getInt("priority"));
                        task.setTaskStatus(tasks.getInt("status"));
                        ParseUser user = tasks.getParseUser("assignedTo");
                       // String username = user.getUsername();
                        if(user!=null) {
                            user.fetchInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject parseObject, ParseException e) {
                                    task.setAssignedTo(((ParseUser) parseObject).getUsername());
                                }
                            });
                        }
                        else
                            task.setAssignedTo("");
                        //task.setAssignedTo(user.getUsername());
                        task.setPriority(tasks.getInt("priority"));
                        taskslist.add(task);
                     }
                } catch (ParseException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // Locate the ListView in listview.xml
            listview = (ListView) findViewById(R.id.tasksList);
            // Pass the results into ListViewAdapter.java

            adapter = new TasksAdapter(thisContext, taskslist);
            listview.setAdapter(adapter);
        }


    }
}
