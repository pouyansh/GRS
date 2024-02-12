import java.util.Arrays;

public class Boundary {

  public Point lowPoint, highPoint, originLowPoint, originHighPoint;
  private Boundary adjusted;
  private int dimension;
  public Point[] A, B;

  Boundary(
    Point lowPoint,
    Point highPoint,
    Point originLowPoint,
    Point originHighPoint,
    Point[] A,
    Point[] B,
    boolean adjust
  ) {
    this.lowPoint = lowPoint;
    this.highPoint = highPoint;
    this.originLowPoint = originLowPoint;
    this.originHighPoint = originHighPoint;
    this.A = A;
    this.B = B;

    this.dimension = lowPoint.coordinates.length;
    if (adjust){
      this.adjusted = new Boundary(lowPoint.copy(), highPoint.copy(), originLowPoint, originHighPoint, null, null, false);
      for (int i = 0; i < dimension; i++) {
        if (lowPoint.coordinates[i] == originLowPoint.coordinates[i]) {
          this.adjusted.lowPoint.coordinates[i] = -10;
        }
        if(highPoint.coordinates[i] == originHighPoint.coordinates[i]) {
          this.adjusted.highPoint.coordinates[i] = 10;
        }
      }
    } else {
      this.adjusted = this;
    }
  }

  public void reset() {
    for (Point a: A) {
      a.reset();
    }
    for (Point b: B) {
      b.reset();
    }
  }

  public boolean checkIfThin(double threshold) {
    for (int i = 0; i < this.dimension; i++) {
      if (highPoint.coordinates[i] - lowPoint.coordinates[i] < threshold) 
        return true;
    }
    return false;
  }

  public double getMinDistanceToBoundary(Point bPoint, int p) {
    double minDist = Double.MAX_VALUE;
    for (int i = 0; i < this.dimension; i++) {
      minDist = Math.min(minDist, Math.min(
        adjusted.highPoint.coordinates[i] - bPoint.coordinates[i],
        bPoint.coordinates[i] - adjusted.lowPoint.coordinates[i])
      );
    }
    return Math.pow(minDist, p);
  }

  public Boundary[] partition() {
    
    double[] middleCoords = new double[dimension];
    for (int i = 0; i < dimension; i++) {
      middleCoords[i] = (this.lowPoint.coordinates[i] + this.highPoint.coordinates[i]) / 2;
    }
    int childnum = (int)Math.pow(dimension, 2);

    Boundary[] children = new Boundary[childnum];
    for (int i = 0; i < childnum; i++) {
      double[] lowCoords = new double[dimension];
      double[] highCoords = new double[dimension];
      int index = i;
      for (int k = 0; k < dimension; k++) {
        if (index % 2 == 0) {
          lowCoords[k] = this.lowPoint.coordinates[k];
          highCoords[k] = middleCoords[k];
        } else {
          lowCoords[k] = middleCoords[k];
          highCoords[k] = this.highPoint.coordinates[k];
        }
        index /= 2;
      }
      children[i] = new Boundary(new Point(lowCoords, -1), new Point(highCoords, -1), originLowPoint, originHighPoint, null, null, true);
    }

    int[] sizesA = findSizes(A, middleCoords);
    int[] sizesB = findSizes(B, middleCoords);

    for (int i = 0; i < childnum; i++) {
      children[i].A = new Point[sizesA[i]];
      children[i].B = new Point[sizesB[i]];
    }
    
    assignPoints(children, middleCoords);
    return children;
  }

  private int[] findSizes(Point[] points, double[] middleCoords) {
    int childnum = (int)Math.pow(dimension, 2);
    // System.out.println("middle points:" + Arrays.toString(middleCoords));
    int[] sizes = new int[childnum];
    for (int i = 0; i < points.length; i++) {
      Point p = points[i];
      int index = 0;
      for (int d = dimension-1; d >= 0; d--) {
        index *= 2;
        if (p.coordinates[d] >= middleCoords[d]) {
          index++;
        }
      }
      // System.out.println("point " + Arrays.toString(p.coordinates) + " " + index);
      sizes[index]++;
    }
    return sizes;
  }

  private void assignPoints(Boundary[] children, double[] middleCoords) {
    int childnum = (int)Math.pow(dimension, 2);
    int[] counterA = new int[childnum];
    int[] counterB = new int[childnum];

    for (Point a: A) {
      int index = 0;
      for (int d = dimension-1; d >= 0; d--) {
        index *= 2;
        if (a.coordinates[d] >= middleCoords[d]) {
          index++;
        }
      }
      children[index].A[counterA[index]++] = a;
    }
    for (Point b: B) {
      int index = 0;
      for (int d = dimension-1; d >= 0; d--) {
        index *= 2;
        if (b.coordinates[d] >= middleCoords[d]) {
          index++;
        }
      }
      children[index].B[counterB[index]++] = b;
    }
  }

  @Override
  public String toString() {
    return String.format(
      "Low Point: " +
      Arrays.toString(this.lowPoint.coordinates) +
      " High Point: " +
      Arrays.toString(this.highPoint.coordinates)
    );
  }
}
