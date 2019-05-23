package com.jwhh.jim.notekeeper;
import android.content.Context;
import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.jwhh.jim.notekeeper.NoteKeeperDatabaseContract.*;

public class NoteRecycleAdapter extends  RecyclerView.Adapter<NoteRecycleAdapter.ViewHolder> {

    private Context mContext = null;

    private final LayoutInflater layoutInflater;

    private Cursor mCursor;
    private int mCoursePos;
    private int mNoteTittlePos;
    private int mIdPos;

    public NoteRecycleAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        cursor=mCursor;
        layoutInflater = LayoutInflater.from( context);
        
        populateColumnsPositions();

    }

    private void populateColumnsPositions() {

        if(mCursor==null){
            return;
        }

            //Get columns Indexes from the Cursor;
        mCoursePos = mCursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);
        mNoteTittlePos = mCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        mIdPos = mCursor.getColumnIndex(NoteInfoEntry._ID);


    }

    public void changeCursor(Cursor cursor){
        if(mCursor!=null){
            mCursor.close();
        }
        mCursor=cursor;
        populateColumnsPositions();
        notifyDataSetChanged();


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView=layoutInflater.inflate(R.layout.item_note_list,parent,false);

        return new  ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        String course=mCursor.getString(mCoursePos);
        String title=mCursor.getString(mNoteTittlePos);
        int id=mCursor.getInt(mIdPos);


        holder.textCourse.setText(course);
        holder.textTittle.setText(title);
        holder.mId =id;

    }

    @Override
    public int getItemCount() {
        return mCursor==null ? 0 :  mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView textCourse;
        public final TextView textTittle;
        public int mId;

        public ViewHolder(View itemView) {
            super(itemView);

            textCourse = itemView.findViewById(R.id.text_course);
            textTittle = itemView.findViewById(R.id.text_tittle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(mContext,NoteActivity.class);
                    intent.putExtra(NoteActivity.NOTE_ID, mId);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}

