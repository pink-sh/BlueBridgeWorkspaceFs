package org.fao.bluebridge;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.fao.bluebridge.Mapper.WorkspaceFile;

import net.fusejna.DirectoryFiller;
import net.fusejna.ErrorCodes;
import net.fusejna.StructStat.StatWrapper;
import net.fusejna.types.TypeMode.NodeType;
import net.fusejna.util.FuseFilesystemAdapterFull;

public class WorkspaceFs extends FuseFilesystemAdapterFull{
	private List<WorkspaceFile> ListOfFiles;
	private String currentPath = "";
	
	private String USERNAME;
	private String TOKEN;
	private String ENDPOINT;
	
	public WorkspaceFs(String username, String token, String endpoint) {
		ListOfFiles = new ArrayList<WorkspaceFile>();
		this.USERNAME = username;
		this.TOKEN = token;
		this.ENDPOINT = endpoint;
	}
	@Override
	public int getattr(final String path, final StatWrapper stat)
	{
		if (path.equals(File.separator)) { // Root directory
			stat.setMode(NodeType.DIRECTORY);
			return 0;
		}
		if (path.equals(currentPath)) {
			stat.setMode(NodeType.DIRECTORY);
			return 0;
		}
		if (getPreviousDirectory(currentPath) != null && getPreviousDirectory(currentPath).equals(path)) {
			stat.setMode(NodeType.DIRECTORY);
			return 0;
		}
		if (!this.currentPath.endsWith(File.separator)) {
			this.currentPath += File.separator;
		}
		if (this.ListOfFiles != null && !this.ListOfFiles.isEmpty()) {
			for (WorkspaceFile f : this.ListOfFiles) {
				if (path.equals(this.currentPath + f.getName())) {
					if (f.getIsDirectory()) {
						stat.setMode(NodeType.DIRECTORY);
						return 0;
					} else {
						stat.setMode(NodeType.FILE).size(f.getLength());
						return 0;
					}
				}
			}
		}
		return -ErrorCodes.ENOSYS();
	}
	
	@Override
	public int readdir(final String path, final DirectoryFiller filler)
	{
		//System.out.println("Path:: " + path);
		this.currentPath = path;
		String toCallPath = path;
		toCallPath = "/Home/" + this.USERNAME + "/Workspace" + path;
		Workspace workspace = new Workspace(this.USERNAME, this.TOKEN, this.ENDPOINT);
		List<WorkspaceFile> list = workspace.readPath(toCallPath);
		for (WorkspaceFile file : list) {
			filler.add(file.getName());
		}
		this.ListOfFiles = list;
		return 0;
	}
	
	private static String getPreviousDirectory(String path) {
		if (null != path && path.length() > 1) {
			int endIndex = path.lastIndexOf(File.separator);
			if (endIndex != -1) {
				return path.substring(0, endIndex);
			}
		}
		return null;
	}
}
