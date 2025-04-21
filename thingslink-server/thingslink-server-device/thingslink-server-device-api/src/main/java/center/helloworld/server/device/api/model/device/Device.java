package center.helloworld.server.device.api.model.device;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zhishun.cai
 * @create 2024/4/12
 * @note 设备
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Device extends Model<Device> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 备注名称
     */
    private String remark;

    /**
     * clientId
     */
    private String clientId;

    /**
     * key
     */
    private String deviceKey;

    /**
     * 秘钥
     */
    private String deviceSecret;

    /**
     * ip
     */
    private String ip;

    /**
     * 0离线 1 在线
     */
    private Integer isOnline = 0;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 产品key
     */
    @TableField(exist = false)
    private String productKey;

    /**
     * 产品名
     */
    @TableField(exist = false)
    private String productName;

    /**
     * 认证方式
     */
    @TableField(exist = false)
    private Integer authWay;

    /**
     * 节点类型
     */
    @TableField(exist = false)
    private Integer nodeType;

    /**
     * 创建时间
     */
    private LocalDateTime createtime;

    /**
     * 修改时间
     */
    private LocalDateTime updatetime;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
