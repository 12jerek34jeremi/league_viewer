package league.panels;

import league.autocomplete.AutoComboBox;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import javax.swing.*;

class InputsPanel extends JPanel {
    private JTextField[] textFields = null;
    private JSpinner[] spinners = null;
    private AutoComboBox[] autoComboBoxes;
    private final Validator validator;
    private final String[] textFieldsLabels, spinnersLabels, autoComboBoxesLabels;

    public InputsPanel(String[] textFieldsLabels, String[] spinnersLabels, String[] autoComboBoxesLabels,
                       String buttonLabel, int textFieldsWidth, int spinnersWidth, Validator validator){
        this.textFieldsLabels = textFieldsLabels;
        this.spinnersLabels = spinnersLabels;
        this.autoComboBoxesLabels = autoComboBoxesLabels;
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
            Pair<String, Triplet<String[], int[], String[]>> filledCheck = checkIfFilled();
            if(filledCheck.getValue0() != null){
                JOptionPane.showMessageDialog(this, filledCheck.getValue0(),
                        "Nieprawidłowe dane", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Triplet<String[], int[], String[]> inputsValues = filledCheck.getValue1();
            String validationResult = validator.check(inputsValues.getValue0(), inputsValues.getValue1(),
                    inputsValues.getValue2());

            if(validationResult == null){
                System.out.println("Tutaj jest wprowadzanie danych pisane jeszcze przez Zuzię.");
            }else{
                JOptionPane.showMessageDialog(this, validationResult,
                        "Nieprawidłowe dane", JOptionPane.WARNING_MESSAGE);
            }
        });
        add(button);
    }

    public void changeLeague(String[][] autoComboBoxesItems){
        for(int i =0; i< autoComboBoxesItems.length; i++){
            autoComboBoxes[i].setKeyWord(autoComboBoxesItems[i]);
        }
    }

    public interface Validator{
        public String check(String[] textFieldsValues, int[] spinnersValues, String[] autoComboBoxesValues);
    }

    private Pair<String, Triplet<String[], int[], String[]>> checkIfFilled(){
        String[] textFieldsValues = null, autoComboBoxesValues = null;
        int[] spinnersValues = null;

        if(textFieldsLabels != null){
            textFieldsValues = new String[textFields.length];
            for (int i = 0; i<textFields.length; i++){
                String text = textFields[i].getText();
                if(text.isEmpty())
                    return new Pair<>("Wypełnij pole o etykiecie:\n\"" + textFieldsLabels[i] + "\"" , null);
                textFieldsValues[i] = text;
            }
        }

        if(spinnersLabels != null){
            spinnersValues = new int[spinners.length];
            for (int i = 0; i<spinners.length; i++){
                try{
                    spinnersValues[i] = Integer.parseInt(spinners[i].getValue().toString());
                }catch (Exception e){
                    return new Pair<>("Wprowadź prawidłową wartość w polu o etykiecie:\n\"" + spinnersLabels[i] + "\"" , null);
                }
            }
        }

        if(autoComboBoxesLabels != null){
            autoComboBoxesValues = new String[autoComboBoxes.length];
            for (int i = 0; i<autoComboBoxes.length; i++){
                Object selectedItem = autoComboBoxes[i].getSelectedItem();
                if(selectedItem == null)
                    return new Pair<>("Wybierz coś z listy w pole o etykiecie:\n\"" + autoComboBoxesLabels[i] + "\"" , null);
                autoComboBoxesValues[i] = selectedItem.toString();
            }
        }

        return new Pair<>(null, new Triplet<>(textFieldsValues, spinnersValues, autoComboBoxesValues));
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
    

}
