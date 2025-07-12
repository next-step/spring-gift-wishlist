package gift.entity;

import gift.shared.domain.UserRole;

import java.util.ArrayList;
import java.util.List;

public class User {
    private Long id;
    private String email;
    private String password;

    private List<Gift> wishList = new ArrayList<>();

    private UserRole role = UserRole.NORMAL;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User() {}

    public Long getId(){
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPasswordMatched(String password){
        return this.password.equals(password);
    }

    public void addWishList(Gift gift){
        wishList.add(gift);
    }
}
