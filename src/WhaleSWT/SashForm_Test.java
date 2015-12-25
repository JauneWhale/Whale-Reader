package WhaleSWT;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
/**
 * 用于测试分隔栏的类，弃用
 * @author Administrator
 * @deprecated
 */
public class SashForm_Test {
	public static void main(String[] args){
		String a = System.getProperty("user.dir");
		System.out.println("用户的当前工作目录:/n"+a);   
	}
  public void haha(String[] args) {
    final Display display = new Display();
    Shell shell = new Shell(display);
    shell.setLayout(new FillLayout());

    SashForm form = new SashForm(shell, SWT.HORIZONTAL);
    form.setLayout(new FillLayout());

    Composite child1 = new Composite(form, SWT.NONE);
    child1.setLayout(new FillLayout());
    new Label(child1, SWT.NONE).setText("Label in pane 1");

    Composite child2 = new Composite(form, SWT.NONE);
    child2.setLayout(new FillLayout());
    new Button(child2, SWT.PUSH).setText("Button in pane2");

    Composite child3 = new Composite(form, SWT.NONE);
    child3.setLayout(new FillLayout());
    new Label(child3, SWT.PUSH).setText("Label in pane3");

    form.setWeights(new int[] { 30, 40, 30 });
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }
}