package work.moonzs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.entity.Comment;
import work.moonzs.domain.vo.CommentVo;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.mapper.CommentMapper;
import work.moonzs.service.CommentService;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:10
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Override
    public PageVo<CommentVo> listComments(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<Comment> list = page.getRecords();
        List<CommentVo> commentListVos = BeanCopyUtil.copyBeanList(list, CommentVo.class);
        return new PageVo<>(commentListVos, page.getTotal(), page.getCurrent(), page.getSize());
    }
}

