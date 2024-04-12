package center.helloworld.server.device.server.device.service;

import center.helloworld.server.device.api.model.device.Device;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author zhishun.cai
 * @create 2024/4/12
 * @note
 */
public interface DeviceService  extends IService<Device> {

    /**
     * 根据客户端ID查询
     * @param clientId
     * @return
     */
    Device deviceByClientId(String clientId);
}
