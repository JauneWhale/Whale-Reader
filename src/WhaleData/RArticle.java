package WhaleData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jdom.Element;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;

/**
 * 用于保存RSS源的文章的结构体类
 * @author Administrator
 *
 */
public class RArticle {
    public String UID;
    public String Title;
    public String URL;
    public String Abstract;
    public String Author;
    public Date date;
    public RSS RSS;
    public boolean ifread,ifremove;
    /**
     * 构造函数：通过XML信息来构造对应的文章信息结构体类
     * @param ua XML存放了文章信息的Element类
     * @param t 该文章是否已读
     * @param r 对应的RSS类指针（反向查询使用）
     */
	public RArticle(Element ua, boolean t, RSS r){
    	UID = ua.getChildText("UID");
    	Title = ua.getChildText("Title");
    	URL = ua.getChildText("URL");
    	Abstract = null;//All abstract information only need to get while update
    	Author = ua.getChildText("Author");
    	ifread = t;
    	RSS = r;
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	try{
    		date = sdf.parse(ua.getChildText("Date"));
    		int days = (int) (((new Date()).getTime() - date.getTime()) / (1000*3600*24));
        	ifremove = (days>7)?true:false;//Judge that if it need to remove
    	}catch(Exception e){ System.out.println(e);}
    }
	/**
	 * 构造函数：通过在线Update来构造文章结构体类
	 * @param entry 在线更新得到的SyndEntry类
	 * @param r 对应的RSS类指针（反向查询使用）
	 * @throws Exception
	 */
	public RArticle(SyndEntry entry, RSS r) throws Exception{
		Title = entry.getTitle();
    	UID = Integer.toHexString(Title.hashCode());
    	URL = entry.getLink();
        SyndContent description = entry.getDescription();
        if(description!=null)
        	Abstract = description.getValue();
    	Author = entry.getAuthor();
    	date = entry.getPublishedDate();
    	RSS = r;
    	ifread = false;
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	try{
    		int days = (int) (((new Date()).getTime() - date.getTime()) / (1000*3600*24));
        	ifremove = (days>7)?true:false;//Judge that if it need to remove
    	}catch(Exception e){ System.out.println(e);}
    }
}
