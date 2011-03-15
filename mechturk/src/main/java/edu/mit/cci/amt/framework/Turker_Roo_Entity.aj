// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.mit.cci.amt.framework;

import edu.mit.cci.amt.framework.Turker;
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

privileged aspect Turker_Roo_Entity {
    
    declare @type: Turker: @Entity;
    
    @PersistenceContext
    transient EntityManager Turker.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Turker.id;
    
    @Version
    @Column(name = "version")
    private Integer Turker.version;
    
    public Long Turker.getId() {
        return this.id;
    }
    
    public void Turker.setId(Long id) {
        this.id = id;
    }
    
    public Integer Turker.getVersion() {
        return this.version;
    }
    
    public void Turker.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void Turker.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Turker.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Turker attached = Turker.findTurker(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Turker.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public Turker Turker.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Turker merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager Turker.entityManager() {
        EntityManager em = new Turker().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Turker.countTurkers() {
        return entityManager().createQuery("select count(o) from Turker o", Long.class).getSingleResult();
    }
    
    public static List<Turker> Turker.findAllTurkers() {
        return entityManager().createQuery("select o from Turker o", Turker.class).getResultList();
    }
    
    public static Turker Turker.findTurker(Long id) {
        if (id == null) return null;
        return entityManager().find(Turker.class, id);
    }
    
    public static List<Turker> Turker.findTurkerEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from Turker o", Turker.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}