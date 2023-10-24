package model;

import java.util.Objects;

public class AccountAnalyticsVO {
	
	private int analytics_id; // serial number; increments by 1; primary key
	private String user_id; // foreign key from users
	private int total_subscribers; // total subscribers from all platforms
	private int total_engagement; // total engagement (add views/likes/comments from all platforms)
	private int total_income; // total income from all platforms
	
	public AccountAnalyticsVO(){
		super();
	}
	
	public AccountAnalyticsVO(int analytics_id, String user_id, int total_subscribers, int total_engagement, int total_income) {
		super();
		this.analytics_id = analytics_id;
		this.user_id = user_id;
		this.total_subscribers = total_subscribers;
		this.total_engagement = total_engagement;
		this.total_income = total_income;
	}

	public int getAnalytics_id() {
		return analytics_id;
	}

	public void setAnalytics_id(int analytics_id) {
		this.analytics_id = analytics_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getTotal_subscribers() {
		return total_subscribers;
	}

	public void setTotal_subscribers(int total_subscribers) {
		this.total_subscribers = total_subscribers;
	}

	public int getTotal_engagement() {
		return total_engagement;
	}

	public void setTotal_engagement(int total_engagement) {
		this.total_engagement = total_engagement;
	}

	public int getTotal_income() {
		return total_income;
	}

	public void setTotal_income(int total_income) {
		this.total_income = total_income;
	}
	
	@Override
	public String toString() {
		return "Account Analytics: [analytics ID = " + analytics_id + ", username = " + user_id + ", total subscribers = " + total_subscribers + ", total engagement = " + 
		total_engagement + ", total income = " + total_income + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.analytics_id, this.user_id, this.total_subscribers, this.total_engagement, this.total_income);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof AccountAnalyticsVO)) {
			return false;
		}
		AccountAnalyticsVO accountAnalyticsVO = (AccountAnalyticsVO)obj;
		return this.analytics_id == accountAnalyticsVO.analytics_id && this.user_id == accountAnalyticsVO.user_id && this.total_subscribers == accountAnalyticsVO.total_subscribers 
				&& this.total_engagement == accountAnalyticsVO.total_engagement && this.total_income == accountAnalyticsVO.total_income;
	}
	
}
