package center.helloworld.server.device.server.device.mapper;

import center.helloworld.server.device.api.model.device.Device;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhishun.cai
 * @create 2024/4/12
 * @note
 */

@Mapper
public interface DeviceMapper extends BaseMapper<Device> {
}
