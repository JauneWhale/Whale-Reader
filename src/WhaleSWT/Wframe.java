package WhaleSWT;

import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Wframe implements TitleListener{
	Shell shell;
	String t;
	int flag;
	Browser browser;
	Display display;
	public Wframe(String title,int height,int width){
		display = new Display();
		shell=new Shell(); 
        shell.setLayout(new FillLayout());
        shell.setText(title); 
        shell.setSize(width,height); 
        //Set the MenuBar
        new WMenuBar(shell);
        //Set the SashForm to make it able to judge the size
        SashForm form = new SashForm(shell, SWT.HORIZONTAL);
        form.setLayout(new FillLayout());
        //Set the RSS list
        
        new WRSSlist(form);
        //Set the Read Area
        browser=new Browser(form,SWT.FILL); 
        browser.setLayout(new FillLayout()); 
        browser.setUrl("d:\\javaworkspace\\WhaleReader\\Model.html");
        flag = 0;
        
        browser.addTitleListener(this);
        //browser.setUrl("www.baidu.com");
        //browser.execute(arg0)
        
        
        form.setWeights(new int[]{20,80});
	}
	private void open() {
		// TODO Auto-generated method stub
		shell.open();
		shell.layout();
        while (!shell.isDisposed()) { 
            if (!display.readAndDispatch()) 
            	display.sleep(); 
          } 
        display.dispose(); 
	} 
	public static void main(String args[]) 
    { 
        Wframe aWindow = new Wframe("Whale Reader",600,800);
        aWindow.open();
    }
	@Override
	public void changed(TitleEvent e) {
		// TODO Auto-generated method stub
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
	}
}
