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

  /**
   * Read the two datasets
   */
  private static void initializeDatasets(int p) {
    A = new Point[N];
    B = new Point[N];
    String[] arr;
    try {
      Scanner scanner = new Scanner(new File(path1));

      for (int i = 0; i < N; i++) {
        arr = scanner.nextLine().split(" ");
        A[i] =
          new Point(
            Double.parseDouble(arr[0]),
            Double.parseDouble(arr[1]),
            arr.length > 2 ? Double.parseDouble(arr[2]) : 0,
            i,
            N
          );
      }
      scanner.close();

      scanner = new Scanner(new File(path2));
      for (int i = 0; i < N; i++) {
        arr = scanner.nextLine().split(" ");
        B[i] =
          new Point(
            Double.parseDouble(arr[0]),
            Double.parseDouble(arr[1]),
            arr.length > 2 ? Double.parseDouble(arr[2]) : 0,
            i, 
            N
          );
      }
      scanner.close();

      // for (int i = 0; i < N; i++) {
      //   for (int j = 0; j < N; j++) {
      //     A[i].getDistance(B[j], p);
      //   }
      // }

    } catch (FileNotFoundException e) {
      System.out.println("File not found");
      e.printStackTrace();
    }
  }

  /**
   *
   * @param p1
   * @param p2
   * @return - Distance between the two points
   */
  private static double getDistance(Point p1, Point p2, int p) {
    return Math.pow(
      ((p1.x - p2.x) * (p1.x - p2.x)) +
      ((p1.y - p2.y) * (p1.y - p2.y)) +
      ((p1.z - p2.z) * (p1.z - p2.z))
    , p/2);
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
    // System.out.println(
    //   "-----------------Divide and Conquer Hungarian Algorithm Solver-----------------"
    // );
    Boundary boundary = new Boundary(1, 0, 0, 1, 1, 0);
    dacha = new DivideAndConquerHungarianAlgorithm(A, B);
    startTime = System.currentTimeMillis();
    dacha.solver(boundary, p);
    endTime = System.currentTimeMillis();
    // if (!validateDualWeights(p)) {
    //   System.out.println("Infeasible dual weights");
    //   System.exit(0);
    // }
    // System.out.println("Feasible dual weights");

    operationNums[index] = dacha.operationsNum;
    hungarianNums[index] = dacha.hungarianNum;
    lastLevelHungrians[index] = dacha.lastLevelHungrians;
    times[index] = (endTime - startTime);
    // System.out.println("Operations Num: " + dacha.operationsNum);
    // System.out.println("Time taken: " + (endTime - startTime) + " ms");
    // System.out.println(
    //   "-------------------------------------------------------------------------------"
    // );
  }

  public static void main(String[] args) {
    try {
      // Add the path to the two datasets and the number of points to be used from each dataset
      path1 = "Datasets/A9.txt";
      path2 = "Datasets/B9.txt";
      for (N = 2500; N <= 20000; N += 2500) {
        operationNums = new long[6];
        hungarianNums = new int[6];
        lastLevelHungrians = new int[6];
        times = new double[6];
        for (int i = 0; i < 6; i++) {
          int p = 2*(i+1);
          initializeDatasets(p);
          // System.out.println("Running for p = " + p);
          runDivideAndConquerHungarianSolver(p, i);
          // runHungarianSolver(i);
        }
        System.out.print("Operations: N: " + N + " -> ");
        for (int i = 0; i < 6; i ++) {
          System.out.print(operationNums[i] + " ");
        }
        System.out.println();
        System.out.print("Hungarians: N: " + N + " -> ");
        for (int i = 0; i < 6; i ++) {
          System.out.print(hungarianNums[i] + " ");
        }
        System.out.println();
        System.out.print("Last Level Hungarians: N: " + N + " -> ");
        for (int i = 0; i < 6; i ++) {
          System.out.print(lastLevelHungrians[i] + " ");
        }
        System.out.println();
        System.out.print("Times: N: " + N + " -> ");
        for (int i = 0; i < 6; i ++) {
          System.out.print(times[i] + " ");
        }
        System.out.println();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
