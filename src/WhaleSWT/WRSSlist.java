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
	 * ���캯��������RSS�����
	 * @param shell Ҫ�����������ָ������shell
	 */
	public WRSSlist(Composite shell){
	   RSSStore adata = new RSSStore(1);
       // ����һ��������
       tree = new Tree(shell, SWT.PUSH);
       FormData formData = new FormData();
       formData.top = new FormAttachment(0, 25);
       formData.left = new FormAttachment(0, 30);
       formData.right = new FormAttachment(0, 60);
       tree.setLayoutData(formData);
       // ��������RSS�ڵ� 
       root = new TreeItem(tree, SWT.NULL); 
       root.setText("Subscriptions"); 
       // ����RSS source�ڵ� 
       rlist = adata.BuildDefTree();
       Iterator<String> i = rlist.iterator();
       while(i.hasNext()){
    	   TreeItem rss = new TreeItem(root, SWT.NULL);
    	   rss.setText(i.next());
       }
       // ����convertImage��������������ͼ�� 
       //convertImage(tree); 
       // Right Click Menu
       addClicktrack();
       addRMenu();
       tree.setLinesVisible(false); 
       tree.setHeaderVisible(false); 
	} 
	/**
	 * ��ӻ�������,�һ�ʱ���µ�ǰ�Ĳ���point,���ʱ���µ�ǰ�����������
	 */
	private void addClicktrack(){
		tree.addListener(SWT.MouseUp, new Listener() {  
			@Override  
			public void handleEvent(Event event) { 
				if(event.button != 1)
					point = new Point(event.x, event.y);  //��ȡ��ǰ����������.  
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
	 * ����һ��˵���
	 */
	private void addRMenu(){
       Menu menu=new Menu(tree);
       //�鿴δ����Ϣ
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
       //�鿴�Ѷ���Ϣ
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
       //�鿴������Ϣ
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
       //�½�RSSԴ
       MenuItem newItem=new MenuItem(menu,SWT.PUSH);
       newItem.setText("Add New Subscription");
       newItem.addSelectionListener(new SelectionAdapter() {
           @Override
           public void widgetSelected(SelectionEvent e) {
        	   InputDialog inputDialog = new InputDialog(Wframe.shell,"���RSSԴ","������feed����","",null);
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
       //�������Ϊ�Ѷ�
       MenuItem markItem=new MenuItem(menu, SWT.PUSH);
       markItem.setText("Mark all Read");
       markItem.addSelectionListener(new SelectionAdapter() {
           @Override
           public void widgetSelected(SelectionEvent e) {
        	   TreeItem item = tree.getItem(point);    //ͨ��������ȡ�ڵ�. 
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
       //����Դ
       MenuItem UpdateItem=new MenuItem(menu,SWT.PUSH);
       UpdateItem.setText("Update");
       UpdateItem.addSelectionListener(new SelectionAdapter() {
           @Override
           public void widgetSelected(SelectionEvent e) {
        	   TreeItem item = tree.getItem(point);    //ͨ��������ȡ�ڵ�.  
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
       //ɾ��Դ
       MenuItem deleteItem=new MenuItem(menu, SWT.PUSH);
       deleteItem.setText("Delete");
       deleteItem.addSelectionListener(new SelectionAdapter() {
           @Override
           public void widgetSelected(SelectionEvent e) {
		       TreeItem item = tree.getItem(point);    //ͨ��������ȡ�ڵ�.  
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
	 * �������µ��Ķ������Ӧ������м�һ�Ĳ���
	 */
	public static void MarkDecrease(){
		String s = preitem.getText();
		int j = s.indexOf("(")+1, k = s.indexOf(")");
		System.out.println(s.substring(j, k));
		int i = Integer.parseInt(s.substring(j, k));
		preitem.setText(s.substring(0,s.indexOf("("))+"("+(i-1)+")");
	}
} 
