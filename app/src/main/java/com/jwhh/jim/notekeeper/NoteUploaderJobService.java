package com.jwhh.jim.notekeeper;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NoteUploaderJobService extends JobService {
public static String EXTRA_DATA_URI="com.jwhh.jim.notekeeper.DATA_URI";
    private NoteUploader mNoteUploader;


    public NoteUploaderJobService() {
    }

    @Override
    public boolean onStartJob(JobParameters params) {

        AsyncTask<JobParameters,Void,Void> task=new AsyncTask<JobParameters, Void, Void>() {
            @Override
            protected Void doInBackground(JobParameters...backgroundParams) {

                JobParameters jobParameters=backgroundParams[0];
               String stringDataUri= jobParameters.getExtras().getString(EXTRA_DATA_URI);

              Uri dataUri=  Uri.parse(stringDataUri);
              mNoteUploader.doUpload(dataUri);

              jobFinished(jobParameters,false);
                return null;
            }
        };

        mNoteUploader = new NoteUploader(this);
         task.execute(params);


        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }


}
