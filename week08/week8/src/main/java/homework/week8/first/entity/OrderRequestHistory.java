package homework.week8.first.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName(value = "order_request_hist")
public class OrderRequestHistory {
	@TableId(value = "order_request_hist_id")
	private Long orderRequestHistId;
	@TableField(value = "order_request_id")
	private Long orderRequestId;
	@TableField(value = "order_request_states")
	private String orderRequestStates;
	@TableField(value = "total_balance")
	private BigDecimal total_balance;
	@TableField(value = "create_time")
	private Timestamp createTime;
	@TableField(value = "update_time")
	private Timestamp updateTime;
}
