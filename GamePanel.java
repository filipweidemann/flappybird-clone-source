package flappybird;
import java.awt.Graphics;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel{
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Bird.flappyBird.repaint(g);
	}
}
