package com.jwhh.jim.notekeeper;

import android.content.Context;
/*import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;*/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import javax.net.ssl.SNIHostName;

public class CourseRecycleAdapter extends  RecyclerView.Adapter<CourseRecycleAdapter.ViewHolder> {

    private  final Context context;
    private final LayoutInflater layoutInflater;
    private  final List<CourseInfo> mCourses;

    public CourseRecycleAdapter(Context context, List<CourseInfo> mCourses) {
        this.context = context;

        layoutInflater = LayoutInflater.from( context);
        this.mCourses = mCourses;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView=layoutInflater.inflate(R.layout.item_course_list,parent,false);

        return new  ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CourseInfo course=mCourses.get(position);

        holder.textCourse.setText(course.getTitle());

        holder.mCurrentPosition=position;

    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView textCourse;

        public int mCurrentPosition;

        public ViewHolder(View itemView) {
            super(itemView);

            textCourse = itemView.findViewById(R.id.text_course);
         //   textTittle = itemView.findViewById(R.id.text_tittle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v,mCourses.get(mCurrentPosition).getTitle(), Snackbar.LENGTH_LONG).show();

                }
            });
        }
    }
}

