package com.dkhalife.projects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.PriorityQueue;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This class represents the area where we will draw everything
 * 
 * @author Dany Khalife
 * @version 1.0
 * @since December, 2012
 * 
 */
class Panel extends JPanel {
	// Eclipse generated UID
	private static final long serialVersionUID = 2396636661038057893L;

	// Panel dimensions
	private int pHeight;
	private int pWidth;

	// Drawing mode
	public static final int VERTICES = 0;
	public static final int EDGES = 1;
	private int drawing = Panel.VERTICES;

	/**
	 * 
	 * This method changes the drawing mode
	 * 
	 * @param drawing the drawing to set
	 * 
	 */
	public void setDrawing(int drawing) {
		this.drawing = drawing;

		if (drawing == EDGES && nearestV != null) {
			V.get(nearestV).setColor(Color.BLACK);
		}
		else if (drawing == VERTICES && nearestV != null) {
			V.get(nearestV).setColor(Color.BLACK);
		}

		repaint();
		nearestV = null;
	}

	// Highlighted vertex
	private Integer nearestV = null;

	// Horizontal and vertical resolutions
	private static final int hres = 20;
	private static final int wres = 20;

	// The list of Vertices
	private Vector<Vertex> V = new Vector<>();

	// The list of Edges
	private Vector<Edge> E = new Vector<>();

	/**
	 * 
	 * A panel is constructed using two arguments, its width and its height
	 * 
	 * @param w The panel's width
	 * @param h The panel's height
	 * 
	 */
	public Panel(int w, int h) {
		// Set dimensions
		pWidth = w * wres;
		pHeight = h * hres;

		// Allow focus events
		setFocusable(true);

		// Draw a thick black border around the panel
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

		// Listen for mouse events
		addMouseListener(new MouseAdapter() {
			// Mouse down
			public void mousePressed(MouseEvent e) {
				// Snap the event's coordinates to the grid
				// The coordinates are also normalized
				int x = snapToGrid(e.getX(), wres) / wres;
				int y = snapToGrid(e.getY(), hres) / hres;

				// If we are drawing Vertices
				if (drawing == VERTICES) {
					// If we left clicked
					if (SwingUtilities.isLeftMouseButton(e)) {
						// Maximum of 26 vertices
						if (Vertex.getId() == 26) {
							return;
						}

						// If we already clicked on a vertex before, we'll need
						// to reset that one's color
						if (nearestV != null) {
							V.get(nearestV).setColor(Color.BLACK);
						}

						// Get the currently clicked vertex, if any
						nearestV = collidesWithVertex(x, y);
						if (nearestV == null) {
							// If we didn't click on an existing vertex, let's
							// add one
							V.add(new Vertex(x, y));
						}
						else {
							// Otherwise let's signal the error by changing the
							// color of the nearest Vertex
							V.get(nearestV).setColor(Color.RED);
						}
					}
					else if (SwingUtilities.isRightMouseButton(e)) {
						// If we right clicked, delete mode is activated

						// Lets find out which vertex was clicked, if any
						// The tolerance is set to 1, which means that the click
						// must be in a radius of 1 apart from the exact
						// coordinates of the vertex
						Integer idToDelete = collidesWithVertex(x, y, 1);

						// If a vertex was indeed clicked
						if (idToDelete != null) {
							// Lets get it
							Vertex toDelete = V.get(idToDelete);

							// Remove it from the list of vertices and add its
							// name to be reused
							V.remove(toDelete);
							Vertex.delete(toDelete);

							// TODO: Check this part for errors, also there
							// might be a faster way to just search for the
							// edges in the list of edges provided by the vertex
							// itself.
							// Make sure the vertex's lists are also updated

							// Delete all arcs passing through this vertex
							// for (int i = E.size() - 1; i >= 0; --i) {
							// Edge k = E.get(i);
							//
							// if (k.getV1() == toDelete
							// || k.getV2() == toDelete) {
							// E.remove(k);
							// }
							// }

							for (int i = 0; i < toDelete.getEdges().size(); ++i) {
								Edge k = toDelete.getEdges().get(i);

								Vertex v = k.getV1();
								if (v == toDelete)
									v = k.getV2();

								E.remove(k);
								v.getEdges().remove(toDelete);
							}
						}
					}
				}
				else if (drawing == EDGES) {
					// If we are drawing the Edges
					if (SwingUtilities.isLeftMouseButton(e)) {
						// If we left clicked, we need to check if the first
						// Vertex was clicked
						if (nearestV == null) { // First selection
							// If not, lets set the first one, if it was found
							nearestV = collidesWithVertex(x, y, 1);

							// And set its color, in the event we did click on
							// one
							if (nearestV != null) {
								V.get(nearestV).setColor(Color.BLUE);
							}
						}
						else {
							// Otherwise, lets remember the first one
							Vertex v1 = V.get(nearestV);
							// Get the current one
							nearestV = collidesWithVertex(x, y, 1);

							// If none was clicked, it meeans we clicked in the
							// blank
							if (nearestV == null) {
								// So we need to deselect the first one by
								// resetting its color
								v1.setColor(Color.BLACK);
								repaint();

								return;
							}

							// If we did in fact click on a vertex, we'll
							// highlight this one too
							Vertex v2 = V.get(nearestV);
							v2.setColor(Color.BLUE);
							repaint();

							// Now, we'll need to ask the user to provide a
							// weight for the Edge
							int weight = 0;
							while (weight <= 0) {
								try {
									weight = Integer
											.parseInt((String) JOptionPane
													.showInputDialog(
															null,
															"Enter the weight (>=0) of this edge:",
															"Graph Algorithms",
															JOptionPane.PLAIN_MESSAGE,
															null, null, ""));
								} catch (NumberFormatException ex) {
									// In case something happened, we'll set the
									// weight to 999 just to make sure the user
									// notices the error and corrects it
									weight = 999;
								}
							}

							// We have all the info now, lets create the edge
							Edge k = new Edge(v1, v2, weight);
							// And it it to the adjaceny list of both vertices
							v1.addEdge(k);
							v2.addEdge(k);

							// Now we'll check if the Edge was already added
							int index = E.indexOf(k);
							if (index == -1) {
								// If not, lets add it to our list
								E.add(k);
							}
							else {
								// Otherwise, just update its weight
								E.get(index).setWeight(weight);
							}

							// Then we'll reset the colors of both vertices
							v1.setColor(Color.BLACK);
							v2.setColor(Color.BLACK);

							// And deslect the last vertex
							nearestV = null;
						}
					}
					else if (SwingUtilities.isRightMouseButton(e)) {
						// Same thing, for right click, delete mode is activated
						if (nearestV == null) { // First selection
							nearestV = collidesWithVertex(x, y, 1);

							// Highlight when necessary
							if (nearestV != null) {
								V.get(nearestV).setColor(Color.RED);
							}
						}
						else {
							// Remember the first vertex
							Vertex v1 = V.get(nearestV);
							// Reset its color
							v1.setColor(Color.BLACK);

							// Get the 2nd one, if any
							nearestV = collidesWithVertex(x, y, 1);

							// If we clicked in the blank
							if (nearestV == null) {
								// Then deselect
								repaint();

								return;
							}

							// Now, lets get the second vertex
							Vertex v2 = V.get(nearestV);

							// And find the associated edge
							Edge k = new Edge(v1, v2, 0);
							int index = E.indexOf(k);

							// If we did find an edge between those two vertices
							if (index != -1) {
								// We'll remove it from the list
								E.remove(E.get(index));

								// And from each vertex's adjacency list
								v1.removeEdge(k);
								v2.removeEdge(k);
							}

							// And we shouldn't forget to deselect the last
							// vertex
							nearestV = null;
						}
					}
				}

				repaint();
			}
		});
	}

	/**
	 * 
	 * This method allows snapping coordinates to the maze grid
	 * 
	 * @param d A coordinate (could be an X, Y, Z ...)
	 * @param res The resolution for the coordinate direction (resX, resY,
	 * resZ...)
	 * @return The closest coordinate to snap to according to the provided
	 * resolution.
	 * 
	 */
	private int snapToGrid(int d, int res) {
		// Compute the first point that is farther than the current point (wrt
		// the origin)
		int snapDown = (int) (res * Math.round(d / res));
		// Compute the first point that is closer than the current point (wrt
		// the origin)
		int snapUp = snapDown + res;

		// Return the closest coordinate between the two
		if (d - snapDown < snapUp - d) {
			return snapDown;
		}
		else {
			return snapUp;
		}
	}

	/**
	 * 
	 * This method is responsible for returning the dimension to our JFrame so
	 * that the maze is fully drawn and not cropped
	 * 
	 * @return The width and height of our maze
	 * 
	 */
	public Dimension getPreferredSize() {
		return new Dimension(pWidth, pHeight);
	}

	/**
	 * This method actually prints the view every time a repaint is needed
	 */
	protected void paintComponent(Graphics h) {
		super.paintComponent(h);
		Graphics2D g = (Graphics2D) h;

		// Lets start with a tiny, dashed grey stroke for the grid
		g.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE,
				BasicStroke.JOIN_ROUND, 0, new float[] { 3 }, 0));
		g.setColor(Color.GRAY);

		// Paint the vertical lines
		for (int i = wres; i < pWidth; i += wres) {
			g.draw(new Line2D.Double(i, 0, i, pHeight));
		}
		// Paint the horizontal lines
		for (int i = hres; i < pHeight; i += hres) {
			g.draw(new Line2D.Double(0, i, pWidth, i));
		}

		// Draw edges
		g.setStroke(new BasicStroke(3));
		g.setFont(new Font("Verdana", Font.BOLD, 14));

		// For each edge
		for (Edge k : E) {
			int x = wres * (k.getV1().getX() + k.getV2().getX()) / 2;
			int y = hres * (k.getV1().getY() + k.getV2().getY()) / 2;

			// Set the color depending on its color
			g.setColor(k.getColor());

			// Draw its weight
			g.drawString("" + k.getWeight(), x, y);

			// And then draw the line linking both vertices it links
			g.drawLine(k.getV1().getX() * wres, k.getV1().getY() * hres, k
					.getV2().getX() * wres, k.getV2().getY() * hres);
		}

		// Draw vertices
		g.setFont(new Font("Verdana", Font.BOLD, 16));
		for (Vertex k : V) {
			// Set the color depending on each vertex
			g.setColor(k.getColor());

			// And draw it as a disk or a full circle
			g.fillOval((k.getX() - 1) * wres, (k.getY() - 1) * hres, 2 * wres,
					2 * hres);

			// Lastly, we'll need to write the name for each vertex in white
			g.setColor(Color.WHITE);
			g.drawString(String.valueOf(k.getName()), k.getX() * wres - wres
					/ 4, k.getY() * hres + hres / 4);
		}
	}

	/**
	 * 
	 * A JavaScript-like alert box using Swing's corresponding function
	 * 
	 * @param msg The message to display
	 * 
	 */
	protected void alert(String msg) {
		JOptionPane.showMessageDialog(this, msg);
	}

	/**
	 * 
	 * This method is responsible to map coordinates to a vertex, if it exists.
	 * The tolerance is set to 2.
	 * 
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 * @return The index of the corresponding Vertex in the list (or -1 in case
	 * of an error)
	 * 
	 */
	private Integer collidesWithVertex(int x, int y) {
		return collidesWithVertex(x, y, 2);
	}

	/**
	 * 
	 * This method is responsible to map coordinates to a vertex, if it exists.
	 * The tolerance is set to 2.
	 * 
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 * @param d The tolerance for both coordinates
	 * 
	 * @return The index of the corresponding Vertex in the list (or -1 in case
	 * of an error)
	 * 
	 */
	private Integer collidesWithVertex(int x, int y, int d) {
		for (int i = 0; i < V.size(); ++i) {
			Vertex k = V.get(i);
			if (Math.abs(k.getX() - x) <= d && Math.abs(k.getY() - y) <= d)
				return i;
		}

		return null;
	}

	/**
	 * 
	 * This method resets the panel by clearing all edges and vertices and
	 * deselecting everything
	 * 
	 */
	public void reset() {
		V.clear();
		E.clear();
		nearestV = null;
		Vertex.setId(0);

		repaint();
	}

	/**
	 * 
	 * This method clears the panel from any algorithm's solution
	 * 
	 */
	public void clear() {
		// Reset the vertices' data
		for (Vertex k : V) {
			k.setPrevious(null);
			k.setViaEdge(null);
			k.setDistance(0);
		}

		// Reset the edges' color
		for (Edge k : E) {
			k.setColor(Color.BLUE);
		}

		repaint();
	}

	/**
	 * 
	 * This method applies Dijkstra's algorithm on the graph to produce the
	 * shortest path from a source to a destination while highlighting all the
	 * edges of the path in green
	 * 
	 */
	public void dijkstra() {
		// First, we'll clear the panel from previous solutions
		clear();

		// We'll ask for both a source and a destination
		Vertex source = getVertex("Enter the source:");

		// If the user did not select one, we'll cancel
		if (source == null)
			return;

		Vertex destination = getVertex("Enter the destination:");

		// If the user did not select one, we'll cancel
		if (destination == null)
			return;

		// Now we need to set each vertex's distance to infinity
		for (Vertex v : V) {
			v.setDistance(Integer.MAX_VALUE);
		}

		// The source vertex will have a distance of 0
		source.setDistance(0);

		// To continue, we'll need a priority queue to keep track of the
		// vertices and sort them using their distance
		PriorityQueue<Vertex> Q = new PriorityQueue<>();
		for (Vertex v : V) {
			Q.add(v);
		}

		// While we still haven't visited every vertex
		while (!Q.isEmpty()) {
			// We'll take the one with the smallest distance
			Vertex u = Q.poll();

			// If it's our destination, we'll stop here since no more processing
			// is needed
			if (u == destination) {
				break;
			}

			// If the distance to reach this vertex is infinity, then this graph
			// is not highly connexe and we can't do anything any more since the
			// remaining vertices will also have infinity as their distance
			if (u.getDistance() == Integer.MAX_VALUE) {
				break;
			}

			// Otherwise, we'll need to check each edge departing from this
			// vertex
			for (Edge e : u.getEdges()) {
				// And get the vertex we can reach using each edge
				Vertex v = e.getV1();

				// We'll make sure we select the right vertex since the edge is
				// not oriented
				if (v == u)
					v = e.getV2();

				// We'll calculate the total alternate distance (using this
				// edge) to arrive to that vertex
				Integer alt = u.getDistance() + e.getWeight();

				// And if it is an improvement over the current distance to
				// reach that vertex
				if (alt < v.getDistance()) {
					// We'll mark it as the new distance for this edge
					v.setDistance(alt);
					// We'll also set the previous vertex as the one we came
					// from
					v.setPrevious(u);
					// We'll also set the edge we used to make the transfer
					v.setViaEdge(e);

					// Since the distance was modified, we'll need to reinsert
					// the vertex in order to update its position in the queue
					Q.remove(v);
					Q.add(v);
				}
			}
		}

		// Now, we'll need to figure out the path taken to arrive from the
		// source to the destination (if it exists)

		// We'll start from the destination
		Vertex current = destination;

		// And we'll go back from each vertex using the tracking information
		while (current.getPrevious() != null) {
			// We'll highlight the chosen edge
			current.getViaEdge().setColor(Color.GREEN);

			// And we'll go back one vertex
			current = current.getPrevious();
		}

		repaint();
	}

	/**
	 * 
	 * This method asks the user to select a vertex by writing its name while
	 * proving a message
	 * 
	 * @param message The message to display to the user in the input box
	 * 
	 * @return The vertex chosen by the user (if it exists) or null in any other
	 * case
	 * 
	 */
	private Vertex getVertex(String message) {
		// First we'll set the input as an empty string
		String s = "";
		// And the chosen vertex as null
		Vertex chosen = null;

		// And while we don't have a valid response
		while (chosen == null && s != null) {
			// We'll keep asking fror a vertex name using the message provided
			s = (String) JOptionPane.showInputDialog(null, message,
					"Graph Algorithms", JOptionPane.PLAIN_MESSAGE, null, null,
					"");

			// If the user canceled the dialog, we need to stop nagging
			if (s == null) {
				break;
			}

			// We definitely need a name with at least one letter
			if (s.length() == 0)
				continue;

			// Otherwise, we'll search for the name provided by the user to find
			// the correct vertex
			for (Vertex v : V) {
				// When we find it
				if (v.getName() == s.toUpperCase().charAt(0)) {
					// We'll set it as chosen
					chosen = v;
					break;
				}
			}
		}

		return chosen;
	}

	/**
	 * 
	 * This method applies Prim's algorithm on the graph to produce a minimal
	 * spanning tree and color it in green on the panel
	 * 
	 */
	public void prim() {
		// First we'll clear the panel from previous solutions
		clear();

		// We'll need a starting vertex for this algorithm
		Vertex source = getVertex("Enter the source:");

		// We'll also need a disjoint set to make sure we don't create cycles
		DisjointSet ds = new DisjointSet(V.size());

		// Finallyk, we'll need a priority queue to sort edges by their weight
		PriorityQueue<Edge> Q = new PriorityQueue<>();

		// First, we'll add the starting vertex
		// We'll mark vertices as visited by setting their distance to 1
		source.setDistance(1);

		// Then we'll add all edges departing from the source vertex
		for (Edge e : source.getEdges()) {
			Q.add(e);
		}

		// Then, while we still have edges waiting to be scanned
		while (!Q.isEmpty()) {
			// We'll take the least costy one
			Edge e = Q.poll();

			// We'll get both vertices
			Vertex v1 = e.getV1();
			Vertex v2 = e.getV2();

			// And their respective indexes in the array
			int u = V.indexOf(v1);
			int v = V.indexOf(v2);

			// We'll check to see if they are NOT connexe (to make sure that by
			// adding them, we wont be creating any cycles)
			if (ds.find(u) != ds.find(v)) {
				// If so, we'll join them in the disjoint set
				ds.union(u, v);

				// We'll highlight the edge we just added
				e.setColor(Color.GREEN);

				// Now we need to add the edges of the newly scanned vertex
				// The vertex can be either so we'll scan both to see which one
				// is not marked as completed

				// Is it V1 ?
				if (v1.getDistance() != 1) {
					// Mark this vertex completed and add his edges to the queue
					// only if they are not there yet
					v1.setDistance(1);
					for (Edge k : v1.getEdges()) {
						if (!Q.contains(k)) {
							Q.add(k);
						}
					}
				}
				else {
					// It's gotta be V2
					// Mark this vertex completed and add his edges to the queue
					// only if they are not there yet
					v2.setDistance(1);
					for (Edge k : v2.getEdges()) {
						if (!Q.contains(k)) {
							Q.add(k);
						}
					}
				}
			}
		}

		repaint();
	}

	/**
	 * 
	 * This method applies Kruskal's algorithm on the graph in order to generate
	 * a minimal spanning tree (shown in green on the panel)
	 * 
	 */
	public void kruskal() {
		// First we'll clear the panel from previous solutions
		clear();

		// We'll need a disjoint set in order to verify connexity and prevent
		// cycles from being created
		DisjointSet ds = new DisjointSet(V.size());

		// A priority queue is also needed in order to sort the edges by their
		// weight
		PriorityQueue<Edge> Q = new PriorityQueue<>();

		// First lets add all the edges to the priority queue, therefore sorting
		// them in ascending order
		for (Edge e : E) {
			Q.offer(e);
		}

		// Now we'll walk through all the edges
		while (!Q.isEmpty()) {
			Edge e = Q.poll();

			// Get the indexes of both vertices the edge links
			int u = V.indexOf(e.getV1());
			int v = V.indexOf(e.getV2());

			// Check to see if both vertices are NOT connexe (to make sure that
			// adding this edge won't create a cycle)
			if (ds.find(u) != ds.find(v)) {
				// If so, we'll join both in the disjoint set
				ds.union(u, v);

				// And highlight the edge we just added on the graph
				e.setColor(Color.GREEN);
			}
		}

		repaint();
	}

	/**
	 * 
	 * This method allows saving the graph into a file
	 * 
	 */
	public void save() {
		try {
			// Open a file chooser to get the save file
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setMultiSelectionEnabled(false);
			fc.setFileFilter(new SimpleFileFilter("sav", "Save File"));
			int result = fc.showSaveDialog(null);

			// If a file was chosen
			if (result == JFileChooser.APPROVE_OPTION) {
				FileOutputStream saveFile = new FileOutputStream(fc
						.getSelectedFile().getAbsoluteFile() + ".sav");

				// Create an ObjectOutputStream to put objects into save file.
				ObjectOutputStream save = new ObjectOutputStream(saveFile);

				// Now we do the save.
				save.writeObject(V);
				save.writeObject(E);

				// Close the file
				save.close();
				saveFile.close();
			}
		} catch (Exception e) {
			// Should anything happen, show an error and print the details to
			// the console
			alert("Save failed! Check the console for errors.");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * This method allows loading a graph previously saved into a file
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void load() {
		clear();
		reset();

		try {
			// Open a file chooser to choose the load file
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setMultiSelectionEnabled(false);
			fc.setFileFilter(new SimpleFileFilter("sav", "Save File"));
			int result = fc.showOpenDialog(null);

			// If a file was selected
			if (result == JFileChooser.APPROVE_OPTION) {
				FileInputStream loadFile = new FileInputStream(fc
						.getSelectedFile().getAbsoluteFile());

				// Create an ObjectInputStream to read objects from the load
				// file.
				ObjectInputStream load = new ObjectInputStream(loadFile);

				// Read the vertices
				V = (Vector<Vertex>) load.readObject();

				// Read the edges
				E = (Vector<Edge>) load.readObject();

				// Close the file
				load.close();
				loadFile.close();

				// Draw the newly loaded graph
				repaint();
			}
		} catch (Exception e) {
			// Should anything happen, show an error and print the details to
			// the console
			alert("Load failed! Check the console for errors.");
			e.printStackTrace();
		}
	}
}
