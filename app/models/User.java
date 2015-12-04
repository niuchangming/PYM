package models;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import Utils.CommonUtil;

import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class User extends Model{
	
	@OneToOne
	public Account account;
	
	@Column(name="first_name")
	public String firstName;
	
	@Column(name="last_name")
	public String lastName;
	
	@Column(name="whats_up")
	@Lob
	public String whatsUp;
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date birthday;
	
	public Blob avatar;
	
	public String email;
	
	public String phone;
	
	public int role;
	
	public int gender;
	
	@OneToOne(cascade=CascadeType.ALL, mappedBy="user") 
	public Address address;
	
	public int occupation;
	
	@Column(name="pym_dollars")
	public int pymDollars;
	
	public int points;
	
	@OneToMany(mappedBy = "user")
	public Set<Resource> resources;
	
	@OneToMany(mappedBy = "user")
	public Set<Comment> comments;
	
	@ManyToMany
	@JoinTable(name="Follower", joinColumns=@JoinColumn(name="user_id"), inverseJoinColumns=@JoinColumn(name="follower_id"))
	public Set<User> followers = new HashSet<User>();

	@ManyToMany(mappedBy="followers", fetch=FetchType.LAZY)
	public Set<User> followings = new HashSet<User>();
	
	public User(Account account){
		this.account = account;
		this.email = account.email;
		this.phone = account.phone;
		this.role = 0;
		this.pymDollars = 0;
		this.points = 0;
	}
	
	public void updateUser(String firstName, String lastName, long birthday, String email, 
			String phone, String whatsUp, int role, int gender, int occupation){
		
		if(!CommonUtil.isBlank(firstName)){
			this.firstName = firstName;
		}
		
		if(!CommonUtil.isBlank(lastName)){
			this.lastName = lastName;
		}
		
		if(birthday != 0){
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(birthday);
			this.birthday= calendar.getTime();
		}
		
		if(!CommonUtil.isBlank(email)){
			this.email= email;
		}
		
		if(!CommonUtil.isBlank(whatsUp)){
			this.whatsUp= whatsUp;
		}else{
			this.whatsUp= "";
		}
		
		if(!CommonUtil.isBlank(phone)){
			this.phone= phone;
		}
		
		if(this.role != role){
			this.role = role;
		}
		
		if(this.gender != gender){
			this.gender = gender;
		}
		
		if(this.occupation != occupation){
			this.occupation = occupation;
		}
	}
	
	public void updateAddress(Address address){
		if(this.address != null){
			this.address.address1 = address.address1;
			this.address.address2 = address.address2;
			this.address.city = address.city;
			this.address.state = address.state;
			this.address.country = address.country;
			this.address.save();
		}else{
			address.user = this;
			address.save();
		}
	}
	
}




