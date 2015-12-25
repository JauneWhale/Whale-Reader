package WhaleData;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
/**
 * 用于存放所有和程序有关的数据修改的类
 * @author Administrator
 *
 */
public class RSSStore {
	public static final String XMLData = "RSSsource.xml";
	public static Hashtable<String,RSS> RS;
	public static Document doc;
	public static RArticle r;
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
	}
	/**
	 * 初始化整个阅读器的数据
	 * @param t
	 */
	public RSSStore(int t){
		//初始胡整个阅读器的数据
		if(t==0){
			RS = new Hashtable<String,RSS>();
			ReadXML();
			//CreateHTML();
			//UpdateXML();
		}
		//更新当前整个阅读器的数据
		if(t==2)
			UpdateXML();
		//其他情况：t==1 获取当前的类模型
	}
    /**
     * 读XML中的数据，包括了用户的RSS源以及之前的未读/已读数据
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
            	//单RSShashtable的修改
            	String a = us.getAttributeValue("source");
            	RSS tmp = RS.get(a);
            	RArticle ua = new RArticle(us, false, tmp);
            	if(!ua.ifremove){
            		tmp.UnreadA.put(ua.UID,ua);
                	tmp.UnReadNum++;
                	//Hhtml.CreateHtml(ua, Hhtml.ITEMS);
                	//Hhtml.CreateHtml(ua, Hhtml.MITEM);
            	}
            	tmp.AllA.put(ua.UID, ua);
             }
            //获取根元素下面的HavereadArticle节点
            Element HavereadSource = (Element) (root.getChildren("HavereadSource")).get(0);
            //获取RSS元素集合
            List<Element> HAList = HavereadSource.getChildren("UA");
            for(int j = 0; j < HAList.size(); j++){
            	Element hs = HAList.get(j);
            	String a = hs.getAttributeValue("source");
            	RSS tmp = RS.get(a);
            	RArticle ha = new RArticle(hs, true, tmp);
            	if(!ha.ifremove){
            		tmp.HavereadA.put(ha.UID,ha);
            		tmp.HaveReadNum++;
                	//Hhtml.CreateHtml(ha, Hhtml.ITEMS);
                	//Hhtml.CreateHtml(ha, Hhtml.MITEM);
            	}
            	tmp.AllA.put(ha.UID, ha);
             }
        } catch (Exception e) {e.printStackTrace();} 
    }
    /**
     * 用于退出程序时保存XML的函数
     * @throws IOException 读写文件出现错误的情况
     * @throws FileNotFoundException 文件不存在的情况
     */
    public void SaveXML() throws FileNotFoundException, IOException{
    	Element root = doc.getRootElement();
        Element UnreadSource = (Element) (root.getChildren("UnreadSource")).get(0);
        Element HavereadSource = (Element) (root.getChildren("HavereadSource")).get(0);
    	//由于之前采用的是懒惰删除，所以最后更新一下XML文件
    	List list=UnreadSource.getChildren("UA");
    	List<Element> rmlist = new LinkedList<Element>();
    	for(Iterator<Element> items = list.iterator();items.hasNext();){
    	    Element ua = (Element)items.next();
    	    RArticle ra = RS.get(ua.getAttributeValue("source")).AllA.get(ua.getChild("UID").getValue());
    	    if(ra.ifremove)	{
    	    	Hhtml.RemovItem(ra.UID);
    	    	rmlist.add(ua);
    	    }
    	    if(ra.ifread)	{ rmlist.add(ua);  }
    	}
    	for(Iterator<Element> items = rmlist.iterator();items.hasNext();){
    	    Element ua = (Element)items.next();
    		ua.getParent().removeContent(ua);
    	}
    	//Check the HavereadSource information if remove
    	list = HavereadSource.getChildren("UA");
    	rmlist = new LinkedList<Element>();
    	for(Iterator items = list.iterator();items.hasNext();){
    		//next()方法返回迭代的下一个元素。重复调用此方法直到hasNext()方法返回 false，
    		//这将精确地一次性返回迭代器指向的集合中的所有元素
    	    Element ua = (Element)items.next();
    	    //Hashtable<String,RArticle> t = RS.get(ua.getAttributeValue("source")).AllA;
    	    RArticle ra = RS.get(ua.getAttributeValue("source")).AllA.get(ua.getChild("UID").getValue());
    	    if(ra.ifremove)	{
    	    	Hhtml.RemovItem(ra.UID);
    	    	rmlist.add(ua);
    	    }
    	}
    	for(Iterator items = rmlist.iterator();items.hasNext();){
    	    Element ua = (Element)items.next();
    		ua.getParent().removeContent(ua);
    	}
    	Format format=Format.getRawFormat();
    	format.setEncoding("UTF-8");
      	//XMLOutputter类提供了将JDOM树输出为字节流的能力
      	XMLOutputter output=new XMLOutputter(format);
      	output.output(doc, new FileOutputStream(XMLData));
    }
    /**
     * 通过网络更新当前的XML
     * @param rid 要更新的RSS源的id号
     * @return 成功则返回更新RSS源的结构体，否则返回null
     */
    public RSS UpdateXML(String rid){
    	RSS r = RS.get(rid);
    	return UpdateXML(r);
    }
    /**
     * 通过网络更新当前的XML
     * @param r 要更新的RSS源
     * @return 成功则返回RSS源结构体，否则返回null
     */
    public RSS UpdateXML(RSS r){
        try {
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
            	if(r.AllA.containsKey(Integer.toHexString(entry.getTitle().hashCode())))
            		continue;
            	RArticle ua = new RArticle(entry, r);
            	if(!ua.ifremove){
            		Hhtml.CreateHtml(ua, Hhtml.ITEMS);
            		Hhtml.CreateHtml(ua, Hhtml.MITEM);
                	r.UnreadA.put(ua.UID, ua);
                	r.AllA.put(ua.UID, ua);
                	r.UnReadNum++;
                	AddtoXML(ua,r.RID);
            	}
            }
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 通过网络更新当前全部RSS源，并保存到XML的document中
     */
    public void UpdateXML(){
        try {
            Iterator i = RS.entrySet().iterator();
            while(i.hasNext()){
                RSS r = (RSS) ((Entry) i.next()).getValue();
                UpdateXML(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 通过url添加一个RSS源，
     * @param url RSS源的feed链接
     * @return 成功则返回添加的RSS结构体，否则返回null
     */
    public RSS AddtoXML(String url){
        XmlReader reader;
		try {
			reader = new XmlReader(new URL(url));
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(reader);
	    	RSS r = new RSS(feed,url);
	    	RS.put(r.RID, r);
	    	AddtoXML(r);
	    	UpdateXML(r);
	    	return r;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
    }
    /**
     * 将一个RSS结构体添加到XML的document中
     * @param r RSS 结构体
     */
    private void AddtoXML(RSS r){
    	Element root = doc.getRootElement();
        Element FeedSource = (Element) (root.getChildren("FeedSource")).get(0);
        Element rss = new Element("RSS");
        rss.addContent(new Element("RID").setText(r.RID));
        rss.addContent(new Element("Title").setText(r.Title));
        rss.addContent(new Element("Feed_URL").setText(r.Feed_URL));
        rss.addContent(new Element("Homepage_URL").setText(r.Homepage_URL));
        FeedSource.addContent(rss);
    }
    /**
     * 将一个更新的文章添加到XML的document中
     * @param ua 更新得来的文章的结构体
     * @param ParentID 对应的RSS源的ID号
     */
    public static void AddtoXML(RArticle ua, String ParentID){
    	String sn = (ua.ifread)?"HavereadSource":"UnreadSource";
    	Element root = doc.getRootElement();
        Element Source = (Element) (root.getChildren(sn)).get(0);
        Element UA = new Element("UA");
        UA.setAttribute("source",ParentID);
        UA.addContent(new Element("UID").setText(ua.UID));
        UA.addContent(new Element("Title").setText(ua.Title));
        UA.addContent(new Element("URL").setText(ua.URL));
        //UA.addContent(new Element("Abstract").setText(ua.Abstract));
        UA.addContent(new Element("Author").setText(ua.Author));
        UA.addContent(new Element("Date").setText((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(ua.date)));
        Source.addContent(UA);
    }
    /**
     * 删除所有的RSS源
     */
	public void DelAllRSSfromXML(){
        Iterator i = RS.entrySet().iterator();
        while(i.hasNext()){
            RSS r = (RSS) ((Entry) i.next()).getValue();
            DelfromXML(r);
        }
	}
    /**
     * 根据id号删除指定的RSS源，同时删除下面的所有文章
     * @param rid 要删除的RSS源的id号
     */
    public void DelfromXML(String rid){
        RSS r = RS.get(rid);
        DelfromXML(r);
    }
    /**
     * 根据RSS结构体删除指定的RSS源，同时删除下面所有文章
     * @param r 要删除的RSS结构体
     */
    public void DelfromXML(RSS r){
    	r.ifremove = true;
    	for(Iterator i = r.AllA.entrySet().iterator();i.hasNext();){
    		RArticle ua = (RArticle) ((Entry) i.next()).getValue();
    		ua.ifremove = true;
    	}
    }
    /**
     * 根据文章结构体删除指定的文章
     * @param ua 文章的结构体
     */
    public void DelfromXML(RArticle ua){
    	ua.ifremove = true;
    }
    /**
     * 将一篇文章标记为已读
     * @param ua 文章的结构体
     */
    public static void MarkReadfromXML(String uid, String rid){
    	RSS r = RSSStore.RS.get(rid);
		RArticle ua = r.UnreadA.get(uid);
    	ua.ifread = true;
    	r.UnreadA.remove(ua.UID);
    	r.UnReadNum--;
    	r.HavereadA.put(ua.UID, ua);
    	r.HaveReadNum++;
    	AddtoXML(ua, r.RID);
    }
    /**
     * 将一篇文章标记为已读
     * @param ua 文章的结构体
     */
    public static void MarkReadfromXML(RArticle ua){
    	ua.ifread = true;
    	RSS r = ua.RSS;
    	r.UnreadA.remove(ua.UID);
    	r.UnReadNum--;
    	r.HavereadA.put(ua.UID, ua);
    	r.HaveReadNum++;
    	AddtoXML(ua, r.RID);
    }
	/**
	 * 将指定RSS源中的所有文章标记为已读
	 * @param rid 指定RSS源的id
	 */
	public static RSS MarkAllRead(String rid){
        RSS r = RS.get(rid);
        MarkAllRead(r);
        return r;
	}
	/**
	 * 将指定RSS源中的所有文章标记为已读
	 * @param r 指定RSS源的结构体
	 */
	public static void MarkAllRead(RSS r){
        Iterator j = r.UnreadA.entrySet().iterator();
        while(j.hasNext()){
        	RArticle ua = (RArticle)((Entry)j.next()).getValue();
        	ua.ifread = true;
        	r.HavereadA.put(ua.UID, ua);
        	r.HaveReadNum++;
        	AddtoXML(ua, r.RID);
        }
        r.UnreadA.clear();
        r.UnReadNum = 0;
	}
	/**
	 * 将所有RSS源中的所有文章标记为已读
	 */
	public static void MarkAllRead(){
        Iterator i = RS.entrySet().iterator();
        while(i.hasNext()){
            RSS r = (RSS) ((Entry) i.next()).getValue();
            MarkAllRead(r);
        }
	}
    /**
     * 获取所有RSS未读源的文章列表
     * @return 返回未读文章列表
     */
    public static List<RArticle>  GetUnreadNewsList(){
    	List<RArticle> listModel = new LinkedList();
        Iterator i = RS.entrySet().iterator();
        while(i.hasNext()){
            RSS r = (RSS) ((Entry) i.next()).getValue();
            Iterator j = r.UnreadA.entrySet().iterator();
            while(j.hasNext()){
            	RArticle u = (RArticle)((Entry)j.next()).getValue();
            	if(!u.ifremove)
            		listModel.add(u);
            }
        }
        return listModel;
    }
    /**
     * 获取指定RSS源的未读文章列表
     * @param RSS 指定RSS源结构体
     * @return 返回未读文章列表
     */
    public static List<RArticle> GetUnreadNewsList(RSS r){
    	List<RArticle> listModel = new LinkedList<RArticle>();
    	Iterator i = r.UnreadA.entrySet().iterator();
    	while(i.hasNext()){
    		RArticle u = (RArticle)((Entry)i.next()).getValue();
    		if(!u.ifremove)
    			listModel.add(u);
    	}
    	listModel = getSortedHashtableByKey(r.UnreadA);
    	return listModel;
    }
    /**
     * 获取所有RSS源已读文章列表
     * @return 返回已读文章列表
     */
    public static List<RArticle>  GetReadNewsList(){
    	List<RArticle> listModel = new LinkedList<RArticle>();
        Iterator i = RS.entrySet().iterator();
        while(i.hasNext()){
            RSS r = (RSS) ((Entry) i.next()).getValue();
            Iterator j = r.HavereadA.entrySet().iterator();
            while(j.hasNext()){
            	RArticle u = (RArticle)((Entry)j.next()).getValue();
            	if(!u.ifremove)
            		listModel.add(u);
            }
        }
        return listModel;
    }
    /**
     * 获取指定RSS源已读文章列表
     * @param RSS 指定RSS源的结构体
     * @return 返回已读文章列表
     */
    public static List<RArticle> GetReadNewsList(RSS r){
    	List<RArticle> listModel = new LinkedList<RArticle>();
    	Iterator i = r.HavereadA.entrySet().iterator();
    	while(i.hasNext()){
    		RArticle u = (RArticle)((Entry)i.next()).getValue();
    		if(!u.ifremove)
    			listModel.add(u);
    	}
    	listModel = getSortedHashtableByKey(r.HavereadA);
    	return listModel;
    }
    /**
     * 获取指定RSS源所有文章列表
     * @param r 指定RSS源的结构体
     * @return 返回所有文章列表
     */
    public static List<RArticle> GetAllNewsList(RSS r){
    	List<RArticle> listModel = new LinkedList<RArticle>();
    	Iterator i = r.AllA.entrySet().iterator();
    	while(i.hasNext()){
    		RArticle u = (RArticle)((Entry)i.next()).getValue();
    		if(!u.ifremove)
    			listModel.add(u);
    	}
    	listModel = getSortedHashtableByKey(r.AllA);
    	return listModel;
    }
    /**
     * 获取所有RSS源的名字以及他们未读文章数字，提供给显示树来显示
     * @return RSS源名字+未读文章数目
     */
	public List<String> BuildDefTree(){
		List<String> rtree = new LinkedList<String>();
        Iterator i = RS.entrySet().iterator();
        while(i.hasNext()){
            Entry<String, WhaleData.RSS> entry = (Entry<String, RSS>) i.next();
			RSS r = (RSS) entry.getValue();
			if(r.ifremove) continue;
            String print;
            print = r.Title + "(" + r.UnReadNum+")";
            rtree.add(print);
		}
        return rtree;
	}
	/**
	 * 将指定哈希表中的文章列表按时间由近到远的顺序排序，以列表的形式返回
	 * @param h
	 * @return 按时间顺序排序的文章列表
	 */
	public static List<RArticle> getSortedHashtableByKey(Hashtable<String,RArticle> h) {
	    Set<Entry<String, RArticle>> set = h.entrySet();
	    Map.Entry<String,RArticle>[] entries = (Map.Entry<String,RArticle>[]) set.toArray(new Map.Entry[set.size()]);
	    Arrays.sort(entries, new Comparator<Object>() {
	      public int compare(Object arg0, Object arg1) {
	        RArticle key1 = (RArticle) ((Map.Entry) arg0).getValue();
	        RArticle key2 = (RArticle) ((Map.Entry) arg1).getValue();
	        return key2.date.compareTo(key1.date);
	      }
	    });
	    List<RArticle> res = new LinkedList<RArticle>();
	    for (int i = 0; i < entries.length; i++) {
	    	res.add(entries[i].getValue());
	    }
	    return res;
	}
}