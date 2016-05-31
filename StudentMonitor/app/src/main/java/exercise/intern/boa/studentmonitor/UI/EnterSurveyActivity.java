package exercise.intern.boa.studentmonitor.UI;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import exercise.intern.boa.studentmonitor.DB.DBOperationsImpl;
import exercise.intern.boa.studentmonitor.DB.IDBOperations;
import exercise.intern.boa.studentmonitor.Model.StudentActivity;
import exercise.intern.boa.studentmonitor.R;
import exercise.intern.boa.studentmonitor.Utility.ActivityConstants;

/*
*
*  A student survey screen which is used to register student daily activities
*
* */
public class EnterSurveyActivity extends FragmentActivity {
    private EditText _bedDate;
    private EditText _bedTime;

    private EditText _wakeupDate;
    private EditText _wakeupTime;

    private EditText _screenTime;
    private EditText _familyTime;

    private Button _submitSurvey;

    private IDBOperations _dbOperations = null;

    private int _studentId = -1;

    private DialogFragment dateFragment = null;
    private DialogFragment timeFragment = null;
    private FragmentTransaction transaction = null;

    private String _wakeup_Date_Picker="WakeupDate_Picker";
    private String _wakeup_Time_Picker="WakeupTime_Picker";

    private TextWatcher commonWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (_bedTime.getText().hashCode() == editable.hashCode()) {
                performHHmmValidation( _bedTime, "Bed Time" );
                if (_bedTime.getError() == null)
                    performBedDateTimeValidation();
                performWakeUpDateClickableValidation();
            } else if (_wakeupTime.getText().hashCode() == editable.hashCode()) {
                performHHmmValidation( _wakeupTime, "WakeUp Time" );
                if (_wakeupTime.getError() == null)
                    performWakeUpDateTimeValidation();
            } else if (_familyTime.getText().hashCode() == editable.hashCode()) {
                performHHmmValidation( _familyTime, "Family Time" );
            } else if (_screenTime.getText().hashCode() == editable.hashCode()) {
                performHHmmValidation( _screenTime, "Screen Time" );
            }

            performSubmitButtonEditable();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_enter_survey );
        _bedTime = (EditText) findViewById( R.id.bedtime_timepicker_display );
        _bedDate = (EditText) findViewById( R.id.bedtime_datepicker_display );

        _wakeupTime = (EditText) findViewById( R.id.wakeuptime_timepicker_display );
        _wakeupDate = (EditText) findViewById( R.id.wakeuptime_datepicker_display );

        _screenTime = (EditText) findViewById( R.id.screentime );
        _familyTime = (EditText) findViewById( R.id.familytime );

        _bedTime.addTextChangedListener( commonWatcher );
        _wakeupTime.addTextChangedListener( commonWatcher );
        _screenTime.addTextChangedListener( commonWatcher );
        _familyTime.addTextChangedListener( commonWatcher );

        _submitSurvey = (Button) findViewById( R.id.submit_survey );

        // Getting the student id from the Calling activity
        _studentId = getIntent().getIntExtra( "StudentId", 0 );
        _dbOperations = new DBOperationsImpl( EnterSurveyActivity.this );
        _submitSurvey.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bedTime = _bedDate.getText().toString() + " " + _bedTime.getText().toString();
                String wakeUpTime = _wakeupDate.getText().toString() + " " + _wakeupTime.getText().toString();
                String screenTime = _screenTime.getText().toString();
                String familyTime = _familyTime.getText().toString();
                StudentActivity surveyRecord = new StudentActivity( bedTime, wakeUpTime, screenTime, familyTime );
                //surveyRecord.setChildId(_studentId);
                boolean result = _dbOperations.addSurvey( surveyRecord, _studentId );
                if (result)
                    Toast.makeText( EnterSurveyActivity.this, "Activity successfully entered",
                            Toast.LENGTH_LONG ).show();
                else
                    Toast.makeText( EnterSurveyActivity.this, "Issue with insertion. Please try again later",
                            Toast.LENGTH_LONG ).show();
                finish();

            }
        } );
    }

    /*
    *   performs the time validation based on HH:mm format. Checks the following:
    *       i)Checks if the field is empty. reports error if its empty
    *       ii)Checks if the entered time is not 5 characters(HH:mm) of length. If yes, reports an error
    *       iii)Check for the proper 24 hrs format. Makes sure HH doesnt exceed 24 and mm doesn't exceed 59
    * */
    private void performHHmmValidation(EditText timeField, String fieldName) {
        String hrsmm = timeField.getText().toString();
        int allowedLen = "HH:mm".length();

        //Check if its null or empty
        String emptyCheckErrorText = (hrsmm.trim().equals( "" )) ? fieldName + " Field cannot be empty" : null;
        timeField.setError( emptyCheckErrorText );
        if (emptyCheckErrorText != null)
            return;

        // Check if its less than 5 characters
        String lengthCheckErrorText = (hrsmm.length() < allowedLen)
                ? fieldName + " should be of the format HH:mm(Totally 5 characters in size)" : null;
        timeField.setError( lengthCheckErrorText );

        if (lengthCheckErrorText != null)
            return;
        //Check for the proper 24 hrs format. Makes sure HH doesnt exceed 24 and mm doesn't exceed 59
        SimpleDateFormat dateFormat = new SimpleDateFormat( "HH:mm" ); //HH = 24h format
        dateFormat.setLenient( false );
        try {
            dateFormat.parse( hrsmm );
        } catch (ParseException e) {
            timeField.setError( fieldName + " HH should be lesser than 24 and m should be lesser than 60" );
        }

        performSubmitButtonEditable();
    }

    /*
    *    Callback method for the Date Picket Fragment which sets the date for the Bed Date field
    *
    *   @param year     int value that denotes the BedDate year of the student(format yyyy)
    *   @param month    int value that denotes the BedDate month of the student (format mm)
    *   @param day      int value that denotes the BedDate day of the student (format dd)
    * */
    void setBedDateVal(int year, int month, int day) {

        _bedDate.setText( convertDateToString( year, month, day ) );
        if (performBedDateTimeValidation()) {
            performWakeUpDateClickableValidation();
            performSubmitButtonEditable();
        }


    }

    /*
    *   Callback method for the TimePicker fragment which sets the Bed Time field
    *
    *   @param hrs denotes the hour of the day when the student went to sleep
    *   @param mins denotes the minutes of the day when the student went to sleep
    *
    * */
    void setBedTimeVal(int hrs, int mins) {
        String bedTimeTextVal = convertTimeToString( hrs, mins );
        _bedTime.setText( bedTimeTextVal );
        if (performBedDateTimeValidation()) {
            performWakeUpDateClickableValidation();
            performSubmitButtonEditable();
        }
    }

    /*
    *    Callback method for the Date Picket Fragment which sets the date for the wake up Date field
    *
    *   @param year     int value that denotes the wakeupDate year of the student(format yyyy)
    *   @param month    int value that denotes the wakeupDate month of the student (format mm)
    *   @param day      int value that denotes the wakeupDate day of the student (format dd)
    * */
    void setWakeUpDateVal(int year, int month, int day) {

        _wakeupDate.setText( convertDateToString( year, month, day ) );

        performWakeUpDateTimeValidation();
        performSubmitButtonEditable();
    }

    /*
    *   Calback method for the TimePicker fragment which sets the wakeup Time field
    *
    *   @param hrs denotes the hour of the day when the student woke up
    *   @param mins denotes the minutes of the day when the student woke up
    *
    * */
    void setWakeUpTimeVal(int hrs, int mins) {

        _wakeupTime.setText( convertTimeToString( hrs, mins ) );

        performWakeUpDateTimeValidation();
        performSubmitButtonEditable();
    }

    /*
    *
    *    Helper method which converts the time information to a String information for setting time to edit text field
    * */

    private String convertTimeToString(int hrs, int mins) {
        String hours, minutes;
        hours = (hrs < 10) ? "0" + hrs : Integer.toString( hrs );
        minutes = (mins < 10) ? "0" + mins : Integer.toString( mins );
        return hours + ":" + minutes;
    }

    /*
    *
    *    Helper method which converts the date information to a String information for setting date to edit text field
    * */
    private String convertDateToString(int year, int month, int day) {
        String mn, dy;
        mn = (month < 10) ? "0" + month : Integer.toString( month );
        dy = (day < 10) ? "0" + day : Integer.toString( day );
        return year + "/" + mn + "/" + dy;
    }

    /*
    *   Callback method for the Date fields of the screen.
    *   Sets the minimum and maximum allowed date
    * */
    @SuppressLint( "CommitTransaction" )
    public void showDatePickerDialog(View view) {
        Bundle fragmentBundle = new Bundle();
        if (dateFragment == null)
            dateFragment = new DatePickerFragment();
        transaction = getFragmentManager().beginTransaction();

        switch (view.getId()) {
            case R.id.bedtime_datepicker_display:
                boolean isSurveyEmpty = _dbOperations.isSurveyEmptyForStudent( _studentId );
                if (isSurveyEmpty) {
                    fragmentBundle.putLong( ActivityConstants.MIN_DATE, System.currentTimeMillis() );
                    fragmentBundle.putLong( ActivityConstants.MAX_DATE, System.currentTimeMillis() );
                } else {
                    //Should be in onStart()
                    String latestWakeUpDate = _dbOperations.getLatestWakeUpDay( _studentId );
                    try {
                        Date preWakeUpDate = new SimpleDateFormat( "yyyy/MM/dd HH:mm" ).parse( latestWakeUpDate );
                        fragmentBundle.putLong( ActivityConstants.MIN_DATE, preWakeUpDate.getTime() );
                        fragmentBundle.putLong( ActivityConstants.MAX_DATE, System.currentTimeMillis() );
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                fragmentBundle.putInt( ActivityConstants.SURVEYACTIVITY, ActivityConstants.BD_DP_VAL );
                dateFragment.setArguments( fragmentBundle );
                dateFragment.show( transaction, "BedTime_Date_Picker" );
                break;

            case R.id.wakeuptime_datepicker_display:
                SimpleDateFormat sdf = new SimpleDateFormat( "yyyy/MM/dd HH:mm" );
                String bedDateTime = _bedDate.getText().toString() + " " + _bedTime.getText().toString().trim();
                long nextDay = 0;
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime( sdf.parse( bedDateTime ) );
                    c.add( Calendar.DATE, 1 );  // number of days to add
                    nextDay = (c.getTimeInMillis() <= System.currentTimeMillis()) ? c.getTimeInMillis() : System.currentTimeMillis();
                    fragmentBundle.putLong( ActivityConstants.MIN_DATE, sdf.parse( bedDateTime ).getTime() );
                    fragmentBundle.putLong( ActivityConstants.MAX_DATE, nextDay );
                } catch (ParseException e) {
                    e.printStackTrace();
                    break;
                }
                fragmentBundle.putInt( ActivityConstants.SURVEYACTIVITY, ActivityConstants.WK_DP_VAL );

                dateFragment.setArguments( fragmentBundle );

                dateFragment.show( transaction, "WakeUpTime_Date_Picker" );
                break;

            default:
                break;

        }
    }

    /*
    *   Performs the validation for the 'WakeUp date' edit text field.
    *   The Wake up date edit text field is enabled iff the Bed Date field is non empty as well as free from error
    * */
    private void performWakeUpDateClickableValidation() {
        String bedDateText = _bedDate.getText().toString();
        String bedTimeText = _bedTime.getText().toString();
        if (((_bedDate.getError() == null) && (_bedTime.getError() == null))
                && (!(bedDateText.trim().equals( "" )) && (!(bedTimeText.trim().equals( "" ))))){
            _wakeupDate.setClickable( true );
            _wakeupTime.setClickable( true );
        } else {
            _wakeupDate.setClickable( false );
            _wakeupTime.setClickable( false );
        }
    }

    /*
    *   Performs the validation for the 'WakeUp time' edit text field.
    *   Checks if the Wake up date field is non empty and
    *       if the entered date and time is greater than the Bed Date Date and Time
    * */
    private void performWakeUpDateTimeValidation() {
        boolean isValid = true;
        String wakeUpDateText = _wakeupDate.getText().toString();
        String wakeUpTimeText = _wakeupTime.getText().toString();
        String wakeUpDateTimeText;

        if (wakeUpDateText.trim().equals( "" )) {
            _wakeupTime.setFocusable( true );
            _wakeupDate.requestFocus();
            _wakeupDate.setError( getResources().getString( R.string.es_wakeup_date ) );
            isValid = false;
        }

        // Checks if the time field is null or empty
        if (wakeUpTimeText.trim().equals( "" )) {
            _wakeupTime.setFocusable( true );
            _wakeupTime.requestFocus();
            _wakeupTime.setError( getResources().getString( R.string.es_wakeup_time ));
            isValid = false;
        }

        // Check if the wake up date and time is greater than the bed date and time

        if (isValid) {
            String bedDateText = _bedDate.getText().toString();
            String bedTimeText = _bedTime.getText().toString();
            String bedDateTimeText = bedDateText + " " + bedTimeText;
            wakeUpDateTimeText = wakeUpDateText + " " + wakeUpTimeText;
            try {
                Date bedTimeDateVal = new SimpleDateFormat( "yyyy/MM/dd HH:mm" )
                        .parse( bedDateTimeText );
                Date wakeUpDateVal = new SimpleDateFormat( "yyyy/MM/dd HH:mm" )
                        .parse( wakeUpDateTimeText );
                if (wakeUpDateVal.before( bedTimeDateVal )) {
                    isValid = false;
                    _wakeupTime.setFocusable( true );
                    _wakeupTime.requestFocus();

                    _wakeupTime.setError( getResources().getString( R.string.es_wakeup_compare ) );
                }

            } catch (ParseException e) {
                _wakeupTime.setError( getResources().getString( R.string.es_wakeup_format ) );
                e.printStackTrace();
            }

        }

        if (isValid)
            _wakeupTime.setError( null );

    }

    /*
    *   Performs the validation for the 'Bed date time' edit text field.
    *   Checks if the Bed dateand time field is non empty and
    *       if the entered date and time is lesser than the wakeup Date and Time
    * */
    private boolean performBedDateTimeValidation() {
        boolean isValid = true;
        String bedDateText = _bedDate.getText().toString();
        String bedTimeText = _bedTime.getText().toString();
        String bedDateTimeText = null;
        Date bedTimeDate = null;

        if ((bedDateText.trim().equals( "" ))) {
            _bedDate.setError( getResources().getString( R.string.es_BedDate_not_empty ) );
            isValid = false;
        }

        if ((bedTimeText.trim().equals( "" ))) {
            _bedTime.setError( getResources().getString( R.string.es_BedTime_not_empty ) );
            isValid = false;
        }
        // Check if the entered date is before the previous date
        if (isValid) {
            bedDateTimeText = bedDateText + " " + bedTimeText;
            String latestWakeUpDate = _dbOperations.getLatestWakeUpDay( _studentId );
            if (!(latestWakeUpDate == null) && !(latestWakeUpDate.trim().equals( "" ))) {
                try {
                    bedTimeDate = new SimpleDateFormat( "yyyy/MM/dd HH:mm" )
                            .parse( bedDateTimeText );
                    Date lastWakeUpDate = new SimpleDateFormat( "yyyy/MM/dd HH:mm" )
                            .parse( latestWakeUpDate );
                    if (bedTimeDate.before( lastWakeUpDate )) {
                        _bedTime.setError( getResources().getString( R.string.es_BedTime_compare_less ) + latestWakeUpDate );
                        isValid = false;
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        //logic to check if the date is smaller than the wakeupdate and time
        if ((isValid) && ((_wakeupDate != null)&& (!(_wakeupDate.getText().toString().trim().equals( "" ))))
                && ((_wakeupTime != null) && (!(_wakeupTime.getText().toString().equals( "" ))))) {
            try {
                //bedDateTimeText=bedDateText+" "+bedTimeText;
                String wakeUpDateTimeText = _wakeupDate + " " + _wakeupTime;
                //Date bedTimeDate = new SimpleDateFormat("yyyy/MM/dd HH:mm")
                //      .parse(bedDateTimeText);
                Date wakeUpDateTime = new SimpleDateFormat( "yyyy/MM/dd HH:mm" )
                        .parse( wakeUpDateTimeText );

                if (bedTimeDate.after( wakeUpDateTime )) {
                    _bedTime.setError( getResources().getString( R.string.es_BedTime_compare_more ) + wakeUpDateTime );
                    isValid = false;
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (isValid)
            _bedTime.setError( null );
        return isValid;
    }

    /*
    *
    *   Performs the logic to enable the submit button.
    *   Enables only when the mandatory fields are filled and error free
    * */
    private void performSubmitButtonEditable() {
        boolean isValid = false;
        String bedDateText = _bedDate.getText().toString();
        String bedTimeText = _bedTime.getText().toString();
        String wakeupDateText = _wakeupDate.getText().toString();
        String wakeupTimeText = _wakeupTime.getText().toString();
        String screenTime = _screenTime.getText().toString();
        String familyTime = _familyTime.getText().toString();

        if ((!(bedDateText.trim().equals( "" )))
                && (!(bedTimeText.trim().equals( "" )))
                && (!(wakeupDateText.trim().equals( "" )))
                && (!(wakeupTimeText.trim().equals( "" )))
                && (!(screenTime.trim().equals( "" )))
                && (!(familyTime.trim().equals( "" )))
                ) {
            if ((_bedDate.getError() == null) && (_bedTime.getError() == null)
                    && (_wakeupDate.getError() == null) && (_wakeupTime.getError() == null)
                    && (_screenTime.getError() == null) && (_familyTime.getError() == null)) {
                isValid = true;
            }
        }

        if (isValid) {
            _submitSurvey.setEnabled( true );
            _submitSurvey.setClickable( true );
        } else {
            _submitSurvey.setEnabled( false );
            _submitSurvey.setClickable( false );
        }

    }

    /*
    *
    *   Shows the Time Picker dialog which allows the user to enter time information.
    * */
    @SuppressLint("CommitTransaction")
    public void showTimePickerDialog(View view) {
        Bundle fragmentBundle = new Bundle();
        if (timeFragment == null)
            timeFragment = new TimePickerFragment();

        transaction = getFragmentManager().beginTransaction();


        switch (view.getId()) {
            case R.id.bedtime_timepicker_display:
                fragmentBundle.putInt(ActivityConstants.SURVEYACTIVITY, ActivityConstants.BD_TP_VAL);
                timeFragment.setArguments( fragmentBundle );
                timeFragment.show( transaction, _wakeup_Date_Picker );
                break;

            case R.id.wakeuptime_timepicker_display:
                fragmentBundle.putInt(ActivityConstants.SURVEYACTIVITY, ActivityConstants.WK_TP_VAL );
                timeFragment.setArguments( fragmentBundle );
                timeFragment.show( transaction, _wakeup_Time_Picker );
                break;

            default:
                break;

        }


    }

}
