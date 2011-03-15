package edu.mit.cci.simulation.model;

import edu.mit.cci.simulation.excel.server.ExcelRunnerStrategy;
import edu.mit.cci.simulation.excel.server.ExcelSimulation;
import edu.mit.cci.simulation.excel.server.ExcelVariable;
import edu.mit.cci.simulation.util.U;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * User: jintrone
 * Date: 2/28/11
 * Time: 7:54 AM
 */

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Component
public class SimulationMockFactory {

    VariableDataOnDemand vdod = new VariableDataOnDemand();


    DefaultSimulationDataOnDemand dod = new DefaultSimulationDataOnDemand();

    MappedSimulationDataOnDemand mdod = new MappedSimulationDataOnDemand();


    public DefaultSimulation getScalarSimulation(int simId, final int output, int varId) {
        DefaultSimulation sim = dod.getSpecificDefaultSimulation(simId);

        Variable vin = vdod.getSpecificVariable(varId);
        vin.setArity(1);
        vin.setDataType(DataType.NUM);
        vin.setPrecision_(0);
        vin.setMin_(0d);
        vin.setMax_(10d);

        Variable vout = vdod.getSpecificVariable(varId + 1);
        vout.setArity(1);
        vout.setDataType(DataType.NUM);
        vout.setPrecision_(0);
        vout.setMin_(0d);
        vout.setMax_(10d);

        sim.getInputs().add(vin);
        sim.getOutputs().add(vout);

        return new DelegatingSim(sim) {

            {
                delegate.setRunStrategy(new RunStrategy() {

                    @Override
                    public String run(String url, Map<String, String> params) throws SimulationException {
                        Map<Variable, Object[]> data = new HashMap<Variable, Object[]>();
                        Variable v = getOutputs().iterator().next();
                        data.put(v, new String[]{output + ""});
                        return U.createStringRepresentation(data);
                    }
                });
            }
        };

    }

    public static DefaultSimulation configurePassThruStrategy(DefaultSimulation sim) {
        return new DelegatingSim(sim) {

            {
                delegate.setRunStrategy(new RunStrategy() {

                    @Override
                    public String run(String url, Map<String, String> params) throws SimulationException {
                        Map<Variable, Object[]> data = new HashMap<Variable, Object[]>();
                        String[] outputvals = new String[params.size()];
                        int i = 0;
                        for (String s:params.values()) {
                            outputvals[i++] = U.unescape(s, null)[0];
                        }
                        for (Variable v : getOutputs()) {
                            String[] output = new String[v.getArity()];
                            for (i = 0;i<v.getArity();i++) {
                                output[i] = outputvals[i%outputvals.length];
                                data.put(v, output);
                            }
                        }
                        return U.createStringRepresentation(data);
                    }
                });
            }
        };
    }

    public MappedSimulation getMappedSimulation(int simid, DefaultSimulation embeddedscalar, int replication, int samplingFreq, ManyToOneMapping type) {
        MappedSimulation sim = mdod.getNewTransientMappedSimulation(simid);
        sim.setReplication(replication);
        sim.setExecutorSimulation(embeddedscalar);
        sim.setSamplingFrequency(samplingFreq);
        sim.setManyToOne(type);
        return sim;
    }

    public Variable getVariable(int arity, String name, DataType type, int precision) {
        Variable v_in = new Variable();
        v_in.setArity(arity);
        v_in.setName(name);
        v_in.setDataType(type);
        v_in.setPrecision_(precision);
        v_in.setMin_(0d);
        v_in.setMax_(10d);
        v_in.persist();
        return v_in;
    }

    public DefaultSimulation getExcelBasedSimulation(byte[] file) {
        DefaultSimulation simulation = new DefaultSimulation();
        simulation.setSimulationVersion(1l);
        simulation.persist();
        Variable dateinput = new Variable();
        dateinput.setName("Year");
        dateinput.setDataType(DataType.NUM);
        dateinput.setArity(11);
        dateinput.setPrecision_(0);
        dateinput.persist();
        dateinput.setMin_(0d);
        dateinput.setMax_(4000d);

        Variable emissionsinput = new Variable();
        emissionsinput.setName("Emissions");
        emissionsinput.setDataType(DataType.NUM);
        emissionsinput.setArity(11);
        emissionsinput.setPrecision_(2);
        emissionsinput.persist();
        emissionsinput.setMin_(0d);
        emissionsinput.setMax_(5d);


        Variable gdpOutput = new Variable();
        gdpOutput.setDataType(DataType.NUM);
        gdpOutput.setName("% GDP");
        gdpOutput.setArity(11);
        gdpOutput.setPrecision_(2);
        gdpOutput.persist();
        gdpOutput.setMin_(-100d);
        gdpOutput.setMax_(100d);

        simulation.getInputs().add(dateinput);
        simulation.getInputs().add(emissionsinput);
        simulation.getOutputs().add(gdpOutput);

        ExcelSimulation esim = new ExcelSimulation();
        esim.setSimulation(simulation);

        ExcelVariable evdatein = new ExcelVariable();
        evdatein.setSimulationVariable(dateinput);
        evdatein.setCellRange("A3:A13");
        evdatein.setWorksheetName("Inputs_Outputs");
        evdatein.setExcelSimulation(esim);

        ExcelVariable evemissionsin = new ExcelVariable();
        evemissionsin.setSimulationVariable(emissionsinput);
        evemissionsin.setCellRange("B3:B13");
        evemissionsin.setWorksheetName("Inputs_Outputs");
        evemissionsin.setExcelSimulation(esim);

        ExcelVariable evgdpout = new ExcelVariable();
        evgdpout.setSimulationVariable(gdpOutput);
        evgdpout.setCellRange("D3:D13");
        evgdpout.setWorksheetName("Inputs_Outputs");
        evgdpout.setExcelSimulation(esim);

        esim.getInputs().add(evdatein);
        esim.getInputs().add(evemissionsin);
        esim.getOutputs().add(evgdpout);

        esim.setFile(file);

        esim.persist();
        simulation.setUrl("/excel/"+esim.getId());

        simulation.setRunStrategy(new ExcelRunnerStrategy(simulation));
        simulation.persist();

        return simulation;

    }





}
