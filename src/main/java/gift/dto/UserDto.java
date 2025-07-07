package gift.dto;

import gift.entity.User;

public class UserDto {

    private Long id;

    private String email;

    private String password;

    public UserDto(User user) {
    }

    public Long getId() {return id;}

    public String getEmail() {return email;}

    public String getPassword() {return password;}

    public void setId(Long id) {this.id = id;}
}
