package edu.mit.cci.simulation.model;


import edu.mit.cci.simulation.util.SimulationValidationException;

public enum ManyToOneMapping {
    SUM() {

        public String reduce(String[] vals) throws SimulationValidationException {
            double sum = 0;
            if (vals == null || vals.length==0) return null;
            for (String v:vals) {
                sum+=Double.parseDouble(v);
            }
            return ""+sum;
        }
    },MEDIAN() {
        public String reduce(String[] vals) throws SimulationValidationException {
            if (vals == null || vals.length==0) return null;

            Double[] d = new Double[vals.length];
            int i = 0;
            for (String v:vals) {
              d[i++] = Double.parseDouble(v);
            }

           int pos = Math.max(0,-1+((vals.length & 1)==1? (int) Math.ceil(vals.length / 2.0d) :vals.length/2));
           return ""+d[pos];
        }
    },FIRST() {
         public String reduce(String[] vals) throws SimulationValidationException {
             if (vals == null || vals.length==0) return null;
             return vals[0];

        }
    },LAST() {
         public String reduce(String[] vals) throws SimulationValidationException {
             if (vals == null || vals.length==0) return null;
             return vals[vals.length-1];

        }
    };


    public abstract String reduce(String[] values) throws SimulationValidationException;
}
