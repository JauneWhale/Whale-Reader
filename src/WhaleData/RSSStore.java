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
 * ���ڴ�����кͳ����йص������޸ĵ���
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
	 * ��ʼ�������Ķ���������
	 * @param t
	 */
	public RSSStore(int t){
		//��ʼ�������Ķ���������
		if(t==0){
			RS = new Hashtable<String,RSS>();
			ReadXML();
			//CreateHTML();
			//UpdateXML();
		}
		//���µ�ǰ�����Ķ���������
		if(t==2)
			UpdateXML();
		//���������t==1 ��ȡ��ǰ����ģ��
	}
    /**
     * ��XML�е����ݣ��������û���RSSԴ�Լ�֮ǰ��δ��/�Ѷ�����
     */
    public void ReadXML(){
        try {
            //����һ��SAXBuilder����
        	SAXBuilder saxBuilder = new SAXBuilder();            
            //��ȡ��Դ
            doc = saxBuilder.build(XMLData);
            //��ȡ��Ԫ��(prop)
            Element root = doc.getRootElement();
            //��ȡ��Ԫ�������FeedSource�ڵ�
            Element RSSRoot = (Element) (root.getChildren("FeedSource")).get(0);
            //��ȡRSSԪ�ؼ���
            List<Element> RSSList = RSSRoot.getChildren("RSS");
            //�����Ӹ�Ԫ�ص���Ԫ�ؼ���(������propertyԪ��)
            for(int j = 0; j < RSSList.size(); j++){
                //��ȡRSSԪ��
            	Element rss = RSSList.get(j);
            	RSS source = new RSS(rss);
            	//RS.add(source);
            	RS.put(source.RID, source);
             }
            //��ȡ��Ԫ�������UnreadArticle�ڵ�
            Element UnreadSource = (Element) (root.getChildren("UnreadSource")).get(0);
            //��ȡRSSԪ�ؼ���
            List<Element> UAList = UnreadSource.getChildren("UA");
            //�����Ӹ�Ԫ�ص���Ԫ�ؼ���(������propertyԪ��)
            for(int j = 0; j < UAList.size(); j++){
                //��ȡUAԪ��
            	Element us = UAList.get(j);
            	//��RSShashtable���޸�
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
            //��ȡ��Ԫ�������HavereadArticle�ڵ�
            Element HavereadSource = (Element) (root.getChildren("HavereadSource")).get(0);
            //��ȡRSSԪ�ؼ���
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
     * �����˳�����ʱ����XML�ĺ���
     * @throws IOException ��д�ļ����ִ�������
     * @throws FileNotFoundException �ļ������ڵ����
     */
    public void SaveXML() throws FileNotFoundException, IOException{
    	Element root = doc.getRootElement();
        Element UnreadSource = (Element) (root.getChildren("UnreadSource")).get(0);
        Element HavereadSource = (Element) (root.getChildren("HavereadSource")).get(0);
    	//����֮ǰ���õ�������ɾ��������������һ��XML�ļ�
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
    		//next()�������ص�������һ��Ԫ�ء��ظ����ô˷���ֱ��hasNext()�������� false��
    		//�⽫��ȷ��һ���Է��ص�����ָ��ļ����е�����Ԫ��
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
      	//XMLOutputter���ṩ�˽�JDOM�����Ϊ�ֽ���������
      	XMLOutputter output=new XMLOutputter(format);
      	output.output(doc, new FileOutputStream(XMLData));
    }
    /**
     * ͨ��������µ�ǰ��XML
     * @param rid Ҫ���µ�RSSԴ��id��
     * @return �ɹ��򷵻ظ���RSSԴ�Ľṹ�壬���򷵻�null
     */
    public RSS UpdateXML(String rid){
    	RSS r = RS.get(rid);
    	return UpdateXML(r);
    }
    /**
     * ͨ��������µ�ǰ��XML
     * @param r Ҫ���µ�RSSԴ
     * @return �ɹ��򷵻�RSSԴ�ṹ�壬���򷵻�null
     */
    public RSS UpdateXML(RSS r){
        try {
            //System.out.println(entry.Feed_URL);
            URL url = new URL(r.Feed_URL);
            // ��ȡRssԴ   
            XmlReader reader = new XmlReader(url);
            System.out.println("RssԴ�ı����ʽΪ��" + reader.getEncoding());
            SyndFeedInput input = new SyndFeedInput();
            // �õ�SyndFeed���󣬼��õ�RssԴ���������Ϣ   
            SyndFeed feed = input.build(reader);
            //System.out.println(feed);
            // �õ�Rss�����������б�   
            List entries = feed.getEntries();
            // ѭ���õ�ÿ��������Ϣ   
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
     * ͨ��������µ�ǰȫ��RSSԴ�������浽XML��document��
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
     * ͨ��url���һ��RSSԴ��
     * @param url RSSԴ��feed����
     * @return �ɹ��򷵻���ӵ�RSS�ṹ�壬���򷵻�null
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
     * ��һ��RSS�ṹ����ӵ�XML��document��
     * @param r RSS �ṹ��
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
     * ��һ�����µ�������ӵ�XML��document��
     * @param ua ���µ��������µĽṹ��
     * @param ParentID ��Ӧ��RSSԴ��ID��
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
     * ɾ�����е�RSSԴ
     */
	public void DelAllRSSfromXML(){
        Iterator i = RS.entrySet().iterator();
        while(i.hasNext()){
            RSS r = (RSS) ((Entry) i.next()).getValue();
            DelfromXML(r);
        }
	}
    /**
     * ����id��ɾ��ָ����RSSԴ��ͬʱɾ���������������
     * @param rid Ҫɾ����RSSԴ��id��
     */
    public void DelfromXML(String rid){
        RSS r = RS.get(rid);
        DelfromXML(r);
    }
    /**
     * ����RSS�ṹ��ɾ��ָ����RSSԴ��ͬʱɾ��������������
     * @param r Ҫɾ����RSS�ṹ��
     */
    public void DelfromXML(RSS r){
    	r.ifremove = true;
    	for(Iterator i = r.AllA.entrySet().iterator();i.hasNext();){
    		RArticle ua = (RArticle) ((Entry) i.next()).getValue();
    		ua.ifremove = true;
    	}
    }
    /**
     * �������½ṹ��ɾ��ָ��������
     * @param ua ���µĽṹ��
     */
    public void DelfromXML(RArticle ua){
    	ua.ifremove = true;
    }
    /**
     * ��һƪ���±��Ϊ�Ѷ�
     * @param ua ���µĽṹ��
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
     * ��һƪ���±��Ϊ�Ѷ�
     * @param ua ���µĽṹ��
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
	 * ��ָ��RSSԴ�е��������±��Ϊ�Ѷ�
	 * @param rid ָ��RSSԴ��id
	 */
	public static RSS MarkAllRead(String rid){
        RSS r = RS.get(rid);
        MarkAllRead(r);
        return r;
	}
	/**
	 * ��ָ��RSSԴ�е��������±��Ϊ�Ѷ�
	 * @param r ָ��RSSԴ�Ľṹ��
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
	 * ������RSSԴ�е��������±��Ϊ�Ѷ�
	 */
	public static void MarkAllRead(){
        Iterator i = RS.entrySet().iterator();
        while(i.hasNext()){
            RSS r = (RSS) ((Entry) i.next()).getValue();
            MarkAllRead(r);
        }
	}
    /**
     * ��ȡ����RSSδ��Դ�������б�
     * @return ����δ�������б�
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
     * ��ȡָ��RSSԴ��δ�������б�
     * @param RSS ָ��RSSԴ�ṹ��
     * @return ����δ�������б�
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
     * ��ȡ����RSSԴ�Ѷ������б�
     * @return �����Ѷ������б�
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
     * ��ȡָ��RSSԴ�Ѷ������б�
     * @param RSS ָ��RSSԴ�Ľṹ��
     * @return �����Ѷ������б�
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
     * ��ȡָ��RSSԴ���������б�
     * @param r ָ��RSSԴ�Ľṹ��
     * @return �������������б�
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
     * ��ȡ����RSSԴ�������Լ�����δ���������֣��ṩ����ʾ������ʾ
     * @return RSSԴ����+δ��������Ŀ
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
	 * ��ָ����ϣ���е������б�ʱ���ɽ���Զ��˳���������б����ʽ����
	 * @param h
	 * @return ��ʱ��˳������������б�
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