package app.zack.mylaundry.data;

public class Machine {


    private long machineId;
    private String mStartTime, mStopTime;
    private boolean isBusy;
    private String userInUse;

    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }

    public String getmStartTime() {
        return mStartTime;
    }

    public void setmStartTime(String mStartTime) {
        this.mStartTime = mStartTime;
    }

    public String getmStopTime() {
        return mStopTime;
    }

    public void setmStopTime(String mStopTime) {
        this.mStopTime = mStopTime;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }

    public String getUserInUse() {
        return userInUse;
    }

    public void setUserInUse(String userInUse) {
        this.userInUse = userInUse;
    }



}
