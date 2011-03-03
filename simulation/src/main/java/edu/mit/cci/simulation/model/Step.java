package edu.mit.cci.simulation.model;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * User: jintrone
 * Date: 2/14/11
 * Time: 1:21 PM
 */
@RooJavaBean
    @RooToString
    @RooEntity
    public class Step {

        private Integer order_;

        @ManyToMany(cascade = CascadeType.ALL)
        private Set<DefaultSimulation> simulations = new HashSet<DefaultSimulation>();



}
