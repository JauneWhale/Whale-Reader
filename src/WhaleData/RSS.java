package WhaleData;

import java.util.Hashtable;
import org.jdom.Element;
import com.sun.syndication.feed.synd.SyndFeed;

/**
 * 负责保存RSS源相关信息的结构体类
 * @author Administrator
 *
 */
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
	/**
	 * 构造函数：通过XML的元素类Element来构造RSS源类
	 * @param rss XML的Element类
	 */
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
    /**
     * 构造函数： 通过Jdom解析XML类后获得的SyndFeed来构造RSS源类
     * @param feed SyndFeed源信息
     * @param fu RSS源链接
     */
    public RSS(SyndFeed feed, String fu){
    	Title = feed.getTitle();
    	RID = Integer.toHexString(Title.hashCode());
    	Feed_URL = fu;
    	Homepage_URL = feed.getLink();
    	UnReadNum = HaveReadNum = 0;
    	UnreadA = new Hashtable<String, RArticle>();
    	HavereadA = new Hashtable<String, RArticle>();
    	dirty = false;
    	ifremove = false;
    	AllA = new Hashtable<String, RArticle>();
    }
    /**
     * 测试用：打印RSS源成员变量信息
     */
    public void printInfo(){
    	System.out.println(Title);
    	System.out.println(RID);
    	System.out.println(Feed_URL);
    	System.out.println(Homepage_URL);
    }
    /*单元测试用函数
	public static void main(String[] args) {
		String url = "http://feeds.appinn.com/appinns/";
		XmlReader reader;
		try {
			reader = new XmlReader(new URL(url));
			System.out.println(reader.getEncoding());
	        SyndFeedInput input = new SyndFeedInput();
	        SyndFeed feed;
			feed = input.build(reader);
	    	RSS r = new RSS(feed, url);
	    	r.printInfo();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException | FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}

