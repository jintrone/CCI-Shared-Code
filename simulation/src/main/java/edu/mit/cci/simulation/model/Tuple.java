package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.util.SimulationValidationException;
import edu.mit.cci.simulation.util.U;
import edu.mit.cci.simulation.util.Validation;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

@RooJavaBean
@RooToString
@RooEntity
@XmlRootElement(name = "Tuple")
@XmlAccessorType(XmlAccessType.NONE)
public class Tuple {


    public Tuple(Variable v) {
        setVar(v);
    }

    @NotNull
    @ManyToOne
    @XmlElement(name = "Variable")
    @XmlIDREF
    private Variable var;

    @Column(columnDefinition = "LONGTEXT")
    @XmlElement(name = "Value")
    private String value_;

    @Column(columnDefinition = "LONGTEXT")
    @XmlElement(name = "Status")
    private String statuses_;

    private transient String[] values;

    private transient Map<Integer, TupleStatus> statuses = new HashMap<Integer,TupleStatus>();

    public Tuple() {

    }

    public String[] getValues() {

        if (values == null) {
            if (value_ == null) return null;
            else
                values = var != null && var.getDataType() == DataType.NUM ? U.unescapeNumeric(value_, var.getPrecision_()) : U.unescape(value_);
        }
        return values;
    }

    public void setValues(String[] values) throws SimulationValidationException {
        Validation.canSet(this, values);
        Validation.checkDataType(var, values, true);
        clearStatus();
        if (var.getDataType() == DataType.NUM) {
            for (int i = 0; i < values.length; i++) {
                if (values[i]==null) continue;
                Double d = Double.parseDouble(values[i]);
               if (d<var.getMin_()||d>var.getMax_()) {
                   setStatus(i,TupleStatus.ERR_OOB);
                    values[i] = null;
               }
            }
        }
        this.values = values;
        this.value_ = (U.escape(values));
    }

    public void setValue_(String val) throws SimulationValidationException {
        setValues(U.unescape(val));
    }

    private void clearStatus() {
        statuses_ = null;
        statuses.clear();
    }


    public TupleStatus getStatus(int i) {
        if (statuses == null) {
            if (statuses_ == null) {
                return null;
            } else {
                statuses = new HashMap(U.parseOrderedMap(statuses_));
            }
        }
        return statuses.get(i);
    }

    public void setStatus(int i, TupleStatus status) {
        TupleStatus current = getStatus(i);
        if (current != status) {
            statuses.put(i, status);
            statuses_ = U.updateStringMap(i + "", status.name(), statuses_);
        }
    }


    public static void main(String[] args) {
        String[] vals = new String[]{"hi;;", "ad%%253Bd;;", "nothing;;;"};
        String encoded = U.escape(vals);
        System.err.println(encoded);

        vals = U.unescape(encoded);
        for (String val : vals) {
            System.err.println(val);
        }

    }

    @XmlAttribute
    @XmlID
    public String getId_() {
        return "" + getId();
    }

    public static Tuple copy(Tuple t) {
        Tuple result = new Tuple();
        result.setVar(t.getVar());
        try {
            result.setValue_(t.getValue_());
        } catch (SimulationValidationException e) {
            throw new RuntimeException("Encountered an invalid tuple on copy",e);
        }

        result.persist();
        return result;
    }


}
