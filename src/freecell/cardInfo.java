package freecell;

/*
 * This class(cardInfo) is used the simplify the process of find card information
 * it is feed the filename and determines the suite, number, and color from the information
 */
public class cardInfo {

    // get the card color
    public String cardColor(String filename) {

        if (filename == null) {
            return "NULL";
        }

        if (filename.contains("diamonds")) {
            return "red";
        } else if (filename.contains("hearts")) {
            return "red";
        } else if (filename.contains("spades")) {
            return "black";
        } else if (filename.contains("clubs")) {
            return "black";
        } else {
            System.out.println("ERROR passed invlaid card name: " + filename);
            return "error";
        }

    }

    // get the suit
    public String cardSuit(String filename) {
        if (filename == null) {
            return "NULL";
        }

        if (filename.contains("diamonds")) {
            return "diamond";
        } else if (filename.contains("hearts")) {
            return "heart";
        } else if (filename.contains("spades")) {
            return "spade";
        } else if (filename.contains("clubs")) {
            return "club";
        } else {
            System.out.println("ERROR passed invlaid card name: " + filename);
            return "error";
        }
    }

    // get the number
    public int cardNumber(String filename) {

        if (filename == null) {
            return 0;
        }
        int cardIndex = filename.indexOf("-");

        if (filename.charAt(cardIndex + 1) == 'a') {
            return 1;
        } else if (filename.charAt(cardIndex + 1) == 'k') {
            return 13;
        } else if (filename.charAt(cardIndex + 1) == 'q') {
            return 12;
        } else if (filename.charAt(cardIndex + 1) == 'j') {
            return 11;
        } else if (filename.charAt(cardIndex + 1) == '1' && filename.charAt(cardIndex + 2) == '0') {
            return 10;
        } else {
            return Integer.parseInt(Character.toString(filename.charAt(cardIndex + 1)));
        }

    }

    //this is a testing class used for debugging
    public static void main(String[] args) {
        String filename = "/freecell/src/cardArt/clubs-q-150.png";

        cardInfo invoke = new cardInfo();
        System.out.println("Card color: " + invoke.cardColor(filename));
        System.out.println("Card suit: " + invoke.cardSuit(filename));
        System.out.println("Card number: " + invoke.cardNumber(filename));

    }

}
