package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (Tag)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-17 14:28:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_tag")
public class Tag {
    //主键    
    @TableId
    private Long id;
    //标签名
    private String tagName;
    //标签描述
    private String description;
    //标签状态(0停用,1正常)
    private Integer status;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
}

