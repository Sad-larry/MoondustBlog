package work.moonzs.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (ArticleTag)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-17 14:28:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_article_tag")
public class ArticleTag {
    //主键    
    @TableId
    private Long id;
    //文章id
    private Long articleId;
    //标签id
    private Long tagId;
}

