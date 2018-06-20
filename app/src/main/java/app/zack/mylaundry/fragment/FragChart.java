package app.zack.mylaundry.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.zack.mylaundry.MainActivity;
import app.zack.mylaundry.R;
import app.zack.mylaundry.utils.CustomDialogBuilder;
import app.zack.mylaundry.utils.MySharedPer;

public class FragChart extends BaseFragment implements View.OnClickListener{

    String data;
    MySharedPer perf;
    WebView webView;
    Button btnRefresh, btnChooseDates;
    Dialog mDatePickerDialog;
    CustomDialogBuilder mDatesDialogBuilder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_report, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshView();

    }

    public void refreshView(){
        perf = new MySharedPer(mActivity);
        data = perf.getMachineInfo(perf.getCurMachine());
        webView.clearCache(true);
        webView.reload();
    }
    private boolean isSameDate(Date date1, Date date2){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyMMdd");
        return sdf.format(date1).equals(sdf.format(date2));
    }

    public void refreshSelectDate(){
        if(perf==null)
            perf = new MySharedPer(mActivity);

        List<Date> clist = fromCalenderList(mDatesDialogBuilder.getCalendarView().getSelectedDates());

        try {
            JSONArray arr = new JSONArray(perf.getMachineInfo(perf.getCurMachine()));
            JSONArray ret = new JSONArray();
            for(int i=0;i<arr.length(); i++){
                JSONObject curObj = arr.getJSONObject(i);
                String time = curObj.getString("time");
                Date occurdate = MySharedPer.fromISO8601UTC(time);
                for(Date picked:clist){
                    if(isSameDate(picked, occurdate)){
                        ret.put(curObj);
                    }
                }
            }
            data = ret.toString();
            Log.d("Select", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        webView.clearCache(true);
        webView.reload();
    }

    public List<Date> fromCalenderList( List<Calendar> clist) {
        List<Date> ret = new ArrayList<Date>();
        for (Calendar c:clist){
            ret.add(c.getTime());
        }
        return ret;
    }


    private void initView(View view) {
        btnRefresh = (Button)view.findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(this);
        btnChooseDates = (Button)view.findViewById(R.id.btn_date);
        btnChooseDates.setOnClickListener(this);
        webView = (WebView) view.findViewById(R.id.web);
        webView.addJavascriptInterface(new WebAppInterface(), "Android");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/timeline.html");
        mDatesDialogBuilder = new CustomDialogBuilder(mActivity, R.layout.custom_dialog_dates_picker);
        mDatePickerDialog = mDatesDialogBuilder.build();

        mDatePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(mDatesDialogBuilder.getCalendarView().getSelectedDates().size()==0)
                    refreshView();
                else
                    refreshSelectDate();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_refresh:
                if(MainActivity.DEBUG)
                    test("[{\"channel\":923375000, \"sf\":10, \"time\":\"2018-06-17T14:18:10\", \"gwip\":\"140.114.71.156\", \"gwid\":\"00001c497b431e9f\", \"repeater\":\"00000000ffffffff\", \"systype\":18, \"rssi\":-124.8, \"snr\":-14.8, \"snr_max\":5.0, \"snr_min\":-20.5, \"macAddr\":\"0000000012345617\", \"data\":\"b000\", \"frameCnt\":36, \"fport\":15}]");
                refreshView();
                break;
            case R.id.btn_date:
                mDatePickerDialog.show();
                break;

        }
    }

    public void test(String message) {
        JSONArray arr = null;
        try {
            arr = new JSONArray(message);
            JSONObject obj = (JSONObject) arr.get(0);
            perf.setMachineInfo(obj.getString("macAddr"), obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class WebAppInterface {

        @JavascriptInterface
        public String getData() {
            return data;
        }


    }

}
