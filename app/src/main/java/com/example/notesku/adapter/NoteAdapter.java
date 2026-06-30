package com.example.notesku.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesku.DetailActivity;
import com.example.notesku.R;
import com.example.notesku.database.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Context context;

    // Data yang ditampilkan
    private List<Note> noteList;

    // Seluruh data (untuk search)
    private List<Note> fullList;

    public NoteAdapter(Context context, List<Note> noteList) {

        this.context = context;

        this.noteList = new ArrayList<>(noteList);
        this.fullList = new ArrayList<>(noteList);

    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_note, parent, false);

        return new NoteViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        Note note = noteList.get(position);

        holder.txtTitle.setText(note.getTitle());
        holder.txtContent.setText(note.getContent());
        holder.txtCategory.setText(note.getCategory());
        holder.txtDate.setText(note.getCreatedAt());

        // Voice Note
        if (note.getAudioPath() == null || note.getAudioPath().isEmpty()) {

            holder.layoutAudio.setVisibility(View.GONE);

        } else {

            holder.layoutAudio.setVisibility(View.VISIBLE);

        }

        // Animasi
        Animation animation = AnimationUtils.loadAnimation(
                context,
                R.anim.item_animation
        );

        holder.itemView.startAnimation(animation);

        // Klik Card
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, DetailActivity.class);

            intent.putExtra("id", note.getId());
            intent.putExtra("title", note.getTitle());
            intent.putExtra("content", note.getContent());
            intent.putExtra("category", note.getCategory());
            intent.putExtra("date", note.getCreatedAt());
            intent.putExtra("audio", note.getAudioPath());

            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    // ==========================
    // UPDATE DATA
    // ==========================

    public void updateData(List<Note> newList) {

        noteList.clear();
        noteList.addAll(newList);

        fullList.clear();
        fullList.addAll(newList);

        notifyDataSetChanged();

    }

    // ==========================
    // SEARCH
    // ==========================

    public void filter(String keyword) {

        noteList.clear();

        if (keyword == null || keyword.trim().isEmpty()) {

            noteList.addAll(fullList);

        } else {

            keyword = keyword.toLowerCase().trim();

            for (Note note : fullList) {

                boolean title =
                        note.getTitle() != null &&
                                note.getTitle().toLowerCase().contains(keyword);

                boolean content =
                        note.getContent() != null &&
                                note.getContent().toLowerCase().contains(keyword);

                boolean category =
                        note.getCategory() != null &&
                                note.getCategory().toLowerCase().contains(keyword);

                if (title || content || category) {

                    noteList.add(note);

                }

            }

        }

        notifyDataSetChanged();

    }

    // ==========================
    // VIEW HOLDER
    // ==========================

    public static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtContent;
        TextView txtCategory;
        TextView txtDate;
        TextView txtAudio;

        ImageView imgMic;

        LinearLayout layoutAudio;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtDate = itemView.findViewById(R.id.txtDate);

            txtAudio = itemView.findViewById(R.id.txtAudio);
            imgMic = itemView.findViewById(R.id.imgMic);
            layoutAudio = itemView.findViewById(R.id.layoutAudio);
        }
    }

}