package WhaleData;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 处理对现实页面的html生成的处理类
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
	 * 核心函数，处理需要生成各种html的驱动函数
	 * @param a 要生成html的对象类
	 * @param mode 生成模式
	 */
	public static void CreateHtml(Object a,int mode){
		switch(mode){
		case GMARK:GroupMark((RSS)a); break; 		//Group Mark:组已阅标志
		case MITEM:ItemMark((RArticle)a); break; 	//Item Mark:文章已阅标志
		case ITEMS:ItemHTML((RArticle)a); break; 	//Items:文章浏览html生成
		case INDEX:BuildIndex(); break; 			//Index:主页的生成
		default:BuildModel((String)a,mode); break;	//各种RSS的阅读列表生成
		}
	}
	/**
	 * RSS源已阅标志
	 * @param r	指定RSS源
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
	 * 文章已阅标志
	 * @param a 指定文章
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
	            // 替换操作
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
	        in = fis.getChannel();//得到对应的文件通道
            out = fos.getChannel();//得到对应的文件通道 
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
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
	 * 文章简介html，可以让用户快速阅读主要信息
	 * @param a 指定文章
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
                // 替换操作
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
                // 替换操作
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
                // 替换操作
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
	 * 模块阅读，可以生成对RSS内所有内容进行标题浏览的界面
	 * @param rid 指定RSS源的id
	 * @param mode 指定显示模式，分别是未读、已读、全部
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
                // 替换操作
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
                // 替换操作
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
                    // 替换操作
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
                // 替换操作
                String tmp =  new String(tmp1.toString());
                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }
            ir.close();
            for (String tmp1 = null; (tmp1 = head.readLine()) != null; tmp1 = null) {
                // 替换操作
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
	 * 新建欢迎主界面（目前为静态Index，暂时留空）
	 */
	private static void BuildIndex(){
		
	}
	/**
	 * 更新Model页面，主要是应付点击之后的是否阅读的判断
	 * @param uid	更新的文章id
	 * @param model	当前Model的工作RSS
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
				// 替换操作
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
	 * 删除文章浏览文件以及标记文件
	 * @param r 指定文章类
	 */
	public static void RemovItem(String UID){
		File fs =new File("html/"+UID+".html");
		if(fs!=null) fs.delete();
		fs =new File("html/mark-"+UID+".html");
		if(fs!=null) fs.delete();
	}
	/**
	 * 删除RSS的浏览列表以及标记文件
	 * @param RID 指定RSS列表
	 */
	public static void RemovRSS(String RID){
		File fs =new File("html/"+RID+".html");
		if(fs!=null) fs.delete();
		fs =new File("html/gmark-"+RID+".html");
		if(fs!=null) fs.delete();
	}
	/**
	 * 文件过滤器，删除标志文件用，由于生成永久，故弃用
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
	 * 删除所有标志文件，由于生成标记设计方便为永久生成，故弃用
	 * @deprecated
	 */
	public void NormalRemoveMark(){
		File directory = new File(System.getProperty("user.dir")+"\\html");
		File[] file = directory.listFiles(new MyFilenameFilter("*mark+"));
		for(int i=0; i<file.length;i++){
			file[i].delete();
		}
	}
	/*测试单元函数
	public static void main(String[] args){
		RSSStore s = new RSSStore(0);
		Hhtml.CreateHtml(s.r,Hhtml.ITEMS);
	}*/
}
