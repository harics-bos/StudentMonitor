package exercise.intern.boa.studentmonitor.DB;

/**
 * A helper class that contains all the columns and the table structure for the Student Table
 */
public class StudentDBQuery {

    private StudentDBQuery() {
        // Only static Methods
    }

    public final static String dbName = "schooldb1";
    public final static int VERSION = 1;

    public final static String STUDENT_TABLE = "studentinfo";
    public final static String CID = "_id";
    public final static String FIRST_NAME = "firstname";
    public final static String LAST_NAME = "lastname";
    public final static String DOB = "dateofbirth";
    public final static String GENDER = "gender";
    public final static String PROFILEPIC = "photo";
    public final static String[] STUDENT_NAME_COLUMNS = {FIRST_NAME, LAST_NAME};
    public final static String[] STUDENT_NAME_COLUMNS_WITH_IMAGE = {PROFILEPIC, FIRST_NAME, LAST_NAME};
    public final static String[] STUDENT_NAME_COLUMNS_WITH_ID = {CID, FIRST_NAME, LAST_NAME};
    public final static String[] STUDENT_NAME_COLUMNS_WITH_ID_AND_IMAGE = {CID, PROFILEPIC, FIRST_NAME, LAST_NAME};
    public final static String[] STUDENT_ALL_COLUMNS = {CID, FIRST_NAME, LAST_NAME, DOB, PROFILEPIC, GENDER};
    public final static String[] STUDENT_ID = {CID};

    public final static String[] CHILD_TABLE_COLUMNS = {CID, FIRST_NAME,
            LAST_NAME, DOB, GENDER};

    public final static String STUDENT_CREATE_CMD =

            "CREATE TABLE " + STUDENT_TABLE + " (" + CID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + PROFILEPIC + " BLOB,"
                    + FIRST_NAME + " TEXT NOT NULL,"
                    + LAST_NAME + " TEXT NOT NULL,"
                    + DOB + " TEXT NOT NULL,"
                    + GENDER + " TEXT NOT NULL)";


}
