package work.moonzs.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author Moondust月尘
 */
@Configuration
public class DataSourceConfig {
    /**
     * 主要数据源
     *
     * @return {@link DataSource}
     */
    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.druid")
    public DataSource primaryDataSource() {
        return DruidDataSourceBuilder.create().build();
    }
}
