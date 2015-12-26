package WhaleSWT;

import java.io.IOException;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import WhaleData.Hhtml;
import WhaleData.RSSStore;

/**
 * 程序主窗口函数，用于所有最初始的建立
 * @author Administrator
 *
 */
public class Wframe implements TitleListener{
	/**
	 * 驱动函数
	 * @param args
	 */
	public static void main(String args[]) 
    { 
        Wframe aWindow = new Wframe("Whale Reader",600,800);
        aWindow.open();
    }
	static String Model;
	static String freshBrowser;
	static Shell shell;
	String t;
	int flag;
	Browser browser;
	Display display;
	RSSStore aData;
	/**
	 * 唯一的构造函数，指定窗口的标题，高度和宽度
	 * @param title 窗口标题
	 * @param height 高度
	 * @param width 宽度
	 */
	public Wframe(String title,int height,int width){
    	Locale.setDefault(Locale.ENGLISH);
		freshBrowser = null;
    	aData = new RSSStore(0);
		display = new Display();
		shell=new Shell(display); 
        shell.setLayout(new FillLayout());
        shell.setText(title); 
        shell.setSize(width,height); 
        //Set the MenuBar
        //new WMenuBar(shell);
        //Set the SashForm to make it able to judge the size
        SashForm form = new SashForm(shell, SWT.HORIZONTAL);
        form.setLayout(new FillLayout());
        //Set the RSS list
        new WRSSlist(form);
        //Set the Read Area
        browser=new Browser(form,SWT.FILL); 
        browser.setLayout(new FillLayout()); 
        browser.setUrl(System.getProperty("user.dir")+"\\html\\Index.html");
        flag = 0;
        browser.addTitleListener(this);
        form.setWeights(new int[]{20,80});
	}
	/**
	 * 窗口执行类，调用后开始打开窗口，同时监听事件。
	 * 主要监听RSS浏览数点击时所导致的浏览器窗口更新的监听情况。
	 */
	private void open() {
		// TODO Auto-generated method stub
		int k;
		String mark1 = null;
		shell.open();
		shell.layout();
        while (!shell.isDisposed()) { 
            if (!display.readAndDispatch()) 
            	display.sleep(); 
            if(!browser.isDisposed()){
                String mark = browser.getUrl();
                if(!mark.equals(mark1)){
                	System.out.println(mark);
                	mark1 = mark;
                }
            }
            if(freshBrowser!=null){
            	browser.setUrl(freshBrowser);
            	freshBrowser=null;
            }
          }
        try {
			aData.SaveXML();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        display.dispose(); 
	} 
	/**	
	 * 提供给浏览器使用的标题监听器，主要监听当前有无标签的跳转产生的“已阅读”的事件
	 */
	@Override
	public void changed(TitleEvent e) {
		// TODO Auto-generated method stub
		int k;
		if(flag==0){
			t = e.title;
			flag=1;
			System.out.println(e.title);
		}
		if(flag==1){
			t = e.title;
			flag=1;
			System.out.println(e.title);
		}
		if(!t.equals(e.title)){
			System.out.println(e.title);
		}
    	if((k = t.indexOf("mark-"))!=-1){
			String a = t.substring(k+5, t.length());
			WRSSlist.MarkDecrease();
			Hhtml.UpdateModel(a,Model);
			RSSStore.MarkReadfromXML(a,Model);
			freshBrowser = System.getProperty("user.dir")+"\\html\\Model.html";
    	}
    	if((k = t.indexOf("items-"))!=-1){
			String a = t.substring(k+6, t.length());
			WRSSlist.MarkDecrease();
			Hhtml.UpdateModel(a,Model);
			RSSStore.MarkReadfromXML(a,Model);
    	}
	}
}
