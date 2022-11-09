package work.moonzs.domain.vo.sys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysMessageVO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 内容
     */
    private String content;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * ip地址
     */
    private String ipAddress;
    /**
     * ip来源
     */
    private String ipSource;
    /**
     * 留言时间
     */
    private Long time;
    /**
     * 状态(0审核,1正常)
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
}
