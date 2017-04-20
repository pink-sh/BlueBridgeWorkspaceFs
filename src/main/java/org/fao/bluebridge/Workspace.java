package org.fao.bluebridge;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.fao.bluebridge.Mapper.Mapper;
import org.fao.bluebridge.Mapper.WorkspaceFile;
import org.xml.sax.SAXException;

public class Workspace {
	private String username;
	private String endPoint;
	private String token;
	
	public Workspace(String username, String token, String endPoint) {
		this.username = username;
		if (!endPoint.endsWith("/")) {
			this.endPoint = endPoint + "/";
		} else {
			this.endPoint = endPoint;
		}
		this.token = token;
	}
	
	private Long getFileLength(String path, String filename) {
		try {
			String authString = this.username + ":" + this.token;
			byte[] authEncBytes = Base64.getEncoder().encode(authString.getBytes());
			String authStringEnc = new String(authEncBytes);
			if (!path.endsWith(File.separator)) {
				path = path + File.separator;
			}
	
			URL url = new URL(this.endPoint + "rest/Download?absPath=" + URLEncoder.encode(path + filename, "UTF-8"));
			URLConnection urlConnection = url.openConnection();
			System.out.println(this.endPoint + "rest/Download?absPath=" + URLEncoder.encode(path + filename, "UTF-8"));
			System.out.println("Authorization: Basic " + authStringEnc);
			urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
			
			String contentLength = urlConnection.getHeaderField("Content-Length");
			if (contentLength != null) {
				Long length = Long.parseLong(contentLength);
				return length;
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<WorkspaceFile> connectToWorkspaceAndList(String path) {
		try {

			String authString = this.username + ":" + this.token;
			byte[] authEncBytes = Base64.getEncoder().encode(authString.getBytes());
			String authStringEnc = new String(authEncBytes);

			URL url = new URL(this.endPoint + "rest/List?absPath=" + path);
			URLConnection urlConnection = url.openConnection();
			System.out.println(this.endPoint + "rest/List?absPath=" + path);
			System.out.println("Authorization: Basic " + authStringEnc);
			urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
			
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			Mapper mapper = new Mapper(sb.toString());
			List<WorkspaceFile> list = mapper.parse();
			List<WorkspaceFile> reparse = new ArrayList<WorkspaceFile>();
			for (WorkspaceFile file : list) {
				if (!file.getIsDirectory()) {
					file.setLength(this.getFileLength(path, file.getName()));
				}
				reparse.add(file);
			}
			return reparse;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<WorkspaceFile> readPath(String path) {
		return this.connectToWorkspaceAndList(path);
	}
}
