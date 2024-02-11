public class Point {

  public double x, y, z, dual;
  public double[] coordinates;
  public int id, matchId;
  private int dimension;

  Point(double[] coordinates) {
    this.coordinates = coordinates;
    this.dimension = coordinates.length;
  }

  double getDistance(Point other, int p) {
    double distance = 0;
    for (int i = 0; i < this.dimension; i++) {
      distance += Math.pow(this.coordinates[i] - other.coordinates[i], 2);
    }
    return Math.pow(distance, p/2);
  }

  public Point copy() {
    double[] copyCoordinates = new double[this.dimension];
    System.arraycopy(this.coordinates, 0, copyCoordinates, 0, this.dimension);
    return new Point(copyCoordinates);
  }

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
