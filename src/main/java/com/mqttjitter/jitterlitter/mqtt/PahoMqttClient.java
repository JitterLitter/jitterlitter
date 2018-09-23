package com.mqttjitter.jitterlitter.mqtt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;


public class PahoMqttClient {
    Context context;
    private static final String TAG = "PahoMqttClient";
    private MqttAndroidClient mqttAndroidClient;

    public void setMqttAndroidClient(MqttAndroidClient mqttAndroidClient) {
        this.mqttAndroidClient = mqttAndroidClient;
    }

    public MqttAndroidClient sendMqttClientSchedule(final Context context, String brokerUrl, final String clientId, final String msg){

        this.context = context;
        mqttAndroidClient = new MqttAndroidClient(context, brokerUrl, clientId);
        try {
            final IMqttToken token = mqttAndroidClient.connect(getMqttConnectionOption());
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    mqttAndroidClient.setBufferOpts(getDisconnectedBufferOptions());
                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    try {
                       publishMessage(mqttAndroidClient, msg, 1, MqttLinker.getPublishTopic());
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    Log.d(TAG, "Success");
                    try {
                        subscribe(mqttAndroidClient, MqttLinker.getSubscribeTopic(),1);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "Failure " + exception.toString());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        return mqttAndroidClient;

    }

    public MqttAndroidClient getMqttClient(final Context context, String brokerUrl, final String clientId) {
        this.context = context;
        mqttAndroidClient = new MqttAndroidClient(context, brokerUrl, clientId);
        try {
            IMqttToken token = mqttAndroidClient.connect(getMqttConnectionOption());
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    mqttAndroidClient.setBufferOpts(getDisconnectedBufferOptions());
                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Success");
                    try {
                        subscribe(mqttAndroidClient, MqttLinker.getSubscribeTopic(),1);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "Failure " + exception.toString());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        return mqttAndroidClient;
    }

    public void disconnectt(@NonNull final MqttAndroidClient client) throws MqttException {
        IMqttToken mqttToken = client.disconnect();
        mqttToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "Successfully disconnected");
                //Toast.makeText(context, "successfully connected : ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.d(TAG, "Failed to disconnected " + throwable.toString());

              //  Toast.makeText(context, "Failed to disconnect : ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    private DisconnectedBufferOptions getDisconnectedBufferOptions() {
        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
        disconnectedBufferOptions.setBufferEnabled(true);
        disconnectedBufferOptions.setBufferSize(100);
        disconnectedBufferOptions.setPersistBuffer(false);
        disconnectedBufferOptions.setDeleteOldestMessages(false);
        return disconnectedBufferOptions;
    }

    @NonNull
    private MqttConnectOptions getMqttConnectionOption() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setUserName(MqttLinker.getUSERNAME());
        mqttConnectOptions.setPassword(MqttLinker.getPASSWORD());
        mqttConnectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_DEFAULT);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setKeepAliveInterval(600000);
        return mqttConnectOptions;

    }


    public void publishMessage(@NonNull MqttAndroidClient client, @NonNull String msg, int qos, @NonNull String topic)
            throws MqttException, UnsupportedEncodingException {
        byte[] encodedPayload = new byte[0];
        encodedPayload = msg.getBytes("UTF-8");
        MqttMessage message = new MqttMessage(encodedPayload);
        message.setId(320);
        message.setRetained(true);
        message.setQos(qos);
        client.publish(topic, message);
    }

    public void subscribe(@NonNull MqttAndroidClient client, @NonNull final String topic, int qos) throws MqttException {
        IMqttToken token = client.subscribe(topic, qos);

        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "Subscribe Successfully " + topic);
              //  Toast.makeText(context, "successfully Subscribe" + topic, Toast.LENGTH_SHORT).show()
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.e(TAG, "Subscribe Failed " + topic);
               // Toast.makeText(context, "failed Subscribe" + topic, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void unSubscribe(@NonNull MqttAndroidClient client, @NonNull final String topic) throws MqttException {

        IMqttToken token = client.unsubscribe(topic);

        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "UnSubscribe Successfully " + topic);
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.e(TAG, "UnSubscribe Failed " + topic);
            }
        });
    }

}


