import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ContactsManagerApp {
    public List<Contact> contactList;
    public ContactsManagerApp(){
        this.contactList = new ArrayList<>();
    }


    public static void main(String[] args) throws FileNotFoundException {
        ContactsManagerApp app = new ContactsManagerApp();

        String directory = "contactsDir";
        String filename = "contacts.txt";

        Path contactsDirectory = Paths.get(directory);
        System.out.println(contactsDirectory.toAbsolutePath());
        Path contactsFile = Paths.get(directory, filename);

        //created directory and files
        app.createFiles(contactsDirectory, contactsFile);

        System.out.println(contactsFile.toString());
        Scanner scanner = new Scanner(new File(contactsFile.toString()));
//        System.out.println(scanner.nextLine());
        String[] lineArray = scanner.nextLine().split(",");
        System.out.println(Arrays.toString(lineArray));
        System.out.println(lineArray[0]);
        System.out.println(lineArray[1]);
    }

    public void createFiles(Path contactsDirectory, Path contactsFile){
        try {
            if(Files.notExists(contactsDirectory)){
                Files.createDirectories(contactsDirectory);
                System.out.println("Directory created");
            }
            if(Files.notExists(contactsFile)){
                Files.createFile(contactsFile);
                System.out.println("file created");
            }
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }





}
