package gift.domain.member;

public class Member {
    private Long id;
    private String email;
    private String password;
    private String name;
    private RoleType role;

    protected Member(){}

    public Member(String email, String password, String name){
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = RoleType.USER;
    }

    public Long getId(){
        return id;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public String getName(){
        return name;
    }
    public String getRole(){
        return role.toString();
    }

    public boolean verifyPassword(String password){
        return this.password.equals(password);
    }

    public void update(String email, String password, String name){
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
