package astar;

import java.awt.BasicStroke;
import java.util.HashMap;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;

import java.util.Random;
public class Node {
	
	public static final int TYPE_NODE = 0, TYPE_START = 2, TYPE_END = 3;
	
	
	private float m_heuristic;
	public String m_label;
	public float m_posX, m_posY;
	
	public int m_type;
	private ArrayList<Node> m_connections;
	private HashMap<Node, Float> m_distances = new HashMap<Node, Float>();
	public Color m_color;
	public Node(){
		m_connections = new ArrayList<Node>();
		
		m_label = "NoLabel";
		m_posX = 0;
		m_posY = 0;
		m_type = TYPE_NODE;
		/*Random random = new Random(System.currentTimeMillis());
		float r = random.nextFloat(0.3f, 1);
		float g = random.nextFloat(0.3f, 1);
		float b = random.nextFloat(0.3f, 1);
		m_color = new Color(r, g, b);*/
	}
	
	public Node(float x, float y, int type) {
		m_connections = new ArrayList<Node>();
		m_label = "NoLabel";
		m_posX = x;
		m_posY = y;
		m_type = type;
		/*Random random = new Random(System.currentTimeMillis());
		float r = random.nextFloat(0.3f, 1);
		float g = random.nextFloat(0.3f, 1);
		float b = random.nextFloat(0.3f, 1);
		m_color = new Color(r, g, b);*/
	}
	
	public void setHeuristic(float h) { m_heuristic = h; }
	public float getHeuristic() { return m_heuristic; }
	
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
				
				g.setColor(Color.black);
				g.drawLine(x1, y1, x2, y2);
				g.setColor(Color.MAGENTA);
				g.drawString(Float.toString(m_distances.get(n)), x3, y3);
				
			}
			g.setStroke(oldStroke);
		}catch(java.util.ConcurrentModificationException e) {
			
		}

		
	}
}
