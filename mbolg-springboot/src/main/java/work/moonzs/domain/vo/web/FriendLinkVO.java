package work.moonzs.domain.vo.web;

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
public class FriendLinkVO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 网站名称
     */
    private String name;
    /**
     * 网站地址
     */
    private String url;
    /**
     * 网站头像地址
     */
    private String avatar;
    /**
     * 网站描述
     */
    private String info;
    /**
     * 创建时间
     */
    private Date createTime;
}
