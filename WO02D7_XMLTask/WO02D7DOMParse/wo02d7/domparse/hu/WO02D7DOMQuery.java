package WO02D7_XMLTask.WO02D7DOMParse.wo02d7.domparse.hu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WO02D7DOMQuery {
    // Helper method to print to console and write to file
    private static void printAndWrite(BufferedWriter writer, String s) throws IOException {
        System.out.println(s);
        writer.write(s);
        writer.newLine();
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        // Step 1: Load the XML file
        File file = new File("WO02D7_XML.xml");
        // Step 2: Create a DocumentBuilderFactory and DocumentBuilder
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        // Step 3: Parse the XML file into a Document object
        Document document = documentBuilder.parse(file);
        // Step 4: Normalize the XML structure
        document.getDocumentElement().normalize();

        // Step 5: Open output file for writing results
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("WO02D7_DOMQueryOutput.txt"))) {
            // Step 6: Query and output customers with more than one email
            printAndWrite(writer, "Customers with more than one email:");
            printAndWrite(writer, CustomersWithMoreThanOneEmail(document));

            // Step 7: Query and output books with price greater than 10.0
            printAndWrite(writer, "Books with price greater than 10.0:");
            printAndWrite(writer, BooksWithPriceGreaterThan(document, 10.0));

            // Step 8: Query and output orders with status 'Shipped'
            printAndWrite(writer, "Orders with status 'Shipped':");
            printAndWrite(writer, OrdersWithStatus(document, "Shipped"));

            // Step 9: Query and output authors with more than one book
            printAndWrite(writer, "Authors with more than one book:");
            printAndWrite(writer, AuthorsWithMoreThanOneBooks(document));
        }
    }

    // Finds customers who have more than one email address
    public static String CustomersWithMoreThanOneEmail(Document document) {
        StringBuilder result = new StringBuilder();
        NodeList customers = document.getElementsByTagName("Customer");
        for (int i = 0; i < customers.getLength(); i++) {
            Node customer = customers.item(i);
            NodeList emails = customer.getChildNodes();
            int emailCount = 0;
            // Count the number of <Email> elements for each customer
            for (int j = 0; j < emails.getLength(); j++) {
                Node emailNode = emails.item(j);
                if (emailNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE &&
                    emailNode.getNodeName().equals("Email")) {
                    emailCount++;
                }
            }
            if (emailCount > 1) {
                result.append("Customer ID: ").append(((org.w3c.dom.Element) customer).getAttribute("CustomerId"))
                      .append(" has ").append(emailCount).append(" emails.\n");
            }
        }
        return result.toString();
    }

    // Finds books with price greater than the given threshold and outputs publisher info
    public static String BooksWithPriceGreaterThan(Document document, double priceThreshold) {
        StringBuilder result = new StringBuilder();
        NodeList books = document.getElementsByTagName("Book");
        for (int i = 0; i < books.getLength(); i++) {
            Node book = books.item(i);
            Element priceNode = (Element) ((org.w3c.dom.Element) book).getElementsByTagName("Price").item(0);
            double price = Double.parseDouble(priceNode.getTextContent());
            if (price > priceThreshold) {
                Element titleNode = (Element) ((org.w3c.dom.Element) book).getElementsByTagName("Title").item(0);
                String publisherId = ((org.w3c.dom.Element) book).getAttribute("PublisherId");
                // Find publisher details by PublisherId
                NodeList publishers = document.getElementsByTagName("Publisher");
                String publisherName = "";
                String foundedYear = "";
                String country = "";
                for (int j = 0; j < publishers.getLength(); j++) {
                    Element publisher = (org.w3c.dom.Element) publishers.item(j);
                    if (publisher.getAttribute("PublisherId").equals(publisherId)) {
                        publisherName = publisher.getElementsByTagName("Name").item(0).getTextContent();
                        foundedYear = publisher.getElementsByTagName("FoundedYear").item(0).getTextContent();
                        country = publisher.getElementsByTagName("Country").item(0).getTextContent();
                        break;
                    }
                }
                result.append("Book Title: ").append(titleNode.getTextContent())
                      .append(", Price: ").append(price)
                      .append(", Publisher: ").append(publisherName)
                      .append(", Founded: ").append(foundedYear)
                      .append(", Country: ").append(country)
                      .append("\n");
            }
        }
        return result.toString();
    }

    // Finds orders with the specified status and outputs customer info
    public static String OrdersWithStatus(Document document, String status) {
        StringBuilder result = new StringBuilder();
        NodeList orders = document.getElementsByTagName("Order");
        for (int i = 0; i < orders.getLength(); i++) {
            Element order = (org.w3c.dom.Element) orders.item(i);
            Node statusNode = order.getElementsByTagName("Status").item(0);
            if (statusNode.getTextContent().equalsIgnoreCase(status)) {
                String orderId = order.getAttribute("OrderId");
                String customerId = order.getAttribute("CustomerId");
                String orderDate = order.getElementsByTagName("Date").item(0).getTextContent();

                // Find customer details by CustomerId
                NodeList customers = document.getElementsByTagName("Customer");
                String customerName = "";
                String customerEmail = "";
                for (int j = 0; j < customers.getLength(); j++) {
                    Element customer = (org.w3c.dom.Element) customers.item(j);
                    if (customer.getAttribute("CustomerId").equals(customerId)) {
                        customerName = customer.getElementsByTagName("Name").item(0).getTextContent();
                        Element emailNode = (Element) customer.getElementsByTagName("Email").item(0);
                        if (emailNode != null) {
                            customerEmail = emailNode.getTextContent();
                        }
                        break;
                    }
                }

                result.append("Order ID: ").append(orderId)
                      .append(", Status: ").append(status)
                      .append(", Date: ").append(orderDate)
                      .append(", Customer: ").append(customerName)
                      .append(", Email: ").append(customerEmail)
                      .append("\n");
            }
        }
        return result.toString();
    }

    // Finds authors who have written more than one book
    public static String AuthorsWithMoreThanOneBooks(Document document) {
        StringBuilder result = new StringBuilder();
        NodeList authors = document.getElementsByTagName("Author");
        NodeList baList = document.getElementsByTagName("B-A");
        for (int i = 0; i < authors.getLength(); i++) {
            Element author = (org.w3c.dom.Element) authors.item(i);
            String authorId = author.getAttribute("AuthorId");
            int bookCount = 0;
            // Count the number of books for each author using B-A relationships
            for (int j = 0; j < baList.getLength(); j++) {
                Element ba = (org.w3c.dom.Element) baList.item(j);
                if (ba.getAttribute("AuthorId").equals(authorId)) {
                    bookCount++;
                }
            }
            if (bookCount > 1) {
                String name = author.getElementsByTagName("Name").item(0).getTextContent();
                result.append("Author: ").append(name)
                      .append(" has ").append(bookCount).append(" books.\n");
            }
        }
        return result.toString();
    }
}
