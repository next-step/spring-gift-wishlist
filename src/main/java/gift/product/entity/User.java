package gift.product.entity;


public class User {

	private final Long id;

	private String email;

	private String password;

	private String nickName;


	public User(Long id, String email, String password, String nickName) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.nickName = nickName;
	}


	public User(String email, String password, String nickName) {
		this(null, email, password, nickName);
	}


	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getNickName() {
		return nickName;
	}


	public void setName(String name) {
		this.nickName = nickName;
	}

}
