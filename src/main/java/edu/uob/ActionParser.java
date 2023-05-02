package edu.uob;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class ActionParser {
    private final File actionsFile;
    private final DocumentBuilder builder;
    private NodeList actions;
    private Element actionElement;
    private GameAction gameAction;
    private String[] triggers;
    private int numActions;
    private final ActionsList actionsList;

    public ActionParser(Map map, File actionsFile){
        this.actionsList = map.getActionList();
        this.actionsFile = actionsFile;
        try{
            this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }catch(ParserConfigurationException pce) {
            throw new RuntimeException("Unable to read actionsFile");
        }

    }

    public void parse() {
        Document document;
        try{
            document = builder.parse(actionsFile);

        }catch(SAXException saxe){
            throw new RuntimeException("SAXException was thrown when attempting to read actions file");
        } catch (IOException e) {
            throw new RuntimeException("IOException was thrown when attempting to read actions file");
        }
        Element root = document.getDocumentElement();
        this.actions = root.getChildNodes();
        this.numActions = actions.getLength();
        createActions();

    }

    private void createActions(){
        for(int i=0; i<numActions; i++){
            if(i%2!=0) {
                this.actionElement = (Element)actions.item(i);
                this.triggers = getElements( "triggers", "keyphrase");
                this.gameAction = new GameAction();
                gameAction.setSubjects(getElements("subjects","entity"));
                gameAction.setConsumed(getElement("consumed"));
                gameAction.setProduced(getElement("produced"));
                gameAction.setNarration(getNarration());
                addActionToList();
            }
        }
    }

    private String [] getElements(String componentName, String elementName){
        Element component = (Element) actionElement.getElementsByTagName(componentName).item(0);
        NodeList elements = component.getElementsByTagName(elementName);
        int length = elements.getLength();
        String[] values = new String[length];
        for(int i=0; i<length; i++){
            values[i] = elements.item(i).getTextContent();
        }
        return values;
    }

    private String getElement(String componentName){
        Element component = (Element) actionElement.getElementsByTagName(componentName).item(0);
        Node entityNode = component.getElementsByTagName("entity").item(0);
        return entityNode !=null ? entityNode.getTextContent() : null;
    }

    private String getNarration(){
        return actionElement.getElementsByTagName("narration").item(0).getTextContent();
    }

    private void addActionToList(){
        for(String trigger: triggers){
            actionsList.addAction(trigger, gameAction);
        }
    }
}
