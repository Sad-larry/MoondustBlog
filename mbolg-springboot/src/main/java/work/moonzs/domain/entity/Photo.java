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
 * 照片(Photo)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_photo")
public class Photo {
    //主键ID    
    @TableId
    private Long id;
    //相册ID
    private Long albumId;
    //照片名
    private String name;
    //照片描述
    private String info;
    //照片地址
    private String url;
    //创建时间
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
}

