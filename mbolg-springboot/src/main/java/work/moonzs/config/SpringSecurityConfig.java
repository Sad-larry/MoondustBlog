package work.moonzs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import work.moonzs.base.filter.JwtAuthenticationTokenFilter;
import work.moonzs.base.handler.AccessDeniedHandlerImpl;
import work.moonzs.base.handler.AuthenticationEntryPointImpl;

import java.util.List;

/**
 * @author Moondust月尘
 */
@Configuration
@EnableMethodSecurity
public class SpringSecurityConfig {
    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;
    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;
    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;

    /**
     * 过滤器链配置
     *
     * @param httpSecurity http安全性
     * @return {@link SecurityFilterChain}
     * @throws Exception 异常
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // 关闭csrf
        httpSecurity.csrf().disable();
        // 使用了jwt就不通过session获取SecurityContext
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 登录接口可以匿名访问 其他接口需要认证
        httpSecurity.authorizeRequests().antMatchers("/system/login", "/system/captchaImage", "/druid/**").anonymous()
                .antMatchers("/web/**").permitAll()
                .antMatchers("/swagger**/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v2/**").permitAll()
                .anyRequest().authenticated();
        // 添加过滤器
        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.exceptionHandling()
                // 认证失败处理器
                .authenticationEntryPoint(authenticationEntryPoint)
                // 授权失败处理器
                .accessDeniedHandler(accessDeniedHandler);
        // 默认logout取消
        httpSecurity.logout().disable();
        // 允许跨域
        httpSecurity.cors().configurationSource(corsConfigurationSource());
        // x-frame-options禁止，允许内嵌IFrame
        httpSecurity.headers().frameOptions().disable();
        return httpSecurity.build();
    }

    /**
     * 使用BCrypt密码编码器 用户注册加密密码 登录则校验加密后的密码
     *
     * @return {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * authenticationConfiguration() + authenticationManagerBean()
     * 将AuthenticationManager注入到容器内给BlogLoginServiceImpl使用
     *
     * @return AuthenticationConfiguration
     */
    @Bean
    public AuthenticationConfiguration authenticationConfiguration() {
        return new AuthenticationConfiguration();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationConfiguration().getAuthenticationManager();
    }

    /**
     * 使用SpringSecurity的跨域处理
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setMaxAge(3600L);
        corsConfiguration.setAllowedOrigins(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
