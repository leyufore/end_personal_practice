package cn.lefore.messagebus;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.util.StringUtils;

/**
 * author: lefore
 * date: 2017/7/31
 * email: 862080515@qq.com
 */
public class App {

    public static void main(String[] args) throws Exception {
        if (args != null && args.length > 0 && !StringUtils.isEmpty(args[0])) {
            System.out.println("args : " + args != null ? "null" : args[0]);
            FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext(new String[]{
                    "config/applicationContext.xml",
                    "config/applicationContext-jedis.xml",
                    "config/applicationContext-rabbitmq.xml",
                    "config/applicationContext-quartz.xml"
            });
            /**
             * ctx.close()
             * 由于 main 中没有执行 ctx.close ，应用会处于一直运行的状态。
             * application-rabbitmq 处于一直监听的状态
             */
        } else {
            ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{
                    "applicationContext.xml",
                    "applicationContext-jedis.xml",
                    "applicationContext-rabbitmq.xml",
                    "applicationContext-quartz.xml"
            });
        }
        System.out.println("message bus has started");
    }

}
