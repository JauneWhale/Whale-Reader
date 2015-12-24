package WhaleHtml;

import WhaleData.*;
import java.io.*;

public class Hhtml {
	//String head;
	String readArea;
	String items;
	final static int GMARK = 0;
	final static int MITEM = 1;
	final static int ITEMS = 2;
	public Hhtml(Object a,int mode){
		switch(mode){
		case GMARK:GroupMark((RSS)a); break;
		case MITEM:ItemMark((RArticle)a); break;
		case ITEMS:ItemHTML((RArticle)a); break;
		}
	}
	private void GroupMark(RSS r){
		try {
			BufferedReader head =new BufferedReader(
					new InputStreamReader(
							new FileInputStream(
									new File("Head.html"))));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void ItemMark(RArticle a){
		try {
			BufferedReader head =new BufferedReader(
					new InputStreamReader(
							new FileInputStream(
									new File("Head.html"))));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void ItemHTML(RArticle a){
		try {
			//Head HTML reading
			BufferedReader head =new BufferedReader(
				new InputStreamReader(
					new FileInputStream(
						new File("Head.html"))));
			StringBuffer strBuf = new StringBuffer();
            for (String tmp1 = null; (tmp1 = head.readLine()) != null; tmp1 = null) {
                // Ìæ»»²Ù×÷
                String tmp =  new String(tmp1.toString().getBytes("UTF-8"));
                if(tmp.indexOf("whale-body")==-1) break;
                tmp = tmp.replaceAll("whale-htmltitle", a.UID);
                System.out.println(tmp);
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
                String tmp =  new String(tmp1.toString().getBytes("UTF-8"));
                if(tmp.indexOf("whale-body")==-1) break;
                tmp = tmp.replaceAll("whale-abstract", a.Abstract);
                tmp = tmp.replaceAll("whale-RSS", a.RSS);
                tmp = tmp.replaceAll("whale-groupdate", a.date.toString());
                tmp = tmp.replaceAll("whale-link", a.URL);
                tmp = tmp.replaceAll("whale-ilink", );
                System.out.println(tmp);
                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }
            
            head.close();
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
}
