package com.jwhh.jim.notekeeper;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.PersistableBundle;
/*import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;*/
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.jwhh.jim.notekeeper.NoteKeeperDatabaseContract.NoteInfoEntry;

import java.util.List;

import static com.jwhh.jim.notekeeper.NoteKeeperDatabaseContract.*;

public class NoteActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public  static final  String NOTE_ID= "com.jwhh.jim.notekeeper.NOTE_POSITION";

    public static final  String ORIGINAL_NOTE_COURSE_ID="com.jwhh.jim.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final  String ORIGINAL_NOTE_COURSE_TITTLE="com.jwhh.jim.notekeeper.ORIGINAL_NOTE_COURSE_TITTLE";

    public static final  String ORIGINAL_NOTE_COURSE_TEXT="com.jwhh.jim.notekeeper.ORIGINAL_NOTE_COURSE_TEXT";
    public static final int ID_NOT_SET = -1;
    public static final int LOADER_NOTES = 0;
    public static final int LOADER_COURSES = 1;
    private NoteInfo mNote;
    private boolean mIsNewNote;
    private Spinner mSpinnerCourses;
    private EditText mEditTittle;
    private EditText mEditText;
    private int mNoteId;
    private boolean isCancelling;
    private String mOriginalNoteCourseId;
    private String mOriginalTittle;
    private String mOriginalTetxt;
    private NoteKeeperOpenHelper mDbOpenHelper;
    private Cursor mNoteCursor;
    private int mCourseIdPos;
    private int mNoteTitlePos;
    private int mNoteTextPos;
    private SimpleCursorAdapter mAdapterCoures;
    private boolean mCourseQueryFinished;
    private boolean mNoteQuerryFinished;

    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mDbOpenHelper = new NoteKeeperOpenHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mSpinnerCourses = (Spinner) findViewById(R.id.spinner_courses);

      //  List<CourseInfo> courses = DataManager.getInstance().getCourses();
        mAdapterCoures = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, null,
                   new String[]{CourseInfoEntry.COLUMN_COURSE_TITLE},
                new int[]{android.R.id.text1},0);

        mAdapterCoures.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCourses.setAdapter(mAdapterCoures);

       getSupportLoaderManager().initLoader(LOADER_COURSES,null,this);

            readDisplayStateValues();
            if(savedInstanceState==null){
                //TODO, cam back to this and make it work
               // saveOriginalNoteValues();

            }else{
                rstoreOriginalNoteValues(savedInstanceState);
            }


        mEditTittle = findViewById(R.id.text_note_title);
        mEditText = findViewById(R.id.text_note_text);

        if(!mIsNewNote){
           // dispayNote();
            getSupportLoaderManager().initLoader(LOADER_NOTES, null, this);
        }

    }

    private void loadCourseData() {

        SQLiteDatabase db=mDbOpenHelper.getReadableDatabase();

        String courseColumns[]={CourseInfoEntry.COLUMN_COURSE_TITLE,
                                 CourseInfoEntry.COLUMN_COURSE_ID,
                                   CourseInfoEntry._ID};
        Cursor cursor=db.query(CourseInfoEntry.TABLE_NAME,courseColumns,null,null,
                               null,null,CourseInfoEntry.COLUMN_COURSE_TITLE);
                          mAdapterCoures.changeCursor(cursor);

    }

    private void loadNoteData() {
      SQLiteDatabase db=mDbOpenHelper.getReadableDatabase();

      String courseId= "android_intents";
      String titleStart="dynamic";
      String selection= NoteInfoEntry._ID + " = ? ";

      String selectionArgs[]={Integer.toString(mNoteId)};

      String noteColums[]={
              NoteInfoEntry.COLUMN_COURSE_ID,
              NoteInfoEntry.COLUMN_NOTE_TITLE,
              NoteInfoEntry.COLUMN_NOTE_TEXT

      };
        mNoteCursor = db.query(NoteInfoEntry.TABLE_NAME,noteColums,selection,selectionArgs,null,null,null);

        mCourseIdPos = mNoteCursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);
        mNoteTitlePos = mNoteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        mNoteTextPos = mNoteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT);

        mNoteCursor.moveToNext();
        displayNote();




    }

    private void rstoreOriginalNoteValues(Bundle savedInstanceState) {
        mOriginalNoteCourseId=savedInstanceState.getString(ORIGINAL_NOTE_COURSE_ID);
        mOriginalTittle=savedInstanceState.getString(ORIGINAL_NOTE_COURSE_TITTLE);
        mOriginalTetxt=savedInstanceState.getString(ORIGINAL_NOTE_COURSE_TEXT);

    }

    //TODO came back to this and make it work

    private void saveOriginalNoteValues() {
        if(mIsNewNote){
            return;
        }
       mOriginalNoteCourseId =  mNote.getCourse().getCourseId();
        mOriginalTittle = mNote.getTitle();
        mOriginalTetxt = mNote.getText();


    }


    private void readDisplayStateValues() {

        Intent intent=getIntent();

                  mNoteId =intent.getIntExtra(NOTE_ID, ID_NOT_SET);//intent.getExtras().getParcelable("NOTE")
                   mIsNewNote = mNoteId == ID_NOT_SET;
                   if(mIsNewNote){
                       
                       createNewNote();

                   } /*else {
                    //   mNote = DataManager.getInstance().getNotes().get(mNoteId);
                   }*/
    }

    private void createNewNote() {
        final ContentValues values=new ContentValues();
        values.put(NoteInfoEntry.COLUMN_COURSE_ID,"");
        values.put(NoteInfoEntry.COLUMN_NOTE_TITLE,"");
        values.put(NoteInfoEntry.COLUMN_NOTE_TEXT,"");

        AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                SQLiteDatabase db= mDbOpenHelper.getReadableDatabase();
                mNoteId=(int) db.insert(NoteInfoEntry.TABLE_NAME,null,values);
                return null;
            }
        };



    }

    private void displayNote() {
        String courseId=mNoteCursor.getString(mCourseIdPos);
        String noteTitle=mNoteCursor.getString(mNoteTitlePos);
        String noteText=mNoteCursor.getString(mNoteTextPos);

        int coursesIndex= getIndexOfCourseId(courseId);

        mSpinnerCourses.setSelection(coursesIndex);

        mEditTittle.setText(noteTitle);
        mEditText.setText(noteText);
    }

    private int getIndexOfCourseId(String courseId) {

        Cursor cursor=mAdapterCoures.getCursor();
        int courseIdPos=cursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_ID);

        int courseRowIndex=0;

        boolean more= cursor.moveToFirst();
        while (more){
            String coursorCourseId=cursor.getString(courseIdPos);
            if(courseId.equalsIgnoreCase(coursorCourseId))
                break;

                courseRowIndex++;

                more=cursor.moveToNext();


        }
        return courseRowIndex;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            sendEmail();
            return true;
        }else if(id==R.id.action_cancel){
            isCancelling = true;

            finish();//The moment to exit the activity,the part of this exiting method,the on Pause Method wil lbe called
        }else if(id==R.id.action_next){
            moveNext();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item=menu.findItem(R.id.action_next);
        int lastNoteIndex=DataManager.getInstance().getNotes().size()-1;
          item.setEnabled(mNoteId <lastNoteIndex);

        return super.onPrepareOptionsMenu(menu);

    }

    private void moveNext() {
        saveNote();
        ++mNoteId;
        mNote=DataManager.getInstance().getNotes().get(mNoteId);

//TODO came back and make this work
      //  saveOriginalNoteValues();
        displayNote();
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(isCancelling){
             if(mIsNewNote){
                deleteNoteFromDatabase();
             }else {
                 storePreviusNoteValue();
             }


        }else {
            saveNote();

        }


    }

    private void deleteNoteFromDatabase() {

        final String selection=NoteInfoEntry._ID + " = ? ";
        final String selectionArgs[]={Integer.toString(mNoteId)};

        AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                SQLiteDatabase db=mDbOpenHelper.getWritableDatabase();
                db.delete(NoteInfoEntry.TABLE_NAME,selection,selectionArgs);
                return null;
            }
        };
          task.execute();

    }

    private void storePreviusNoteValue() {
        CourseInfo course=DataManager.getInstance().getCourse(mOriginalNoteCourseId);
        mNote.setCourse(course);
        mNote.setTitle(mOriginalTittle);
        mNote.setText(mOriginalTetxt);

    }
    //TODO  came back and fix this,make the app work properly
   /* @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(ORIGINAL_NOTE_COURSE_ID,mOriginalNoteCourseId);
        outState.putString(ORIGINAL_NOTE_COURSE_TITTLE,mOriginalTittle);
        outState.putString(ORIGINAL_NOTE_COURSE_TEXT,mOriginalTetxt);
    }*/

    private void saveNote() {

        String courseId=selectedCourseId();
      String noteTitle=  mEditTittle.getText().toString();
      String noteText=  mEditText.getText().toString();
      saveNoteToDataBase(courseId,noteTitle,noteText);

    }

    private String selectedCourseId() {

    int selectedPosition=mSpinnerCourses.getSelectedItemPosition();
    Cursor cursor= mAdapterCoures.getCursor();
    cursor.moveToPosition(selectedPosition);
    int courseIdPos=cursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_ID);
    String courseId=cursor.getString(courseIdPos);

    return courseId;
    }

    private void saveNoteToDataBase(String courseId,String noteTitle,  String noteText ){
        final String selection=NoteInfoEntry._ID + " = ? ";
        final String selectionArgs[]={Integer.toString(mNoteId)};

        final ContentValues values=new ContentValues();
        values.put(NoteInfoEntry.COLUMN_COURSE_ID,courseId);
        values.put(NoteInfoEntry.COLUMN_NOTE_TITLE,noteTitle);
        values.put(NoteInfoEntry.COLUMN_NOTE_TEXT,noteText);

        AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                SQLiteDatabase db= mDbOpenHelper.getWritableDatabase();
                db.update(NoteInfoEntry.TABLE_NAME,values,selection,selectionArgs);

                return null;
            }

        };
        task.execute();



    }


    private void sendEmail() {

        CourseInfo course= (CourseInfo) mSpinnerCourses.getSelectedItem();

        String subject=mEditTittle.getText().toString();

        String text= "Checkout what I have lerned in the Plural Sight Course \"" + " \"" + course.getTitle()+ "\"" + mEditText.getText().toString() ;

        Intent intent=new Intent(Intent.ACTION_SEND);
          intent.setType("message/rfc822");
          intent.putExtra(Intent.EXTRA_SUBJECT,subject);
          intent.putExtra(intent.EXTRA_TEXT,text);

         // startActivity(intent);
        startActivity(Intent.createChooser(intent,
      "Send Email Using: "));

    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        CursorLoader loader=null;
        if(id==LOADER_NOTES)
            loader=createLoaderNote();
else if(id==LOADER_COURSES)
       loader=createLoaderCourses();


        return loader;
    }

    private CursorLoader createLoaderCourses() {
        mCourseQueryFinished = false;

        return new CursorLoader(this){
            @Override
            public Cursor loadInBackground() {

                SQLiteDatabase db=mDbOpenHelper.getReadableDatabase();

                String courseColumns[]={CourseInfoEntry.COLUMN_COURSE_TITLE,
                        CourseInfoEntry.COLUMN_COURSE_ID,
                        CourseInfoEntry._ID};

                return  db.query(CourseInfoEntry.TABLE_NAME,courseColumns,null,null,
                        null,null,CourseInfoEntry.COLUMN_COURSE_TITLE);
            }
        };
    }

    private CursorLoader createLoaderNote() {
        mNoteQuerryFinished = false;

        return new CursorLoader(this){
            @Override
            public Cursor loadInBackground() {
                SQLiteDatabase db=mDbOpenHelper.getReadableDatabase();

                String selection= NoteInfoEntry._ID + " = ? ";

                String selectionArgs[]={Integer.toString(mNoteId)};

                String noteColums[]={
                        NoteInfoEntry.COLUMN_COURSE_ID,
                        NoteInfoEntry.COLUMN_NOTE_TITLE,
                        NoteInfoEntry.COLUMN_NOTE_TEXT

                };
                return db.query(NoteInfoEntry.TABLE_NAME,noteColums,selection,selectionArgs,null,null,null);

            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        if(loader.getId()==LOADER_NOTES)
            loadFinishedNotes(data);

        else if(loader.getId()==LOADER_COURSES){
            mAdapterCoures.changeCursor(data);
            mCourseQueryFinished=true;
            displayNoteWhenQuerryFinished();
        }


    }

    private void loadFinishedNotes(Cursor data) {
        mNoteCursor=data;

        mCourseIdPos = mNoteCursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);
        mNoteTitlePos = mNoteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        mNoteTextPos = mNoteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT);
        mNoteCursor.moveToNext();
        mNoteQuerryFinished=true;
        displayNoteWhenQuerryFinished();

    }

    private void displayNoteWhenQuerryFinished() {
        if(mNoteQuerryFinished && mCourseQueryFinished){
            displayNote();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        if(loader.getId()==LOADER_NOTES){
            if(mNoteCursor!=null)
                mNoteCursor.close();

            else if(loader.getId()==LOADER_COURSES){
                mAdapterCoures.changeCursor(null);
            }


        }


    }
}
