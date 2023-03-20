package work.moonzs.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import work.moonzs.base.enums.StatusConstants;

import java.util.Collection;
import java.util.Set;

/**
 * 登录用户实体类
 *
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// @JsonIncludeProperties({"userId", "user", "loginTime", "expireTime", "userUid", "role", "permissions"})
@JsonIgnoreProperties({"enabled", "accountNonExpired", "password", "username", "accountNonLocked", "credentialsNonExpired", "authorities"})
public class LoginUser implements UserDetails {
    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 用户信息
     */
    private User user;
    /**
     * 登录时间
     */
    private Long loginTime;
    /**
     * 过期时间
     */
    private Long expireTime;
    /**
     * 用户唯一标识
     */
    private String userUid;
    /**
     * 用户角色
     */
    private String role;
    /**
     * 用户权限
     */
    private Set<String> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Set<GrantedAuthority> authorities = new HashSet<>();
        // for (String role : roles) {
        //     authorities.add(new SimpleGrantedAuthority(role));
        // }
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return StatusConstants.NORMAL.equals(user.getStatus());
    }
}
