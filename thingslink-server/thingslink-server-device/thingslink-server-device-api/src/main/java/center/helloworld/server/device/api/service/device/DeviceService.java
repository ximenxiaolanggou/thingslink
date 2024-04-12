package center.helloworld.server.device.api.service.device;

import center.helloworld.server.device.api.model.device.Device;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhishun.cai
 * @create 2024/4/12
 * @note
 */
@FeignClient(value = "Thingslink-Server-Device", path = "/server-device/device")
public interface DeviceService {

    @GetMapping("deviceByClientId")
    Device deviceByClientId(@RequestParam("clientId") String clientId);
}
