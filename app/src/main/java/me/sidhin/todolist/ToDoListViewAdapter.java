package me.sidhin.todolist;/*
 * Created by Zero on 27-May-16.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class ToDoListViewAdapter extends ArrayAdapter {

    public static int TAG_KEY_HOLDER = R.id.detail;

    private ArrayList<Task> tasks;
    private Context context;
    private TaskDatabase db;
    public ToDoListViewAdapter(Context c, ArrayList<Task> t) {
        super(c, R.layout.todolist_layout);
        tasks = t;
        context = c;
        db = new TaskDatabase(context);
    }


    public int getCount() {
        return tasks.size();
    }

    public Object getItem(int position) {
        return tasks.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        Log.i("Display", position + "");
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.todolist_layout, null);
            holder = new ViewHolder();

            holder.details = (TextView) convertView
                    .findViewById(R.id.detail);

            holder.ck1 = (CheckBox) convertView
                    .findViewById(R.id.checkBox);
            convertView.setTag(TAG_KEY_HOLDER, holder);

        } else {

            holder = (ViewHolder) convertView.getTag(TAG_KEY_HOLDER);
        }

        holder.details.setText(tasks.get(position).Details);
        holder.ck1.setChecked(tasks.get(position).done);
        holder.ck1.setClickable(false);
            /* To StrikeThrough textView in case of done */
        if (holder.ck1.isChecked()) {
            holder.details.setPaintFlags(holder.details.getPaintFlags()
                    | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else{
            holder.details.setPaintFlags(holder.details.getPaintFlags()
                    & ~(Paint.STRIKE_THRU_TEXT_FLAG));
        }
        return convertView;
    }
}
