import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
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
//        while (app.promptUser());

        app.searchDropDown();
//        app.addContact();

//        for (Contact contact : app.contactList) {
//            System.out.println(contact.getFirstName());
//        }
//        app.writeContacts(app.contactList, contactsFilename);


    }

    public void searchDropDown(){
        String firstName, lastName;
        String searchName = JOptionPane.showInputDialog(null, "Please enter a name to search.");
        String[] searchArrayKeywords = searchName.split(" ");
        List<Contact> searchedList = new ArrayList<>();

        for (Contact contact : this.contactList) {
            for (String item : searchArrayKeywords) {
                if (contact.getFirstName().toUpperCase().contains(item.toUpperCase()) || contact.getLastName().toUpperCase().contains(item.toUpperCase())) {
                    searchedList.add(new Contact(contact.getFirstName(), contact.getLastName(), contact.getPhoneNumber()));
                }
            }
//            if (contact.getFirstName().contains(searchArrayKeywords))
        }

        for (Contact contact : searchedList){
            System.out.println(contact.getFirstName() + " " + contact.getLastName());
        }

        String[] namesArray = new String[searchedList.size()];
        int index = 0;
        for (Contact contact: searchedList) {
            namesArray[index] = (contact.getFirstName() + " " + contact.getLastName()).toUpperCase();
            index++;
        }
        String searchResponse = (String)JOptionPane.showInputDialog(null, "Select an option:", "Customized Dialog", JOptionPane.PLAIN_MESSAGE, null, namesArray, searchedList.get(0));
        firstName = searchResponse.substring(0, searchResponse.indexOf(" "));
        lastName = searchResponse.substring(searchResponse.indexOf(" ") + 1);
        System.out.println(firstName + " " + lastName);
        viewContacts(firstName, lastName);
    }

    public void deleteContact() {
        String[] namesArray = new String[this.contactList.size()];
        int index = 0;
        for (Contact contact: this.contactList) {
            namesArray[index] = (index + 1) + ". " + (contact.getFirstName() + " " + contact.getLastName()).toUpperCase();
            index++;
        }
        String stringResponse = (String)JOptionPane.showInputDialog(null, "Select an option:", "Customized Dialog", JOptionPane.PLAIN_MESSAGE, null, namesArray, this.contactList.get(0));
        int response = Integer.parseInt(stringResponse.substring(0,1));
        this.contactList.remove(response-1);
    }

    public boolean promptUser() {
        //User options
        ImageIcon basketball = new ImageIcon("src/channel.png");
        Object[] possibilities = {"1. View contacts.", "2. Add a new contact.", "3. Search a contact by name.", "4. Delete an existing contact.", "5. Exit."};
        String stringResponse = (String)JOptionPane.showInputDialog(null, "Select an option:", "Customized Dialog", JOptionPane.PLAIN_MESSAGE, basketball, possibilities, possibilities[0]);
        System.out.println(stringResponse);

        int option = Integer.parseInt(stringResponse.substring(0,1));
        System.out.println(option);
        switch (option) {
            case 1:
                this.viewContacts();
                return true;
            case 2:
                this.addContact();
                return true;
            case 3:
                this.searchDropDown();
                return true;
            case 4:
                this.deleteContact();
                return true;
            case 5:
            default:
                return false;
        }
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
        String firstName = JOptionPane.showInputDialog("Please enter the contact's first name:");
        String lastName = JOptionPane.showInputDialog("Please enter the contact's last name:");
        String phoneNumber = JOptionPane.showInputDialog("Please enter the contact's phone number:");
        this.contactList.add(new Contact(firstName, lastName, phoneNumber));
        JOptionPane.showMessageDialog(null, "Contact Added!");
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

    public void viewContacts(){
        System.out.printf("%-21s | %-13s\n", "NAME", "PHONE NUMBER");
        for(Contact contact : this.contactList){
            System.out.printf("%-10s %-10s | %-13s\n", contact.getFirstName(), contact.getLastName(), contact.getPhoneNumber());
        }
    }
    public void viewContacts(String firstName, String lastName){
        for(Contact contact : this.contactList){
            if(contact.getFirstName().equalsIgnoreCase(firstName) && contact.getLastName().equalsIgnoreCase(lastName)){
                System.out.println(contact.getFirstName() + " " + contact.getLastName() + ": " + contact.getPhoneNumber());
            }
        }
    }


}
