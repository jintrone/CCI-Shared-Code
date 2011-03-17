package edu.mit.cci.simulation.client;

/**
 * Created by IntelliJ IDEA.
 * User: jintrone
 * Date: May 31, 2010
 * Time: 10:40:02 PM
 * To change this template use File | Settings | File Templates.
 */
public enum TupleStatus {
    OUT_OF_RANGE("@RANGE"),INVALID("@ERROR"),NORMAL(null);
    private String code;

    TupleStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static TupleStatus lookup(String lookup) {
        for (TupleStatus st:values()) {
            if (st.code!=null && st.code.equals(lookup)) {
                return st;
            }
        }
        return null;
    }

}
