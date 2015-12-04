package models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import Utils.CommonUtil;

import play.db.jpa.Model;

@Entity
public class Channel extends Model{
	public String name;
	public String image;
	public int code;
	
	@OneToMany(mappedBy = "channel")
	public Set<Resource> resources;
	
	public static void createChannelsByJson(String json){
		if(CommonUtil.isBlank(json)){
			throw new RuntimeException("Country data is required.");
		}
		
		LinkedHashMap<String, Class> clzMap = new LinkedHashMap<String, Class>();
		clzMap.put("values", Channel.class);
		
		List<Channel> channels = CommonUtil.parseArray(json, clzMap);
		
		for(Channel channel : channels){
			channel.save();
		}
	}
}



















