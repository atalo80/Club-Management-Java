import javax.swing.*;

/**
 * Defines a soldier club goer's data and GUI structure.
 * @author Magen Rosenwasser, Atalo Tarafa.
 */
public class Soldier extends Person {

    //------------------------------------------- Fields ------------------------------------------

    private String personalNum; // Personal number of soldier.
    private final JTextField personalNumTxtF; // Text field for sodlier's personal number.
    private final JPanel personalNumPnl; // Panel for soldier's personal number.

    //------------------------------------------- Constructors ------------------------------------

    /**
     * Constructor - 5 Parameters.
     * @param id Soldier's ID number.
     * @param name Soldier's first name.
     * @param surname Soldier's last name.
     * @param tel Soldier's telephone number.
     * @param personalNum Soldier's personal number.
     */
    public Soldier(String id, String name, String surname, String tel, String personalNum) {
        super(id, name, surname, tel);

        this.personalNum = personalNum;

        personalNumTxtF = new JTextField(personalNum, 30);
        personalNumPnl = new JPanel();
        personalNumPnl.add(new JLabel("Personal NO.", JLabel.TRAILING));
        personalNumPnl.add(personalNumTxtF);
        addToCenter(personalNumPnl);

        setTitle("Soldier");
        setSize(450, 250);
        setLocationRelativeTo(null); // Open window at center of screen.
    }

    /**
     * Constructor - Parameterless.
     * Used for creating a blank clubber entity.
     */
    public Soldier() {
        this(null, null, null, null, null);
        toggleCancelButton(false);
        setVisible(true); // It is called when creating a new member, so an empty frame shall popup.
    }

    /**
     * Constructor for club manager.
     * Receives the managers reference.
     * See class structure: {@link NightClubMgmtApp}.
     * @param clubManager The manager who created this instance of clubber.
     */
    public Soldier(NightClubMgmtApp clubManager) {
        this(); // Call parameterless constructor.
        setClubManager(clubManager);
    }

    //------------------------------------------- Methods -----------------------------------------
    //------------------------------------------- Overridden Methods ------------------------------

    /**
     * Refers to the manger to check if a clubber with this id or personal number exists.
     * Extends {@link Person#duplicateCredentialCheck()} functions for personal number.
     * @return True if matching credentials found, false otherwise.
     */
    @Override
    protected boolean duplicateCredentialCheck() {
        boolean idCheck = super.duplicateCredentialCheck();
        if(duplicateCredentialCheck(personalNumTxtF.getText())) {
            JOptionPane.showMessageDialog(null, String.format("Clubber with personal mum %s already exists.%nType a different one.%n", personalNumTxtF.getText(), JOptionPane.INFORMATION_MESSAGE));
            return true;
        }
        if(idCheck) // If only id is bad.
            return true;
        return false;
    }

    /**
     * Checks for matching key according to certain ID parameters.
     * Extends {@link Person#match(String key)} functions for personal number.
     * @return True if key matches either id or personal number.
     */
    @Override
    public boolean match(String key) {
        return super.match(key) || key.equals(personalNum);
    }

    /**
     * Does a regex validation for the text fields.
     * Extends {@link Person#validateData() } functions for personal number.
     * @return True if validation passed, false otherwise.
     */
    @Override
    protected boolean validateData() {
        if(super.validateData()) { // First, validate native fields.
            if (!personalNumTxtF.getText().matches("[ROC]/[1-9]\\d{6}")) { // If passed, validate special field.
                addAsterisk(personalNumPnl);
                return false; // Validation failed at special field.
            }
            return true; // All validations passed
        }
        return false; // If native fields validation failed.
    }

    /**
     * Resets textfields according to internal data.
     * Extends {@link Person#rollBack()} for personal number.
     */
    @Override
    protected void rollBack() {
        super.rollBack();
        personalNumTxtF.setText(personalNum);
    }

    /**
     * Replaces internal data according to text fields.
     * Extends {@link Person#commit()} for personal number.
     */
    @Override
    protected void commit() {
        super.commit();
        personalNum = personalNumTxtF.getText();
    }

} // Soldier - End of class definition. 
