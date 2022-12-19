package league.panels;

import league.conectivity.DataProvider;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public abstract class LeaguePanel extends JPanel implements ActionListener{

    protected DataProvider dataProvider;

    private IndexButton firstButton;
    private final JScrollPane scrollPane;
    private final JPanel header;

    public LeaguePanel(String[] columnsNames, String message){
        setLayout(new BorderLayout());

        //HEADER CREATION
        header = new JPanel(new GridLayout(1, columnsNames.length));
        for(String columnsName : columnsNames){
            header.add(new JLabel(columnsName));
        }

        //ELEMENTS LIST JPANEL CREATION (the one with box layout)
        JPanel elementsPanel = new JPanel();
        elementsPanel.add(new JLabel(message));
        firstButton = new IndexButton("", 0);//just so a Resize Listener works properly

        //ScrollPaneCreations
        scrollPane = new JScrollPane(elementsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        //FINISH
        add(header, BorderLayout.PAGE_START);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void changeLeague(DataProvider dataProvider){
        JPanel elementsPanel = new JPanel();
        this.dataProvider = dataProvider;
        elementsPanel.setLayout(new BoxLayout(elementsPanel, BoxLayout.PAGE_AXIS));

        //RESIZE LISTENER ADDITION
        elementsPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) { adjustBorder(); }

            @Override
            public void componentShown(ComponentEvent e) { adjustBorder(); }
        });

        firstButton = fillElementsPanel(dataProvider, elementsPanel);
        scrollPane.setViewportView(elementsPanel);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        int index = ((IndexButton) actionEvent.getSource()).index;
        launchNewWindow(index);
    }

    abstract void launchNewWindow(int index);

    abstract protected IndexButton fillElementsPanel(DataProvider dataProvider, JPanel elementsPanel);


    private void adjustBorder(){
        header.setBorder(BorderFactory.createEmptyBorder(0,0,0,
                firstButton.getWidth() + scrollPane.getVerticalScrollBar().getWidth()));
    }
}
