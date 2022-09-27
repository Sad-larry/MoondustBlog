package work.moonzs.domain.vo;

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
public class UserInfoVo {
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 电话
     */
    private String mobile;
    /**
     * 电子邮件
     */
    private String email;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 介绍
     */
    private String intro;
    /**
     * 生日
     */
    private Date birthday;
}