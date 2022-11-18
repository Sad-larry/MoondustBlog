package work.moonzs.base.quartz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Moondust月尘
 */
@Component("blogQuartz")
@Slf4j
public class BlogQuartz {
    public void updateReadQuantity() {
        log.info("time : {}", new Date());
    }
}
