package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (Image)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-17 14:28:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_image")
public class Image {
    //主键    
    @TableId
    private Long id;
    //图片名称
    private String imageName;
    //图片描述
    private String imageDesc;
    //图片地址
    private String imageSrc;
    //图片状态(0停用,1正常)
    private Integer status;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
}

