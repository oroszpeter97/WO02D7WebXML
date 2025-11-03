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
import org.w3c.dom.NodeList;
import java.time.LocalDate;
import org.xml.sax.SAXException;

public class WO02D7DOMModify {
    private static void printAndWrite(BufferedWriter writer, String s) throws IOException {
        System.out.println(s);
        writer.write(s);
        writer.newLine();
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        // Load the XML file
        File file = new File("WO02D7_XML.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);
        document.getDocumentElement().normalize();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("WO02D7_DOMModifyOutput.txt"))) {
            // Demonstrate adding a new customer
            printAndWrite(writer, "Adding new customer:");
            printAndWrite(writer, AddNewCustomer(document, "Aurelia Starling", "aurelia.starling@example.com"));

            // Demonstrate updating a book price
            printAndWrite(writer, "\nUpdating book price: ");
            printAndWrite(writer, UpdateBookPrice(document, "B001", 29.99));

            // Demonstrate removing an author and related books
            printAndWrite(writer, "\nRemoving author:");
            printAndWrite(writer, RemoveAuthor(document, "A002"));

            // Demonstrate adding a new email to a customer
            printAndWrite(writer, "\nAdd new customer email:");
            printAndWrite(writer, AddNewCustomerEmail(document, "C001", "john.new@example.com"));
        }
    }

    public static String AddNewCustomer(Document document, String name, String email) {
        // Find the highest CustomerId to generate a new unique ID
        NodeList customers = document.getElementsByTagName("Customer");
        int maxId = 0;
        for (int i = 0; i < customers.getLength(); i++) {
            Element cust = (Element) customers.item(i);
            String idStr = cust.getAttribute("CustomerId");
            if (idStr.startsWith("C")) {
                try {
                    int id = Integer.parseInt(idStr.substring(1));
                    if (id > maxId) maxId = id;
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
        }
        String newCustomerId = String.format("C%03d", maxId + 1);

        // Create new Customer element and set attributes
        Element newCustomer = document.createElement("Customer");
        newCustomer.setAttribute("CustomerId", newCustomerId);

        // Add Name element
        Element nameElem = document.createElement("Name");
        nameElem.setTextContent(name);
        newCustomer.appendChild(nameElem);

        // Add Email element
        Element emailElem = document.createElement("Email");
        emailElem.setTextContent(email);
        newCustomer.appendChild(emailElem);

        // Add RegistrationDate element with today's date
        Element regDateElem = document.createElement("RegistrationDate");
        regDateElem.setTextContent(LocalDate.now().toString());
        newCustomer.appendChild(regDateElem);

        // Append the new Customer to the root element
        document.getDocumentElement().appendChild(newCustomer);

        // Find the highest ProfileId to generate a new unique ID
        NodeList profiles = document.getElementsByTagName("Profile");
        int maxProfileId = 0;
        for (int i = 0; i < profiles.getLength(); i++) {
            Element prof = (Element) profiles.item(i);
            String idStr = prof.getAttribute("ProfileId");
            if (idStr.startsWith("P")) {
                try {
                    int id = Integer.parseInt(idStr.substring(1));
                    if (id > maxProfileId) maxProfileId = id;
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
        }
        String newProfileId = String.format("P%03d", maxProfileId + 1);

        // Create new Profile element and set attributes
        Element newProfile = document.createElement("Profile");
        newProfile.setAttribute("ProfileId", newProfileId);
        newProfile.setAttribute("CustomerId", newCustomerId);

        // Add minimal Address element with empty fields
        Element addressElem = document.createElement("Address");
        Element cityElem = document.createElement("City");
        cityElem.setTextContent("");
        Element streetElem = document.createElement("Street");
        streetElem.setTextContent("");
        Element numberElem = document.createElement("Number");
        numberElem.setTextContent("");
        addressElem.appendChild(cityElem);
        addressElem.appendChild(streetElem);
        addressElem.appendChild(numberElem);
        newProfile.appendChild(addressElem);

        // Add empty PhoneNumber and PaymentMethod elements
        Element phoneElem = document.createElement("PhoneNumber");
        phoneElem.setTextContent("");
        newProfile.appendChild(phoneElem);

        Element paymentElem = document.createElement("PaymentMethod");
        paymentElem.setTextContent("");
        newProfile.appendChild(paymentElem);

        // Append the new Profile to the root element
        document.getDocumentElement().appendChild(newProfile);

        return "New customer added: " + name + " (CustomerId: " + newCustomerId + ", ProfileId: " + newProfileId + ")";
    }

    public static String UpdateBookPrice(Document document, String bookId, double newPrice) {
        // Search for the Book element with the given BookId
        NodeList books = document.getElementsByTagName("Book");
        for (int i = 0; i < books.getLength(); i++) {
            Element book = (Element) books.item(i);
            if (bookId.equals(book.getAttribute("BookId"))) {
                // Update the Price element if found
                NodeList prices = book.getElementsByTagName("Price");
                if (prices.getLength() > 0) {
                    Element priceElem = (Element) prices.item(0);
                    priceElem.setTextContent(String.format("%.2f", newPrice));
                    return "Book price updated: " + bookId + " to " + newPrice;
                } else {
                    return "Book found but no <Price> element: " + bookId;
                }
            }
        }
        return "Book not found: " + bookId;
    }

    public static String RemoveAuthor(Document document, String authorId) {
        // Remove the Author element with the given AuthorId
        NodeList authors = document.getElementsByTagName("Author");
        boolean authorRemoved = false;
        for (int i = 0; i < authors.getLength(); i++) {
            Element author = (Element) authors.item(i);
            if (authorId.equals(author.getAttribute("AuthorId"))) {
                author.getParentNode().removeChild(author);
                authorRemoved = true;
                break;
            }
        }
        if (!authorRemoved) {
            return "Author not found: " + authorId;
        }

        // Remove B-A (Book-Author) elements and collect affected BookIds
        NodeList baList = document.getElementsByTagName("B-A");
        java.util.Set<String> affectedBookIds = new java.util.HashSet<>();
        // Iterate backwards to safely remove nodes
        for (int i = baList.getLength() - 1; i >= 0; i--) {
            Element ba = (Element) baList.item(i);
            if (authorId.equals(ba.getAttribute("AuthorId"))) {
                affectedBookIds.add(ba.getAttribute("BookId"));
                ba.getParentNode().removeChild(ba);
            }
        }

        // For each affected BookId, check if it still has any B-A connection
        for (String bookId : affectedBookIds) {
            boolean stillConnected = false;
            NodeList baList2 = document.getElementsByTagName("B-A");
            for (int j = 0; j < baList2.getLength(); j++) {
                Element ba2 = (Element) baList2.item(j);
                if (bookId.equals(ba2.getAttribute("BookId"))) {
                    stillConnected = true;
                    break;
                }
            }
            // If no other B-A connection, remove the Book element
            if (!stillConnected) {
                NodeList books = document.getElementsByTagName("Book");
                for (int k = 0; k < books.getLength(); k++) {
                    Element book = (Element) books.item(k);
                    if (bookId.equals(book.getAttribute("BookId"))) {
                        book.getParentNode().removeChild(book);
                        break;
                    }
                }
            }
        }

        return "Author removed: " + authorId + " and associated books (if no other authors) removed.";
    }

    public static String AddNewCustomerEmail(Document document, String customerId, String newEmail) {
        // Find the Customer element with the given CustomerId
        NodeList customers = document.getElementsByTagName("Customer");
        for (int i = 0; i < customers.getLength(); i++) {
            Element customer = (Element) customers.item(i);
            if (customerId.equals(customer.getAttribute("CustomerId"))) {
                // Add a new Email element to the customer
                Element emailElem = document.createElement("Email");
                emailElem.setTextContent(newEmail);
                customer.appendChild(emailElem);
                return "New email added to customer " + customerId + ": " + newEmail;
            }
        }
        return "Customer not found: " + customerId;
    }
}
