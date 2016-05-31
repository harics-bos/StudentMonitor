package exercise.intern.boa.studentmonitor.UI;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import exercise.intern.boa.studentmonitor.DB.DBOperationsImpl;
import exercise.intern.boa.studentmonitor.DB.IDBOperations;
import exercise.intern.boa.studentmonitor.DB.StudentDBQuery;
import exercise.intern.boa.studentmonitor.Model.Student;
import exercise.intern.boa.studentmonitor.R;
import exercise.intern.boa.studentmonitor.Utility.ActivityConstants;

/*
*   A generic activity which displays all the students in a list view. It is the center point for proceeding with the following functionalities:
*       i)  Edit Child information
*       ii) Add Survey for student
* */

public class ViewChildActivity extends ListActivity {
    private IDBOperations _dbOperations;
    private Button _viewActivity = null;
    private String _firstName = null;
    private String _lastName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.content_view_child );
        _viewActivity = (Button) findViewById( R.id.view_activity_button );
        setButtonState( false );
        ListView listView = getListView();
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.requestFocusFromTouch();
                adapterView.setSelection( i );
                setButtonState( true );
                //ImageView profilePhoto = (ImageView) view.findViewById( R.id.profile_icon );
                TextView firstNameText = (TextView) view.findViewById( R.id.firstname_view );
                TextView lastNameText = (TextView) view.findViewById( R.id.secondname_view );
                _firstName = firstNameText.getText().toString();
                _lastName = lastNameText.getText().toString();

            }
        } );

    }

    @Override
    protected void onStart() {
        super.onStart();
        _dbOperations = new DBOperationsImpl( this );

        Cursor cursor = _dbOperations.getAllStudents();
        SimpleCursorAdapter cursorAdapter = new CustomSimpleAdapter( this, R.layout.activity_view_child_list_layout, cursor,
                StudentDBQuery.STUDENT_NAME_COLUMNS_WITH_IMAGE, new int[]{R.id.profile_icon, R.id.firstname_view, R.id.secondname_view},
                0 );
        setListAdapter(cursorAdapter);
        int fromActivity = getIntent().getIntExtra( ActivityConstants.CALLING_ACTIVITY, 0 );
        registerButtonListener( fromActivity );
    }


    /*
      *    Based on the calling activity, the functionality of the submit button is set.
      *    If the Calling activity requests to Edit Child Information, then StudentRegistrationActivity is invoked
      *    If the Calling activity requests to add survey Information for a child, then StudentRegistrationActivity is invoked
      *
      *    @param  fomActivityVal  an int value which determines the activity that is calling this activity
    * */
    private void registerButtonListener(int fomActivityVal) {
        switch (fomActivityVal) {
            case ActivityConstants.EDIT_CHILD_ACTIVITY:
                _viewActivity.setText(getResources().getString( R.string.edit ));
                _viewActivity.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent openRegistrationIntent = new Intent( ViewChildActivity.this, StudentRegistrationActivity.class );
                        openRegistrationIntent.putExtra( ActivityConstants.FROM_EDIT_STUDENT_ACTIVITY,
                                ActivityConstants.EDIT_CHILD_ACTIVITY );
                        //Loading Student Details
                        Student student = _dbOperations.getStudent( _firstName, _lastName );
                        openRegistrationIntent.putExtra( ActivityConstants.STUDENTDATA, student );
                        startActivity( openRegistrationIntent );
                    }
                } );
                break;

            case ActivityConstants.VIEW_CHILD_SURVEY:
                _viewActivity.setText(getResources().getString(R.string.add_survey));
                _viewActivity.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent openSurveyIntent = new Intent( ViewChildActivity.this, EnterSurveyActivity.class );
                        openSurveyIntent.putExtra( ActivityConstants.FROM_EDIT_STUDENT_ACTIVITY,
                                ActivityConstants.VIEW_CHILD_SURVEY );
                        //Loading Student Details
                        Student student = _dbOperations.getStudent( _firstName, _lastName );
                        openSurveyIntent.putExtra( ActivityConstants.STUDENTID, student.getId() );
                        startActivity( openSurveyIntent );
                    }
                } );
                break;

            default:
                break;

        }
    }
    /*
    *    Sets the enability and disability for the submit button.
    *    The button will be enabled only if the user selects any student in the list item
    *
    *    @param  state  a boolean value which determines the enblity and disablity of the submit buttons
    * */
    private void setButtonState(boolean state) {
        _viewActivity.setClickable( state );
        _viewActivity.setEnabled( state );
    }
}
