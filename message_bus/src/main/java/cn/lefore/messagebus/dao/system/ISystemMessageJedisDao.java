package cn.lefore.messagebus.dao.system;

/**
 * Created by wenrule on 2017/7/31.
 */
public interface ISystemMessageJedisDao {

    void save(String sysMess);

    String getSystemMessage(String key);
}
