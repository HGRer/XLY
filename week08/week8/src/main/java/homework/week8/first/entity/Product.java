package homework.week8.first.entity;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
	@TableId(value = "product_id")
	private Long productId;
	@TableField(value = "product_name")
	private String productName;
	@TableField(value = "product_other_name")
	private String productOtherName;
	@TableField(value = "product_type")
	private String productType;
	@TableField(value = "product_unit")
	private String productUnit;
	@TableField(value = "product_states")
	private String productStates;
	@TableField(value = "create_time")
	private Timestamp createTime;
	@TableField(value = "update_time")
	private Timestamp updateTime;
	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", productOtherName="
				+ productOtherName + ", productType=" + productType + ", productUnit=" + productUnit
				+ ", productStates=" + productStates + ", createTime=" + createTime + ", updateTime=" + updateTime
				+ "]";
	}
}
