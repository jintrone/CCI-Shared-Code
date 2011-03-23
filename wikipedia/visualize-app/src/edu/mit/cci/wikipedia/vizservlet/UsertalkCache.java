package edu.mit.cci.wikipedia.vizservlet;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class UsertalkCache {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String author;
	
	@Persistent
	private Text network;
	
	@Persistent
	private String pageTitle;
	
	@Persistent
	private Date date;
	
	public UsertalkCache(String pageTitle, String author, String _network, Date date) {
		this.network = new Text(_network);
		this.author = author;
		this.pageTitle = pageTitle;
		this.date = date;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getNetwork() {
		return network.getValue();
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
	
	public void setNetwork(String _network) {
		this.network = new Text(_network);
	}
	
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
}
