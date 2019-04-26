package com.jwhh.jim.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
                //   startActivity( new Intent(NotelistActivity.this,NoteActivity.class));

                //  Toast.makeText(getApplicationContext(),"am here",Toast.LENGTH_LONG).show();
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

                   final LinearLayoutManager notesLinerLayoutManger=new LinearLayoutManager(this);
                   recycleNotes.setLayoutManager(notesLinerLayoutManger);

                   List<NoteInfo> notes=DataManager.getInstance().getNotes();
                    noteRecycleAdapter = new NoteRecycleAdapter(this,notes);
                    recycleNotes.setAdapter(noteRecycleAdapter);
                   // recycleNotes.notify();
    }



}


