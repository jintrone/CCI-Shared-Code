package edu.mit.cci.simulation.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = Tuple.class)
public class TupleDataOnDemand {

    @Autowired
    private VariableDataOnDemand variableDataOnDemand;

      public Tuple getNewTransientTuple(int index) {
        edu.mit.cci.simulation.model.Tuple obj = new edu.mit.cci.simulation.model.Tuple();

        obj.setVar(variableDataOnDemand.getRandomVariable());

          return obj;
    }
}
