package WO02D7_XMLTask.WO02D7DOMParse.wo02d7.domparse.hu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

public class WO02D7DOMRead {
    // Helper method to print to both console and file
    private static void printAndWrite(BufferedWriter writer, String s) throws IOException {
        System.out.println(s);
        writer.write(s);
        writer.newLine();
    }

    private static void printfAndWrite(BufferedWriter writer, String format, Object... args) throws IOException {
        String s = String.format(format, args);
        System.out.print(s);
        writer.write(s);
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        // Step 1: Load and parse the XML file
        File file = new File("WO02D7_XML.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);
        document.getDocumentElement().normalize();

        // Open output file for writing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("WO02D7_DOMReadOutput.txt"))) {

            // Step 2: Print the root element name
            printAndWrite(writer, "Root element: " + document.getDocumentElement().getNodeName());

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
                                printAndWrite(writer, "\n=== Customer Table ===");
                                printfAndWrite(writer, "| %-12s | %-20s | %-60s | %-16s |%n", "CustomerId", "Name", "Emails", "RegistrationDate");
                                printAndWrite(writer, "|--------------|----------------------|--------------------------------------------------------------|------------------|");
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
                            printfAndWrite(writer, "| %-12s | %-20s | %-60s | %-16s |%n",
                                customerId,
                                name,
                                String.join(", ", emailList),
                                registrationDate
                            );
                            break;
                        case "Profile":
                            // Print Profile table header if not already printed
                            if (!profileHeader) {
                                printAndWrite(writer, "\n=== Profile Table ===");
                                printfAndWrite(writer, "| %-10s | %-12s | %-30s | %-30s | %-70s |%n", "ProfileId", "CustomerId", "Address", "PhoneNumbers", "PaymentMethods");
                                printAndWrite(writer, "|------------|--------------|--------------------------------|--------------------------------|------------------------------------------------------------------------|");
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
                            printfAndWrite(writer, "| %-10s | %-12s | %-30s | %-30s | %-70s |%n",
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
                                printAndWrite(writer, "\n=== Order Table ===");
                                printfAndWrite(writer, "| %-10s | %-12s | %-12s | %-10s |%n", "OrderId", "CustomerId", "Date", "Status");
                                printAndWrite(writer, "|------------|--------------|--------------|------------|");
                                orderHeader = true;
                            }
                            // Extract and print Order data
                            String orderId = childElement.getAttribute("OrderId");
                            String orderCustomerId = childElement.getAttribute("CustomerId");
                            String orderDate = childElement.getElementsByTagName("Date").item(0).getTextContent();
                            String orderStatus = childElement.getElementsByTagName("Status").item(0).getTextContent();
                            printfAndWrite(writer, "| %-10s | %-12s | %-12s | %-10s |%n",
                                orderId,
                                orderCustomerId,
                                orderDate,
                                orderStatus
                            );
                            break;
                        case "Author":
                            // Print Author table header if not already printed
                            if (!authorHeader) {
                                printAndWrite(writer, "\n=== Author Table ===");
                                printfAndWrite(writer, "| %-10s | %-25s | %-10s | %-15s |%n", "AuthorId", "Name", "BirthYear", "Nationality");
                                printAndWrite(writer, "|------------|---------------------------|------------|-----------------|");
                                authorHeader = true;
                            }
                            // Extract and print Author data
                            String authorId = childElement.getAttribute("AuthorId");
                            String authorName = childElement.getElementsByTagName("Name").item(0).getTextContent();
                            String birthYear = childElement.getElementsByTagName("BirthYear").item(0).getTextContent();
                            String nationality = childElement.getElementsByTagName("Nationality").item(0).getTextContent();
                            printfAndWrite(writer, "| %-10s | %-25s | %-10s | %-15s |%n",
                                authorId,
                                authorName,
                                birthYear,
                                nationality
                            );
                            break;
                        case "Publisher":
                            // Print Publisher table header if not already printed
                            if (!publisherHeader) {
                                printAndWrite(writer, "\n=== Publisher Table ===");
                                printfAndWrite(writer, "| %-12s | %-20s | %-12s | %-10s |%n", "PublisherId", "Name", "FoundedYear", "Country");
                                printAndWrite(writer, "|--------------|----------------------|--------------|------------|");
                                publisherHeader = true;
                            }
                            // Extract and print Publisher data
                            String publisherId = childElement.getAttribute("PublisherId");
                            String publisherName = childElement.getElementsByTagName("Name").item(0).getTextContent();
                            String foundedYear = childElement.getElementsByTagName("FoundedYear").item(0).getTextContent();
                            String country = childElement.getElementsByTagName("Country").item(0).getTextContent();
                            printfAndWrite(writer, "| %-12s | %-20s | %-12s | %-10s |%n",
                                publisherId,
                                publisherName,
                                foundedYear,
                                country
                            );
                            break;
                        case "Book":
                            // Print Book table header if not already printed
                            if (!bookHeader) {
                                printAndWrite(writer, "\n=== Book Table ===");
                                printfAndWrite(writer, "| %-8s | %-12s | %-30s | %-8s | %-20s |%n", "BookId", "PublisherId", "Title", "Price", "ISBN");
                                printAndWrite(writer, "|----------|--------------|--------------------------------|----------|----------------------|");
                                bookHeader = true;
                            }
                            // Extract and print Book data, add $ after price
                            String bookId = childElement.getAttribute("BookId");
                            String bookPublisherId = childElement.getAttribute("PublisherId");
                            String title = childElement.getElementsByTagName("Title").item(0).getTextContent();
                            String price = childElement.getElementsByTagName("Price").item(0).getTextContent();
                            price = price.trim() + "$";
                            String isbn = childElement.getElementsByTagName("ISBN").item(0).getTextContent();
                            printfAndWrite(writer, "| %-8s | %-12s | %-30s | %-8s | %-20s |%n",
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
                                printAndWrite(writer, "\n=== OrderItem Table ===");
                                printfAndWrite(writer, "| %-12s | %-10s | %-8s | %-8s | %-8s |%n", "OrderItemId", "OrderId", "BookId", "Quantity", "Total");
                                printAndWrite(writer, "|--------------|------------|----------|----------|----------|");
                                orderItemHeader = true;
                            }
                            // Extract and print OrderItem data, add $ after total
                            String orderItemId = childElement.getAttribute("OrderItemId");
                            String orderItemOrderId = childElement.getAttribute("OrderId");
                            String orderItemBookId = childElement.getAttribute("BookId");
                            String quantity = childElement.getElementsByTagName("Quantity").item(0).getTextContent();
                            String total = childElement.getElementsByTagName("Total").item(0).getTextContent();
                            total = total.trim() + "$";
                            printfAndWrite(writer, "| %-12s | %-10s | %-8s | %-8s | %-8s |%n",
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
                                printAndWrite(writer, "\n=== Book-Author Connection Table ===");
                                printfAndWrite(writer, "| %-8s | %-10s |%n", "BookId", "AuthorId");
                                printAndWrite(writer, "|----------|------------|");
                                baHeader = true;
                            }
                            // Extract and print Book-Author connection data
                            String baBookId = childElement.getAttribute("BookId");
                            String baAuthorId = childElement.getAttribute("AuthorId");
                            printfAndWrite(writer, "| %-8s | %-10s |%n",
                                baBookId,
                                baAuthorId
                            );
                            break;
                        default:
                            break;
                    }
                }
            }
        } // writer is auto-closed here
    }
}
