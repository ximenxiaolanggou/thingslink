package center.helloworld.server.device.server.device.web;

import center.helloworld.server.device.server.device.service.DeviceService;
import center.helloworld.server.device.api.model.device.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhishun.cai
 * @create 2024/4/12
 * @note
 */
@RestController
@RequestMapping("device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    /**
     * 根据客户端ID查询
     * @param clientId
     * @return
     */
    @GetMapping("deviceByClientId")
    public Device deviceByClientId(@RequestParam("clientId") String clientId) {
        return deviceService.deviceByClientId(clientId);
    }
}
