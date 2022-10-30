package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户反馈表(FeedBack)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_feed_back")
public class FeedBack {
    //主键ID    
    @TableId
    private Long id;
    //邮箱
    private String email;
    //标题
    private String title;
    //详细内容
    private String content;
    //图片地址
    private String imgUrl;
    //反馈类型(1需求,2缺陷)
    private Integer type;
    //添加时间
    private Date createTime;
}

