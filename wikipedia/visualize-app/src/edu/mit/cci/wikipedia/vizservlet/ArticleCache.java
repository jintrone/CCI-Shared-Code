package edu.mit.cci.wikipedia.vizservlet;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ArticleCache {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String author;
	
	@Persistent
	private String pageTitle;
	
	@Persistent
	private int size;
	
	@Persistent
	private Date date;
	
	public ArticleCache(String pageTitle, String author, Date date, int size) {
		this.author = author;
		this.pageTitle = pageTitle;
		this.date = date;
		this.size = size;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getPageTitle() {
		return pageTitle;
	}
	
	public Date getDate() {
		return date;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
}
