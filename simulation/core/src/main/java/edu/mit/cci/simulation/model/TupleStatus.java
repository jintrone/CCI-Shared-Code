package edu.mit.cci.simulation.model;

/**
 * User: jintrone
 * Date: 3/14/11
 * Time: 3:49 PM
 */
public enum TupleStatus {
    ERR_OOB("@ERR_OOB"), ERR_CALC("@ERR_CALC");

    final String encoded;

    TupleStatus(String encoded) {
        this.encoded = encoded;
    }

    public String encode() {
        return encoded;
    }

    public static TupleStatus decode(String s) {
        for (TupleStatus ts:TupleStatus.values()) {
            if (ts.encoded.equals(s)) {
                return ts;
            }
        }
        return null;
    }
}
