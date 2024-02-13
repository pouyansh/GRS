public class DivideAndConquerHungarianAlgorithm {

  private int N, matchingCardinality;
  private double[] distance;
  private boolean[] visited;
  private double matchingCost;
  private Boundary origin;
  static final double slackThreshold = Math.pow(10, -30);
  static final double widthThreshold = 0.00000001;
  static final double INFINITY = Double.MAX_VALUE;
  private int[] matching, mappingB, parent;
  public long operationsNum;
  public int hungarianNum;
  public int lastLevelHungrians;

  /**
   * @param A - List of points in A
   * @param B - List of points in B
   */
  public DivideAndConquerHungarianAlgorithm(Boundary b) {
    N = b.A.length;
    this.operationsNum = 0;
    this.hungarianNum = 0;
    this.lastLevelHungrians = 0;
    this.origin = b;

    // Clear the stale values
    for (int i = 0; i < N; i++) {
      this.origin.A[i].matchId = -1;
      this.origin.A[i].dual = 0;
      this.origin.B[i].matchId = -1;
      this.origin.B[i].dual = 0;
    }
  }

  /**
   * @param p1
   * @param p2
   * @return - Distance between the two points
   */
  private double getDistance(Point p1, Point p2, Integer p) {
    // return Math.pow(
    //   ((p1.x - p2.x) * (p1.x - p2.x)) +
    //   ((p1.y - p2.y) * (p1.y - p2.y)) +
    //   ((p1.z - p2.z) * (p1.z - p2.z))
    // , p/2);
    return p1.getDistance(p2, p);
  }

  /**
   * @return - Matching cost
   */
  public double getMatchingCost() {
    return this.matchingCost;
  }

  /**
   * @return - Matching cardinality
   */
  public int getMatchingCardinality() {
    return this.matchingCardinality;
  }

  /**
   * @return - Min Cost Matching
   */
  public int[] getMatching() {
    return this.matching;
  }

  /**
   * Gets the matching cardinality and cost
   */
  private void getMatchingCardinalityAndCost(Boundary b, int p) {
    matchingCardinality = 0;
    matchingCost = 0;
    matching = new int[N];
    for (int i = 0; i < N; i++) {
      if (b.A[i].matchId != -1) {
        matchingCardinality++;
        matchingCost += getDistance(b.A[i], b.B[b.A[i].matchId], p);
        matching[i] = b.A[i].matchId;
      }
    }
  }

  /**
   * @param n - Number of points under consideration
   * @return - Index of the point that is not yet visited and has the minimum distance
   */
  private int getMinDistanceNode(int n) {
    double minDist = INFINITY;
    int minIdx = -1;
    for (int i = 0; i < n; i++) {
      if (!visited[i] && distance[i] < minDist) {
        minDist = distance[i];
        minIdx = i;
      }
    }
    return minIdx;
  }

  private void checkBoundaryDistance(Point bPoint, int u, Boundary b, int p) {
    double slack = b.getMinDistanceToBoundary(bPoint, p) - bPoint.dual;
    if (Math.abs(slack) <= slackThreshold) {
      slack = 0;
    }
    if (distance[0] > distance[u] + slack) {
      distance[0] = distance[u] + slack;
      parent[0] = u;
    }
  }

  private void performNormalCall(Point bPoint, Boundary b, int u, int m, int p) {
    for (int v = 1; v < m + 1; v++) {
      if (bPoint.matchId != b.A[v - 1].id) {
        double slack = b.A[v - 1].getDistance(bPoint, p) - b.A[v - 1].dual - bPoint.dual;
        if (Math.abs(slack) <= slackThreshold) {
          slack = 0;
        }
        if (distance[v] > distance[u] + slack) {
          distance[v] = distance[u] + slack;
          parent[v] = u;
        }
      }
    }
  }

  /**
   * Augments the matching and updates the dual weights
   * @param A - List of points in set A
   * @param B - List of points in set B
   * @param b - Boundary
   */
  void hungarianSearch(
    Boundary b,
    int idx, 
    int pp,
    int level
  ) {
    int m = b.A.length;
    int k = m + b.B.length + 1;

    // Initialize the distance and visited arrays
    for (int i = 0; i < k; i++) {
      visited[i] = false;
      distance[i] = 10;
      parent[i] = -1;
    }

    this.operationsNum += b.A.length + b.B.length;
    this.hungarianNum += 1;
    if (level == 0) lastLevelHungrians++;

    // Distance from the source to itself is 0
    distance[m + idx + 1] = 0.0;
    double lMin = INFINITY;
    int freeMinDistanceIdxInA = -1;

    checkBoundaryDistance(b.B[idx], m + idx + 1, b, pp);
    
    performNormalCall(b.B[idx], b, m + idx + 1, m, pp);

    // Conduct Dijsktra's until a free point in set A or a boundary point is found
    while (true) {
      int u = getMinDistanceNode(m + 1);

      // Stop as soon as a free point in A or a boundary point is found
      if (u == 0 || b.A[u - 1].matchId == -1) {
        freeMinDistanceIdxInA = u;
        lMin = distance[u];
        break;
      }


      // Mark u as visited
      visited[u] = true;

      // Update the distances of the neighbours of u
      // u is a matched point in A and will have only one neighbor
      int w = mappingB[b.A[u - 1].matchId];
      distance[w] = distance[u];
      parent[w] = u;
      u = w;

      // u is now a point of B
      // All the boundaries and unmatched points in A can be reached from u
      checkBoundaryDistance(b.B[u - m - 1], u, b, pp);
      performNormalCall(b.B[u - m - 1], b, u, m, pp);
    }
    int pathArraySize = 0;
    int freeMinDistanceIdxInACopy = freeMinDistanceIdxInA;
    while (freeMinDistanceIdxInACopy > -1) {
      pathArraySize++;
      freeMinDistanceIdxInACopy = parent[freeMinDistanceIdxInACopy];
    }
    int[] path = new int[pathArraySize];
    int index = 0;
    while (freeMinDistanceIdxInA > -1) {
      path[index++] = freeMinDistanceIdxInA;
      freeMinDistanceIdxInA = parent[freeMinDistanceIdxInA];
    }

    // Update the matching (Augmenting the path)
    for (int i = 0; i < pathArraySize - 1; i += 2) {
      if (path[i] == 0) {
        b.B[path[i + 1] - m - 1].matchId = -2;
      } else {
        b.A[path[i] - 1].matchId = b.B[path[i + 1] - m - 1].id;
        b.B[path[i + 1] - m - 1].matchId = b.A[path[i] - 1].id;
      }
    }

    // Update the dual weights
    for (int i = 1; i < k; i++) {
      if (distance[i] < lMin) {
        if (i >= m + 1) {
          b.B[i - m - 1].dual = b.B[i - m - 1].dual + (lMin - distance[i]); // dual weight update of points in set B
        } else {
          b.A[i - 1].dual = b.A[i - 1].dual - (lMin - distance[i]); // dual weight update of points in set A
        }
      }
    }
  }

  /**
   * Solver function
   * @param A - List of points in set A
   * @param B - List of points in set B
   * @param b - Boundary
   */
  void solverHelper(Boundary b, int pp, int level) {

    // If no points of set B are present or both left and right or both top and bottom boundaries are the same, do nothing
    if (b.B.length == 0) {
      return;
    }

    // If two opposite boundaries coincide, do nothing
    if (b.checkIfThin(widthThreshold)) {
      return;
    }

    // If no points of set A are present, update the dual weights of all points in set B to be the shortest distance to the boundary
    if (b.A.length == 0) {
      for (int i = 0; i < b.B.length; i++) {
        b.B[i].dual = b.getMinDistanceToBoundary(b.B[i], pp);
      }
      return;
    }

    // If only one point of set B is present, match it to the nearest point or the boundary
    if (b.B.length == 1) {
      double minDist = b.getMinDistanceToBoundary(b.B[0], pp);

      int matchedPointIdx = -1;
      for (int i = 0; i < b.A.length; i++) {
        double temp = getDistance(b.A[i], b.B[0], pp);
        if (temp <= minDist) {
          minDist = temp;
          matchedPointIdx = i;
        }
      }
      if (matchedPointIdx != -1) {
        b.B[0].matchId = b.A[matchedPointIdx].id;
        b.A[matchedPointIdx].matchId = b.B[0].id;
      }
      b.B[0].dual = minDist;
      return;
    }

    // Else split the box into 4 smaller boxes and recursively solve for the smaller subproblems
    // Divide Step
    Boundary[] children = b.partition();

    // Solve the 4 subproblems independently
    for (int i = 0; i < children.length; i++) {
      solverHelper(children[i], pp, level + 1);
    }

    for (int i = 0; i < b.B.length; i++) {
      mappingB[b.B[i].id] = i + b.A.length + 1;
    }

    // Perform hungarian searches for the points matched to the boundaries
    // Conquer Step
    for (int i = 0; i < b.B.length; i++) {
      if (b.B[i].matchId >= 0) {
        continue;
      }

      // Match to the boundary if possible
      double minDist = b.getMinDistanceToBoundary(b.B[i], pp);
      if (Math.abs(minDist - b.B[i].dual) <= slackThreshold) {
        b.B[i].matchId = -2;
        continue;
      }
      hungarianSearch(b, i, pp, level);
    }
  }

  /**
   * @param b - Boundary
   * @return - Minimum cost for exact euclidean bipartite matching of points in A and B
   */
  public void solver(int p) {
    distance = new double[2 * N + 1];
    parent = new int[2 * N + 1];
    visited = new boolean[2 * N + 1];
    mappingB = new int[N];
    solverHelper(origin, p, 0);
    // getMatchingCardinalityAndCost(origin, p);
  }
}
