package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户信息表(UserAuth)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user_auth")
public class UserAuth {
    //用户ID    
    @TableId
    private Long id;
    //邮箱号
    private String email;
    //用户昵称
    private String nickname;
    //用户头像
    private String avatar;
    //用户简介
    private String intro;
    //个人网站
    private String webSite;
    //是否禁用(0否,1是)
    private Integer isDisable;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
}

