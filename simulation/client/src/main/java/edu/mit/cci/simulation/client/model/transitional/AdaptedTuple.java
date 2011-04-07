package edu.mit.cci.simulation.client.model.transitional;

import edu.mit.cci.simulation.client.Tuple;
import edu.mit.cci.simulation.client.TupleStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jintrone
 * Date: 3/20/11
 * Time: 12:06 PM
 */
public class AdaptedTuple implements Tuple {


    public String[] values;
    public List<TupleStatus> statuses;

    public AdaptedTuple(edu.mit.cci.simulation.model.Tuple[] tuples,int index) {
       values = new String[tuples.length];
        statuses = new ArrayList<TupleStatus>();
        for (int i=0;i<tuples.length;i++) {
            values[i]  = tuples[i].getValues()[index];
            statuses.add(transform(tuples[i].getStatus(index)));
        }
    }

    @Override
    public String[] getValues() {
       return values;
    }


    @Override
    public TupleStatus getStatus(int index) {
        if (statuses.size() <= index) {
            return null;
        }
        return statuses.get(index);
    }

    @Override
    public List<TupleStatus> getAllStatuses() {
       return statuses;
    }

    private static TupleStatus transform(edu.mit.cci.simulation.model.TupleStatus status) {
        if (status == null) return TupleStatus.NORMAL;
         switch (status) {
             case ERR_OOB: {
                 return TupleStatus.OUT_OF_RANGE;
             }

             case ERR_CALC: {
                 return TupleStatus.INVALID;
             }

             default: {
                 return TupleStatus.NORMAL;
             }
         }
    }

    @Override
     public void setValues(String[] vals) {
         //To change body of implemented methods use File | Settings | File Templates.
     }

}
