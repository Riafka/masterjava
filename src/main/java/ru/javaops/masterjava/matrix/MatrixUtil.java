package ru.javaops.masterjava.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    //implemented parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        final int indexCount = matrixA.length / MainMatrix.THREAD_NUMBER;
        int indexBegin = 0;
        int indexEnd = indexCount;
        List<Future<MyTask>> futures = new ArrayList<>();
        final CompletionService<MyTask> completionService = new ExecutorCompletionService<>(executor);

        for (int i = 0; i < MainMatrix.THREAD_NUMBER; i++) {
            MyTask thread = new MyTask(indexBegin, indexEnd, matrixA, matrixB, matrixC);
            Future<MyTask> futureResult = completionService.submit(thread);
            futures.add(futureResult);
            indexBegin += indexCount;
            indexEnd += indexCount;
        }

//        for (Future<MyTask> future : futures) {
//            MyTask task = future.get();
//            for (int i = task.beginIndex; i < task.endIndex; i++) {
//                matrixC[i] = task.matrixC[i];
//            }
//        }
        while (!futures.isEmpty()) {
            Future<MyTask> future = completionService.poll(5, TimeUnit.SECONDS);
            if (future == null) {
                throw new InterruptedException();
            }
            futures.remove(future);
            MyTask task = future.get();
            for (int i = task.beginIndex; i < task.endIndex; i++) {
                matrixC[i] = task.matrixC[i];
            }
        }

        return matrixC;
    }

    //optimized by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        int[] thatColumn = new int[matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            for (int k = 0; k < matrixSize; k++) {
                thatColumn[k] = matrixB[k][i];
            }

            for (int j = 0; j < matrixSize; j++) {
                int[] thisRow = matrixA[i];
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += thisRow[k] * thatColumn[k];
                }
                matrixC[i][j] = sum;
            }
        }
        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    static class MyTask implements Callable<MyTask> {
        final int beginIndex;
        final int endIndex;
        final int[][] matrixA;
        final int[][] matrixB;
        final int[][] matrixC;

        public MyTask(int beginIndex, int endIndex, int[][] matrixA, int[][] matrixB, int[][] matrixC) {
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
            this.matrixA = matrixA;
            this.matrixB = matrixB;
            this.matrixC = matrixC;
        }

        @Override
        public MyTask call() {
            final int matrixSize = matrixA.length;

            int[] thatColumn = new int[matrixSize];
            for (int i = beginIndex; i < endIndex; i++) {
                for (int k = 0; k < matrixSize; k++) {
                    thatColumn[k] = matrixB[k][i];
                }

                for (int j = 0; j < matrixSize; j++) {
                    int[] thisRow = matrixA[i];
                    int sum = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        sum += thisRow[k] * thatColumn[k];
                    }
                    matrixC[i][j] = sum;
                }
            }
            return this;
        }
    }
}
