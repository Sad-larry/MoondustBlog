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
 * 相册(PhotoAlbum)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_photo_album")
public class PhotoAlbum {
    //主键ID    
    @TableId
    private Long id;
    //相册名
    private String name;
    //相册描述
    private String info;
    //相册封面
    private String cover;
    //状态值(0公开,1私密)
    private Integer status;
    //创建时间
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
}

