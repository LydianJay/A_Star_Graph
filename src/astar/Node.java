package astar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;

import java.util.Random;
public class Node {
	private float m_heuristic;
	public String m_label;
	public float m_posX, m_posY;
	
	private ArrayList<Node> m_connections;
	public Color m_color;
	public Node(){
		m_connections = new ArrayList<Node>();
		m_label = "NoLabel";
		m_posX = 0;
		m_posY = 0;
		Random random = new Random(System.currentTimeMillis());
		float r = random.nextFloat(0.3f, 1);
		float g = random.nextFloat(0.3f, 1);
		float b = random.nextFloat(0.3f, 1);
		m_color = new Color(r, g, b);
	}
	
	public Node(float x, float y) {
		m_connections = new ArrayList<Node>();
		m_label = "NoLabel";
		m_posX = x;
		m_posY = y;
		Random random = new Random(System.currentTimeMillis());
		float r = random.nextFloat(0.3f, 1);
		float g = random.nextFloat(0.3f, 1);
		float b = random.nextFloat(0.3f, 1);
		m_color = new Color(r, g, b);
	}
	
	public void setHeuristic(float h) { m_heuristic = h; }
	public float getHeuristic() { return m_heuristic; }
	
	public void addConnection(Node n) {
		if(!m_connections.contains(n))
			m_connections.add(n);
	}
	
	
	
	public void drawNode(Graphics2D g, int cameraX, int cameraY, int nodeSize) {
		try {
			
			g.setColor(m_color);
			g.fillOval((int)(m_posX + cameraX), (int)(m_posY + cameraY) , nodeSize, nodeSize);
			Stroke oldStroke = g.getStroke();
			g.setStroke(new BasicStroke(10));
			g.setColor(Color.black);
			for(Node n : m_connections) {
				g.drawLine((int)(m_posX + cameraX) + nodeSize/2, (int)(m_posY + cameraY) + nodeSize, (int)(n.m_posX + cameraX) + nodeSize/2, (int)(n.m_posY + cameraY) + nodeSize/2);
			}
			g.setStroke(oldStroke);
		}catch(java.util.ConcurrentModificationException e) {
			
		}
		
	}
	
}
