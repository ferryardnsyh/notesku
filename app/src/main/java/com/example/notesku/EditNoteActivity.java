package com.example.notesku;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notesku.database.DatabaseHelper;
import com.example.notesku.database.Note;
import com.google.android.material.appbar.MaterialToolbar;

public class EditNoteActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;

    private EditText edtTitle, edtCategory, edtContent;
    private Button btnUpdate;

    private DatabaseHelper databaseHelper;

    private int id;
    private String date;
    private String audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        // ==========================
        // Toolbar
        // ==========================

        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(v ->
                getOnBackPressedDispatcher().onBackPressed());

        // ==========================
        // Inisialisasi View
        // ==========================

        edtTitle = findViewById(R.id.edtTitle);
        edtCategory = findViewById(R.id.edtCategory);
        edtContent = findViewById(R.id.edtContent);
        btnUpdate = findViewById(R.id.btnUpdate);

        databaseHelper = new DatabaseHelper(this);

        // ==========================
        // Ambil Data Intent
        // ==========================

        Intent intent = getIntent();

        id = intent.getIntExtra("id", 0);

        edtTitle.setText(intent.getStringExtra("title"));
        edtCategory.setText(intent.getStringExtra("category"));
        edtContent.setText(intent.getStringExtra("content"));

        date = intent.getStringExtra("date");
        audio = intent.getStringExtra("audio");

        // ==========================
        // Update Catatan
        // ==========================

        btnUpdate.setOnClickListener(v -> {

            String title = edtTitle.getText().toString().trim();
            String category = edtCategory.getText().toString().trim();
            String content = edtContent.getText().toString().trim();

            if (title.isEmpty()) {
                edtTitle.setError("Judul tidak boleh kosong");
                edtTitle.requestFocus();
                return;
            }

            if (category.isEmpty()) {
                edtCategory.setError("Kategori tidak boleh kosong");
                edtCategory.requestFocus();
                return;
            }

            if (content.isEmpty()) {
                edtContent.setError("Isi catatan tidak boleh kosong");
                edtContent.requestFocus();
                return;
            }

            Note note = new Note();

            note.setId(id);
            note.setTitle(title);
            note.setCategory(category);
            note.setContent(content);
            note.setCreatedAt(date);
            note.setAudioPath(audio);

            int result = databaseHelper.updateNote(note);

            if (result > 0) {

                Toast.makeText(
                        EditNoteActivity.this,
                        "Catatan berhasil diperbarui",
                        Toast.LENGTH_SHORT
                ).show();

                Intent mainIntent = new Intent(
                        EditNoteActivity.this,
                        MainActivity.class
                );

                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(mainIntent);
                finish();

            } else {

                Toast.makeText(
                        EditNoteActivity.this,
                        "Gagal memperbarui catatan",
                        Toast.LENGTH_SHORT
                ).show();

            }

        });

    }

}