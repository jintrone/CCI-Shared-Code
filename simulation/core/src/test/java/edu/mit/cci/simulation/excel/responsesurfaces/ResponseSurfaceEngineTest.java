package edu.mit.cci.simulation.excel.responsesurfaces;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * User: jintrone
 * Date: 4/7/11
 * Time: 9:38 AM
 */
public class ResponseSurfaceEngineTest {
    @Test
	public void testInterpolation(){
		IAMResponseSurfaceEngine fee = new IAMResponseSurfaceEngine();
		Integer[] cols = {2000, 2005, 2020,2030};

		double scenvalues2[][] = { { 13.685, 15.62, 15.41, 13.34 }, { 13.685, 14.92, 14.24,12.1}, { 13.685, 14.14, 12.14, 8.03 },{13.685, 14.91,14.24,12.1},{13.685, 14.54, 13.48, 10.49} };
		double outpvalues2[][] = { { 36.46, 42.6227, 48.051, 53.713 }, { 36.46, 42.5951544, 48.000024 ,53.587318 }, { 36.45975, 42.44516, 47.7097, 53.110855 },{36.45975,42.5838342,47.998608,53.60994},{36.45975,42.5031776,47.89099,53.324344} };
		DoubleMatrix2D scen2 = new DenseDoubleMatrix2D(scenvalues2);
		DoubleMatrix2D outp2 = new DenseDoubleMatrix2D(outpvalues2);
		SimpleResponseSurface<Float,Integer> surf = fee.generateResponseSurface(2000, cols, scen2, outp2);

		Double[][] testData = {
				{.033248,.062477, -.00583, .046563},
				{-.1129,-.01498, -.00275,.038523 },
				{-.41323,-.23347, -.00208,.022111},
				{.062477, .089514,-.00729, .069983},
				{-.01498, .040555, -.00272, .040594},
				{-.23347, -.11582, .002895, .043405},
				{.089514, .090245, -.03356, .363419},
				{.040555,.083303, -.00159, .012581},
				{-.11582, -.07052, .000593,.02353},
				{.090245,.141396, -.00211, .014926},
				{.083303, .12605, -.00159, .012581},
				{-.07052, -.02521, .000593, .02353}
		};
		int t = 0;
		List<Slice<Float,Integer>> slices = surf.getSlices();
		Assert.assertEquals(4,slices.size());
		for(int i = 0; i < slices.size(); i++){
			Slice<Float,Integer> slice = slices.get(i);
			Assert.assertEquals(3,slice.size());
		    for(int j = 0; j < slice.size(); j++){
		    	SliceSegment<Float,Integer> seg = slice.get(j);
		    	assertAlmostEqual(testData[t][0],seg.fromCriterion,1e-2);
		    	assertAlmostEqual(testData[t][1],seg.toCriterion,1e-2);
		    	assertAlmostEqual(testData[t][2],seg.function.getParam(0),1e-2);
		    	assertAlmostEqual(testData[t][3],seg.function.getParam(1),1e-2);
		    	t++;
		    }
		}

	}

	private void assertAlmostEqual(double expected, double actual, double tol){
    	Assert.assertTrue("expected " + expected + " received " + actual,
                Math.abs(expected - actual) < tol);


	}
}
