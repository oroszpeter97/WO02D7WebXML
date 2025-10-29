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

public class DomReadWO02D71 {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        File xmlFile = new File("WO02D7_1029/WO02D7_orarend.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document orarend = dBuilder.parse(xmlFile);
        orarend.getDocumentElement().normalize();
        System.out.println("Gyoker elem: " + orarend.getDocumentElement().getNodeName());

        NodeList nList = orarend.getElementsByTagName("ora");
        for(int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            System.out.println("\nAktualis elem: " + nNode.getNodeName());

            if(nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;
                
                Element node1 = (Element) elem.getElementsByTagName("targy").item(0);
                String oTargy = node1.getTextContent();

                String oid = elem.getAttribute("id");
                String otipus = elem.getAttribute("tipus");

                Element oIdopont = (Element) elem.getElementsByTagName("idopont").item(0);
                String oNap = oIdopont.getElementsByTagName("nap").item(0).getTextContent();
                String oTol = oIdopont.getElementsByTagName("tol").item(0).getTextContent();
                String oIg = oIdopont.getElementsByTagName("ig").item(0).getTextContent();

                Node node2 = elem.getElementsByTagName("helyszin").item(0);
                String oHelyszin = node2.getTextContent();

                Node node3 = elem.getElementsByTagName("oktato").item(0);
                String oOktato = node3.getTextContent();

                Node node4 = elem.getElementsByTagName("szak").item(0);
                String oSzak = node4.getTextContent();

                System.out.println("Targy: " + oTargy);
                System.out.println("Ora id: " + oid);
                System.out.println("Ora tipus: " + otipus);
                System.out.println("Nap: " + oNap);
                System.out.println("Tol: " + oTol);
                System.out.println("Ig: " + oIg);
                System.out.println("Helyszin: " + oHelyszin);
                System.out.println("Oktato: " + oOktato);
                System.out.println("Szak: " + oSzak);
            }
        }
    }
}
