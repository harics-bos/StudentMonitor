package exercise.intern.boa.studentmonitor.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HariharanSridharan on 5/16/16.
 * <p/>
 * Singleton Implementation to prevent Memory Leak.
 * Maintaining single instance of SQlite Database throughout the application.
 * Note:Intentionally it is not made Thread safe because the requirements doesn't make way for concurrent usage of the database.
 * Making it Thread safe would just be an overhead for this app in terms of performance.
 */
class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static DatabaseOpenHelper _dbInstance = null;
    private Context _mContext;

    private DatabaseOpenHelper(Context context) {
        // This call to the super class creates the db post which our onCreate() gets called.
        super( context, StudentDBQuery.dbName, null, StudentDBQuery.VERSION );
        this._mContext = context;
    }

    public static DatabaseOpenHelper getInstance(Context context) {
        if (_dbInstance == null) {
            _dbInstance = new DatabaseOpenHelper( context.getApplicationContext() );
        }

        return _dbInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL( StudentDBQuery.STUDENT_CREATE_CMD );
        sqLiteDatabase.execSQL( StudentSurveyDBQuery.STUDENTSURVEY_CREATE_CMD );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Do Nothing
    }
}
