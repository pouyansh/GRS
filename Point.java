public class Point {

  public double x, y, z, dual;
  public int id, matchId;
  private double[] distances;

  Point(double x, double y, int id, int N) {
    this.x = x;
    this.y = y;
    this.z = 0;
    this.id = id;
    this.dual = 0.0;
    this.matchId = -1;
    // this.distances = new double[N];
    // for (int i = 0; i < N; i++) {
    //   this.distances[i] = -1;
    // }
  }

  Point(double x, double y, double z, int id, int N) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.id = id;
    this.dual = 0.0;
    this.matchId = -1;
    // this.distances = new double[N];
    // for (int i = 0; i < N; i++) {
    //   this.distances[i] = -1;
    // }
  }

  // double getDistance(Point other, int p) {
  //   if(this.distances[other.id] == -1) {
  //     this.distances[other.id] = Math.pow(
  //         ((this.x - other.x) * (this.x - other.x)) +
  //         ((this.y - other.y) * (this.y - other.y)) +
  //         ((this.z - other.z) * (this.z - other.z))
  //       , p/2);
  //     // System.out.println(this.distances[other.id]);

  //   }
  //   return this.distances[other.id];
  // }

  @Override
  public String toString() {
    return String.format(
      "X: " +
      this.x +
      " Y: " +
      this.y +
      " Z: " +
      this.z +
      " Dual Weight: " +
      this.dual +
      " Id: " +
      this.id +
      " Match Id: " +
      this.matchId
    );
  }
}
