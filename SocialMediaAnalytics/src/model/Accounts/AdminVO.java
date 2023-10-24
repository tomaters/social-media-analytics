package model.Accounts;

public class AdminVO extends MembersVO {
	
	private boolean adminLoggedIn;
	private String user_id = "Admin";
	private String user_pass  = "coolAdminPW";
	
	public AdminVO() {
		super();
	}
	
	public AdminVO(String user_id, String user_pass) {
		super(user_id, user_pass);
	}
	
	public boolean isAdminLoggedIn() {
		return adminLoggedIn;
	}

	public void setAdminLoggedIn(boolean adminLoggedIn) {
		this.adminLoggedIn = adminLoggedIn;
	}

	public String getUsername() {
		return user_id;
	}

	public String getPassword() {
		return user_pass;
	}
}
