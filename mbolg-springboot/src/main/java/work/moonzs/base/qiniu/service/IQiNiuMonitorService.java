package work.moonzs.base.qiniu.service;

import com.qiniu.cdn.CdnResult;

/**
 * 七牛云CDN相关功能接口
 *
 * @author Moondust月尘
 */
public interface IQiNiuMonitorService {
    /**
     * 刷新文件
     *
     * @return {@link CdnResult.RefreshResult}
     */
    CdnResult.RefreshResult refreshFile();

    /**
     * 刷新目录
     * 因为刷新目录权限需要联系技术支持开通，所以目录需要查看里面的所有文件，并执行刷新文件方法
     *
     * @return {@link CdnResult.RefreshResult}
     */
    CdnResult.RefreshResult refreshDir();

    /**
     * 得到更新结果
     * 文档中将刷新结果放在一个html文件中，那么这里可以返回html的url
     *
     * @return {@link String}
     */
    String getRefreshResult();

    /**
     * 预取文件，新资源提前由 CDN 拉取到 CDN 缓存节点，提高用户访问速度
     *
     * @param urls url
     * @return {@link CdnResult.PrefetchResult}
     */
    CdnResult.PrefetchResult prefetchingFile(String... urls);

    /**
     * 获取域名流量
     *
     * @param fromDate    从日期
     * @param toDate      到目前为止
     * @param granularity 粒度，支持的取值为 5min ／ hour ／day
     * @return {@link CdnResult.FluxResult}
     */
    CdnResult.FluxResult obtainingDomainTraffic(String fromDate, String toDate, String granularity);

    /**
     * 获得域名带宽
     *
     * @param fromDate    从日期
     * @param toDate      到目前为止
     * @param granularity 粒度，支持的取值为 5min ／ hour ／day
     * @return {@link CdnResult.BandwidthResult}
     */
    CdnResult.BandwidthResult obtainingDomainBandwidth(String fromDate, String toDate, String granularity);

    /**
     * 获取日志下载链接
     *
     * @param logDate 日志日期
     * @return {@link CdnResult.LogListResult}
     */
    CdnResult.LogListResult obtainLogLink(String logDate);
}
