package WhaleSWT;

import WhaleData.*;

/**
 * ������Ӧ���һ��˵��Ĳ����࣬�����������
 * @author Administrator
 *
 */
public class WOperate {
	String rid;
	RSS r;
	/**
	 * ���캯�������Ը��ݵ�ǰ�Ĳ˵��Լ�ָ����Ӧ�ĺ���
	 * @param command ָ���ַ���
	 * @param item ���ڵ��ַ���
	 */
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
	/**
	 * �������룬ת��Ϊ��ϣ��ļ�ֵ���Ա���Ҷ�Ӧ��RSS�ṹ��
	 * @param item RSSԴ����
	 * @return
	 */
	public static String Decode(String item){
		int k = item.indexOf('(');
		if(k!=-1) item = item.substring(0, k);
		return Integer.toHexString(item.hashCode());
	}
	/**
	 * ����µ�RSSԴ��������Ӧ�ﴦ���ˣ����գ�
	 * @param item
	 */
	private void addNewSubscription(String item){
	}
	/**
	 * �������Ϊ�Ѷ�
	 * @param item RSSԴ����
	 */
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
	/**
	 * ����Դ
	 * @param item RSSԴ����
	 */
	private void Update(String item){
		RSS tmp;
		RSSStore adata = new RSSStore(1);
		if(item.equals("Subscriptions")){
			adata.UpdateXML();
		}
		else{
			rid = Decode(item);
			tmp = adata.UpdateXML(rid);
			r = (tmp==null)?r:tmp;
		}
	}
	/**
	 * ɾ��Դ
	 * @param item RSSԴ����
	 */
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
