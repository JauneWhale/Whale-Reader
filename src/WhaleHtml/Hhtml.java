package WhaleHtml;

import WhaleData.*;
import java.io.*;
import java.util.Iterator;
import java.util.Map.Entry;

public class Hhtml {
	//String head;
	String readArea;
	String items;
	public final static int GMARK = 0;
	public final static int MITEM = 1;
	public final static int ITEMS = 2;
	public final static int UMODEL = 3;
	public final static int HMODEL = 4;
	public final static int AMODEL = 5;
	public final static int INDEX = 6;
	public static void main(String[] args){
		RSSStore s = new RSSStore(0);
		Hhtml.CreateHtml(s.r,Hhtml.ITEMS);
	}
	/**
	 * 
	 * @param a
	 * @param mode
	 */
	public static void CreateHtml(Object a,int mode){
		switch(mode){
		case GMARK:GroupMark((RSS)a); break;
		case MITEM:ItemMark((RArticle)a); break;
		case ITEMS:ItemHTML((RArticle)a); break;
		case INDEX:BuildIndex(); break;
		default:BuildModel((String)a,mode); break;
		}
	}
	/**
	 * 
	 * @param r
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
	 * 
	 * @param a
	 */
	private static void ItemMark(RArticle a){
		try {
			StringBuffer strBuf = new StringBuffer();
			strBuf.append(a.UID);
            //HTML File Writer
            String  strBuf2 = new String(strBuf.toString());
            //      System.out.println(strBuf2);
            PrintWriter printWriter = new PrintWriter("html/mark-"+a.UID+".html");
            printWriter.write(strBuf2.toString().toCharArray());
            printWriter.flush();
            printWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private static void ItemHTML(RArticle a){
		try {
			//Head HTML reading
			BufferedReader head =new BufferedReader(
				new InputStreamReader(
					new FileInputStream(
						new File("Head.html"))));
			StringBuffer strBuf = new StringBuffer();
            for (String tmp1 = null; (tmp1 = head.readLine()) != null; tmp1 = null) {
                // Ìæ»»²Ù×÷
                String tmp =  new String(tmp1.toString());
                if(tmp.indexOf("whale-body") != -1){
                	tmp.replaceAll("whale-body", "");
                	break;
                }
                tmp = tmp.replaceAll("whale-htmltitle", a.UID);
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
                // Ìæ»»²Ù×÷
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
                // Ìæ»»²Ù×÷
                String tmp =  new String(tmp1.toString());
                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }
            head.close();
            //HTML File Writer
            String  strBuf2 = new String(strBuf.toString());
            PrintWriter printWriter = new PrintWriter("html/"+a.UID+".html");
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
	 * 
	 * @param r
	 */
	private static void BuildModel(String rid, int mode){
		try {
			RSSStore adata = new RSSStore(1);
			RSS r = adata.RS.get(rid);
			//Head HTML reading
			BufferedReader head =new BufferedReader(
				new InputStreamReader(
					new FileInputStream(
						new File("Head.html"))));
			StringBuffer strBuf = new StringBuffer();
            for (String tmp1 = null; (tmp1 = head.readLine()) != null; tmp1 = null) {
                // Ìæ»»²Ù×÷
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
                // Ìæ»»²Ù×÷
                String tmp =  new String(tmp1.toString());
                if(tmp.indexOf("whale-body") != -1){
                	tmp.replaceAll("whale-body", "");
                	break;
                }
                tmp = tmp.replaceAll("whale-RSS", r.Title);
                tmp = tmp.replaceAll("whale-gmark", System.getProperty("user.dir")+"\\html\\gmark-"+r.RID+".html");
                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }
            
            //Items
            Iterator i;
            switch(mode){
            case UMODEL:i = r.UnreadA.entrySet().iterator(); break;
            case HMODEL:i = r.HavereadA.entrySet().iterator(); break;
            case AMODEL:i = r.AllA.entrySet().iterator(); break;
            default: i = r.AllA.entrySet().iterator(); break;
            }
            while(i.hasNext()){
    			BufferedReader items =new BufferedReader(
    					new InputStreamReader(
    						new FileInputStream(
    							new File("Items.html"))));
            	RArticle ra = (RArticle)((Entry)i.next()).getValue();
                for (String tmp1 = null; (tmp1 = items.readLine()) != null; tmp1 = null) {
                    // Ìæ»»²Ù×÷
                    String tmp =  new String(tmp1.toString());
                    tmp = tmp.replaceAll("whale-RSS", r.Title);
                    tmp = tmp.replaceAll("whale-title", ra.Title);
                    tmp = tmp.replaceAll("whale-author", ra.Author);
                    tmp = tmp.replaceAll("whale-date", ra.date.toString());
                    tmp = tmp.replaceAll("whale-link", r.Homepage_URL);
                    tmp = tmp.replaceAll("whale-show", ra.UID+".html");
                    tmp = tmp.replaceAll("whale-readornot", (ra.ifread)?"read":"unread");
                    tmp = tmp.replaceAll("whale-mark", System.getProperty("user.dir")+"\\html\\mark-"+ra.UID+".html");
                    strBuf.append(tmp);
                    strBuf.append(System.getProperty("line.separator"));
                }
                items.close();
            }
            
            //Tail
            for (String tmp1 = null; (tmp1 = ir.readLine()) != null; tmp1 = null) {
                // Ìæ»»²Ù×÷
                String tmp =  new String(tmp1.toString());
                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }
            ir.close();
            for (String tmp1 = null; (tmp1 = head.readLine()) != null; tmp1 = null) {
                // Ìæ»»²Ù×÷
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
	private static void BuildIndex(){
		
	}
	public static void RemovItem(RArticle r){
		File fs =new File("html/"+r.UID+".html");
		if(fs!=null) fs.delete();
		fs =new File("html/mark-"+r.UID+".html");
		if(fs!=null) fs.delete();
	}
}
