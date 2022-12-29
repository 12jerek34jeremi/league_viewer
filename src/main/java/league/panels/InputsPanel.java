package league.panels;

import league.autocomplete.AutoComboBox;

import javax.swing.*;

class InputsPanel extends JPanel {
    private JTextField[] textFields = null;
    private JSpinner[] spinners = null;
    private AutoComboBox[] autoComboBoxes;
    private Validator validator;

    public InputsPanel(String[] textFieldsLabels, String[] spinnersLabels, String[] autoComboBoxesLabels,
                       String buttonLabel, int textFieldsWidth, int spinnersWidth, Validator validator){
        this.validator = validator;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        if(textFieldsLabels != null){
            textFields = new JTextField[textFieldsLabels.length];
            for(int i = 0; i<textFieldsLabels.length; i++){
                textFields[i] = new JTextField(textFieldsWidth);
                add(createInputPanel(textFieldsLabels[i], textFields[i]));
            }
        }
        if(spinnersLabels != null){
            spinners = new JSpinner[spinnersLabels.length];
            for(int i = 0; i<spinnersLabels.length; i++){
                spinners[i] = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
                ((JSpinner.DefaultEditor) spinners[i].getEditor()).getTextField().setColumns(spinnersWidth);
                add(createInputPanel(spinnersLabels[i], spinners[i]));
            }
        }
        if(autoComboBoxesLabels != null){
            autoComboBoxes = new AutoComboBox[autoComboBoxesLabels.length];
            for(int i = 0; i<autoComboBoxesLabels.length; i++){
                autoComboBoxes[i] = new AutoComboBox();
                autoComboBoxes[i].setPrototypeDisplayValue("To jest naprawde długi tekst, napr");
                add(createInputPanel(autoComboBoxesLabels[i], autoComboBoxes[i]));
            }
        }

        JButton button = new JButton(buttonLabel);
        button.addActionListener(action -> {
            String validationResult = validator.check(textFields, spinners, autoComboBoxes);
            if(validationResult == null){
                System.out.println("Tutaj jest wprowadzanie danych pisane jeszcze przez Zuzię.");
            }else{
                JOptionPane.showMessageDialog(this, validationResult, "Nieprawidłowe dane", JOptionPane.WARNING_MESSAGE);
            }
        });
        add(button);
    }

    public void changeLeague(String[][] autoComboBoxesItems){
        for(int i =0; i< autoComboBoxesItems.length; i++){
            autoComboBoxes[i].setKeyWord(autoComboBoxesItems[i]);
        }
    }

    private static JPanel createInputPanel(String labelText, JComponent inputComponent){
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);

        inputComponent.setMaximumSize(inputComponent.getPreferredSize());
        label.setMaximumSize(label.getPreferredSize());

        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(label);
        panel.add(inputComponent);

        panel.setMaximumSize(panel.getPreferredSize());
        return panel;
    }
    
    public interface Validator{
        public String check(JTextField[] textFields, JSpinner[] spinners, AutoComboBox[] autoComboBoxes);
    }
}
