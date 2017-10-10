package freecell;

/*
 * This class(IntelligentMover) does several things
 * the basic concept behind this class is that all automatic non AI methods should located here
 * This class handles automatic moving of cards to the final cells
 * This class handles both the smart and dumb methods of tranfering cards
 */
public class IntelligentMover {

    public IntelligentMover() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    /*
	 * This is the smart method of tranfering a card between two different
	 * columns This method will detect if there is a transpherable column of
	 * cards above the selected card if there is then this method will
	 * intelligently determines if just one card should be moved or weather the
	 * part/whole thing can be transfered all in one move this method also
	 * factors in the amount of available space to move cards
     */
    public void transferCardSmart(int selectedRow, int selectedCol, int finalRow, int mainCol) {
        // Define some variables
        hybrid hybridMain = new hybrid(false);
        cardInfo cardInfo = new cardInfo();
        String[] tempTranspher = new String[12];

        // Add first value to the temp array
        tempTranspher[0] = hybridMain.colData[selectedCol][selectedRow];

        // This section adds all cards above the first one that are legal to the
        // transpher list
        for (int i = 1; i < 12; i++) {
            if (selectedRow - i >= 0) {
                if (hybridMain.colData[selectedCol][selectedRow - i] != null) {
                    if (cardInfo.cardNumber(hybridMain.colData[selectedCol][selectedRow - i]) == cardInfo
                            .cardNumber(hybridMain.colData[selectedCol][selectedRow - i + 1]) + 1
                            && cardInfo.cardColor(hybridMain.colData[selectedCol][selectedRow - i]) != cardInfo.cardColor(hybridMain.colData[selectedCol][selectedRow - i
                            + 1])) {

                        tempTranspher[i] = hybridMain.colData[selectedCol][selectedRow - i];

                    } else {
                        i = 12;
                    }
                }
            }
        }

        // Now if any cards are in the temp transfer list check to see if
        for (int i = 0; i < 12; i++) {
            if (tempTranspher[i] != null) {
                if (hybridMain.verboseMode) {
                    System.out.println("Found Point 1");
                    System.out.println("mainCol: " + mainCol + " and finalRow: " + finalRow);
                    System.out.println("selectedCol: " + selectedCol + " and selectedRow: " + selectedRow);
                }

                boolean result;
                if (finalRow > 0) {
                    result = cardInfo.cardNumber(hybridMain.colData[mainCol][finalRow - 1]) == 1 + cardInfo.cardNumber(tempTranspher[i])
                            && cardInfo.cardColor(hybridMain.colData[mainCol][finalRow - 1]) != cardInfo.cardColor(tempTranspher[i]);
                } else {
                    result = true;
                }

                // This section detects if there is enough space to transfer the
                // cards
                if (i <= hybridMain.getFreeSpaceAvailiable()) {
                    // if the card in the temp list can be inserted under the
                    // clicked card allow through
                    if (result) {

                        // add each valid card one at a time
                        for (int j = i; j >= 0; j--) {
                            // this finds and rearrange the card
                            for (int k = 0; k < 52; k++) {

                                if (hybridMain.card[k].getName() == tempTranspher[j]) {

                                    hybridMain.card[k].setBounds((hybridMain.colBuffer + hybridMain.colWidth * mainCol), (hybridMain.rowDown + hybridMain.rowOffset
                                            * (finalRow + i - j)), hybridMain.card[k].getWidth(), hybridMain.card[k].getHeight());

                                    // Then change the colData library so we
                                    // know
                                    // were
                                    // the card
                                    // is in the future
                                    hybridMain.colData[mainCol][finalRow + i - j] = tempTranspher[j];

                                    if (hybrid.verboseMode) {
                                        System.out.println("Removing: " + hybridMain.colData[selectedCol][selectedRow - j]);
                                    }
                                    hybridMain.colData[selectedCol][selectedRow - j] = null;

                                    if (hybrid.verboseMode) {
                                        System.out.println("Added " + tempTranspher[j] + " to new row");

                                    }
                                    // This section adds and removes the cards
                                    // from
                                    // the
                                    // layer
                                    // insuring proper z order
                                    hybridMain.layeredPane.remove(hybridMain.card[k]);
                                    hybridMain.layeredPane.add(hybridMain.card[k], new Integer(5), 0);

                                    k = 52;
                                }
                            }

                        }
                    }
                }

            } else {
                i = 12;
            }
        }
    }

    /*
	 * This is the dumb way to transfer cards, is mearly moves one card to the
	 * new column
     */
    public void transferCardDumb(int selectedRow, int selectedCol, int finalRow, int mainCol) {

        // Then we cycle through all the cards looking for the one that was
        // previously selected,
        // we then change its coordinates to the new column
        // selected and to the first available free row space
        hybrid hybrid = new hybrid(false);
        cardInfo cardInfo = new cardInfo();

        for (int i = 0; i < 52; i++) {

            // If the card matches change location, if not keep iterating
            if (hybrid.card[i].getName() == hybrid.colData[selectedCol][selectedRow]) {

                if (finalRow > 0) {
                    if (cardInfo.cardColor(hybrid.colData[selectedCol][selectedRow]) != cardInfo.cardColor(hybrid.colData[mainCol][finalRow - 1])
                            && cardInfo.cardNumber(hybrid.colData[selectedCol][selectedRow]) + 1 == cardInfo.cardNumber(hybrid.colData[mainCol][finalRow - 1])) {

                        if (hybrid.verboseMode) {
                            System.out.println("User moving card from row to non-empty row");
                            System.out.println("Source Card: " + cardInfo.cardNumber(hybrid.colData[selectedCol][selectedRow])
                                    + cardInfo.cardColor(hybrid.colData[selectedCol][selectedRow]));
                            System.out.println("Destination location: " + cardInfo.cardNumber(hybrid.colData[mainCol][finalRow - 1])
                                    + cardInfo.cardColor(hybrid.colData[mainCol][finalRow - 1]));
                        }

                        // This changes the location of the card
                        hybrid.card[i].setBounds((hybrid.colBuffer + hybrid.colWidth * mainCol), (hybrid.rowDown + hybrid.rowOffset * finalRow),
                                hybrid.card[i].getWidth(), hybrid.card[i].getHeight());

                        // Then change the colData library so we know were the
                        // card is in the future
                        hybrid.colData[mainCol][finalRow] = hybrid.colData[selectedCol][selectedRow];
                        hybrid.colData[selectedCol][selectedRow] = null;

                        // This section adds and removes the cards from the
                        // layer insuring proper z order
                        hybrid.layeredPane.remove(hybrid.card[i]);
                        hybrid.layeredPane.add(hybrid.card[i], new Integer(5), 0);

                    }
                } else {

                    if (hybrid.verboseMode) {
                        System.out.println("User transphered card from row to empty row");
                        System.out.println("Source Card: " + cardInfo.cardNumber(hybrid.colData[selectedCol][selectedRow])
                                + cardInfo.cardColor(hybrid.colData[selectedCol][selectedRow]));
                    }

                    // This changes the location of the card
                    hybrid.card[i].setBounds((hybrid.colBuffer + hybrid.colWidth * mainCol), (hybrid.rowDown + hybrid.rowOffset * finalRow), hybrid.card[i].getWidth(),
                            hybrid.card[i].getHeight());

                    // Then change the colData library so we know were the card
                    // is in the future
                    hybrid.colData[mainCol][finalRow] = hybrid.colData[selectedCol][selectedRow];
                    hybrid.colData[selectedCol][selectedRow] = null;

                    // This section adds and removes the cards from the // layer
                    // insuring proper z order layeredPane.remove(card[i]);
                    hybrid.layeredPane.add(hybrid.card[i], new Integer(5), 0);
                }

                // Then exit the loop i = 52; }
            }
        }

    }

    // this method is for detecting cards that should be put into the final play
    // section immediately and moving them
    public void detector() {

        hybrid data = new hybrid(false);
        cardInfo cardInfo = new cardInfo();

        int lowestNumber = 14;
        int temp = 0;
        int finalCellTransfer = 0;

        boolean passThrough = false;
        boolean cardMoved = false;

        // detect lowest card in the final cells
        for (int i = 0; i < 4; i++) {
            if (data.finalData[i] != null) {
                if (cardInfo.cardNumber(data.finalData[i]) < lowestNumber) {
                    lowestNumber = cardInfo.cardNumber(data.finalData[i]);
                }
            } else {
                lowestNumber = 0;
            }
        }

        if (data.verboseMode) {
            System.out.println("\nLowest number in the final Cell is: " + lowestNumber);
        }

        // check the play field as well as the freecells for a matching card to
        // move up
        for (int i = 0; i < data.numberOfColumns + 1; i++) {

            // if your searching the play field find the end of each row
            if (i < data.numberOfColumns) {
                temp = 0;
                while (data.colData[i][temp] != null) {
                    temp++;
                }
            }

            // if the check is in the Freecell zone this section of the code
            // combined with a section at the end
            // modifies the loop to account for the fact that the Freecells need
            // to be parsed differently
            if (i == data.numberOfColumns) {
                // this complex bit of code handles the run through of the free
                // cell area
                if (passThrough) {
                    if (hybrid.verboseMode) {
                        System.out.println("passThrough freecell: " + temp);
                    }
                    temp++;
                } else {
                    temp = 1;
                    passThrough = true;
                }
            }

            // if the row is full(ie not null) then check to see if the card is
            // compatible to be swapped up to a final space
            if (temp > 0) {
                temp--;
                if (data.verboseMode) {
                    System.out.println("(In the detector method)Temp equals: " + temp);
                }

                // check to see if this is the card that could fit in the final
                // row
                if (cardInfo.cardNumber(data.colData[i][temp]) == lowestNumber + 1) {
                    if (data.verboseMode) {
                        System.out.println("Possible Canidate: " + data.colData[i][temp]);
                    }

                    // one case for numbers above 1 and one case for aces
                    if (lowestNumber > 0) {
                        // find the final cell to put the card in
                        for (int j = 0; j < 4; j++) {
                            if (cardInfo.cardSuit(data.colData[i][temp]) == cardInfo.cardSuit(data.finalData[j])) {
                                finalCellTransfer = j;
                            }

                        }

                        // move the card
                        for (int k = 0; k < 52; k++) {
                            if (data.card[k].getName() == data.colData[i][temp]) {
                                if (data.verboseMode) {
                                    System.out.println("Card Moved Automatically");
                                }

                                data.finalData[finalCellTransfer] = data.colData[i][temp];
                                data.colData[i][temp] = null;

                                data.card[k].setBounds((finalCellTransfer * data.finalSpaceWidth) + data.finalSpaceRowBuffer, data.finalSpaceColBuffer,
                                        data.card[k].getWidth(), data.card[k].getHeight());

                                data.layeredPane.remove(data.card[k]);
                                data.layeredPane.add(data.card[k], new Integer(5), 0);
                                cardMoved = true;
                            }
                        }

                    } // Case for aces
                    else {
                        if (cardInfo.cardNumber(data.colData[i][temp]) == lowestNumber + 1) {

                            System.out.println("Possible Canidate(Ace): " + data.colData[i][temp]);

                            // one case for numbers above 1 and one case for
                            // aces
                            // find the final cell to put the card in
                            for (int j = 0; j < 4; j++) {
                                System.out.println("On iteration " + j + "final cell is " + data.finalData[j]);
                                if (data.finalData[j] == null) {
                                    finalCellTransfer = j;
                                    System.out.println("finalCellTranspher: " + j);
                                }
                            }

                            // move the card
                            for (int k = 0; k < 52; k++) {
                                if (data.card[k].getName() == data.colData[i][temp]) {
                                    System.out.println("Card Moved Automatically");

                                    data.finalData[finalCellTransfer] = data.colData[i][temp];
                                    data.colData[i][temp] = null;

                                    data.card[k].setBounds((finalCellTransfer * data.finalSpaceWidth) + data.finalSpaceRowBuffer, data.finalSpaceColBuffer,
                                            data.card[k].getWidth(), data.card[k].getHeight());

                                    data.layeredPane.remove(data.card[k]);
                                    data.layeredPane.add(data.card[k], new Integer(5), 0);
                                    cardMoved = true;
                                }
                            }

                        }

                    }
                }

            }
            // this section works in conjunctor with a loop at the top
            // to modify the loop to run through the freecells
            temp++;
            if (i == data.numberOfColumns) {
                i--;
                if (temp == data.numberOfFreecells) {
                    i = data.numberOfColumns + 2;

                }

            }
        }
        // this method will make recursive calls to itself until it stops moving
        // cards
        if (cardMoved) {
            detector();
        }
    }
}
