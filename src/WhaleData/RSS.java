package WhaleData;

import java.util.Hashtable;
import org.jdom.Element;

public class RSS {
    public String RID;
    public String Title;
    public String Feed_URL;
    public String Homepage_URL;
    public int UnReadNum,HaveReadNum;
    public boolean dirty,ifremove;
	public Hashtable<String,RArticle> UnreadA;
	public Hashtable<String,RArticle> HavereadA;
	public Hashtable<String,RArticle> AllA;
    public RSS(){
    	
    }
    public RSS(Element rss){
    	RID = rss.getChildText("RID");
    	Title = rss.getChildText("Title");
    	Feed_URL = rss.getChildText("Feed_URL");
    	Homepage_URL = rss.getChildText("Homepage_URL");
    	UnReadNum = HaveReadNum = 0;
    	UnreadA = new Hashtable<String, RArticle>();
    	HavereadA = new Hashtable<String, RArticle>();
    	dirty = false;
    	ifremove = false;
    	AllA = new Hashtable<String, RArticle>();
    }
}
