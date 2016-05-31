package exercise.intern.boa.studentmonitor.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import exercise.intern.boa.studentmonitor.DB.DBOperationsImpl;
import exercise.intern.boa.studentmonitor.DB.IDBOperations;
import exercise.intern.boa.studentmonitor.Model.Student;
import exercise.intern.boa.studentmonitor.R;

/**
 *   Presents the Expandable List UI for displaying the Survey information of all the students
 *
 */
public class ViewSurveylist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_surveylist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ArrayList<Student> childList;
        IDBOperations _dbOperations = new DBOperationsImpl(ViewSurveylist.this);

        List<Student> listOfStudents=_dbOperations.getAllStudentsWithSurvey();
        if(listOfStudents instanceof ArrayList){
            childList=(ArrayList<Student>)listOfStudents;
        }else{
            childList=new ArrayList<Student>(listOfStudents);
        }

        ExpandableListView studentSurveyList=(ExpandableListView) findViewById(R.id.expandableListView1);
        SurveyExpandableAdapter adapter=new SurveyExpandableAdapter(this,childList);
        studentSurveyList.setAdapter(adapter);
    }

}
