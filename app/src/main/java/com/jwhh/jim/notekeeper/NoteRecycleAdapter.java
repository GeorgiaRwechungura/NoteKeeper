package com.jwhh.jim.notekeeper;
import android.content.Context;
import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteRecycleAdapter extends  RecyclerView.Adapter<NoteRecycleAdapter.ViewHolder> {

    private  final Context context;
    private final LayoutInflater layoutInflater;
    private  final List<NoteInfo> mNotes;

    public NoteRecycleAdapter(Context context, List<NoteInfo> mNotes) {
        this.context = context;

        layoutInflater = LayoutInflater.from( context);
        this.mNotes = mNotes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView=layoutInflater.inflate(R.layout.item_note_list,parent,false);

        return new  ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        NoteInfo note=mNotes.get(position);

        holder.textCourse.setText(note.getCourse().getTitle());
        holder.textTittle.setText(note.getTitle());
        holder.mCurrentPosition=position;

    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView textCourse;
        public final TextView textTittle;
        public int mCurrentPosition;

        public ViewHolder(View itemView) {
            super(itemView);

            textCourse = itemView.findViewById(R.id.text_course);
            textTittle = itemView.findViewById(R.id.text_tittle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context,NoteActivity.class);
                    intent.putExtra(NoteActivity.NOTE_POSITION,mCurrentPosition);
                    context.startActivity(intent);
                }
            });
        }
    }
}

