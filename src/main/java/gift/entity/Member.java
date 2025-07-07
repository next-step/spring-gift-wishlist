package gift.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class Member implements UserDetails {

    private Long identifyNumber;
    private String email;
    private String password;
    private String authority;

    public Member() {}

    public Member(Long identifyNumber, String email, String password, String authority) {
        this.identifyNumber = identifyNumber;
        this.email = email;
        this.password = password;
        this.authority = authority;
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.authority = "ROLE_USER";
    }

    public Member updateIdentifyNumber(Long identifyNumber) {
        return new Member(identifyNumber, email, password, authority);
    }

    public Long getIdentifyNumber() {
        return identifyNumber;
    }

    public Member updateEmail(String email) {
        return new Member(identifyNumber, email, password, authority);
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(authority));
    }

    public Member updatePassword(String password) {
        return new Member(identifyNumber, email, password, authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return true;
    }
}
