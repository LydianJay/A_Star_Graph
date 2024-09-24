package rendering;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseMotionListener;
import astar.Node;
import java.util.ArrayList;
import astar.Algorithm;

public class Render extends JPanel implements MouseListener, MouseMotionListener, KeyListener{
	
	private int m_width, m_height;	// width and height of jpanel
	private float m_cameraX = 0, m_cameraY = 0; // the camera position
	private Image m_frontBuffer, m_backBuffer; // the front and back buffer for double buffering
	private final int m_nodeSize = 80; 	// the size of each node in screen
	private int m_mouseOldX, m_mouseOldY; // for tracking 
	private int m_mouseX = 0, m_mouseY = 0; // 
	private float m_dragSentivity = 0.8f; //  the drag sentivity
	
	private int m_currentMouseKeyDragging; // the current mouse button that is dragging
	
	private ArrayList<Node> m_nodes; // the list of existing nodes
	private Node m_currentActiveNode = null; // the current active node
	private Node m_currentSelectionNode = null; // the current selected node
	private Node m_startNode = null; // the reference to the start node
	private Node m_endNode = null; // the reference to the end node
	private Algorithm algo = new Algorithm();
	private static final long serialVersionUID = 1L; // useless shit
	private boolean doUpdate = true;
	
	public Render(int w, int h){ // init jpanel and stuff
		
		
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
		
		g2.drawImage(m_frontBuffer, 0, 0, this); // draw the front buffer
		g2.dispose();
	}
	
	/*
	 * The update function that updates everything
	 */
	public void update() {
		
		
		while(true) {
			
			
			if(doUpdate) {
				m_backBuffer = createImage(m_width, m_height);
				Graphics2D graphics = (Graphics2D)(m_backBuffer.getGraphics());
				
				
				Image temp = m_backBuffer;
				m_backBuffer = m_frontBuffer; // swap the front and back buffer
				m_frontBuffer = temp;
				
				
				drawNodes(graphics); // render the buffer
				
				doUpdate = false;
			}
			  // update the screen
			repaint();
			
		}
	}
	
	private void drawNodes(Graphics2D g) {
		
		try {
			//draws the connections/lines first
			for(Node n : m_nodes) {
				n.drawConnections(g, (int)m_cameraX, (int)m_cameraY, m_nodeSize);
			}
			
			//draws the nodes last
			for(Node n : m_nodes) {
				n.drawNode(g, (int)m_cameraX, (int)m_cameraY, m_nodeSize);
			}
			
			
			if(m_currentSelectionNode != null) {
				
				Node n = m_currentSelectionNode;
				int x = (int)(n.m_posX + m_cameraX) + m_nodeSize/2;
				int y = (int)(n.m_posY + m_cameraY) + m_nodeSize/2;
				int x2 = (int)( m_mouseX + m_cameraX);
				int y2 = (int)(m_mouseY + m_cameraY);
				Stroke oldStroke = g.getStroke();
				g.setStroke(new BasicStroke(10));
				g.drawLine(x , y, x2, y2);
				g.setStroke(oldStroke);
				
			}
			
		}
		catch(java.util.ConcurrentModificationException e) {
			
		}
		
		
	}
	
	/*
	 * checks if the position of the cursor is inside a node
	 */
	private boolean isCursorInsideCircle(int x, int y, int nX, int nY) {
		
		int s = m_nodeSize;
		// AABB collision detection
		if(x >= nX && x <= nX + s + (s/16) && y >= nY && y <= nY + s + (s/2)) {
			return true;
		}
		return false;
		
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		
		doUpdate = true;
		
		if(e.getButton() == MouseEvent.BUTTON1) { // if left click occurred create a new node in a position if in that position no node exist
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
				m_nodes.add(new Node((float)x - (m_nodeSize / 2) , (float)y - m_nodeSize, Node.TYPE_NODE));
		}
	}



	@Override
	public void mousePressed(MouseEvent e) {
		Point loc = e.getPoint();
		doUpdate = true;
		int x = (int) (loc.x - m_cameraX);
		int y = (int) (loc.y - m_cameraY);
		m_currentMouseKeyDragging = e.getButton();
		switch(e.getButton()) {
			
		case MouseEvent.BUTTON1:  // if left click is pressed it will select the node for dragging
			
			
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
		case MouseEvent.BUTTON3: // if right click is pressed we select the node that will have a connection or be deleted
			
			
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
		doUpdate = true;
		int x = (int) (loc.x - m_cameraX);
		int y = (int) (loc.y - m_cameraY);
		m_mouseX = x;
		m_mouseY = y;
		
		switch(m_currentMouseKeyDragging) {
		case MouseEvent.BUTTON1:
			Node n = m_currentActiveNode; // the node that was pressed 
			
			if(n != null) {
				n.m_posX = x - (m_nodeSize / 2);	// update the position of the node
				n.m_posY = y - m_nodeSize;
				n.updateDistance(); 
				
				
				for(Node a : m_nodes) {// update the distance for each of its connections 
					if(a != n) {
						if(a.hasConnection(n))
							a.updateDistance();
					}
				}
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
		doUpdate = true;
		int x = (int) (loc.x - m_cameraX);
		int y = (int) (loc.y - m_cameraY);
		
		switch(e.getButton()) {
		case MouseEvent.BUTTON1:
			
			m_currentActiveNode = null; // left click was released the currentActiveNode or dragging node will be released
			break;
		case MouseEvent.BUTTON3:
			
			/*
			 * If right click is released and there is a node in the cursor it will add that connection
			 * 
			 */
			
			for(Node n : m_nodes) {
				
				if(isCursorInsideCircle(x, y, (int)(n.m_posX), (int)(n.m_posY) ) && n != m_currentSelectionNode) {
					
					if(m_currentSelectionNode != null) {
						m_currentSelectionNode.addConnection(n);
						n.addConnection(m_currentSelectionNode);
						m_currentSelectionNode = null;
						return;
					}
					
					
				}
			}
			
			/*
			 * if no node was in the cursor position the node that was held previously will be deleted
			 */
			
			if(m_currentSelectionNode != null) { 
				m_currentSelectionNode.clearConnections();
				
				for(int i = 0; i < m_nodes.size(); i++) {
					if(m_currentSelectionNode != m_nodes.get(i)) {
						m_nodes.get(i).removeConnection(m_currentSelectionNode);
					}
				}
				
				m_nodes.remove(m_currentSelectionNode);
			}
			
			m_currentSelectionNode = null;
			break;
		}
		
		

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		doUpdate = true;
		Point loc = e.getPoint();
		
		int x = (int) (loc.x - m_cameraX);
		int y = (int) (loc.y - m_cameraY);
		
		m_mouseX = x; // save mouse posistion for later use
		m_mouseY = y;
		
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
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		doUpdate = true;
		final float fScrollSpeed = 30;
		switch(e.getKeyCode()) {
		
		// ---------- Scrolling with WASD keys -----------------
		case KeyEvent.VK_A:
			m_cameraX -= fScrollSpeed * m_dragSentivity;
			break;
		case KeyEvent.VK_D:
			m_cameraX += fScrollSpeed * m_dragSentivity;
			break;
		case KeyEvent.VK_S:
			m_cameraY += fScrollSpeed * m_dragSentivity;
			break;
		case KeyEvent.VK_W:
			m_cameraY -= fScrollSpeed * m_dragSentivity;
			break;
		// ---------- Scrolling with WASD keys -----------------
		
		
		case KeyEvent.VK_SPACE: // reset and start the a star algorithm 
			
			
			if(m_startNode != null && m_endNode != null) {
				
				algo.reset();
				
				for(Node n : m_nodes) {
					if(n.getType() == Node.TYPE_PATH )
						n.setType(Node.TYPE_NODE);
					n.setParentNode(null);
				}
				
				
				algo.initAlgorithm(m_nodes, m_startNode, m_endNode);
				algo.runAStarAlgorithm();
			}
			
			break;
		case KeyEvent.VK_ESCAPE: // reset a star algorithm
			algo.reset();
			for(Node n : m_nodes) {
				if(n.getType() == Node.TYPE_PATH )
					n.setType(Node.TYPE_NODE);
				
				n.setParentNode(null);
			}
			break;


		}
		
	}



	@Override
	public void keyReleased(KeyEvent e) {
		doUpdate = true;
		switch(e.getKeyCode()) {
		
		case KeyEvent.VK_Q: {
			int x = m_mouseX, y = m_mouseY;
			
			boolean alreadyExist = false;
			for(Node n : m_nodes) {
				if(isCursorInsideCircle(x , y, (int)(n.m_posX), (int)(n.m_posY ) )) {
					alreadyExist = true;
					
					if(m_startNode != null) {
						m_startNode.setType(Node.TYPE_NODE);
						
					}
					n.setType(Node.TYPE_START);
					m_startNode = n;
					m_startNode.setType(Node.TYPE_START);
					
					return;
				}
			}
			if(!alreadyExist) {
				
				if(m_startNode != null) {
					m_startNode.setType(Node.TYPE_NODE);
					
				}
				Node n = new Node((float)x - (m_nodeSize / 2) , (float)y - m_nodeSize, Node.TYPE_START);
				m_startNode = n;
				m_nodes.add(n);
				
				
			}
				
		}
			
			break;
		case KeyEvent.VK_E:{
			int x = m_mouseX, y = m_mouseY;
			
			boolean alreadyExist = false;
			for(Node n : m_nodes) {
				if(isCursorInsideCircle(x , y, (int)(n.m_posX), (int)(n.m_posY ) )) {
					alreadyExist = true;
					
					if(m_endNode != null) {
						m_endNode.setType(Node.TYPE_NODE);
						
					}
					n.setType(Node.TYPE_END);
					m_endNode = n;
					m_endNode.setType(Node.TYPE_END);
					
					
					return;
				}
			}
			if(!alreadyExist) {
				
				if(m_endNode != null) {
					m_endNode.setType(Node.TYPE_NODE);
					
				}
				Node n = new Node((float)x - (m_nodeSize / 2) , (float)y - m_nodeSize, Node.TYPE_END);
				m_endNode = n;
				m_nodes.add(n);
			}
			
			
			
		}
			
			break;
		}
		
	}
	
}
