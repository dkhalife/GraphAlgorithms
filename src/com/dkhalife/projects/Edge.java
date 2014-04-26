package com.dkhalife.projects;

import java.awt.Color;
import java.io.Serializable;

/**
 * 
 * This class implements a Graph's Edge
 * 
 * @author Dany Khalife
 * @version 1.0
 * @since December, 2012
 * 
 */
public class Edge implements Comparable<Edge>, Serializable {
	// The serial version
	private static final long serialVersionUID = 8445161114077569257L;

	// The edge will link two vertices
	private Vertex v1 = null;
	private Vertex v2 = null;

	// Will have a weight
	private int weight = Integer.MAX_VALUE;

	// And a drawing color
	private Color color = Color.BLUE;

	/**
	 * 
	 * In order to construct an edge we need to know both vertices and a weight
	 * 
	 * @param v1 The first vertex (The order doesn't matter)
	 * @param v2 The second vertex (The order doesn't matter)
	 * @param weight The weight of the edge
	 * 
	 */
	public Edge(Vertex v1, Vertex v2, int weight) {
		this.v1 = v1;
		this.v2 = v2;
		this.weight = weight;
	}

	/**
	 * 
	 * Setter for the weight
	 * 
	 * @param weight the weight to set
	 * 
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * 
	 * Getter for the first vertex
	 * 
	 * @return the v1
	 * 
	 */
	public Vertex getV1() {
		return v1;
	}

	/**
	 * 
	 * Getter for the second vertex
	 * 
	 * @return the v2
	 * 
	 */
	public Vertex getV2() {
		return v2;
	}

	/**
	 * 
	 * Getter for the weight
	 * 
	 * @return the weight
	 * 
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * 
	 * Getter for the color
	 * 
	 * @return the color
	 * 
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * 
	 * Setter for the color
	 * 
	 * @param color the color to set
	 * 
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * 
	 * The equals implementation consists of comparing both vertices. Two dedges
	 * are considered the same if they have both edges, no matter in which order
	 * they are. This implementation makes the edges non directed. In order to
	 * make a directed edge simply extend this class and override the equals
	 * implementation.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (v1 == null) {
			if (other.v1 != null)
				return false;
		}
		if (v2 == null) {
			if (other.v2 != null)
				return false;
		}

		if (v1.equals(other.v1) && v2.equals(other.v2)) {
			return true;
		}
		else if (v1.equals(other.v2) && v2.equals(other.v1)) {
			return true;
		}

		return false;
	}

	/**
	 * 
	 * The compareTo implementation compares two Edges with respect to their
	 * weight
	 * 
	 */
	public int compareTo(Edge n) {
		if (getWeight() == n.getWeight())
			return 0;
		else if (getWeight() < n.getWeight())
			return -1;
		else
			return 1;
	}
}
