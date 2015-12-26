package WhaleView;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

import javax.swing.*;

import WhaleData.RSSStore;
/**
 * Swing版本的view部分，已弃用
 * @author Administrator
 * @deprecated
 */
public class Wframe1 extends JFrame {
	int height,width;
	//Frame Class
    MenuBar mb;  
    WReadArea ReadArea;
    WRSSlist RSSList;
    WNewslist NewsList;
    Menu mFile;  
    MenuItem mNewSub, mNewFolder, mExit;  
    Container content;
    JSplitPane CPane;
    //Data Class
	RSSStore aData;
    private void WSetMenuBar(){
        mb = new MenuBar(); // 创建菜单栏MenuBar  
        mFile = new Menu("File");
        mNewSub = new MenuItem("New Subscription");  
        mNewFolder = new MenuItem("New Folder");
        mExit = new MenuItem("Exit Whale Reader");
          
        mFile.add(mNewSub);  
        mFile.add(mNewFolder);  
        // 加入分割线  
        mFile.addSeparator();  
        mFile.add(mExit);
        mb.add(mFile);
        this.setMenuBar(mb);
    }
	private void WSetRSSList(){
		//initialize the RSSList
		RSSList = new WRSSlist();
		//content.add(RSSList, BorderLayout.WEST);
	}
	private void WSetNewsList(){
		NewsList = new WNewslist();
	}
	private void WSetReadArea(){
		//Link to the URL and put them in the Read Area
		//initialize the Read Area
		ReadArea = new WReadArea();
		//content.add(RSSList, BorderLayout.WEST);
	}
    public Wframe1(String title,int height,int width){
    	super(title);
    	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    addWindowListener(new WindowAdapter() {
	    	public void windowClosing(WindowEvent e) {
	    		try {
					aData.SaveXML();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		System.exit(0);
	    	}
	    }
	    );
    	aData = new RSSStore(0);
    	this.height = height;
    	this.width = width;
		content = getContentPane();
		WSetMenuBar();
		WSetRSSList();
		WSetReadArea();
		WSetNewsList();
	    Dimension minimumSize = new Dimension(200,200);
	    RSSList.setMinimumSize(minimumSize);
	    CPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,false,RSSList,NewsList.getlist());
	    		//new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,NewsList.getlist(),ReadArea));
	    CPane.setOneTouchExpandable(true);
	    content.add(CPane);
    } 
    public static void main(String[] args) {
    	int height,width;
    	width = 640;
    	height = 480;
        Wframe1 aWindow = new Wframe1("Whale Reader",width,height);
        Toolkit theKit = aWindow.getToolkit();       // Get the window toolkit
	    Dimension wndSize = theKit.getScreenSize();  // Get screen size

		aWindow.setBounds(wndSize.width/4, wndSize.height/8,   // Position
	                      width, height);  // Size
	    //aWindow.
	    aWindow.setVisible(true); 
  }
}