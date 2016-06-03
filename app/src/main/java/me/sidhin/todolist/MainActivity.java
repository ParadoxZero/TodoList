package me.sidhin.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TaskDatabase db;
    private ArrayList<Task> tasks = null;
    private int lastIndex = 0;
    private ArrayAdapter adapter;
    private ListView listView;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        db = new TaskDatabase(this);
        tasks = db.getAllTask();
        Log.i("Database", tasks.size() + "");
        if (tasks.size() > 0)
            lastIndex = tasks.get(tasks.size() - 1).ID + 1;

        adapter = new ToDoListViewAdapter(this, tasks);
        //adapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,tasks);
        context = this;
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { //DELETE SELECTED TASK
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View v, int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Delete Task")
                        .setMessage("Do you want to delete this Task ?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Task t = (Task) v.getTag(ToDoListViewAdapter.TAG_KEY_TASK);
                                db.deleteEntry(t.ID);
                                tasks.clear();
                                tasks.addAll(db.getAllTask());
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //TO MARK TASK DONE
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task t = (Task) view.getTag(ToDoListViewAdapter.TAG_KEY_TASK);
                ViewHolder holder = (ViewHolder) view.getTag(ToDoListViewAdapter.TAG_KEY_HOLDER);
                t.done = !t.done;
                holder.ck1.setChecked(t.done);
                if (t.done) {
                    /* To set striked */
                    holder.details.setPaintFlags(holder.details.getPaintFlags()
                            | Paint.STRIKE_THRU_TEXT_FLAG);

                } else {
                    holder.details.setPaintFlags(holder.details.getPaintFlags()
                            & ~(Paint.STRIKE_THRU_TEXT_FLAG));
                }

                db.update(t);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                /* todo add settings activity */

                return true;

            case R.id.action_add:
                addNewTask();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void addNewTask() {
        final EditText DialogText = new EditText(this);
        final Context c = this;
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add a new task")
                .setMessage("What do you want to do next?")
                .setView(DialogText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(DialogText.getText());
                        addEntry(task);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }

    private void addEntry(String details) {
        db.addEntry(new Task(lastIndex, details));
        tasks.clear();
        tasks.addAll(db.getAllTask());
        lastIndex++;
        adapter.notifyDataSetChanged();
    }

}
