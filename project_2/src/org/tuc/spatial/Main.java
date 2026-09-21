package org.tuc.spatial;

import org.tuc.spatial.tests.BenchmarkRunner;

public class Main {
    public static void main(String[] args) {
        BenchmarkRunner benchmarkRunner = new BenchmarkRunner();
        benchmarkRunner.validateResultsN10000();
        System.out.println('\n' + "=====================================================" +'\n');
        benchmarkRunner.runPerformanceTests();
    }
}