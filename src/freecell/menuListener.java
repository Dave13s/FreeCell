package freecell;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import javax.swing.ImageIcon;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.text.View;


/* 
 * This is a listener class for the main menu and associated submenu's
 */
public class menuListener implements ActionListener, ItemListener {

    public menuListener() {

    }

    @SuppressWarnings("deprecation")
    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem) (e.getSource());
        String actionDescription = source.getAccessibleContext().getAccessibleDescription();

        // Displays a window asking the user if there sure they want to quit
        // if not return
        if (actionDescription == "Exit") {
            JOptionPane pane = new JOptionPane("Are you sure you want to Exit?");
            Object[] options = new String[]{"Yes", "No"};
            pane.setOptions(options);
            JDialog dialog = pane.createDialog(new JFrame(), "Dilaog");
            dialog.show();
            Object obj = pane.getValue();
            int result = -1;
            for (int k = 0; k < options.length; k++) {
                if (options[k].equals(obj)) {
                    result = k;
                }
            }

            //if the user presses no simply return
            if (result == 1) {
                return;
            }

            System.exit(0);
        }

        if (actionDescription == "New Game") {
            hybrid data = new hybrid(false);
            data.newGame();
        }

        if (actionDescription == "Options") {
            menuMaker menuMaker = new menuMaker();
            menuMaker.makeOptionsWindow();
        }

        if (actionDescription == "Cards") {
            menuMaker menuMaker = new menuMaker();
            menuMaker.makeCardWindow();
        }

        if (actionDescription == "Background") {
            menuMaker menuMaker = new menuMaker();
            menuMaker.makeBackgroundWindow();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void itemStateChanged(ItemEvent e) {
        JMenuItem source = (JMenuItem) (e.getSource());
        System.out.println("ItemChange Source: " + source.getLabel());

    }
}
