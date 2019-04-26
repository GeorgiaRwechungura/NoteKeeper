package com.jwhh.jim.notekeeper;

import android.content.Intent;
import android.os.Bundle;

import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    public  static final  String NOTE_POSITION= "com.jwhh.jim.notekeeper.NOTE_POSITION";

    public static final  String ORIGINAL_NOTE_COURSE_ID="com.jwhh.jim.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final  String ORIGINAL_NOTE_COURSE_TITTLE="com.jwhh.jim.notekeeper.ORIGINAL_NOTE_COURSE_TITTLE";

    public static final  String ORIGINAL_NOTE_COURSE_TEXT="com.jwhh.jim.notekeeper.ORIGINAL_NOTE_COURSE_TEXT";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo mNote;
    private boolean mIsNewNote;
    private Spinner mSpinnerCourses;
    private EditText mEditTittle;
    private EditText mEditText;
    private int mnNotePosition;
    private boolean isCancelling;
    private String mOriginalNoteCourseId;
    private String mOriginalTittle;
    private String mOriginalTetxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mSpinnerCourses = (Spinner) findViewById(R.id.spinner_courses);

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCourses.setAdapter(adapterCourses);

            readDisplayStateValues();
            if(savedInstanceState==null){
                saveOriginalNoteValues();

            }else{
                rstoreOriginalNoteValues(savedInstanceState);
            }


        mEditTittle = findViewById(R.id.text_note_title);
        mEditText = findViewById(R.id.text_note_text);

        if(!mIsNewNote){
            dispayNote(mSpinnerCourses, mEditTittle, mEditText);
        }

    }

    private void rstoreOriginalNoteValues(Bundle savedInstanceState) {
        mOriginalNoteCourseId=savedInstanceState.getString(ORIGINAL_NOTE_COURSE_ID);
        mOriginalTittle=savedInstanceState.getString(ORIGINAL_NOTE_COURSE_TITTLE);
        mOriginalTetxt=savedInstanceState.getString(ORIGINAL_NOTE_COURSE_TEXT);

    }

    private void saveOriginalNoteValues() {
        if(mIsNewNote){
            return;
        }
        mOriginalNoteCourseId = mNote.getCourse().getCourseId();
        mOriginalTittle = mNote.getTitle();
        mOriginalTetxt = mNote.getText();
    }


    private void readDisplayStateValues() {

        //Bundle intentData =getIntent().getExtras();

        Intent intent=getIntent();

//           if(intent!=null)
                  mnNotePosition =intent.getIntExtra(NOTE_POSITION,POSITION_NOT_SET);//intent.getExtras().getParcelable("NOTE")
                   mIsNewNote = mnNotePosition==POSITION_NOT_SET;
                   if(mIsNewNote){
                       
                       createNewNote();

                   } else {
                       mNote = DataManager.getInstance().getNotes().get(mnNotePosition);
                   }
    }

    private void createNewNote() {
        DataManager dm= DataManager.getInstance();
        mnNotePosition = dm.createNewNote();
        mNote=dm.getNotes().get(mnNotePosition);
    }

    private void dispayNote(Spinner spinnerCourses, EditText editTittle, EditText editText) {

        List <CourseInfo> courses= DataManager.getInstance().getCourses();

        int coursesIndex= courses.indexOf(mNote.getCourse());

        spinnerCourses.setSelection(coursesIndex);

        editTittle.setText(mNote.getTitle());
        editText.setText(mNote.getText());
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
          item.setEnabled(mnNotePosition<lastNoteIndex);

        return super.onPrepareOptionsMenu(menu);

    }

    private void moveNext() {
        saveNote();
        ++mnNotePosition;
        mNote=DataManager.getInstance().getNotes().get(mnNotePosition);

        saveOriginalNoteValues();
        dispayNote(mSpinnerCourses,mEditTittle,mEditText);
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(isCancelling){
             if(mIsNewNote){
                 DataManager.getInstance().removeNote(mnNotePosition);
             }else {
                 storePreviusNoteValue();
             }


        }else {
            saveNote();

        }


    }

    private void storePreviusNoteValue() {
        CourseInfo course=DataManager.getInstance().getCourse(mOriginalNoteCourseId);
        mNote.setCourse(course);
        mNote.setTitle(mOriginalTittle);
        mNote.setText(mOriginalTetxt);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(ORIGINAL_NOTE_COURSE_ID,mOriginalNoteCourseId);
        outState.putString(ORIGINAL_NOTE_COURSE_TITTLE,mOriginalTittle);
        outState.putString(ORIGINAL_NOTE_COURSE_TEXT,mOriginalTetxt);
    }

    private void saveNote() {
        mNote.setCourse((CourseInfo)mSpinnerCourses.getSelectedItem());
        mNote.setText(mEditTittle.getText().toString());
        mNote.setTitle(mEditText.getText().toString());

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

}
