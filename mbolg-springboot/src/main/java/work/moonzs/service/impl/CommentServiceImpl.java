package work.moonzs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.entity.Comment;
import work.moonzs.domain.vo.CommentListVo;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.enums.StatusConstants;
import work.moonzs.mapper.CommentMapper;
import work.moonzs.service.CommentService;
import work.moonzs.utils.BeanCopyUtils;

import java.util.List;

/**
 * (Comment)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:04
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    /**
     * 评论列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link ResponseResult}<{@link ?}>
     */
    @Override
    public ResponseResult<?> listComments(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getStatus, StatusConstants.NORMAL);
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<Comment> list = page.getRecords();
        List<CommentListVo> commentListVos = BeanCopyUtils.copyBeanList(list, CommentListVo.class);
        PageVo<CommentListVo> pageVo = new PageVo<>(commentListVos, page.getTotal(), page.getCurrent(), page.getSize());
        return ResponseResult.success(pageVo);
    }
}

