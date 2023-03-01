package work.moonzs;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Moondust月尘
 * 博客项目启动类，包括前台和后台服务
 */
// @EnableSystemLog
@SpringBootApplication
@EnableEncryptableProperties
public class MBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(MBlogApplication.class, args);
    }
}
// 我突然发现，对于一个个人博客来说，前端页面为何需要用户登录呢，用户登录可以产生什么样的效果，给用户可以提供怎样的价值呢
// 没有，用户登录功能好像就是为了实现而去实现，这个功能仅仅只是为了告诉用户，你可以登录我的博客，但是你依旧做不了什么，未登录的
// 依旧可以做你登录的事，那么，为何要去实现这个功能呢？大费周章的搞个邮箱验证码登录，然后前端一直在修改UI，劳心劳肺
// 所以我打算，注册放到后台注册，但是相对应的，做一个权限管理系统，不同用户权限只能做相应的事，前端依旧可以登录，但是登录能干什么呢
// 这个还是得先想想