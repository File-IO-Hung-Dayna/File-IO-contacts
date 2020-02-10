import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ContactsManagerApp {
    public List<Contact> contactList;

    public ContactsManagerApp() {
        this.contactList = new ArrayList<>();
    }


    public static void main(String[] args) {
        ContactsManagerApp app = new ContactsManagerApp();

        String directory = "contactsDir";
        String filename = "contacts.txt";

        Path contactsDirectory = Paths.get(directory);
        System.out.println(contactsDirectory.toAbsolutePath());
        Path contactsFilename = Paths.get(directory, filename);

        /*Created directory and files*/
        app.createFiles(contactsDirectory, contactsFilename);
        /*Reads the file and creates the contact list on the app class*/
        app.readContactsFile(contactsFilename);
        app.addContact();

        for (Contact contact : app.contactList) {
            System.out.println(contact.getFirstName());
        }
        app.writeContacts(app.contactList, contactsFilename);

    }

    public void writeContacts(List<Contact> contactsList, Path contactsFilename) {
        try {
            Files.writeString(contactsFilename, "", StandardOpenOption.TRUNCATE_EXISTING);
            for (Contact contact : contactsList) {
                Files.writeString(contactsFilename, contact.getFirstName() + " " + contact.getLastName() + "," + contact.getPhoneNumber() + "\n", StandardOpenOption.APPEND);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void addContact() {
        String firstName = JOptionPane.showInputDialog("Please enter the contact's first name.");
        String lastName = JOptionPane.showInputDialog("Please enter the contact's last name.");
        String phoneNumber = JOptionPane.showInputDialog("Please enter the contact's phone number.");
        this.contactList.add(new Contact(firstName, lastName, phoneNumber));
    }

    public void readContactsFile(Path contactsFilename) {
        try {
            Scanner scanner = new Scanner(new File(contactsFilename.toString()));
            String firstName, lastName, phoneNumber;
            String[] lineArray;
            while (scanner.hasNextLine()) {
                lineArray = scanner.nextLine().split(",");
                firstName = lineArray[0].substring(0, lineArray[0].indexOf(" "));
                lastName = lineArray[0].substring(lineArray[0].indexOf(" ") + 1);
                phoneNumber = lineArray[1];
                this.contactList.add(new Contact(firstName, lastName, phoneNumber));
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    public void createFiles(Path contactsDirectory, Path contactsFilename) {
        try {
            if (Files.notExists(contactsDirectory)) {
                Files.createDirectories(contactsDirectory);
                System.out.println("Directory created");
            }
            if (Files.notExists(contactsFilename)) {
                Files.createFile(contactsFilename);
                System.out.println("file created");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


}
