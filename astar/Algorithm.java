package astar;
import java.util.ArrayList;




/*
 * 
 * This is the algorithm class the contains the methods for the A star algorithm
 * 
 */


public class Algorithm {
	
	
	private ArrayList<Node> m_nodes; // the reference the nodes
	private Node m_start;			// reference to the start node
	private Node m_end;				// reference to the end node
	private ArrayList<Node> m_openList = new ArrayList<Node>(), m_closeList = new ArrayList<Node>();	// the open list and close list necessary for the algo
	
	public void initAlgorithm(ArrayList<Node> n, Node str, Node end) {
		m_nodes = n;
		m_start = str;
		m_end = end;
		
		
	}
	
	/*
	 * Calculates the fcost, gcost and heuristic of each neighbor nodes
	 * also updates the parent nodes of each of the neighbor specified in the parameter
	 */
	
	private void calculateNextNodes(Node n) {
		
		ArrayList<Node> next = n.getConnections();
		
		for (Node nxt : next) {
			
			if(m_closeList.contains(nxt)) {
				continue;
			}
			
			if(!m_openList.contains(nxt)) {
				nxt.setParentNode(n);
				nxt.calculateHeuristic(m_end);
				float gCost = nxt.getDistanceFromStartNode(m_start) + n.getDistanceFromStartNode(m_start);
				nxt.setGCost(gCost);
				nxt.updateFCost();
				
				m_openList.add(nxt); // adds the neighbor node to the open list/unvisited list
			}
			
		}
		
	}
	
	
	public void reset() {
		m_openList.clear();
		m_closeList.clear();
	}
	
	
	public int getLowestFCost() {
		
		int index = 0;
		int i = 0;
		float lowestFcost = Float.MAX_VALUE;
		for(Node n : m_openList) {
			if(n.getFCost() < lowestFcost) {
				index = i;
				lowestFcost = n.getFCost();
			}
			i++;
		}
		return index;
	}
	
	
	public void runAStarAlgorithm() {
		
		Node currentNode = m_start;
		m_openList.add(currentNode);
		calculateNextNodes(currentNode);
		
		while(currentNode != m_end && !m_openList.isEmpty()) {
			
			
			int idx = getLowestFCost();
			
			
			currentNode = m_openList.get(idx);
			m_openList.remove(idx);
			
			
			if(!m_closeList.contains(currentNode)) {
				m_closeList.add(currentNode);
			}
			
			if(currentNode == m_end) {
				
				break;
			}
			
			
			calculateNextNodes(currentNode);
		}
		
		
		
		
		Node pathing = m_end.getParentNode();
		
	
		while(pathing != m_start && pathing != null) {
			
			
			
			pathing.setType(Node.TYPE_PATH);
			
			pathing = pathing.getParentNode();
		}
		
		
	}
	
	
	
}
