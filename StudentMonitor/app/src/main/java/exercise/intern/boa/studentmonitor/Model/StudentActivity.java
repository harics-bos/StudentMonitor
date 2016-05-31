package exercise.intern.boa.studentmonitor.Model;

import java.util.Date;

/**
 * A model class that corresponds to the Survey Table
 */
public class StudentActivity {

    private int _id;
    private int _childId;
    private String _bedTime;
    private String _wokeUpTime;
    private String _screenTime;
    private String _familyTime;
    private String _totalFreeSpent;

    public StudentActivity(String bedTime, String wokeUpTime, String screenTime, String familyTime){
        this._bedTime=bedTime;
        this._wokeUpTime=wokeUpTime;
        this._screenTime=screenTime;
        this._familyTime=familyTime;
    }

    public int getChildId() {
        return _childId;
    }

    public void setChildId(int childId){
        this._childId=childId;
    }

   /* public void set_childId(int _childId) {
        this._childId = _childId;
    }*/

    public String getBedTime() {
        return _bedTime;
    }

    public void setBedTime(String _bedTime) {
        this._bedTime = _bedTime;
    }

    public String getWokeUpTime() {
        return _wokeUpTime;
    }

    public void setWokeUpTime(String _wokeUpTime) {
        this._wokeUpTime = _wokeUpTime;
    }

    public String getScreenTime() {
        return _screenTime;
    }

    public void setScreenTime(String _screenTime) {
        this._screenTime = _screenTime;
    }

    public String getFamilyTime() {
        return _familyTime;
    }

    public void setFamilyTime(String _familyTime) {
        this._familyTime = _familyTime;
    }

    public String getTotalFreeTimeSpent() {
        return _totalFreeSpent;
    }
}
