package WhaleSWT;

import WhaleData.*;

/**
 * 用于相应树右击菜单的操作类，负责控制数据
 * @author Administrator
 *
 */
public class WOperate {
	String rid;
	RSS r;
	/**
	 * 构造函数：可以根据当前的菜单以及指令构造对应的函数
	 * @param command 指令字符串
	 * @param item 树节点字符串
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
	 * 用于译码，转换为哈希表的键值，以便查找对应的RSS结构体
	 * @param item RSS源名字
	 * @return
	 */
	public static String Decode(String item){
		int k = item.indexOf('(');
		if(k!=-1) item = item.substring(0, k);
		return Integer.toHexString(item.hashCode());
	}
	/**
	 * 添加新的RSS源（都在响应里处理了，留空）
	 * @param item
	 */
	private void addNewSubscription(String item){
	}
	/**
	 * 标记所有为已读
	 * @param item RSS源名字
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
	 * 更新源
	 * @param item RSS源名字
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
	 * 删除源
	 * @param item RSS源名字
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
