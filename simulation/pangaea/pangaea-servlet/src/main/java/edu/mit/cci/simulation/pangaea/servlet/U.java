package edu.mit.cci.simulation.pangaea.servlet;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

/**
 * User: jintrone
 * Date: 3/15/11
 * Time: 9:02 PM
 */
public class U {

    private static String VAL_SEP = ";";
    private static String VAR_SEP = "&";
    private static String VAR_VAL_SEP = "=";
    private static String NULL_VAL = "<null/>";

    public static String stringify(Map<String, Object> map) {
        StringBuilder builder = new StringBuilder();
        String vsep = "";
        for (Map.Entry<String, Object> ent : map.entrySet()) {
            builder.append(vsep);
            Object o = ent.getValue();
            if (o instanceof String[]) {
                builder.append(ent.getKey()).append(VAR_VAL_SEP).append(escape((String[]) ent.getValue()));
            } else if (o == null) {
                builder.append(ent.getKey()).append(VAR_VAL_SEP).append(NULL_VAL);
            } else {
                builder.append(ent.getKey()).append(VAR_VAL_SEP).append(escape(new String[]{ent.getValue().toString()}));
            }

            vsep = VAR_SEP;
        }
        return builder.toString();
    }

    public static String encode(String val) throws UnsupportedEncodingException {
        return URLEncoder.encode(val, "UTF-8");
    }

    public static String decode(String val) throws UnsupportedEncodingException {
        return URLDecoder.decode(val, "UTF-8");
    }

    public static String escape(Object[] vals) {
        StringBuffer buffer = new StringBuffer();
        if (vals == null || vals.length == 0) return "";
        String vsep = "";
        for (int i = 0; i < vals.length; i++) {
            Object val = vals[i];
            buffer.append(vsep);
            try {
                if (val == null) {
                    buffer.append(NULL_VAL);
                } else {
                    buffer.append(encode(val.toString()));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            vsep = VAL_SEP;
        }
        return buffer.toString();
    }
}
