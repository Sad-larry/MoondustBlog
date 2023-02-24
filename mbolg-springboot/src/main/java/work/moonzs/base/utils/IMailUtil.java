package work.moonzs.base.utils;

import cn.hutool.core.lang.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.util.DateUtils;
import work.moonzs.domain.entity.MailEntity;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Locale;

/**
 * 邮件工具类
 *
 * @author Moondust月尘
 */
@Component
@Slf4j
public class IMailUtil {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sendFrom;
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 发送简单邮件
     *
     * @param mailEntity 邮件实体
     */
    public void sendSimpleMail(MailEntity mailEntity) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            // 设置发送邮件账号
            simpleMailMessage.setFrom(sendFrom);
            // 设置接收邮件的人，可以多个
            simpleMailMessage.setTo(mailEntity.getSendTo());
            // 设置发送邮件的主题
            simpleMailMessage.setSubject(mailEntity.getSubject());
            // 设置发送邮件的内容
            simpleMailMessage.setText(mailEntity.getText());
            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            log.error("邮件发送失败!");
        }
    }

    /**
     * 发送html电子邮件
     *
     * @param mailEntity 邮件实体
     */
    public void sendHtmlEMail(MailEntity mailEntity) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper minehelper = new MimeMessageHelper(message, true);
            // 设置发送邮件账号
            minehelper.setFrom(sendFrom);
            // 设置接收邮件的人，可以多个
            minehelper.setTo(mailEntity.getSendTo());
            // 设置发送邮件的主题
            minehelper.setSubject(mailEntity.getSubject());
            // 设置发送邮件的内容 第二个设置为true则可以发送带HTML的邮件
            minehelper.setText(mailEntity.getText(), true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            log.error("邮件发送失败!");
        }
    }

    /**
     * 获得博客注册验证码模板
     *
     * @param email   用户邮箱
     * @param code    验证码
     * @param timeout 超时时间
     * @return {@link String}
     */
    public String getRegisterMailCode(String email, String code, int timeout) {
        Context context = new Context();
        //设置模板所需的参数
        context.setVariable("title", "注册验证码");
        context.setVariable("email", email);
        context.setVariable("code", code);
        context.setVariable("timeout", timeout);
        context.setVariable("date", DateUtils.format(new Date(), "yyyy-MM-dd hh:mm:ss", Locale.CHINESE));
        //通过模板类将动态参数传入HTML模板,并返回模板内容 参数一:模板名字，参数二：动态参数Web文本
        return templateEngine.process("/RegisterMailCode", context);
    }

    /**
     * 验证是否为可用邮箱地址
     *
     * @param email 电子邮件
     * @return boolean
     */
    public boolean verifyMail(String email) {
        return Validator.isEmail(email);
    }
}
