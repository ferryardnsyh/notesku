package com.example.notesku.database;

public class Note {

    private int id;
    private String title;
    private String content;
    private String category;
    private String audioPath;
    private String createdAt;

    // Constructor kosong
    public Note() {
    }

    // Constructor lengkap
    public Note(int id, String title, String content,
                String category, String audioPath, String createdAt) {

        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.audioPath = audioPath;
        this.createdAt = createdAt;
    }

    // Getter dan Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}