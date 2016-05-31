package exercise.intern.boa.studentmonitor.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import exercise.intern.boa.studentmonitor.DB.DBOperationsImpl;
import exercise.intern.boa.studentmonitor.DB.IDBOperations;
import exercise.intern.boa.studentmonitor.R;
import exercise.intern.boa.studentmonitor.Utility.ActivityConstants;


/**
 * Entry Point User Interface for the app.
 * Provides User Interface for the following functions:
 * i)   Adding New Child
 * ii)  Editing the Child Information
 * iii) Adding the survey report for the child
 *  iv) Viewing the Survey Report
 */

public class MainActivity extends AppCompatActivity {

    private Button _viewReport;
    private Button _editChild;
    private Button _survey;


    /*
    *    Sets the User Interface for this screen by specifying the layout file
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _viewReport = (Button) findViewById(R.id.viewReport);
        _editChild = (Button) findViewById(R.id.editChild);
        _survey = (Button) findViewById(R.id.viewSurvey);
    }


    /*
    *   Initializes the activity state. Checks if there exists atleast one child and enables the
     *   View Survey Report, Edit Child and Add Survey buttons accordingly
    * */
    @Override
    protected void onStart() {
        super.onStart();
        IDBOperations dbOperations = new DBOperationsImpl(MainActivity.this);
        performButtonEnablityVal(dbOperations.getTotalChildCount()>0);
    }


    /*
    *   Enables/Disables the View Survey Report, Edit Child and Add Survey buttons
    * */
    private void performButtonEnablityVal(boolean activate){
            _viewReport.setEnabled(activate);
            _editChild.setEnabled(activate);
            _survey.setEnabled(activate);
    }

    /*
    *   Common OnClick Listener for the following functions:
    *     i)Add Child
    *     ii)Edit Child
    *     iii)Add Survey
    *     iv)View Survey Report
    * */
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.viewReport:
                Intent viewChildIntent = new Intent(MainActivity.this, ViewSurveylist.class);
                startActivity(viewChildIntent);
                break;
            case R.id.addChild:
                Intent addChildIntent = new Intent(MainActivity.this, StudentRegistrationActivity.class);
                startActivity(addChildIntent);
                break;
            case R.id.editChild:
                Intent editChildIntent = new Intent(MainActivity.this, ViewChildActivity.class);
                editChildIntent.putExtra(ActivityConstants.CALLING_ACTIVITY, ActivityConstants.EDIT_CHILD_ACTIVITY);
                startActivity(editChildIntent);
                break;
            case R.id.viewSurvey:
                Intent viewSurveIntent = new Intent(MainActivity.this, ViewChildActivity.class);
                viewSurveIntent.putExtra(ActivityConstants.CALLING_ACTIVITY, ActivityConstants.VIEW_CHILD_SURVEY);
                startActivity(viewSurveIntent);
                break;
            default:
                break;
        }
    }
}
