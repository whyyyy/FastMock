package com.mock.model;

public class MockRequestStrategy {
	private String id;
	private String mockUriId;
	private String mockRequestName;
	private Integer requestWait;
	private String verifyExpect;
	private String responseExpect;
	private Integer isRun;
	private String remark;
	private Integer orderNum;
	private Integer idex;
	private String createTime;
	private String updateTime;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMockUriId() {
		return mockUriId;
	}
	public void setMockUriId(String mockUriId) {
		this.mockUriId = mockUriId;
	}
	public String getMockRequestName() {
		return mockRequestName;
	}
	public void setMockRequestName(String mockRequestName) {
		this.mockRequestName = mockRequestName;
	}
	public Integer getRequestWait() {
		return requestWait;
	}
	public void setRequestWait(Integer requestWait) {
		this.requestWait = requestWait;
	}
	public String getVerifyExpect() {
		return verifyExpect;
	}
	public void setVerifyExpect(String verifyExpect) {
		this.verifyExpect = verifyExpect;
	}
	public String getResponseExpect() {
		return responseExpect;
	}
	public void setResponseExpect(String responseExpect) {
		this.responseExpect = responseExpect;
	}
	public Integer getIsRun() {
		return isRun;
	}
	public void setIsRun(Integer isRun) {
		this.isRun = isRun;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public Integer getIdex() {
		return idex;
	}
	public void setIdex(Integer idex) {
		this.idex = idex;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "MockRequestStrategy{" +
				"id='" + id + '\'' +
				", mockUriId='" + mockUriId + '\'' +
				", mockRequestName='" + mockRequestName + '\'' +
				", requestWait=" + requestWait +
				", verifyExpect='" + verifyExpect + '\'' +
				", responseExpect='" + responseExpect + '\'' +
				", isRun=" + isRun +
				", remark='" + remark + '\'' +
				", orderNum=" + orderNum +
				", idex=" + idex +
				", createTime='" + createTime + '\'' +
				", updateTime='" + updateTime + '\'' +
				'}';
	}
}
