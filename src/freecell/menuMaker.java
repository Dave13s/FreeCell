package freecell;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

/*
 * This class handles the management of the menu
 * it also handles the submenus
 */
public class menuMaker {

    public String tmpBackground;
    public boolean option1 = false;
    public boolean option2 = false;
    public boolean option3 = false;

    // This method handles the menu
    public JMenuBar createMenuBar() {

        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem newGameItem, optionsItem, menuItem, exitItem;

        // Create the menu bar.
        menuBar = new JMenuBar();

        menuBar.setBorder(BorderFactory.createTitledBorder(""));

        // Build the first menu.
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBar.add(menu);

        // New Game item
        newGameItem = new JMenuItem(" New Game ", KeyEvent.VK_T);
        // menuItem.setMnemonic(KeyEvent.VK_T); //used constructor instead
        newGameItem.getAccessibleContext().setAccessibleDescription("New Game");
        newGameItem.addActionListener(new menuListener());
        menu.add(newGameItem);

        // options menu items
        menu.addSeparator();
        optionsItem = new JMenuItem(" Options ", KeyEvent.VK_T);
        optionsItem.getAccessibleContext().setAccessibleDescription("Options");
        optionsItem.addActionListener(new menuListener());
        menu.add(optionsItem);

        // a submenu
        menu.addSeparator();
        submenu = new JMenu(" Appearance ");
        submenu.setMnemonic(KeyEvent.VK_S);
        menuItem = new JMenuItem(" Cards ");
        menuItem.getAccessibleContext().setAccessibleDescription("Cards");
        menuItem.addActionListener(new menuListener());
        submenu.add(menuItem);
        menuItem = new JMenuItem(" Background ");
        menuItem.getAccessibleContext().setAccessibleDescription("Background");
        menuItem.addActionListener(new menuListener());
        submenu.add(menuItem);
        menu.add(submenu);

        // Build second menu in the menu bar.
        menu = new JMenu(" Exit ");
        menu.setMnemonic(KeyEvent.VK_N);
        menu.getAccessibleContext().setAccessibleDescription("Exit");
        menuBar.add(menu);

        exitItem = new JMenuItem(" Exit ", KeyEvent.VK_T);
        exitItem.getAccessibleContext().setAccessibleDescription("Exit");
        exitItem.addActionListener(new menuListener());
        menu.add(exitItem);

        menuBar.setAlignmentX(0);

        return menuBar;
    }

    /*
	 * This method is for the option windows
     */
    public void makeOptionsWindow() {
        String line;

        // these variables are for if a user presses cancel
        boolean finalOption1 = false;
        boolean finalOption2 = false;
        boolean finalOption3 = false;

        // open the config file to set the check in the checkboxes
        try {
            Scanner scanner = new Scanner(new FileReader("options.txt"));
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.contains("option1")) {
                    if (line.contains("true")) {
                        option1 = true;
                        finalOption1 = true;
                    }
                }
                if (line.contains("option2")) {
                    if (line.contains("true")) {
                        option2 = true;
                        finalOption2 = true;
                    }
                }
                if (line.contains("option3")) {
                    if (line.contains("true")) {
                        option3 = true;
                        finalOption3 = true;
                    }
                }
            }
            scanner.close();

        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // each check box is selected if previously set to true in the config
        // file
        JCheckBox checkboxOptions = new JCheckBox("Options1");
        if (option1) {
            checkboxOptions.setSelected(true);
        }
        // make new class for options box
        checkboxOptions.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                // if the box is clicked switch the value
                option1 = !option1;
            }
        });

        // repeat this for each button
        JCheckBox checkboxStatistics = new JCheckBox("Display Statistics");
        if (option2) {
            checkboxStatistics.setSelected(true);
        }
        checkboxStatistics.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                option2 = !option2;
            }
        });

        // repeat this for each button
        JCheckBox checkboxTime = new JCheckBox("Display Time");
        if (option3) {
            checkboxTime.setSelected(true);
        }
        checkboxTime.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                option3 = !option3;
            }
        });

        // display the window
        String message = "Options";
        Object[] params = {message, checkboxOptions, checkboxStatistics, checkboxTime};
        int n = JOptionPane.showConfirmDialog(null, params, "Options", JOptionPane.OK_CANCEL_OPTION);

        // if ok was selected write to the file
        if (n == 0) {
            try {
                FileWriter outFile = new FileWriter("options.txt");
                PrintWriter out = new PrintWriter(outFile);

                if (option1 == true) {
                    out.println("option1 = true");
                } else {
                    out.println("option1 = false");
                }

                if (option2 == true) {
                    out.println("option2 = true");
                } else {
                    out.println("option2 = false");
                }

                if (option3 == true) {
                    out.println("option3 = true");
                } else {
                    out.println("option3 = false");
                }

                hybrid hybrid = new hybrid(false);
                out.print("card = ");
                out.print(hybrid.cardLocation);

                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            option1 = finalOption1;
            option2 = finalOption2;
            option3 = finalOption3;
        }
    }

    /* 
	 * This method is for the appearance windows ie background
     */
    public void makeBackgroundWindow() {
        hybrid hybrid = new hybrid(false);

        JButton b1 = new JButton("Load File");
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Create a file chooser
                final JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(null);

                hybrid hybrid = new hybrid(false);
                if (hybrid.verboseMode) {
                    System.out.println("File loaded in background loader: " + fc.getSelectedFile());
                }

                if (returnVal == 0) {
                    tmpBackground = fc.getSelectedFile().toString();
                }

            }
        });

        // display the window
        String message = "Background";
        Object[] params = {message, b1, hybrid.backgroundImage};
        int n = JOptionPane.showConfirmDialog(null, params, "Options", JOptionPane.OK_CANCEL_OPTION);

        if (n == 0) {
            if (hybrid.verboseMode) {
                System.out.println("User Pressed Ok in Background Box");
            }

            if (tmpBackground != null) {
                hybrid.backgroundImage = tmpBackground;
                hybrid.newBackground();
            }
        } else {
            if (hybrid.verboseMode) {
                System.out.println("User Pressed Cancel");
            }
        }
    }

    /*
	 * Make a window for selecting card types
     */
    public void makeCardWindow() {
        hybrid hybrid = new hybrid(false);
        String cardType1String = "Card Type 1";
        String cardType2String = "Card Type 2";

        JRadioButton cardType1 = new JRadioButton(cardType1String);
        cardType1.setMnemonic(KeyEvent.VK_B);
        cardType1.setActionCommand(cardType1String);
        cardType1.setSelected(true);

        JRadioButton cardType2 = new JRadioButton(cardType2String);
        cardType2.setMnemonic(KeyEvent.VK_C);
        cardType2.setActionCommand(cardType2String);

        ButtonGroup group = new ButtonGroup();
        group.add(cardType1);
        group.add(cardType2);

        String message = "Cards";
        Object[] params = {message, cardType1, cardType2};
        int n = JOptionPane.showConfirmDialog(null, params, "Options", JOptionPane.OK_CANCEL_OPTION);
    }

    /*
	 * This method is for the initial loading of the options from the file to
	 * the program
     */
    public void initialLoadOptions() {
        String line;

        try {
            Scanner scanner = new Scanner(new FileReader("options.txt"));
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.contains("option1")) {
                    if (line.contains("true")) {
                        option1 = true;
                    }
                }
                if (line.contains("option2")) {
                    if (line.contains("true")) {
                        option2 = true;
                    }
                }
                if (line.contains("option3")) {
                    if (line.contains("true")) {
                        option3 = true;
                    }
                }
                if (line.contains("card")) {
                    int i = 0;
                    String tmp = "1";
                    while (line.charAt(i) != '=') {
                        i++;
                    }
                    i++;
                    while (line.length() > i) {
                        tmp = tmp + Character.toString(line.charAt(i));
                        i++;
                    }
                    hybrid hybrid = new hybrid(false);
                    hybrid.cardLocation = tmp;
                }
            }
            scanner.close();

        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
