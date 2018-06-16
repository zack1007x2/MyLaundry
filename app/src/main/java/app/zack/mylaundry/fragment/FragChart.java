package app.zack.mylaundry.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.zack.mylaundry.R;
import app.zack.mylaundry.utils.MySharedPer;

public class FragChart extends BaseFragment implements View.OnClickListener{

    String data;
    MySharedPer perf;
    WebView webView;
    Button btnRefresh;


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

    private void initView(View view) {
        btnRefresh = (Button)view.findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(this);
        webView = (WebView) view.findViewById(R.id.web);
        webView.addJavascriptInterface(new WebAppInterface(), "Android");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/timeline.html");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_refresh:
//                test("[{\"channel\":923375000, \"sf\":10, \"time\":\"2018-06-17T14:18:10\", \"gwip\":\"140.114.71.156\", \"gwid\":\"00001c497b431e9f\", \"repeater\":\"00000000ffffffff\", \"systype\":18, \"rssi\":-124.8, \"snr\":-14.8, \"snr_max\":5.0, \"snr_min\":-20.5, \"macAddr\":\"0000000012345617\", \"data\":\"b000\", \"frameCnt\":36, \"fport\":15}]");
                refreshView();
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
