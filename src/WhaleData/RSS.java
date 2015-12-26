package WhaleData;

import java.util.Hashtable;
import org.jdom.Element;
import com.sun.syndication.feed.synd.SyndFeed;

/**
 * ���𱣴�RSSԴ�����Ϣ�Ľṹ����
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
	 * ���캯����ͨ��XML��Ԫ����Element������RSSԴ��
	 * @param rss XML��Element��
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
     * ���캯���� ͨ��Jdom����XML����õ�SyndFeed������RSSԴ��
     * @param feed SyndFeedԴ��Ϣ
     * @param fu RSSԴ����
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
     * �����ã���ӡRSSԴ��Ա������Ϣ
     */
    public void printInfo(){
    	System.out.println(Title);
    	System.out.println(RID);
    	System.out.println(Feed_URL);
    	System.out.println(Homepage_URL);
    }
    /*��Ԫ�����ú���
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

