package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.util.U;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * User: jintrone
 * Date: 2/9/11
 * Time: 2:00 PM
 */
@RooJavaBean
@RooToString
@RooEntity
public class Variable {

    private String name;

    private String description;

    @NotNull
    private Integer arity = 1;

    @Enumerated
    private DataType dataType;

    private Integer precision_;

    private Double max_;

    private Double min_;

    private String _optionsRaw;

    @Transient
    private String[] options;

    @ManyToOne
    private edu.mit.cci.simulation.model.Variable indexingVariable;

    public String[] getOptions() {
        if (_optionsRaw == null) {
            return null;
        } else if (options == null) {
            options = U.unescape(_optionsRaw);
        }
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
        _optionsRaw = U.escape(options);
    }
}
