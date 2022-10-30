package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 前端页面表(Page)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_page")
public class Page {
    //主键ID    
    @TableId
    private Long id;
    //页面名称
    private String pageName;
    //页面标签
    private String pageLabel;
    //页面图源
    private String pageCover;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
}

