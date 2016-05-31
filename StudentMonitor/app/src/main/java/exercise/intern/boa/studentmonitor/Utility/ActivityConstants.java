package exercise.intern.boa.studentmonitor.Utility;

/**
 * Created by HariharanSridharan on 5/17/16.
 *
 * A class which holds String Constants that are used while invoking Intents.
 * Contains only Static field members used only by Intents
 */
public class ActivityConstants {


    private ActivityConstants(){
        //Only static members
    }


    public final static String CALLING_ACTIVITY="Calling_Activity";

    public final static String FROM_EDIT_STUDENT_ACTIVITY="Edit Screen";

    public  final static String STUDENTID="StudentId";

    public  final static String STUDENTDATA="StudentData";


    public final static int MAIN_ACTIVITY=1;

    public final static int STUDENT_REGISTRATION_ACTIVITY=2;

    public final static int VIEW_CHILD_ACTIVITY=3;

    public final static int EDIT_CHILD_ACTIVITY=4;

    public final static int REGISTRATION_MIN_AGE=5;

    public final static int REGISTRATION_MAX_AGE=12;

    public final static int VIEW_CHILD_SURVEY=7;

    public final static String SURVEYACTIVITY="SurveyActivity";
    public final static int BD_DP_VAL=11;
    public final static int WK_DP_VAL=12;
    public final static int BD_TP_VAL=13;
    public final static int WK_TP_VAL=14;

    public static String MIN_DATE="MinDate";
    public static String MAX_DATE="MaxDate";

}
