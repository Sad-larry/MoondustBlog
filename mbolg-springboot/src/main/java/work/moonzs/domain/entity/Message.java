package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 留言板表(Message)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_message")
public class Message {
    //主键ID    
    @TableId
    private Long id;
    //内容
    private String content;
    //用户昵称
    private String nickname;
    //用户头像
    private String avatar;
    //ip地址
    private String ipAddress;
    //ip来源
    private String ipSource;
    //留言时间
    private Long time;
    //状态(0审核,1正常)
    private Integer status;
    //创建时间
    private Date createTime;
}

