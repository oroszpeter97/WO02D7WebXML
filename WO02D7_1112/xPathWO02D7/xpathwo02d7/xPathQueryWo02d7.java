package WO02D7_1112.xPathWO02D7.xpathwo02d7;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class xPathQueryWo02d7 {
    public static void main(String args[])
    {
        try
        {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse("studentWO02D7.xml");

            document.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();

            String neptunkod = "class";
            NodeList neptunKod = (NodeList) xPath.compile(neptunkod).evaluate(document, XPathConstants.NODESET);

            for(int i = 0; i < neptunKod.getLength(); i++)
            {
                Node node = neptunKod.item(i);
                System.out.println("Aktualis elem: " + node.getNodeName());

                if(node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("student"))
                {
                    Element element = (Element) node;

                    System.out.println("Hallgató id: " + element.getAttribute("id"));
                    System.out.println("Hallgató Keresztnév: " + element.getElementsByTagName("keresztnev").item(0).getTextContent());
                    System.out.println("Hallgató Vezetéknév: "+ element.getElementsByTagName("vezeteknev").item(0).getTextContent());
                    System.out.println("Hallgató Becenév: "+ element.getElementsByTagName("becenev").item(0).getTextContent());
                }
            }
        }
        catch (Exception e)
        {

        }
    }
}
