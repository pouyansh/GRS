import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Runner {

  static int N, pythonMatchingCardinality;
  static long startTime, endTime, totalTime, pythonMatchingCalculationTime;
  static int[] pythonMatching;
  static double totalCost, pythonMatchingCost;
  static Point[] A, B;
  static HungarianAlgorithm ha;
  static DivideAndConquerHungarianAlgorithm dacha;
  static double slackThreshold = 0.0000001;
  static String path1, path2;
  static long[] operationNums;
  static int[] hungarianNums;
  static double[] times;
  static int[] lastLevelHungrians;
  static int dimension;

  /**
   * Read the two datasets
   */
  private static void initializeDatasets() {
    A = new Point[N];
    B = new Point[N];
    String[] arr;
    try {
      Scanner scanner = new Scanner(new File(path1));

      for (int i = 0; i < N; i++) {
        arr = scanner.nextLine().split(" ");
        double[] coords = {Double.parseDouble(arr[0]),
          Double.parseDouble(arr[1])};
        A[i] =
          new Point(coords, i);
      }
      scanner.close();

      scanner = new Scanner(new File(path2));
      for (int i = 0; i < N; i++) {
        arr = scanner.nextLine().split(" ");
        double[] coords = {Double.parseDouble(arr[0]), Double.parseDouble(arr[1])};
        B[i] =
          new Point(coords, i);
      }
      scanner.close();

    } catch (FileNotFoundException e) {
      System.out.println("File not found");
      e.printStackTrace();
    }
  }


  private static void generateDatasets() {
    Random rand = new Random();
    A = new Point[N];
    B = new Point[N];
    String[] arr;
    for (int i = 0; i < N; i++) {
      double[] coords = new double[dimension];
      for (int j = 0; j < dimension; j++) {
        coords[j] = rand.nextDouble();
      }
      A[i] =
        new Point(coords, i);
    }

    for (int i = 0; i < N; i++) {
      double[] coords = new double[dimension];
      for (int j = 0; j < dimension; j++) {
        coords[j] = rand.nextDouble();
      }
      B[i] =
        new Point(coords, i);
    }

  }

  /**
   *
   * @param p1
   * @param p2
   * @return - Distance between the two points
   */
  private static double getDistance(Point p1, Point p2, int p) {
    return p1.getDistance(p2, p);
  }

  /**
   * Validates the dual weights
   */
  private static boolean validateDualWeights(int p) {
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        double slack = getDistance(A[i], B[j], p) - A[i].dual - B[j].dual;
        if (slack < -1 * slackThreshold) {
          System.out.println("Slack: " + slack + " is negative");
          return false;
        }
        if (A[i].matchId == j && slack > slackThreshold) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Runs the Hungarian algorithm solver
   */
  private static void runHungarianSolver(Integer p) {
    System.out.println(
      "--------------------------Hungarian Algorithm Solver---------------------------"
    );
    ha = new HungarianAlgorithm(A, B);
    startTime = System.currentTimeMillis();
    ha.solver(p);
    endTime = System.currentTimeMillis();
    // if (!validateDualWeights(p)) {
    //   System.out.println("Infeasible dual weights");
    //   System.exit(0);
    // }
    // System.out.println("Feasible dual weights");
    System.out.printf("Matching Cost: %.4f \n", ha.getMatchingCost());
    System.out.println("Time taken: " + (endTime - startTime) + " ms");
    System.out.println(
      "-------------------------------------------------------------------------------"
    );
  }

  /**
   * Runs the Divide and Conquer Hungarian algorithm solver
   */
  private static void runDivideAndConquerHungarianSolver(int p, int index) {
    System.out.println(
      "-----------------Divide and Conquer Hungarian Algorithm Solver-----------------"
    );
    double[] lowCoords = {0, 0};
    double[] highCoords = {1, 1};
    Boundary boundary = new Boundary(new Point(lowCoords, -1), new Point(highCoords, -1), new Point(lowCoords, -1), new Point(highCoords, -1), A, B , true);
    boundary.reset();
    dacha = new DivideAndConquerHungarianAlgorithm(boundary);
    startTime = System.currentTimeMillis();
    dacha.solver(p);
    endTime = System.currentTimeMillis();
    if (!validateDualWeights(p)) {
      System.out.println("Infeasible dual weights");
      System.exit(0);
    }
    System.out.println("Feasible dual weights");

    operationNums[index] = dacha.operationsNum;
    hungarianNums[index] = dacha.hungarianNum;
    lastLevelHungrians[index] = dacha.lastLevelHungrians;
    times[index] = (endTime - startTime);
    System.out.println("N: " + N + " dimension: " + dimension + " p: " + p);
    System.out.println("Operations: " + dacha.operationsNum);
    System.out.println("Hungarians: " + dacha.hungarianNum);
    System.out.println("LastLevel: " + dacha.lastLevelHungrians);
    System.out.println("Time taken: " + (endTime - startTime) + " ms");
    System.out.println(
      "-------------------------------------------------------------------------------"
    );
  }

  public static void main(String[] args) {
    // Add the path to the two datasets and the number of points to be used from each dataset
    path1 = "Datasets/A1.txt";
    path2 = "Datasets/B1.txt";
    for (dimension = 5; dimension <= 9; dimension += 1) {
      for (N = 2500; N <= 12500; N += 2500) {
        operationNums = new long[6];
        hungarianNums = new int[6];
        lastLevelHungrians = new int[6];
        times = new double[6];
        generateDatasets();
        for (int i = 0; i <= 5; i++) {
          int p = i+1;
          System.out.println(
          "----------------------------------"
          );
          System.out.println(
            "N = " + N + " dimension = " + dimension + " p = " + p
          );
          System.out.println(
            "----------------------------------"
          );
          runDivideAndConquerHungarianSolver(p, i);
        }
        // runDivideAndConquerHungarianSolver(1, 0);
        System.out.print("Operations -> ");
        for (int i = 0; i < 6; i ++) {
          System.out.print(operationNums[i] + " ");
        }
        System.out.println();
        System.out.print("Hungarians -> ");
        for (int i = 0; i < 6; i ++) {
          System.out.print(hungarianNums[i] + " ");
        }
        System.out.println();
        System.out.print("Last Level Hungarians -> ");
        for (int i = 0; i < 6; i ++) {
          System.out.print(lastLevelHungrians[i] + " ");
        }
        System.out.println();
        System.out.print("Times -> ");
        for (int i = 0; i < 6; i ++) {
          System.out.print(times[i] + " ");
        }
        System.out.println();
      }
    }
  }
}
