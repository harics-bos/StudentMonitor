package exercise.intern.boa.studentmonitor.UI;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import exercise.intern.boa.studentmonitor.DB.DBOperationsImpl;
import exercise.intern.boa.studentmonitor.DB.IDBOperations;
import exercise.intern.boa.studentmonitor.DB.StudentDBQuery;
import exercise.intern.boa.studentmonitor.Model.Student;
import exercise.intern.boa.studentmonitor.R;
import exercise.intern.boa.studentmonitor.Utility.ActivityConstants;
import exercise.intern.boa.studentmonitor.Utility.StudentHelper;


/*
*
*  A student Registration screen which can be either used for
*           i)creating a new Student
*                   or
*          ii)Editing an existing student information
* */
public class StudentRegistrationActivity extends FragmentActivity {

    private static final int PICK_FROM_FILE = 2;
    // ContentValues will contain the list of columns and their values while updating the database
    private ContentValues colnValsPair;
    private IDBOperations _dbOperations;
    private EditText _firstName;
    private EditText _lastName;
    private EditText _dob;
    private Button _done;
    private RadioGroup _genderGroup;
    private RadioButton _genderButton;
    private Student _studentInfo;
    private int _fromEditActivity;
    private Uri imageCaptureUri;
    private ImageView mImageView;
    private Bitmap bitmap = null;


    // Takes Care of the validation for the first name and last name field
    private TextWatcher editTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // do nothing
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // do nothing
        }

        @Override
        public void afterTextChanged(Editable editable) {
            EditText name = (_firstName.getText().hashCode() == editable.hashCode()) ? _firstName : _lastName;
            String nameText = name.getText().toString();
            boolean isError = ((nameText.trim().equals( "" )));
            String errorText = (isError) ? name.getHint() + " " + getResources().getString( R.string.student_registration_field_not_empty ) : null;
            name.setError( errorText );
            checkButtonEnablity();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_student_registration );


        //Mapping UI elements from the layout file
        Button btn_choose_image;
        _firstName = (EditText) findViewById( R.id.firstname );
        _lastName = (EditText) findViewById( R.id.lastname );
        _genderGroup = (RadioGroup) findViewById( R.id.radioSex );
        _dob = (EditText) findViewById( R.id.dob_datepicker );
        _done = (Button) findViewById( R.id.done );
        mImageView = (ImageView) findViewById( R.id.icon );

        // Adding TextWatcher to firstname and lastname fields
        _firstName.addTextChangedListener( editTextWatcher );
        _lastName.addTextChangedListener( editTextWatcher );


        final String[] items = new String[]{getResources().getString( R.string.student_registration_image_SDCARD )};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.select_dialog_item, items );
        AlertDialog.Builder builder = new AlertDialog.Builder( StudentRegistrationActivity.this );
        builder.setTitle( getResources().getString( R.string.student_registration_selectImage ) );
        builder.setAdapter( adapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent();
                intent.setType( "image/*" );
                intent.setAction( Intent.ACTION_GET_CONTENT );
                startActivityForResult( Intent.createChooser( intent, "Complete action using" ), PICK_FROM_FILE );

                checkButtonEnablity();
            }

        } );

        final AlertDialog dialog = builder.create();

        btn_choose_image = (Button) findViewById( R.id.upload_button );
        btn_choose_image.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.show();
            }
        } );

        _genderGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                checkButtonEnablity();
            }
        } );

        // Seting the onclicklistener for the Submit(Done) button
        _done.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (_fromEditActivity == ActivityConstants.EDIT_CHILD_ACTIVITY) {
                    // The activity is invoked for update operation. Perform Update operation.
                    updateStudentData();
                } else {
                    // Performs the insert operation
                    Toast notifyResult = fetchAndPersistStudentData() ?
                            Toast.makeText( StudentRegistrationActivity.this, getString( R.string.student_registration_insert_success ),
                                    Toast.LENGTH_LONG ) : Toast.makeText( StudentRegistrationActivity.this, getString( R.string.student_registration_insert_failure ),
                            Toast.LENGTH_LONG );
                    notifyResult.show();
                }
                finish();
            }
        } );


    }

    @Override
    protected void onStart() {
        super.onStart();
        //Getting the Database Connection
        _dbOperations = new DBOperationsImpl( StudentRegistrationActivity.this );

        _fromEditActivity = getIntent().getIntExtra( ActivityConstants.FROM_EDIT_STUDENT_ACTIVITY, 0 );
        // Preparing the screen for Update Operation if this Screen is invoked to perform Edit Child functionality
        if (_fromEditActivity == ActivityConstants.EDIT_CHILD_ACTIVITY) {
            colnValsPair = new ContentValues();
            _studentInfo = getIntent().getParcelableExtra( ActivityConstants.STUDENTDATA );
            prePopulateStudentFields( _studentInfo.getFirstName(),
                    _studentInfo.getLastName(), _studentInfo.getGender(), _studentInfo.getDateOfBirth(), _studentInfo.getProfilePhoto() );
        }
        //Initially the submit button should be disabled
        setButtonState( false );
    }

    /*
    *
    *    Updates the fields that are edited by the user. Compares every field with the old values of those fields.
    *    If any value mismatches, sends it to update to the database
    * */

    private void updateStudentData() {
        String editedFirstName = _firstName.getText().toString();
        String editedLastName = _lastName.getText().toString();
        String editedStudentGender = ((RadioButton) findViewById( _genderGroup.getCheckedRadioButtonId() )).getText().toString();
        String editedDOB = _dob.getText().toString();
        int selectedGender = _genderGroup.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        _genderButton = (RadioButton) findViewById( selectedGender );
        String studentGender = _genderButton.getText().toString();

        if (bitmap != null) {
            byte[] editedBitmap = StudentHelper.getBytes( bitmap );
            if (!(Arrays.equals( editedBitmap, _studentInfo.getProfilePhoto() ))) {
                colnValsPair.put( StudentDBQuery.PROFILEPIC, editedBitmap );
            }
        }
        if (!(_studentInfo.getFirstName().equals( editedFirstName ))) {
            colnValsPair.put( StudentDBQuery.FIRST_NAME, editedFirstName );
        }
        if (!(_studentInfo.getLastName().equals( editedLastName ))) {
            colnValsPair.put( StudentDBQuery.LAST_NAME, editedLastName );
        }
        if (!(_studentInfo.getGender().equals( editedStudentGender ))) {
            colnValsPair.put( StudentDBQuery.GENDER, editedStudentGender );
        }
        if (!(_studentInfo.getDateOfBirth().equals( editedDOB ))) {
            colnValsPair.put( StudentDBQuery.DOB, editedDOB );
        }
        //Allow the update to take place only if there exists any entry
        if (colnValsPair.size() > 0) {
            //_dbOperations = new DBOperationsImpl(StudentRegistrationActivity.this);
            int rows = _dbOperations.updateStudent( colnValsPair, _studentInfo.getFirstName(), _studentInfo.getLastName() );
            //rows = rows + 0;
            Toast.makeText( StudentRegistrationActivity.this, rows + " " + getResources().getString( R.string.student_registration_update_success ),
                    Toast.LENGTH_LONG ).show();
        }
    }

    /*
    *   Performs the insert operation. persists all the studen information to the database
    * */

    private boolean fetchAndPersistStudentData() {

        String studentFirstName = _firstName.getText().toString();
        String studentLastName = _lastName.getText().toString();
        String studentDateOfBirth = _dob.getText().toString();

        // get selected radio button from radioGroup
        int selectedGender = _genderGroup.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        _genderButton = (RadioButton) findViewById( selectedGender );

        String studentGender = _genderButton.getText().toString();
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource( getResources(), R.drawable.blank_profile_pic );
            bitmap = Bitmap.createScaledBitmap( bitmap, StudentHelper.IMAGE_WIDTH, StudentHelper.IMAGE_HEIGHT, false );
        }
        byte[] profilePhoto = StudentHelper.getBytes( bitmap );
        Student student = new Student( studentFirstName, studentLastName, studentGender, profilePhoto, studentDateOfBirth );

        // Lazy loading for interacting with the database
        //_dbOperations = new DBOperationsImpl(StudentRegistrationActivity.this);
        return _dbOperations.addStudent( student );
    }


    /*
    *  Prepopulates all the student information in the register screen for a particular student.
    *
    *   @param preFirstName        The firstname of the selected student from the List Activity
    *   @param preLastName         The  Lastname of the selected student from the List Activity
    *   @param preGender           The  gender of the selected student from the List Activity
    *   @param preDOB              The DOB of the selected student from the List Activity
    *   @param preProfilePic       The profile photo of the selected student from the List Activity in bytes
    * */
    private void prePopulateStudentFields(String preFirstName, String preLastName, String preGender, String preDOB, byte[] preProfilePic) {
        _firstName.setText( preFirstName );
        _lastName.setText( preLastName );
        _dob.setText( preDOB );
        RadioButton male = (RadioButton) findViewById( R.id.radioMale );
        int radioId = male.getText().toString().equalsIgnoreCase( preGender ) ? R.id.radioMale : R.id.radioFemale;
        _genderGroup.check( radioId );
        Bitmap prevPhoto = preProfilePic != null ? Bitmap.createScaledBitmap( StudentHelper.getImage( preProfilePic ), StudentHelper.IMAGE_WIDTH, StudentHelper.IMAGE_HEIGHT, false ) : null;
        mImageView.setImageDrawable( null );
        mImageView.setImageBitmap( null );
        mImageView.setImageBitmap( prevPhoto );
    }

    /*
    *   Turns on and off the enablity and editability of the submit button
    *   @param visibility Boolean value which determines the on and off state
    * */

    private void setButtonState(boolean visibility) {
        _done.setEnabled( visibility );
        _done.setClickable( visibility );
    }

    /*
    *   Checks if the submit button can be enabled or not.
    *   Checks if all the required fields are non-empty
    *   Checks if all the fields are error free
    *   If both conditions are met, then enables the submit button.
    * */

    private void checkButtonEnablity() {

        EditText firstName = (EditText) findViewById( R.id.firstname );
        String firstNameText = firstName.getText().toString();
        String firstNameError = (String) firstName.getError();

        EditText lastName = (EditText) findViewById( R.id.lastname );
        String lastNameText = lastName.getText().toString();
        String lastNameError = (String) lastName.getError();

        // EditText dob = (EditText) findViewById(R.id.dobText);
        String dobText = _dob.getText().toString();
        String dobError = (String) _dob.getError();

        // Allows the Button to be visible and editable only when all the mandatory texts are non null and error free
        if ((firstNameText.trim().equals( "" ))
                || (lastNameText.trim().equals( "" ))
                || (dobText.trim().equals( "" ))
                )
            setButtonState( false );
        else if ((firstNameError == null || firstNameError.trim().equals( "" ))
                && (lastNameError == null || lastNameError.trim().equals( "" ))
                && (dobError == null || dobError.trim().equals( "" ))
                )
            setButtonState( true );
    }

    /*
    *   Sets the minimum date and max date for the user to enter the DOB of their child.
    *
    * */
    public void showDatePickerDialog(View view) {
        Bundle dateBundle = new Bundle();
        Calendar cal = Calendar.getInstance();
        cal.add( Calendar.YEAR, -StudentHelper.MAX_YRS );
        Date minYrsBack = cal.getTime();
        cal.add( Calendar.YEAR, (StudentHelper.MAX_YRS - StudentHelper.MIN_YRS) );
        Date maxYrsBack = cal.getTime();
        dateBundle.putLong( ActivityConstants.MIN_DATE, minYrsBack.getTime() );
        dateBundle.putLong( ActivityConstants.MAX_DATE, maxYrsBack.getTime() );
        dateBundle.putInt( ActivityConstants.SURVEYACTIVITY, ActivityConstants.BD_DP_VAL );
        DialogFragment dateFragment = new DatePickerFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        dateFragment.setArguments( dateBundle );
        dateFragment.show( transaction, "RegDOB_Date_Picker" );

    }

    /*
    *   Callback method for the DatePickerFragment class to set the selected DOB for the child.
    *
    *   @param year     int value that denotes the birth year of the student(format yyyy)
    *   @param month    int value that denotes the birth month of the student (format mm)
    *   @param day      int value that denotes the birth day of the student (format dd)
    *
    * */

    void setBedDateVal(int year, int month, int day) {
        String dob_month = (month < 10) ? "0" + month : Integer.toString( month );
        String dob_day = (day < 10) ? "0" + day : Integer.toString( day );
        String studentDOB = year + StudentHelper.DATE_SEPARATOR + dob_month + StudentHelper.DATE_SEPARATOR + dob_day;
        _dob.setText( studentDOB );
        checkButtonEnablity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (resultCode != RESULT_OK)
            return;
        bitmap = null;
        String path = "";
        if (requestCode == PICK_FROM_FILE) {
            imageCaptureUri = data.getData();
            path = getrealPathFromURI( imageCaptureUri );

            if (path == null)
                path = imageCaptureUri.getPath();
            if (path != null)
                bitmap = BitmapFactory.decodeFile( path );
        } else {
            path = imageCaptureUri.getPath();
            bitmap = BitmapFactory.decodeFile( path );
        }
        Bitmap resizedBitmap = Bitmap.createScaledBitmap( bitmap, StudentHelper.IMAGE_WIDTH, StudentHelper.IMAGE_HEIGHT, false );
        //mImageView=(ImageView)findViewById(R.id.icon);

        mImageView.setImageDrawable( null );// Added to refresh image for loading new images
        mImageView.setImageBitmap( null );
        mImageView.setImageBitmap( resizedBitmap );
    }

    private String getrealPathFromURI(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query( uri, proj, null, null, null );
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow( MediaStore.Images.Media.DATA );
        cursor.moveToFirst();
        String column = cursor.getString( column_index );
        cursor.close();
        return column;
    }
}



