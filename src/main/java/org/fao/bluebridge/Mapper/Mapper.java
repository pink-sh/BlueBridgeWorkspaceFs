package org.fao.bluebridge.Mapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Mapper {
	private String XML;
	
	public Mapper(String xml) {
		this.XML = xml;
	}
	
	public List<WorkspaceFile> parse() throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		ByteArrayInputStream input =  new ByteArrayInputStream(XML.getBytes("UTF-8"));
		
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(input);
        
        doc.getDocumentElement().normalize();
        
        NodeList nList = doc.getElementsByTagName("entry");
        
        List<WorkspaceFile> fileList = new ArrayList<WorkspaceFile>();
        for (int temp = 0; temp < nList.getLength(); temp++) {
        	Node nNode = nList.item(temp);
        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        		Element eElement = (Element) nNode;
        		String filename = eElement
                        .getElementsByTagName("string")
                        .item(0)
                        .getTextContent();
        		Boolean isDirectory = false;
        		if (eElement.getElementsByTagName("boolean").item(0).getTextContent().equalsIgnoreCase("true")) {
        			isDirectory = true;
        		}
        		WorkspaceFile single = new WorkspaceFile();
        		single.setName(filename);
        		single.setIsDirectory(isDirectory);
        		fileList.add(single);
        	}
        }
        return fileList;
	}
}
