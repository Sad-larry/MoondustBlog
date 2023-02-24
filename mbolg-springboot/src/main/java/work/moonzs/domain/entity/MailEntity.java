package work.moonzs.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailEntity {
    /**
     * 发送邮件的人
     */
    private String sendFrom;
    /**
     * 接收邮件的人
     */
    private String sendTo;
    /**
     * 发送邮件的主题
     */
    private String subject;
    /**
     * 发送邮件的内容
     */
    private String text;
    /**
     * 发送日期
     */
    private Date sentDate;
}
