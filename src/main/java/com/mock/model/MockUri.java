package com.mock.model;

public class MockUri {
	private String id;
	private String mockProjectId;
	private String mockUriName;
	private String mockUri;
	private String mockMethod;
	private Integer isRun;
	private Integer idex;
	private String createTime;
	private String updateTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMockProjectId() {
		return mockProjectId;
	}
	public void setMockProjectId(String mockProjectId) {
		this.mockProjectId = mockProjectId;
	}
	public String getMockUriName() {
		return mockUriName;
	}
	public void setMockUriName(String mockUriName) {
		this.mockUriName = mockUriName;
	}
	public String getMockUri() {
		return mockUri;
	}
	public void setMockUri(String mockUri) {
		this.mockUri = mockUri;
	}
	public String getMockMethod() {
		return mockMethod;
	}
	public void setMockMethod(String mockMethod) {
		this.mockMethod = mockMethod;
	}
	public Integer getIsRun() {
		return isRun;
	}
	public void setIsRun(Integer isRun) {
		this.isRun = isRun;
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
		return "MockUri{" +
				"id='" + id + '\'' +
				", mockProjectId='" + mockProjectId + '\'' +
				", mockUriName='" + mockUriName + '\'' +
				", mockUri='" + mockUri + '\'' +
				", mockMethod='" + mockMethod + '\'' +
				", isRun=" + isRun +
				", idex=" + idex +
				", createTime='" + createTime + '\'' +
				", updateTime='" + updateTime + '\'' +
				'}';
	}
}
