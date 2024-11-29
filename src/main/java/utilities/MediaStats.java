package utilities;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class MediaStats {
    static String callConnectionStatsJson = null;
    static String script = "return (async function() {" +
            "  const result = await App.getCallConnectionStats();" +
            "  return JSON.stringify(result);" +
            "})().then(arguments[arguments.length - 1]);";
    public static String initializeConnection(WebDriver driver){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        callConnectionStatsJson = (String) js.executeScript(script);
        return "App Stats:" +callConnectionStatsJson;
    }

    private static JSONObject getConnectionStats() {
        return new JSONObject(callConnectionStatsJson);
    }
    private static JSONObject getSFUUpstreamConnection() {
        return getConnectionStats().getJSONObject("sfuUpstreamConnection");
    }

    private static JSONObject getSFUConnection() {
        JSONObject jsonObject = getConnectionStats();
        JSONArray sfuDownstreamConnections = jsonObject.getJSONArray("sfuDownstreamConnections");
        return sfuDownstreamConnections.getJSONObject(0);
    }
    private static double getKey(JSONObject connection, String key) {
        return connection.has(key) ? connection.getDouble(key) : 0.00;
    }
    private static JSONObject getMCUConnection() {
        return getConnectionStats().getJSONObject("mcuConnection");
    }

    public static double getInboundBitRate(String mode) {
        return mode.equals("SFU") ? getKey(getSFUConnection(), "inbound_bitrate") :
                getKey(getMCUConnection(), "inbound_bitrate");
    }

    public static double getInboundFPS(String mode) {
        return mode.equals("SFU") ? getKey(getSFUConnection(), "inbound_fps") :
                getKey(getMCUConnection(), "inbound_fps");
    }

    public static double getInboundHeight(String mode) {
        return mode.equals("SFU") ? getKey(getSFUConnection(), "inbound_height") :
                getKey(getMCUConnection(), "inbound_height");
    }

    public static double getOutboundBitRate(String mode) {
        if (mode.equals("SFU")) {
            JSONObject sfuUpstreamConnection = getSFUUpstreamConnection();
            return getKey(sfuUpstreamConnection, "outbound_bitrate");
        } else if (mode.equals("MCU")) {
            return getKey(getMCUConnection(), "outbound_bitrate");
        }
        return 0.00;
    }

    public static double getOutboundFPS(String mode) {
        if (mode.equals("SFU")) {
            JSONObject sfuUpstreamConnection = getSFUUpstreamConnection();
            return getKey(sfuUpstreamConnection, "outbound_fps");
        } else if (mode.equals("MCU")) {
            return getKey(getMCUConnection(), "outbound_fps");
        }
        return 0.00;
    }

    public static double getOutboundHeight(String mode) {
        if (mode.equals("SFU")) {
            JSONObject sfuUpstreamConnection = getSFUUpstreamConnection();
            return getKey(sfuUpstreamConnection, "outbound_height");
        } else if (mode.equals("MCU")) {
            return getKey(getMCUConnection(), "outbound_height");
        }
        return 0.00;
    }
    public static double getOutboundWidth(String mode) {
        if (mode.equals("SFU")) {
            JSONObject sfuUpstreamConnection = getSFUUpstreamConnection();
            return getKey(sfuUpstreamConnection, "outbound_width");
        } else if (mode.equals("MCU")) {
            return getKey(getMCUConnection(), "outbound_width");
        }
        return 0.00;
    }

}
