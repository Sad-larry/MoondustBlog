package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (User)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-17 14:28:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class User {
    //主键    
    @TableId
    private Long id;
    //用户名
    private String userName;
    //昵称
    private String nickName;
    //密码
    private String password;
    //手机号
    private String mobile;
    //电子邮箱
    private String email;
    //用户头像
    private String avatar;
    //个人简介
    private String intro;
    //生日
    private Date birthday;
    //账号状态(0停用,1正常)
    private Integer status;
    //注册时间
    private Date createTime;
    //更新时间
    private Date updateTime;
}

