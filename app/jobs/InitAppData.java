package jobs;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import Utils.FileReader;

import flexjson.JSONDeserializer;
import models.Account;
import models.Channel;
import models.User;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.vfs.VirtualFile;

@OnApplicationStart
public class InitAppData extends Job{
	
	@Override
	public void doJob() throws Exception {
		createAdmin();
		insertTags();
	}
	
	private void insertTags() {
		if(Channel.count() == 0){
			String path = Play.configuration.getProperty("assets.path") + "/channels.json";
			VirtualFile vf = VirtualFile.fromRelativePath(path);
			String json = FileReader.read(vf.inputstream());
			Channel.createChannelsByJson(json);
		}
	}
	
	private void createAdmin(){
		long count = User.count("byRole", 2);
		if(count < 1){
			Account account = new Account(0, "admin@pym.com", "admin0pym", null, null);
			account.save();
			
			User admin = new User(account);
			admin.role = 2;
			admin.save();
		}
	}
	
}
