// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.model.DefaultSimulation;
import java.lang.Integer;
import java.lang.Long;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import org.springframework.transaction.annotation.Transactional;

privileged aspect DefaultSimulation_Roo_Entity {
    
    declare @type: DefaultSimulation: @Entity;
    
    @PersistenceContext
    transient EntityManager DefaultSimulation.entityManager;
    

    
    @Version
    @Column(name = "version")
    private Integer DefaultSimulation.version;
    

    
    public Integer DefaultSimulation.getVersion() {
        return this.version;
    }
    
    public void DefaultSimulation.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void DefaultSimulation.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void DefaultSimulation.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            DefaultSimulation attached = DefaultSimulation.findDefaultSimulation(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void DefaultSimulation.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public DefaultSimulation DefaultSimulation.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        DefaultSimulation merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager DefaultSimulation.entityManager() {
        EntityManager em = new DefaultSimulation().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long DefaultSimulation.countDefaultSimulations() {
        return entityManager().createQuery("select count(o) from DefaultSimulation o", Long.class).getSingleResult();
    }
    
    public static List<DefaultSimulation> DefaultSimulation.findAllDefaultSimulations() {
        return entityManager().createQuery("select o from DefaultSimulation o", DefaultSimulation.class).getResultList();
    }
    
    public static DefaultSimulation DefaultSimulation.findDefaultSimulation(Long id) {
        if (id == null) return null;
        return entityManager().find(DefaultSimulation.class, id);
    }
    
    public static List<DefaultSimulation> DefaultSimulation.findDefaultSimulationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from DefaultSimulation o", DefaultSimulation.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
