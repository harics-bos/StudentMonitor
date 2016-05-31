package exercise.intern.boa.studentmonitor.UI;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

import exercise.intern.boa.studentmonitor.Utility.ActivityConstants;

/**
 * A custom Time dialog that allows the user to select and set the time to their activity
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    /**
     * Constructs a custom Dialog Time Container
     * for the user to set time. Follows the 24 hrs format
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    /*
    *  Sets the time to the callback method of the calling activity
    * */
    @Override
    public void onTimeSet(TimePicker timePicker, int hrs, int mins) {

        if(getActivity() instanceof EnterSurveyActivity){
            EnterSurveyActivity surveyActivity=(EnterSurveyActivity)getActivity();
            Bundle bundle=getArguments();
            int val=bundle.getInt( ActivityConstants.SURVEYACTIVITY);
            if(val==ActivityConstants.BD_TP_VAL)
                surveyActivity.setBedTimeVal(hrs,mins);
            else if(val==ActivityConstants.WK_TP_VAL)
                surveyActivity.setWakeUpTimeVal(hrs,mins);
        }

        this.getDialog().dismiss();


    }
}
