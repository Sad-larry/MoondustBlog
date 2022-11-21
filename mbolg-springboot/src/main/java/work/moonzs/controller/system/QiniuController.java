package work.moonzs.controller.system;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.moonzs.base.qiniu.service.QiniuService;

/**
 * @author Moondust月尘
 */
@RestController("SystemQiniuC")
@RequestMapping("/system/qiniu")
@RequiredArgsConstructor
public class QiniuController {
    /**
     * 七牛云的存储空间，一般我都是存在一个空间里面，如果多个存储空间，就需要手动传参
     */
    @Value("${oss.qiniu.bucket}")
    private String BUCKET;
    
    private final QiniuService qiniuService;

}
