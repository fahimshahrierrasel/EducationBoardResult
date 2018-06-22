package com.treebricks.educationboardresult.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SubjectResult implements Parcelable {
    String subjectCode;
    String subjectName;
    String grade;

    public SubjectResult(String subjectCode, String subjectName, String grade) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.grade = grade;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getGrade() {
        return grade;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.subjectCode);
        dest.writeString(this.subjectName);
        dest.writeString(this.grade);
    }

    protected SubjectResult(Parcel in) {
        this.subjectCode = in.readString();
        this.subjectName = in.readString();
        this.grade = in.readString();
    }

    public static final Parcelable.Creator<SubjectResult> CREATOR = new Parcelable.Creator<SubjectResult>() {
        @Override
        public SubjectResult createFromParcel(Parcel source) {
            return new SubjectResult(source);
        }

        @Override
        public SubjectResult[] newArray(int size) {
            return new SubjectResult[size];
        }
    };

    @Override
    public String toString() {
        return "Subject Code-"+subjectCode+", Subject Name-"+subjectName+", Grade-"+grade;
    }
}
