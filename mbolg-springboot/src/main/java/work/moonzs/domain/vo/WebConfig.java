package work.moonzs.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 网站配置表(WebConfig)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_web_config")
public class WebConfig {
    //主键ID    
    @TableId
    private Long id;
    //logo(文件UID)
    private String logo;
    //网站名称
    private String name;
    //介绍
    private String summary;
    //关键字
    private String keyword;
    //作者
    private String author;
    //备案号
    private String recordNum;
    //网站地址
    private String webUrl;
    //支付宝收款码FileId
    private String aliPay;
    //微信收款码FileId
    private String weixinPay;
    //github地址
    private String github;
    //gitee地址
    private String gitee;
    //QQ号
    private String qqNumber;
    //邮箱
    private String email;
    //显示的列表(用于控制邮箱、QQ、QQ群、Github、Gitee、微信是否显示在前端)
    private String showList;
    //登录方式列表(用于控制前端登录方式，如账号密码,码云,Github,QQ,微信)
    private String loginTypeList;
    //是否开启评论(0否,1是)
    private Integer openComment;
    //是否开启赞赏(0否,1是)
    private Integer openAdmiration;
    //游客头像
    private String touristAvatar;
    //公告
    private String bulletin;
    //作者简介
    private String authorInfo;
    //作者头像
    private String authorAvatar;
    //关于我
    private String aboutMe;
    //是否开启音乐播放器(0否,1是)
    private Integer isMusicPlayer;
    //创建时间
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
}

