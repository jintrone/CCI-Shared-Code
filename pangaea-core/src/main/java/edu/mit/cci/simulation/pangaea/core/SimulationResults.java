package edu.mit.cci.simulation.pangaea.core;

import java.util.*;


public class SimulationResults {
    
    private static float DEFAULT_FF_DIVISOR = 3.66F;

    private static float CUMULATIVE_EMISSIONS_2005 = 40.548074f;

    

	public enum VensimVariable implements Variable {
		DEVELOPED_COUNTRIES_FF_EMISSIONS("Developed countries fossil fuel emissions","DevelopedFossilFuelEmissions", "Aggregated CO2 FF emissions[Developed Countries]", new Divider(DEFAULT_FF_DIVISOR)),
		DEVELOPINGA_COUNTRIES_FF_EMISSIONS("Developing A countries fossil fuel emissions","DevelopingAFossilFuelEmissions", "Aggregated CO2 FF emissions[Developing A Countries]", new Divider(DEFAULT_FF_DIVISOR)),
        DEVELOPINGB_COUNTRIES_FF_EMISSIONS("Developing B fossel fuel emissions","DevelopingBFossilFuelEmissions", "Aggregated CO2 FF emissions[Developing B Countries]", new Divider(DEFAULT_FF_DIVISOR)),
        CO2_CONCENTRATION("CO2 Concentration","AtmosphericCO2Concentration", "Atm conc CO2[\"Deterministic\"]"),
        TEMP_CHANGE("Expected temperature change","GlobalTempChange", "Temperature change from preindustrial[\"Deterministic\"]"),
        SEA_LEVEL_RISE("Sea level rise","Sea_Level_Rise_output", "Change in Sea Level[\"Deterministic\"]",new Accumulator()),
        BAU_CO2_CONCENTRATION("BAU CO2 Concentration","BAUCO2Concentration", "BAU atm conc CO2"),
        BAU_TEMP_CHANGE("Expected BAU Temperature Change","ExpectedBAUTempChange", "BAU temperature change from preindustrial"),
        CO2_TARGET("CO2 Target","CO2Target", "target CO2eq Scenario 2 emissions"),
        RADIATIVE_FORCING("Total Radiative Forcing","RadiativeForcing","Radiative Forcing[\"Deterministic\"]"),
        CO2_RADIATIVE_FORCING("CO2 Radiative Forcing","CO2RadiativeForcing","CO2 Radiative Forcing[\"Deterministic\"]"),
        GLOBAL_CH4_EMISSIONS_CO2E("Global CO2e emissions from CH4","GlobalCH4EmissionsCO2e", "Global CO2eq emissions from CH4"),
        GLOBAL_N2O_EMISSIONS_CO2E("Global CO2e emissions from N2O","GlobalN2OEmissionsCO2e", "Global CO2eq emissions from N2O"),
        GLOBAL_FF_EMISSIONS_CO2E("Global CO2e FF emissions","GlobalCO2FFEmissions", "Global CO2 FF emissions"),
        YEAR("Year","Year", "Time"),
		;
		/*
		
		CO2_TARGET("CO2 Target","CO2Target"), 
		YEAR("Year","Year");
		*/
;
		String name;
		String internalName;
		String vensimName;
		G processor;

		private VensimVariable(String name,String internalName, String vensimName) {
			this.name = name;
			this.internalName = internalName;
			this.vensimName = vensimName;
			
		}

        private VensimVariable(String name,String internalName, String vensimName, G processor) {
            this(name,internalName,vensimName);
            this.processor = processor;
        }

        public float[] modify(float[] input) {
           return processor==null?input:processor.process(input);
        }

        public String getInternalName() {
			return internalName;
		}


		public String toString() {
			return name;
		}

	}

    public static interface F {
        public float[] process(SimulationResults vars, Variable... toprocess);
    }

    public static interface G {
        public float[] process(float[] f);
    }

    public static class Divider implements G {

        float denominator;

        public Divider(float denominator) {
          this.denominator=denominator;
        }

        public float[] process(float[] f) {
            for (int i=0;i<f.length;i++) {
                f[i] /= denominator;
            }
            return f;
        }
    }

    public static class Accumulator implements G {

        float denominator;

        public Accumulator() {
        }

        public float[] process(float[] f) {
            for (int i=0;i<f.length;i++) {
                f[i] = f[i]+(i==0?0:f[i-1]);
            }
            return f;
        }
    }


    public enum CompositeVariable implements Variable {

        CUMULATIVE_EMISSIONS_N2O_CH4_CO2("Cumulative emissions (CO2+N2O+CH4) rel. to 2005","CumulativeEmissionsRel2005",new
                Variable[]{VensimVariable.GLOBAL_CH4_EMISSIONS_CO2E,
                           VensimVariable.GLOBAL_N2O_EMISSIONS_CO2E,
                           VensimVariable.GLOBAL_FF_EMISSIONS_CO2E}, new F() {
            public float[] process(SimulationResults vars, Variable... toprocess) {
                float[] result = new float[vars.get(toprocess[0]).size()];
                int idx = 0;
                while (idx<result.length) {
                    for (Variable v:toprocess) {
                        result[idx]+=(Float)vars.get(v).get(idx).val;
                    }
                    result[idx]= (result[idx]-CUMULATIVE_EMISSIONS_2005)/ CUMULATIVE_EMISSIONS_2005;
                    idx++;
                }
                return result;
            }


        });

        String name;
		String internalName;
        Variable[] vars;
        F function;

        CompositeVariable(String name,String internalName, Variable[] vars, F function) {
            this.name = name;
            this.internalName = internalName;
            this.vars = vars;
            this.function = function;

        }

        public String getInternalName() {
			return internalName;
		}

         public float[] modify(float[] v) {
                return v;
            }

        public float[] process(SimulationResults map) {
            return function.process(map,vars);
        }


		public String toString() {
			return name;
		}


    }

	private Map<Variable, List<ScalarElement>> data = new HashMap<Variable, List<ScalarElement>>();

	private List<ScalarElement> getCollection(Variable v) {
		if (!data.containsKey(v)) {
			data.put(v, new ArrayList<ScalarElement>());
		}
		return data.get(v);
	}

	public void addDataPoint(Variable v, Number idx, Number val) {
		getCollection(v).add(new ScalarElement(val));
	}

	public void createIndexFor(Variable v, int min, int max) {
		List<ScalarElement> elts = getCollection(v);
		List<ScalarElement> idx = new ArrayList<ScalarElement>();
		int inc = (max - min) / (elts.size() - 1);
		for (ScalarElement elt : elts) {
			idx.add(new ScalarElement(min));
			min += inc;
		}
		//data.put(VensimVariable.YEAR, idx);

	}

	

	public void addDataPoints(Variable variable,String[] vals) {
		int idx = 0;
            float[] f = new float[vals.length];

            for (int i = 0;i<vals.length;i++) {
                f[i] = Float.parseFloat(vals[i]);
            }
            addDataPoints(variable,f);

		}
	
	   public void addDataPoints(Variable variable,float[] vals) {
	        int idx = 0;

	            for (float v:variable.modify(vals)) {
	                getCollection(variable).add(new ScalarElement(v));
	            }
	        }



	public List<ScalarElement> get(Variable v) {
		return data.get(v);
	}

	public Set<Variable> getPopulatedVariables() {
		return data.keySet();
	}

	public String toString() {
		String result = "";
		List<Variable> keys = new ArrayList<Variable>(data.keySet());
		Collections.sort(keys, new Comparator<Variable>() {

			public int compare(Variable o1, Variable o2) {
				return o1.getInternalName().compareTo(o2.getInternalName());
			}
		});
		for (Variable v : keys) {
			result += v + ":" + data.get(v) + "\n";
		}
		return result;
	}

	public static class ScalarElement {

		public Number val;

		public ScalarElement(Number val) {
			this.val = val;
		}

		public String toString() {
			return "[" + val + "]";
		}
	}

	public static class IndexedElement {

		public Number idx;
		public Number val;

		public IndexedElement(Number idx, Number val) {
			this.idx = idx;
			this.val = val;
		}

		public String toString() {
			return "[" + idx + "," + val + "]";
		}

	}

}