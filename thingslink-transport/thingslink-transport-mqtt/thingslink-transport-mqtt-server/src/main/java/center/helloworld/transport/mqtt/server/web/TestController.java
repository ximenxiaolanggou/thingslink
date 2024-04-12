package center.helloworld.transport.mqtt.server.web;

import center.helloworld.server.device.api.service.device.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhishun.cai
 * @create 2024/4/12
 * @note
 */

@RestController
@RequestMapping
public class TestController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping
    public Object list() {
        return deviceService.deviceByClientId("62e78509e4b078f1a8733354");
    }
}
