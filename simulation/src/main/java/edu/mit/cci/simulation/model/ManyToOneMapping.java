package edu.mit.cci.simulation.model;


public enum ManyToOneMapping {
    SUM() {

        public void reduce(Tuple t) {
            double sum = 0;
            if (t.getValues() == null || t.getValues().length==0) return;
            for (String v:t.getValues()) {
                sum+=Double.parseDouble(v);
            }
            t.setValue_(sum+";");
        }
    },MEDIAN() {
        public void reduce(Tuple t) {
            if (t.getValues() == null || t.getValues().length==0) return;
            String[] vals = t.getValues();
            Double[] d = new Double[vals.length];
            int i = 0;
            for (String v:vals) {
              d[i++] = Double.parseDouble(v);
            }

           int pos = Math.max(0,-1+((vals.length & 1)==1? (int) Math.ceil(vals.length / 2.0d) :vals.length/2));
           t.setValue_(d[pos]+";");
        }
    },FIRST() {
         public void reduce(Tuple t) {
             if (t.getValues() == null || t.getValues().length==0) return;
             t.setValue_(t.getValues()[0]+";");

        }
    },LAST() {
         public void reduce(Tuple t) {
             if (t.getValues() == null || t.getValues().length==0) return;
             t.setValue_(t.getValues()[t.getValues().length-1]+";");

        }
    };

    public void reduce(Tuple t) {

    }
}
