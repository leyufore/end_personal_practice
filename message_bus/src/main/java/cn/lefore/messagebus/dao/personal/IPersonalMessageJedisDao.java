package cn.lefore.messagebus.dao.personal;

/**
 * author: lefore
 * date: 2017/8/7
 * email: 862080515@qq.com
 */
public interface IPersonalMessageJedisDao {

    void addCollMess(String collMes);

    void delCollMess(String collMes);

    void bufferAndProcessMess(String jsonMess);
}
