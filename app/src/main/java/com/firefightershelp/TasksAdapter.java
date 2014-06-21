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
public class TasksAdapter extends BaseAdapter {
   /* @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }
*/

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }*/
    private Context thisContext;
    private LayoutInflater inflater;
    private List<Task> tasksList = null;
    private ArrayList<Task> arraylist;
    private Task task;

    public TasksAdapter(Context context, List<Task> postsList) {
        thisContext = context;
        inflater = LayoutInflater.from(thisContext);
        this.tasksList = postsList;
        this.arraylist = new ArrayList<Task>();
        this.arraylist.addAll(postsList);
    }

    @Override
    public int getCount() {
        return tasksList.size();
    }

    @Override
    public Task getItem(int position) {
        return tasksList.get(position);
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final TaskHolder holder;
        task = tasksList.get(position);
        if (view == null) {
            view = inflater.inflate(R.layout.list_item, null);
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
        String assignedTo = (task.getAssignedTo()!=null && task.getAssignedTo().equals("") ? task.getAssignedTo() : "" );
        holder.taskAssignedTo.setText(assignedTo);
        if(holder.taskId!= null)
            holder.taskId.setText(Integer.toString(task.getTaskId()));
        holder.taskName.setText(task.getTaskName());
        if(task.getTaskStatus() == 0){
            LightingColorFilter darken = new LightingColorFilter(0,0xFF0000);
            holder.taskAssignedTo.getBackground().setColorFilter(darken);
            holder.taskAssignedTo.setTextColor(Color.BLACK);
        }
        else if(task.getTaskStatus() == 1){
            LightingColorFilter darken = new LightingColorFilter(0,0xFFFF00);
            holder.taskAssignedTo.getBackground().setColorFilter(darken);
            holder.taskAssignedTo.setTextColor(Color.BLACK);
        }
        else if(task.getTaskStatus() == 2){
            LightingColorFilter darken = new LightingColorFilter(0,0x008000);
            holder.taskAssignedTo.getBackground().setColorFilter(darken);
            holder.taskAssignedTo.setTextColor(Color.BLACK);
        }
        holder.taskAssignedTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater =
                        (LayoutInflater)thisContext
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

                Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);

                Spinner popupSpinner = (Spinner) popupView.findViewById(R.id.popupspinner);
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<String>(thisContext,
                                android.R.layout.simple_spinner_item, MyApplication.usernames);
            }
        });
        return view;
    }
    static class TaskHolder {
        TextView taskName;
        TextView taskId;
        Button taskAssignedTo;

        public TaskHolder(View convertView){
            // UI references
            taskName= (TextView) convertView.findViewById(R.id.taskName);
            taskId = (TextView) convertView.findViewById(R.id.taskNo);
            taskAssignedTo= (Button) convertView.findViewById(R.id.taskButton);
        }

    }

}
