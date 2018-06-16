package app.zack.mylaundry.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;


public class MySharedPer {

    public final static String Cur = "Current";
    public final static String Cur_M = "Current_Machine";
    public final static String L = "_list";
    public final static String MACHINE_LIST = "machine_list";
    SharedPreferences sp;

    public MySharedPer(Context c) {
        sp = c.getSharedPreferences("app", Context.MODE_PRIVATE);
    }


    public boolean checkIDExist(String str) {
        return TextUtils.isEmpty(sp.getString(str, null));
    }

    public boolean varifyLogin(String id, String pwd) {
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(pwd)) {
            return false;
        } else {
            Log.e("Zack", sp.getString(id, null) + "@@" + pwd);

            if (pwd.equals(sp.getString(id, null))) {
                sp.edit()
                        .putString(Cur, id)
                        .putString(Cur + L, sp.getString(id + L, null))
                        .apply();
                return true;
            } else {
                return false;
            }
        }

    }

    public void regester(String ID, String PWD) {
        JSONArray list = new JSONArray();
        try {
            list.put(0, PWD);
            list.put(3, ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("LIST----------", list.toString());
        sp.edit()
                .putString(ID, PWD)
                .apply();

        sp.edit()
                .putString(ID + L, list.toString())
                .apply();

        sp.edit()
                .putString(Cur, ID)
                .putString(Cur + L, list.toString())
                .apply();
    }


    public void setUserInfo(String name, String phone, String mail, String other) {
        try {

            String id = sp.getString(Cur, null);
            String list = sp.getString(Cur + L, null);
            JSONArray jlist = new JSONArray(list);
            jlist.put(1, name);
            jlist.put(2, phone);
            jlist.put(3, mail);
            jlist.put(4, other);
            list = jlist.toString();

            sp.edit()
                    .putString(id + L, list)
                    .apply();

            sp.edit()
                    .putString(Cur + L, list)
                    .apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void logout() {
        sp.edit().remove(Cur).remove(Cur + L).apply();
    }

    public boolean isLogin() {
        return !TextUtils.isEmpty(sp.getString(Cur, null));
    }

    public String getUserInfo() {
        String id = sp.getString(Cur, null);
        Log.d("getUserInfo", sp.getString(id + L, null));
        return sp.getString(id + L, null);
    }

    public boolean setMachineInfo(String macAddr, JSONObject JObjtoStore) {
        String machineInfo = sp.getString(macAddr, null);
        addMachineList(macAddr);
        if (machineInfo == null) {
            JSONArray arr = new JSONArray();
            arr.put(JObjtoStore);
            sp.edit().putString(macAddr, arr.toString()).apply();
            return true;
        }

        JSONObject obj;

        try {
            JSONArray arr = new JSONArray(machineInfo);
            obj = (JSONObject) arr.get(arr.length() - 1);
            Date lastTime = fromISO8601UTC(obj.getString("time"));
            Date currentTime = fromISO8601UTC(JObjtoStore.getString("time"));
            if ((currentTime.getTime() - lastTime.getTime()) > 1800000) {//>30 min
                arr.put(JObjtoStore);
                sp.edit().putString(macAddr, arr.toString()).apply();
                return true;
            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getMachineInfo(String macAddr) {
        String machineInfo = sp.getString(macAddr, null);
        return machineInfo;
    }

    public void setCurMachine(String macAddr){
        sp.edit().putString(Cur_M, macAddr).apply();
    }

    public String getCurMachine(){
        return sp.getString(Cur_M, null);
    }

    public void addMachineList(String macAddr) {
        HashSet set = (HashSet) sp.getStringSet(MACHINE_LIST, null);
        if(set==null){
            set = new HashSet();
        }
        if (!set.contains(macAddr)) {
            set.add(macAddr);
            sp.edit().putStringSet(MACHINE_LIST, set).apply();

        }
        return;
    }

    public Set getMachineList(){
        return sp.getStringSet(MACHINE_LIST, null);
    }

    public static Date fromISO8601UTC(String dateStr) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(tz);

        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
