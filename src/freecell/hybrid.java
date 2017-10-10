package freecell;

import java.awt.Dimension;

import javacards.Card;
import javacards.Deck;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/*
 * This class is the main section 
 * it handles the main management of the cards
 * the gui
 * allot of shared variables
 * card movement
 * bunch of other stuff
 */
public class hybrid extends JPanel {

    private static final long serialVersionUID = 1L;
    // various things for jLabel and arrays for data storage
    static JLayeredPane layeredPane;
    static public JLabel[] card = new JLabel[52];
    private JLabel back;
    static String[] filename = new String[52];

    // These holds location data for the layout of the cards
    static int numberOfColumns = 8;
    static public int numberOfRows = 30;
    static public int rowDown = 175;
    static public int colBuffer = 24;
    static public int colWidth = 125;
    static public int rowOffset = 30;

    static public int numberOfFreecells = 4;
    static public int freecellsRowBuffer = 14;
    static public int freecellsColBuffer = 10;
    static public int freecellsWidth = 110;

    static public int finalSpaceRowBuffer = 575;
    static public int finalSpaceColBuffer = 10;
    static public int finalSpaceWidth = 110;

    // These are global variables used in the movement of cards
    static int selectedRow = -1;
    static int selectedCol = -1;
    static int selectedCell = -1;

    // This is the main matrix for data storage it holds information about where
    // each card is
    static public String[][] colData = new String[numberOfColumns + 1][numberOfRows];

    // This array is used for holding data about cards in the final section
    static public String[] finalData = new String[4];

    //String backgroundImage = "../cardArt/demoBack3.png";
    public String backgroundImage = "C:\\Users\\Me\\Desktop\\freecell\\src\\cardArt\\demoBack3.png";
    public String cardLocation = "cardArtResized";

    // Stuff for the menu
    JTextArea output;
    JScrollPane scrollPane;

    static public boolean verboseMode = false;

    // This section draws the main window and then invokes the constructor to
    // handle internal windows components
    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("Freecell");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set up the content pane.
        JComponent newContentPane = new hybrid(true);
        newContentPane.setOpaque(true); // content panes must be opaque
        frame.setContentPane(newContentPane);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    // default constructor, will only run if passed true, that way this class
    // can be invoked without reseting card layout
    // This class handle the background and higher level GUI management
    public hybrid(boolean run) {

        if (run) {
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

            // add class for menu
            menuMaker menuMaker = new menuMaker();

            // check the file for default options
            menuMaker.initialLoadOptions();

            // Create and set up the layered pane.
            layeredPane = new JLayeredPane();
            layeredPane.setPreferredSize(new Dimension(1024, 600));

            if (verboseMode) {
                System.out.println("Loading background Image: " + backgroundImage);
            }
            // Define the background image
            final ImageIcon backgroundPic = new ImageIcon(backgroundImage);

            // setup the background image
            back = new JLabel(backgroundPic);
            back.setBounds(0, 0, 1024, 600);
            layeredPane.add(back, new Integer(1), 0);

            if (verboseMode) {
                System.out.println("Start Drawing cards");
            }
            // draw set and paint the cards
            drawDeck();
            populateCols();
            paintCols();

            if (verboseMode) {
                System.out.println("Starting mouse listener");
            }
            // define the mouse listener
            layeredPane.addMouseMotionListener(new clickH());
            layeredPane.addMouseListener(new clickH());

            if (verboseMode) {
                System.out.println("Adding menu");
            }
            // add the menu
            add(menuMaker.createMenuBar());

            // and the layered pane
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(layeredPane);
        }
    }

    // Populate the filename array with cards using the javacards package
    // Also add proper syntax format for the cards
    public void drawDeck() {

        if (verboseMode) {
            System.out.println("\nDrawing cards");
        }
        Deck cardDeck = new Deck();
        Card temp;

        for (int i = 51; i >= 0; i--) {
            temp = cardDeck.drawFromDeck();
            filename[i] = "../" + cardLocation + "/" + temp.getSuit() + "-" + temp.getRank() + "-150.png";

            if (verboseMode) {
                System.out.println("Drew card " + filename[i] + " in position " + i);
            }
        }
    }

    // This method takes the cards out of the drawn deck(filename array) and
    // sorts them into properly set rows and columns
    // in the colData matrix/database
    public void populateCols() {
        int temp = 0;

        if (numberOfColumns == 8) {
            if (verboseMode) {
                System.out.println("\nSorting cards for" + numberOfColumns);
            }
            // populate the first four columns that are 8 deep
            for (int i = 3; i >= 0; i--) {
                for (int j = 6; j >= 0; j--) {
                    if (verboseMode) {
                        System.out.println("Temp: " + temp + " " + filename[temp] + " colData: " + colData[i][j]);
                    }
                    colData[i][j] = filename[temp];
                    temp++;
                }
            }

            // populate the last four columns that are 7 deep
            for (int i = 7; i >= 4; i--) {
                for (int j = 5; j > -1; j--) {
                    colData[i][j] = filename[temp];
                    temp++;
                }
            }
        }
    }

    // This method grabs the data from colData database and passes that to the
    // renderer using the icon array card[]
    public void paintCols() {

        if (verboseMode) {
            System.out.println("\nPainting Columns");
        }

        // iterate through both the column, and the cards in that column
        int temp = 0;
        for (int i = numberOfColumns - 1; i > -1; i--) {
            int j = 0;
            while (colData[i][j] != null) {
                // fetch the filename from the colData store based on its column
                // and row spacing
                ImageIcon icon = createImageIcon(colData[i][j]);
                // assign that card a number based on its position
                card[temp] = new JLabel(icon);
                // set its spacing based on its location
                card[temp].setName(colData[i][j]);
                card[temp].setBounds((colBuffer + colWidth * i), (rowDown + rowOffset * j), icon.getIconWidth(), icon.getIconHeight());

                if (verboseMode) {
                    System.out.println("Added card: " + colData[i][j] + " to position " + (colBuffer + colWidth * i) + ", " + (rowDown + rowOffset * j));
                }

                // add it to the renderer
                layeredPane.add(card[temp], new Integer(3), 0);
                j++;
                temp++;
            }
        }
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = hybrid.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    // This thingy handles the movement of the cards in rows
    public static void mainPlayField(int x, int y) {

        // When a card is clicked the clickH class sends the coordinates to this
        // class
        int locationX = x;
        int locationY = y;

        // The cards column and row are determined from those coordinates
        int mainCol = ((locationX - colBuffer) / colWidth);
        int mainRow = ((locationY - rowDown) / rowOffset);

        // Variables are reset, the finalRow is used to find the bottom empty
        // space in a column
        int finalRow = 0;

        // For cardInfo
        cardInfo cardInfo = new cardInfo();

        if (verboseMode) {
            System.out.println("\nmainPlayField Invoked at position: " + locationX + ", " + locationY);
        }

        // Check to see if your not selecting the same cell, if so clear
        // selection
        if (mainCol == selectedCol && mainRow == selectedRow) {
            if (verboseMode) {
                System.out.println("\nROW CLEARED due to double click in row");
            }
            selectedRow = -1;
            selectedCol = -1;
            selectedCell = -1;
            return;
        }

        // Check to see if there is a previously selected card and not the same
        // column
        if (selectedRow != -1) {
            if (selectedCol != mainCol) {

                if (verboseMode) {
                    System.out.println("\nRow to Row card movement started");
                    System.out.println("SelectedRow: " + selectedRow + "and selectedCol: " + selectedCol);
                    System.out.println("Current Row: " + mainRow + "and current Column: " + mainCol + "\n");
                }

                // This finds the first available space open for the card to
                // reside
                for (int i = 0; i <= numberOfRows; i++) {
                    if (colData[mainCol][i] == null) {
                        finalRow = i;
                        i = numberOfRows + 1;
                    }
                }

                IntelligentMover mover = new IntelligentMover();
                mover.transferCardSmart(selectedRow, selectedCol, finalRow, mainCol);
                // mover.transferCardDumb(selectedRow, selectedCol, finalRow,
                // mainCol);

                // When finished with the operation reset the seleceted row and
                // column variables
                selectedRow = -1;
                selectedCol = -1;
                selectedCell = -1;

                return;
            }

        }

        // If a filled freecell was previously selected, the prepare to move
        // that card to a row
        if (selectedCell != -1 && colData[numberOfColumns][selectedCell] != null) {
            if (verboseMode) {
                System.out.println("\nNon empty freecell traspher to row");
            }

            for (int i = 0; i <= numberOfRows; i++) {
                if (colData[mainCol][i] == null) {
                    finalRow = i;
                    i = numberOfRows + 1;
                }
            }

            for (int i = 0; i < 52; i++) {
                // If the card matches change location, if not keep iterating
                if (card[i].getName() == colData[numberOfColumns][selectedCell]) {

                    if (finalRow > 0) {
                        if (cardInfo.cardColor(colData[numberOfColumns][selectedCell]) != cardInfo.cardColor(colData[mainCol][finalRow - 1])
                                && cardInfo.cardNumber(colData[numberOfColumns][selectedCell]) + 1 == cardInfo.cardNumber(colData[mainCol][finalRow - 1])) {

                            if (verboseMode) {
                                System.out.println("User moving card from frecell to non-empty row");
                                System.out.println("Source Card: " + cardInfo.cardNumber(colData[numberOfColumns][selectedCell])
                                        + cardInfo.cardColor(colData[numberOfColumns][selectedCell]));
                                System.out.println("Destination location: " + cardInfo.cardNumber(colData[mainCol][finalRow - 1])
                                        + cardInfo.cardColor(colData[mainCol][finalRow - 1]));
                            }

                            card[i].setBounds((colBuffer + colWidth * mainCol), (rowDown + rowOffset * finalRow), card[i].getWidth(), card[i].getHeight());

                            // Then change the colData library so we know were
                            // the card
                            // is in the future
                            colData[mainCol][finalRow] = colData[numberOfColumns][selectedCell];
                            colData[numberOfColumns][selectedCell] = null;

                            // This section adds and removes the cards from the
                            // layer
                            // insuring proper z order
                            layeredPane.remove(card[i]);
                            layeredPane.add(card[i], new Integer(5), 0);
                        }
                    } else {

                        if (verboseMode) {
                            System.out.println("User transphered card from freecell to empty row");
                            System.out.println("Source Card: " + cardInfo.cardNumber(colData[numberOfColumns][selectedCell])
                                    + cardInfo.cardColor(colData[numberOfColumns][selectedCell]));
                        }

                        card[i].setBounds((colBuffer + colWidth * mainCol), (rowDown + rowOffset * finalRow), card[i].getWidth(), card[i].getHeight());

                        // Then change the colData library so we know were the
                        // card
                        // is in the future
                        colData[mainCol][finalRow] = colData[numberOfColumns][selectedCell];
                        colData[numberOfColumns][selectedCell] = null;

                        // This section adds and removes the cards from the
                        // layer
                        // insuring proper z order
                        layeredPane.remove(card[i]);
                        layeredPane.add(card[i], new Integer(5), 0);
                    }

                    // Then exit the loop
                    i = 52;
                    selectedRow = -1;
                    selectedCol = -1;
                    selectedCell = -1;
                    return;
                }
            }
        }

        // If a card has not previously been selected this section adds it to
        // the selectedRow and selectedCol variables if it is a valid card
        if (selectedCell == -1 && selectedRow == -1 && colData[mainCol][0] != null) {

            // Find finalRow thus only allowing the user to select the ending
            // card in a row
            for (int i = 0; i <= numberOfRows; i++) {
                if (colData[mainCol][i] == null) {
                    finalRow = i;
                    i = numberOfRows + 1;
                }
            }

            if (verboseMode) {
                System.out.println("\nBoth cell and row were not selected, selecting new row");
                System.out.println("FinalRow: " + finalRow);
                System.out.println("Selected Row: " + mainRow);
                System.out.println("New Column " + mainCol);
            }

            if (mainRow + 3 >= finalRow) {
                // When it find the columns simply add it to the variable
                selectedCol = mainCol;

                // the row is not so simple, since the end card has a much
                // larger hit box you have to check three spaces down to see
                // if there
                // is a card
                selectedRow = finalRow - 1;
                return;
            }
        }
    }

    // This method handles the selection and movement of cells in the freecells
    public static void freecells(int x, int y) {

        // initilize some variables
        int locationX = x;
        int locationY = y;
        int cell = (locationX - freecellsRowBuffer) / freecellsWidth;

        if (verboseMode) {
            System.out.println("\nFreecell Selected");
            System.out.println("Cell " + cell);
        }

        // Check to see if your not selecting the same cell
        if (selectedCell == cell) {
            if (verboseMode) {
                System.out.println("\nFREECELL CLEARED due to double click in Freecell");
            }
            selectedRow = -1;
            selectedCol = -1;
            selectedCell = -1;
            return;
        }

        // If a row has previously been selected and a cell has not AND the
        // current cell is empty
        // Remove the card from the row and insert it into the freecell
        if (selectedCell == -1 && selectedRow != -1 && colData[numberOfColumns][cell] == null) {

            if (verboseMode) {
                System.out.println("Row previously selected and current freecell empty");
                System.out.println("Moving row to freecell");
                System.out.println("Card: " + colData[selectedCol][selectedRow]);
            }

            // cycles through each card looking for the previously selected
            // card
            for (int i = 0; i < 52; i++) {

                // when you find it remove it from its old position and add
                // it to the freecell
                if (card[i].getName() == colData[selectedCol][selectedRow]) {

                    colData[numberOfColumns][((locationX - freecellsRowBuffer) / freecellsWidth)] = colData[selectedCol][selectedRow];
                    colData[selectedCol][selectedRow] = null;

                    card[i].setBounds((cell * freecellsWidth) + freecellsRowBuffer, freecellsColBuffer, card[i].getWidth(), card[i].getHeight());

                    layeredPane.remove(card[i]);
                    layeredPane.add(card[i], new Integer(5), 0);

                    // Then exit the loop
                    i = 52;

                }

            }

            // If a freecell was not selected, clear all selected rows/cells
            selectedCell = -1;
            selectedRow = -1;
            selectedCol = -1;
            return;
        }

        // if a different freecell is selected and that cell is full and current
        // cell is empty,
        // transfer that card to the new cell
        if (selectedCell != -1 && selectedCell != cell && colData[numberOfColumns][selectedCell] != null && colData[numberOfColumns][cell] == null) {

            if (verboseMode) {
                System.out.println("Moving one filled cell to a empty one");
                System.out.println("Card: " + colData[numberOfColumns][selectedCell]);
                System.out.println("New Cell: " + cell);
            }

            for (int i = 0; i < 52; i++) {

                // when you find it remove it from its old position and add
                // it to the freecell
                if (card[i].getName() == colData[numberOfColumns][selectedCell]) {

                    colData[numberOfColumns][cell] = colData[numberOfColumns][selectedCell];
                    colData[numberOfColumns][selectedCell] = null;

                    card[i].setBounds((cell * freecellsWidth) + freecellsRowBuffer, freecellsColBuffer, card[i].getWidth(), card[i].getHeight());

                    layeredPane.remove(card[i]);
                    layeredPane.add(card[i], new Integer(5), 0);

                    // Then exit the loop
                    i = 52;
                }
            }
            selectedCell = -1;
            selectedRow = -1;
            selectedCol = -1;
            return;
        }

        // If neither a row or a previous cell has been selected, set the
        // currently clicked cell as the selected cell
        if (selectedCell == -1 && selectedRow == -1 && colData[numberOfColumns][cell] != null) {
            if (verboseMode) {
                System.out.println("Neither a Row nor a Freecell was selected, and the current cell is full");
                System.out.println("Selected Cell: " + cell);
            }
            selectedCell = cell;
            return;
        }
    }

    // This handles user custom card movements to the final cells
    public static void finalCells(int x, int y) {
        int locationX = x;
        int locationY = y;
        int cell = (locationX - finalSpaceRowBuffer) / finalSpaceWidth;
        cardInfo cardInfo = new cardInfo();

        if (verboseMode) {
            System.out.println("\nFinal Cell selected");
        }

        // if a cell is selected and row is not move from the freecell to the
        // finalCell
        if (selectedCell != -1 && selectedRow == -1) {

            if (verboseMode) {
                System.out.println("Freecell transphered to final cell");
            }

            for (int i = 0; i < 52; i++) {

                // when you find it remove it from its old position and add it
                // to the final cell
                if (card[i].getName() == colData[numberOfColumns][selectedCell]) {

                    if (finalData[cell] == null && cardInfo.cardNumber(colData[numberOfColumns][selectedCell]) == 1) {
                        if (verboseMode) {
                            System.out.println("Ace Transphered to final Cell: " + cell);
                        }

                        finalData[cell] = colData[numberOfColumns][selectedCell];

                        colData[numberOfColumns][selectedCell] = null;

                        card[i].setBounds((cell * finalSpaceWidth) + finalSpaceRowBuffer, finalSpaceColBuffer, card[i].getWidth(), card[i].getHeight());

                        layeredPane.remove(card[i]);
                        layeredPane.add(card[i], new Integer(5), 0);
                    }

                    if (finalData[cell] != null && cardInfo.cardNumber(colData[numberOfColumns][selectedCell]) == 1 + cardInfo.cardNumber(finalData[cell])
                            && cardInfo.cardSuit(colData[numberOfColumns][selectedCell]) == cardInfo.cardSuit(finalData[cell])) {

                        if (verboseMode) {
                            System.out.println(cardInfo.cardNumber(colData[numberOfColumns][selectedCell]) + cardInfo.cardSuit(colData[numberOfColumns][selectedCell])
                                    + "Transphered to cell: " + cell);
                        }

                        finalData[cell] = colData[numberOfColumns][selectedCell];

                        colData[numberOfColumns][selectedCell] = null;

                        card[i].setBounds((cell * finalSpaceWidth) + finalSpaceRowBuffer, finalSpaceColBuffer, card[i].getWidth(), card[i].getHeight());

                        layeredPane.remove(card[i]);
                        layeredPane.add(card[i], new Integer(5), 0);
                    }

                    // Then exit the loop
                    i = 52;

                }

            }

            selectedCell = -1;
            selectedRow = -1;
            selectedCol = -1;

        }

        // if the row is selected and not the freecell move from the row to the
        // final cell
        if (selectedRow != -1 && selectedCell == -1) {

            if (verboseMode) {
                System.out.println("Row transphered to final cell");
            }

            for (int i = 0; i < 52; i++) {

                // when you find it remove it from its old position and add it
                // to the freecell
                if (card[i].getName() == colData[selectedCol][selectedRow]) {

                    if (finalData[cell] == null && cardInfo.cardNumber(colData[selectedCol][selectedRow]) == 1) {

                        if (verboseMode) {
                            System.out.println("Ace Transphered to final Cell: " + cell);
                        }

                        finalData[cell] = colData[selectedCol][selectedRow];
                        colData[selectedCol][selectedRow] = null;

                        card[i].setBounds((cell * finalSpaceWidth) + finalSpaceRowBuffer, finalSpaceColBuffer, card[i].getWidth(), card[i].getHeight());

                        layeredPane.remove(card[i]);
                        layeredPane.add(card[i], new Integer(5), 0);

                    }

                    if (finalData[cell] != null && cardInfo.cardNumber(colData[selectedCol][selectedRow]) == 1 + cardInfo.cardNumber(finalData[cell])
                            && cardInfo.cardSuit(colData[selectedCol][selectedRow]) == cardInfo.cardSuit(finalData[cell])) {

                        if (verboseMode) {
                            System.out.println(cardInfo.cardNumber(colData[selectedCol][selectedRow]) + cardInfo.cardSuit(colData[selectedCol][selectedRow])
                                    + "Transphered to final cell: " + cell);
                        }

                        finalData[cell] = colData[selectedCol][selectedRow];
                        colData[selectedCol][selectedRow] = null;

                        card[i].setBounds((cell * finalSpaceWidth) + finalSpaceRowBuffer, finalSpaceColBuffer, card[i].getWidth(), card[i].getHeight());

                        layeredPane.remove(card[i]);
                        layeredPane.add(card[i], new Integer(5), 0);
                    }
                    // Then exit the loop
                    i = 52;

                }

            }

        }
        selectedRow = -1;
        selectedCol = -1;
        selectedCell = -1;

    }

    // This function returns available space to move cards
    public int getFreeSpaceAvailiable() {
        int freeSpace = 0;
        int finalSpace = 0;
        for (int i = 0; i < numberOfFreecells; i++) {
            if (colData[numberOfColumns][i] == null) {
                freeSpace++;
            }
        }
        for (int i = 0; i < numberOfColumns; i++) {
            if (colData[i][0] == null) {
                finalSpace++;
            }
        }

        // this is me being lazy and hard coding the freecell numbers in instead
        // of
        // figuring out a mathematical equation to do it
        switch (freeSpace) {
            case (0):
                switch (finalSpace) {
                    case (0):
                        return 0;
                    case (1):
                        return 1;
                    case (2):
                        return 3;
                    case (3):
                        return 6;
                    case (4):
                        return 10;
                    default:
                        return 20;
                }
            case (1):
                switch (finalSpace) {
                    case (0):
                        return 1;
                    case (1):
                        return 3;
                    case (2):
                        return 6;
                    case (3):
                        return 10;
                    case (4):
                        return 20;
                    default:
                        return 20;
                }
            case (2):
                switch (finalSpace) {
                    case (0):
                        return 2;
                    case (1):
                        return 5;
                    case (2):
                        return 9;
                    case (3):
                        return 20;
                    case (4):
                        return 20;
                    default:
                        return 20;
                }

            case (3):
                switch (finalSpace) {
                    case (0):
                        return 3;
                    case (1):
                        return 7;
                    case (2):
                        return 12;
                    case (3):
                        return 20;
                    case (4):
                        return 20;
                    default:
                        return 20;
                }

            case (4):
                switch (finalSpace) {
                    case (0):
                        return 4;
                    case (1):
                        return 9;
                    case (2):
                        return 20;
                    case (3):
                        return 20;
                    case (4):
                        return 20;
                    default:
                        return 20;
                }
        }
        return 0;
    }

    /*
	 * This method clears the field and resets all important variables It then
	 * reinvokes the card drivers
     */
    public void newGame() {
        // claer the main field and freecells
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns + 1; j++) {
                colData[j][i] = null;
            }
        }
        // clear the final cells
        for (int i = 0; i < 4; i++) {
            finalData[i] = null;
        }

        hybrid hybrid = new hybrid(false);
        hybrid.newBackground();

        // finally remove all the old cards
        for (int i = 0; i < 52; i++) {
            layeredPane.remove(card[i]);
            card[i] = null;
        }

        // redraw the field
        drawDeck();
        populateCols();
        paintCols();
    }

    public void newBackground() {
        // Define the background image
        final ImageIcon backgroundPic = new ImageIcon(backgroundImage);

        // setup the background image
        back = new JLabel(backgroundPic);
        back.setBounds(0, 0, 1024, 600);
        // remove the old one
        layeredPane.remove(back);
        // add the new one
        layeredPane.add(back, new Integer(1), 0);
    }
}
