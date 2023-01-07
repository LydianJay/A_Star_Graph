package astar;

import java.awt.BasicStroke;
import java.util.HashMap;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;


public class Node {
	
	public static final int TYPE_NODE = 0, TYPE_START = 2, TYPE_END = 3, TYPE_PATH = 4; // the type constant
	
	// -- Stuff use in the a star algorithm
	private float m_heuristic = 0;
	private float m_fCost = Float.MAX_VALUE;
	private float m_gCost = 0;
	private Node m_parentNode = null;
	// ----------------------
	
	
	public float m_posX, m_posY; 											// position of the node in the world
	
	public int m_type; 														// what type of nodes specified by TYPE_NODE, TYPE_START...
	private ArrayList<Node> m_connections;
	private HashMap<Node, Float> m_distances = new HashMap<Node, Float>(); 	// the hashmap that contains the distances of each connections of this node
	public Color m_color;													// color of the node this might differ by the type... probably redundant variable
	public Node(){
		m_connections = new ArrayList<Node>();
		
		
		m_posX = 0;
		m_posY = 0;
		m_type = TYPE_NODE;
		
	}
	
	public Node(float x, float y, int type) {
		m_connections = new ArrayList<Node>();
		
		m_posX = x;
		m_posY = y;
		m_type = type;
		
	}
	
	public Node getParentNode() {
		return m_parentNode;
	}

	public void setParentNode(Node m_parentNode) {
		this.m_parentNode = m_parentNode;
	}
	
	public void setGCost(float gCost) {
		m_gCost = gCost;
	}
	
	void updateFCost() {
		m_fCost = m_gCost + m_heuristic;
	}
	
	public float getFCost() {
		return m_fCost;
	}
	
	public float getGCost() {
		return m_gCost;
	}
	
	public void calculateHeuristic(Node n) {
		
		// calculate the distance from the end node to this node
		// n = end node
		if(this == n) {
			m_heuristic = 0;
			return;
		}
		
		int x1 = (int) m_posX, y1 = (int) m_posY;
		int x2 = (int) n.m_posX, y2 = (int) n.m_posY;
		
		m_heuristic = (float) Math.sqrt( Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) ); // euclidean distance
		
	}

	public void setHeuristic(float h) { m_heuristic = h; }
	public float getHeuristic() { return m_heuristic; }
	
	
	public float getDistanceFromStartNode(Node start) {
		
		Node n = start;
		int x1 = (int) m_posX, y1 = (int) m_posY;
		int x2 = (int) n.m_posX, y2 = (int) n.m_posY;
		return (float) Math.sqrt( Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) );
	}
	
	
	public  ArrayList<Node> getConnections() {
		return m_connections;
	}
	
	public float getDistanceFromNode(Node n) {
		return m_distances.get(n);
	}
	
	
	public void addConnection(Node n) {
		if(!m_connections.contains(n)) {
			m_connections.add(n);
			
			int x1 = (int) m_posX, y1 = (int) m_posY;
			int x2 = (int) n.m_posX, y2 = (int) n.m_posY;
			
			float fDistance = (float) Math.sqrt( Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) );
			m_distances.put(n, fDistance);
		}
			
	}
	
	public void updateDistance() {
		/*
		 * This method is called as the nodes is drag around the world
		 * its position will change does all of its the distance for each connections needs to be updated
		 * 
		 */
		for(int i = 0; i < m_connections.size(); i++) {
			Node n = m_connections.get(i);
			int x1 = (int) m_posX, y1 = (int) m_posY;
			int x2 = (int) n.m_posX, y2 = (int) n.m_posY;
			float fDistance = (float) Math.sqrt( Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) );
			m_distances.put(n, fDistance);
			
		}
		
	}
	
	public void setType(int type) {
		m_type = type;
	}
	public int getType() { return m_type; }
	
	public void drawNode(Graphics2D g, int cameraX, int cameraY, int nodeSize) {
		
		/*
		 * Draws the node with corresponding color that differs for each type
		 * 
		 */
		try {
			
			switch(m_type) {
			case TYPE_NODE:
				g.setColor(Color.LIGHT_GRAY);
				break;
			case TYPE_START:
				g.setColor(Color.green);
				break;
			case TYPE_END:
				g.setColor(Color.red);
				break;
			case TYPE_PATH:
				g.setColor(Color.BLUE);
				break;
			}
			
			
			g.fillOval((int)(m_posX + cameraX), (int)(m_posY + cameraY) , nodeSize, nodeSize);
		
		}catch(java.util.ConcurrentModificationException e) {
			
		}
		
	}
	public boolean hasConnection(Node n) {
		return m_connections.contains(n);
	}
	public void clearConnections() {
		m_connections.clear();
		m_distances.clear();
	}
	
	public void removeConnection(Node n) {
		m_connections.remove(n);
		m_distances.remove(n);
	}
	
	public void drawConnections(Graphics2D g,int cameraX, int cameraY, int nodeSize) {
		
		/*
		 * Draws the lines/connections to each node connected
		 */
		
		try {
			
			Stroke oldStroke = g.getStroke();
			g.setStroke(new BasicStroke(10));
			g.setColor(Color.black);
			
			for(Node n : m_connections) {
				int x1 = (int)(m_posX + cameraX) + nodeSize/2;
				int y1 = (int)(m_posY + cameraY) + nodeSize/2;
				int x2 = (int)(n.m_posX + cameraX) + nodeSize/2;
				int y2 = (int)(n.m_posY + cameraY) + nodeSize/2;
				int x3 = (int)((x1 + x2) / 2);
				int y3 = (int)((y1 + y2) / 2);
				
				
				
				switch(n.m_type) {
				
				case TYPE_PATH:
					g.setColor(Color.green);
					
					
					break;
					
				case TYPE_END:
					if(m_type == TYPE_PATH)
						g.setColor(Color.green);
					else
						g.setColor(Color.black);
					break;
				case TYPE_START:
					if(m_type == TYPE_PATH)
						g.setColor(Color.green);
					else
						g.setColor(Color.black);
					break;
				
					
				default:
					g.setColor(Color.black);
					break;
				}
				
				
				if(m_type != TYPE_PATH) {
					g.setColor(Color.black);
				}
				
				
				
				g.drawLine(x1, y1, x2, y2);
				
				
				g.setColor(Color.MAGENTA);
				g.drawString(Float.toString(m_distances.get(n)), x3, y3);
				
			}
			g.setStroke(oldStroke);
		}catch(java.util.ConcurrentModificationException e) {
			
		}

		
	}
}
