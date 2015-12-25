package WhaleSWT;

import org.eclipse.swt.SWT; 
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display; 
import org.eclipse.swt.widgets.Event; 
import org.eclipse.swt.widgets.Label; 
import org.eclipse.swt.widgets.Listener; 
import org.eclipse.swt.widgets.Shell; 
import org.eclipse.swt.widgets.Text;

/**
 * 用于测试浏览器的类，弃用
 * @author Administrator
 * @deprecated
 */
public class Browser_Test 
{ 
    public static void main(String args[]) 
    { 
        Display display=new Display(); 
        Shell shell=new Shell(display); 
        shell.setText("SWT Browser Test"); 
        shell.setSize(800,600); 
        
        final Text text=new Text(shell,SWT.BORDER); 
        text.setBounds(110,5,560,25); 
        Button button=new Button(shell,SWT.BORDER); 
        button.setBounds(680,5,100,25);        
        button.setText("go"); 
        Label label=new Label(shell,SWT.LEFT); 
        label.setText("输入网址 :"); 
        label.setBounds(5, 5, 100, 25); 
        
        final Browser browser=new Browser(shell,SWT.FILL); 
        browser.setBounds(5,30,780,560); 
        
        button.addListener(SWT.Selection, new Listener() 
        { 
            public void handleEvent(Event event) 
            { 
                /*String input=text.getText().trim(); 
                if(input.length()==0)return; 
                if(!input.startsWith("http://")) 
                { 
                    input="http://"+input; 
                    text.setText(input); 
                } 
                browser.setUrl(input); */
            	browser.setUrl("f:\\test.html");
            } 
        }); 
        
        shell.open(); 
        while (!shell.isDisposed()) { 
            if (!display.readAndDispatch()) 
              display.sleep(); 
          } 
          display.dispose(); 
        
    }
    

    /*
    Label label_remark;
    shell.setLayout(new FormLayout());
    Shell composite = shell;
    //final Composite composite = new Composite(shell, SWT.NONE);
    composite.setLayout(new FormLayout());

    final Label label_name = new Label(composite, SWT.NONE);
    final FormData formData = new FormData();
    formData.top = new FormAttachment(0, 25);
    formData.left = new FormAttachment(0, 30);
    formData.right = new FormAttachment(0, 60);
    label_name.setLayoutData(formData);
    label_name.setText("姓名");

    Text text_name = new Text(composite, SWT.BORDER);
    formData.bottom = new FormAttachment(text_name, 0, SWT.BOTTOM);
    final FormData formData_1 = new FormData();
    formData_1.top = new FormAttachment(0, 25);
    formData_1.right = new FormAttachment(100, -32);
    formData_1.bottom = new FormAttachment(0, 43);
    formData_1.left = new FormAttachment(label_name, 5, SWT.DEFAULT);
    text_name.setLayoutData(formData_1);

    Text text_remark = new Text(composite, SWT.BORDER);
    final FormData formData_2 = new FormData();
    formData_2.bottom = new FormAttachment(100, -16);
    formData_2.right = new FormAttachment(100, -32);
    formData_2.top = new FormAttachment(0, 62);
    formData_2.left = new FormAttachment(0, 65);
    text_remark.setLayoutData(formData_2);
    label_remark = new Label(composite, SWT.NONE);
    final FormData formData_3 = new FormData();
    formData_3.top = new FormAttachment(44, 0);
    formData_3.bottom = new FormAttachment(51, 0);
    formData_3.right = new FormAttachment(0, 60);
    formData_3.left = new FormAttachment(0, 30);
    label_remark.setLayoutData(formData_3);
    label_remark.setText("说明");
    
    
    Button button=new Button(shell,SWT.PUSH);   
    button.setText("go");    
    /*button.setBounds(680,5,100,25);  
    Label label=new Label(shell,SWT.LEFT); 
    label.setText("输入网址 :"); 
    label.setBounds(5, 5, 100, 25); 
    final Label k = new Label(shell,SWT.PUSH);
    k.setText("haha");
    k.setBounds(5, 5, 100, 25); */
    //final Browser browser=new Browser(shell,SWT.PUSH);
    //browser.setBounds(5,30,780,560); 
    
    /*button.addListener(SWT.Selection, new Listener() 
    { 
        public void handleEvent(Event event) 
        { 
            /*String input=text.getText().trim(); 
            if(input.length()==0)return; 
            if(!input.startsWith("http://")) 
            { 
                input="http://"+input; 
                text.setText(input); 
            } 
            browser.setUrl(input); 
        	browser.setUrl("f:\\test.html");
        } 
    }); 
    
       
       // 创建一个可编辑的TreeEditor对象 
       final TreeEditor editor = new TreeEditor(tree); 
       editor.horizontalAlignment = SWT.LEFT; 
       editor.grabHorizontal = true; 
       editor.grabVertical = true;
       editor.minimumWidth = 30; 
       // 注册选中事件 
       tree.addSelectionListener(new SelectionAdapter() { 
           // 当鼠标双击节点时使节点可编辑 
           public void widgetDefaultSelected(SelectionEvent e) { 
               // 释放之前编辑的控件 
               Control oldEditor = editor.getEditor(); 
               if (oldEditor != null) 
                   oldEditor.dispose(); 
               // 获得触发事件的TreeItem，如果为null，返回 
               TreeItem item = (TreeItem) e.item; 
               if (item == null) 
                   return; 
               // 创建一个文本框，作为编辑节点时输入的文字 
               Text newEditor = new Text(tree, SWT.NONE); 
               // 将树节点的值赋值给文本框 
               newEditor.setText(item.getText()); 
               // 当文本框的值改变时，也相应的该把你树节点数据的值 
               newEditor.addModifyListener(new ModifyListener() { 
                   public void modifyText(ModifyEvent e) { 
                       Text text = (Text) editor.getEditor(); 
                       editor.getItem().setText(text.getText()); 
                   } 
               }); 
               newEditor.selectAll();// 选中所有文本框 
               newEditor.setFocus();// 并将焦点设置为该文本框 
               // 将树节点与文本框节点绑定 
               editor.setEditor(newEditor, item); 
           } 

       }); 
    *
    */
} 
