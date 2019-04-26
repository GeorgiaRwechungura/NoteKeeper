package com.jwhh.jim.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NoteRecycleAdapter noteRecycleAdapter;
    private RecyclerView mRecycleItems;
    private LinearLayoutManager mNotesLinerLayoutManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,NoteActivity.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        initilizeDisplayContent();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();

        //mAdapterInfo.notifyDataSetChanged();
        noteRecycleAdapter.notifyDataSetChanged();
    }

    private void initilizeDisplayContent() {


        mRecycleItems = (RecyclerView) findViewById(R.id.list_items);

        mNotesLinerLayoutManger = new LinearLayoutManager(this);


        List<NoteInfo> notes=DataManager.getInstance().getNotes();
        noteRecycleAdapter = new NoteRecycleAdapter(this,notes);
        displayNotes();

        // recycleNotes.notify();
    }

    private void displayNotes() {

        mRecycleItems.setLayoutManager(mNotesLinerLayoutManger);

        mRecycleItems.setAdapter(noteRecycleAdapter);
        NavigationView navigationView=findViewById(R.id.nav_view);
        Menu menu=navigationView.getMenu();
        menu.findItem(R.id.nav_notes).setChecked(true);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notes) {
            displayNotes();
           // handleSelection("Notes");
            // Handle the camera action
        } else if (id == R.id.nav_courses) {
            handleSelection(" Courses");

        } else if (id == R.id.nav_share) {

            handleSelection("Don't you think you have shared enough?");
        } else if (id == R.id.nav_send) {
            handleSelection("Send");

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleSelection(String message) {

        View view=findViewById(R.id.list_items);
        Snackbar.make(view,message,Snackbar.LENGTH_LONG).show();

    }
}
