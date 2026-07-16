package com.pinche.authority.manager.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class A{
  private Document doc = null;

  public A(String xmlContent, String flag)
    throws ParserConfigurationException, SAXException, IOException
  {
    StringReader sr = new StringReader(xmlContent);
    InputSource is = new InputSource(sr);
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    db.setEntityResolver(new EntityResolver(){
      public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        return new InputSource(new StringReader(""));
      }
    });
    this.doc = db.parse(is);
  }

  public A(String xmlFilePath)
    throws ParserConfigurationException, SAXException, IOException
  {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();

    db.setEntityResolver(new EntityResolver()
    {
      public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        return new InputSource(new StringReader(""));
      }
    });
    this.doc = db.parse(new File(xmlFilePath));
  }

  public A(InputSource is)
    throws ParserConfigurationException, SAXException, IOException
  {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();

    db.setEntityResolver(new EntityResolver()
    {
      public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        return new InputSource(new StringReader(""));
      }
    });
    this.doc = db.parse(is);
  }

  public NodeList readNode(String nodeName)
  {
    return this.doc.getElementsByTagName(nodeName);
  }

  public void updateNodeContent(String nodeName, int index, String newContent)
  {
    NodeList nodeList = this.doc.getElementsByTagName(nodeName);

    if (nodeList.getLength() >= index + 1)
      nodeList.item(index).setTextContent(newContent);
  }

  public String xmlToString() throws Exception {
    String xmlStr = "";
    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer t = tf.newTransformer();
    t.setOutputProperty("encoding", "gbk");
    ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
    t.transform(new DOMSource(this.doc), new StreamResult(bos));
    xmlStr = bos.toString();
    return xmlStr;
  }
}
