package work.moonzs.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.web.common.BusinessAssert;
import work.moonzs.domain.entity.Category;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysCategoryVO;
import work.moonzs.domain.vo.web.CategoryVO;
import work.moonzs.mapper.CategoryMapper;
import work.moonzs.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 博客分类表(Category)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:06
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public boolean isExistCategoryById(Long categoryId) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getId, categoryId);
        return count(queryWrapper) > 0;
    }

    /**
     * 通过分类名字查询分类
     *
     * @param categoryName 分类名字
     * @return {@link Long}
     */
    public Long selectCategoryByCategoryName(String categoryName) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getName, categoryName);
        Category category = getOne(queryWrapper);
        if (category != null) {
            return category.getId();
        }
        return null;
    }

    @Override
    public boolean isExistCategoryByCategoryName(String categoryName) {
        return ObjUtil.isNotNull(selectCategoryByCategoryName(categoryName));
    }

    /**
     * 通过分类名字判断是存在相同分类，这个方法是在更新操作完之后调用的，判断数据库中是否有相同的分类名的分类
     *
     * @param categoryName 分类名字
     * @return boolean
     */
    public boolean isExistSameCategoryByCategoryName(String categoryName) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getName, categoryName);
        // 不同判断状态
        return count(queryWrapper) > 1;
    }

    @Override
    public PageVO<SysCategoryVO> listCategory(Integer pageNum, Integer pageSize, String fuzzyField) {
        Page<Category> page = new Page<>(pageNum, pageSize);
        // 对名字进行模糊查询，当模糊字段为空时则不匹配
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(fuzzyField), Category::getName, fuzzyField);
        page(page, queryWrapper);
        return new PageVO<>(BeanCopyUtil.copyBeanList(page.getRecords(), SysCategoryVO.class), page.getTotal());
    }

    @Override
    public SysCategoryVO getCategoryById(Long categoryId) {
        Category byId = getById(categoryId);
        return ObjUtil.isNotNull(byId) ? BeanCopyUtil.copyBean(byId, SysCategoryVO.class) : null;
    }

    @Override
    public Long insertCategory(Category category) {
        // 判断分类名是否有相同的，有就不添加
        // 如果存在相同的分类，isExistCategory为true，那么就不符合方法条件，断言失败抛出异常
        BusinessAssert.isFalse(isExistCategoryByCategoryName(category.getName()), AppHttpCodeEnum.CATEGORY_EXIST);
        getBaseMapper().insert(category);
        return category.getId();
    }

    @Override
    public boolean updateCategory(Category category) {
        // 判断该ID的分类是否存在，如果不存在则返回失败
        BusinessAssert.isTure(isExistCategoryById(category.getId()), AppHttpCodeEnum.CATEGORY_NOT_EXIST);
        updateById(category);
        // 判断相同的分类名是否不存在，如果存在则返回失败，事务回滚，取消更新
        BusinessAssert.isFalse(isExistSameCategoryByCategoryName(category.getName()), AppHttpCodeEnum.CATEGORY_EXIST);
        return true;
    }

    @Override
    public boolean deleteCategory(Long[] categoryIds) {
        return removeBatchByIds(List.of(categoryIds));
    }

    @Override
    public Long insertCategoryWithName(String categoryName) {
        Long categoryId = selectCategoryByCategoryName(categoryName);
        // 不存在分类则插入
        if (ObjUtil.isNull(categoryId)) {
            Category category = new Category();
            category.setName(categoryName);
            getBaseMapper().insert(category);
            return category.getId();
        }
        return categoryId;
    }

    @Override
    public List<CategoryVO> listWebCategory() {
        // 前端博客页面只需 id,name以及文章数量ArticleNum 属性就可
        List<CategoryVO> categoryVOS = BeanCopyUtil.copyBeanList(baseMapper.listWebCategory(), CategoryVO.class);
        // 过滤掉文章数量大于0的分类id，并按照文章数量进行排序
        return categoryVOS.stream()
                .filter(categoryVO -> categoryVO.getArticleNum() > 0)
                .sorted((o1, o2) -> Integer.compare(o2.getArticleNum(), o1.getArticleNum()))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryVO getBlogCategoryById(Long articleId) {
        Category category = getOne(new LambdaQueryWrapper<Category>().select(Category::getId, Category::getName).eq(Category::getId, articleId));
        return BeanCopyUtil.copyBean(category, CategoryVO.class);
    }

    @Override
    public void incrCategoryClickVolume(Long categoryId) {
        LambdaUpdateWrapper<Category> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.setSql("click_volume = click_volume + 1");
        updateWrapper.eq(Category::getId, categoryId);
        update(updateWrapper);
    }
}


