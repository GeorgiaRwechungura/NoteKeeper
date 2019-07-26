package com.jwhh.jim.notekeeper;

import android.app.IntentService;
import android.content.Intent;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class NoteServiceBackup extends IntentService {

    public static final String EXTRA_COURSE_ID = "com.jwhh.jim.notekeeper.extra.COURSE_ID";

    public NoteServiceBackup() {

        super("NoteServiceIntent");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String backupCourseId=intent.getStringExtra(EXTRA_COURSE_ID);
            NoteBackup.doBackup(this,backupCourseId);



        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */

}
