package utilities;

import core.ExtentManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

public class MediaStats {
    static String callConnectionStatsJson = null;
    static String script = "return (async function() {" +
            "  const result = await App.getCallConnectionStats();" +
            "  return JSON.stringify(result);" +
            "})().then(arguments[arguments.length - 1]);";


    public static void main(String[] args) {
        String jsonString = "{...}"; // Replace with the actual JSON string

        // Call the user-defined function
        Map<String, Object> extractedValues = extractMediaStreamInfo(jsonString);

        // Print the extracted values with precise formatting
        extractedValues.forEach((key, value) -> {
            if (value instanceof Double) {
                System.out.printf("%s: %.10f%n", key, (Double) value); // Print double with 10 decimal places
            } else {
                System.out.println(key + ": " + value);
            }
        });
    }

    public static String getCallConnectionStatsJson(WebDriver driver){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        callConnectionStatsJson = (String) js.executeScript(script);
        return callConnectionStatsJson;
    }

    public static Map<String, Object> extractMediaStreamInfo(String jsonString) {
        Map<String, Object> mediaStreamInfo = new HashMap<>();

        JSONObject jsonObject = new JSONObject(jsonString);

        // Extract outbound values from sfuUpstreamConnection
        if (jsonObject.has("sfuUpstreamConnection")) {
            JSONObject upstreamConnection = jsonObject.getJSONObject("sfuUpstreamConnection");
            extractOutboundValues(upstreamConnection, mediaStreamInfo);
        }

        // Extract inbound values from sfuDownstreamConnections
        if (jsonObject.has("sfuDownstreamConnections")) {
            JSONArray downstreamConnections = jsonObject.getJSONArray("sfuDownstreamConnections");
            if (downstreamConnections.length() > 0) {
                JSONObject downstreamConnection = downstreamConnections.getJSONObject(0);
                extractInboundValues(downstreamConnection, mediaStreamInfo);
            }
        }

        // Extract values from mcuConnection
        if (jsonObject.has("mcuConnection")) {
            JSONObject mcuConnection = jsonObject.getJSONObject("mcuConnection");
            extractInboundValues(mcuConnection, mediaStreamInfo);
            extractOutboundValues(mcuConnection, mediaStreamInfo);
        }

        return mediaStreamInfo;
    }

    private static void extractOutboundValues(JSONObject connection, Map<String, Object> mediaStreamInfo) {
        if (connection.has("outbound_fps")) {
            mediaStreamInfo.put("outbound_fps", connection.getInt("outbound_fps"));
        }
        if (connection.has("outbound_width")) {
            mediaStreamInfo.put("outbound_width", connection.getInt("outbound_width"));
        }
        if (connection.has("outbound_height")) {
            mediaStreamInfo.put("outbound_height", connection.getInt("outbound_height"));
        }
        if (connection.has("outbound_bitrate")) {
            // Extract precise double
            double outboundBitrate = connection.getDouble("outbound_bitrate");
            mediaStreamInfo.put("outbound_bitrate", outboundBitrate);
        }
    }

    private static void extractInboundValues(JSONObject connection, Map<String, Object> mediaStreamInfo) {
        if (connection.has("inbound_fps")) {
            mediaStreamInfo.put("inbound_fps", connection.getInt("inbound_fps"));
        }
        if (connection.has("inbound_width")) {
            mediaStreamInfo.put("inbound_width", connection.getInt("inbound_width"));
        }
        if (connection.has("inbound_height")) {
            mediaStreamInfo.put("inbound_height", connection.getInt("inbound_height"));
        }
        if (connection.has("inbound_bitrate")) {
            // Extract precise double
            double inboundBitrate = connection.getDouble("inbound_bitrate");
            mediaStreamInfo.put("inbound_bitrate", inboundBitrate);
        }
    }
    private static double toDouble(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof Double) {
            return (Double) value;
        } else {
            throw new IllegalArgumentException("Value is neither Integer nor Double: " + value);
        }
    }

    public static double parseExpectedValue(String value) {
        return Double.parseDouble(value.split(" ")[1]);
    }

    public static String parseOperator(String value) {
        return value.split(" ")[0];
    }

    public static void validateAndLog(String fieldName, double actualValue, double expectedValue, String operator) {
        boolean isValid;
        switch (operator) {
            case "<":
                isValid = actualValue < expectedValue;
                break;
            case ">":
                isValid = actualValue > expectedValue;
                break;
            case "=":
                isValid = actualValue == expectedValue;
                break;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }

        // Log result
        String result = String.format(
                "%s: %s (Expected: %s %.2f, Actual: %.2f)",
                fieldName, isValid ? "PASS" : "FAIL", operator, expectedValue, actualValue
        );
        System.out.println(result);
        ExtentManager.getTest().info(result);

        // Assertion
        assert isValid : fieldName + " validation failed! Expected: " + operator + " " + expectedValue + ", Actual: " + actualValue;
    }
}

