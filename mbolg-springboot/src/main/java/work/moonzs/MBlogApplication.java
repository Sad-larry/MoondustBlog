package work.moonzs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import work.moonzs.base.annotation.EnableSystemLog;

/**
 * @author Moondust月尘
 * 博客项目启动类，包括前台和后台服务
 */
// @EnableSystemLog
@SpringBootApplication
public class MBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(MBlogApplication.class, args);
    }
}
