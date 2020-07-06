package com.jakezhou.notesapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ListView notesList;
    static ArrayList<String> notesArray = new ArrayList<>();
    static ArrayAdapter<String> notesAdapter;

    static SharedPreferences sharedPref;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = getApplicationContext();
        sharedPref = context.getSharedPreferences("com.jakezhou.notesapp", Context.MODE_PRIVATE);

        Set<String> notesSet = sharedPref.getStringSet("notes", null);

        if(notesSet == null) {
            notesArray.add("Example Note");
        }
        else {
            notesArray = new ArrayList<>(notesSet);
        }

        //Setting up notes listview adapter
        notesList = findViewById(R.id.notesList);
        notesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, notesArray);
        notesList.setAdapter(notesAdapter);

        //Open the note on click
        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                Intent openNoteIntent = new Intent(context, NoteActivity.class);
                openNoteIntent
                        .putExtra("id", position);
                startActivity(openNoteIntent);
            }
        });

        //Delete notes with long click
        notesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final TextView textView = (TextView) view;

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Are you sure you want to remove this note")
                        .setMessage("This will permanently delete your note")
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notesArray.remove(textView.getText().toString());
                                notesAdapter.notifyDataSetChanged();

                                Set<String> notesSet = new HashSet<>(notesArray);
                                sharedPref.edit().putStringSet("notes", notesSet).apply();

                                dialog.dismiss();
                            }
                        })
                        .show();

                return true;
            }
        });

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addNote) {
            Intent addNoteIntent = new Intent(context, NoteActivity.class);
            startActivity(addNoteIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
