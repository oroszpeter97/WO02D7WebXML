package WO02D7_XMLTask.WO02D7DOMParse.wo02d7.domparse.hu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class WO02D7DomQuery {
    private static void printAndWrite(BufferedWriter writer, String s) throws IOException {
        System.out.println(s);
        writer.write(s);
        writer.newLine();
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        File file = new File("WO02D7_XML.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);
        document.getDocumentElement().normalize();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("WO02D7_DomQueryOutput.txt"))) {
            printAndWrite(writer, "Customers with more than one email:");
            printAndWrite(writer, CustomersWithMoreThanOneEmail(document));

            printAndWrite(writer, "Books with price greater than 10.0:");
            printAndWrite(writer, BooksWithPriceGreaterThan(document, 10.0));

            printAndWrite(writer, "Orders with status 'Shipped':");
            printAndWrite(writer, OrdersWithStatus(document, "Shipped"));

            printAndWrite(writer, "Authors with more than one book:");
            printAndWrite(writer, AuthorsWithMoreThanOneBooks(document));
        }
    }

    public static String CustomersWithMoreThanOneEmail(Document document) {
        StringBuilder result = new StringBuilder();
        var customers = document.getElementsByTagName("Customer");
        for (int i = 0; i < customers.getLength(); i++) {
            var customer = customers.item(i);
            var emails = customer.getChildNodes();
            int emailCount = 0;
            for (int j = 0; j < emails.getLength(); j++) {
                var emailNode = emails.item(j);
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

    public static String BooksWithPriceGreaterThan(Document document, double priceThreshold) {
        StringBuilder result = new StringBuilder();
        var books = document.getElementsByTagName("Book");
        for (int i = 0; i < books.getLength(); i++) {
            var book = books.item(i);
            var priceNode = ((org.w3c.dom.Element) book).getElementsByTagName("Price").item(0);
            double price = Double.parseDouble(priceNode.getTextContent());
            if (price > priceThreshold) {
                var titleNode = ((org.w3c.dom.Element) book).getElementsByTagName("Title").item(0);
                String publisherId = ((org.w3c.dom.Element) book).getAttribute("PublisherId");
                var publishers = document.getElementsByTagName("Publisher");
                String publisherName = "";
                String foundedYear = "";
                String country = "";
                for (int j = 0; j < publishers.getLength(); j++) {
                    var publisher = (org.w3c.dom.Element) publishers.item(j);
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

    public static String OrdersWithStatus(Document document, String status) {
        StringBuilder result = new StringBuilder();
        var orders = document.getElementsByTagName("Order");
        for (int i = 0; i < orders.getLength(); i++) {
            var order = (org.w3c.dom.Element) orders.item(i);
            var statusNode = order.getElementsByTagName("Status").item(0);
            if (statusNode.getTextContent().equalsIgnoreCase(status)) {
                String orderId = order.getAttribute("OrderId");
                String customerId = order.getAttribute("CustomerId");
                String orderDate = order.getElementsByTagName("Date").item(0).getTextContent();

                var customers = document.getElementsByTagName("Customer");
                String customerName = "";
                String customerEmail = "";
                for (int j = 0; j < customers.getLength(); j++) {
                    var customer = (org.w3c.dom.Element) customers.item(j);
                    if (customer.getAttribute("CustomerId").equals(customerId)) {
                        customerName = customer.getElementsByTagName("Name").item(0).getTextContent();
                        var emailNode = customer.getElementsByTagName("Email").item(0);
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

    public static String AuthorsWithMoreThanOneBooks(Document document) {
        StringBuilder result = new StringBuilder();
        var authors = document.getElementsByTagName("Author");
        var baList = document.getElementsByTagName("B-A");
        for (int i = 0; i < authors.getLength(); i++) {
            var author = (org.w3c.dom.Element) authors.item(i);
            String authorId = author.getAttribute("AuthorId");
            int bookCount = 0;
            for (int j = 0; j < baList.getLength(); j++) {
                var ba = (org.w3c.dom.Element) baList.item(j);
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
