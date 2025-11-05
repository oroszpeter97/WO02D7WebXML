package WO02D7_1105.DomParseWO02D7.domWO02D71105;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

class DomModifyWO02D7 {
    public static void main(String argv[]) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException{
        File file = new File("WO02D7_hallgat.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);
        document.getDocumentElement().normalize();
        
        Node hallgato = document.getElementsByTagName("hallgato").item(0);
        NamedNodeMap attributomok = hallgato.getAttributes();
        Node hallgatoAttributum = attributomok.getNamedItem("id");
        hallgatoAttributum.setTextContent("05");
        
        NodeList hallgatoGyerekek = hallgato.getChildNodes();
        for (int i = 0; i < hallgatoGyerekek.getLength(); i++){
            Node node = hallgatoGyerekek.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) node;
                if ("keresztnev".equals(element.getNodeName())){
                    if ("Pál".equals(element.getTextContent())){
                        element.setTextContent("Olivia");
                    }
                }
                if("vezeteknev".equals(element.getNodeName())){
                    if ("Kiss".equals(element.getTextContent())){
                        element.setTextContent("Erős");
                    }
                }
            }
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transform = transformerFactory.newTransformer();
        
        DOMSource dom = new DOMSource();

        System.out.println("--- Szerkesztett dokumentum ---");
        StreamResult streamResult = new StreamResult(System.out);
        transform.transform(dom, streamResult);
    }
}