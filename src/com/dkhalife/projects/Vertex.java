package com.dkhalife.projects;

import java.awt.Color;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 
 * This class implements a Graph's Vertex
 * 
 * @author Dany Khalife
 * @version 1.0
 * @since December, 2012
 * 
 */
public class Vertex implements Comparable<Vertex>, Serializable {
	// The serial version ID
	private static final long serialVersionUID = -8045762176441807172L;

	// The vertex's coordinates
	private int x;
	private int y;

	// Its name
	private char name;

	// And its color
	private Color color = Color.BLACK;

	// The adjacency list (In a form of a list of edges)
	private List<Edge> edges = new LinkedList<>();

	// The distance to this vertex
	private Integer distance;

	// The ID for automatic name generation
	private static int id = 0;

	// The set of deleted vertex names (to be reused)
	private static Queue<String> deleted = new LinkedList<>();

	// The parent, or previous vertex of the current one
	private Vertex previous = null;

	// The edge that let us arrive here
	private Edge viaEdge = null;

	/**
	 * 
	 * Getter for the edge that let us arrive to this vertex
	 * 
	 * @return the viaEdge
	 * 
	 */
	public Edge getViaEdge() {
		return viaEdge;
	}

	/**
	 * 
	 * Getter for the distance
	 * 
	 * @return the distance
	 * 
	 */
	public Integer getDistance() {
		return distance;
	}

	/**
	 * 
	 * Setter for the distance
	 * 
	 * @param distance the distance to set
	 * 
	 */
	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	/**
	 * 
	 * Setter for the edge that let us arrive to this vertex
	 * 
	 * @param viaEdge the viaEdge to set
	 * 
	 */
	public void setViaEdge(Edge viaEdge) {
		this.viaEdge = viaEdge;
	}

	/**
	 * 
	 * Getter for the list of outgoing edges from this vertex
	 * 
	 * @return The list of edges departing from this vertex
	 * 
	 */
	public List<Edge> getEdges() {
		return edges;
	}

	/**
	 * 
	 * Getter for the parent or previous Vertex
	 * 
	 * @return the previous
	 * 
	 */
	public Vertex getPrevious() {
		return previous;
	}

	/**
	 * 
	 * Setter for the parent or previous Vertex
	 * 
	 * @param previous the previous to set
	 * 
	 */
	public void setPrevious(Vertex previous) {
		this.previous = previous;
	}

	/**
	 * 
	 * In order to construct a vertex, we only need its coordinates
	 * 
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 * 
	 */
	public Vertex(int x, int y) {
		this.x = x;
		this.y = y;
		name = getNewName();
	}

	/**
	 * 
	 * When a vertex is deleted, consider calling this method to reuse its name
	 * 
	 * @param v The Vertex for which to reuse the name
	 * 
	 */
	public static void delete(Vertex v) {
		deleted.offer(String.valueOf(v.getName()));
	}

	/**
	 * 
	 * Adds an outgoing Edge departing from this Vertex (only once)
	 * 
	 * @param e The Edge to add
	 * 
	 */
	public void addEdge(Edge e) {
		if (!edges.contains(e))
			edges.add(e);
	}

	/**
	 * 
	 * Removes an outgoing Edge that was departing from this Vertex (if it exists)
	 * 
	 * @param e The Edge to be removed
	 * 
	 */
	public void removeEdge(Edge e) {
		edges.remove(e);
	}

	/**
	 * 
	 * Getter for the X coordinate
	 * 
	 * @return the x
	 * 
	 */
	public int getX() {
		return x;
	}

	/**
	 * 
	 * Getter for the Y coordinate
	 * 
	 * @return the y
	 * 
	 */
	public int getY() {
		return y;
	}

	/**
	 * 
	 * Getter for the Vertex's name
	 * 
	 */
	public char getName() {
		return name;
	}

	/**
	 * 
	 * Getter for te Vertex's color
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
	 * New unique name generator
	 * 
	 */
	private static char getNewName() {
		if (deleted.isEmpty())
			return (char) ('A' + (id++));
		else
			return deleted.poll().charAt(0);
	}

	/**
	 * 
	 * Setter for the ID
	 * 
	 * @param id the id to set
	 * 
	 */
	public static void setId(int id) {
		Vertex.id = id;
	}

	/**
	 * 
	 * Getter for the ID
	 * 
	 * @return the id
	 * 
	 */
	public static int getId() {
		return id;
	}

	/**
	 * 
	 * This equals implementation compares two vertices by their name.
	 * Two vertices are considered equal if both of them have the same name.
	 * For this project, the coordinates are not considered important in comparing two vertices. 
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
		Vertex other = (Vertex) obj;
		if (name != other.name)
			return false;
		return true;
	}

	/**
	 * 
	 * The compareTo implementation compares two vertices by their distance
	 * 
	 */
	public int compareTo(Vertex other) {
		if (distance == other.distance)
			return 0;
		else if (distance < other.distance)
			return -1;
		else
			return 1;
	}
}