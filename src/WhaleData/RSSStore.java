package WhaleData;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.DefaultListModel;

import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * Store all the related information about RSS
 * @author Administrator
 *
 */
public class RSSStore {
	//public static List<RSS> RS;
	//public static List<UnreadArticle> UA;
	public static final String XMLData = "RSSsource.xml";
	public static LibInfo LIB;
	public static Hashtable<String,RSS> RS;
	public static Document doc;
	public static void main(String args[]){
		RSSStore a = new RSSStore(1);
		try {
			a.SaveXML();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*for(int i=0; i<a.RS.size();i++){
			System.out.println(Integer.toHexString((a.RS.get(i).Title.hashCode())));
		}
		System.out.println(Integer.toHexString("Chrome快捷键大全".hashCode()));*/
	}
	/**
	 * 初始化整个阅读器的数据
	 */
	public RSSStore(int t){
		if(t==0){
			RS = new Hashtable<String,RSS>();
			//RS = new LinkedList<RSS>();
			//UA = new LinkedList<UnreadArticle>();
			LIB = new LibInfo(0);
			ReadXML();
			UpdateXML();
		}
		if(t==2)
			UpdateXML();
	}
    /**
     * 读XML中的数据，包括了用户的RSS源以及之前的未读数据
     */
    public void ReadXML(){
        try {
            //创建一个SAXBuilder对象
        	SAXBuilder saxBuilder = new SAXBuilder();            
            //读取资源
            doc = saxBuilder.build(XMLData);
            //获取根元素(prop)
            Element root = doc.getRootElement();
            //获取根元素下面的FeedSource节点
            Element RSSRoot = (Element) (root.getChildren("FeedSource")).get(0);
            //获取RSS元素集合
            List<Element> RSSList = RSSRoot.getChildren("RSS");
            //遍历子根元素的子元素集合(即遍历property元素)
            for(int j = 0; j < RSSList.size(); j++){
                //获取RSS元素
            	Element rss = RSSList.get(j);
            	RSS source = new RSS(rss);
            	//RS.add(source);
            	RS.put(source.RID, source);
             }
            //获取根元素下面的UnreadArticle节点
            Element UnreadSource = (Element) (root.getChildren("UnreadSource")).get(0);
            //获取RSS元素集合
            List<Element> UAList = UnreadSource.getChildren("UA");
            //遍历子根元素的子元素集合(即遍历property元素)
            for(int j = 0; j < UAList.size(); j++){
                //获取UA元素
            	Element us = UAList.get(j);
            	RArticle ua = new RArticle(us, false);
            	//单RSShashtable的修改
            	String a = us.getAttributeValue("source");
            	RSS tmp = RS.get(a);
            	tmp.UnreadA.put(ua.UID,ua);
            	tmp.AllA.put(ua.UID, ua);
            	tmp.UnReadNum++;
             }
            //获取根元素下面的HavereadArticle节点
            Element HavereadSource = (Element) (root.getChildren("HavereadSource")).get(0);
            //获取RSS元素集合
            List<Element> HAList = HavereadSource.getChildren("UA");
            for(int j = 0; j < HAList.size(); j++){
            	Element hs = HAList.get(j);
            	RArticle ha = new RArticle(hs, true);
            	String a = hs.getAttributeValue("source");
            	RSS tmp = RS.get(a);
            	tmp.HavereadA.put(ha.UID,ha);
            	tmp.AllA.put(ha.UID, ha);
            	tmp.HaveReadNum++;
             }
        } catch (Exception e) {e.printStackTrace();} 
    }
    /**
     * 通过网络更新当前的XML
     */
    public void UpdateXML(){
    	/*for(int i=0; i<=RS.size(); i++){
    		UA.addAll(RSSParse.ParseRss(RS.get(i).Feed_URL));
    	}*/
        try {
            Iterator i = RS.entrySet().iterator();
            while(i.hasNext()){
                RSS r = (RSS) ((Entry) i.next()).getValue();
                //System.out.println(entry.Feed_URL);
                URL url = new URL(r.Feed_URL);
                // 读取Rss源   
                XmlReader reader = new XmlReader(url);
                System.out.println("Rss源的编码格式为：" + reader.getEncoding());
                SyndFeedInput input = new SyndFeedInput();
                // 得到SyndFeed对象，即得到Rss源里的所有信息   
                SyndFeed feed = input.build(reader);
                //System.out.println(feed);
                // 得到Rss新闻中子项列表   
                List entries = feed.getEntries();
                // 循环得到每个子项信息   
                for (int j = 0; j < entries.size(); j++) {
                    SyndEntry entry = (SyndEntry) entries.get(j);
                	if(r.UnreadA.containsKey(entry.getTitle().hashCode()))
                		continue;
                	RArticle ua = new RArticle(entry);
                	LibInfo.Nuid++;
                	r.UnreadA.put(ua.UID, ua);
                	r.AllA.put(ua.UID, ua);
                	AddtoXML(ua,r.RID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void AddtoXML(RSS r){
    	Element root = doc.getRootElement();
        Element FeedSource = (Element) (root.getChildren("FeedSource")).get(0);
        Element rss = new Element("RSS");
        rss.addContent(new Element("RID").setText(r.RID));
        rss.addContent(new Element("Title").setText(r.Title));
        rss.addContent(new Element("Feed_URL").setText(r.Feed_URL));
        rss.addContent(new Element("Homepage_URL").setText(r.Homepage_URL));
        FeedSource.addContent(rss);
    }
    public void AddtoXML(RArticle ua, String ParentID){
    	String sn = (ua.ifread)?"HavereadSource":"UnreadSource";
    	Element root = doc.getRootElement();
        Element Source = (Element) (root.getChildren(sn)).get(0);
        Element UA = new Element("UA");
        UA.setAttribute("source",ParentID);
        UA.addContent(new Element("UID").setText(ua.UID));
        UA.addContent(new Element("Title").setText(ua.Title));
        UA.addContent(new Element("URL").setText(ua.URL));
        UA.addContent(new Element("Abstract").setText(ua.Abstract));
        UA.addContent(new Element("Author").setText(ua.Author));
        UA.addContent(new Element("Date").setText((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(ua.date)));
        Source.addContent(UA);
    }
    public void DelfromXML(RSS r){
    	r.ifremove = true;
    	for(Iterator i = r.AllA.entrySet().iterator();i.hasNext();){
    		RArticle ua = (RArticle) ((Entry) i.next()).getValue();
    		ua.ifremove = true;
    	}
    }
    public void DelfromXML(RArticle ua){
    	ua.ifremove = true;
    }
    /**
     * 保存XML
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public void SaveXML() throws FileNotFoundException, IOException{
    	Element root = doc.getRootElement();
        Element UnreadSource = (Element) (root.getChildren("UnreadSource")).get(0);
        Element HavereadSource = (Element) (root.getChildren("HavereadSource")).get(0);
    	//Element类中定义了获取子元素的方法,得到所有子元素
	    DefaultListModel listModel = new DefaultListModel();
    	List list=UnreadSource.getChildren("UA");
    	for(Iterator items = list.iterator();items.hasNext();){
    		//next()方法返回迭代的下一个元素。重复调用此方法直到hasNext()方法返回 false，
    		//这将精确地一次性返回迭代器指向的集合中的所有元素
    	    Element ua = (Element)items.next();
    	    Hashtable<String,RArticle> t = RS.get(ua.getAttributeValue("source")).AllA;
    	    RArticle ra = RS.get(ua.getAttributeValue("source")).AllA.get(ua.getChild("UID").getValue());
    	    if(ra.ifremove)	{ ua.getParent().removeContent(ua); }
    	    if(ra.ifread)	{ ua.getParent().removeContent(ua); }
    	}
    	Format format=Format.getRawFormat();
    	format.setEncoding("UTF-8");
      	//XMLOutputter类提供了将JDOM树输出为字节流的能力
      	XMLOutputter output=new XMLOutputter(format);
      	output.output(doc, new FileOutputStream(XMLData));
    }
    public DefaultListModel  GetNewslist(){
	    DefaultListModel listModel = new DefaultListModel();
        Iterator i = RS.entrySet().iterator();
        while(i.hasNext()){
            RSS r = (RSS) ((Entry) i.next()).getValue();
            Iterator j = r.UnreadA.entrySet().iterator();
            while(j.hasNext()){
            	RArticle u = (RArticle)((Entry)j.next()).getValue();
            	listModel.addElement(u.Title);
            }
        }
        return listModel;
    }
}