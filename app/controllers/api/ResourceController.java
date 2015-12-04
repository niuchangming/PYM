package controllers.api;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import flexjson.JSONSerializer;

import Utils.CommonUtil;
import models.Account;
import models.Channel;
import models.Comment;
import models.Resource;
import models.User;
import models.Error;
import play.Play;
import play.db.jpa.Blob;
import play.mvc.Controller;

public class ResourceController extends Controller{
	
	public static void allResource(){
		List<Resource> resources = Resource.findAll();
		
		renderJSON(CommonUtil.toJson(resources, 
				"*.class",
				"*.id",
				"*.persistent",
				"user.account",
				"user.address",
				"user.avatar.file",
				"user.avatar.image",
				"user.avatar.store",
				"user.resources",
				"user.followers",
				"user.followings",
				"user.comments",
				"videoThumbnail.file",
				"videoThumbnail.image",
				"videoThumbnail.store",
				"channel.resources",
				"comments"));
	}
	
	public static void resourcesByChannel(String accessToken, String channelCode){
		Account account = Account.find("access_token = ?", accessToken).first();
		if(account == null){
			renderJSON(new Error("The account cannot be found."));
		}
		
		Channel channel = Channel.find("byCode", Integer.parseInt(channelCode)).first();
		if(channel == null){
			renderJSON(new Error("The channel cannot be found."));
		}
		
		List<Resource> resources = Resource.find("byChannel", channel).fetch();
		
		renderJSON(CommonUtil.toJson(resources, 
				"*.class",
				"*.id",
				"*.persistent",
				"videoThumbnail.file",
				"videoThumbnail.image",
				"videoThumbnail.store",
				"channel.resources",
				"comments",
				"user.account",
				"user.address",
				"user.avatar.file",
				"user.avatar.image",
				"user.avatar.store",
				"user.resources",
				"user.followers",
				"user.followings",
				"user.comments"));
	}
	
	public static void addLike(String accessToken, long resId){
		Account account = Account.find("access_token = ?", accessToken).first();
		if(account == null){
			renderJSON(new Error("The account cannot be found."));
		}
		
		Resource resource = Resource.findById(resId);
		if(resource == null){
			renderJSON(new Error("Resource cannot be found."));
		}
		
		resource.likeCount = resource.likeCount + 1;
		resource.save();
		
		renderJSON(CommonUtil.toJson(resource, 
				"*.class",
				"*.id",
				"*.persistent",
				"videoThumbnail.file",
				"videoThumbnail.image",
				"videoThumbnail.store",
				"channel.resources",
				"comments",
				"user.account",
				"user.address",
				"user.avatar.file",
				"user.avatar.image",
				"user.avatar.store",
				"user.resources",
				"user.followers",
				"user.followings",
				"user.comments"));
	}
	
	public static void upload(String accessToken, File file, Blob thumbnail, String title, String caption, int type, int channelCode){
		Account account = Account.find("access_token = ?", accessToken).first();
		if(account == null){
			renderJSON(new Error("The account cannot be found."));
		}
		
		if(account.user == null){
			account.user = new User(account);
			account.user.save();
		}
		
		Channel channel = Channel.find("code = ?", channelCode).first();
		if(channel == null){
			renderJSON(new Error("The channel cannot be found."));
		}
		
		Resource resource = new Resource(account.user, file, thumbnail, title, caption, type, channel);
		resource.save();
		
		renderJSON(CommonUtil.toJson(resource, 
				"*.class",
				"*.id",
				"*.persistent",
				"channel.resources",
				"user",
				"videoThumbnail",
				"comments"));
	}
	
	public static void getUploadedUser(String accessToken, long resId){
		Account account = Account.find("access_token = ?", accessToken).first();
		if(account == null){
			renderJSON(new Error("The account cannot be found."));
		}
		
		Resource resource = Resource.findById(resId);
		if(resource == null){
			renderJSON(new Error("Resource cannot be found."));
		}
		
		User user = resource.user;
		
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
	
	public static void comment(String accessToken, long resourceId, String content){
		Account account = Account.find("access_token = ?", accessToken).first();
		if(account == null){
			renderJSON(new Error("The account cannot be found."));
		}
		
		Resource resource = Resource.findById(resourceId);
		if(resource == null){
			renderJSON(new Error("The resource cannot be found."));
		}
		
		Comment comment = new Comment(account.user, resource, content);
		comment.save();
		
		renderJSON(CommonUtil.toJson(comment,
				"*.class",
				"*.id",
				"*.persistent",
				"resource",
				"user.account",
				"user.address",
				"user.avatar.file",
				"user.avatar.image",
				"user.avatar.store",
				"user.comments",
				"user.followers",
				"user.followings",
				"user.resources"));
	}
	
	public static void getCommentsByResourceId(String accessToken, long resourceId){
		Account account = Account.find("access_token = ?", accessToken).first();
		if(account == null){
			renderJSON(new Error("The account cannot be found."));
		}
		
		Resource resource = Resource.findById(resourceId);
		if(resource == null){
			renderJSON(new Error("The resource cannot be found."));
		}
		
		List<Comment> comments = Comment.find("byResource", resource).fetch();
		
		renderJSON(CommonUtil.toJson(comments,
				"*.class",
				"*.id",
				"*.persistent",
				"resource",
				"user.account",
				"user.address",
				"user.avatar.file",
				"user.avatar.image",
				"user.avatar.store",
				"user.comments",
				"user.followers",
				"user.followings",
				"user.resources"));
	}
	
}






























