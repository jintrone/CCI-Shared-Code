package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.util.U;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooEntity
public class Tuple {



    @NotNull
    @ManyToOne
    private Variable var;

    @Column(columnDefinition="LONGTEXT")
    private String value_;

    @Transient
    private String[] values;

    public String[] getValues() {

        if (values == null) {
            if (value_ == null) return null;
            else values = U.unescape(value_);
        }
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
        this.value_=(U.escape(values));
    }

    public void setValue_(String val) {
        this.value_ = val;
        this.values = null;
    }


    public static void main(String[] args) {
        String[] vals = new String[] {"hi;;","ad%%253Bd;;","nothing;;;"};
        String encoded = U.escape(vals);
         System.err.println(encoded);

        vals = U.unescape(encoded);
        for (String val:vals) {
          System.err.println(val);
        }

    }

    public static Tuple copy(Tuple t) {
        Tuple result = new Tuple();
        result.setValue_(t.getValue_());
        result.setVar(t.getVar());
        result.persist();
        return result;
    }


}
