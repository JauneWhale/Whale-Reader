package WhaleData;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * �������ʵҳ���html���ɵĴ�����
 * @author Administrator
 *
 */
public class Hhtml {
	public final static int GMARK = 0;
	public final static int MITEM = 1;
	public final static int ITEMS = 2;
	public final static int UMODEL = 3;
	public final static int HMODEL = 4;
	public final static int AMODEL = 5;
	public final static int INDEX = 6;
	/**
	 * ���ĺ�����������Ҫ���ɸ���html����������
	 * @param a Ҫ����html�Ķ�����
	 * @param mode ����ģʽ
	 */
	public static void CreateHtml(Object a,int mode){
		switch(mode){
		case GMARK:GroupMark((RSS)a); break; 		//Group Mark:�����ı�־
		case MITEM:ItemMark((RArticle)a); break; 	//Item Mark:�������ı�־
		case ITEMS:ItemHTML((RArticle)a); break; 	//Items:�������html����
		case INDEX:BuildIndex(); break; 			//Index:��ҳ������
		default:BuildModel((String)a,mode); break;	//����RSS���Ķ��б�����
		}
	}
	/**
	 * RSSԴ���ı�־
	 * @param r	ָ��RSSԴ
	 */
	private static void GroupMark(RSS r){
		try {
			StringBuffer strBuf = new StringBuffer();
			strBuf.append(r.RID);
            //HTML File Writer
            String  strBuf2 = new String(strBuf.toString());
            //      System.out.println(strBuf2);
            PrintWriter printWriter = new PrintWriter("html/gmark-"+r.RID+".html");
            printWriter.write(strBuf2.toString().toCharArray());
            printWriter.flush();
            printWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * �������ı�־
	 * @param a ָ������
	 */
	private static void ItemMark(RArticle a){
		FileInputStream fis=null;   
		FileOutputStream fos=null;       
		FileChannel in = null;
        FileChannel out = null;
		try { 
			BufferedReader head =new BufferedReader(
					new InputStreamReader(
						new FileInputStream(
							new File("Mark.html"))));
			StringBuffer strBuf = new StringBuffer();
	        for (String tmp1 = null; (tmp1 = head.readLine()) != null; tmp1 = null) {
	            // �滻����
	            String tmp =  new String(tmp1.toString());
	            tmp = tmp.replaceAll("whale-mark", "mark-"+a.UID);
	            strBuf.append(tmp);
	            strBuf.append(System.getProperty("line.separator"));
	        }
            String  strBuf2 = new String(strBuf.toString());
            PrintWriter printWriter = new PrintWriter("html/mark-"+a.UID+".html");
            printWriter.write(strBuf2.toString().toCharArray());
            printWriter.flush();
            printWriter.close();
			/*fis=new FileInputStream("html/"+a.UID+".html"); 
			fos=new FileOutputStream("html/mark-"+a.UID+".html");
	        in = fis.getChannel();//�õ���Ӧ���ļ�ͨ��
            out = fos.getChannel();//�õ���Ӧ���ļ�ͨ�� 
            in.transferTo(0, in.size(), out);//��������ͨ�������Ҵ�inͨ����ȡ��Ȼ��д��outͨ��
            fis.close();
            in.close();
            fos.close();
            out.close();
			StringBuffer strBuf = new StringBuffer();
			strBuf.append(a.UID);
            //HTML File Writer
            String  strBuf2 = new String(strBuf.toString());
            //      System.out.println(strBuf2);
            PrintWriter printWriter = new PrintWriter("html/mark-"+a.UID+".html");
            printWriter.write(strBuf2.toString().toCharArray());
            printWriter.flush();
            printWriter.close();*/
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * ���¼��html���������û������Ķ���Ҫ��Ϣ
	 * @param a ָ������
	 */
	private static void ItemHTML(RArticle a){
		try {
			//Head HTML reading
			BufferedReader head =new BufferedReader(
				new InputStreamReader(
					new FileInputStream(
						new File("Head.html"))));
			StringBuffer strBuf = new StringBuffer();
            for (String tmp1 = null; (tmp1 = head.readLine()) != null; tmp1 = null) {
                // �滻����
                String tmp =  new String(tmp1.toString());
                if(tmp.indexOf("whale-body") != -1){
                	tmp.replaceAll("whale-body", "");
                	break;
                }
                tmp = tmp.replaceAll("whale-htmltitle", "items-"+a.UID);
                //System.out.println(tmp);
                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }
            //Body HTML reading
			BufferedReader read =new BufferedReader(
					new InputStreamReader(
						new FileInputStream(
							new File("Read.html"))));
            for (String tmp1 = null; (tmp1 = read.readLine()) != null; tmp1 = null) {
                // �滻����
                String tmp =  new String(tmp1.toString());
                tmp = tmp.replaceAll("whale-Atitle", a.Title);
                tmp = tmp.replaceAll("whale-abstract", java.util.regex.Matcher.quoteReplacement(a.Abstract));
                tmp = tmp.replaceAll("whale-RSS", a.RSS.Title);
                tmp = tmp.replaceAll("whale-groupdate", a.date.toString());
                tmp = tmp.replaceAll("whale-link", a.URL);
                tmp = tmp.replaceAll("whale-ilink", a.RSS.Homepage_URL);
                tmp = tmp.replaceAll("whale-author", a.Author);
                tmp = tmp.replaceAll("whale-date", a.date.toString());
                //System.out.println(tmp);
                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }
            read.close();
            for (String tmp1 = null; (tmp1 = head.readLine()) != null; tmp1 = null) {
                // �滻����
                String tmp =  new String(tmp1.toString());
                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }
            head.close();
            //HTML File Writer
            String  strBuf2 = new String(strBuf.toString());
            PrintWriter printWriter = new PrintWriter("html/items-"+a.UID+".html");
            printWriter.write(strBuf2.toString().toCharArray());
            printWriter.flush();
            printWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e){
			e.printStackTrace();
			System.out.println(a.Abstract);
		}
	}
	/**
	 * ģ���Ķ����������ɶ�RSS���������ݽ��б�������Ľ���
	 * @param rid ָ��RSSԴ��id
	 * @param mode ָ����ʾģʽ���ֱ���δ�����Ѷ���ȫ��
	 */
	private static void BuildModel(String rid, int mode){
		try {
			RSS r = RSSStore.RS.get(rid);
			//Head HTML reading
			BufferedReader head =new BufferedReader(
				new InputStreamReader(
					new FileInputStream(
						new File("Head.html"))));
			StringBuffer strBuf = new StringBuffer();
            for (String tmp1 = null; (tmp1 = head.readLine()) != null; tmp1 = null) {
                // �滻����
                String tmp =  new String(tmp1.toString());
                if(tmp.indexOf("whale-body") != -1){
                	tmp.replaceAll("whale-body", "");
                	break;
                }
                tmp = tmp.replaceAll("whale-htmltitle", r.RID);
                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }
            
            //Item Around
			BufferedReader ir =new BufferedReader(
					new InputStreamReader(
						new FileInputStream(
							new File("ItemRound.html"))));
            for (String tmp1 = null; (tmp1 = ir.readLine()) != null; tmp1 = null) {
                // �滻����
                String tmp =  new String(tmp1.toString());
                if(tmp.indexOf("whale-body") != -1){
                	tmp.replaceAll("whale-body", "");
                	break;
                }
                tmp = tmp.replaceAll("whale-RSS", r.Title);
                tmp = tmp.replaceAll("whale-link", r.Homepage_URL);
                tmp = tmp.replaceAll("whale-gmark", System.getProperty("user.dir")+"\\html\\gmark-"+r.RID+".html");
                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }
            
            //Items
            List<RArticle> list = new  LinkedList<RArticle>();
            switch(mode){
            case UMODEL:list = RSSStore.GetUnreadNewsList(r); break;
            case HMODEL:list = RSSStore.GetReadNewsList(r); break;
            case AMODEL:list = RSSStore.GetAllNewsList(r); break;
            default: list = RSSStore.GetUnreadNewsList(r); break;
            }
            for(int i =0 ;i<list.size();i++){
    			BufferedReader items =new BufferedReader(
    					new InputStreamReader(
    						new FileInputStream(
    							new File("Items.html"))));
            	RArticle ra = list.get(i);
                for (String tmp1 = null; (tmp1 = items.readLine()) != null; tmp1 = null) {
                    // �滻����
                    String tmp =  new String(tmp1.toString());
                    tmp = tmp.replaceAll("whale-RSS", r.Title);
                    tmp = tmp.replaceAll("whale-title", ra.Title);
                    tmp = tmp.replaceAll("whale-author", ra.Author);
                    tmp = tmp.replaceAll("whale-date", ra.date.toString());
                    tmp = tmp.replaceAll("whale-link", r.Homepage_URL);
                    tmp = tmp.replaceAll("whale-show", "items-"+ra.UID+".html");
                    tmp = tmp.replaceAll("whale-readornot", (ra.ifread)?"read":"unread");
                    tmp = tmp.replaceAll("whale-mark", (ra.ifread)?"":("mark-"+ra.UID+".html"));
                    strBuf.append(tmp);
                    strBuf.append(System.getProperty("line.separator"));
                }
                items.close();
            }
            
            //Tail
            for (String tmp1 = null; (tmp1 = ir.readLine()) != null; tmp1 = null) {
                // �滻����
                String tmp =  new String(tmp1.toString());
                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }
            ir.close();
            for (String tmp1 = null; (tmp1 = head.readLine()) != null; tmp1 = null) {
                // �滻����
                String tmp =  new String(tmp1.toString());
                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }
            head.close();
            //HTML File Writer
            String  strBuf2 = new String(strBuf.toString());
            PrintWriter printWriter = new PrintWriter("html/Model.html");
            printWriter.write(strBuf2.toString().toCharArray());
            printWriter.flush();
            printWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * �½���ӭ�����棨ĿǰΪ��̬Index����ʱ���գ�
	 */
	private static void BuildIndex(){
		
	}
	/**
	 * ����Modelҳ�棬��Ҫ��Ӧ�����֮����Ƿ��Ķ����ж�
	 * @param uid	���µ�����id
	 * @param model	��ǰModel�Ĺ���RSS
	 */
	public static void UpdateModel(String uid, String model){
		RSS r = RSSStore.RS.get(model);
		RArticle ua = r.AllA.get(uid);
		//HTML reading
		BufferedReader html;
		BufferedReader hr;
		String t;
		try {
			hr = new BufferedReader(
					new InputStreamReader(
						new FileInputStream(
							new File("Haveread.html"))));
			t = hr.readLine().toString();
			t = t.replaceAll("whale-show", "items-"+ua.UID+".html");
			t = t.replaceAll("whale-title", ua.Title);
			t = t.replaceAll("whale-author", ua.Author);
			hr.close();
			html = new BufferedReader(
				new InputStreamReader(
					new FileInputStream(
						new File("html\\Model.html"))));
			StringBuffer strBuf = new StringBuffer();
			for (String tmp1 = null; (tmp1 = html.readLine()) != null; tmp1 = null) {
				// �滻����
				String tmp =  new String(tmp1.toString());
				if(tmp.indexOf(uid) != -1){
					tmp = t;
				}
				strBuf.append(tmp);
				strBuf.append(System.getProperty("line.separator"));
			}
            html.close();
            //HTML File Writer
            String  strBuf2 = new String(strBuf.toString());
            PrintWriter printWriter = new PrintWriter("html/Model.html");
            printWriter.write(strBuf2.toString().toCharArray());
            printWriter.flush();
            printWriter.close();
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * ɾ����������ļ��Լ�����ļ�
	 * @param r ָ��������
	 */
	public static void RemovItem(String UID){
		File fs =new File("html/"+UID+".html");
		if(fs!=null) fs.delete();
		fs =new File("html/mark-"+UID+".html");
		if(fs!=null) fs.delete();
	}
	/**
	 * ɾ��RSS������б��Լ�����ļ�
	 * @param RID ָ��RSS�б�
	 */
	public static void RemovRSS(String RID){
		File fs =new File("html/"+RID+".html");
		if(fs!=null) fs.delete();
		fs =new File("html/gmark-"+RID+".html");
		if(fs!=null) fs.delete();
	}
	/**
	 * �ļ���������ɾ����־�ļ��ã������������ã�������
	 * @author Administrator
	 * @deprecated
	 */
	private class MyFilenameFilter implements FilenameFilter {  
	    private Pattern p;  
	    public MyFilenameFilter(String regex) {  
	        p = Pattern.compile(regex);  
	    }  
	    public boolean accept(File file, String name) {  
	        return p.matcher(name).matches();  
	    }  
	} 
	/**
	 * ɾ�����б�־�ļ����������ɱ����Ʒ���Ϊ�������ɣ�������
	 * @deprecated
	 */
	public void NormalRemoveMark(){
		File directory = new File(System.getProperty("user.dir")+"\\html");
		File[] file = directory.listFiles(new MyFilenameFilter("*mark+"));
		for(int i=0; i<file.length;i++){
			file[i].delete();
		}
	}
	/*���Ե�Ԫ����
	public static void main(String[] args){
		RSSStore s = new RSSStore(0);
		Hhtml.CreateHtml(s.r,Hhtml.ITEMS);
	}*/
}
