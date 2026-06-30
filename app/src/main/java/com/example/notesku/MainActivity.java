package com.example.notesku;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesku.adapter.NoteAdapter;
import com.example.notesku.database.DatabaseHelper;
import com.example.notesku.database.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private CardView cardAbout;
    private EditText searchView;

    private DatabaseHelper databaseHelper;
    private NoteAdapter adapter;
    private List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi View
        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);
        cardAbout = findViewById(R.id.cardAbout);
        searchView = findViewById(R.id.searchView);

        // Database
        databaseHelper = new DatabaseHelper(this);

        // Ambil data
        noteList = databaseHelper.getAllNotes();

        // Adapter
        adapter = new NoteAdapter(this, noteList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Tambah Catatan
        fabAdd.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AddNoteActivity.class);

            startActivity(intent);

        });

        // About
        cardAbout.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AboutActivity.class);

            startActivity(intent);

        });

        // =============================
        // SEARCH
        // =============================

        searchView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start,
                                          int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s,
                                      int start,
                                      int before,
                                      int count) {

                adapter.filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter.updateData(databaseHelper.getAllNotes());

        if(searchView != null){

            adapter.filter(searchView.getText().toString());

        }

    }

}