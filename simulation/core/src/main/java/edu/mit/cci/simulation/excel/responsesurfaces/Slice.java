package edu.mit.cci.simulation.excel.responsesurfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
*   A Slice is a composition of {@link SliceSegment}s; it describes the surface between
 *  two adjacent datasets.
 *
 *  The Slice maintains segments in order according to {@link edu.mit.cci.simulation.excel.responsesurfaces.SliceSegment#getIndex()}
 *
 *  Slice implements the {@link Comparable} interface.  Comparing two slices will order them according to
 *  {@link edu.mit.cci.simulation.excel.responsesurfaces.SliceSegment#getToCriterion()}, which is the upper bound of the slice
 *
 *
 * User: jintrone
* Date: 2/24/11
* Time: 9:13 AM
*/


public class Slice<T extends Comparable<T>,U extends Comparable<U>> extends ArrayList<SliceSegment<T ,U >> implements Comparable<Slice<T,U>>  {

    Comparator<SliceSegment<T, U>> segmentComparator = new Comparator<SliceSegment<T, U>> () {

        @Override
        public int compare(SliceSegment<T, U> segment1, SliceSegment<T, U> segment2) {
          return segment1.getIndex().compareTo(segment2.getIndex());
        }
    };

    @Override
    public SliceSegment<T, U> set(int i, SliceSegment<T, U> sliceSegment) {
        throw new UnsupportedOperationException("Slice does not support explicit positioning of segments; modify the segment's index to obtain a different ordering");
    }

    @Override
    public boolean add(SliceSegment<T, U> sliceSegment) {
        boolean result = super.add(sliceSegment);
        List<SliceSegment<T,U>> tmp = new ArrayList<SliceSegment<T,U>>(this);
        Collections.sort(tmp,segmentComparator);
        this.clear();
        this.addAll(tmp);
        return result;
    }

    @Override
    public void add(int i, SliceSegment<T, U> sliceSegment) {
        throw new UnsupportedOperationException("Slice does not support explicit positioning of segments; modify the segment's index to obtain a different ordering");
    }

    public int compareTo(Slice<T,U> other) {
        SliceSegment<T,U> otherrange = other.get(other.size()-1);
        SliceSegment<T,U> myrange = get(this.size()-1);
        return myrange.getToCriterion().compareTo(otherrange.getToCriterion());

    }




}
