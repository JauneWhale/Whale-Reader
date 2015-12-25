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
    public RSS RSS;
    public boolean ifread,dirty,ifremove;
	public RArticle(Element ua, boolean t, RSS r){
    	UID = ua.getChildText("UID");
    	Title = ua.getChildText("Title");
    	URL = ua.getChildText("URL");
    	//Abstract = ua.getChildText("Abstract");
    	Abstract = null;//All abstract information only need to get while update
    	Author = ua.getChildText("Author");
    	ifread = t;
    	dirty = false;
    	RSS = r;
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	try{
    		date = sdf.parse(ua.getChildText("Date"));
    		int days = (int) (((new Date()).getTime() - date.getTime()) / (1000*3600*24));
        	ifremove = (days>7)?true:false;//Judge that if it need to remove
    	}catch(Exception e){ System.out.println(e);}
    }
	public RArticle(SyndEntry entry, RSS r) throws Exception{
    	Title = entry.getTitle();
    	UID = Integer.toHexString(Title.hashCode());
    	URL = entry.getLink();
        SyndContent description = entry.getDescription();
    	Abstract = description.getValue();
    	Author = entry.getAuthor();
    	date = entry.getPublishedDate();
    	RSS = r;
    	ifread = false;
    	dirty = false;
    	ifremove = false;
    }
}
