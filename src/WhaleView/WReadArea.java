package WhaleView;

import javax.swing.Box;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.*;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
/**
 * Swing版本的view部分，已弃用
 * @author Administrator
 * @deprecated
 */
public class WReadArea extends Box implements HyperlinkListener{
	public JTextField Content;
	JEditorPane editorPane = new JEditorPane();
	public WReadArea(){
		super(0);
		String path = "http://m.baidu.com/";
		editorPane.setContentType("text/html");
		editorPane.setEditable(false);     //请把editorPane设置为只读，不然显示就不整齐
		try{
			editorPane.setPage(new URL(path));
			editorPane.addHyperlinkListener(this);    //让我们的主体类实现了HyperlinkListener接口
		}catch(Exception e){
			System.out.println("Error:"+e);
		}
		JScrollPane scrollPane = new JScrollPane(editorPane);
		this.add(scrollPane, BorderLayout.CENTER);
	}
	@Override
	public void hyperlinkUpdate(HyperlinkEvent e){
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED){  
			JEditorPane pane = (JEditorPane) e.getSource();  
			if (e instanceof HTMLFrameHyperlinkEvent){
				HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;  
				HTMLDocument doc = (HTMLDocument) pane.getDocument();  
				doc.processHTMLFrameHyperlinkEvent(evt);  
		    }  
			else{  
	            try{  
		            pane.setPage(e.getURL());  
		        }  
		        catch (Throwable t){  
		        	t.printStackTrace();  
		        }  
		    }  
		}
	}
}


/*
public class WReadArea extends Box{
	private TabList head;
	private JTabbedPane tabbedPane;
	public WReadArea(){
		//Read some file and link the URLs
		super(0);
		tabbedPane = new JTabbedPane();
		head = new TabList();
		TabList tmp = head;
		while(tmp!=null){
			WaddTab(tmp);
			tmp = tmp.next;
		}
		this.add(tabbedPane);
	}
    protected Component makeTextPanel(String text) {  
        JPanel panel = new JPanel(false);  
        JLabel filler = new JLabel(text);  
        filler.setHorizontalAlignment(JLabel.CENTER);  
        panel.setLayout(new GridLayout(1, 1));  
        panel.add(filler);  
        return panel;  
    }  
	protected static ImageIcon createImageIcon(String path) {  
        // java.net.URL imgURL = TabbedPaneDemo.class.getResource(path);  
        if (path != null) {  
            return new ImageIcon(path);  
        } else {  
            System.out.println("Couldn't find file: " + path);  
            return null;  
        }  
    }
	private void WaddTab(TabList tmp){
		tabbedPane.addTab(tmp.title, tmp.icon, tmp.tab);
	}
	class TabList{
		public Component tab;
		public TabList next;
		public ImageIcon icon;
		public String title;
		public TabList(){
			icon = createImageIcon("images/MyIcon.gif");  
	        JTabbedPane tabbedPane = new JTabbedPane();
	        String title = "Test1";//readfile
	        tab = makeTextPanel(title);
	        next = null;
		}
	}
}
*/