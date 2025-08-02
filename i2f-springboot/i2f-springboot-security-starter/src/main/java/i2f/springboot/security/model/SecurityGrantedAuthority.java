package i2f.springboot.security.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author Ice2Faith
 * @date 2022/4/15 16:16
 * @desc
 */
@Data
@NoArgsConstructor
public class SecurityGrantedAuthority implements GrantedAuthority, Comparable<GrantedAuthority> {
    private String authority;

    public SecurityGrantedAuthority(SimpleGrantedAuthority authority) {
        this(authority.getAuthority());
    }

    public SecurityGrantedAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public int compareTo(GrantedAuthority o) {
        return this.getAuthority().compareTo(o.getAuthority());
    }
}
