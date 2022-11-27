package work.moonzs.service;

import java.util.HashMap;

/**
 * 网站服务接口
 *
 * @author Moondust月尘
 * @since 2022-11-27 11:03:30
 */
public interface HomeService {
    /**
     * 获取网站信息
     *
     * @return {@link HashMap}<{@link String}, {@link Object}>
     */
    HashMap<String, Object> getBlogInfo();
}
