package com.jwhh.jim.notekeeper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
/*import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/
import android.preference.PreferenceManager;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static com.jwhh.jim.notekeeper.NoteActivity.LOADER_NOTES;
import static com.jwhh.jim.notekeeper.NoteKeeperDatabaseContract.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    private NoteRecycleAdapter mNoteRecycleAdapter;
    private RecyclerView mRecycleItems;
    private LinearLayoutManager mNotesLinerLayoutManger;
    private GridLayoutManager mCourseLayoutManger;
    private CourseRecycleAdapter mCourseRecycleAdapter;
    private NoteKeeperOpenHelper mDbOpenHelper;

    List<CourseInfo> courses = new ArrayList<>();

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDbOpenHelper= new NoteKeeperOpenHelper(this);

        mCourseRecycleAdapter = new CourseRecycleAdapter(this,courses);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,NoteActivity.class);
                startActivity(intent);
            }
        });

//        PreferenceManager.setDefaultValues(this,R.xml.root_preferences,false);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);

      //Come back and fix the coulor of the toggle

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        initilizeDisplayContent();

    }

    @Override
    protected void onDestroy() {
         mDbOpenHelper.close();
        super.onDestroy();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();


        getSupportLoaderManager().restartLoader(LOADER_NOTES,null,this);

         //TODO  //Came back to this method and make updateNavHeader(): it work
        updateNavHeader();
    }

    private void loadNotes() {
       SQLiteDatabase db= mDbOpenHelper.getReadableDatabase();

        final String[] noteColumns = {
                NoteInfoEntry.COLUMN_NOTE_TITLE,
                NoteInfoEntry.COLUMN_COURSE_ID,
                NoteInfoEntry._ID
        };

        String noteOrderBy = NoteInfoEntry.COLUMN_COURSE_ID + "," + NoteInfoEntry.COLUMN_NOTE_TITLE;

        final Cursor noteCursor = db.query(NoteInfoEntry.TABLE_NAME, noteColumns,
                null, null, null, null, noteOrderBy);
        mNoteRecycleAdapter.changeCursor(noteCursor);

    }


    private void updateNavHeader() {
        NavigationView navigationView=findViewById(R.id.nav_view);
        View headerView=navigationView.getHeaderView(0);
        TextView usernaTextView=headerView.findViewById(R.id.text_user_name);
        TextView emailAdressTextView=headerView.findViewById(R.id.text_email_adress);


        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(this);
        String userName=preferences.getString("user_display_name"," ");
        String  emailAdress=preferences.getString("user_email_address"," ");

       usernaTextView.setText(userName);
        emailAdressTextView.setText(emailAdress);

    }

    private void initilizeDisplayContent() {

       DataManager.loadFromDatabase(mDbOpenHelper);

        mRecycleItems = (RecyclerView) findViewById(R.id.list_items);

        mNotesLinerLayoutManger = new LinearLayoutManager(this);
        mCourseLayoutManger = new GridLayoutManager(this,getResources().getInteger(R.integer.course_grid_span));


        mNoteRecycleAdapter = new NoteRecycleAdapter(this,null);

        courses= DataManager.getInstance().getCourses();
           displayNotes();

        // recycleNotes.notify();
    }

    private void displayNotes() {

        mRecycleItems.setLayoutManager(mNotesLinerLayoutManger);

        mRecycleItems.setAdapter(mNoteRecycleAdapter);


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
            Intent intent=new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(intent);
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

           // handleSelection(( R.string.nave_share_message));
            handleShare();
        } else if (id == R.id.nav_send) {
            handleSelection((R.string.nav_send_message));

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("NewApi")
    private void handleShare() {

        View view=findViewById(R.id.list_items);
        Snackbar.make(view, "Share to " +
                PreferenceManager.getDefaultSharedPreferences(this).getString("user_favorite_social",""),
                        Snackbar.LENGTH_LONG).show();


    }

    private void handleSelection(int message_id) {

        View view=findViewById(R.id.list_items);
        Snackbar.make(view,message_id,Snackbar.LENGTH_LONG).show();

    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        CursorLoader loader=null;
            if(id==LOADER_NOTES){
                loader=new CursorLoader(this){
                    @Override
                    public Cursor loadInBackground() {

                        SQLiteDatabase db=mDbOpenHelper.getReadableDatabase();

                        final String noteOrderBy= CourseInfoEntry.COLUMN_COURSE_TITLE  + "," + NoteInfoEntry.COLUMN_NOTE_TITLE;


                        String noteColums[]={
                                NoteInfoEntry.getQName(NoteInfoEntry._ID),
                                NoteInfoEntry.COLUMN_NOTE_TITLE,
                                CourseInfoEntry.COLUMN_COURSE_TITLE

                        };

                        //note_info JOIN course_info ON note_info.course_id = course_info.course.id
                        String tablesWithJoin= NoteInfoEntry.TABLE_NAME + " JOIN " +
                                CourseInfoEntry.TABLE_NAME + " ON " +
                                NoteInfoEntry.getQName(NoteInfoEntry.COLUMN_COURSE_ID) + " = " +
                                CourseInfoEntry.getQName(CourseInfoEntry.COLUMN_COURSE_ID);


                        return db.query(tablesWithJoin,noteColums,null,null,null,null,noteOrderBy);

                    }
                };
            }
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(loader.getId()==LOADER_NOTES){
            mNoteRecycleAdapter.changeCursor(data);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mNoteRecycleAdapter.changeCursor(null);

    }
}
