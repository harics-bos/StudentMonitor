package exercise.intern.boa.studentmonitor.UI;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import exercise.intern.boa.studentmonitor.DB.StudentDBQuery;
import exercise.intern.boa.studentmonitor.R;
import exercise.intern.boa.studentmonitor.Utility.StudentHelper;

/**
 * A custom adapter for displaying a customized list view
 *  with list items comprising of an Image View and Text View
 */
public class CustomSimpleAdapter extends SimpleCursorAdapter {

    private LayoutInflater _mLayoutInflater;
    private int layout;

    /* For performance sake a View Holder is defined which holds the view for the list items.
     * This prevents creating a new view from the xml layout file which is a relatively costly operation
     */
    private final class ViewHolder {
        TextView fName;
        TextView lName;
        ImageView profilePhoto;

        ViewHolder(View view) {
            profilePhoto=(ImageView)view.findViewById(R.id.profile_icon);
            fName=(TextView)view.findViewById(R.id.firstname_view);
            lName=(TextView)view.findViewById(R.id.secondname_view);
        }
    }

    public CustomSimpleAdapter(Context context,int layout, Cursor c,String[] from,int[] to,int flags) {
        super(context,layout,c,from,to,flags);
        this.layout=layout;
        this._mLayoutInflater=LayoutInflater.from(context);

    }

    @Override
    public View newView (Context context, Cursor cursor, ViewGroup parent) {
        View view=_mLayoutInflater.inflate(layout,parent,false);
        view.setTag( new ViewHolder(view) );
        return view;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder=(ViewHolder)view.getTag();

        int imageIndex=cursor.getColumnIndexOrThrow(StudentDBQuery.PROFILEPIC);
        int fNameIndex=cursor.getColumnIndexOrThrow(StudentDBQuery.FIRST_NAME);
        int lNameIndex=cursor.getColumnIndexOrThrow(StudentDBQuery.LAST_NAME);

        byte[] profilePhotoAsBytes=cursor.getBlob(imageIndex);
        Bitmap pic= StudentHelper.getImage(profilePhotoAsBytes);
        Bitmap rescaledPic=Bitmap.createScaledBitmap(pic, StudentHelper.IMAGE_WIDTH, StudentHelper.IMAGE_HEIGHT, false);

        holder.profilePhoto.setImageBitmap(rescaledPic);
        holder.fName.setText(cursor.getString(fNameIndex));
        holder.lName.setText(cursor.getString(lNameIndex));

    }


}
