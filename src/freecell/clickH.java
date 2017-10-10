package freecell;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/*
 * The clickH is a listener for the main play field
 * currently it only moves on a mouseclick
 */
public class clickH extends JPanel implements MouseListener, MouseMotionListener {

    public clickH() {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        hybrid hybrid = new hybrid(false);
        IntelligentMover checker = new IntelligentMover();
        int locationX = e.getX();
        int locationY = e.getY();
        int column = ((locationX - hybrid.colBuffer) / hybrid.colWidth);
        int row = ((locationY - hybrid.rowDown) / hybrid.rowOffset);

        // check for cards to automatically move
        checker.detector();

        //This section finds where a user clicks and invokes the proper method
        if (locationX > hybrid.colBuffer) {
            if (locationY > hybrid.rowDown) {
                if (hybrid.verboseMode) {
                    System.out.println("You clicked on a row in the main area!!!");
                    System.out.println("Column: " + column);
                    System.out.println("Row: " + row);
                }
                hybrid.mainPlayField(locationX, locationY);
                checker.detector();
            }
            if (locationY < hybrid.rowDown && locationX < hybrid.finalSpaceRowBuffer) {
                hybrid.freecells(locationX, locationY);
                checker.detector();
            }
            if (locationY < hybrid.rowDown && locationX > hybrid.finalSpaceRowBuffer) {
                hybrid.finalCells(locationX, locationY);
                checker.detector();
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }
}
