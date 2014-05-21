package sokoban;
import java.awt.Color;
import java.awt.*;
import java.awt.Graphics;
import java.awt.MediaTracker;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JWindow;

/** A simple Splash screen. */
public class SplashScreen  extends JWindow {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected ImageIcon im;

  public SplashScreen(JFrame f, String progName, String fileName) {
    super();
    // Can't use Swing border on JWindow: not a JComponent.
    // setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    im = new ImageIcon(fileName);
    if (im.getImageLoadStatus() != MediaTracker.COMPLETE)
      JOptionPane.showMessageDialog(f,
        "Warning: can't load image " + fileName + "\n" +
        "Please be sure you have installed " + progName + " correctly",
        "Warning",
        JOptionPane.WARNING_MESSAGE);
    int w = im.getIconWidth(), h = im.getIconHeight();
    setSize(w, h);
    UtilGUI.center(this);
    addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        dispose();
      }
    });
    addKeyListener(new KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        dispose();
      }
    });
  }

  public void paint(Graphics g) {
    im.paintIcon(this, g, 0, 0);
    g.setColor(Color.green);
    g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 7, 7);
  }
}
class UtilGUI {

  /** Center a Window, Frame, JFrame, Dialog, etc. */
  public static void centre(Window w) {
    // After packing a Frame or Dialog, center it on the screen.
    Dimension us = w.getSize(), them = Toolkit.getDefaultToolkit()
        .getScreenSize();
    int newX = (them.width - us.width) / 2;
    int newY = (them.height - us.height) / 2;
    w.setLocation(newX, newY);
  }

  /**
   * Center a Window, Frame, JFrame, Dialog, etc., but do it the American
   * Spelling Way :-)
   */
  public static void center(Window w) {
    UtilGUI.centre(w);
  }

  /** Maximize a window, the hard way. */
  public static void maximize(Window w) {
    Dimension us = w.getSize(), them = Toolkit.getDefaultToolkit()
        .getScreenSize();
    w.setBounds(0, 0, them.width, them.height);
  }
}
