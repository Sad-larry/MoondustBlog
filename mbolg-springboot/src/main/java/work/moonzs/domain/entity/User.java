package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户基础信息表(User)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class User {
    //主键ID
    @TableId
    private Long id;
    //账号
    private String username;
    //登录密码
    private String password;
    //用户详情ID
    private Long userAuthId;
    //角色ID
    private Long roleId;
    //用户状态(0禁用,1正常)
    private Integer status;
    //ip地址
    private String ipAddress;
    //ip来源
    private String ipSource;
    //登录系统
    private String os;
    //登录方式
    private Integer loginType;
    //浏览器
    private String browser;
    //最后登录时间
    private Date lastLoginTime;
    //创建时间
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
}

