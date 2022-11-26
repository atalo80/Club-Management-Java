import javax.swing.*;

/**
 * Defines a club goer's data and GUI structure.
 * @author Magen Rosenwasser, Atalo Tarafa.
 */
public class Person extends ClubAbstractEntity {

    //------------------------------------------- Fields ------------------------------------------

    private String id, name, surname, tel; // Clubber's personal information.
    private final JTextField idTxtF, nameTxtF, surnameTxtF, telTxtF; // Text fields for personal information.
    private final JPanel idPnl, namePnl, surnamePnl, telPnl; // Panels for every data field.

    //------------------------------------------- Constructors ------------------------------------

    /**
     * Constructor - 4 Parameters.
     * Uses an overloaded version of {@link #setTitle(String)} to name it's frame.
     * @param id Person's ID number.
     * @param name Person's first name.
     * @param surname Person's last name.
     * @param tel Person's telephone number.
     */
    public Person(String id, String name, String surname, String tel) {

        this.id = id;
        this.name = name;
        this.surname = surname;
        this.tel = tel;

        idTxtF = new JTextField(id, 30);
        nameTxtF = new JTextField(name, 30);
        surnameTxtF = new JTextField(surname, 30);
        telTxtF = new JTextField(tel, 30);

        idPnl = new JPanel();
        namePnl = new JPanel();
        surnamePnl = new JPanel();
        telPnl = new JPanel();

        createFieldPanels();

        setTitle("Person");
        setSize(450,220);
        setLocationRelativeTo(null); // Open window at center of screen.
    }

    /**
     * Constructor - Parameterless.
     * Used for creating a blank clubber entity.
     * Uses {@link ClubAbstractEntity#toggleCancelButton(boolean safeToEnable)} to disable cancel button
     * on newly created Person clubber.
     */
    public Person() {
        this(null,null,null,null);
        toggleCancelButton(false);
        setVisible(true); // It is called when creating a new member, so an empty frame shall popup.
    }

    /**
     * Constructor for club manager.
     * Receives the managers reference.
     * See class structure: {@link NightClubMgmtApp}.
     * Refers manager to memory using {@link ClubAbstractEntity#setClubManager(NightClubMgmtApp clubManager)}.
     * @param clubManager The manager who created this instance of clubber.
     */
    public Person(NightClubMgmtApp clubManager) {
        this(); // Call parameterless constructor.
        setClubManager(clubManager);
    }

    //------------------------------------------- Methods -----------------------------------------

    /**
     * Checks if this class is considered empty.
     * Since all fields need to be validated and commited at the same time,
     * it's enough to check the id
     * @return True if this instance is considered empty, false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return id == null;
    }

    /**
     * Create panels with labels for each text field and add to the center panel of the frame.
     * Places each panel in the center panel using {@link ClubAbstractEntity#addToCenter(Component guiComponent)}.
     */
    private void createFieldPanels() {
        JPanel[] panels = { idPnl, namePnl, surnamePnl, telPnl };
        JTextField[] txtFields = { idTxtF, nameTxtF, surnameTxtF, telTxtF };
        String[] labels = { "ID", "Name", "Surname", "Tel" };
        for (int i = 0; i < panels.length; i++) {
            panels[i].add(new JLabel(labels[i], JLabel.TRAILING));
            panels[i].add(txtFields[i]);
            addToCenter(panels[i]);
        }
    }

    //------------------------------------------- Overridden Methods ------------------------------

    /**
     * Refers to the manger to check if a clubber with this id exists.
     * Uses {@link ClubAbstractEntity#duplicateCredentialCheck(String key)} to ask the manager directly.
     * @return True if matching credentials found, false otherwise.
     */
    @Override
    protected boolean duplicateCredentialCheck() {
        if(duplicateCredentialCheck(idTxtF.getText())) {
            JOptionPane.showMessageDialog(null, String.format("Clubber with same id %s already exists.%nType a different one.%n", idTxtF.getText(), JOptionPane.INFORMATION_MESSAGE));
            return true;
        }
        return false;
    }

    /**
     * Returns true if given value of key matches id value.
     * Uses {@link java.lang.String#equals(Object anObject)} for checking key matching id.
     * @param key Key to be matched with id.
     * @return True if key matches the id.
     */
    @Override
    public boolean match(String key) {
        return key.equals(id);
    }

    /**
     * Validates the JTextField data values using regex pattern matching.
     * Uses {@link java.lang.String#matches(String regex)} for comparing string content with given regex patterns.
     * Uses {@link ClubAbstractEntity#addAsterisk(JPanel jp)} to mark faulty fields.
     * @return True if validation passed, false otherwise.
     */
    @Override
    protected boolean validateData() {
        removeAsterisk(); // Remove previous red asterisk of error before revalidation.
        String[] pattern = { "\\d-\\d{7}[|][1-9]",
                "[A-Z][a-z]+",
                "([A-Z][a-z]*['-]?)+",
                "\\+[(][1-9]\\d{0,2}[)][1-9]\\d{0,2}-[1-9]\\d{6}" };
        String[] textFidData = { idTxtF.getText(),
                nameTxtF.getText(),
                surnameTxtF.getText(),
                telTxtF.getText() };
        JPanel[] panels = { idPnl, namePnl, surnamePnl, telPnl };      
        for(int i = 0; i < pattern.length; i++)
            if (!textFidData[i].matches(pattern[i])) {
                addAsterisk(panels[i]); // Sets asterisk at given error location.
                return false;
            }
        return true;
    }

    /**
     * Sets the string data according to JtextField inputs.
     * Uses {@link ClubAbstractEntity#toggleCancelButton(boolean safeToEnable)}
     * to enable cancel button after successful commit.
     */
    @Override
    protected void commit() {
        toggleCancelButton(true); // After first commit cancel button can be enabled.
        id = idTxtF.getText();
        name = nameTxtF.getText();
        surname = surnameTxtF.getText();
        tel = telTxtF.getText();
    }

    /**
     * Resets the JtextField inputs according to string data.
     * Uses {@link ClubAbstractEntity#removeAsterisk()} to remove faulty field mark
     * (if exists).
     */
    @Override
    protected void rollBack() {
        removeAsterisk();
        idTxtF.setText(id);
        nameTxtF.setText(name);
        surnameTxtF.setText(surname);
        telTxtF.setText(tel);
    }

    /**
     * Sets the title of the frame by clubber type + "Clubber's Data".
     * Overrides using the {@link java.awt.Frame#setTitle(String title)}.
     * @param typeName The type of clubber as string.
     */
    @Override
    public void setTitle(String typeName) {
        super.setTitle(typeName + " Clubber's Data");
    }

} // Person - End of class definition.
