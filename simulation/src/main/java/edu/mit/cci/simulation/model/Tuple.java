package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.util.U;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@RooJavaBean
@RooToString
@RooEntity
@XmlRootElement(name="Tuple")
@XmlAccessorType(XmlAccessType.NONE)
public class Tuple {



    @NotNull
    @ManyToOne
    @XmlElement(name="Variable")
    private Variable var;

    @Column(columnDefinition="LONGTEXT")
    @XmlElement(name="Value")
    private String value_;

    private transient String[] values;

    public String[] getValues() {

        if (values == null) {
            if (value_ == null) return null;
            else values = var!=null&&var.getDataType()==DataType.NUM?U.unescapeNumeric(value_,var.getPrecision_()):U.unescape(value_);
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
