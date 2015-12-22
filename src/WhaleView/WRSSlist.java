package WhaleView;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.tree.*;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import javax.swing.*;
import WhaleData.*;

public class WRSSlist extends Box{
	private DefaultTreeModel model; 
	private JTree Wtree; 
	private JButton addSiblingButton; 
	private JButton addChildButton; 
	private JButton deleteButton; 
	private JButton editButton; 
	private RSSStore data; 
	public WRSSlist() {
		super(0);
		data = new RSSStore(1);
		TreeNode root = MakeSourceTree();
		model = new DefaultTreeModel(root,true); 
		Wtree = new JTree(model); 
		DefaultTreeCellRenderer renderer=new DefaultTreeCellRenderer(); 
		renderer.setLeafIcon(new ImageIcon("1.gif")); 
		renderer.setClosedIcon(new ImageIcon("2.gif")); 
		renderer.setOpenIcon(new ImageIcon("3.gif")); 
		//renderer.setBackgroundNonSelectionColor(Color.BLUE); 
		renderer.setBackgroundSelectionColor(Color.BLUE); 
		renderer.setBorderSelectionColor(Color.BLUE); 
		Wtree.setCellRenderer(renderer); 
		// add scroll pane with tree to content pane 
		JScrollPane scrollPane = new JScrollPane(Wtree); 
		scrollPane.getVerticalScrollBar();
		scrollPane.getHorizontalScrollBar();
		this.add(scrollPane); 
	}
	public TreeNode MakeSourceTree() { 
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(" ");
		DefaultMutableTreeNode DefRoot = new DefaultMutableTreeNode("Subscriptions"); 
		DefaultMutableTreeNode WatRoot = new DefaultMutableTreeNode("Watches");
		DefaultMutableTreeNode TagRoot = new DefaultMutableTreeNode("Tags");
		root.add(DefRoot);
		root.add(WatRoot); 
		root.add(TagRoot); 
		BuildDefTree(DefRoot);
		BuildWatTree(DefRoot);
		BuildTagTree(DefRoot);
		return root; 
	}
	public void BuildDefTree(DefaultMutableTreeNode DefRoot){
		DefaultMutableTreeNode RSS;
        Iterator i = data.RS.entrySet().iterator();
        if(!i.hasNext()){
			RSS = new DefaultMutableTreeNode("Add new Subscriptions now");
			RSS.setAllowsChildren(false);
			DefRoot.add(RSS); 
        }
        while(i.hasNext()){
            Entry<String, WhaleData.RSS> entry = (Entry<String, RSS>) i.next();
			RSS r = (RSS) entry.getValue();
            String print;
            if(r.UnReadNum!=0){
            	print = r.Title + "(" + r.UnReadNum+")";
            }
            else{
            	print = r.Title;
            }
			RSS = new DefaultMutableTreeNode(print);
			RSS.setAllowsChildren(false);
			DefRoot.add(RSS); 
		}
	}
	public void BuildWatTree(DefaultMutableTreeNode WatRoot){
		
	}
	public void BuildTagTree(DefaultMutableTreeNode TagRoot){
		
	}
	/*public TreeNode makeSampleTree() { 
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("World"); 
		DefaultMutableTreeNode country = new DefaultMutableTreeNode("USA"); 
		root.add(country); 
		DefaultMutableTreeNode state = new DefaultMutableTreeNode("California"); 
		country.add(state); 
		DefaultMutableTreeNode city = new DefaultMutableTreeNode("San Jose"); 
		state.add(city); 
		city = new DefaultMutableTreeNode("Cupertino"); 
		state.add(city); 
		state = new DefaultMutableTreeNode("Michigan"); 
		country.add(state); 
		city = new DefaultMutableTreeNode("Ann Arbor"); 
		state.add(city); 
		country = new DefaultMutableTreeNode("Germany"); 
		root.add(country); 
		state = new DefaultMutableTreeNode("Schleswig-Holstein"); 
		country.add(state); 
		city = new DefaultMutableTreeNode("Kiel"); 
		state.add(city); 
		return root; 
	}*/
}
