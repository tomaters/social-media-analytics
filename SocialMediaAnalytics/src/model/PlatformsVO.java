package model;

import java.util.Objects;

public class PlatformsVO {
	
	private int platform_id;
	private String platform_name; // name of platform (YouTube/TikTok/Instagram/Facebook); primary key
	private String user_id; // foreign key from users
	private int subscribers; // total number of subscribers (necessary)
	private int views; // total number of views (not necessary)
	private int likes; // total number of likes (not necessary)
	private int comments; // total number of comments (not necessary)
	private int engagement; // views + likes + comments
	private int income; // income from platform (necessary)
	
	public PlatformsVO(){
		super();
	}
	
	public PlatformsVO(String platform_name, String user_id, int subscribers, int views, int likes, int comments, int engagement, int income) {
		super();
		this.platform_name = platform_name;
		this.user_id = user_id;
		this.subscribers = subscribers;
		this.views = views;
		this.likes = likes;
		this.comments = comments;
		this.engagement = engagement;
		this.income = income;
	}

	public int getPlatform_id() {
		return platform_id;
	}

	public void setPlatform_id(int platform_id) {
		this.platform_id = platform_id;
	}

	public String getPlatform_name() {
		return platform_name;
	}

	public void setPlatform_name(String platform_name) {
		this.platform_name = platform_name;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(int subscribers) {
		this.subscribers = subscribers;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public int getEngagement() {
		return engagement;
	}

	public void setEngagement(int engagement) {
		this.engagement = engagement;
	}

	public int getIncome() {
		return income;
	}

	public void setIncome(int income) {
		this.income = income;
	}
	
	@Override
	public String toString() {
		return "Platform: [platform name = " + platform_name + ", username = " + user_id + ", subscribers = " + subscribers + ", views = " + views + 
			", likes = " + likes + ", comments = " + comments + "engagement : " + engagement + ", income : " + income + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.platform_name, this.user_id, this.subscribers, this.views, this.likes, this.comments, this.engagement, this.income);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof PlatformsVO)) {
			return false;
		}
		PlatformsVO platformsVO = (PlatformsVO)obj;
		return this.platform_name == platformsVO.platform_name && this.user_id == platformsVO.user_id && this.subscribers == platformsVO.subscribers && 
			this.views == platformsVO.views && this.likes == platformsVO.likes && this.comments == platformsVO.comments && this.engagement == 
			platformsVO.engagement && this.income == platformsVO.income;
	}

}
