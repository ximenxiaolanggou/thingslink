package center.helloworld.transport.mqtt.server.session.dao;

import center.helloworld.transport.mqtt.server.properties.MqttConfigProperty;
import center.helloworld.transport.mqtt.server.session.sessionstorage.SessionStorageStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * @author zhishun.cai
 * @date 2024/3/19
 * @note
 */

@Repository
public class SessionDao {

    @Autowired
    private SessionStorageStrategy sessionStorage;

    @Autowired
    private MqttConfigProperty property;


    @PostConstruct
    public void init() {
        System.out.println(sessionStorage);
    }



}
