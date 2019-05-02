package com.jwhh.jim.notekeeper;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
/*import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/
import android.view.View;
/*import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;*/
import android.view.MenuItem;
/*
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
*/

/*import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;*/
import android.view.Menu;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NoteRecycleAdapter noteRecycleAdapter;
    private RecyclerView mRecycleItems;
    private LinearLayoutManager mNotesLinerLayoutManger;
    private GridLayoutManager mCourseLayoutManger;
    private CourseRecycleAdapter mCourseRecycleAdapter;


    @SuppressLint("ResourceAsColor")
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
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);

       /* ActionBar actionBar = getActionBar();

        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
      //  drawer.setBackgroundColor(R.color.hamburgerWhite);
               // drawer.getDrawer.setColor(getResources().getColor(R.color.hamburgerWhite);

        drawerLayout.addDrawerListener(toggle);
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
        mCourseLayoutManger = new GridLayoutManager(this,getResources().getInteger(R.integer.course_grid_span));


        List<NoteInfo> notes=DataManager.getInstance().getNotes();
        noteRecycleAdapter = new NoteRecycleAdapter(this,notes);

        List<CourseInfo> courses= DataManager.getInstance().getCourses();
        mCourseRecycleAdapter = new CourseRecycleAdapter(this,courses);
           displayNotes();

        // recycleNotes.notify();
    }

    private void displayNotes() {

        mRecycleItems.setLayoutManager(mNotesLinerLayoutManger);

        mRecycleItems.setAdapter(noteRecycleAdapter);
        selectNavigationMenuItem(R.id.nav_notes);

    }
    private  void selectNavigationMenuItem(int id){

         NavigationView navigationView=findViewById(R.id.nav_view);  
         Menu menu=navigationView.getMenu();                         
         menu.findItem(id).setChecked(true);
    }

    private void displayCourses(){
        mRecycleItems.setLayoutManager(mCourseLayoutManger);
        mRecycleItems.setAdapter(mCourseRecycleAdapter);
        selectNavigationMenuItem(R.id.nav_courses);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
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
            displayCourses();

        } else if (id == R.id.nav_share) {

            handleSelection(( R.string.nave_share_message));
        } else if (id == R.id.nav_send) {
            handleSelection((R.string.nav_send_message));

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleSelection(int message_id) {

        View view=findViewById(R.id.list_items);
        Snackbar.make(view,message_id,Snackbar.LENGTH_LONG).show();

    }
}
