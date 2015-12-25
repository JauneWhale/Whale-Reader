package WhaleData;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
/**
 * ��ǰ���ڻ�ȡ���RSSԴ��Ϣ�Ĳ����࣬������
 * @author Administrator
 * @deprecated
 *
 */
public final class RSSParse {
    /**
     * Replace by ParseRss
     * @param rss
     * @return
     */
    public static String[] parseRss(String rss) {
    	String[] res;
        try {
            URL url = new URL(rss);
            // ��ȡRssԴ   
            XmlReader reader = new XmlReader(url);
            System.out.println("RssԴ�ı����ʽΪ��" + reader.getEncoding());
            SyndFeedInput input = new SyndFeedInput();
            // �õ�SyndFeed���󣬼��õ�RssԴ���������Ϣ   
            SyndFeed feed = input.build(reader);
            //System.out.println(feed);
            // �õ�Rss�����������б�   
            List entries = feed.getEntries();
            res = new String[entries.size()];
            // ѭ���õ�ÿ��������Ϣ   
            for (int i = 0; i < entries.size(); i++) {
                SyndEntry entry = (SyndEntry) entries.get(i);
                // ���⡢���ӵ�ַ�������顢ʱ����һ��RssԴ�����������ɲ���   
                System.out.println("���⣺" + entry.getTitle());
                res[i] = entry.getTitle();
                System.out.println("���ӵ�ַ��" + entry.getLink());
                SyndContent description = entry.getDescription();
                System.out.println("�����飺" + description.getValue());
                System.out.println("����ʱ�䣺" + entry.getPublishedDate());
                // ������RssԴ���ȵļ�������   
                System.out.println("��������ߣ�" + entry.getAuthor());
                // �˱��������ķ���   
                List categoryList = entry.getCategories();
                System.out.println(entry.getLink());//###############
                if (categoryList != null) {
                    for (int m = 0; m < categoryList.size(); m++) {
                        SyndCategory category = (SyndCategory) categoryList.get(m);
                        System.out.println("�˱��������ķ��룺" + category.getName());
                    }
                }
                // �õ���ý�岥���ļ�����Ϣ�б�   
                List enclosureList = entry.getEnclosures();
                if (enclosureList != null) {
                    for (int n = 0; n < enclosureList.size(); n++) {
                        SyndEnclosure enclosure = (SyndEnclosure) enclosureList.get(n);
                        System.out.println("��ý�岥���ļ���" + entry.getEnclosures());
                    }
                }
                System.out.println();
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}