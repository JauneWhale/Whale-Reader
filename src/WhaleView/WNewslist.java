package WhaleView;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import WhaleData.*;

import javax.swing.*;
import java.awt.*;
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
		DefaultListModel listModel = data.GetNewslist();
	    list = new JList(listModel);
	    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    list.addListSelectionListener(this);
	}
	public JScrollPane getlist(){
		JScrollPane res = new JScrollPane(list); 
		return res;
	}
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
	}
}
