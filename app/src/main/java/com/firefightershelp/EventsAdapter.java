package com.firefightershelp;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NaveenT on 6/21/14.
 */
public class EventsAdapter extends BaseAdapter {

    @Override
    public long getItemId(int position) {
        return position;
    }

    private Context thisContext;
    private LayoutInflater inflater;
    private List<Event> eventsList = null;
    private ArrayList<Event> arraylist;
    private Event event;

    public EventsAdapter(Context context, List<Event> eventsList) {
        thisContext = context;
        inflater = LayoutInflater.from(thisContext);
        this.eventsList = eventsList;
        this.arraylist = new ArrayList<Event>();
        this.arraylist.addAll(eventsList);
    }

    @Override
    public int getCount() {
        return eventsList.size();
    }

    @Override
    public Event getItem(int position) {
        return eventsList.get(position);
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final TaskHolder holder;
        event = eventsList.get(position);
        if (view == null) {
            view = inflater.inflate(R.layout.events_item, null);
            holder = new TaskHolder(view);
            view.setTag(holder);
        } else {
            if (view.getTag() != null)
                holder = (TaskHolder) view.getTag();
            else {
                holder = new TaskHolder(view);
                view.setTag(holder);
            }
        }

        holder.eventName.setText(event.getName());
        return view;
    }
    static class TaskHolder {
        TextView eventName;

        public TaskHolder(View convertView){
            // UI references
            eventName= (TextView) convertView.findViewById(R.id.eventName);
        }

    }

}

