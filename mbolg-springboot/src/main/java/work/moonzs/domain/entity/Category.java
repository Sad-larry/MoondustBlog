package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 博客分类表(Category)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_category")
public class Category {
    //主键ID    
    @TableId
    private Long id;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //分类名称
    private String name;
    //分类点击量
    private Integer clickVolume;
    //排序
    private Integer sort;
}

