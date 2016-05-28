package me.sidhin.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TaskDatabase db;
    private ArrayList<Task> tasks = null;
    private int lastIndex = 0;
    private ArrayAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        db = new TaskDatabase(this);
        tasks = db.getAllTask();
        if (tasks.size() > 0)
            lastIndex = tasks.get(tasks.size() - 1).ID + 1;
        /* BUG! items from database not being displayed,
         * Even when using simple adapter.
         * TODO solve listView diplay bug
         */

        adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

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
                        Toast.makeText(c, task + tasks.size(), Toast.LENGTH_SHORT).show(); // Just for debug
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }

    private void addEntry(String details) {
        db.addEntry(new Task(lastIndex, details));
        tasks = db.getAllTask();
        lastIndex++;
        adapter.notifyDataSetChanged();
    }

}
