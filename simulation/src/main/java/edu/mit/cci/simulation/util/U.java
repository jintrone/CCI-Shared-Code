package edu.mit.cci.simulation.util;

import edu.mit.cci.simulation.model.DataType;
import edu.mit.cci.simulation.model.SimulationException;
import edu.mit.cci.simulation.model.Tuple;
import edu.mit.cci.simulation.model.TupleStatus;
import edu.mit.cci.simulation.model.Variable;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: jintrone
 * Date: 2/10/11
 * Time: 6:03 PM
 */
public class U {

    private static String VAL_SEP = ";";
    private static String VAR_SEP = "&";
    private static String VAR_VAL_SEP = "=";
    private static String NULL_VAL = "<null/>";

    private static Logger log = Logger.getLogger(U.class);



    public static String escape(Object[] vals) {
        StringBuffer buffer = new StringBuffer();
        if (vals == null || vals.length == 0) return "";
        for (Object val : vals) {
            try {
                if (val == null) {
                    buffer.append(NULL_VAL);
                } else if (val instanceof TupleStatus) {
                    buffer.append(((TupleStatus)val).encode());

                } else {
                     buffer.append(encode(val.toString()));
                }
                buffer.append(VAL_SEP);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return buffer.toString();
    }

    public static String encode(String val) throws UnsupportedEncodingException {
        return URLEncoder.encode(val, "UTF-8");
    }

    public static String[] unescape(String vals, Map<Integer, TupleStatus> status) {
        List<String> result = new ArrayList<String>();
        if (vals != null && !vals.trim().isEmpty()) {
            String[] str = vals.split(VAL_SEP);
            for (int i=0;i<str.length;i++) {
                String val = str[i];
                try {
                    if (val.equals(NULL_VAL)) {
                        result.add(null);
                    } else if (TupleStatus.decode(val)!=null) {
                        result.add(null);
                        if (status!=null) {
                            status.put(i,TupleStatus.decode(val));
                        }
                    } else result.add(URLDecoder.decode(val, "UTF-8"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toArray(new String[]{});
    }

    public static String[] unescapeNumeric(String vals, int precision) {
        List<String> result = new ArrayList<String>();
        if (vals != null && !vals.trim().isEmpty()) {
            for (String val : vals.split(VAL_SEP)) {
                try {
                    if (val.equals(NULL_VAL)) {
                        result.add(null);
                    } else {
                        String tmp = URLDecoder.decode(val, "UTF-8");
                        Double num = Double.valueOf(tmp);
                        Validation.notNull(num, "Parsed value from tuple");
                        result.add(String.format("%." + precision + "f", num));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toArray(new String[]{});
    }

    public static String executePost(String url, Map<String, String> params) throws IOException, MalformedURLException {
        new URL(url);
        HttpClient httpclient = new DefaultHttpClient();

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> ent : params.entrySet()) {
            formparams.add(new BasicNameValuePair(ent.getKey(), ent.getValue()));
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(entity);

        ResponseHandler<String> handler = new ResponseHandler<String>() {
            public String handleResponse(
                    HttpResponse response) throws ClientProtocolException, IOException {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity);
                } else {
                    return null;
                }
            }
        };


        String result = httpclient.execute(httppost, handler);
        return result;

    }

    public static Map<Variable, Tuple> convertToVarTupleMap(Map<String, String> params) throws SimulationException {
        Map<Variable, Tuple> result = new HashMap<Variable, Tuple>();
        //debugging
        List<Variable> vars = Variable.findAllVariables();
        for (Variable v : vars) {
            log.debug("Found " + v.getId());
            log.debug("Recall: " + Variable.findVariable(v.getId()));
        }
        //end debugging

        for (Map.Entry<String, String> ent : params.entrySet()) {
            Variable v = Variable.findVariable(Long.parseLong(ent.getKey()));
            if (v == null)
                throw new SimulationException("Variable for id:" + ent.getKey() + " could not be identified");
            Tuple t = new Tuple(v);
            t.setValues(unescape(ent.getValue(), null));

            result.put(v, t);
        }
        return result;
    }

    /**
     * @param input Presumes data formatted as a URL query parameter string with UTF-8 encoded values
     * @return A List of Tuples.  Tuples are not explicitly persisted here.
     */
    public static List<Tuple> parseVariableMap(String input) throws SimulationException {
        List<Tuple> result = new ArrayList<Tuple>();
        if (input.trim().isEmpty()) {
            throw (new SimulationException("Error parsing results from string: " + input));
        }
        String[] vars = input.split(VAR_SEP);
        for (String seg : vars) {
            String[] varval = seg.split(VAR_VAL_SEP);

            Variable v = Variable.findVariable(Long.parseLong(varval[0]));
            if (v == null) {
                throw new SimulationException("Could not identify variable in response: " + varval[0]);
            }
            Tuple t = new Tuple(v);
            t.setValue_(varval[1]);

            result.add(t);
        }
        return result;

    }

    public static String stringifyMap(Map<String, String> map) {
        StringBuilder builder = new StringBuilder();
        String vsep = "";
        for (Map.Entry<String, String> ent : map.entrySet()) {
            builder.append(vsep);
            builder.append(ent.getKey()).append(VAR_VAL_SEP).append(escape(new String[]{ent.getValue()}));
            vsep = VAR_SEP;
        }
        return builder.toString();
    }

    public static Map<String, String> mapifyString(String str) {
        String[] parts = str.split(VAR_SEP);
        Map<String, String> result = new HashMap<String, String>();
        if (!str.trim().isEmpty()) {
            for (String part : parts) {
                String[] vv = part.split(VAR_VAL_SEP);
                if (vv.length < 2) continue;
                String[] val = unescape(vv[1], null);
                if (val.length < 1) continue;
                if (val.length > 1) {
                    log.warn("Encountered an encoded array; return first value and dumping the rest");
                }
                result.put(vv[0], val[0]);
            }
        }
        return result;


    }


    public static Variable copy(Variable from, Variable to) {
        to.setName(from.getName());
        to.setDataType(from.getDataType());
        to.setMax_(from.getMax_());
        to.setMin_(from.getMin_());
        to.setDescription(from.getDescription());
        to.set_optionsRaw(from.get_optionsRaw());
        to.setPrecision_(from.getPrecision_());
        to.setArity(from.getArity());
        return to;
    }


    public static String createStringRepresentation(Map<Variable, Object[]> input) {
        StringBuilder builder = new StringBuilder();
        String vsep = "";
        for (Map.Entry<Variable, Object[]> ent : input.entrySet()) {
            builder.append(vsep);
            builder.append(ent.getKey().getId()).append(VAR_VAL_SEP).append(escape(ent.getValue()));
            vsep = VAR_SEP;
        }
        return builder.toString();
    }

    public static String createStringRepresentationFromTuple(Map<Variable, Tuple> input) {
        StringBuilder builder = new StringBuilder();
        String vsep = "";
        for (Map.Entry<Variable, Tuple> ent : input.entrySet()) {
            builder.append(vsep);
            builder.append(ent.getKey().getId()).append(VAR_VAL_SEP).append(ent.getValue().getValue_());
            vsep = VAR_SEP;
        }
        return builder.toString();
    }

    public static void copyRange(Tuple from, Tuple to, int fromidx, int toidx) throws SimulationValidationException {
        String[] vals = Arrays.copyOfRange(from.getValues(), fromidx, toidx);
        to.setValues(vals);

    }

    public static void join(Tuple first, Tuple second) throws SimulationValidationException {
        String sep = first.getValue_().isEmpty() || first.getValue_().endsWith(";") ? "" : ";";
        first.setValue_(first.getValue_() + sep + second.getValue_());
    }

    public static String join(String first, String second) throws SimulationValidationException {
        String sep = first.isEmpty() || first.endsWith(";") ? "" : ";";
        return first+sep+second;
    }

    public static boolean equals(Object one, Object two) {
        return one == null ? two == null : one.equals(two);
    }

    public static String getCellValueAsString(HSSFSheet worksheet, int rowCounter, int colCounter,
                                              String defaultValue) throws SimulationException {
        Validation.notNull(worksheet, "worksheet");

        Row row = worksheet.getRow(rowCounter);
        if (row == null) {
            return defaultValue;
        }
        Cell cell = row.getCell(colCounter);
        if (cell == null) {
            return defaultValue;
        }
        // cell should be either blank,string, numeric or formula
        if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return defaultValue;
        } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return cell.getNumericCellValue() + "";
        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_STRING) {
                return cell.getStringCellValue();
            } else if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC) {
                return cell.getNumericCellValue() + "";
            } else if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_ERROR) {
                throw new SimulationComputationException("Error computing fomula");
            } else {
                throw new SimulationException("invalid formula type with cached type of  "
                        + cell.getCachedFormulaResultType() + " for row of " + rowCounter + " and col of " + colCounter);
            }
        } else {
            throw new SimulationException("invalid type with type of  " + cell.getCellType() + " for rowr of "
                    + rowCounter + " and col of " + colCounter);
        }

    }

    public static ConcreteSerializableCollection wrap(Collection c) {
        return new ConcreteSerializableCollection(c);
    }


    public static Map<Integer, TupleStatus[]> parseOrderedMap(String str) {
        String[] parts = str.split(VAR_SEP);
        Map<Integer, TupleStatus[]> result = new HashMap<Integer, TupleStatus[]>();
        if (!str.trim().isEmpty()) {
            for (String part : parts) {
                String[] vv = part.split(VAR_VAL_SEP);
                if (vv.length < 2) continue;
                String[] val = unescape(vv[1], null);
                if (val.length < 1) continue;
                TupleStatus[] statuses = new TupleStatus[val.length];
                for (int i = 0; i < val.length; i++) {
                    statuses[i] = TupleStatus.valueOf(val[i]);
                }
                result.put(Integer.parseInt(vv[0]), statuses);
            }
        }
        return result;
    }

    public static String updateStringMap(String key, String val, String map) {
        if (map == null) map = "";
        String value = key + VAR_VAL_SEP;
        try {
            value+=encode(val);
        } catch (UnsupportedEncodingException e) {
            log.warn("Unsupported encoding "+e.getMessage()+" falling back to raw string");
            value+=val;
        }
        if (map.isEmpty()) {
            return value;
        } else {
            Pattern p = Pattern.compile("(" + VAR_SEP + "|^)" + key +  VAR_VAL_SEP + "[^" + VAR_SEP + "]+");
            Matcher m = p.matcher(map);
            if (m.find()) {
                return m.replaceAll("$1"+value);
            }
        }
        return map + VAR_SEP + value;
    }

    public static String format(Variable var, String value)
    {
        if (var.getDataType() == DataType.NUM && value !=null) {
            Double num = Double.parseDouble(value);
            return String.format("%." + var.getPrecision_() + "f", num);
        } else {
            return value;
        }
    }
}
