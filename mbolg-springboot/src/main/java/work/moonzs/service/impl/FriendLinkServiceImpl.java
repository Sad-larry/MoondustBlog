package work.moonzs.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.web.common.BusinessAssert;
import work.moonzs.domain.entity.FriendLink;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysFriendLinkVO;
import work.moonzs.mapper.FriendLinkMapper;
import work.moonzs.service.FriendLinkService;

import java.util.List;

/**
 * 友情链接表(FriendLink)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:16
 */
@Service("friendLinkService")
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink> implements FriendLinkService {
    /**
     * 通过url判断是否重复请求
     *
     * @param url url
     * @return boolean
     */
    private boolean isExistFriendLinkByUrl(String url) {
        return count(new LambdaQueryWrapper<FriendLink>().eq(FriendLink::getUrl, url)) > 0;
    }

    /**
     * 判断修改后是否存在相同的url链接
     *
     * @param url url
     * @return boolean
     */
    private boolean isExistSameFriendLinkByUrl(String url) {
        return count(new LambdaQueryWrapper<FriendLink>().eq(FriendLink::getUrl, url)) > 1;
    }

    @Override
    public PageVO<SysFriendLinkVO> listFriendLink(Integer pageNum, Integer pageSize, String name, Integer status) {
        LambdaQueryWrapper<FriendLink> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(name), FriendLink::getName, name)
                .eq(ObjUtil.isNotNull(status), FriendLink::getStatus, status)
                .orderByAsc(FriendLink::getSort);
        Page<FriendLink> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        return new PageVO<>(BeanCopyUtil.copyBeanList(page.getRecords(), SysFriendLinkVO.class), page.getTotal());
    }

    @Override
    public Long insertFriendLink(FriendLink friendLink) {
        BusinessAssert.isFalse(isExistFriendLinkByUrl(friendLink.getUrl()), AppHttpCodeEnum.DATA_EXIST);
        baseMapper.insert(friendLink);
        return friendLink.getId();
    }

    @Override
    public boolean updateFriendLink(FriendLink friendLink) {
        FriendLink byId = getById(friendLink.getId());
        // 更新的数据存在
        BusinessAssert.notNull(byId, AppHttpCodeEnum.DATA_NOT_EXIST);
        boolean isPass = byId.getStatus().equals(StatusConstants.NORMAL);
        // 更新的数据必须审核通过
        BusinessAssert.isTure(isPass, AppHttpCodeEnum.DATA_UNDER_REVIEW);
        updateById(friendLink);
        // 更新的数据在网站地址这块不能一样
        BusinessAssert.isFalse(isExistSameFriendLinkByUrl(friendLink.getUrl()), AppHttpCodeEnum.DATA_EXIST);
        return true;
    }

    @Override
    public boolean deleteFriendLink(Long[] friendLinkIds) {
        return removeBatchByIds(List.of(friendLinkIds));
    }

    @Override
    public void topFriendLink(Long friendLinkId) {
        LambdaUpdateWrapper<FriendLink> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(FriendLink::getId, friendLinkId).set(FriendLink::getSort, 0);
        update(updateWrapper);
    }

    @Override
    public void passFriendLink(Long[] friendLinkIds) {
        LambdaUpdateWrapper<FriendLink> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(FriendLink::getId, List.of(friendLinkIds)).set(FriendLink::getStatus, StatusConstants.NORMAL);
        update(updateWrapper);
    }
}

