import java.util.Arrays;

public class Point {

  public double dual;
  public double[] coordinates;
  public int id, matchId;
  private int dimension;

  Point(double[] coordinates, int id) {
    this.coordinates = coordinates;
    this.dimension = coordinates.length;
    this.id = id;
    reset();
  }

  public void reset() {
    this.dual = 0;
    this.matchId = -1;
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
    return new Point(copyCoordinates, -1);
  }

  @Override
  public String toString() {
    return String.format(
      "Coords: " + Arrays.toString(coordinates) +
      " Dual Weight: " +
      this.dual +
      " Id: " +
      this.id +
      " Match Id: " +
      this.matchId
    );
  }
}
