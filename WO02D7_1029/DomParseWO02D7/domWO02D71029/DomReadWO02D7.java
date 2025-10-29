package WO02D7_1029.DomParseWO02D7.domWO02D71029;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DomReadWO02D7 {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        File xmlFile = new File("WO02D7_1029/WO02D7hallgato.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder dBuilder = factory.newDocumentBuilder();

        Document wo02d7 = dBuilder.parse(xmlFile);
        wo02d7.getDocumentElement().normalize();

        System.out.println("Gyoker elem: " + wo02d7.getDocumentElement().getNodeName());

        NodeList nList = wo02d7.getElementsByTagName("hallgato");

        for(int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            System.out.println("\nAktualis elem: " + nNode.getNodeName());

            if(nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;

                String hid = elem.getAttribute("id");

                Node node1 = elem.getElementsByTagName("keresztnev").item(0); 
                String kName = node1.getTextContent();

                Node node2 = elem.getElementsByTagName("vezeteknev").item(0);
                String vName = node2.getTextContent();

                Node node3 = elem.getElementsByTagName("foglalkozas").item(0);
                String fName = node3.getTextContent();

                System.out.println("Hallgato id: " + hid);
                System.out.println("Keresztnev: " + kName);
                System.out.println("Vezeteknev: " + vName);
                System.out.println("Foglalkozas: " + fName);
            }
        }
    }


}