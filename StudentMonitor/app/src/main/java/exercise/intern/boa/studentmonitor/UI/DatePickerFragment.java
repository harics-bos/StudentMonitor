package exercise.intern.boa.studentmonitor.UI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;


import java.util.Calendar;

import exercise.intern.boa.studentmonitor.Utility.ActivityConstants;

/**
 * A custom Date dialog that allows the user to select and set the date to their activity
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    /**
     * Constructs a custom Dialog Calendar Container
     * withs customized date values for the user to select
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle dateBundle = getArguments();
        // Use the current date as the default date in the picker
        final Calendar cal = Calendar.getInstance();
        int year = cal.get( Calendar.YEAR );
        int month = cal.get( Calendar.MONTH );
        int day = cal.get( Calendar.DAY_OF_MONTH );

        //Get the Min Date and Max Date values set by the Calling Activities
        long minDate = dateBundle.getLong( ActivityConstants.MIN_DATE );
        long maxDate = dateBundle.getLong( ActivityConstants.MAX_DATE );
        DatePickerDialog dateDialog = new DatePickerDialog( getActivity(), this, year, month, day );
        DatePicker datePicker = dateDialog.getDatePicker();
        datePicker.setMinDate( minDate );
        datePicker.setMaxDate( maxDate );
        return dateDialog;
    }

    /*
    *  Sets the year, month and day of the selected date to the Calling activity's Callback Method
    * */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Bundle dateBundle = getArguments();
        if (getActivity() instanceof EnterSurveyActivity) {
            EnterSurveyActivity surveyActivity = (EnterSurveyActivity) getActivity();
            int val = dateBundle.getInt( ActivityConstants.SURVEYACTIVITY );
            if (val == ActivityConstants.BD_DP_VAL)
                surveyActivity.setBedDateVal( year, month + 1, day );
            else if (val == ActivityConstants.WK_DP_VAL)
                surveyActivity.setWakeUpDateVal( year, month + 1, day );
        } else if (getActivity() instanceof StudentRegistrationActivity) {
            StudentRegistrationActivity regActivity = (StudentRegistrationActivity) getActivity();
            regActivity.setBedDateVal( year, month + 1, day );
        }

        this.getDialog().dismiss();
    }
}
