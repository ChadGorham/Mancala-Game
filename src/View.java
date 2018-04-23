import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class View extends JPanel implements ChangeListener
{
    private static final int BOARD_WIDTH = 700;
    private static final int BOARD_HEIGHT = 300;
    private static final int OFFSET_FROM_BORDER = 30;
    private int x = (App.FRAME_WIDTH-BOARD_WIDTH)/2;
    private int y = (App.FRAME_HEIGHT-BOARD_HEIGHT)/2;
    private ArrayList<Shape> pits;
    private Color backgroundColor;
    private ArrayList<Integer> stoneCount;
    private Model model;

    View(Model m, Color col)
    {
        model = m;
        stoneCount = m.getStoneCount();
        pits = new ArrayList<>();
        backgroundColor = col;
        double topX = BOARD_WIDTH/8;
        double baseY = y + OFFSET_FROM_BORDER;
        double pitWidth = BOARD_WIDTH / 10;
        double pitHeight = BOARD_HEIGHT / 3;

        pits.add(new RoundRectangle2D.Double(x+20, y+OFFSET_FROM_BORDER/2, pitWidth, BOARD_HEIGHT-OFFSET_FROM_BORDER, 60, 60));// Add Player 2 Mancala at index 13.

        int topY = y+((BOARD_HEIGHT*2)/3)-OFFSET_FROM_BORDER;

        for (int i = 1; i < 7; i++)                                                 // Top Row Pits
            pits.add(new Ellipse2D.Double(x+(topX*i)+10, topY, pitWidth, pitHeight));

        pits.add(new RoundRectangle2D.Double(x+(topX*7), y+ OFFSET_FROM_BORDER/2, pitWidth, BOARD_HEIGHT-OFFSET_FROM_BORDER, 60, 60));// Add Player 1 Mancala at index 0

        for (int i = 6; i > 0; i--)                                                 // Bottom Row Row Pits
            pits.add(new Ellipse2D.Double(x+(topX*i)+10, baseY, pitWidth, pitHeight));
    }

    void attachMouseListener(MouseListener l)
    {
        addMouseListener(l);
    }

    int insideShape(Point2D p)
    {
    	//int state = 0; use state variable for turns which change from 0 to 1, use this to determine which mancala is skipped
        int result = -1;
        for (int i = 0; i < pits.size(); i++)
            if (pits.get(i).contains(p))
            {
            	result = i;
            	int numstones = stoneCount.get(i);
            	stoneCount.set(i,0);
            	for(int j=0; j<numstones;j++)
            	{
            		//if(j+1==numstones&& new pit value = 1)
            	//	{
            	//		set paired pit and current pit to 0
            	//		
            	//	}
            		if(i==13)
            		{
            			i=-1;
            		}
            		stoneCount.set(i+1, stoneCount.get(i+1)+1);
            		i++;
            	}
            }
        repaint();
        return result;
        
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(3));
        for (int i = 0; i < pits.size(); i++)
        {
            Shape e = pits.get(i);
            Rectangle bound = e.getBounds();
            g2.draw(e);
            int count = stoneCount.get(i);
            Ellipse2D stone;
            double row = bound.getCenterX()-12;
            double col = bound.getCenterY()-(count/2.5)*12;
            int colCount = 0;
            int rowCount = 0;
            for (int j = 0; j < count; j++)
            {
                col += colCount++ % 2 == 0 ? 14 : 0;
                double x = rowCount++ % 2 == 0 ? row + 14 : row;
                stone = new Ellipse2D.Double(x, col, 10,10);
                g2.fill(stone);
            }
        }
        RoundRectangle2D boundary = new RoundRectangle2D.Double(x, y, BOARD_WIDTH,BOARD_HEIGHT,100,100);
        g2.setStroke(new BasicStroke(5));
        setBackground(backgroundColor);
        g2.draw(boundary);
    }

    public void stateChanged(ChangeEvent e)
    {
        stoneCount = model.getStoneCount();
        repaint();
    }
}
