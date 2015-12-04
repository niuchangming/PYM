package controllers.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;
import Utils.CommonUtil;
import Utils.EmailSender;
import models.Account;
import models.User;
import play.Play;
import play.mvc.Controller;
import models.Error;

public class AccountController extends Controller{
	
	public static void signup(int accountType, String email, String password, String phone, String facebookId){
		if(!notExists(email, phone)){
			renderJSON(new Error("Account already exists."));
		}
		
		final Account account = new Account(accountType, email, password, phone, facebookId);
		account.save();
		
		if(account.accountType == 0){
			String activeLink = Play.configuration.getProperty("application.baseUrl") 
					+ "api/AccountController/activeAccountByEmail?accessToken=" + account.accessToken;
			EmailSender.generateAndSendEmail(account.email, activeLink);
		}else if(account.accountType == 1){
			new Thread(new  Runnable() {
				
				@Override
				public void run() {
					CommonUtil.sendSMS(account.phone, "Your active code is: " + account.activeCode);
				}
				
			}).start();
		}
		
		renderJSON(CommonUtil.toJson(account, 
				"*.class",
				"*.id",
				"*.persistent",
				"password",
				"activeCode",
				"activeExpireDatetime",
				"user"));
	}
	
	public static void login(int accountType, String email, String password, String phone, String facebookId){
		Account account = null;
		switch(accountType){
		case 0:
			account = Account.find("byEmailAndPassword", email, CommonUtil.md5(password)).first();
			break;
		case 1:
			account = Account.find("byPhoneAndPassword", phone, CommonUtil.md5(password)).first();
			break;
		case 2:
			account = Account.find("byFacebookId", facebookId).first();
			break;
		}
		
		if(account == null){
			renderJSON(new Error("The account cannot be found."));
		}else{
			renderJSON(CommonUtil.toJson(account, 
					"*.class",
					"*.id",
					"*.persistent",
					"activeCode",
					"activeExpireDatetime",
					"password",
					"user"));
		}
	}
	
	public static void activeAccountByPhone(String accessToken, int code){
		Account account = Account.find("access_token = ?", accessToken).first();
		if(account == null){
			renderJSON(new Error("The account cannot be found."));
		}
		
		if(account.isActive){
			renderJSON(new Error("Your account is already active."));
		}
		
		if(account.activeCode == code && account.activeExpireDatetime.after(new Date())){
			account.isActive = true;
			account.save();
			renderJSON(CommonUtil.toJson(account, 
					"*.class",
					"*.id",
					"*.persistent",
					"activeCode",
					"activeExpireDatetime",
					"password",
					"user"));
		}else{
			renderJSON(new Error("Active code is incorrect or expired."));
		}
	}
	
	public static void activeAccountByEmail(String accessToken){
		Account account = Account.find("access_token = ?", accessToken).first();
		if(account == null){
			renderJSON(new Error("The account cannot be found."));
		}
		
		if(account.isActive){
			renderJSON(new Error("Your account is already active."));
		}
		
		if(account.activeExpireDatetime.after(new Date())){
			account.isActive = true;
			account.save();
			renderText("Your account is active");
		}else{
			renderJSON(new Error("Active code is incorrect or expired."));
		}
	}
	
	public static void smsActiveCode(String accessToken){
		final Account account = Account.find("access_token = ?", accessToken).first();
		if(account == null){
			renderJSON(new Error("The account cannot be found."));
		}
		
		if(account.isActive){
			renderJSON(new Error("Your account is already active."));
		}
		
		account.activeCode = account.genernateActiveCode();
		account.activeExpireDatetime = account.addMoreMins(new Date(), 5);
		account.save();
		
		if(account.accountType == 1){
			new Thread(new  Runnable() {
				
				@Override
				public void run() {
					CommonUtil.sendSMS(account.phone, "Your active code is: " + account.activeCode);
				}
			}).start();
		}
		
		renderText("Sent");
	}
	
	public static void loginWithFacebook(String facebookId){
		if(CommonUtil.isBlank(facebookId))
			renderJSON(new Error("Facebook ID is empty."));
		
		Account account = Account.find("byFacebookId", facebookId).first();
		if(account == null){
			account = new Account(2, null, null, null, facebookId);
			account.save();
		}
		
		renderJSON(CommonUtil.toJson(account, 
				"*.class",
				"*.id",
				"*.persistent",
				"password",
				"activeCode",
				"activeExpireDatetime",
				"user"));
	}
	
	public static void resetPassword(String accessToken, String oldPassword, String newPassword){
		Account account = Account.find("access_token = ?", accessToken).first();
		if(account == null){
			renderJSON(new Error("The account cannot be found."));
		}
		
		if(!account.password.equals(CommonUtil.md5(oldPassword))){
			renderJSON(new Error("The old password is incorrect."));
		}
		
		account.password = CommonUtil.md5(newPassword);
		account.save();
		
		renderJSON(CommonUtil.toJson(account, 
				"*.class",
				"*.id",
				"*.persistent",
				"password",
				"activeCode",
				"activeExpireDatetime",
				"user"));
	}
	
	public static void requestPassword(String email, String phone){
		Account account = null;
		
		if(!CommonUtil.isBlank(phone)){
			account = Account.find("phone = ?", phone).first();
		}else if(!CommonUtil.isBlank(email)){
			account = Account.find("email = ?", email).first();
		}
		
		if(account == null){
			renderJSON(new Error("The account cannot be found."));
		}
		
		String newPassword = CommonUtil.generateSessionKey(6);
		account.password = CommonUtil.md5(newPassword);
		account.save();
		
		if(account.accountType == 0){
			EmailSender.SendPasswordEmail(account.email, newPassword);
		}else if(account.accountType == 1){
			CommonUtil.sendSMS(phone, "Your new password is: " + newPassword);
		}
		
		renderJSON("{\"message\":\"success\"}");
	}
	
	public static boolean notExists(String email, String phone){
		Account account = null;
		if(CommonUtil.isBlank(phone)){
			account = Account.find("byEmail", email).first();
		}else{
			account = Account.find("byPhone", phone).first();
		}
		
		if (account != null)
			return false;

		return true;
	}

}
















