package com.mqttjitter.jitterlitter.mqtt;

public class MqttLinker {

    private static  String MQTT_BROKER_URL ;

    private static String PUBLISH_TOPIC ;
    private static String SUBSCRIBE_TOPIC ;

    private static String CLIENT_ID ;

    private static char[] PASSWORD ;

    private static String USERNAME ;

    public void setAIO(String aio){
        PASSWORD = aio.toCharArray();
    }
    public void setUSERNAME(String username){
        USERNAME = username;        
    }
    public void setClientId(String clientId){
        CLIENT_ID = clientId;
    }
    public void setPublishTopic(String publishTopic){
        PUBLISH_TOPIC = publishTopic;
    }

    public void setSubscribeTopic(String subscribeTopic){
        SUBSCRIBE_TOPIC = subscribeTopic;
    }

    public void setMqttBrokerUrl(String url){
        MQTT_BROKER_URL = "tcp://" + url +":1883";
    }

    public static String getClientId() {
        return CLIENT_ID;
    }

    public static String getMqttBrokerUrl() {
        return MQTT_BROKER_URL;
    }

    public static String getPublishTopic() {
        return PUBLISH_TOPIC;
    }

    public static String getSubscribeTopic() {
        return SUBSCRIBE_TOPIC;
    }

    public static String getUSERNAME() {
        return USERNAME;
    }

    public static char[] getPASSWORD() {
        return PASSWORD;
    }
}


