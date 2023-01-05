package rendering;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseMotionListener;
import astar.Node;
import java.util.ArrayList;

public class Render extends JPanel implements MouseListener, MouseMotionListener, KeyListener{
	
	private int m_width, m_height;
	private float m_cameraX = 0, m_cameraY = 0;
	private Image m_frontBuffer, m_backBuffer;
	private final int m_nodeSize = 80; 
	private int m_mouseOldX, m_mouseOldY;
	private float m_dragSentivity = 0.4f;
	private int m_currentMouseKeyDragging;
	
	private ArrayList<Node> m_nodes;
	private Node m_currentActiveNode = null;
	private Node m_currentSelectionNode = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public Render(int w, int h){
		m_nodes = new ArrayList<Node>();
		m_currentMouseKeyDragging = 0;
		
		this.m_width = w;
		this.m_height = h;
		setVisible(true);
		setPreferredSize(new Dimension(m_width, m_height));
		setDoubleBuffered(true);
		setOpaque(true);
	}
	
	
	
	public void paintComponent(Graphics g) {
		
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(m_frontBuffer, 0, 0,this);
		g2.dispose();
	}
	
	public void update() {
		
		
		while(true) {
			m_backBuffer = createImage(m_width, m_height);
			Graphics2D graphics = (Graphics2D)(m_backBuffer.getGraphics());
			graphics.setColor(Color.cyan);
			Image temp = m_backBuffer;
			m_backBuffer = m_frontBuffer;
			m_frontBuffer = temp;
			
			
			drawNodes(graphics);
			
			
			repaint();
		
			
		}
	}
	
	private void drawNodes(Graphics2D g) {
		
		for(Node n : m_nodes) {
			n.drawNode(g, (int)m_cameraX, (int)m_cameraY, m_nodeSize);
		}
		
	}
	
	
	private boolean isCursorInsideCircle(int x, int y, int nX, int nY) {
		
		int s = m_nodeSize;
		
		if(x >= nX && x <= nX + s + (s/16) && y >= nY && y <= nY + s + (s/2)) {
			return true;
		}
	
		
		return false;
		
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	
		
		if(e.getButton() == MouseEvent.BUTTON1) {
			Point loc = e.getPoint();
			
			int x = (int) (loc.x - m_cameraX);
			int y = (int) (loc.y - m_cameraY);
			boolean alreadyExist = false;
			for(Node n : m_nodes) {
				if(isCursorInsideCircle(x , y, (int)(n.m_posX), (int)(n.m_posY ) )) {
					alreadyExist = true;
					return;
				}
			}
			if(!alreadyExist)
				m_nodes.add(new Node((float)x - (m_nodeSize / 2) , (float)y - m_nodeSize));
		}
	}



	@Override
	public void mousePressed(MouseEvent e) {
		Point loc = e.getPoint();
		
		int x = (int) (loc.x - m_cameraX);
		int y = (int) (loc.y - m_cameraY);
		m_currentMouseKeyDragging = e.getButton();
		switch(e.getButton()) {
			
		case MouseEvent.BUTTON1: 
			
			
			m_mouseOldX = x;
			m_mouseOldY = y;
			for(Node n : m_nodes) {
				if(isCursorInsideCircle(x, y, (int)(n.m_posX), (int)(n.m_posY) )) {
					m_currentActiveNode = n;
					return;
				}
			}
			
		
			break;
			// ------------------------------------
		case MouseEvent.BUTTON3: 
			
			
			for(Node n : m_nodes) {
				if(isCursorInsideCircle(x, y, (int)(n.m_posX), (int)(n.m_posY) )) {
					m_currentSelectionNode = n;
					return;
				}
			}
		
			break;
			// -------------------------------------
		}
		
		
		
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		Point loc = e.getPoint();
		
		int x = (int) (loc.x - m_cameraX);
		int y = (int) (loc.y - m_cameraY);
		
		
		switch(m_currentMouseKeyDragging) {
		case MouseEvent.BUTTON1:
			Node n = m_currentActiveNode;
			
			if(n != null) {
				n.m_posX = x - (m_nodeSize / 2);
				n.m_posY = y - m_nodeSize;
			}
			else{
				m_cameraX -= (m_mouseOldX - x) * m_dragSentivity;
				m_cameraY -= (m_mouseOldY - y) * m_dragSentivity;
				m_mouseOldX = x;
				m_mouseOldY = y;
			}
			break;
		}
		
		
		
		
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
		Point loc = e.getPoint();
		
		int x = (int) (loc.x - m_cameraX);
		int y = (int) (loc.y - m_cameraY);
		
		switch(e.getButton()) {
		case MouseEvent.BUTTON1:
			
			m_currentActiveNode = null;
			break;
		case MouseEvent.BUTTON3:
			
			for(Node n : m_nodes) {
				
				if(isCursorInsideCircle(x, y, (int)(n.m_posX), (int)(n.m_posY) ) && n != m_currentSelectionNode) {
					 
					m_currentSelectionNode.addConnection(n);
					
					return;
				}
			}
			
			m_currentSelectionNode = null;
			break;
		}
		
		
		

	}



	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}




	



	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
		switch(e.getKeyCode()) {
		case KeyEvent.VK_A:
			m_cameraX--;
			break;
		case KeyEvent.VK_D:
			m_cameraX++;
			break;
		case KeyEvent.VK_S:
			m_cameraY++;
			break;
		case KeyEvent.VK_W:
			m_cameraY--;
			break;
		}
		
	}



	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
