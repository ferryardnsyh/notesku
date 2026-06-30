package com.example.notesku;

import android.content.Intent;
import android.os.Bundle;

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

        // Inisialisasi Database
        databaseHelper = new DatabaseHelper(this);

        // Mengambil semua data dari SQLite
        noteList = databaseHelper.getAllNotes();

        // Menampilkan data ke RecyclerView
        adapter = new NoteAdapter(this, noteList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Tombol Tambah Catatan
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivity(intent);
        });

        // Tombol About
        cardAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        noteList.clear();
        noteList.addAll(databaseHelper.getAllNotes());

        adapter.notifyDataSetChanged();
    }
}