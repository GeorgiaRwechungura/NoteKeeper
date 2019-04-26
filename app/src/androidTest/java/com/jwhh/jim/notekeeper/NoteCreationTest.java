package com.jwhh.jim.notekeeper;

import android.provider.ContactsContract;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.TypeTextAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.AndroidJUnitRunner;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static org.hamcrest.Matchers. *;
import static android.support.test.espresso.Espresso.pressBack;
import  static  android.support.test.espresso.assertion.ViewAssertions.*;
@RunWith(AndroidJUnit4.class)
public class NoteCreationTest {

    static DataManager sDatamanager;

@BeforeClass
    public static void classSetup()throws  Exception{
        sDatamanager= DataManager.getInstance();

    }

    @Rule
   public ActivityTestRule<NotelistActivity> mNoteNoteActivityRule=
            new ActivityTestRule<>(NotelistActivity.class);

    @Test
    public void CreateNote(){

        final  CourseInfo course=sDatamanager.getCourse("java_lang");
        final  String noteTittle="Test Note title";
        final String noteText= "This is the body of our test note";

     /*   ViewInteraction fabNewNote= onView(withId(R.id.floatingActionButton));
        fabNewNote.perform(click());*/
        onView(withId(R.id.floatingActionButton)).perform(click());

        onView(withId(R.id.spinner_courses)).perform(click());


        onData(allOf(instanceOf(CourseInfo.class),equalTo(course))).perform(click());
        onView(withId(R.id.spinner_courses)).check(matches(withSpinnerText(containsString(course.getTitle()))));

        onView(withId(R.id.text_note_title)).perform(typeText(noteTittle))
                .check(matches(withText(containsString(noteTittle))));


        onView(withId(R.id.text_note_text)).perform(typeText(noteText),
                closeSoftKeyboard());


        onView(withId(R.id.text_note_text)).check(matches(withText(containsString(noteText))));

        pressBack();


        int noteIndex=sDatamanager.getNotes().size()-1;
        NoteInfo note=sDatamanager.getNotes().get(noteIndex);

        assertEquals(course,note.getCourse());
        assertEquals(noteTittle,note.getText());
        assertEquals(noteText,note.getTitle());

    }


}