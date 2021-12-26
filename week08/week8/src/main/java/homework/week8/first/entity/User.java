package homework.week8.first.entity;

import java.sql.Date;
import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class User {
	@TableId(value="user_id")
	private Long userId;
	private String name;
	private String gender;
	@TableField(value="connect_type")
	private String connectType;
	@TableField(value="connect_value")
	private String connectValue;
	private String address;
	private Date birthday;
	@TableField(value = "create_time")
	private Timestamp createTime;
	@TableField(value = "update_time")
	private Timestamp updateTime;
	@Override
	public String toString() {
		return "User [userId=" + userId + ", name=" + name + ", gender=" + gender + ", connectType=" + connectType
				+ ", connectValue=" + connectValue + ", address=" + address + ", birthday=" + birthday + ", createTime="
				+ createTime + ", updateTime=" + updateTime + "]";
	}
}
