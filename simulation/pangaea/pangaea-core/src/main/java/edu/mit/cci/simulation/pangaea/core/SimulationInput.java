package edu.mit.cci.simulation.pangaea.core;

import java.util.HashMap;
import java.util.Map;

public class SimulationInput {


	private static final Map<String, InputVariable> nameVarMap = new HashMap<String, InputVariable>();

	public enum InputVariable {

		DEVELOPED_FF_CHANGE("Developed countries fossil fuel emissions","Pct change in Developed FF emissions",-40),
		DEVELOPED_FF_START("Developed countries fossil fuel emissions start reduction year","Developed start year",2025),
		DEVELOPED_FF_TARGET("Developed countries fossil fuel emissions target reduction year","Developed target year",2050),
		DEVELOPED_REFERENCE_YEAR("Developed countries reference year", "Developed reference year", 2005),

		
		DEVELOPINGA_FF_CHANGE("Developing countries fossil fuel emissions","Pct change in Developing A FF emissions",-40),
		DEVELOPINGA_FF_START("Developing countries fossil fuel emissions start reduction year","Developing A start year",2025),
		DEVELOPINGA_FF_TARGET("Developing countries fossil fuel emissions target reduction year","Developing A target year",2050),
        DEVELOPINGA_REFERENCE_YEAR("Developing A countries reference year", "Developing A reference year", 2005),

		DEVELOPINGB_FF_CHANGE("Rest of the world fossel fuel emissions","Pct change in Developing B FF emissions",-40),
		DEVELOPINGB_FF_START("Rest of the world fossil fuel emissions start reduction year","Developing B start year",2025),
		DEVELOPINGB_FF_TARGET("Rest of the world fossil fuel emissions target reduction year","Developing B target year",2050),
        DEVELOPINGB_REFERENCE_YEAR("Developing B countries reference year", "Developing B reference year", 2005),
		
		

		DEFORESTATION("Deforestation","Global land use change",1.0),
		AFFORESTATION("Afforestation","Target Sequestration",0.0);


		String name;
		String internalName;
		Number defvalue;


		private InputVariable(String name, String internalName, Number val) {
			this.name = name;
			this.internalName = internalName;
			this.defvalue = val;
			nameVarMap.put(internalName, this);
		}

		public String internalName() {
			return internalName;
		}

		public Number defaultValue() {
			return defvalue;
		}

		public String toString() {
			return name;
		}

	}


	private Map<InputVariable,Number> data = new HashMap<InputVariable,Number>();

	public SimulationInput() {
		for (InputVariable v: InputVariable.values()) {
			setVariable(v,null);
		}
	}


	public void setVariable(InputVariable v, Number val) {
		data.put(v, val);
	}

	public Number getValue(InputVariable v) {
		return data.get(v)==null?v.defvalue:data.get(v);
	}

	public Map<InputVariable,Number> getAllVariables() {
		return data;
	}


}