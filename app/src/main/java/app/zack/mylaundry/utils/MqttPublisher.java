package app.zack.mylaundry.utils;

import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by Zack on 2016/8/8.
 */

public class MqttPublisher {

    MyLogger Log = MyLogger.getLogger(getClass().getSimpleName());

    public static final int HEART_BEAT_INTERVAL = 5000;
    public static final String HOST = "tcp://167.99.224.125";
    public static final int PORT = 1883;
    public static final String TOPIC = "GIOT-GW/UL/1C497B43217A";
    public static final int QOS = 0;
    private String clientId = MqttClient.generateClientId();
//    private String clientId = "jasdfg";
    private MqttAndroidClient mqttClient = null;
    private Context context = null;
    private MqttConnectOptions connOpts;
    private boolean connected, isTryingConnecting;
    private static MqttPublisher publisher;
//    private Thread mHeartbeatThread;
    private IMqttActionListener mPublishCallback;


    private MqttPublisher() {
    }

    public synchronized static MqttPublisher getPublisher() {
        if (publisher == null) {
            publisher = new MqttPublisher();
        }
        return publisher;
    }

    public void connect(Context ctx, MqttCallback callback, IMqttActionListener publishCallback) throws Exception {
        if(isTryingConnecting)
            return;

        isTryingConnecting = true;
        this.context = ctx;
        mPublishCallback = publishCallback;
        String uri = HOST + ":" + PORT;
        Log.v("connecting to " + uri);

        mqttClient = new MqttAndroidClient(context, uri, clientId);
        connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setKeepAliveInterval(10000);
        connOpts.getDebug();
        connOpts.setAutomaticReconnect(true);
        mqttClient.setTraceEnabled(true);
        mqttClient.connect(connOpts, null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("connect success! : " + mqttClient.getResultCode());
                try {
                    mqttClient.subscribe(TOPIC, QOS);
                    connected = true;
//                    startHeartBeatThread();
                } catch (MqttException e) {
                    Log.e(e);
                    connected = false;
                }
                isTryingConnecting = false;
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.e("connect fail!");
                Log.e(exception);
                try {
                    disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                connected = false;
//                isTryingConnecting = false;
            }
        });
        mqttClient.setCallback(callback);
    }

//    private void startHeartBeatThread() {
//        mHeartbeatThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (connected){
//                    publishHeartBeat();
//                    SystemClock.sleep(HEART_BEAT_INTERVAL);
//                }
//            }
//        });
//        mHeartbeatThread.start();
//    }

    public void disconnect() throws Exception {
        Log.e("MQTT disconnect");
//        if(mHeartbeatThread!=null){
//            mHeartbeatThread.interrupt();
//            mHeartbeatThread = null;
//        }
        if (mqttClient != null) {
            mqttClient.disconnect();
            mqttClient.unregisterResources();
            mqttClient.close();
            mqttClient = null;
            connected = false;
        }

    }

    public boolean isConnected(){
        return connected;
    }

//    public String getMqttToken(long userId, String MUID, long timeStamp){
//        StringBuilder strToEncryptBuilder = new StringBuilder();
//        strToEncryptBuilder.append(TOKEN_KEY+"&");
//        strToEncryptBuilder.append("ubroadHeartbead&");
//        strToEncryptBuilder.append(WebUtils.getFormatedUserId(userId)+"&");
//        strToEncryptBuilder.append(MUID+"&");
//        strToEncryptBuilder.append(timeStamp);
//        strToEncryptBuilder.append("&");
//        return Utils.Encrypt_SHA1(strToEncryptBuilder.toString());
//    }

//    public String generateHeartBeatMsg(long userId, String MUID, long timeStamp){
//        StringBuilder ret = new StringBuilder();
//        ret.append("HEARTBEAT@");
//        ret.append(Utils.Encrypt_SHA1(getMqttToken(userId, MUID, timeStamp))+"@");
//        ret.append(timeStamp + "@");
//        ret.append(WebUtils.getFormatedUserId(userId) + "@");
//
//        return ret.toString();
//    }


//    public void publishHeartBeat() {
//        //HEARTBEAT@sha1Token@timestamp@UID@
//        User user;
//        try {
//            user = MyApplication.mDaoSession.getUserDao().loadAll().get(0);
//        }catch (IndexOutOfBoundsException e){
//            return;
//        }
//
//        String msg_heartbeat = generateHeartBeatMsg(user.getUserId(), user.getMuid(), System.currentTimeMillis() / 1000);
////        Log.i("msg_heartbeat : " + msg_heartbeat);
//        MqttMessage message = new MqttMessage(msg_heartbeat.getBytes());
//        message.setQos(QOS);
//        try {
//            mqttClient.publish(TOPIC, message);
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }

//    public void publishMqttMsg(Device device, String content){
//        while (MyApplication.losingMsgQueue.getMsgCount()>0){
//            publishMsg(MyApplication.losingMsgQueue.getQueueDevice(), MyApplication.losingMsgQueue.getQueueMsg());
//        }
//        publishMsg(device,content);
//
//    }
//
//    private void publishMsg(Device device, String content){
////        Log.d("MQTT service publishMqttMsg -- " + content);
//        MqttMessage message = new MqttMessage(content.getBytes());
//        message.setQos(QOS);
//        try {
//            mqttClient.publish(TOPIC, message, device, mPublishCallback);
//        } catch (MqttException e) {
//            e.printStackTrace();
//            MyApplication.losingMsgQueue.addQueueMsg(device, content);
//        }
//    }
}