package WO02D7_XMLTask.WO02D7DOMParse.wo02d7.domparse.hu;

import java.io.File;
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

public class WO02D7DomRead {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        // Step 1: Load and parse the XML file
        File file = new File("WO02D7_XML.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);
        document.getDocumentElement().normalize();

        // Step 2: Print the root element name
        System.out.println("Root element: " + document.getDocumentElement().getNodeName());

        // Step 3: Get all direct children of the root element
        Element root = document.getDocumentElement();
        NodeList children = root.getChildNodes();

        // Step 4: Flags to ensure each table header prints only once
        boolean customerHeader = false;
        boolean profileHeader = false;
        boolean orderHeader = false;
        boolean authorHeader = false;
        boolean publisherHeader = false;
        boolean bookHeader = false;
        boolean orderItemHeader = false;
        boolean baHeader = false;

        // Step 5: Iterate through all child elements and print their data in tables
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) child;

                switch (childElement.getNodeName()) {
                    case "Customer":
                        // Print Customer table header if not already printed
                        if (!customerHeader) {
                            System.out.println("\n=== Customer Table ===");
                            System.out.printf("| %-12s | %-20s | %-60s | %-16s |%n", "CustomerId", "Name", "Emails", "RegistrationDate");
                            System.out.println("|--------------|----------------------|--------------------------------------------------------------|------------------|");
                            customerHeader = true;
                        }
                        // Extract and print Customer data
                        String customerId = childElement.getAttribute("CustomerId");
                        String name = childElement.getElementsByTagName("Name").item(0).getTextContent();
                        NodeList emails = childElement.getElementsByTagName("Email");
                        List<String> emailList = new ArrayList<>();
                        for (int j = 0; j < emails.getLength(); j++) {
                            emailList.add(emails.item(j).getTextContent());
                        }
                        String registrationDate = childElement.getElementsByTagName("RegistrationDate").item(0).getTextContent();
                        System.out.printf("| %-12s | %-20s | %-60s | %-16s |%n",
                            customerId,
                            name,
                            String.join(", ", emailList),
                            registrationDate
                        );
                        break;
                    case "Profile":
                        // Print Profile table header if not already printed
                        if (!profileHeader) {
                            System.out.println("\n=== Profile Table ===");
                            System.out.printf("| %-10s | %-12s | %-30s | %-30s | %-70s |%n", "ProfileId", "CustomerId", "Address", "PhoneNumbers", "PaymentMethods");
                            System.out.println("|------------|--------------|--------------------------------|--------------------------------|------------------------------------------------------------------------|");
                            profileHeader = true;
                        }
                        // Extract and print Profile data
                        String profileId = childElement.getAttribute("ProfileId");
                        String profileCustomerId = childElement.getAttribute("CustomerId");
                        Element addressElement = (Element) childElement.getElementsByTagName("Address").item(0);
                        String city = addressElement.getElementsByTagName("City").item(0).getTextContent();
                        String street = addressElement.getElementsByTagName("Street").item(0).getTextContent();
                        String number = addressElement.getElementsByTagName("Number").item(0).getTextContent();
                        NodeList phoneNumbers = childElement.getElementsByTagName("PhoneNumber");
                        List<String> phoneList = new ArrayList<>();
                        for (int j = 0; j < phoneNumbers.getLength(); j++) {
                            phoneList.add(phoneNumbers.item(j).getTextContent().trim());
                        }
                        NodeList paymentMethods = childElement.getElementsByTagName("PaymentMethod");
                        List<String> paymentList = new ArrayList<>();
                        for (int j = 0; j < paymentMethods.getLength(); j++) {
                            paymentList.add(paymentMethods.item(j).getTextContent().trim());
                        }
                        System.out.printf("| %-10s | %-12s | %-30s | %-30s | %-70s |%n",
                            profileId,
                            profileCustomerId,
                            city + ", " + street + " " + number,
                            String.join(", ", phoneList),
                            String.join(", ", paymentList)
                        );
                        break;
                    case "Order":
                        // Print Order table header if not already printed
                        if (!orderHeader) {
                            System.out.println("\n=== Order Table ===");
                            System.out.printf("| %-10s | %-12s | %-12s | %-10s |%n", "OrderId", "CustomerId", "Date", "Status");
                            System.out.println("|------------|--------------|--------------|------------|");
                            orderHeader = true;
                        }
                        // Extract and print Order data
                        String orderId = childElement.getAttribute("OrderId");
                        String orderCustomerId = childElement.getAttribute("CustomerId");
                        String orderDate = childElement.getElementsByTagName("Date").item(0).getTextContent();
                        String orderStatus = childElement.getElementsByTagName("Status").item(0).getTextContent();
                        System.out.printf("| %-10s | %-12s | %-12s | %-10s |%n",
                            orderId,
                            orderCustomerId,
                            orderDate,
                            orderStatus
                        );
                        break;
                    case "Author":
                        // Print Author table header if not already printed
                        if (!authorHeader) {
                            System.out.println("\n=== Author Table ===");
                            System.out.printf("| %-10s | %-25s | %-10s | %-15s |%n", "AuthorId", "Name", "BirthYear", "Nationality");
                            System.out.println("|------------|---------------------------|------------|-----------------|");
                            authorHeader = true;
                        }
                        // Extract and print Author data
                        String authorId = childElement.getAttribute("AuthorId");
                        String authorName = childElement.getElementsByTagName("Name").item(0).getTextContent();
                        String birthYear = childElement.getElementsByTagName("BirthYear").item(0).getTextContent();
                        String nationality = childElement.getElementsByTagName("Nationality").item(0).getTextContent();
                        System.out.printf("| %-10s | %-25s | %-10s | %-15s |%n",
                            authorId,
                            authorName,
                            birthYear,
                            nationality
                        );
                        break;
                    case "Publisher":
                        // Print Publisher table header if not already printed
                        if (!publisherHeader) {
                            System.out.println("\n=== Publisher Table ===");
                            System.out.printf("| %-12s | %-20s | %-12s | %-10s |%n", "PublisherId", "Name", "FoundedYear", "Country");
                            System.out.println("|--------------|----------------------|--------------|------------|");
                            publisherHeader = true;
                        }
                        // Extract and print Publisher data
                        String publisherId = childElement.getAttribute("PublisherId");
                        String publisherName = childElement.getElementsByTagName("Name").item(0).getTextContent();
                        String foundedYear = childElement.getElementsByTagName("FoundedYear").item(0).getTextContent();
                        String country = childElement.getElementsByTagName("Country").item(0).getTextContent();
                        System.out.printf("| %-12s | %-20s | %-12s | %-10s |%n",
                            publisherId,
                            publisherName,
                            foundedYear,
                            country
                        );
                        break;
                    case "Book":
                        // Print Book table header if not already printed
                        if (!bookHeader) {
                            System.out.println("\n=== Book Table ===");
                            System.out.printf("| %-8s | %-12s | %-30s | %-8s | %-20s |%n", "BookId", "PublisherId", "Title", "Price", "ISBN");
                            System.out.println("|----------|--------------|--------------------------------|----------|----------------------|");
                            bookHeader = true;
                        }
                        // Extract and print Book data, add $ after price
                        String bookId = childElement.getAttribute("BookId");
                        String bookPublisherId = childElement.getAttribute("PublisherId");
                        String title = childElement.getElementsByTagName("Title").item(0).getTextContent();
                        String price = childElement.getElementsByTagName("Price").item(0).getTextContent();
                        price = price.trim() + "$";
                        String isbn = childElement.getElementsByTagName("ISBN").item(0).getTextContent();
                        System.out.printf("| %-8s | %-12s | %-30s | %-8s | %-20s |%n",
                            bookId,
                            bookPublisherId,
                            title,
                            price,
                            isbn
                        );
                        break;
                    case "OrderItem":
                        // Print OrderItem table header if not already printed
                        if (!orderItemHeader) {
                            System.out.println("\n=== OrderItem Table ===");
                            System.out.printf("| %-12s | %-10s | %-8s | %-8s | %-8s |%n", "OrderItemId", "OrderId", "BookId", "Quantity", "Total");
                            System.out.println("|--------------|------------|----------|----------|----------|");
                            orderItemHeader = true;
                        }
                        // Extract and print OrderItem data, add $ after total
                        String orderItemId = childElement.getAttribute("OrderItemId");
                        String orderItemOrderId = childElement.getAttribute("OrderId");
                        String orderItemBookId = childElement.getAttribute("BookId");
                        String quantity = childElement.getElementsByTagName("Quantity").item(0).getTextContent();
                        String total = childElement.getElementsByTagName("Total").item(0).getTextContent();
                        total = total.trim() + "$";
                        System.out.printf("| %-12s | %-10s | %-8s | %-8s | %-8s |%n",
                            orderItemId,
                            orderItemOrderId,
                            orderItemBookId,
                            quantity,
                            total
                        );
                        break;
                    case "B-A":
                        // Print Book-Author Connection table header if not already printed
                        if (!baHeader) {
                            System.out.println("\n=== Book-Author Connection Table ===");
                            System.out.printf("| %-8s | %-10s |%n", "BookId", "AuthorId");
                            System.out.println("|----------|------------|");
                            baHeader = true;
                        }
                        // Extract and print Book-Author connection data
                        String baBookId = childElement.getAttribute("BookId");
                        String baAuthorId = childElement.getAttribute("AuthorId");
                        System.out.printf("| %-8s | %-10s |%n",
                            baBookId,
                            baAuthorId
                        );
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
