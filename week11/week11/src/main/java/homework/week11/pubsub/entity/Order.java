package homework.week11.pubsub.entity;

public class Order {
	private int userId;
	private int orderId;
	private String flag = "0";
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	@Override
	public String toString() {
		return "Order [用户=" + userId + ", 订单号=" + orderId + ", flag=" + flag + "]";
	}
}
