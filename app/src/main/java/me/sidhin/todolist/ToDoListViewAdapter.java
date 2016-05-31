package me.sidhin.todolist;/*
 * Created by Zero on 27-May-16.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class ToDoListViewAdapter extends ArrayAdapter {
    private ArrayList<Task> tasks;
    private Context context;

    public ToDoListViewAdapter(Context c, ArrayList<Task> t) {
        super(c, R.layout.todolist_layout);
        tasks = t;
        context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.todolist_layout, null, true);
        TextView t = (TextView) row.findViewById(R.id.detail);
        t.setText(tasks.get(position).Details);
        CheckBox c = (CheckBox) row.findViewById(R.id.checkBox);
        c.setChecked(tasks.get(position).done);

        /* ToDO set checked listener ! */

        return row;
    }
}
