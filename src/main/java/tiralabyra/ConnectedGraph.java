package tiralabyra;

public class ConnectedGraph {

  /**
   * Edges (node-node neighborhoods) of this graph.
   */
  private Edge[][] edges;

  /**
   * Which node is the entry point.
   */
  private int entryNodeIndex = -1;

  /**
   * Maximum number of neighbors a node can have.
   */
  private final int numberOfNeighborhoods = 8;

  /**
   * Which node is the exit point.
   */
  private int exitNodeIndex = -1;

  /**
   * Build a connected graph based on an input image, with all neighborhoods
   * and input/exit nodes in place.
   * @param image Input image with correctly color-labeled walls and entry/exit
   * points.
   */
  public ConnectedGraph(final Image image) {
    edges = new Edge[image.getNumberOfPixels()][numberOfNeighborhoods];

    // Direction vectors, for clarity.
    final Point dE  = new Point(1,  0);
    final Point dSE = new Point(1,  1);
    final Point dS  = new Point(0,  1);
    final Point dSW = new Point(-1, 1);
    final Point dW  = new Point(-1, 0);

    // Clarify magic numbers
    final int dirNumE  = 1;
    final int dirNumSE = 2;
    final int dirNumS  = 3;
    final int dirNumSW = 4;

    // Assign a neighborhood to all non-wall pixels.
    for (Point point : image.getPixelPositions()) {
      if (image.isTraversableAt(point)) {
        // Non-wall pixel to the East is always traversable.
        if (image.isTraversableAt(Point.add(point, dE))) {
          addEdge(image.getIndexForPixel(point),
                  image.getIndexForPixel(Point.add(point, dE)),
                  dirNumE, 1.0);
        }

        // Non-wall pixel to the South-East is traversable unless there's a
        // corner in between.
        if (image.isTraversableAt(Point.add(point, dSE))
            && image.isTraversableAt(Point.add(point, dS))
            && image.isTraversableAt(Point.add(point, dE))) {
          addEdge(image.getIndexForPixel(point),
                  image.getIndexForPixel(Point.add(point, dSE)),
                  dirNumSE, Math.sqrt(2.0));
        }

        // Non-wall pixel to the South is always traversable.
        if (image.isTraversableAt(Point.add(point, dS))) {
          addEdge(image.getIndexForPixel(point),
                  image.getIndexForPixel(Point.add(point, dS)),
                  dirNumS, 1.0);
        }

        // Non-wall pixel to the South-West is traversable unless there's a
        // corner in between.
        if (image.isTraversableAt(Point.add(point, dSW))
            && image.isTraversableAt(Point.add(point, dW))
            && image.isTraversableAt(Point.add(point, dE))) {
          addEdge(image.getIndexForPixel(point),
                  image.getIndexForPixel(Point.add(point, dSW)),
                  dirNumSW, Math.sqrt(2.0));
        }
      }

      // Assign entry and exit point.
      if (image.hasEntryPointAt(point)) {
        entryNodeIndex = image.getIndexForPixel(point);
      }
      if (image.hasExitPointAt(point)) {
        exitNodeIndex = image.getIndexForPixel(point);
      }
    }

    if (entryNodeIndex == -1) {
      System.out.println("Labyrinth has no entry point. Please color one "
                         + "pixel as #FF0000.");
      return;
    }
    if (exitNodeIndex == -1) {
      System.out.println("Labyrinth has no exit point. Please color one "
                         + "pixel as #00FF00.");
      return;
    }
  }

  /**
   * Add a bidirectional neighborhood for two nodes.
   * @param nodeIndex     Index of the originating node in the graph.
   * @param neighborIndex Index of the neighbor node in the graph.
   * @param direction     Direction of neighborhood; used as an inner index.
   * @param weight        Edge weight (cost).
   */
  public void addEdge(final int nodeIndex, final int neighborIndex,
                      final int direction, final double weight) {

    if (nodeIndex >= 0 && nodeIndex < edges.length
        && neighborIndex >= 0 && neighborIndex < edges.length
        && direction >= 0 && direction < numberOfNeighborhoods) {
      edges[nodeIndex][direction] =
          new Edge(nodeIndex, neighborIndex, weight);

      // Set the opposite direction as well.
      int otherDirection = (direction + numberOfNeighborhoods / 2)
                           % numberOfNeighborhoods;
      edges[nodeIndex][otherDirection] =
          new Edge(nodeIndex, neighborIndex, weight);
    }
  }

  /**
   * @return Index of the entry node.
   */
  public int getEntryNodeIndex() {
    return entryNodeIndex;
  }

  /**
   * @return Index of the exit node.
   */
  public int getExitNodeIndex() {
    return exitNodeIndex;
  }

  /**
   * @param nodeIndex Index of the requested origin node.
   * @return An array of edges.
   */
  public Edge[] getEdgesFrom(final int nodeIndex) {
    return edges[nodeIndex];
  }
}
