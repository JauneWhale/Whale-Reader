package WhaleSWT;


import java.util.Iterator;
import java.util.List;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT; 
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import WhaleData.*;

public class WRSSlist{ 
	public static Point point;
	public static List<String> rlist;
	public static TreeItem root;
	public static Tree tree;
	public static TreeItem preitem;
	public static void main(String[] args) {
	}
	/**
	 * 构造函数：建立RSS浏览树
	 * @param shell 要生成浏览数的指定窗口shell
	 */
	public WRSSlist(Composite shell){
	   RSSStore adata = new RSSStore(1);
       // 创建一个树对象
       tree = new Tree(shell, SWT.PUSH);
       FormData formData = new FormData();
       formData.top = new FormAttachment(0, 25);
       formData.left = new FormAttachment(0, 30);
       formData.right = new FormAttachment(0, 60);
       tree.setLayoutData(formData);
       // 创建树的RSS节点 
       root = new TreeItem(tree, SWT.NULL); 
       root.setText("Subscriptions"); 
       // 创建RSS source节点 
       rlist = adata.BuildDefTree();
       Iterator<String> i = rlist.iterator();
       while(i.hasNext()){
    	   TreeItem rss = new TreeItem(root, SWT.NULL);
    	   rss.setText(i.next());
       }
       // 调用convertImage方法来设置树的图标 
       //convertImage(tree); 
       // Right Click Menu
       addClicktrack();
       addRMenu();
       tree.setLinesVisible(false); 
       tree.setHeaderVisible(false); 
	} 
	/**
	 * 添加击键跟踪,右击时更新当前的操作point,左击时更新当前的浏览器界面
	 */
	private void addClicktrack(){
		tree.addListener(SWT.MouseUp, new Listener() {  
			@Override  
			public void handleEvent(Event event) { 
				if(event.button != 1)
					point = new Point(event.x, event.y);  //获取当前点击的坐标点.  
				else{
					Point p = new Point(event.x,event.y);
					TreeItem item;
					item = tree.getItem(p);
					if(item==null) return;
					if(item.getText().indexOf("Subscriptions")!=-1)
						return;
					String s = WOperate.Decode(item.getText());
					Hhtml.CreateHtml(s, Hhtml.UMODEL);
		    		Wframe.Model = s;
		    		preitem = item;
					Wframe.freshBrowser = System.getProperty("user.dir")+"\\html\\Model.html";
				}
			}  
		});  
	}
	/**
	 * 添加右击菜单项
	 */
	private void addRMenu(){
       Menu menu=new Menu(tree);
       //查看未读消息
       MenuItem UnreadItem=new MenuItem(menu, SWT.PUSH);
       UnreadItem.setText("View Unread Content");
       UnreadItem.addSelectionListener(new SelectionAdapter() {
           @Override
           public void widgetSelected(SelectionEvent event) {
	    		TreeItem item;
	    		try{
	    			item = tree.getItem(point);
	    		}catch(Exception e){
	    			return;
	    		}
	    		if(item==null) return;
	    		if(item.getText().indexOf("Subscriptions")!=-1)
	    			return;
	    		String s = WOperate.Decode(item.getText());
	    		Hhtml.CreateHtml(s, Hhtml.UMODEL);
	    		Wframe.Model = s;
	    		preitem = item;
	    		Wframe.freshBrowser = System.getProperty("user.dir")+"\\html\\Model.html";
           }
       });
       //查看已读消息
       MenuItem HavereadItem=new MenuItem(menu, SWT.PUSH);
       HavereadItem.setText("View HaveRead Content");
       HavereadItem.addSelectionListener(new SelectionAdapter() {
           @Override
           public void widgetSelected(SelectionEvent event) {
	    		TreeItem item;
	    		try{
	    			item = tree.getItem(point);
	    		}catch(Exception e){
	    			return;
	    		}
	    		if(item==null) return;
	    		if(item.getText().indexOf("Subscriptions")!=-1)
	    			return;
	    		String s = WOperate.Decode(item.getText());
	    		Hhtml.CreateHtml(s, Hhtml.HMODEL);
	    		Wframe.Model = s;
	    		preitem = item;
	    		Wframe.freshBrowser = System.getProperty("user.dir")+"\\html\\Model.html";   
           }
       });
       //查看所有消息
       MenuItem AllItem=new MenuItem(menu, SWT.PUSH);
       AllItem.setText("View All Content");
       AllItem.addSelectionListener(new SelectionAdapter() {
           @Override
           public void widgetSelected(SelectionEvent event) {
	    		TreeItem item;
	    		try{
	    			item = tree.getItem(point);
	    		}catch(Exception e){
	    			return;
	    		}
	    		if(item==null) return;
	    		if(item.getText().indexOf("Subscriptions")!=-1)
	    			return;
	    		String s = WOperate.Decode(item.getText());
	    		Hhtml.CreateHtml(s, Hhtml.AMODEL);
	    		Wframe.Model = s;
	    		preitem = item;
	    		Wframe.freshBrowser = System.getProperty("user.dir")+"\\html\\Model.html";
           }
       });
       
       new MenuItem(menu, SWT.SEPARATOR);
       //新建RSS源
       MenuItem newItem=new MenuItem(menu,SWT.PUSH);
       newItem.setText("Add New Subscription");
       newItem.addSelectionListener(new SelectionAdapter() {
           @Override
           public void widgetSelected(SelectionEvent e) {
        	   InputDialog inputDialog = new InputDialog(Wframe.shell,"添加RSS源","请输入feed链接","",null);
        	   if(inputDialog.open() == InputDialog.OK){
        	       String url = inputDialog.getValue();
        	       RSSStore adata = new RSSStore(1);
        	       RSS r = adata.AddtoXML(url);
        	       if(r!=null){
        	    	   TreeItem rss = new TreeItem(root, SWT.NULL);
        	    	   rss.setText(r.Title + "(" + r.UnReadNum+")");
        	    	   tree.redraw();
        	    	   return;
        	       }else{
        	    	   MessageBox dialog=new MessageBox(Wframe.shell,SWT.OK|SWT.CANCEL|SWT.ICON_ERROR);
            		   dialog.setText("Error");
            		   dialog.setMessage("Not a valid URL!");
            		   dialog.open();
        	       }
        	   }else{
        		   return;
        	   }
           }
       });
       //标记所有为已读
       MenuItem markItem=new MenuItem(menu, SWT.PUSH);
       markItem.setText("Mark all Read");
       markItem.addSelectionListener(new SelectionAdapter() {
           @Override
           public void widgetSelected(SelectionEvent e) {
        	   TreeItem item = tree.getItem(point);    //通过坐标点获取节点. 
        	   if(item==null || item.getText().indexOf("Subscriptions")!=-1){
        		   MessageBox dialog=new MessageBox(Wframe.shell,SWT.OK|SWT.CANCEL|SWT.ICON_QUESTION);
        		   dialog.setText("Warning");
        		   dialog.setMessage("Mark ALL feeds Read?");
        		   if(dialog.open()==SWT.CANCEL) return;
        	       Iterator<String> i = rlist.iterator();
        	       int j = 0;
        	       while(i.hasNext()){
        	    	   TreeItem rss = item.getItem(j++);
        	    	   String s = i.next();
        	    	   int k = s.indexOf('(');
        	    	   if(k!=-1) s = s.substring(0, k);
        	    	   rss.setText(s);
        	       }
        	   }else{
        		   MessageBox dialog=new MessageBox(Wframe.shell,SWT.OK|SWT.CANCEL|SWT.ICON_QUESTION);
        		   dialog.setText("Warning");
        		   dialog.setMessage("Mark ALL Read?");
        		   if(dialog.open()==SWT.CANCEL) return;
            	   WOperate wo = new WOperate(e.getSource().toString(),item.getText());
        		   item.setText(wo.r.Title+"(0)");
        	   }
           }
       });
       
       new MenuItem(menu, SWT.SEPARATOR);
       //更新源
       MenuItem UpdateItem=new MenuItem(menu,SWT.PUSH);
       UpdateItem.setText("Update");
       UpdateItem.addSelectionListener(new SelectionAdapter() {
           @Override
           public void widgetSelected(SelectionEvent e) {
        	   TreeItem item = tree.getItem(point);    //通过坐标点获取节点.  
        	   String op = e.getSource().toString();
		       WOperate wo = new WOperate(op,item.getText());
        	   if(item==null || item.getText().indexOf("Subscriptions")!=-1){
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
       //删除源
       MenuItem deleteItem=new MenuItem(menu, SWT.PUSH);
       deleteItem.setText("Delete");
       deleteItem.addSelectionListener(new SelectionAdapter() {
           @Override
           public void widgetSelected(SelectionEvent e) {
		       TreeItem item = tree.getItem(point);    //通过坐标点获取节点.  
        	   new WOperate(e.getSource().toString(),item.getText());
        	   if(item==null || item.getText().indexOf("Subscriptions")!=-1){
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
        	    	   TreeItem rss = new TreeItem(root, SWT.NULL);
        	    	   rss.setText(i.next());
        	       }
        	   }
           }
       });
       tree.setMenu(menu);
	}
	/**
	 * 产生了新的阅读项，将对应的项进行减一的操作
	 */
	public static void MarkDecrease(){
		String s = preitem.getText();
		int j = s.indexOf("(")+1, k = s.indexOf(")");
		System.out.println(s.substring(j, k));
		int i = Integer.parseInt(s.substring(j, k));
		preitem.setText(s.substring(0,s.indexOf("("))+"("+(i-1)+")");
	}
} 
