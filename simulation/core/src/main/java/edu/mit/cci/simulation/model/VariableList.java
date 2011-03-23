package edu.mit.cci.simulation.model;

import com.sun.tools.javac.resources.version;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: jintrone
 * Date: 3/23/11
 * Time: 12:59 AM
 */

@Entity
@Configurable
public class VariableList {

    public Set<DefaultVariable> getVariables() {
        return variables;
    }

    public void setVariables(Set<DefaultVariable> variables) {
        this.variables = variables;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<DefaultVariable> variables = new HashSet<DefaultVariable>();

    @PersistenceContext
    transient EntityManager entityManager;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            VariableList attached = findVariableList(this.id);
            this.entityManager.remove(attached);
        }
    }

    @Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

    @Transactional
    public VariableList merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        VariableList merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public static final EntityManager entityManager() {
        EntityManager em = new VariableList().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countVariableLists() {
        return entityManager().createQuery("select count(o) from VariableList o", Long.class).getSingleResult();
    }

    public static List<VariableList> findAllVariableLists() {
        return entityManager().createQuery("select o from VariableList o", VariableList.class).getResultList();
    }

    public static VariableList findVariableList(Long id) {
        if (id == null) return null;
        return entityManager().find(VariableList.class, id);
    }

    public static List<VariableList> findVariableEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from VariableList o", VariableList.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }



}
