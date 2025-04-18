import animatedapp.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
    

/**
 *  A thread that is used to solve Reve's problem - It is animated.
 * 
 * @author Charles Hoot 
 * @version 5.0
 */

    
public class RevesActionThread extends ActionThread
{
    
    /**
     * Constructor for objects of class RevesActionThread
     */
    public RevesActionThread()
    {
        super();

    }

    public String getApplicationTitle()
    {
        return "Reve's Puzzle (Skeleton)";
    }
    
    

    // **************************************************************************
    // This is application specific code
    // **************************************************************************    

    // These are the variables that are parameters of the application and can be
    // set via the application specific GUI
    // Make sure they are initialized
    private int disksToUse = 10;
   
    
    
    private int disks;
    
    // Displayed objects
    private Pole a, b, c, d;
    private int movesMade;
    private String moveString;
    
    public void init() 
    {
        disks = disksToUse;
        movesMade = 0;
        moveString = "";

        // ADD INITIALIZATION CODE HERE

        //step 3
        a = new Pole ("A", 10);
        b = new Pole ("B", 10);
        c = new Pole ("C", 10);
        d = new Pole ("D", 10);

        //step 4
        for (int i= disks; i >=1; i--)
        {
            Disk disk = new Disk(i);
            a.addDisk(disk);
        }



    }


    public void executeApplication() {
        // ADD CODE THAT WILL DO A SINGLE EXECUTION

        //moveDisk(a,b); // step 7

        //towersOfHanoi(disks, a, b, d); //step 9

        reves(disks, a,d, b,c); // step 12
    }

    /**
     * Move a disk from one pole to another pole.
     *
     * @param from The source pole.
     * @param to   The destination pole.
     */
    public void moveDisk(Pole from, Pole to) {
        Disk toMove = from.removeDisk(); //step 6 remove the top disk from the source pole

        to.addDisk(toMove); // add disk to target pole

        // ADD CODE HERE TO MOVE A DISK FROM ONE POLE TO THE OTHER

        movesMade++;
        moveString = "Move #" + movesMade
                + ": Moved disk " + toMove.getSize()
                + " from " + from.getName()
                + " to " + to.getName();

        animationPause();
    }


    // ADD METHODS HERE
    //step 8
    public void towersOfHanoi(int disks, Pole from, Pole extra, Pole to) {
        if (disks == 1) {
            moveDisk(from, to);

        } else {
            towersOfHanoi(disks - 1, from,to, extra);
            //move disks from starting pole to ending pole.
            moveDisk(from, to);
            towersOfHanoi(disks - 1, extra, from, to);
        }
    }

    //step 10
    int computeK (int n)
    {
        int kValue = 1;

        while ((kValue * (kValue+1)) / 2 < n )
        {
            kValue++;
        }

        return kValue;
    }

    //step 11
    public void reves(int n, Pole source, Pole destination, Pole extra1, Pole extra2)
    {
        if (n ==0)
        {
            return;
        }
        else if (n == 1)
        {
            moveDisk(source, destination);
        }
        else
        {
            int k = computeK(n);

            reves(n-k, source, extra1, extra2, destination);
            towersOfHanoi(k, source, extra2, destination);
            reves(n-k, extra1, destination, source, extra2);
        }
    }
    
    /***************************************************************************
     * *************************************************************************
     * ALL THE CODE PAST THIS POINT SHOULD NOT BE CHANGED
     * *************************************************************************
     * *************************************************************************
     */



    private static int DISPLAY_HEIGHT = 300;
    private static int DISPLAY_WIDTH = 500;

    public JPanel createAnimationPanel()
    {
        return new AnimationPanel();
    }

    private static int NORTH_PANEL_HEIGHT = 50;
    private static int INDENT = 50;
    private static int SCALE = 2;
    private static int TEXT_HEIGHT = 30;
    private static int MAX_DISKS = 15;


    // This privately defined class does the drawing the application needs
    public class AnimationPanel extends JPanel
    {
        public AnimationPanel()
        {
            super();
            setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        }

        public void paintComponent(Graphics g)
        {
            int sw;
            super.paintComponent(g);
            int delta = Disk.BASEWIDTH*SCALE*MAX_DISKS;

            // draw the move string if it has a value
            if(moveString != null)
            {
                g.drawString(moveString, INDENT, NORTH_PANEL_HEIGHT + TEXT_HEIGHT);
            }


            // draw the four poles if they have been created
            FontMetrics fm = g.getFontMetrics();            
            if(d!= null)
            {
                a.drawOn(g, delta/2, DISPLAY_HEIGHT-2*TEXT_HEIGHT, SCALE);
                sw = fm.stringWidth(a.getName());
                g.drawString(a.getName(), delta/2 - sw/2, DISPLAY_HEIGHT-TEXT_HEIGHT);
                
                b.drawOn(g, 3*delta/2, DISPLAY_HEIGHT-2*TEXT_HEIGHT, SCALE);
                sw = fm.stringWidth(b.getName());
                g.drawString(b.getName(), 3*delta/2 - sw/2, DISPLAY_HEIGHT-TEXT_HEIGHT);
                
                c.drawOn(g, 5*delta/2, DISPLAY_HEIGHT-2*TEXT_HEIGHT, SCALE);
                sw = fm.stringWidth(c.getName());
                g.drawString(c.getName(), 5*delta/2 - sw/2, DISPLAY_HEIGHT-TEXT_HEIGHT);
                
                d.drawOn(g, 7*delta/2, DISPLAY_HEIGHT-2*TEXT_HEIGHT, SCALE);
                sw = fm.stringWidth(d.getName());
                g.drawString(d.getName(), 7*delta/2 - sw/2, DISPLAY_HEIGHT-TEXT_HEIGHT);
            }

        }
    }
    
    // **************************************************************************
    // This is the application specific GUI code
    // **************************************************************************    

    private JTextField disksTextField;
    private JLabel setupStatusLabel;
    private JPanel setupPanel;
    
    public void setUpApplicationSpecificControls()
    {
        getAnimationPanel().setLayout(new BorderLayout());
        
        
        disksTextField = new JTextField("");
        disksTextField.addActionListener(
            new ActionListener() 
            {
                public void actionPerformed(ActionEvent event) 
                {
                    disksTextFieldHandler();
                    getAnimationPanel().repaint();
                }
            }
        );


        
        setupStatusLabel = new JLabel("");
        
        setupPanel = new JPanel();
        setupPanel.setLayout(new GridLayout(2,2));
        
        setupPanel.add(new JLabel("Number of disks (1-15):"));
        setupPanel.add(disksTextField);
        setupPanel.add(setupStatusLabel);
        
        getAnimationPanel().add(setupPanel,BorderLayout.NORTH);
               
    }

   
   
    private void disksTextFieldHandler()
    {
    try
        {
            if(applicationControlsAreActive())   // Only change if we are in the setup phase
            {
                String input = disksTextField.getText().trim();
                int value = Integer.parseInt(input);
                if( value>=1 &&value <= MAX_DISKS)
                {
                    disksToUse = value;
                    setupStatusLabel.setText("Set number of disks to " + disksToUse);
                }
                else
                {
                    setupStatusLabel.setText("Bad value for number of disks");
                }
                init();
                getAnimationPanel().repaint();
                
            }
        }
        catch(Exception e)
        {
            // don't change the delta if we had an exception
            setupStatusLabel.setText("Need integer value for number of disks");
        }
    
    }  
            
} // end class RevesActionThread

