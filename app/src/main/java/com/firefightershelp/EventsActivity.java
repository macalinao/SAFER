package com.firefightershelp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firefightershelp.R;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends Activity {
    Context thisContext;
    private TextView welcomeText;

    private EventsAdapter adapter;
    private List<Event> eventsList;
    List<ParseObject> ob;
    private ListView eventsListView;
    private final String TAG="EventsActivity";
    private SpeechRecognizer sr;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        thisContext = this;
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
            newTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNewTaskClicked(v);
                }
            });
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
        List<Task> tasksList = new ArrayList<Task>();
        //Starting the speech recognition Service
        sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(new listener());
        startService(new Intent(getBaseContext(), MyService.class));
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
        sr.startListening(intent);

        welcomeText = (TextView) findViewById(R.id.welcomeEvents);
        welcomeText.setText("Welcome "+MyApplication.user+"!");
        new ParseData().execute();

    }
    private void onNewTaskClicked(View v){

    }
    class listener implements RecognitionListener
    {
        public void onReadyForSpeech(Bundle params)
        {
            Log.d(TAG, "onReadyForSpeech");
        }
        public void onBeginningOfSpeech()
        {
            Log.d(TAG, "onBeginningOfSpeech");
        }
        public void onRmsChanged(float rmsdB)
        {
            Log.d(TAG, "onRmsChanged");
        }
        public void onBufferReceived(byte[] buffer)
        {
            Log.d(TAG, "onBufferReceived");
        }
        public void onEndOfSpeech()
        {
            Log.d(TAG, "onEndofSpeech");
        }
        public void onError(int error)
        {
            Log.d(TAG,  "error " +  error);
           // mText.setText("error " + error);
        }
        public void onResults(Bundle results)
        {
            String str = new String();
            Log.d(TAG, "onResults " + results);
            ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            /*for (int i = 0; i < data.size(); i++)
            {
                Log.d(TAG, "result " + data.get(i));
                str += data.get(i);
            }*/
            Toast.makeText(thisContext,data.get(0),Toast.LENGTH_LONG).show();
            if(data.get(0).equalsIgnoreCase("help")){
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Task");
                query.whereMatches("objectId", "oaVQjyn8jF");
                try {
                    List<ParseObject> listofTasks = query.find();
                    ParseObject object = listofTasks.get(0);
                    object.put("needsAttention",true);
                    object.saveInBackground();

                }catch (ParseException e){
                    e.printStackTrace();
                }
            }
            sr.startListening(intent);
            //mText.setText("results: " + str);
        }
        public void onPartialResults(Bundle partialResults)
        {
            Log.d(TAG, "onPartialResults");
        }
        public void onEvent(int eventType, Bundle params)
        {
            Log.d(TAG, "onEvent " + eventType);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getBaseContext(), MyService.class));
        sr.destroy();
    }

    private class ParseData extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
        //    Parse.initialize(thisContext, "pAwMknVW1OcjKXYK7Lpj5Jibigfzio8kb2CdBScO", "YRL8bqthghkC5TsIPdybWGTIzuwab2i808JiSeUZ");
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            eventsList = new ArrayList<Event>();
            try {
                // Get posts by users current user is following
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Event");
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
                for (ParseObject events : ob) {
                    final Event event = new Event();
                    event.setName(events.getString("name"));
                    event.setObjectId(events.getObjectId());
                    eventsList.add(event);
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
            eventsListView = (ListView) findViewById(R.id.eventsList);
            // Pass the results into ListViewAdapter.java

            adapter = new EventsAdapter(thisContext, eventsList);
            eventsListView.setAdapter(adapter);
            eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String eventId=eventsList.get(position).getObjectId();
                    Intent tasksIntent = new Intent(thisContext, DispatcherActivity.class);
                    tasksIntent.putExtra("objectId",eventId);
                    startActivity(tasksIntent);
                }
            });
        }
    }

}
