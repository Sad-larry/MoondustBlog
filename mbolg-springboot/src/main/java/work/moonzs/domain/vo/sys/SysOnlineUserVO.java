package work.moonzs.domain.vo.sys;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "在线用户DTO")
public class SysOnlineUserVO {
    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 用户唯一标识
     */
    private String userUid;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * ip地址
     */
    private String ipAddress;
    /**
     * ip来源
     */
    private String ipSource;
    /**
     * 登录系统
     */
    private String os;
    /**
     * 浏览器
     */
    private String browser;
    /**
     * 登录时间
     */
    private Date loginTime;
    /**
     * 最近一次操作时间
     */
    private Date lastActivityTime;
}
