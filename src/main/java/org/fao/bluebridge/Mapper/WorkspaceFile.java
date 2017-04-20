package org.fao.bluebridge.Mapper;

public class WorkspaceFile{
	private String name;
	private Boolean isDirectory;
	private Long length;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getIsDirectory() {
		return isDirectory;
	}
	public void setIsDirectory(Boolean isDirectory) {
		this.isDirectory = isDirectory;
	}
	/**
	 * @return the length
	 */
	public Long getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(Long length) {
		this.length = length;
	}
}
