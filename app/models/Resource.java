package models;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.Play;
import play.db.jpa.Blob;
import play.db.jpa.Model;
import play.libs.Images;

@Entity
public class Resource extends Model{
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User user;
	
	public String title;
	
	public String caption;
	
	public int type;
	
	@Column(name="like_count")
	public int likeCount;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="upload_datetime", nullable = false)
	public Date uploadDatetime;
	
	@Column(name="file_path")
	public String filePath;
	
	@Column(name="video_thumbnail")
	public Blob videoThumbnail;
	
	@Column(name="photo_thumbnail")
	public String photoThumbnail;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id")
	public Channel channel;
	
	@OneToMany(mappedBy = "resource")
	public Set<Comment> comments;

	public Resource(User user, File file, Blob thumbnail, String title, String caption, int type, Channel channel) {
		super();
		this.user = user;
		this.title = title;
		this.caption = caption;
		this.filePath = saveFile("attachments.path", "attachments", file);
		this.type = type;
		if(type == 1){
			this.videoThumbnail = thumbnail;
		}else{
			this.photoThumbnail = Play.configuration.getProperty("application.baseUrl") + "data/thumbnails/" + file.getName();
			Images.resize(file, getFile("thumbnails.path", "thumbnails", file.getName()), 300, -1);
		}
		this.likeCount = 0;
		this.uploadDatetime = new Date();
		this.channel = channel;
	}
	
	public String saveFile(String configPath, String folderName, File file){
		InputStream input = null;
		OutputStream output = null;
		File newFile = getFile(configPath, folderName, file.getName());
		
		try {
			byte[] buffer = new byte[8 * 1024];
			input = new FileInputStream(file);
			output = new FileOutputStream( newFile );
			
			int bytesRead;
		    while ((bytesRead = input.read(buffer)) != -1) {
		      output.write(buffer, 0, bytesRead);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(output != null){
					output.close();
				}
				if(input != null){
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return Play.configuration.getProperty("attachments.path", "attachments") + "/" + file.getName();
	}

	public File getFile(String configPath, String folderName, String fileName) {
        return new File(getStore(configPath, folderName), fileName);
    }
	
	public static File getStore(String configPath, String folderName) {
        String name = Play.configuration.getProperty(configPath, folderName);
        File store = null;
        if(new File(name).isAbsolute()) {
            store = new File(name);
        } else {
            store = Play.getFile(name);
        }
        if(!store.exists()) {
            store.mkdirs();
        }
        return store;
    }
}




































