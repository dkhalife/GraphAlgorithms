package com.dkhalife.projects;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

/**
 * 
 * This class is the entry point for our Graph Algorithms application
 * 
 * @author Dany Khalife
 * @version 1.0
 * @since December, 2012
 * 
 */
public class GraphAlgorithms {
	// The list of buttons this application offers
	private static JButton btnDrawVertices, btnDrawEdges, btnClear,
			btnDijkstra, btnPrim, btnKruskal, btnReset, btnLoad, btnSave;

	// The drawing panel
	private static Panel p;

	/**
	 * 
	 * The main method creates and shows the GUI for this application
	 * 
	 * @param args No console arguments are taken in consideration for this
	 * project
	 * 
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	/**
	 * 
	 * This method creates and shows the GUI for this application
	 * 
	 */
	private static void createAndShowGUI() {
		// Create a window that will hold everything
		JFrame window = new JFrame("Graph Algorithms");

		// Show it and make it non-resizable
		window.setVisible(true);
		window.setResizable(false);

		// Set the default close operation
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add the drawing panel
		JPanel panel = new JPanel(new BorderLayout());
		window.add(panel);

		// Create a vertical box, so that items are stacked on top of each other
		Box vb = Box.createVerticalBox();
		panel.add(vb);

		// Create a first line
		Box hb = Box.createHorizontalBox();
		vb.add(hb);

		// Write the title
		JLabel title = new JLabel("Graph Algorithms");
		// With font
		title.setFont(new Font("Verdana", Font.BOLD, 20));
		// Set margins
		setMargin(title, 20, 0, 30, 0);

		// Add the title centered
		hb.add(Box.createHorizontalGlue());
		hb.add(title);
		hb.add(Box.createHorizontalGlue());

		// Create a second line that will hold the buttons
		hb = Box.createHorizontalBox();
		vb.add(hb);

		// Make sure the buttons will be centered
		hb.add(Box.createHorizontalGlue());

		// Add the Vertices button
		btnDrawVertices = new JButton("Vertices");
		btnDrawVertices.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawVertices();
			}
		});
		btnDrawVertices.setEnabled(false);
		hb.add(btnDrawVertices);

		// Spacing between the buttons
		hb.add(Box.createHorizontalStrut(10));

		// Add the Edges button
		btnDrawEdges = new JButton("Edges");
		btnDrawEdges.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawEdges();
			}
		});
		hb.add(btnDrawEdges);

		// Spacing between the buttons
		hb.add(Box.createHorizontalStrut(10));

		// Add the Clear button
		btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		hb.add(btnClear);

		// Spacing between the buttons
		hb.add(Box.createHorizontalStrut(50));

		// Add the Dijkstra button
		btnDijkstra = new JButton("Dijkstra");
		btnDijkstra.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p.dijkstra();
			}
		});
		hb.add(btnDijkstra);

		// Spacing between the buttons
		hb.add(Box.createHorizontalStrut(10));

		// Add the Prim button
		btnPrim = new JButton("Prim");
		btnPrim.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p.prim();
			}
		});
		hb.add(btnPrim);

		// Spacing between the buttons
		hb.add(Box.createHorizontalStrut(10));

		// Add the Kruskal button
		btnKruskal = new JButton("Kruskal");
		btnKruskal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p.kruskal();
			}
		});
		hb.add(btnKruskal);

		// Spacing between the buttons
		hb.add(Box.createHorizontalStrut(10));

		// Add the Reset button
		btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});
		hb.add(btnReset);

		// Spacing between buttons
		hb.add(Box.createHorizontalStrut(50));

		// Add the Import button
		btnLoad = new JButton("Import");
		btnLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p.load();
			}
		});
		hb.add(btnLoad);

		// Spacing between the buttons
		hb.add(Box.createHorizontalStrut(10));

		// Add the Export button
		btnSave = new JButton("Export");
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p.save();
			}
		});
		hb.add(btnSave);

		// Make sure we have the buttons centered
		hb.add(Box.createHorizontalGlue());

		// Add vertical spacing
		vb.add(Box.createVerticalStrut(10));

		// Create a 50x30 panel
		p = new Panel(50, 30);

		// Add a new line
		hb = Box.createHorizontalBox();
		vb.add(hb);

		// Add the panel with a spacing of 10 on each side
		hb.add(Box.createHorizontalStrut(10));
		hb.add(p);
		hb.add(Box.createHorizontalStrut(10));

		// Add vertical spacing
		vb.add(Box.createVerticalStrut(10));

		// Pack the window so that it fits it contents
		window.pack();
	}

	/**
	 * 
	 * This method adds artifical margins to a JComponent
	 * 
	 * @param c The component in question
	 * @param top The top margin
	 * @param right The right margin
	 * @param bottom The bottom margin
	 * @param left The left margin
	 * 
	 */
	private static void setMargin(JComponent c, int top, int right, int bottom,
			int left) {
		// Get the current border
		Border current = c.getBorder();
		
		// Create another border with the specified margins
		Border margin = new EmptyBorder(top, left, bottom, right);

		// If we have no current border, lets set it
		if (current == null) {
			c.setBorder(margin);
		}
		else {
			// Otherwise, we'll just merge both
			c.setBorder(new CompoundBorder(margin, current));
		}
	}

	/**
	 * 
	 * This method is responsible of changing the drawing mode to VERTICES
	 *  
	 */
	public static void drawVertices() {
		p.setDrawing(Panel.VERTICES);
		btnDrawVertices.setEnabled(false);
		btnDrawEdges.setEnabled(true);
	}

	/**
	 * 
	 * This method is responsible of changing the drawing mode to EDGES
	 * 
	 */
	public static void drawEdges() {
		p.setDrawing(Panel.EDGES);
		btnDrawVertices.setEnabled(true);
		btnDrawEdges.setEnabled(false);
	}

	/**
	 * 
	 * This method resets the application by resetting the drawing mode and the panel
	 */
	public static void reset() {
		btnDrawVertices.setEnabled(false);
		btnDrawEdges.setEnabled(true);
		p.setDrawing(Panel.VERTICES);

		p.reset();
	}

	/**
	 * 
	 * This method clears the panel by removing the algorithm's solutions
	 * 
	 */
	public static void clear() {
		p.clear();
	}
}