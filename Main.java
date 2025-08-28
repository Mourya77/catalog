import java.math.BigInteger;
import java.util.*;
import java.nio.file.*;  // for reading file
import java.io.*;        // for IOException

public class ParseJsonManual {
    public static void main(String[] args) throws IOException {
        // ---- Step 1: Read JSON from file ----
        String json = new String(Files.readAllBytes(Paths.get("input.json")));

        // ---- Step 2: Parse n and k ----
        int n = Integer.parseInt(json.split("\"n\":")[1].split(",")[0].trim());
        int k = Integer.parseInt(json.split("\"k\":")[1].split("}")[0].trim());

        System.out.println("n = " + n + ", k = " + k);

        // ---- Step 3: Extract x, base, value ----
        List<BigInteger> xs = new ArrayList<>();
        List<BigInteger> ys = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            String key = "\"" + i + "\": {";
            int idx = json.indexOf(key);
            if (idx != -1) {
                int start = idx + key.length();
                int end = json.indexOf("}", start);
                String content = json.substring(start, end);

                String baseField = "\"base\": \"";
                int baseIdx = content.indexOf(baseField) + baseField.length();
                int baseEnd = content.indexOf("\"", baseIdx);
                String base = content.substring(baseIdx, baseEnd);

                String valueField = "\"value\": \"";
                int valueIdx = content.indexOf(valueField) + valueField.length();
                int valueEnd = content.indexOf("\"", valueIdx);
                String value = content.substring(valueIdx, valueEnd);

                BigInteger xi = BigInteger.valueOf(i); // x = key number
                BigInteger yi = new BigInteger(value, Integer.parseInt(base)); // decode

                xs.add(xi);
                ys.add(yi);

                System.out.println("Point (" + xi + "," + yi + ")");
            }
        }

        // ---- Step 4: Interpolation at x=0 ----
        BigInteger c = lagrangeInterpolation(xs, ys, k, BigInteger.ZERO);
        System.out.println("Constant term c = " + c);
    }

    // Lagrange interpolation for first k points
    public static BigInteger lagrangeInterpolation(List<BigInteger> x, List<BigInteger> y, int k, BigInteger xEval) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {
            BigInteger term = y.get(i);
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    BigInteger num = xEval.subtract(x.get(j));
                    BigInteger den = x.get(i).subtract(x.get(j));
                    term = term.multiply(num).divide(den); // exact division
                }
            }
            result = result.add(term);
        }
        return result;
    }
}
