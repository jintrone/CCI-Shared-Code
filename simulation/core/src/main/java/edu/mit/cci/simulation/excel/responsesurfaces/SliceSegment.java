package edu.mit.cci.simulation.excel.responsesurfaces;

/**
 * A SliceSegment wraps a single function that describes a portion of the inferred surface
 * between two datasets a given index.  E.g. a line drawn between points chosen at a given year
 * between two adjacent datasets.
 *
 * A SliceSegment is a segment of an {@link Slice}.
 *
 *
 *
* User: jintrone
* Date: 2/24/11
* Time: 9:05 AM
*/
public class SliceSegment<T extends Comparable<T>,U extends Comparable<U>>  {

    public T fromCriterion;
    public T toCriterion;
    public Polynomial function;
    public U index;

    public SliceSegment(T from, T to, U index, Polynomial function) {
        this.function = function;
        if (from.compareTo(to)>0) throw new IllegalArgumentException("From must be less than to");
        this.fromCriterion = from;
        this.toCriterion = to;
        this.index = index;
    }

    public U getIndex() {
        return index;
    }

    public T getFromCriterion() {
        int i = fromCriterion.compareTo(toCriterion);
        return fromCriterion;
    }

    public T getToCriterion() {
        return toCriterion;
    }

    public Polynomial getFunction() {
        return function;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(fromCriterion).append("-").append(toCriterion).append(" : ").append(function);
        return builder.toString();
    }



}
