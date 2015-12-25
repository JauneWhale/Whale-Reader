package WhaleSWT;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import WhaleData.*;

public class WOperate {
	String rid;
	RSS r;
	public WOperate(String command, String item) {
		rid = null;
		r = null;
		if(command.indexOf("Add New Subscription")!=-1)
			this.addNewSubscription(item);
		if(command.indexOf("Mark all Read")!=-1)
			this.markAllRead(item);
		if(command.indexOf("Update")!=-1)
			this.Update(item);
		if(command.indexOf("Delete")!=-1)
			this.Delete(item);
	}
	public static String Decode(String item){
		int k = item.indexOf('(');
		if(k!=-1) item = item.substring(0, k);
		return Integer.toHexString(item.hashCode());
	}
	private void addNewSubscription(String item){
	}
	private void markAllRead(String item){
		RSSStore adata = new RSSStore(1);
		if(item.equals("Subscriptions")){
			adata.MarkAllRead();
		}
		else{
			rid = Decode(item);
			r = adata.MarkAllRead(rid);
		}
	}
	private void Update(String item){
		RSSStore adata = new RSSStore(1);
		if(item.equals("Subscriptions")){
			adata.UpdateXML();
		}
		else{
			rid = Decode(item);
			r = adata.UpdateXML(rid);
		}
	}
	private void Delete(String item){
		RSSStore adata = new RSSStore(1);
		if(item.equals("Subscriptions"))
			adata.DelAllRSSfromXML();
		else{
			rid = Decode(item);
			adata.DelfromXML(rid);
		}
	}
}
