package work.moonzs.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVO {
    /**
     * 电子邮件
     */
    private String email;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 介绍
     */
    private String intro;
    /**
     * 个人网站
     */
    private String webSite;
}