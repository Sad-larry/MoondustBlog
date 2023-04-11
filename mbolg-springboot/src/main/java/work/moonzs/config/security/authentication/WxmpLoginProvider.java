package work.moonzs.config.security.authentication;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.Resource;

/**
 * 自定义微信小程序登录身份验证处理器
 *
 * @author Moondust月尘
 * @date 2023/03/02
 */
@Slf4j
public class WxmpLoginProvider implements AuthenticationProvider {
    protected final Log logger = LogFactory.getLog(this.getClass());
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private final UserDetailsChecker authenticationChecks = new WxmpLoginProvider.DefaultAuthenticationChecks();
    // 需要注入的用户信息查询服务
    @Resource(name = "UserDetailsServiceImplForWxmpLogin")
    private UserDetailsService userDetailsService;

    public WxmpLoginProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * 身份验证主逻辑
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof WxmpLoginAuthenticationToken wxmpLoginAuthenticationToken)) {
            return null;
        } else {
            // 判断登录方式是否为 微信小程序 登录
            UserDetails userDetails = userDetailsService.loadUserByUsername((String) authentication.getPrincipal());
            if (userDetails == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            // 身份检验，判断是否锁定、过期等
            this.authenticationChecks.check(userDetails);
            return this.createSuccessAuthentication(userDetails, authentication);
        }
    }

    private Authentication createSuccessAuthentication(UserDetails userDetails, Authentication authentication) {
        WxmpLoginAuthenticationToken result = new WxmpLoginAuthenticationToken(userDetails, null);
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        //第二步拦截封装了WxLoginAuthenticationToken，此处校验，如果是该类型，则在该处理器做登录校验
        return WxmpLoginAuthenticationToken.class.isAssignableFrom(aClass);
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * 默认身份验证检查
     *
     * @author Moondust月尘
     * @date 2023/03/02
     */
    private class DefaultAuthenticationChecks implements UserDetailsChecker {
        private DefaultAuthenticationChecks() {
        }

        @Override
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                WxmpLoginProvider.this.logger.debug("Failed to authenticate since user account is locked");
                throw new LockedException(WxmpLoginProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked", "User account is locked"));
            } else if (!user.isEnabled()) {
                WxmpLoginProvider.this.logger.debug("Failed to authenticate since user account is disabled");
                throw new DisabledException(WxmpLoginProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"));
            } else if (!user.isAccountNonExpired()) {
                WxmpLoginProvider.this.logger.debug("Failed to authenticate since user account has expired");
                throw new AccountExpiredException(WxmpLoginProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.expired", "User account has expired"));
            }
        }
    }
}
