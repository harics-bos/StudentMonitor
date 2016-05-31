package exercise.intern.boa.studentmonitor.DB;

/**
 * A helper class that contains all the columns and the table structure for the Survey Table
 */
public class StudentSurveyDBQuery {

    private StudentSurveyDBQuery() {
        // Only public static members
    }

    public static final String SURVEY_TABLE = "studentsurvey";

    public static final String SID = "_id";
    public static final String S_CID = "cid";//Mapping with the Student
    public static final String BEDTIME = "bedtime";
    public static final String WAKEUPTIME = "wakeuptime";
    public static final String SCREENTIME = "screentime";
    public static final String FAMILYTIME = "familytime";
    public static final String TOTALFREETIMESPENT = "totalfreetime";
    public final static String[] SURVEY_ALL_COLUMNS = {S_CID, BEDTIME, WAKEUPTIME, SCREENTIME, FAMILYTIME};


    public final static String STUDENTSURVEY_CREATE_CMD =

            "CREATE TABLE " + SURVEY_TABLE + " ("
                    + SID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + S_CID + " INTEGER NOT NULL, "
                    + BEDTIME + " TEXT NOT NULL, "
                    + WAKEUPTIME + " TEXT NOT NULL, "
                    + SCREENTIME + " TEXT NOT NULL, "
                    + FAMILYTIME + " TEXT NOT NULL, "
                    + TOTALFREETIMESPENT + " TEXT NOT NULL,"
                    + " FOREIGN KEY(" + S_CID + ") REFERENCES " + StudentDBQuery.STUDENT_TABLE + "(" + StudentDBQuery.CID + ")"
                    + ")";


}
