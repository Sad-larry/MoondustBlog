package work.moonzs.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 博客-标签关联表(ArticleTag)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_article_tag")
public class ArticleTag {
    //主键ID    
    @TableId
    private Long id;
    //文章ID
    private Long articleId;
    //标签ID
    private Long tagId;
}

