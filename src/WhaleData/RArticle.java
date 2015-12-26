package WhaleData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jdom.Element;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;

/**
 * ���ڱ���RSSԴ�����µĽṹ����
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
     * ���캯����ͨ��XML��Ϣ�������Ӧ��������Ϣ�ṹ����
     * @param ua XML�����������Ϣ��Element��
     * @param t �������Ƿ��Ѷ�
     * @param r ��Ӧ��RSS��ָ�루�����ѯʹ�ã�
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
	 * ���캯����ͨ������Update���������½ṹ����
	 * @param entry ���߸��µõ���SyndEntry��
	 * @param r ��Ӧ��RSS��ָ�루�����ѯʹ�ã�
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
