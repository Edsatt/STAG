package edu.uob;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class ActionParser {
    private File actionsFile;
    private Map map;
    private DocumentBuilder builder;
    private Document document;
    private NodeList actions;
    public ActionParser(Map map, File actionsFile){
        this.map = map;
        this.actionsFile = actionsFile;
        try{
            this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }catch(ParserConfigurationException pce) {
            throw new RuntimeException("Unable to read actionsFile");
        }

    }

    public void parse() {
        try{
            this.document = builder.parse(actionsFile);

        }catch(SAXException saxe){
            throw new RuntimeException("SAXException was thrown when attempting to read actions file");
        } catch (IOException e) {
            throw new RuntimeException("IOException was thrown when attempting to read actions file");
        }
//        Element root = document.getDocumentElement();
//        this.actions = root.getChildNodes();
//        System.out.println(actions.getLength());
//        Element firstItem = (Element)actions.item(7);
//        System.out.println(firstItem);
////        Element triggers = (Element)firstItem.getElementsByTagName("triggers").item(0);
////        System.out.println(triggers);
    }
}
