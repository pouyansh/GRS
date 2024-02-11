public class Boundary {

  public Point lowPoint, highPoint, originLowPoint, originHighPoint;
  private Boundary adjusted;
  private int dimension;

  Boundary(
    Point lowPoint,
    Point highPoint,
    Point originLowPoint,
    Point originHighPoint,
    boolean adjust
  ) {
    this.lowPoint = lowPoint;
    this.highPoint = highPoint;
    this.originLowPoint = originLowPoint;
    this.originHighPoint = originHighPoint;

    this.dimension = lowPoint.coordinates.length;
    if (adjust){
      this.adjusted = new Boundary(lowPoint.copy(), highPoint.copy(), false);
      for (int i = 0; i < dimension; i++) {
        if (lowPoint.coordinates[i] == originLowPoint.coordinates[i]) {
          this.adjusted.lowPoint[i] = -10;
        }
        if(highPoint.coordinates[i] == originHighPoint.coordinates[i]) {
          this.adjusted.highPoint[i] = 10;
        }
      }
    } else {
      this.adjusted = this;
    }
  }

  public boolean checkIfThin(double threshold) {
    for (int i = 0; i < this.dimension; i++) {
      if (highPoint.coordinates[i] - lowPoint.coordinates[i] < threshold) 
        return true;
    }
    return false;
  }

  private double getMinDistanceToBoundary(Point bPoint, int p) {
    double minDist = Double.MAX_VALUE;
    for (int i = 0; i < this.dimension; i++) {
      minDist = Math.min(minDist, Math.min(
        adjusted.highPoint.coordinates[i] - bPoint.coordinates[i],
        bPoint.coordinates[i] - adjusted.lowPoint.coordinates[i])
      );
    }
    return Math.pow(minDist, p);
  }

  @Override
  public String toString() {
    return String.format(
      "Low Point: " +
      this.lowPoint.toString() +
      " Top Point: " +
      this.topPoint.toString()
    );
  }
}
