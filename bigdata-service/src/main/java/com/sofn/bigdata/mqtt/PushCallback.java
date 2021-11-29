package com.sofn.bigdata.mqtt;

/**
 *
 * Description:
 * @author admin
 */

import com.alibaba.fastjson.JSON;
import com.sofn.bigdata.entities.MarshInfo;
import com.sofn.bigdata.kafka.MyKafkaProducer;
import com.sofn.bigdata.kafka.ProducerManager;
import com.sofn.bigdata.utils.MarshJudgeUtil;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

import static com.sofn.bigdata.utils.MarshJudgeUtil.reLoadConfig;

/**
 * 发布消息的回调类
 *
 * 必须实现MqttCallback的接口并实现对应的相关接口方法CallBack 类将实现 MqttCallBack。
 * 每个客户机标识都需要一个回调实例。在此示例中，构造函数传递客户机标识以另存为实例数据。
 * 在回调中，将它用来标识已经启动了该回调的哪个实例。
 * 必须在回调类中实现三个方法：
 *
 *  public void messageArrived(MqttTopic topic, MqttMessage message)接收已经预订的发布。
 *
 *  public void connectionLost(Throwable cause)在断开连接时调用。
 *
 *  public void deliveryComplete(MqttDeliveryToken token))
 *  接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用。
 *  由 MqttClient.connect 激活此回调。
 *
 */
public class PushCallback implements MqttCallback {
    protected String brokerList;  //在回调函数中，将消息发送到kafka中

    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        System.out.println("连接断开...");
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // subscribe后得到的消息会执行到这里面
        System.out.println("接收消息主题 : " + topic);
        System.out.println("接收消息Qos : " + message.getQos());
        System.out.println("接收消息内容 : " + new String(message.getPayload()));
        sentToKafka(topic,new String(message.getPayload()));
    }

    private void sentToKafka(String topic, String message){
        try {
            MarshInfo marshInfo = JSON.parseObject(message, MarshInfo.class);
            reLoadConfig();
            if (MarshJudgeUtil.isValid(marshInfo)) {
                //注意，此种方式，每条消息还必须完整的包含siteId,采集频率字段
                if(marshInfo.getSiteId()==null || marshInfo.getCollectFrequency()==null){
                    System.out.println("报文信息有误...");
                }

                //如果当前报文有效，补充字段(按天分区字段)，录入到hive仓库
                marshInfo.setDay(marshInfo.getMonitorTime().substring(0, 10).replaceAll("-", ""));
                List<String> list = new ArrayList<>();
                list.add(marshInfo.toString());
                MyKafkaProducer kafkaProducer = ProducerManager.getProducer(
                        topic,
                        brokerList,
                        list);
                ProducerManager.pool.submit(kafkaProducer);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("报文异常！");
        }
    }
}
