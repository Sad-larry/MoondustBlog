package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 友情链接表(FriendLink)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_friend_link")
public class FriendLink {
    //主键ID    
    @TableId
    private Long id;
    //网站名称
    private String name;
    //网站地址
    private String url;
    //网站头像地址
    private String avatar;
    //网站描述
    private String info;
    //邮箱
    private String email;
    //排序
    private Integer sort;
    //ENUM-状态(0待审核,1通过)
    private Integer status;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
}

