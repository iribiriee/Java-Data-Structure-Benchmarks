package org.tuc.spatial.tests;

import org.tuc.spatial.Monster;
import org.tuc.spatial.QuadTree;
import org.tuc.spatial.SpatialHash;
import org.tuc.spatial.TucPoint;
import org.tuc.spatial.util.AccessCounter;
import org.tuc.spatial.util.FileReader;

import java.util.List;

public class BenchmarkRunner {

    private static final int K = 1024;
    private static final int D = 10;
    private static final int B1 = 4;
    private static final int B2 = 32;
    private static final int[] N_VALUES = {200, 500, 2000, 5000, 10000};
    private static final String[] TYPES = {"sparse", "dense"};

    public void runPerformanceTests() {
        double[][] allAccessCounts = new double[N_VALUES.length][12];
        double[][] allTimeNs = new double[N_VALUES.length][12];

        for (int nIndex = 0; nIndex < N_VALUES.length; nIndex++) {
            int N = N_VALUES[nIndex];
            double[] accessCounts = new double[12];
            double[] timeNs = new double[12];

            for (int t = 0; t < TYPES.length; t++) {
                String type = TYPES[t];

                QuadTree qt = new QuadTree(K, D);
                SpatialHash sh1 = new SpatialHash(K, D, B1);
                SpatialHash sh2 = new SpatialHash(K, D, B2);

                List<Monster> monsters = FileReader.readMonsterFromDisk(N, type);
                for (Monster m : monsters) {
                    qt.insert(m);
                    sh1.insert(m);
                    sh2.insert(m);
                }

                List<TucPoint> singleSearchPoints = FileReader.readCoordinatesFromFile("single_search_" + type + "_" + N + ".bin");
                for (TucPoint p : singleSearchPoints) {
                    // QuadTree
                    AccessCounter.reset();
                    long t1 = System.nanoTime();
                    qt.search(p.getX(), p.getY());
                    long t2 = System.nanoTime();
                    accessCounts[t * 6] += AccessCounter.get();
                    timeNs[t * 6] += (t2 - t1);

                    // SpatialHash B=4
                    AccessCounter.reset();
                    t1 = System.nanoTime();
                    sh1.search(p.getX(), p.getY());
                    t2 = System.nanoTime();
                    accessCounts[t * 6 + 1] += AccessCounter.get();
                    timeNs[t * 6 + 1] += (t2 - t1);

                    // SpatialHash B=32
                    AccessCounter.reset();
                    t1 = System.nanoTime();
                    sh2.search(p.getX(), p.getY());
                    t2 = System.nanoTime();
                    accessCounts[t * 6 + 2] += AccessCounter.get();
                    timeNs[t * 6 + 2] += (t2 - t1);
                }

                List<TucPoint> nearSearchPoints = FileReader.readCoordinatesFromFile("near_search_" + type + "_" + N + ".bin");
                for (TucPoint p : nearSearchPoints) {
                    // QuadTree
                    AccessCounter.reset();
                    long t1 = System.nanoTime();
                    qt.findNearPoints(p.getX(), p.getY());
                    long t2 = System.nanoTime();
                    accessCounts[t * 6 + 3] += AccessCounter.get();
                    timeNs[t * 6 + 3] += (t2 - t1);

                    // SpatialHash B=4
                    AccessCounter.reset();
                    t1 = System.nanoTime();
                    sh1.findNearPoints(p.getX(), p.getY());
                    t2 = System.nanoTime();
                    accessCounts[t * 6 + 4] += AccessCounter.get();
                    timeNs[t * 6 + 4] += (t2 - t1);

                    // SpatialHash B=32
                    AccessCounter.reset();
                    t1 = System.nanoTime();
                    sh2.findNearPoints(p.getX(), p.getY());
                    t2 = System.nanoTime();
                    accessCounts[t * 6 + 5] += AccessCounter.get();
                    timeNs[t * 6 + 5] += (t2 - t1);
                }
            }

            allAccessCounts[nIndex] = accessCounts;
            allTimeNs[nIndex] = timeNs;
        }

        // ===== PRINT TABLE: Access Counts =====
        System.out.println("\n=== Average Access Counts per Structure per N ===");
        System.out.printf("%-7s%-15s%-15s%-15s%-15s%-15s%-15s%-15s%-15s%-15s%-15s%-15s%-15s\n",
                "N", "S1_Sparse", "S2_Sparse", "S3_Sparse", "R1_Sparse", "R2_Sparse", "R3_Sparse",
                "S1_Dense", "S2_Dense", "S3_Dense", "R1_Dense", "R2_Dense", "R3_Dense");

        for (int i = 0; i < N_VALUES.length; i++) {
            System.out.printf("%-7d", N_VALUES[i]);
            for (int j = 0; j < 12; j++) {
                System.out.printf("%-15.2f", allAccessCounts[i][j] / 100); // avg over 100 ops
            }
            System.out.println();
        }

        // ===== PRINT TABLE: Execution Times =====
        System.out.println("\n=== Average Execution Time (ms) per Structure per N ===");
        System.out.printf("%-7s%-15s%-15s%-15s%-15s%-15s%-15s%-15s%-15s%-15s%-15s%-15s%-15s\n",
                "N", "S1_Sparse", "S2_Sparse", "S3_Sparse", "R1_Sparse", "R2_Sparse", "R3_Sparse",
                "S1_Dense", "S2_Dense", "S3_Dense", "R1_Dense", "R2_Dense", "R3_Dense");

        for (int i = 0; i < N_VALUES.length; i++) {
            System.out.printf("%-7d", N_VALUES[i]);
            for (int j = 0; j < 12; j++) {
                double ms = allTimeNs[i][j] / 100_000.0; // avg over 100 ops
                System.out.printf("%-15.2f", ms);
            }
            System.out.println();
        }
    }



    public void validateResultsN10000() {
        final int K = 1024;
        final int D = 10;
        final int B1 = 4;
        final int B2 = 32;

        // Load monsters for N = 10000 (dense)
        List<Monster> monsters = FileReader.readMonsterFromDisk(10000, "dense");

        // Initialize structures
        QuadTree quadTree = new QuadTree(K, D);
        SpatialHash spatialHash4 = new SpatialHash(K, D, B1);
        SpatialHash spatialHash32 = new SpatialHash(K, D, B2);

        // Insert all monsters into all structures
        for (Monster monster : monsters) {
            quadTree.insert(monster);
            spatialHash4.insert(monster);
            spatialHash32.insert(monster);
        }

        // Specific points to search
        int[][] queryPoints = {
                {1018, 558}, {992, 518}, {594, 646},
                {581, 836}, {718, 827}, {633, 930}
        };

        System.out.println("=== Validation for N = 10000 ===\n");
        System.out.println("🔍 Exact Monster Name Searches:");
        for (int[] point : queryPoints) {
            TucPoint p1 = quadTree.search(point[0], point[1]);
            TucPoint p2 = spatialHash4.search(point[0], point[1]);
            TucPoint p3 = spatialHash32.search(point[0], point[1]);

            String name1 = (p1 instanceof Monster) ? ((Monster)p1).getName() : "null";
            String name2 = (p2 instanceof Monster) ? ((Monster)p2).getName() : "null";
            String name3 = (p3 instanceof Monster) ? ((Monster)p3).getName() : "null";

            System.out.printf("Point (%d,%d):\n", point[0], point[1]);
            System.out.printf("  QuadTree     → %s\n", name1);
            System.out.printf("  SpatialHash4 → %s\n", name2);
            System.out.printf("  SpatialHash32→ %s\n\n", name3);
        }

        // Range search near point
        int centerX = 900, centerY = 688;

        List<String> near1 = quadTree.findNearPoints(centerX, centerY).stream()
                .map(p -> ((Monster)p).getName()).sorted().toList();

        List<String> near2 = spatialHash4.findNearPoints(centerX, centerY).stream()
                .map(p -> ((Monster)p).getName()).sorted().toList();

        List<String> near3 = spatialHash32.findNearPoints(centerX, centerY).stream()
                .map(p -> ((Monster)p).getName()).sorted().toList();

        System.out.println(" Monsters near (900, 688):");
        System.out.println("  QuadTree     → " + near1);
        System.out.println("  SpatialHash4 → " + near2);
        System.out.println("  SpatialHash32→ " + near3);

        boolean allEqual = near1.equals(near2) && near1.equals(near3);
        System.out.println("\n All structures agree on near points? " + (allEqual ? "TRUE" : "FALSE"));
    }

}
