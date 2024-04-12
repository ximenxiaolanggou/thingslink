package center.helloworld.server.device.server.device.service.impl;

import center.helloworld.server.device.server.device.mapper.DeviceMapper;
import center.helloworld.server.device.server.device.service.DeviceService;
import center.helloworld.server.device.api.model.device.Device;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhishun.cai
 * @create 2024/4/12
 * @note
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    /**
     * 根据客户端ID查询
     * @param clientId
     * @return
     */
    @Override
    public Device deviceByClientId(String clientId) {
        return deviceMapper.selectOne(new LambdaQueryWrapper<Device>().eq(Device::getClientId, clientId));
    }
}
