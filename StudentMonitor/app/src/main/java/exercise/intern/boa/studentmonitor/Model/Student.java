package exercise.intern.boa.studentmonitor.Model;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

import exercise.intern.boa.studentmonitor.Utility.StudentHelper;

/**
 * A model class that corresponds to Student table
 */
public class Student implements Parcelable {

    private int _id;
    private String _firstName;
    private String _lastName;
    private String _gender;
    private byte[] _profilePhoto;
    private String _dateOfBirth;
   private List<StudentActivity> _childActivities=null;

    public Student(String firstName, String lastName, String gender, byte[] profilePhoto, String dateOfBirth){
        this._firstName=firstName;
        this._lastName=lastName;
        this._gender=gender;
        this._profilePhoto=profilePhoto;
        this._dateOfBirth=dateOfBirth;

    }

    public Student(String firstName, String lastName, String gender, String dateOfBirth){
        this(firstName,lastName,gender,null,dateOfBirth);
    }

    protected Student(Parcel in) {
        _id = in.readInt();
        _firstName = in.readString();
        _lastName = in.readString();
        _gender = in.readString();
        _profilePhoto = new byte[in.readInt()];
        in.readByteArray(_profilePhoto);
        //in.readByteArray(_profilePhoto);
        _dateOfBirth = in.readString();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public int getId(){
        return _id;
    }

    public void setId(int id){
        this._id=id;
    }

    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String firstName) {
        this._firstName = firstName;
    }

    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String lastName) {
        this._lastName = lastName;
    }

    public String getGender() {
        return _gender;
    }

    public void setGender(String gender) {
        this._gender = gender;
    }

    public String getDateOfBirth() {
        return _dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this._dateOfBirth = dateOfBirth;
    }

    public List<StudentActivity> getChildActivties(){
        return _childActivities;
    }

    public void setChildActivities(List<StudentActivity> surveyList){
        this._childActivities=surveyList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(_id);
        parcel.writeString(_firstName);
        parcel.writeString(_lastName);
        parcel.writeString(_gender);
        /*dest.writeInt(_byte.length);
        dest.writeByteArray(_byte);*/
        //parcel.writeByteArray(_profilePhoto);
        parcel.writeInt(_profilePhoto.length);
        parcel.writeByteArray(_profilePhoto);
        parcel.writeString(_dateOfBirth);
    }

    public byte[] getProfilePhoto() {
        return _profilePhoto;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this._profilePhoto = profilePhoto;
    }
}
