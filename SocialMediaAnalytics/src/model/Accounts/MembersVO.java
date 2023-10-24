package model.Accounts;

import java.util.Objects;

public class MembersVO {
	
	private boolean isLoggedIn; // check logged in status; not in SQL
	private String user_name; // name, not username (necessary)
	private String user_id; // username; primary key
	private String user_pass; // password (necessary)
	private String user_email; // email (necessary)
	
	public MembersVO() {
		super();
	}
	
	public MembersVO(String user_id, String user_pass) { // username/password constructor for login
		this.user_id = user_id;
		this.user_pass = user_pass;
	}
	
	public MembersVO(boolean isLoggedIn, String user_name, String user_id, String user_pass, String user_email) {
		super();
		this.isLoggedIn = isLoggedIn;
		this.user_name = user_name;
		this.user_id = user_id;
		this.user_pass = user_pass;
		this.user_email = user_email;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public String getName() {
		return user_name;
	}

	public void setName(String user_name) {
		this.user_name = user_name;
	}

	public String getUsername() {
		return user_id;
	}

	public void setUsername(String user_id) {
		this.user_id = user_id;
	}

	public String getPassword() {
		return user_pass;
	}

	public void setPassword(String user_pass) {
		this.user_pass = user_pass;
	}

	public String getEmail() {
		return user_email;
	}

	public void setEmail(String user_email) {
		this.user_email = user_email;
	}
	
	@Override
	public String toString() {
		return "User: [name = " + user_name + ", username = " + user_id + ", password = " + user_pass + ", email = " + user_email + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.user_name, this.user_id, this.user_pass, this.user_email);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof MembersVO)) {
			return false;
		}
		MembersVO membersVO = (MembersVO)obj;
		return this.user_name == membersVO.user_name && this.user_id == membersVO.user_id &&
			this.user_pass == membersVO.user_pass && this.user_email == membersVO.user_email;
	}
}
