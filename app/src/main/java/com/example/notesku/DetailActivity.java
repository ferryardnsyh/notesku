package com.example.notesku;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notesku.database.DatabaseHelper;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;

    private TextView txtTitle, txtCategory, txtDate, txtContent, txtAudioStatus;

    private Button btnEdit, btnDelete, btnPlay, btnStop;

    private MaterialCardView cardAudio;

    private DatabaseHelper databaseHelper;

    private MediaPlayer mediaPlayer;

    private int id;

    private String title, category, date, content, audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // =============================
        // Toolbar
        // =============================

        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(v ->
                getOnBackPressedDispatcher().onBackPressed()
        );

        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        finish();
                    }
                });

        // =============================
        // Inisialisasi View
        // =============================

        txtTitle = findViewById(R.id.txtTitle);
        txtCategory = findViewById(R.id.txtCategory);
        txtDate = findViewById(R.id.txtDate);
        txtContent = findViewById(R.id.txtContent);
        txtAudioStatus = findViewById(R.id.txtAudioStatus);

        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnPlay = findViewById(R.id.btnPlay);
        btnStop = findViewById(R.id.btnStop);

        cardAudio = findViewById(R.id.cardAudio);

        databaseHelper = new DatabaseHelper(this);

        // =============================
        // Ambil Data
        // =============================

        Intent intent = getIntent();

        id = intent.getIntExtra("id", 0);
        title = intent.getStringExtra("title");
        category = intent.getStringExtra("category");
        content = intent.getStringExtra("content");
        date = intent.getStringExtra("date");
        audio = intent.getStringExtra("audio");

        txtTitle.setText(title);
        txtCategory.setText(category);
        txtDate.setText(date);
        txtContent.setText(content);

        // =============================
        // Audio
        // =============================

        if (audio == null || audio.trim().isEmpty()) {

            cardAudio.setVisibility(android.view.View.GONE);

        } else {

            File file = new File(audio);

            if (file.exists()) {

                txtAudioStatus.setText("Voice Note tersedia");

            } else {

                txtAudioStatus.setText("File rekaman tidak ditemukan");

            }

        }

        // =============================
        // Play Audio
        // =============================

        btnPlay.setOnClickListener(v -> playAudio());

        // =============================
        // Stop Audio
        // =============================

        btnStop.setOnClickListener(v -> stopAudio());

        // =============================
        // Edit
        // =============================

        btnEdit.setOnClickListener(v -> {

            Intent editIntent =
                    new Intent(DetailActivity.this,
                            EditNoteActivity.class);

            editIntent.putExtra("id", id);
            editIntent.putExtra("title", title);
            editIntent.putExtra("content", content);
            editIntent.putExtra("category", category);
            editIntent.putExtra("date", date);
            editIntent.putExtra("audio", audio);

            startActivity(editIntent);

        });

        // =============================
        // Delete
        // =============================

        btnDelete.setOnClickListener(v -> {

            new AlertDialog.Builder(this)
                    .setTitle("Hapus Catatan")
                    .setMessage("Yakin ingin menghapus catatan ini?")
                    .setPositiveButton("Ya", (dialog, which) -> {

                        databaseHelper.deleteNote(id);

                        Toast.makeText(this,
                                "Catatan berhasil dihapus",
                                Toast.LENGTH_SHORT).show();

                        finish();

                    })
                    .setNegativeButton("Batal", null)
                    .show();

        });

    }

    private void playAudio() {

        if (audio == null || audio.trim().isEmpty()) {

            Toast.makeText(this,
                    "Tidak ada rekaman.",
                    Toast.LENGTH_SHORT).show();

            return;

        }

        try {

            stopAudio();

            mediaPlayer = new MediaPlayer();

            mediaPlayer.setDataSource(audio);

            mediaPlayer.prepare();

            mediaPlayer.start();

            Toast.makeText(this,
                    "Memutar Voice Note...",
                    Toast.LENGTH_SHORT).show();

        } catch (IOException e) {

            Toast.makeText(this,
                    "Gagal memutar audio.",
                    Toast.LENGTH_SHORT).show();

        }

    }

    private void stopAudio() {

        if (mediaPlayer != null) {

            if (mediaPlayer.isPlaying()) {

                mediaPlayer.stop();

            }

            mediaPlayer.release();

            mediaPlayer = null;

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAudio();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAudio();
    }

}