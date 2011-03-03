package edu.mit.cci.simulation.excel.responsesurfaces;


import java.util.ArrayList;
import java.util.List;

/**
 * Represents a polynomial equation of any degree
 */
public class Polynomial {

    private int degree = 0;
    private List<Double> params;

    public Polynomial(int degree) {

        if (degree < 0) throw new IllegalArgumentException("Degree must be non-negative");
        this.degree = degree;
        params = new ArrayList<Double>(degree);
        for (int i = 0;i<=degree;i++) {
            params.add(0.0);
        }
    }

    /**
     * Each element in the array is applied to the degree corresponding to
     * that element's position; e.g. the 0th element is the intercept, the
     * 1st element is the parameter for x, the 2nd for x^2, etc.
     *
     * @param p Multipliers at each position in the polinomial
     */
    public Polynomial(double[] p) {
        degree=p.length;
        this.params = new ArrayList<Double>(degree);
        for (int i =0;i<=degree;i++) {
            params.add(0d);
        }
        for (int i = 0;i<p.length;i++) {
            setParam(i,p[i]);
        }
    }

    /**
     * Convenience function; equivalent to setParam(0,value)
     *
     * @param intercept
     */
    public void setIntercept(double intercept) {
        params.set(0,intercept);
    }

    public double getIntercept() {
        return params.get(0);
    }

    public void setParam(int position, double value) {
        if (position < 0 || position > degree) throw new IllegalArgumentException("Position is not valid for this polynomial");

        params.set(position,value);
    }

    public Double getParam(int position) {
        if (position <0 || position>degree) return null;
        return params.get(position);
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("f(x) = ");
        for (int i = degree;i>-1;i--) {
            double param = getParam(i);
            if (param == 0) continue;
            string.append(param<0?"-":"+").append(Math.abs(param));
            if (i>0) {
                string.append("x");
            }
            if (i>1) {
                string.append("^").append(i);
            }
            if (i>0) string.append(" ");
        }
        return string.toString();
    }

    public double eval(double x) {
        double result = 0;
        int exp = 0;
        for (double p:params) {
            result+= Math.pow(x,exp)*p;
        }
        return result;
    }
}
