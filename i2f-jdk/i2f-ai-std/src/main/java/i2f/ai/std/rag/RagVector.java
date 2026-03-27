package i2f.ai.std.rag;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/3/25 14:04
 * @desc
 */
@Data
@NoArgsConstructor
public class RagVector {
    protected double[] array;

    public RagVector(double[] arr) {
        this.array = arr;
    }

    public RagVector(int dimension) {
        this.array = new double[dimension];
    }

    public static RagVector fromArray(double[] vector) {
        RagVector ret = new RagVector();
        ret.setArray(vector);
        return ret;
    }

    public static RagVector fromList(List<Double> vector) {
        RagVector ret = new RagVector();
        double[] arr = new double[vector.size()];
        int i = 0;
        for (Double item : vector) {
            arr[i++] = item;
        }
        ret.setArray(arr);
        return ret;
    }

    public static RagVector fromFloatArray(float[] vector) {
        RagVector ret = new RagVector();
        double[] arr = new double[vector.length];
        int i = 0;
        for (float item : vector) {
            arr[i++] = item;
        }
        ret.setArray(arr);
        return ret;
    }

    public static RagVector fromFloatList(List<Float> vector) {
        RagVector ret = new RagVector();
        double[] arr = new double[vector.size()];
        int i = 0;
        for (Float item : vector) {
            arr[i++] = item;
        }
        ret.setArray(arr);
        return ret;
    }

    public double[] toArray() {
        double[] ret = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            ret[i] = array[i];
        }
        return ret;
    }

    public List<Double> toList() {
        List<Double> ret = new ArrayList<>(array.length);
        for (double item : array) {
            ret.add(item);
        }
        return ret;
    }

    public float[] toFlatArray() {
        float[] ret = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            ret[i] = (float) array[i];
        }
        return ret;
    }

    public List<Float> toFloatList() {
        List<Float> ret = new ArrayList<>(array.length);
        for (double item : array) {
            ret.add((float) item);
        }
        return ret;
    }

    public int dimension() {
        return array.length;
    }

    public RagVector copy() {
        RagVector ret = new RagVector();
        ret.setArray(this.toArray());
        return ret;
    }

    public double dot(RagVector v2) {
        return dot(this, v2);
    }

    public double norm() {
        return norm(this);
    }

    public double cosineSimilar(RagVector v2) {
        return cosineSimilar(this, v2);
    }

    public static double cosineSimilar(RagVector v1, RagVector v2) {
        double n1 = v1.norm();
        double n2 = v2.norm();
        if (n1 == 0 || n2 == 0) {
            return 0;
        }
        double dot = v1.dot(v2);
        return dot / (n1 * n2);
    }

    public static double dot(RagVector v1, RagVector v2) {
        if (v1.array.length != v2.array.length) {
            throw new IllegalArgumentException("vector dot require size equals");
        }
        double ret = 0;
        for (int i = 0; i < v1.array.length; i++) {
            ret += v1.array[i] * v2.array[i];
        }
        return ret;
    }

    public static double norm(RagVector v) {
        double sum = 0;
        for (double num : v.array) {
            sum += Math.pow(num, 2.0);
        }
        return Math.sqrt(sum);
    }
}
