import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

/**
 * Main application class for the N.K club in Nahariya.
 * @author Magen Rosenwasser, Atalo Tarafa.
 */
public class NightClubMgmtApp extends JFrame implements ActionListener {
    
    //------------------------------------------- Fields ------------------------------------------

    private ArrayList<ClubAbstractEntity> clubbers; // Night-Club Regular Customers Repository
    private final JComboBox<String> memberTpyesCmb; // Combo box of member types for addition.
    private final JButton searchButton, addButton, exitButton; // Search and add members buttons.

    //------------------------------------------- Constructors ------------------------------------

    /**
     * Constructor - Parameterless.
     * loads clubber list from file using {@link #loadClubbersDBFromFile()}.
     */
    public NightClubMgmtApp() {
        clubbers = new ArrayList<ClubAbstractEntity>();
        loadClubbersDBFromFile();
        
        String[] memberTypes = { "Person", "Soldier", "Student" }; // List of member types for combo box.

        setLayout(new FlowLayout());

        JLabel SearchLbl = new JLabel("Search club member by ID:");
        searchButton = new JButton("Search");
        JLabel addMemberLbl = new JLabel("Add a new club member:");
        memberTpyesCmb = new JComboBox<String>(memberTypes);
        addButton = new JButton("Add");
        JLabel closeAppLbl = new JLabel("Close app and save data:");
        exitButton = new JButton("Save & exit");       
        
        // Some componenets are to be set to the same dimensions
        Component[] guiComponents = {SearchLbl, searchButton, addMemberLbl, closeAppLbl, exitButton};
        for(Component c : guiComponents) {
            c.setPreferredSize(new Dimension(160,25));
        }

        // Can't add in loop, because some coponenets are to be in certain order in a different size.
        add(SearchLbl);
        add(searchButton);
        add(addMemberLbl);
        memberTpyesCmb.setPreferredSize(new Dimension(80,25));
        add(memberTpyesCmb);
        addButton.setPreferredSize(new Dimension(75,25));
        add(addButton);
        add(closeAppLbl);    
        add(exitButton);

        JButton[] buttons = {searchButton, addButton, exitButton};
        for(JButton b : buttons) {
            b.addActionListener(this);
        }

        setTitle("BK Club manager");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevents closing of the frame. Force save of club data to file.
        setResizable(false); // Prevents resizing of the frame.
        setSize(400, 140);
        setLocationRelativeTo(null); // Open window at center of screen
        setVisible(true);
    }

    //------------------------------------------- Methods -----------------------------------------

    /**
     * Checks if a different clubber with same credential exists.
     * Iterates over polymporphic instances of {@link ClubAbstractEntity} and uses
     * {@link ClubAbstractEntity#match(String key)} impliminations of each instance.
     * @param key The credential which to check with.
     * @param c The newly created entity who wants to use given credential.
     * @return True if a different clubber with same credential exists, false otherwise.
     */
    public boolean duplicateKeyCheck(String key, ClubAbstractEntity c) {
        for (ClubAbstractEntity clubber : clubbers)
            if (clubber != c &&  clubber.match(key)) // Does a different club member with same key credential exists?
                return true;
        return false; // No clubber with same id was found.
    }

    /**
     * Search for an existing clubber via given key.
     * Uses {@link #writeClubbersDBtoFile()} to create a new file if doesn't exist.
     */
    private void manipulateDB() {
        boolean found = false;
        while (true) {
            String inputStr = JOptionPane.showInputDialog("Please Enter The Clubber's Key ");
            if(inputStr == null) { //If input dialog button "cancel" was pressed.
                writeClubbersDBtoFile();
                return;
            }
            for (ClubAbstractEntity clubber : clubbers)
                if (clubber.match(inputStr)) {
                    found = true;
                    clubber.setLocationRelativeTo(null);
                    clubber.setVisible(true);
                    clubber.toFront();
                    clubber.requestFocus();
                    break;
                }
            if (!found)
                JOptionPane.showMessageDialog(null,  String.format("Clubber with key %s does not exist%n" , inputStr, JOptionPane.INFORMATION_MESSAGE));
            else {
                found = !found;
                return;
                }
        }
    } // End of method - manipulateDB

    /**
     * Load clubbers list from a binary file save to clubbers arrayList. 
     * Uses {@link #cleanEmptyEntries()} to remove faulty (empty) entities after file load.
     */
    @SuppressWarnings("unchecked") // Suppresses readObject cast safety warning for cmd compilation.
    private void loadClubbersDBFromFile() {
        // Read data from file, create the corresponding objects and put them.
        try {
            FileInputStream fis = new FileInputStream("BKCustomers.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            clubbers = (ArrayList<ClubAbstractEntity>)ois.readObject();
            cleanEmptyEntries(); // Delete empty entries.
            ois.close();
            fis.close();
        }
        catch(FileNotFoundException e) {
            return; // If first run, file shouldn't exist anyway.
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog (null, String.format("Error. File cannot be read, or clubbers format was changed.\n" + e, JOptionPane.ERROR_MESSAGE));
        }
        catch(ClassNotFoundException e) {
            JOptionPane.showMessageDialog (null, String.format("Error. Class cannot be found.\n" + e, JOptionPane.ERROR_MESSAGE));
        }
    }

    /**
     * Write clubbers list to a binary file save from clubbers arrayList.
     */
    private void writeClubbersDBtoFile() {
        // Write all the objects’ data in clubbers ArrayList into the file
        try {
            FileOutputStream fos = new FileOutputStream("BKCustomers.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(clubbers);
            oos.close();
            fos.close();
        }
        catch(FileNotFoundException e) {
            JOptionPane.showMessageDialog (null, String.format("Error. File does not exist.\n" + e, JOptionPane.ERROR_MESSAGE));
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog (null, String.format("Error. File cannot be written.\n" + e, JOptionPane.ERROR_MESSAGE));
        }
    }

    /**
     * If previously application was closed in the middle of adding new memebers and didn't pass validation for any of them,
     * They will be added as empty objects - and so must be removed with application's next bootup.
     * Uses {@link java.util.ArrayList#listIterator()} to manipulate list during iteration.
     */
    private void cleanEmptyEntries() {
        ListIterator<ClubAbstractEntity> iter = clubbers.listIterator();
        while(iter.hasNext())
            if(iter.next().isEmpty())
                iter.remove();
    }

    /**
     * Add a new clubber to clubbers arrayList.
     * Creates an instance of one of the following: {@link Person}, {@link Soldier} or {@link Student}.
     * @param memberType Type of member to add as represented by a string.
     */
    private void addNewMember(String memberType) {
        switch (memberType) {
            default:                     
            case "Person":
                clubbers.add(new Person(this));
                break;
            case "Soldier":
                clubbers.add(new Soldier(this));
                break;
            case "Student":
                clubbers.add(new Student(this));
                break;
        }
    }

    //------------------------------------------- Overridden Methods ------------------------------

    /**
     * Listener for this frame's buttons.
     * @param e The clicked button.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton)
            addNewMember(String.valueOf(memberTpyesCmb.getSelectedItem()));
        else if (e.getSource() == searchButton)
            manipulateDB();
        else if (e.getSource() == exitButton) {
            writeClubbersDBtoFile();
            dispose();
            System.exit(0);
        }
    }

    //------------------------------------------- Maib Method -------------------------------------

    /**
     * Main method for this application.
     * @param args arguments (unused).
     */
    public static void main(String[] args) {
        NightClubMgmtApp appliction = new NightClubMgmtApp();
    }

}// NightClubMgmtApp - End of class definition.
