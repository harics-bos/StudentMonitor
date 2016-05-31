package exercise.intern.boa.studentmonitor.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;

import java.util.ArrayList;

import java.sql.Blob;
import java.util.List;

import exercise.intern.boa.studentmonitor.Model.Student;
import exercise.intern.boa.studentmonitor.Model.StudentActivity;
import exercise.intern.boa.studentmonitor.Utility.StudentHelper;

/**
 * Implements the IDBOperations interface. Provides all the required database related transactions
 */
public class DBOperationsImpl implements IDBOperations {

    private DatabaseOpenHelper _dbAccessor = null;

    public DBOperationsImpl(Context context) {
        _dbAccessor = DatabaseOpenHelper.getInstance( context );

    }

    @Override
    public int getTotalChildCount() {
        String count_query = "SELECT COUNT(*) FROM " + StudentDBQuery.STUDENT_TABLE;
        int numOfRows = (int) DatabaseUtils.longForQuery( _dbAccessor.getReadableDatabase(), count_query, null );
        return numOfRows;
    }

    @Override
    public boolean checkIfUserExist(String firstName, String lastName) {

        String count_query = "SELECT COUNT(*) FROM " + StudentDBQuery.STUDENT_TABLE;
        int numOfRows = (int) DatabaseUtils.longForQuery( _dbAccessor.getReadableDatabase(), count_query, null );
        return numOfRows > 0;
    }

    @Override
    public Cursor getAllStudents() {

        return _dbAccessor.getWritableDatabase().query( StudentDBQuery.STUDENT_TABLE,
                StudentDBQuery.STUDENT_NAME_COLUMNS_WITH_ID_AND_IMAGE, null, new String[]{}, null, null,
                null );
    }

    @Override
    public boolean addStudent(Student student) {
        boolean result = true;
        ContentValues values = new ContentValues();
        values.put( StudentDBQuery.FIRST_NAME, student.getFirstName() );
        values.put( StudentDBQuery.LAST_NAME, student.getLastName() );
        values.put( StudentDBQuery.GENDER, student.getGender() );
        values.put( StudentDBQuery.DOB, student.getDateOfBirth() );
        values.put( StudentDBQuery.PROFILEPIC, student.getProfilePhoto() );
        long rowId = _dbAccessor.getWritableDatabase().insert( StudentDBQuery.STUDENT_TABLE, null, values );
        // If any error occurs during insert, -1 will be returned
        return rowId!=-1;
    }

    @Override
    public int getStudentId(String firstName, String lastName) {
        int id = -1;
        Cursor studentCursor = _dbAccessor.getWritableDatabase().query( StudentDBQuery.STUDENT_TABLE,
                StudentDBQuery.STUDENT_ID, null, new String[]{firstName, lastName}, null, null,
                null );
        if (studentCursor.moveToFirst()) {
            id = studentCursor.getInt( studentCursor.getColumnIndex( StudentDBQuery.CID ) );
        }
        return id;
    }

    @Override
    public int updateStudent(ContentValues values, String firstName, String lastName) {
        String[] whereArgs = new String[]{firstName, lastName};
        String whereClause = StudentDBQuery.FIRST_NAME + "=?" + " AND " + StudentDBQuery.LAST_NAME + "=?";
        // String whereClause = StudentDBQuery.FIRST_NAME + " =?"+" AND "+StudentDBQuery.LAST_NAME +" =?";

        //String[] whereArgs = { "2"};
        //String whereClause = StudentDBQuery.CID + " =?";

        int affectedRows = _dbAccessor.getWritableDatabase().update(
                StudentDBQuery.STUDENT_TABLE,
                values,
                whereClause,
                whereArgs
        );

        return affectedRows;
    }

    @Override
    public Student getStudent(String firstName, String lastName) {
        Student studentInfo = null;
        String whereClauseColns = StudentDBQuery.FIRST_NAME + "=?" + " AND " + StudentDBQuery.LAST_NAME + "=?";
        Cursor studentCursor = _dbAccessor.getWritableDatabase().query( StudentDBQuery.STUDENT_TABLE,
                StudentDBQuery.STUDENT_ALL_COLUMNS, whereClauseColns, new String[]{firstName, lastName}, null, null,
                null );
        if (studentCursor.moveToFirst()) {
            String fName = studentCursor.getString( studentCursor.getColumnIndex( StudentDBQuery.FIRST_NAME ) );
            String lName = studentCursor.getString( studentCursor.getColumnIndex( StudentDBQuery.LAST_NAME ) );
            String dob = studentCursor.getString( studentCursor.getColumnIndex( StudentDBQuery.DOB ) );
            String gender = studentCursor.getString( studentCursor.getColumnIndex( StudentDBQuery.GENDER ) );
            byte[] profilePic = studentCursor.getBlob( studentCursor.getColumnIndex( StudentDBQuery.PROFILEPIC ) );
            int id = studentCursor.getInt( studentCursor.getColumnIndex( StudentDBQuery.CID ) );
            studentInfo = new Student( fName, lName, gender, profilePic, dob );
            studentInfo.setId( id );
        }
        studentCursor.close();
        return studentInfo;
    }

    /*
            Used to insert Survey details of the student in the survey table.
     */
    @Override
    public boolean addSurvey(StudentActivity survey, int studentId) {
        boolean result = true;

        Cursor cursor = _dbAccessor.getReadableDatabase()
                .rawQuery( "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + StudentSurveyDBQuery.SURVEY_TABLE + "'", null );
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                //return true;
            }
            cursor.close();
        }
        ContentValues values = new ContentValues();
        values.put( StudentSurveyDBQuery.S_CID, studentId );
        values.put( StudentSurveyDBQuery.BEDTIME, survey.getBedTime() );
        values.put( StudentSurveyDBQuery.WAKEUPTIME, survey.getWokeUpTime() );
        values.put( StudentSurveyDBQuery.SCREENTIME, survey.getScreenTime() );
        values.put( StudentSurveyDBQuery.FAMILYTIME, survey.getFamilyTime() );
        values.put( StudentSurveyDBQuery.TOTALFREETIMESPENT, "09:00" );
        long rowId = _dbAccessor.getWritableDatabase().insert( StudentSurveyDBQuery.SURVEY_TABLE, null, values );
        // If any error occurs during insert, -1 will be returned
        if (rowId == -1)
            result = false;
        return result;
    }

    /*
           Checks if the survey for a student is empty or not
     */
    public boolean isSurveyEmptyForStudent(int studentid) {

        String count_query = "SELECT COUNT(*) FROM " + StudentSurveyDBQuery.SURVEY_TABLE + " where " + StudentSurveyDBQuery.S_CID + "=" + studentid;
        int numOfRows = (int) DatabaseUtils.longForQuery( _dbAccessor.getReadableDatabase(), count_query, null );
        return numOfRows == 0;

    }

    public String getLatestWakeUpDay(int studentId) {
        String wakeUpTime = null;
        String latestWakeUpDateQuery = "select " + StudentSurveyDBQuery.WAKEUPTIME + " from " + StudentSurveyDBQuery.SURVEY_TABLE
                + " where " + StudentSurveyDBQuery.S_CID + "=" + studentId + " ORDER BY " + StudentSurveyDBQuery.SID + " DESC LIMIT 1";

        Cursor surveyCursor = _dbAccessor.getReadableDatabase()
                .rawQuery( latestWakeUpDateQuery, null );
        if (surveyCursor != null) {
            if (surveyCursor.moveToFirst()) {

                wakeUpTime = surveyCursor.getString( surveyCursor.getColumnIndex( StudentSurveyDBQuery.WAKEUPTIME ) );
            }

            surveyCursor.close();

        }

        return wakeUpTime;
    }

    /*
    *
    *   Returns all the students with their survey activitie. To be used by ExapndableListAdapter
    *
     */
    @Override
    public List<Student> getAllStudentsWithSurvey() {
        List<Student> students = new ArrayList<Student>();

        Cursor studentCursor = _dbAccessor.getWritableDatabase().query( StudentDBQuery.STUDENT_TABLE,
                StudentDBQuery.STUDENT_ALL_COLUMNS, null, new String[]{}, null, null,
                null );

        //Cursor studentCursor=getAllStudents();

        while (studentCursor.moveToNext()) {

            int studentId = studentCursor.getInt( studentCursor.getColumnIndex( StudentDBQuery.CID ) );
            String fName = studentCursor.getString( studentCursor.getColumnIndex( StudentDBQuery.FIRST_NAME ) );
            String lName = studentCursor.getString( studentCursor.getColumnIndex( StudentDBQuery.LAST_NAME ) );
            String gender = studentCursor.getString( studentCursor.getColumnIndex( StudentDBQuery.GENDER ) );
            String dob = studentCursor.getString( studentCursor.getColumnIndex( StudentDBQuery.DOB ) );
            byte[] profilePic = studentCursor.getBlob( studentCursor.getColumnIndex( StudentDBQuery.PROFILEPIC ) );
            //Bitmap scaledPic=Bitmap.createScaledBitmap(StudentHelper.getImage(profilePic), 360, 360, false);
            Student student = new Student( fName, lName, gender, profilePic, dob );
            Cursor surveyCursor = _dbAccessor.getWritableDatabase().query( StudentSurveyDBQuery.SURVEY_TABLE,
                    StudentSurveyDBQuery.SURVEY_ALL_COLUMNS, StudentSurveyDBQuery.S_CID + "=?",
                    new String[]{Integer.toString( studentId )}, null, null,
                    null );
            List<StudentActivity> surveyList = new ArrayList<StudentActivity>();
            while (surveyCursor.moveToNext()) {
                String bedTime = surveyCursor.getString( surveyCursor.getColumnIndex( StudentSurveyDBQuery.BEDTIME ) );
                String wakeupTime = surveyCursor.getString( surveyCursor.getColumnIndex( StudentSurveyDBQuery.WAKEUPTIME ) );
                String screenTime = surveyCursor.getString( surveyCursor.getColumnIndex( StudentSurveyDBQuery.SCREENTIME ) );
                String familyTime = surveyCursor.getString( surveyCursor.getColumnIndex( StudentSurveyDBQuery.FAMILYTIME ) );
                StudentActivity survey = new StudentActivity( bedTime, wakeupTime, screenTime, familyTime );
                surveyList.add( survey );
            }

            student.setChildActivities( surveyList );
            students.add( student );
        }


        return students;

    }
}
