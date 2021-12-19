package homework.week7.entity;

import java.sql.Date;
import java.sql.Timestamp;

public class User{
	private Long userId;
	private String address;
	private String name;
	private String gender;
	private String connectValue;
	private Date birthday;
	private Timestamp createTime;
	private Timestamp updateTime;
	private String connectType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getConnectValue() {
		return connectValue;
	}

	public void setConnectValue(String connectValue) {
		this.connectValue = connectValue;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String adderss) {
		this.address = adderss;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getConnectType() {
		return connectType;
	}

	public void setConnectType(String connectType) {
		this.connectType = connectType;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", address=" + address + ", name=" + name + ", gender=" + gender
				+ ", connectValue=" + connectValue + ", birthday=" + birthday + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", connectType=" + connectType + "]";
	}
}
