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
@TableName(value = "product_price_hist")
public class ProductPriceHistory {
	@TableId(value = "product_price_hist_id")
	private Long productPriceHistId;
	@TableField(value = "current_price")
	private BigDecimal currentPrice;
	@TableField(value = "is_available")
	private Integer isAvailable;
	@TableField(value = "effective_begin_time")
	private Timestamp effectiveBeginTime;
	@TableField(value = "effective_end_time")
	private Timestamp effectiveEndTime;
	@TableField(value = "create_time")
	private Timestamp createTime;
	@TableField(value = "update_time")
	private Timestamp updateTime;
	@TableField(value = "product_id")
	private Long productId;
}
