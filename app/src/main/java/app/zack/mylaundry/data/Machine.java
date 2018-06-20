package app.zack.mylaundry.data;

public class Machine {


    private long machineId;
    private String machineName;
    private String gwip, gwid, rssi;


    public String getGwip() {
        return gwip;
    }

    public void setGwip(String gwip) {
        this.gwip = gwip;
    }

    public String getGwid() {
        return gwid;
    }

    public void setGwid(String gwid) {
        this.gwid = gwid;
    }



    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }
}
