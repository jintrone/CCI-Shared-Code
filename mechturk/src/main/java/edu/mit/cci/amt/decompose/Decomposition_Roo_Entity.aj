// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.mit.cci.amt.decompose;

import edu.mit.cci.amt.decompose.Decomposition;
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

privileged aspect Decomposition_Roo_Entity {
    
    declare @type: Decomposition: @Entity;
    
    @PersistenceContext
    transient EntityManager Decomposition.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Decomposition.id;
    
    @Version
    @Column(name = "version")
    private Integer Decomposition.version;
    
    public Long Decomposition.getId() {
        return this.id;
    }
    
    public void Decomposition.setId(Long id) {
        this.id = id;
    }
    
    public Integer Decomposition.getVersion() {
        return this.version;
    }
    
    public void Decomposition.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void Decomposition.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Decomposition.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Decomposition attached = Decomposition.findDecomposition(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Decomposition.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public Decomposition Decomposition.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Decomposition merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager Decomposition.entityManager() {
        EntityManager em = new Decomposition().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Decomposition.countDecompositions() {
        return entityManager().createQuery("select count(o) from Decomposition o", Long.class).getSingleResult();
    }
    
    public static List<Decomposition> Decomposition.findAllDecompositions() {
        return entityManager().createQuery("select o from Decomposition o", Decomposition.class).getResultList();
    }
    
    public static Decomposition Decomposition.findDecomposition(Long id) {
        if (id == null) return null;
        return entityManager().find(Decomposition.class, id);
    }
    
    public static List<Decomposition> Decomposition.findDecompositionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from Decomposition o", Decomposition.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}