import javax.swing.*;

/**
 * Defines a student club goer's data and GUI structure.
 * @author Magen Rosenwasser, Atalo Tarafa.
 */
public class Student extends Person {

    //------------------------------------------- Fields ------------------------------------------

    private String studentId; // Student Id information field.
    private final JTextField studentIdTxtF; // Text field for student's id.
    private final JPanel studentIdPnl; // Panel for student id.

    //------------------------------------------- Constructors ------------------------------------

    /**
     * Constructor - 5 Parameters.
     * @param id Student's ID number.
     * @param name Student's first name.
     * @param surname Student's last name.
     * @param tel Student's telephone number.
     * @param studentId Student's student ID number.
     */
    public Student(String id, String name, String surname,String tel ,String studentId) {
        super(id, name, surname, tel);

        this.studentId = studentId;

        studentIdTxtF = new JTextField(studentId, 30);
        studentIdPnl = new JPanel();
        studentIdPnl.add(new JLabel("Student ID", JLabel.TRAILING));
        studentIdPnl.add(studentIdTxtF);
        addToCenter(studentIdPnl);

        setTitle("Student");
        setSize(450, 250);
        setLocationRelativeTo(null); // Open window at center of screen.
    }

    /**
     * Constructor - Parameterless.
     * Used for creating a blank clubber entity.
     */
    public Student() {
        this(null, null, null, null, null);
        toggleCancelButton(false);
        setVisible(true);
    }

    /**
     * Constructor for club manager.
     * Receives the managers reference.
     * See class structure: {@link NightClubMgmtApp}.
     * @param clubManager The manager who created this instance of clubber.
     */
    public Student(NightClubMgmtApp clubManager) {
        this(); // Call parameterless constructor.
        setClubManager(clubManager);
    }

    //------------------------------------------- Methods -----------------------------------------
    //------------------------------------------- Overridden Methods ------------------------------

    /**
     * Refers to the manger to check if a clubber with this id or student id exists.
     * Extends {@link Person#duplicateCredentialCheck()} functions for student id.
     * @return True if matching credentials found, false otherwise.
     */
    @Override
    protected boolean duplicateCredentialCheck() {
        boolean idCheck = super.duplicateCredentialCheck();
        if(duplicateCredentialCheck(studentIdTxtF.getText().substring(4))) {
            JOptionPane.showMessageDialog(null,  String.format("Clubber with student id %s already exists.%nType a different one.%n", studentIdTxtF.getText(), JOptionPane.INFORMATION_MESSAGE));
            return true;
        }
        if(idCheck) // If only id is bad.
            return true;
        return false;
    }

    /**
     * Checks for matching key according to certain ID parameters.
     * Extends {@link Person#match(String key)} functions for student id.
     * @return True if key matches either id or student id.
     */
    @Override
    public boolean match(String key) {
        return super.match(key) || key.equals(studentId.substring(4));
    }

    /**
     * Does a regex validation for the text fields.
     * Extends {@link Person#validateData() } functions for student id.
     * @return True if validation passed, false otherwise.
     */
    @Override
    protected boolean validateData() {
        if(super.validateData()) { // First, validate native fields.
            if (!studentIdTxtF.getText().matches("[A-Z]{3}/[1-9]\\d{4}")) { // If passed, validate special field.
                addAsterisk(studentIdPnl);
                return false; // Validation failed at special field.
            }
            return true; // All validations passed
        }
        return false; // If native fields validation failed.
    }

    /**
     * Resets textfields according to internal data.
     * Extends {@link Person#rollBack()} for student id.
     */
    @Override
    protected void rollBack() {
        super.rollBack();
        studentIdTxtF.setText(studentId);
    }

    /**
     * Replaces internal data according to text fields.
     * Extends {@link Person#commit()} for student id.
     */
    @Override
    protected void commit() {
        super.commit();
        studentId = studentIdTxtF.getText();
    }

} // Student - End of class definition. 
