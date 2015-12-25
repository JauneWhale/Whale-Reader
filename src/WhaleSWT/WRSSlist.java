package WhaleSWT;


import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT; 
import org.eclipse.swt.custom.TreeEditor; 
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import WhaleData.*;
import WhaleHtml.Hhtml;

public class WRSSlist{ 
	public static Point point;
	public static List<String> rlist;
	public static void main(String[] args) {
	}
	public WRSSlist(Composite shell){
	   RSSStore adata = new RSSStore(1);
       // 创建一个树对象
       Tree tree = new Tree(shell, SWT.PUSH);
       FormData formData = new FormData();
       formData.top = new FormAttachment(0, 25);
       formData.left = new FormAttachment(0, 30);
       formData.right = new FormAttachment(0, 60);
       tree.setLayoutData(formData);
       // 创建树的RSS节点 
       TreeItem root = new TreeItem(tree, SWT.NULL); 
       root.setText("Subscriptions"); 
       // 创建RSS source节点 
       rlist = adata.BuildDefTree();
       Iterator<String> i = rlist.iterator();
       while(i.hasNext()){
    	   TreeItem rss = new TreeItem(root, SWT.NULL);
    	   rss.setText(i.next());
       }
       // 调用convertImage方法来设置树的图标 
       convertImage(tree); 
       // Right Click Menu
       addRMenu(tree);
       tree.setLinesVisible(false); 
       tree.setHeaderVisible(false); 
	} 

   // 设置树图标的方法 
	public static void convertImage(Tree tree) { 
       // 这里假设只有一个根节点 
       TreeItem[] items = tree.getItems();
       for(int i=0; i<items.length; i++)
    	   setChildImage(items[i]); 
	} 

   // 设置一个节点的方法，该方法非常重要，要理解该方法的递归用法 
   // 参数item可以把单独看作是树中的某一个TreeItem 
	public static void setChildImage(TreeItem item) { 
       // 首先获得该TreeItem的所有子TreeItem 
       TreeItem[] items = item.getItems(); 
       // 循环每一个TreeItem 
       for (int i = 0; i < items.length; i++) { 
           setChildImage(items[i]); 
       } 
	} 
	public static void addRMenu(Tree tree){
	   tree.addListener(SWT.MouseUp, new Listener() {  
		    @Override  
		    public void handleEvent(Event event) { 
		    	if(event.button != 1)
		    		point = new Point(event.x, event.y);  //获取当前点击的坐标点.  
		    	else{
		    		Point p = new Point(event.x,event.y);
		    		TreeItem item;
		    		try{
		    			item = tree.getItem(p);
		    		}catch(Exception e){
		    			return;
		    		}
		    		if(item==null) return;
		    		if(item.getText().indexOf("Subscriptions")!=-1)
		    			return;
		    		String s = WOperate.Decode(item.getText());
	            	Hhtml.CreateHtml(s, Hhtml.UMODEL);
	            	Wframe.freshBrowser = System.getProperty("user.dir")+"\\html\\Model.html";
		    	}
		    }  
		});  
       Menu menu=new Menu(tree);
       MenuItem newItem=new MenuItem(menu,SWT.PUSH);
       newItem.setText("Add New Subscription");
       
       newItem.addSelectionListener(new SelectionAdapter() {
           @Override
           public void widgetSelected(SelectionEvent e) {
        	   InputDialog inputDialog = new InputDialog(Wframe.shell,"标题","提示信息","默认值",null);
        	   //InputDialog i = new InputDialog(null, null, null, null, null);
        	   if(inputDialog.open() == InputDialog.OK){
        	       String value = inputDialog.getValue();
        	   }
        	   TreeItem item = tree.getItem(point);    //通过坐标点获取节点.  
		       new WOperate(e.getSource().toString(),item.getText());
           }
       });
       MenuItem markItem=new MenuItem(menu, SWT.PUSH);
       markItem.setText("Mark all Read");
       markItem.addSelectionListener(new SelectionAdapter() {
           @Override
           public void widgetSelected(SelectionEvent e) {
        	   TreeItem item = tree.getItem(point);    //通过坐标点获取节点.  
        	   if(item.getText().indexOf("Subscriptions")!=-1){
        		   MessageBox dialog=new MessageBox(Wframe.shell,SWT.OK|SWT.CANCEL|SWT.ICON_QUESTION);
        		   dialog.setText("Warning");
        		   dialog.setMessage("Mark ALL feeds Read?");
        		   if(dialog.open()==SWT.CANCEL) return;
        	   }else{
        		   MessageBox dialog=new MessageBox(Wframe.shell,SWT.OK|SWT.CANCEL|SWT.ICON_QUESTION);
        		   dialog.setText("Warning");
        		   dialog.setMessage("Mark ALL Read?");
        		   if(dialog.open()==SWT.CANCEL) return;
        	   }
        	   WOperate wo = new WOperate(e.getSource().toString(),item.getText());
        	   if(item.getText().indexOf("Subscriptions")!=-1){
        	       Iterator<String> i = rlist.iterator();
        	       int j = 0;
        	       while(i.hasNext()){
        	    	   TreeItem rss = item.getItem(j++);
        	    	   String s = i.next();
        	    	   int k = s.indexOf('(');
        	    	   if(k!=-1) s = s.substring(0, k);
        	    	   rss.setText(s);
        	       }
        	   }
        	   else
        		   item.setText(wo.r.Title);
           }
       });
       
       new MenuItem(menu, SWT.SEPARATOR);
       
       MenuItem UpdateItem=new MenuItem(menu,SWT.PUSH);
       UpdateItem.setText("Update");
       UpdateItem.addSelectionListener(new SelectionAdapter() {
           @Override
           public void widgetSelected(SelectionEvent e) {
        	   TreeItem item = tree.getItem(point);    //通过坐标点获取节点.  
        	   String op = e.getSource().toString();
		       WOperate wo = new WOperate(op,item.getText());
        	   if(item.getText().indexOf("Subscriptions")!=-1){
        		   RSSStore adata = new RSSStore(1);
        	       List<String> rlist = adata.BuildDefTree();
        	       Iterator<String> i = rlist.iterator();
        	       int j = 0;
        	       while(i.hasNext()){
        	    	   TreeItem rss = item.getItem(j++);
        	    	   rss.setText(i.next());
        	       }
        	   }
        	   else
        		   item.setText(wo.r.Title + "(" + wo.r.UnReadNum+")");
           }
       });
       MenuItem deleteItem=new MenuItem(menu, SWT.PUSH);
       deleteItem.setText("Delete");
       
       deleteItem.addSelectionListener(new SelectionAdapter() {
           @Override
           public void widgetSelected(SelectionEvent e) {
		       TreeItem item = tree.getItem(point);    //通过坐标点获取节点.  
        	   new WOperate(e.getSource().toString(),item.getText());
        	   if(item.getText().indexOf("Subscriptions")!=-1){
        		   MessageBox dialog=new MessageBox(Wframe.shell,SWT.OK|SWT.CANCEL|SWT.ICON_INFORMATION);
        		   dialog.setText("Warning");
        		   dialog.setMessage("Remove ALL feed?");
        		   if(dialog.open()==SWT.CANCEL) return;
        		   item.removeAll();
        	   }
        	   else{
        		   MessageBox dialog=new MessageBox(Wframe.shell,SWT.OK|SWT.CANCEL|SWT.ICON_INFORMATION);
        		   dialog.setText("Warning");
        		   dialog.setMessage("Remove this feed?");
        		   if(dialog.open()==SWT.CANCEL) return;
        		   TreeItem t = item.getParentItem();
        		   t.removeAll();
        		   RSSStore adata = new RSSStore(1);
        	       List<String> rlist = adata.BuildDefTree();
        	       Iterator<String> i = rlist.iterator();
        	       int j = 0;
        	       while(i.hasNext()){
        	    	   TreeItem rss = t.getItem(j++);
        	    	   rss.setText(i.next());
        	       }
        	   }
           }
       });
       tree.setMenu(menu);
	}
} 
