// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.mit.cci.simulation.model;

import java.lang.String;

privileged aspect Step_Roo_ToString {
    
    public String Step.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order_: ").append(getOrder_()).append(", ");
        sb.append("Simulations: ").append(getSimulations() == null ? "null" : getSimulations().size());
        return sb.toString();
    }
    
}
