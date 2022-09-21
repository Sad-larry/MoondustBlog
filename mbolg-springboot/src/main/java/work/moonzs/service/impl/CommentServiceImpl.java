package work.moonzs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.Comment;
import work.moonzs.mapper.CommentMapper;
import work.moonzs.service.CommentService;

/**
 * (Comment)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-09-21 19:37:18
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}

