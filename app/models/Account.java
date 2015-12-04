package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import Utils.AES;
import Utils.CommonUtil;

import play.db.jpa.Model;

@Entity
public class Account extends Model{
	
	@Column(name="account_type")
	public int accountType;
	
	public String email;
	
	public String password;
	
	@Column(name="facebook_id")
	public String facebookId;
	
	public String phone;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="register_datetime")
	public Date registerDateTime;
	
	@Column(name="access_token")
	public String accessToken;
	
	@Column(name="active_code")
	public int activeCode;
	
	@Column(name="active_expire_datetime")
	public Date activeExpireDatetime;
	
	@Column(name="is_active")
	public boolean isActive;
	
	@OneToOne(cascade=CascadeType.ALL, mappedBy="account") 
	public User user;
	
	public Account(int accountType, String email, String password, String phone, String facebookId){
		this.registerDateTime = new Date();
		this.activeCode = genernateActiveCode();
		
		switch(accountType){
		case 0:
			this.email = email;
			this.password = CommonUtil.md5(password);
			this.accountType = 0;
			this.accessToken = genernateAcessToken(email);
			this.activeExpireDatetime = addMoreMins(new Date(), 24*60);
			this.isActive = false;
			break;
		case 1:
			this.phone = phone;
			this.password = CommonUtil.md5(password);
			this.accountType = 1;
			this.accessToken = genernateAcessToken(phone);
			this.activeExpireDatetime = addMoreMins(new Date(), 2);
			this.isActive = false;
			break;
		case 2:
			this.facebookId = facebookId;
			this.accountType = 2;
			this.accessToken = genernateAcessToken(facebookId);
			this.activeExpireDatetime = addMoreMins(new Date(), 0);
			this.isActive = true;
			break;
		}
	}
	
	public Date addMoreMins(Date date, int expand){
		Calendar calendar = Calendar.getInstance();
	    calendar.add(Calendar.MINUTE, expand);
	    return calendar.getTime();
	}
	
	public int genernateActiveCode(){
		Random r = new Random();
		List<Integer> codes = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++){
		    int x = r.nextInt(9999);
		    while (codes.contains(x))
		        x = r.nextInt(9999);
		    codes.add(x);
		}
		return Integer.parseInt(String.format("%04d", codes.get(0)));
	}
	
	public String genernateAcessToken(String param){
		return AES.encrypt(
				CommonUtil.formatDateTime(this.registerDateTime)
				+ param, null).trim();
	}

}




















