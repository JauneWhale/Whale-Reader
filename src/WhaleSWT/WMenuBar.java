package WhaleSWT;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class WMenuBar {
	public WMenuBar(final Shell shell){
		Menu mainMenu=new Menu(shell,SWT.BAR);
		shell.setMenuBar(mainMenu);
		//Menu mainMenu=new Menu(shell,SWT.POP_UP); //��������ʽ�˵�
		//shell.setMenu(mainMenu); //��������ʽ�˵�
		{
			//"�ļ�"��
			MenuItem fileItem=new MenuItem(mainMenu,SWT.CASCADE);
			fileItem.setText("�ļ�&F");
			//"�ļ�"�˵�
			Menu fileMenu=new Menu(shell,SWT.DROP_DOWN);
			fileItem.setMenu(fileMenu);
			{
				//"�½�"��
				MenuItem newFileItem=new MenuItem(fileMenu,SWT.CASCADE);
				newFileItem.setText("�½�&N");
				//"�½�"�˵�
				Menu newFileMenu=new Menu(shell,SWT.DROP_DOWN);
				newFileItem.setMenu(newFileMenu);
				{
					//"�½���Ŀ"��
					MenuItem newProjectItem=new MenuItem(newFileMenu,SWT.PUSH);
					newProjectItem.setText("��Ŀ\tCtrl+Shift+N");
					//���ÿ�ݼ�
					newProjectItem.setAccelerator(SWT.CTRL+SWT.SHIFT+'N');
					//����¼�����
					newProjectItem.addSelectionListener(new SelectionAdapter(){
						public void widgetSelected(SelectionEvent e){
							Text text=new Text(shell,SWT.MULTI|SWT.BORDER|SWT.WRAP);
							text.setBounds(10,10,100,30);
							text.setText("��ѡ���ˡ��½���Ŀ��");
						}
					});
					new MenuItem(newFileMenu,SWT.SEPARATOR);
					new MenuItem(newFileMenu,SWT.PUSH).setText("��");
					new MenuItem(newFileMenu,SWT.PUSH).setText("��");
				}
				MenuItem openFileItem=new MenuItem(fileMenu,SWT.CASCADE);
				openFileItem.setText("��&O");
				MenuItem exitItem=new MenuItem(fileMenu,SWT.CASCADE);
				exitItem.setText("�˳�&E");
			}
			MenuItem helpItem=new MenuItem(mainMenu,SWT.CASCADE);
			helpItem.setText("����&H");
		}
	}
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		new WMenuBar(shell);
		shell.pack();
		shell.open();
		while(!shell.isDisposed()){ //���������û�йر���һֱѭ��
			if(!display.readAndDispatch()){ //���display��æ
				display.sleep(); //����
			}
		}
		display.dispose(); //����display
	}
}