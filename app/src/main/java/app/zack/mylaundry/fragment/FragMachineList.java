package app.zack.mylaundry.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import app.zack.mylaundry.R;
import app.zack.mylaundry.data.Machine;
import app.zack.mylaundry.data.MachineAdapter;
import app.zack.mylaundry.utils.MqttPublisher;
import app.zack.mylaundry.utils.MyLogger;
import app.zack.mylaundry.utils.MySharedPer;

public class FragMachineList extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    Handler mHandler;

    private ListView lvMachine;
    private SwipeRefreshLayout swipe;
    private CountDownTimer timer;
    private MachineAdapter mAdapter;
    private List<Machine> machineList = new ArrayList<Machine>();
    private ImageView imgLogo;
    MyLogger log = MyLogger.getLogger(getClass().getSimpleName());
    MqttPublisher mMqtt;
    MySharedPer perf;


    private MqttCallback mMqttCallback = new MqttCallback() {

        @Override
        public void connectionLost(Throwable cause) {
            log.e(cause);
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            log.d(topic + "@@@@" + message);
            machineList.clear();
            JSONArray arr = new JSONArray(message.toString());
            JSONObject obj = (JSONObject) arr.get(0);
            perf.setMachineInfo(obj.getString("macAddr"), obj);
            refreshList();
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            log.i("deliveryComplete :" + token);
        }
    };

    public void refreshList() {
        machineList.clear();
        if (perf == null) {
            perf = new MySharedPer(mActivity);
        }
        if (perf.getMachineList() != null) {
            HashSet<String> set = (HashSet) perf.getMachineList();
            for (String s : set) {
                Machine mMachine = new Machine();
                mMachine.setMachineName(s);
                machineList.add(mMachine);
            }
            mAdapter.setList(machineList);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void test(String message) {
        machineList.clear();
        JSONArray arr = null;
        try {
            arr = new JSONArray(message);
            JSONObject obj = (JSONObject) arr.get(0);
            perf.setMachineInfo(obj.getString("macAddr"), obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        refreshList();
    }

    private IMqttActionListener mIMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            log.e("publish onSuccess");
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            log.e("publish onFailure");
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_machine_list, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mHandler = new Handler();
        imgLogo = (ImageView) view.findViewById(R.id.img_logo);
        imgLogo.setOnClickListener(this);


        mMqtt = MqttPublisher.getPublisher();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mMqtt.connect(mActivity, mMqttCallback, mIMqttActionListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 3000);

        lvMachine = (ListView) view.findViewById(R.id.lvMessage);
        lvMachine.setOnItemClickListener(this);
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe.setRefreshing(false);
                        refreshList();
                    }
                }, 1000);
            }
        });
        swipe.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light);
        mAdapter = new MachineAdapter(getActivity());
        mAdapter.setList(machineList);
        lvMachine.setAdapter(mAdapter);
        refreshList();
//        test("[{\"channel\":923375000, \"sf\":10, \"time\":\"2018-06-12T04:18:10\", \"gwip\":\"140.114.71.156\", \"gwid\":\"00001c497b431e9f\", \"repeater\":\"00000000ffffffff\", \"systype\":18, \"rssi\":-124.8, \"snr\":-14.8, \"snr_max\":5.0, \"snr_min\":-20.5, \"macAddr\":\"0000000012345617\", \"data\":\"b000\", \"frameCnt\":36, \"fport\":15}]");
//        test("[{\"channel\":923375000, \"sf\":10, \"time\":\"2018-06-12T06:18:10\", \"gwip\":\"140.114.71.156\", \"gwid\":\"00001c497b431e9f\", \"repeater\":\"00000000ffffffff\", \"systype\":18, \"rssi\":-124.8, \"snr\":-14.8, \"snr_max\":5.0, \"snr_min\":-20.5, \"macAddr\":\"0000000012345617\", \"data\":\"b000\", \"frameCnt\":36, \"fport\":15}]");
//        test("[{\"channel\":923375000, \"sf\":10, \"time\":\"2018-06-13T01:18:10\", \"gwip\":\"140.114.71.156\", \"gwid\":\"00001c497b431e9f\", \"repeater\":\"00000000ffffffff\", \"systype\":18, \"rssi\":-124.8, \"snr\":-14.8, \"snr_max\":5.0, \"snr_min\":-20.5, \"macAddr\":\"0000000012345617\", \"data\":\"b000\", \"frameCnt\":36, \"fport\":15}]");
//        test("[{\"channel\":923375000, \"sf\":10, \"time\":\"2018-06-18T15:18:10\", \"gwip\":\"140.114.71.156\", \"gwid\":\"00001c497b431e9f\", \"repeater\":\"00000000ffffffff\", \"systype\":18, \"rssi\":-124.8, \"snr\":-14.8, \"snr_max\":5.0, \"snr_min\":-20.5, \"macAddr\":\"0000000012345670\", \"data\":\"b000\", \"frameCnt\":36, \"fport\":15}]");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_logo:
                mActivity.replaceFragment(R.layout.frag_user_info);
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mAdapter.notifyDataSetChanged();
        perf.setCurMachine(mAdapter.getItem(position).getMachineName());
        mActivity.replaceFragment(R.layout.frag_report);
    }
}
