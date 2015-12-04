package controllers.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Utils.CommonUtil;

import play.Play;
import play.db.jpa.Blob;
import play.mvc.Controller;

import models.Account;
import models.Address;
import models.User;
import models.Error;

public class UserController extends Controller{
	
	public static void updateUser(String accessToken, 
			String firstName, 
			String lastName,
			long birthday,
			String email,
			String phone,
			String whatsUp,
			int role,
			int gender,
			int occupation){
		
		Account account = Account.find("access_token = ?", accessToken).first();
		
		if(account == null){
			renderJSON(new Error("Account cannot be found."));
		}
		
		User user = account.user;
		
		if(user == null){
			user = new User(account);
		}
		
		user.updateUser(firstName, lastName, birthday, email, phone, whatsUp, role, gender, occupation);
		user.save();
		
		renderJSON(CommonUtil.toJson(user, 
				"*.class",
				"*.id",
				"*.persistent",
				"comments",
				"account",
				"address",
				"avatar.file",
				"avatar.image",
				"avatar.store",
				"resources",
				"followers",
				"followings"));
	} 
	
	public static void updateAddress(String accessToken, Address address){
		Account account = Account.find("access_token = ?", accessToken).first();
		
		if(account == null){
			renderJSON(new Error("Account cannot be found."));
		}
		
		User user = account.user;
		
		if(user == null){
			user = new User(account);
			user.save();
		}
		
		if(address != null){
			user.updateAddress(address);
			user.save();
			
			renderJSON(CommonUtil.toJson(address, 
					"*.class",
					"*.id",
					"*.persistent",
					"user"));
		}else{
			renderJSON(new Error("Address is empty."));
		}
	}
	
	public static void updateAvatar(String accessToken, Blob photo){
		Account account = Account.find("access_token = ?", accessToken).first();
		
		if(account == null){
			renderJSON(new Error("Account cannot be found."));
		}
		
		User user = account.user;
		
		if(user == null){
			user = new User(account);
		}
		
		user.avatar = photo;
		user.save();

		renderJSON(CommonUtil.toJson(user, 
				"*.class",
				"*.id",
				"*.persistent",
				"comments",
				"account",
				"address",
				"avatar.file",
				"avatar.image",
				"avatar.store",
				"resources",
				"followers",
				"followings"));
	}

	public static void getUser(String accessToken){
		Account account = Account.find("access_token = ?", accessToken).first();
		if(account == null){
			renderJSON(new Error("Account cannot be found."));
		}
		
		if(account.user == null){
			renderJSON(new Error("User is empty"));
		}
		
		User user = account.user;
		
		renderJSON(CommonUtil.toJson(user, 
				"*.class",
				"*.id",
				"*.persistent",
				"comments",
				"account",
				"address",
				"avatar.file",
				"avatar.image",
				"avatar.store",
				"resources",
				"followers",
				"followings"));
	}
	
	public static void getAddress(String accessToken){
		Account account = Account.find("access_token = ?", accessToken).first();
		if(account == null){
			renderJSON(new Error("Account cannot be found."));
		}
		
		Address address = null;
		if(account.user != null){
			address = account.user.address;
		}
		
		renderJSON(CommonUtil.toJson(address, 
				"*.class",
				"*.id",
				"*.persistent",
				"user"));
	}
	
	public void followUser(String accessToken, long userId){
		Account account = Account.find("access_token = ?", accessToken).first();
		if(account == null){
			renderJSON(new Error("Account cannot be found."));
		}
		
		User follower = User.findById(userId);
		if(follower == null){
			renderJSON(new Error("Following cannot be found."));
		}
		
		User user = account.user;
		follower.followings.add(user);
		follower.save();
		
		renderJSON(CommonUtil.toJson(follower, "*.class",
				"*.id",
				"*.persistent",
				"comments",
				"account",
				"address",
				"avatar",
				"resources",
				"followers",
				"followers.account",
				"followers.comments",
				"followers.avatar.file",
				"followers.avatar.image",
				"followers.avatar.store",
				"followers.address",
				"followers.resources",
				"followers.followers",
				"followers.followings"));
	}
	
}














