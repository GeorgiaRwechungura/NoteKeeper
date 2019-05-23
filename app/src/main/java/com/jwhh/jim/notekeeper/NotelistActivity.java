package com.jwhh.jim.notekeeper;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
/*import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;*/

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NotelistActivity extends AppCompatActivity {
    private NoteRecycleAdapter noteRecycleAdapter;

    // private ArrayAdapter<NoteInfo> mAdapterInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_notelist);

        FloatingActionButton floatingActionButton= findViewById(R.id.floatingActionButton);

          floatingActionButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  Intent intent=new Intent(NotelistActivity.this,NoteActivity.class);
                  startActivity(intent);
              }
          });
        
        initilizeDisplayContent();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        //mAdapterInfo.notifyDataSetChanged();
        noteRecycleAdapter.notifyDataSetChanged();
    }

    private void initilizeDisplayContent() {


                   final RecyclerView recycleNotes= (RecyclerView) findViewById(R.id.list_notes);

                   final LinearLayoutManager notesLinerLayoutManger=new LinearLayoutManager(NotelistActivity.this);
                   recycleNotes.setLayoutManager(notesLinerLayoutManger);

                   List<NoteInfo> notes=DataManager.getInstance().getNotes();
                    noteRecycleAdapter = new NoteRecycleAdapter(this, (Cursor) notes);
                    recycleNotes.setAdapter(noteRecycleAdapter);
                   // recycleNotes.notify();
    }



}


