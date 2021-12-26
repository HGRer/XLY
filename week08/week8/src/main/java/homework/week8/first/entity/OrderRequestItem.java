package homework.week8.first.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "order_request_item")
public class OrderRequestItem {
	@TableId(value = "order_request_item_id")
	private Long orderRequestItemId;
	@TableField(value = "order_request_id")
	private Long orderRequestId;
	@TableField(value = "product_id")
	private Long productId;
	@TableField(value = "quantity")
	private BigDecimal quantity;
	@TableField(value = "quantity_unit")
	private String quantityUnit;
	@TableField(value = "current_price")
	private BigDecimal currentPrice;
}
