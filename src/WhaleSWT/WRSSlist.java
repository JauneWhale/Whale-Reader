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
       // ����һ��������
       Tree tree = new Tree(shell, SWT.PUSH);
       FormData formData = new FormData();
       formData.top = new FormAttachment(0, 25);
       formData.left = new FormAttachment(0, 30);
       formData.right = new FormAttachment(0, 60);
       tree.setLayoutData(formData);
       // ��������RSS�ڵ� 
       TreeItem root = new TreeItem(tree, SWT.NULL); 
       root.setText("Subscriptions"); 
       // ����RSS source�ڵ� 
       rlist = adata.BuildDefTree();
       Iterator<String> i = rlist.iterator();
       while(i.hasNext()){
    	   TreeItem rss = new TreeItem(root, SWT.NULL);
    	   rss.setText(i.next());
       }
       // ����convertImage��������������ͼ�� 
       convertImage(tree); 
       // Right Click Menu
       addRMenu(tree);
       tree.setLinesVisible(false); 
       tree.setHeaderVisible(false); 
	} 

   // ������ͼ��ķ��� 
	public static void convertImage(Tree tree) { 
       // �������ֻ��һ�����ڵ� 
       TreeItem[] items = tree.getItems();
       for(int i=0; i<items.length; i++)
    	   setChildImage(items[i]); 
	} 

   // ����һ���ڵ�ķ������÷����ǳ���Ҫ��Ҫ���÷����ĵݹ��÷� 
   // ����item���԰ѵ������������е�ĳһ��TreeItem 
	public static void setChildImage(TreeItem item) { 
       // ���Ȼ�ø�TreeItem��������TreeItem 
       TreeItem[] items = item.getItems(); 
       // ѭ��ÿһ��TreeItem 
       for (int i = 0; i < items.length; i++) { 
           setChildImage(items[i]); 
       } 
	} 
	public static void addRMenu(Tree tree){
	   tree.addListener(SWT.MouseUp, new Listener() {  
		    @Override  
		    public void handleEvent(Event event) { 
		    	if(event.button != 1)
		    		point = new Point(event.x, event.y);  //��ȡ��ǰ����������.  
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
        	   InputDialog inputDialog = new InputDialog(Wframe.shell,"����","��ʾ��Ϣ","Ĭ��ֵ",null);
        	   //InputDialog i = new InputDialog(null, null, null, null, null);
        	   if(inputDialog.open() == InputDialog.OK){
        	       String value = inputDialog.getValue();
        	   }
        	   TreeItem item = tree.getItem(point);    //ͨ��������ȡ�ڵ�.  
		       new WOperate(e.getSource().toString(),item.getText());
           }
       });
       MenuItem markItem=new MenuItem(menu, SWT.PUSH);
       markItem.setText("Mark all Read");
       markItem.addSelectionListener(new SelectionAdapter() {
           @Override
           public void widgetSelected(SelectionEvent e) {
        	   TreeItem item = tree.getItem(point);    //ͨ��������ȡ�ڵ�.  
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
        	   TreeItem item = tree.getItem(point);    //ͨ��������ȡ�ڵ�.  
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
		       TreeItem item = tree.getItem(point);    //ͨ��������ȡ�ڵ�.  
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
