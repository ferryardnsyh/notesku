package com.example.notesku;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.notesku.database.DatabaseHelper;
import com.example.notesku.database.Note;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;

    private EditText edtTitle, edtCategory, edtContent;

    private MaterialButton btnSave, btnRecord;

    private DatabaseHelper databaseHelper;

    private MediaRecorder mediaRecorder;

    private String audioPath = "";

    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

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

        btnSave = findViewById(R.id.btnSave);
        btnRecord = findViewById(R.id.btnRecord);

        databaseHelper = new DatabaseHelper(this);

        // ==========================
        // Permission Microphone
        // ==========================

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.RECORD_AUDIO
                    },
                    100);

        }

        // ==========================
        // Tombol Rekam
        // ==========================

        btnRecord.setOnClickListener(v -> {

            if (isRecording) {

                stopRecording();

            } else {

                startRecording();

            }

        });

        // ==========================
        // Tombol Simpan
        // ==========================

        btnSave.setOnClickListener(v -> saveNote());

    }

    private void saveNote() {

        if (isRecording) {

            Toast.makeText(
                    this,
                    "Hentikan rekaman terlebih dahulu.",
                    Toast.LENGTH_SHORT
            ).show();

            return;

        }

        String title = edtTitle.getText().toString().trim();
        String category = edtCategory.getText().toString().trim();
        String content = edtContent.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {

            edtTitle.setError("Judul tidak boleh kosong");
            edtTitle.requestFocus();
            return;

        }

        if (TextUtils.isEmpty(category)) {

            edtCategory.setError("Kategori tidak boleh kosong");
            edtCategory.requestFocus();
            return;

        }

        if (TextUtils.isEmpty(content)) {

            edtContent.setError("Isi catatan tidak boleh kosong");
            edtContent.requestFocus();
            return;

        }

        String date = new SimpleDateFormat(
                "dd MMM yyyy",
                Locale.getDefault())
                .format(new Date());

        Note note = new Note();

        note.setTitle(title);
        note.setCategory(category);
        note.setContent(content);
        note.setCreatedAt(date);
        note.setAudioPath(audioPath);

        long result = databaseHelper.insertNote(note);

        if (result > 0) {

            Toast.makeText(
                    this,
                    "Catatan berhasil disimpan.",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Gagal menyimpan catatan.",
                    Toast.LENGTH_SHORT
            ).show();

        }

    }

    // ==========================
    // Mulai Rekam
    // ==========================

    private void startRecording() {

        try {

            audioPath = getExternalFilesDir(null)
                    .getAbsolutePath()
                    + "/audio_"
                    + System.currentTimeMillis()
                    + ".3gp";

            mediaRecorder = new MediaRecorder();

            mediaRecorder.setAudioSource(
                    MediaRecorder.AudioSource.MIC);

            mediaRecorder.setOutputFormat(
                    MediaRecorder.OutputFormat.THREE_GPP);

            mediaRecorder.setAudioEncoder(
                    MediaRecorder.AudioEncoder.AMR_NB);

            mediaRecorder.setOutputFile(audioPath);

            mediaRecorder.prepare();

            mediaRecorder.start();

            isRecording = true;

            btnRecord.setText("Stop Rekam");

            Toast.makeText(
                    this,
                    "Mulai merekam...",
                    Toast.LENGTH_SHORT
            ).show();

        } catch (IOException e) {

            Toast.makeText(
                    this,
                    "Gagal memulai rekaman.",
                    Toast.LENGTH_SHORT
            ).show();

        }

    }

    // ==========================
    // Stop Rekam
    // ==========================

    private void stopRecording() {

        try {

            if (mediaRecorder != null) {

                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;

            }

            isRecording = false;

            btnRecord.setText("Rekam Suara");

            Toast.makeText(
                    this,
                    "Rekaman berhasil disimpan.",
                    Toast.LENGTH_SHORT
            ).show();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Gagal menghentikan rekaman.",
                    Toast.LENGTH_SHORT
            ).show();

        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isRecording) {
            stopRecording();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaRecorder != null) {

            mediaRecorder.release();
            mediaRecorder = null;

        }
    }

}