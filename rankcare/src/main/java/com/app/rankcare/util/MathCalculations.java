package com.app.rankcare.util;

import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;

public class MathCalculations {
    public static double[] calculateAValue(int n, double mean, double sd) {
        double pow2 = Math.pow(mean, 2);
        double sdPow2 = Math.pow(sd, 2);
        double mu = Math.log(pow2 / Math.sqrt(sdPow2 + pow2));
        double sigma = Math.sqrt(Math.log(sdPow2 / pow2 + 1));

        return MathCalculations.calculateLogNrnd(n, mu, sigma);
    }

    public static double[] calculateLogNrnd(int n, double scale, double shape) {
        if (scale == 0 && shape == 0) {
            return new double[n];
        }

        double[] val = new double[n];

        LogNormalDistribution logNormalDistribution = new LogNormalDistribution(scale, shape, 1);

        for (int i = 0; i < n; i++) {
            val[i] = logNormalDistribution.sample();
        }

        return val;
    }

    public static double[] calculateNormrnd(int n, double scale, double shape) {
        if (scale == 0 && shape == 0) {
            return new double[n];
        }

        double[] val = new double[n];

        NormalDistribution normalDistribution = new NormalDistribution(scale, shape, 1);

        for (int i = 0; i < n; i++) {
            val[i] = normalDistribution.sample();
        }

        return val;
    }

    public static double[] divideArray(double[] arr, double by) {
        double[] res = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i] / by;
        }

        return res;
    }

    public static double[] divideArray(double[] arr, double[] by) {
        double[] res = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i] / by[i];
        }

        return res;
    }

    public static double[] multiplyArray(double[] arr, double by) {
        double[] res = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i] * by;
        }

        return res;
    }

    public static double[] multiplyArray(double[] arr, double[] by) {
        double[] res = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i] * by[i];
        }

        return res;
    }

    public static double[] addArray(double[] arr, double[] by) {
        double[] res = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i] + by[i];
        }

        return res;
    }

    public static double mean(double[] arr) {
        double sum = 0;

        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }

        return sum / arr.length;
    }

    public static void printArray(double[] arr) {
        for (double anArr : arr) {
            System.out.println(anArr);
        }
    }
}
