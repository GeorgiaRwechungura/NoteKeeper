package com.jwhh.jim.notekeeper;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Jim.
 */

public final class NoteInfo implements Parcelable {
    private CourseInfo mCourse;
    private String mTitle;
    private String mText;
    private int mId;

    public NoteInfo(int id,CourseInfo course, String title, String text) {
        mId = id;
        mCourse = course;
        mTitle = title;
        mText = text;
    }

    public NoteInfo(Parcel source) {
        mCourse=source.readParcelable(CourseInfo.class.getClassLoader());

        Log.v("LoghereS ", " source"+ source);
        mTitle=source.readString();
        mText=source.readString();
    }

    public NoteInfo(Integer integer, Object course, Object title, Object text) {

    }

    public CourseInfo getCourse() {
        return mCourse;
    }

    public void setCourse(CourseInfo course) {
        this.mCourse = course;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getText() {
        return mText;
    }
    public int getId() { return mId; }


    public void setText(String text) {
        this.mText = text;
    }

    private String getCompareKey() {
        return mCourse.getCourseId() + "|" + mTitle + "|" + mText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteInfo that = (NoteInfo) o;

        return getCompareKey().equals(that.getCompareKey());
    }

    @Override
    public int hashCode() {
        return getCompareKey().hashCode();
    }

    @Override
    public String toString() {
        return getCompareKey();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      //   dest.writeInt(mId);
         dest.writeParcelable(mCourse,0);
         dest.writeString(mTitle);
         dest.writeString(mText);

    }
    public final static Parcelable.Creator <NoteInfo>CREATOR =  new Parcelable.Creator<NoteInfo>(){

        @Override
        public NoteInfo createFromParcel(Parcel source)
        {
            return new NoteInfo(source);
        }

        @Override
        public NoteInfo[] newArray(int size) {
            return new NoteInfo[size];
        }
    };
}
