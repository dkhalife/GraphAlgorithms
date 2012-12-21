GraphAlgorithms
===============

This Java Swing application is an easy to use application relying on basic point and click interactions.

Instructions:
-------------

By default, the application starts in Vertex Drawing mode, which allows you to add or delete vertices. 

1. Playing with vertices:
-------------------------

To add a vertex, simply point and click on the grid. A letter will be automatically assigned to the vertex you just created.

For now, a maximum of 26 vertices can exist at the same time and vertices cannot be very close to each other for display reasons.

If you try to add a vertex very close to another one, the second will be highlighted in red to show you why your vertex was not added.

To delete a vertex, simply right click on it and it will be gone. (The vertex's letter will be reused).

2. Playing with edges:
----------------------

After adding vertices to your graph, usually the next step is to add edges to it.

To start, we'll need to make sure to switch to the Edge Drawing mode by clicking on the 'Edges' button.

Now, to add an edge between two vertices, you'll have to click on them one after the other.

When the first vertex is selected it will be highlighted and once both are selected, the edge will be created and you'll be asked to enter a weight for this edge.

For simplicity, the application allows only a single edge between two distinct vertices. 

So, in order to modify an edge's weight all you have to do is reinsert an edge between the same vertices.

To delete an edge, the operation is a bit similar, just right click on both vertices it links and it will be gone.

3. Resetting vs Clearing:
-------------------------

In order to reset the grid, just click on 'Reset' and all vertices and edges will be gone.

If you only want to remove the solution an algorithm produced, simply click on 'Clear'.

4. Import - Export:
-------------------

It is common to use this application for demonstration reasons, so i have included a import-export mechanism.

This will allow you to save your vertices and edges in a .sav file and reuse it later.

Note that this doesn't save the result of an algorithm to keep the file size small (as it can be regenerated once the file is loaded).

5. Graph Algorithms:
--------------------

This application allows you to exectute 3 popular graph algoritms on your graph: Dijkstra's short path algorithm, Prim's and Kruskal's respective minimal spanning tree algorithms.

If you would like to see another one added to the application just let me know.