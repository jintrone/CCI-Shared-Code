package edu.mit.cci.simulation.client;

import edu.mit.cci.simulation.client.model.impl.ClientMetaData;


import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * MetaData is used to describe the inputs and outputs in a simulation
 *
 * @author jintrone
 *
 */
@XmlJavaTypeAdapter(ClientMetaData.Adapter.class)
public interface MetaData extends HasId {

    /**
     * Describes how meta data relates to other meta data in the set
     * @author jintrone
     *
     */
    public enum VarContext {
        /**
         * Indicates that the variable is an index
         */
        INDEX,
        /**
         * Indicates that the variable is indexed by the INDEX
         */
        INDEXED,
        /**
         * Indicates that the variable is a standalone value
         */
        SCALAR,

        LIST
    }

    /**
     * Describes how this variable should be interpreted
     * @author jintrone
     *
     */
    public enum VarType {
        /**
         * This variable lies on a continuous range between a min and a max
         */
        RANGE,
        /**
         * This variable is categorical, and take one of a finite number of values
         */
        CATEGORICAL,
        /**
         * This variable is not interpreted; assumed to be free text
         */
        FREE,
        /**
         * This variable is used as input.  It lies on a continuous range between a min
         * and a max.  However, if it is non-integral, the two integer values bounding
         * this value will be used as input, and you will receive two results.
         */
        FUZZY_DISCRETE
    }

    public Long getId();


    /**
     * The MetaData that is an index for this MetaData (if this one is INDEXED)
     * @return
     */
    public MetaData getIndexingMetaData();
    public void setIndexingMetaData(MetaData md);

    public String getName();
    public void setName(String name);


    public String getInternalName();
    public void setInternalName(String name);

    /**
     * The units for this MetaData - should not be an array!
     * @return
     */
    public String[] getUnits();
    public void setUnits(String[] units);

    /**
     * The profile (datatype) for this MetaData - should not be an array!
     * @return
     */
    public Class<Object>[] getProfile();
    public void setProfile(Class<Object>[] profile);


    /**
     * The label for this MetaData - should not be an array!
     * @return
     */
    public String[] getLabels();
    public void setLabels(String[] lables);

    /**
     * The max value for this MetaData - should not be an array!
     * @return
     */
    public String[] getMax();
    public void setMax(String[] n);

    /**
     * The min value for this MetaData - should not be an array!
     * @return
     */
    public String[] getMin();
    public void setMin(String[] n);

    /**
     * The default value for this MetaData - should not be an array!
     * @return
     */
    public String[] getDefault();
    public void setDefault(String[] n);

    public String[] getCategories();


    public String getDescription();
    public void setDescription(String desc);

    public VarContext getVarContext();
    public void setVarContext(VarContext t);

    public VarType getVarType();
    public void setVarType(VarType t);

    public void setExternalInfo(String info);
    public String getExternalInfo();

    //public boolean isIndex();
    public boolean getIndex();
    public void setIndex(boolean b);


    public static class Utils {

        public static String formatNumber(String actual) {
//			if (cls == null || actual==null) return null;
//			if (cls == java.lang.Integer.class) {
//				return actual.intValue();
//			} else if (cls == java.lang.Float.class) {
//				return actual.floatValue();
//			} else if (cls == java.lang.Double.class) {
//				return actual.doubleValue();
//			}
            return actual;
        }

//		public static Number[] convertArray(String[] array, MetaData md) {
//			Number[] result = new Number[array.length];
//
//			for (int i =0;i<array.length;i++) {
//				result[i] = formatNumber(md.getProfile()[i],new Double(array[i]));
//			}
//			return result;
//		}

        // new edit
        public static String[] convertArray(String[] array, MetaData md) {
            String[] result = new String[array.length];
            for (int i =0;i<array.length;i++) {
                result[i] = formatNumber(new String(array[i]));
            }
            return result;
        }
    }

    void setCategories(String[] categories);


}