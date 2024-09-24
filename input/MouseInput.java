package input;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseInput implements MouseListener{

	public boolean isClick = false;
	public MouseEvent mousePressed;
	public int mouse = 1;
	public int x, y;
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		isClick = true;
		Point loc = e.getPoint();
		
		x = loc.x;
		y = loc.y;
		
		mouse = e.getButton();
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		mousePressed = e;
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
