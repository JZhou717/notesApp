package com.jakezhou.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;
import java.util.Set;

import static com.jakezhou.notesapp.MainActivity.notesAdapter;
import static com.jakezhou.notesapp.MainActivity.notesArray;
import static com.jakezhou.notesapp.MainActivity.sharedPref;

public class NoteActivity extends AppCompatActivity {

    EditText textArea;
    Intent data;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        textArea = findViewById(R.id.noteArea);

        data = this.getIntent();
        id = data.getIntExtra("id", -1);
        //If the note already exists
        if(id != -1) {
            textArea.setText(notesArray.get(id));
        }
        else {
            notesArray.add("");
            id = notesArray.size() - 1;
            notesAdapter.notifyDataSetChanged();
        }

        textArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notesArray.set(id, s.toString());
                notesAdapter.notifyDataSetChanged();

                Set<String> notesSet = new HashSet<>(notesArray);
                sharedPref.edit().putStringSet("notes", notesSet).apply();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


}
