package cn.lefore.messagebus;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author: lefore
 * date: 2017/8/5
 * email: 862080515@qq.com
 */
public class ProgramMessageServiceTest {

    public static void main(String[] args) {
        testRabbitmqSystemMessage();
    }

    public static void testRabbitmqSystemMessage(){
        ConnectionFactory cf = new CachingConnectionFactory("localhost");

        RabbitAdmin admin = new RabbitAdmin(cf);
        admin.declareQueue(new Queue("sysMessQueue"));

        RabbitTemplate rabbitTemplate = new RabbitTemplate(cf);
        rabbitTemplate.setRoutingKey("sysMessQueue");
        rabbitTemplate.setQueue("sysMessQueue");
        rabbitTemplate.convertAndSend("{\"userType\":\"moretv\",\"publishTime\":\"2000\",\"uid\":\"2\",\"type\":\"2\",\"content\":\"content_msg\",\"title\":\"title_msg\",\"code\":\"code_msg\",\"invalidTime\":\"2000\",\"displayTime\":\"2000\",\"validTime\":\"2000\"}");
    }

}
