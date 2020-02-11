import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ContactsManagerApp {
    public List<Contact> contactList;

    public ContactsManagerApp() {
        this.contactList = new ArrayList<>();
    }

    public static void main(String[] args) {
        ContactsManagerApp app = new ContactsManagerApp();
        ImageIcon codey = new ImageIcon("src/codey.png");
        String directory = "contactsDir";
        String filename = "contacts.txt";
        Path contactsDirectory = Paths.get(directory);
        Path contactsFilename = Paths.get(directory, filename);
        /*Created directory and files*/
        app.createFiles(contactsDirectory, contactsFilename);
        /*Reads the file and creates the contact list on the app class*/
        app.readContactsFile(contactsFilename);
        /*Prompts user interaction*/
        while (app.promptUser()) ;
        /*Writes to the document before close and show's a closing message*/
        app.writeContacts(app.contactList, contactsFilename);
        JOptionPane.showMessageDialog(null, "Thanks for using our Contacts Manager App!!", "Goodbye!", JOptionPane.PLAIN_MESSAGE, codey);
    }

    /*Displays the search prompt and the dropdown to select the filtered names*/
    public void searchDropDown() {
        String firstName, lastName, searchResponse;
        String[] namesArray;
        String searchName = JOptionPane.showInputDialog(null, "Please enter a name to search.");
        String[] searchArrayKeywords = searchName.split(" ");
        List<Contact> searchedList = new ArrayList<>();
        int index = 0;
        for (Contact contact : this.contactList) {
            for (String item : searchArrayKeywords) {
                if (contact.getFirstName().toUpperCase().contains(item.toUpperCase()) || contact.getLastName().toUpperCase().contains(item.toUpperCase())) {
                    searchedList.add(new Contact(contact.getFirstName(), contact.getLastName(), contact.getPhoneNumber()));
                }
            }
        }
        namesArray = new String[searchedList.size()];
        for (Contact contact : searchedList) {
            namesArray[index] = (contact.getFirstName() + " " + contact.getLastName()).toUpperCase();
            index++;
        }
        searchResponse = (String) JOptionPane.showInputDialog(null, "Select an option:", "Search Dropdown", JOptionPane.PLAIN_MESSAGE, null, namesArray, searchedList.get(0));
        firstName = searchResponse.substring(0, searchResponse.lastIndexOf(" "));
        lastName = searchResponse.substring(searchResponse.lastIndexOf(" ") + 1);
        viewContacts(firstName, lastName);
    }

    /*Displays the users available to delete*/
    public void deleteContact() {
        String[] namesArray = new String[this.contactList.size()];
        String stringResponse;
        int index = 0, response;
        for (Contact contact : this.contactList) {
            namesArray[index] = (index + 1) + ". " + (contact.getFirstName() + " " + contact.getLastName()).toUpperCase();
            index++;
        }
        stringResponse = (String) JOptionPane.showInputDialog(null, "Select an option:", "Delete user Dropdown", JOptionPane.PLAIN_MESSAGE, null, namesArray, this.contactList.get(0));
        response = Integer.parseInt(stringResponse.substring(0, 1));
        this.contactList.remove(response - 1);
    }

    /*Prompts the user to select an option via JOptionPane*/
    public boolean promptUser() {
        ImageIcon basketball = new ImageIcon("src/channel.png");
        Object[] possibilities = {"1. View contacts.", "2. Add a new contact.", "3. Search a contact by name.", "4. Delete an existing contact.", "5. Exit."};
        String stringResponse;
        int option;
        try {
            stringResponse = (String) JOptionPane.showInputDialog(null, "Select an option:", "User Options", JOptionPane.PLAIN_MESSAGE, basketball, possibilities, possibilities[0]);
            option = Integer.parseInt(stringResponse.substring(0, 1));
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
        } catch (NullPointerException npe) {
            return false;
        }
    }

    /*Writes to the file based on passed in list and file path*/
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

    /*Adds each contact to the list of names based on input provided.*/
    public void addContact() {
        String firstName = JOptionPane.showInputDialog("Please enter the contact's first name:");
        String lastName = JOptionPane.showInputDialog("Please enter the contact's last name:");
        String phoneNumber;
        boolean correctEntry = false;
        int overrideResponse = -1;
        int index = 0;
        do {
            try {
                phoneNumber = JOptionPane.showInputDialog(null, "Please enter the contact's phone number (###-###-#### for 10-digit or ###-#### for 7-digit):", "Phone Number Entry", JOptionPane.PLAIN_MESSAGE);
                if (phoneNumber.matches("[1-9][0-9][0-9]-[0-9][0-9][0-9][0-9]|[1-9][0-9][0-9]-[1-9][0-9][0-9]-[0-9][0-9][0-9][0-9]")) {
                    for (Contact contact : this.contactList) {
                        if (contact.getFirstName().equalsIgnoreCase(firstName) && contact.getLastName().equalsIgnoreCase(lastName)) {
                            overrideResponse = JOptionPane.showConfirmDialog(null, "An entry is already associated with that first/last name. Do you want to replace the entry?", "Override?", JOptionPane.YES_NO_OPTION);
                            // If User clicked no then overrideResponse is equal to 1
                            if (overrideResponse == 1) {
                                return;
                            }
                            // If user clicks yes, then overrideResponse is equal to 0
                            else if (overrideResponse == 0) {
                                this.contactList.remove(index);
                                this.contactList.add(new Contact(firstName, lastName, phoneNumber));
                                JOptionPane.showMessageDialog(null, "Contact Added!");
                                this.contactList.sort(Contact::compareTo);
                                correctEntry = true;
                                break;
                            }
                        }
                        index++;
                    }
                    if (overrideResponse == -1) {
                        this.contactList.add(new Contact(firstName, lastName, phoneNumber));
                        JOptionPane.showMessageDialog(null, "Contact Added!");
                        this.contactList.sort(Contact::compareTo);
                        correctEntry = true;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Error with formatting", "Warning!!!", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NullPointerException npe) {
                break;
            }
        } while (!correctEntry);
    }

    /*Reads the file and stores data into the contacts list*/
    public void readContactsFile(Path contactsFilename) {
        try {
            Scanner scanner = new Scanner(new File(contactsFilename.toString()));
            String firstName, lastName, phoneNumber;
            String[] lineArray;
            while (scanner.hasNextLine()) {
                lineArray = scanner.nextLine().split(",");
                firstName = lineArray[0].substring(0, lineArray[0].lastIndexOf(" "));
                lastName = lineArray[0].substring(lineArray[0].lastIndexOf(" ") + 1);
                phoneNumber = lineArray[1];
                this.contactList.add(new Contact(firstName, lastName, phoneNumber));
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    /*Creates the files if not already created*/
    public void createFiles(Path contactsDirectory, Path contactsFilename) {
        try {
            if (Files.notExists(contactsDirectory)) {
                Files.createDirectories(contactsDirectory);
            }
            if (Files.notExists(contactsFilename)) {
                Files.createFile(contactsFilename);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /*View all contacts*/
    public void viewContacts() {
        ImageIcon basketball = new ImageIcon("src/channel.png");
        StringBuilder displayString = new StringBuilder();
        displayString.append("CONTACT LIST\n");
        for (Contact contact : this.contactList) {
            displayString.append(contact.getFirstName()).append(" ").append(contact.getLastName()).append(": ").append(contact.getPhoneNumber()).append("\n");
        }
        JOptionPane.showMessageDialog(null, displayString, "Your Contact's Details", JOptionPane.PLAIN_MESSAGE, basketball);
    }

    /*View individual contacts based on first and last name*/
    public void viewContacts(String firstName, String lastName) {
        ImageIcon basketball = new ImageIcon("src/channel.png");
        for (Contact contact : this.contactList) {
            if (contact.getFirstName().equalsIgnoreCase(firstName) && contact.getLastName().equalsIgnoreCase(lastName)) {
                JOptionPane.showMessageDialog(null, String.format("Name: %s %s\nPhone Number: %s", contact.getFirstName(), contact.getLastName(), contact.getPhoneNumber()), "Your Contact's Details", JOptionPane.PLAIN_MESSAGE, basketball);
            }
        }
    }
}
