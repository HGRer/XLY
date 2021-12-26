package homework.week8.first.entity;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName(value = "meta_data_cd")
public class MetaDataCode {
	@TableId(value = "meta_data_cd_id")
	private Long metaDataCodeId;
	@TableField(value = "meta_data_cd")
	private String metaDataCode;
	@TableField(value = "meta_data_desc")
	private String metaDataDesc;
	@TableField(value = "defun_ind")
	private String defunctInd;
	@TableField(value = "create_time")
	private Timestamp createTime;
	@TableField(value = "update_time")
	private Timestamp updateTime;
	@Override
	public String toString() {
		return "MetaDataCode [metaDataCodeId=" + metaDataCodeId + ", metaDataCode=" + metaDataCode + ", metaDataDesc="
				+ metaDataDesc + ", defunctInd=" + defunctInd + ", createTime=" + createTime + ", updateTime="
				+ updateTime + "]";
	}
	
}
