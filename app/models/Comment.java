package models;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Comment extends Model{
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User user;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "resource_id")
	public Resource resource;
	
	public String content;
	
	public Date commentDatetime;

	public Comment(User user, Resource resource, String content) {
		super();
		this.user = user;
		this.resource = resource;
		this.content = content;
		this.commentDatetime = new Date();
	}
}
