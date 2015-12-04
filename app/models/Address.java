package models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class Address extends Model{
	
	@OneToOne
	public User user;
	
	public String address1;
	
	public String address2;
	
	public String city;
	
	public String state;
	
	public String country;
	
	public String zip;
}
