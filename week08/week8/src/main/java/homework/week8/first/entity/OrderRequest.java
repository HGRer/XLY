package homework.week8.first.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "order_request")
public class OrderRequest {
	@TableId(value = "order_request_id")
	private Long orderRequestId;
	@TableField(value = "order_request_states")
	private String orderRequestStates;
	@TableField(value = "total_balance")
	private BigDecimal totalBalance;
	@TableField(value = "create_time")
	private Timestamp createTime;
	@TableField(value = "update_time")
	private Timestamp updateTime;
	@Override
	public String toString() {
		return "OrderRequest [orderRequestId=" + orderRequestId + ", orderRequestStates=" + orderRequestStates
				+ ", totalBalance=" + totalBalance + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
}
