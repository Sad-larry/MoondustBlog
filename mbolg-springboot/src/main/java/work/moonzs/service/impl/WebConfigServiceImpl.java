package work.moonzs.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.entity.WebConfig;
import work.moonzs.domain.vo.sys.SysWebConfigVO;
import work.moonzs.mapper.WebConfigMapper;
import work.moonzs.service.WebConfigService;

import java.util.List;

import static work.moonzs.base.enums.StatusConstants.LIMIT_ONE;

/**
 * 网站配置表(WebConfig)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:36
 */
@Service("webConfigService")
public class WebConfigServiceImpl extends ServiceImpl<WebConfigMapper, WebConfig> implements WebConfigService {

    @Override
    public SysWebConfigVO getWebConfig() {
        List<WebConfig> list = list();
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        // 获取第一个网站配置
        return BeanCopyUtil.copyBean(list.get(0), SysWebConfigVO.class);
    }

    @Override
    public boolean updateWebConfig(WebConfig webConfig) {
        WebConfig byId = getById(webConfig.getId());
        if (ObjUtil.isNull(byId)) {
            return false;
        }
        // 因为mybatis-plus默认更新策略是NOT_EMPTY，所以当要更新的字段为空值，不更新该字段
        // 但是有些数据我们要更新为空，所以可以改变全局更新策略，或者在实体类上添加策略，再是使用updateWrapper设置为空
        return updateById(webConfig);
    }

    @Override
    public SysWebConfigVO getBaseWebConfig() {
        LambdaQueryWrapper<WebConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                WebConfig::getLogo,
                WebConfig::getName,
                WebConfig::getSummary,
                WebConfig::getKeyword,
                WebConfig::getAuthor,
                WebConfig::getRecordNum,
                WebConfig::getWebUrl,
                WebConfig::getAliPay,
                WebConfig::getWeixinPay,
                WebConfig::getGithub,
                WebConfig::getGitee,
                WebConfig::getQqNumber,
                WebConfig::getEmail,
                WebConfig::getShowList,
                WebConfig::getLoginTypeList,
                WebConfig::getTouristAvatar,
                WebConfig::getBulletin,
                WebConfig::getAuthorInfo,
                WebConfig::getAuthorAvatar,
                WebConfig::getAboutMe,
                WebConfig::getIsMusicPlayer
        ).last(LIMIT_ONE);
        WebConfig webConfig = getOne(queryWrapper);
        if (ObjUtil.isNull(webConfig)) {
            return null;
        }
        return BeanCopyUtil.copyBean(webConfig, SysWebConfigVO.class);
    }
}

