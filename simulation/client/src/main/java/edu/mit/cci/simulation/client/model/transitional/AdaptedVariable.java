package edu.mit.cci.simulation.client.model.transitional;

import edu.mit.cci.simulation.client.MetaData;
import edu.mit.cci.simulation.client.Tuple;
import edu.mit.cci.simulation.client.Variable;
import edu.mit.cci.simulation.client.comm.RepositoryManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;

/**
 * User: jintrone
 * Date: 3/20/11
 * Time: 12:05 PM
 */
public class AdaptedVariable extends AdaptedObject<edu.mit.cci.simulation.model.Tuple> implements Variable {


    List<Tuple> values = null;
    edu.mit.cci.simulation.model.Tuple index = null;



    public AdaptedVariable(edu.mit.cci.simulation.model.Tuple tuple, Set<edu.mit.cci.simulation.model.Tuple> peers, RepositoryManager manager) {
        super(tuple,manager);

        if (peers != null && tuple.getVar().getIndexingVariable()!=null) {
            edu.mit.cci.simulation.model.Variable indexing = tuple.getVar().getIndexingVariable();

            for (edu.mit.cci.simulation.model.Tuple t:peers) {
                if (t.getVar().equals(indexing)) {
                    index = t;
                    break;
                }
            }
        }

    }

    public Long getId() {
        return model().getId();
    }

    @Override
    public List<Tuple> getValue() {
        if (values == null) {
            values = new ArrayList<Tuple>();
             edu.mit.cci.simulation.model.Tuple[] inputs = null;
            if (index!=null) {
                inputs = new edu.mit.cci.simulation.model.Tuple[] {index,model()};
            } else {
                inputs = new edu.mit.cci.simulation.model.Tuple[] {model()};
            }
            for (int i=0;i<model().getValues().length;i++) {
              values.add(new AdaptedTuple(inputs,i ));
            }
        }
        return values;
    }

    @Override
    public MetaData getMetaData() {
        return (MetaData) manager().getAdaptor(model().getVar());
    }



    @Override
    public void setMetaData(MetaData md) {
    }

    @Override
    public void addValue(Tuple t) {
    }
    
    public String getValueAsJSON() {
        List<Tuple> value = getValue();
        String[][] valueForJSON = new String[value.size()][2];
        int i=0;
        for (Tuple val: value) {
            valueForJSON[i++] = val.getValues();
        }
        return JSONArray.fromObject(valueForJSON).toString();
    }
}
