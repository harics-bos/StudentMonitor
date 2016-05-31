package exercise.intern.boa.studentmonitor.DB;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

import exercise.intern.boa.studentmonitor.Model.Student;
import exercise.intern.boa.studentmonitor.Model.StudentActivity;

/**
 * Created by HariharanSridharan on 5/16/16.
 */
public interface IDBOperations {

    // Checks if the User exist in the Database.

    public int getTotalChildCount();

    public boolean checkIfUserExist(String firstName, String lastName);

    public Cursor getAllStudents();

    public boolean addStudent(Student student);

    public int getStudentId(String firstName, String lastName);

    public Student getStudent(String firstName, String lastName);

    public int updateStudent(ContentValues values,String firstName, String lastName);

    public boolean addSurvey(StudentActivity survey, int studentId);

    public boolean isSurveyEmptyForStudent(int studentid);

    public String getLatestWakeUpDay(int studentId);

    public List<Student> getAllStudentsWithSurvey();


}
