package hello.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FileEntity {

	@Id
	private String name;
	private String uploadTime;
	
	public FileEntity() {
		
	}
	
	public FileEntity(String name, String uploadTime) {
		super();
		this.name = name;
		this.uploadTime = uploadTime;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}
	
	
}
