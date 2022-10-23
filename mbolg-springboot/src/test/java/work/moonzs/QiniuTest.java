package work.moonzs;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.DownloadUrl;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import work.moonzs.service.IQiNiuService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Moondust月尘
 */
@SpringBootTest
public class QiniuTest {
    @Value("${oss.qiniu.domain}")
    private String DOMAIN_URL;
    @Value("${oss.qiniu.access-key}")
    private String ACCESS_KEY;
    @Value("${oss.qiniu.secret-key}")
    private String SECRET_KEY;
    @Value("${oss.qiniu.bucket}")
    private String BUCKET;

    @Autowired
    private IQiNiuService qiniuService;

    @Test
    public void test3() throws IOException {
        File file = new File("D:\\Java-workspace\\what.jpg");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(input));
        String s = qiniuService.uploadImage(multipartFile);
        System.out.println(s);
    }

    @Test
    public void test2() {
        // qiniuService.getFileInfo("what.jpg");
        // qiniuService.updateFileType("what.jpg", "gif");
        FileInfo[] fileInfos = qiniuService.listFileInfo("2022");
        for (FileInfo item : fileInfos) {
            System.out.println("=====");
            System.out.println(item.key);
            System.out.println(item.hash);
            System.out.println(item.fsize);
            System.out.println(item.mimeType);
            System.out.println(item.putTime);
            System.out.println(item.endUser);
        }
    }

    @Test
    public void test() throws UnsupportedEncodingException, QiniuException {
        String fileName = "what.jpg";
        String domainOfBucket = DOMAIN_URL;
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        String finalUrl = String.format("%s%s", domainOfBucket, encodedFileName);
        System.out.println(finalUrl);

        // domain   下载 domain, eg: qiniu.com【必须】
        // useHttps 是否使用 https【必须】
        // key      下载资源在七牛云存储的 key【必须】
        DownloadUrl url = new DownloadUrl("niu.moonzs.work", false, encodedFileName);
        // url.setAttname(attname) // 配置 attname
        //         .setFop(fop) // 配置 fop
        //         .setStyle(style, styleSeparator, styleParam) // 配置 style
        String urlString = url.buildURL();
        System.out.println(urlString);

        //带进度显示的文件下载
        long l = HttpUtil.downloadFile(urlString, FileUtil.file("D:\\Java-workspace"), new StreamProgress() {
            @Override
            public void start() {
                Console.log("开始下载。。。。");
            }

            @Override
            public void progress(long total, long progressSize) {
                Console.log("已下载：{}，总共：{}", FileUtil.readableFileSize(progressSize), FileUtil.readableFileSize(total));
            }

            @Override
            public void finish() {
                Console.log("下载完成！");
            }
        });
    }
}
