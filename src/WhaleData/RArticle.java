package WhaleData;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.jdom.Element;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;

public class RArticle {
    public String UID;
    public String Title;
    public String URL;
    public String Abstract;
    public String Author;
    public Date date;
    public boolean ifread,dirty,ifremove;
	public RArticle(Element ua, boolean t){
    	UID = ua.getChildText("UID");
    	Title = ua.getChildText("Title");
    	URL = ua.getChildText("URL");
    	Abstract = ua.getChildText("Abstract");
    	Author = ua.getChildText("Author");
    	ifread = t;
    	dirty = false;
    	ifremove = false;
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	try{
    		date = sdf.parse(ua.getChildText("Date"));
    	}catch(Exception e){ System.out.println(e);}
    }
	public RArticle(SyndEntry entry) throws Exception{
    	Title = entry.getTitle();
    	UID = Integer.toHexString(Title.hashCode());
    	URL = entry.getLink();
        SyndContent description = entry.getDescription();
    	Abstract = description.getValue();
    	Author = entry.getAuthor();
    	date = entry.getPublishedDate();
    	ifread = false;
    	dirty = false;
    	ifremove = false;
    }
}
