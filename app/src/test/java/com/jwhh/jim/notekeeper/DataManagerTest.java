package com.jwhh.jim.notekeeper;

import android.provider.ContactsContract;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataManagerTest {
      static   DataManager sdDataManger;

      @BeforeClass
      public static void ClassSetup()throws Exception{
              sdDataManger= DataManager.getInstance();


      }

        @Before
        public void Setup()throws  Exception{
               // DataManager dm=DataManager.getInstance();
                sdDataManger.getNotes().clear();
                sdDataManger.initializeExampleNotes();

        }
   // private

    @Test
    public void createNewNote()throws Exception{


            //DataManager dm = DataManager.getInstance();
            final CourseInfo course= sdDataManger.getCourse("android_async");
            final String noteTitttle="Test note Tttle";
            final String noteText="This is the body text of my text note";

            int noteIndex=sdDataManger.createNewNote();
            NoteInfo newNote=sdDataManger.getNotes().get(noteIndex);
            newNote.setCourse(course);
            newNote.setTitle(noteTitttle);
            newNote.setText(noteText);

            NoteInfo compareNote=sdDataManger.getNotes().get(noteIndex);
            assertEquals(course,compareNote.getCourse());
            assertEquals(noteTitttle,compareNote.getTitle());
            assertEquals(noteText,compareNote.getText());


    }
   @Test
public void findSimilarNotes(){

        //final DataManager dm=DataManager.getInstance();
        final CourseInfo course=sdDataManger.getCourse("android_async");
        final String noteTitttle="Test note Tttle";
        final String noteText1="This is the body text of my text note";
        final String noteText2="This is the body text of my second text note";

        int noteIndex1=sdDataManger.createNewNote();
        NoteInfo newNote1= sdDataManger.getNotes().get(noteIndex1);
        newNote1.setCourse(course);
        newNote1.setTitle(noteTitttle);
        newNote1.setText(noteText1);

        int noteIndex2=sdDataManger.createNewNote();
        NoteInfo newNote2=sdDataManger.getNotes().get(noteIndex2);
        newNote2.setCourse(course);
        newNote2.setTitle(noteTitttle);
        newNote2.setText(noteText2);

        int foundIndex1=sdDataManger.findNote(newNote1);
        assertEquals(noteIndex1,foundIndex1);

        int foundIndex2=sdDataManger.findNote(newNote2);
        assertEquals(noteIndex2,foundIndex2);

}

@Test
    public void CreateNewnoteOneStepCreation(){
          final CourseInfo course=sdDataManger.getCourse("android_async");
          String noteTittle="Test Note Tittle";
          String noteText="This is the body of my text note";


          int noteIndex=sdDataManger.createNewNote(course,noteTittle,noteText);

          NoteInfo compareNote=sdDataManger.getNotes().get(noteIndex);
          assertEquals(course, compareNote.getCourse());
          assertEquals(noteTittle,compareNote.getTitle());
          assertEquals(noteText,compareNote.getText());
}
}
