package WhaleView;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import WhaleData.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
public class WNewslist implements ListSelectionListener{
	JList list;
	RSSParse tmp;
	int rows;
	public WNewslist(){
		super();
		RSSStore data = new RSSStore(1);
		List<RArticle> l = data.GetNewslist();
	    DefaultListModel listModel = new DefaultListModel();

    	//Box b = Box.createHorizontalBox();
    	//b.add(new JLabel(u.Title));
	    for(int i=0; i<l.size(); i++)
	    	listModel.addElement(l.get(i));

	    list = new JList(listModel);
	    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    list.addListSelectionListener(this);
	    list.setCellRenderer( new CustomListCellRenderer() );
	}
	public JScrollPane getlist(){
		JScrollPane res = new JScrollPane(list); 
		res.setHorizontalScrollBarPolicy(res.HORIZONTAL_SCROLLBAR_NEVER);
		return res;
	}
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
	}
    class CustomListCellRenderer implements ListCellRenderer{
    	public Component getListCellRendererComponent(
    		JList list,
 			Object value,
 			int index,
 			boolean isSelected,
 			boolean cellHasFocus
 		){
    		RArticle ua = (RArticle)value;
    		Box b = Box.createVerticalBox();
    		b.setBorder(new BevelBorder(1));
    		//b.setSize(arg0, arg1);;
    		//b.add(new JButton(ua.Title));
    		JLabel title = new JLabel("<html><a href=aaa>"+ua.Title+"</a></html>");
    		title.addMouseListener( new MouseAdapter(){//添加鼠标事件侦听器,当单击标签时,打开网页
    			   public void mouseClicked( MouseEvent e ){
    			}
    		});
    		JButton expand = new JButton("△");
    		expand.addActionListener(new ActionListener(){
    			public void actionPerformed( ActionEvent e){
    				System.out.println("Haha");
    			}
    		});
    		b.add(expand);
    		b.add(title);
    		b.add(new JLabel(ua.Author));
    		return b;
    		//return new JButton( (String)value );
   	 	}
    }
}
