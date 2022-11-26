import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

/**
 * Abstract class for a clubbers entity.
 * The class defines how each entity will be displayed using GUI components and how it's data behaves.
 * @author Magen Rosenwasser, Atalo Tarafa.
 */
public abstract class ClubAbstractEntity extends JFrame {

    //------------------------------------------- Fields ------------------------------------------

    private final JButton okButton, cancelButton; // Ok and cancel buttons.
    private final JPanel centerPanel; // Cneter panel of frame (for JtextFields and their Jlabels).
    private final ButtonsHandler handler; // Handler for the frame's buttons.
    private final JLabel errorSymbol; // Asterisk symbol which changes locations on centerPanel depanding on validation.
    private JPanel errorLocation; // Remembers the panel where error occured.
    private NightClubMgmtApp clubManager; // The club manager this class is called from.

    //------------------------------------------- Constructors ------------------------------------

    /**
     * Default constructor. Parameterless.
     */
    public ClubAbstractEntity() {
        errorSymbol = new JLabel("*");
        errorSymbol.setForeground(Color.red);
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        centerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel southPanel = new JPanel();
        handler = new ButtonsHandler();
        okButton.addActionListener(handler);
        cancelButton.addActionListener(handler);
        setLayout(new BorderLayout());
        southPanel.add(okButton);
        southPanel.add(cancelButton);
        add(southPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevents closing of the frame.
        setResizable(false); // Prevents resizing of the frame.
    }

    //------------------------------------------- Abstract Methods --------------------------------

    /**
     * Checks if it's an empty class.
     * it's enough to check id becuase if it's null - class didn't pass validation,
     * thus is considered empty.
     * @return True If this instance is empty, false otherwise.
     */
    public abstract boolean isEmpty();

    /**
     * An abstract method that defines an interface for "matching."
     * Each inheritor is required to implement this method that receives a search key
     * (String type) and returns true / false whether or not this key matches the
     * customer's unique ID.
     * @param key The identification credential to match with.
     * @return True if a clubber with this key exists, false otherwise.
     */
    public abstract boolean match(String key);

    /**
     * Used to check if matching identification credentials exists for a different clubber.
     * Implemented by inheritors for different key credentials that they use for identification.
     * Must use its overloaded function for asking the manager directly,
     * {@link #duplicateCredentialCheck(String key)} to get the credentials for validation.
     * @return True if a different clubber exists, false otherwise.
     */
    protected abstract boolean duplicateCredentialCheck();

    /**
     * An abstract method that defines an interface for validation.
     * Each inheritor is required to implement this method which returns true / false
     * if the validation of the contents of all it's JTextFields
     * (according to the criteria listed below) has passed / failed.
     * @return True if validation passed, false otherwise.
     */
    protected abstract boolean validateData();

    /**
     * An abstract method that defines an interface for saving The information in the "Belly."
     * Every inheritor is required to implement this method that performs saving the information from
     * The various JTextFields (belonging to it!) To the fields kept in it's "belly."
     */
    protected abstract void commit();

    /**
     * An abstract method that defines an interface for rollback.
     * Every inheritor is required to implement this method which performs a rollback of data -
     * that is, all the information stored in it's "belly" is copied to his appropriate JTextFields
     * (no matter what was in those JTextFields before!).
     */
    protected abstract void rollBack();

    //------------------------------------------- Methods -----------------------------------------

    /**
     * Places red asterisk representing an errored validation of a certain text field.
     * @param jp The JPanel reference of a failed validated field.
     */
    protected void addAsterisk(JPanel jp) {
        errorLocation = jp;
        errorLocation.add(errorSymbol); // Set asterisk near it.
        errorLocation.revalidate();
        errorLocation.repaint();
    }

    /**
     * Removes red asterisk representing an errored validation field.
     * Used before revalidation of fields and after a successful commit.
     */
    protected void removeAsterisk() {
        if (errorLocation == null)
            return;
        errorLocation.remove(errorSymbol);
        errorLocation.revalidate();
        errorLocation.repaint();
    }

    /**
     * Adds GUI components to the center panel of the frame.
     * @param guiComponent The GUI component to be placed in the center panel.
     */
    protected void addToCenter(Component guiComponent) {
        centerPanel.add(guiComponent);
    }

    /**
     * Toggles cancel button clickability.
     * @param safeToEnable True if safe to allow, false to disable.
     */
    protected void toggleCancelButton(boolean safeToEnable) {
        cancelButton.setEnabled((safeToEnable ? true : false));
    }

    /**
     * Asks the club manager if a different instance of a clubber with this credential
     * exists who is not this instance.
     * Called by inheritors inside implimintation of an overloaded parameterless version:
     * {@link #duplicateCredentialCheck()}.
     * @param key Credential to check.
     * @return True if different clubber with this credential exists, false otherwise.
     */
    protected boolean duplicateCredentialCheck(String key) {
        return clubManager.duplicateKeyCheck(key, this);
    }

    /**
     * Sets the current Club manager of this class.
     * Used by inheritors mostly.
     * @param clubManager The club who manages this person.
     */
    protected void setClubManager(NightClubMgmtApp clubManager) {
        this.clubManager = clubManager;
    }

    //------------------------------------------- Nested classes ----------------------------------

    /**
     * Nested class. Handles the behaviour of the "Ok" and "Cancel" buttons.
     * @author Magen Rosenwasser, Atalo Tarafa.
     */
    private class ButtonsHandler implements ActionListener, Serializable {

        /**
         * Handles The actions to be taken when a certain button is pressed.
         * @param e The pressed button.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == okButton) {
                if(validateData() && !duplicateCredentialCheck()) { // If regex is proper and no other member with same key credentials exists.
                    commit();
                    setVisible(false);
                }
                else
                    return;
            }
            else
                if(e.getSource() == cancelButton) {
                    rollBack(); // revert old data to the text fields.
                    setVisible(false);
                }
        }

    } // ButtonsHandler - End of class definition.

} // ClubAbstractEntity - End of class definition.
