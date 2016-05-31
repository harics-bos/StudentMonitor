package exercise.intern.boa.studentmonitor.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import exercise.intern.boa.studentmonitor.Model.Student;
import exercise.intern.boa.studentmonitor.Model.StudentActivity;
import exercise.intern.boa.studentmonitor.R;
import exercise.intern.boa.studentmonitor.Utility.StudentHelper;

/**
 * A custom adapter for displaying a customized expandable list
 *  with header projecting student information and detail projecting the survey activities
 */
public class SurveyExpandableAdapter extends BaseExpandableListAdapter {

    private ArrayList<Student> _children = null;
    private LayoutInflater _inflater = null;

    /* For performance sake a View Holder is defined which holds the view for the header items.
     * This prevents creating a new view from the xml layout file which is a relatively costly operation
     */
    private final class GroupViewHolder {

        TextView firstNameView;
        TextView lastNameView;
        ImageView profile;
    }

    /* For performance sake a View Holder is defined which holds the view for the detail items.
     * This prevents creating a new view from the xml layout file which is a relatively costly operation
     */
    private final class ChildViewHolder {

        TextView bedTimeView;
        TextView wakeUpTimeView;
        TextView screenTimeView;
        TextView familyTimeView;
    }

    public SurveyExpandableAdapter(Context context, ArrayList<Student> children) {
        this._children = children;
        _inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    }


    @Override
    public int getGroupCount() {
        return _children.size();
    }

    @Override
    public int getChildrenCount(int groupPos) {
        return _children.get( groupPos ).getChildActivties().size();
    }

    @Override
    public Object getGroup(int groupPos) {
        return _children.get( groupPos );
    }

    @Override
    public Object getChild(int groupPos, int childPos) {
        return _children.get( groupPos ).getChildActivties().get( childPos );
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {

        GroupViewHolder groupViewHolder;
        //ONLY INFLATE XML TEAM ROW MODEL IF ITS NOT PRESENT,OTHERWISE REUSE IT
        if (view == null) {
            view = _inflater.inflate( R.layout.student_header, null );

            groupViewHolder = new GroupViewHolder();
            groupViewHolder.firstNameView = (TextView) view.findViewById( R.id.firstname_group );
            groupViewHolder.lastNameView = (TextView) view.findViewById( R.id.secondname_group );
            groupViewHolder.profile = (ImageView) view.findViewById( R.id.profile_group );
            view.setTag( groupViewHolder );
        } else {
            groupViewHolder = (GroupViewHolder) view.getTag();
        }

        Student child = (Student) getGroup( groupPosition );
        byte[] profilePicAsBytes = child.getProfilePhoto();
        Bitmap originalBitMap = StudentHelper.getImage( profilePicAsBytes );
        Bitmap scaledProfilePic = Bitmap.createScaledBitmap( originalBitMap, StudentHelper.IMAGE_WIDTH, StudentHelper.IMAGE_HEIGHT, false );

        groupViewHolder.firstNameView.setText( child.getFirstName() );
        groupViewHolder.lastNameView.setText( child.getLastName() );
        groupViewHolder.profile.setImageBitmap( scaledProfilePic );

        return view;
    }

    @Override
    public View getChildView(int groupPos, int childPos, boolean isLastChild, View view, ViewGroup parent) {

        ChildViewHolder childViewHolder;
        if (view == null) {
            view = _inflater.inflate( R.layout.survey_detail, null );
              childViewHolder = new ChildViewHolder();
              childViewHolder.bedTimeView = (TextView) view.findViewById( R.id.bedtime_display );
              childViewHolder.wakeUpTimeView = (TextView) view.findViewById( R.id.wakeuptime_display );
              childViewHolder.screenTimeView = (TextView) view.findViewById( R.id.screentime_display );
              childViewHolder.familyTimeView = (TextView) view.findViewById( R.id.familytime_display );
            view.setTag( childViewHolder );
        } else {
             childViewHolder = (ChildViewHolder) view.getTag();
        }

        StudentActivity survey = (StudentActivity) getChild( groupPos, childPos );

         childViewHolder.bedTimeView.setText( survey.getBedTime() );
         childViewHolder.wakeUpTimeView.setText( survey.getWokeUpTime() );
         childViewHolder.screenTimeView.setText( survey.getScreenTime() );
         childViewHolder.familyTimeView.setText( survey.getFamilyTime() );

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
