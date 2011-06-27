package to.crap.utils.FanThrottleSMC;

/*
 * FanThrottleSMC
 * @author: Johannes Heck
 * http://crap.to
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMCParser {
	private Pattern pattern = Pattern.compile("(....)  \\[(....)\\]  (?:\\d+ )?\\(bytes (.*)\\)$");
	private String[] keys = { "F0Ac", "F1Ac", "F2Ac", "F3Ac", "TA0P", "TC0D", "TC1D", "TC2D", "TC3D", "Th0H", "TC0H", "TC0P", "TN0P", "TG0D", "TG0H", "TG0P", "FGC0", "FRC0", "FRC1", "FRC2", "FRC3", "VC0C", "VC1C", "VC2C", "VC3C", "VM0R", "Tm0P", "F0Mx", "F1Mx" };
	private String[] desc = { "Fan 0 RPM", "Fan 1 RPM", "Fan 2 RPM", "Fan 3 RPM", "Ambient temp", "CPU 0 die temp", "CPU 1 die temp", "CPU 2 die temp", "CPU 3 die temp", "CPU heatsink temp", "CPU heatsink temp", "CPU proximity temp", "Northbridge temp", "GPU die temp", "GPU board temp", "GPU proximity temp", "Non Apple GPU frequency", "CPU 0 frequency", "CPU 1 frequency", "CPU 2 frequency", "CPU 3 frequency", "CPU 0 Vcore voltage", "CPU 1 Vcore voltage", "CPU 2 Vcore voltage", "CPU 3 Vcore voltage", "Memory voltage", "DIMM temp", "Fan 0 Max Speed" , "Fan 1 Max Speed" };
	private String path;
	
	public SMCParser(String path) {
		this.path = path;
	}
	
	public String[] getKeys() { return keys; }
	public String[] getDesc() { return desc; }
	
	private boolean keyExists(String k) {
		for(int i = 0; i < this.keys.length; i++) 
			if(k.equals(this.keys[i]))
				return true;
		return false;
	}
	
	private String parseConsole(Process p) throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = input.readLine();
		Matcher matcher = this.pattern.matcher(line);
		if(matcher.find()) {
			return matcher.group(3).replace(' ', '\0');
		}
		return null;
	}
	
	public String getValue(String k) throws IOException {
		if(keyExists(k)) {
			Process p = Runtime.getRuntime().exec(path + " -k " + k + " -r");
			return parseConsole(p);
		}
		return "Key doesn't exist";
	}
	
	public void setValue(String k, String value) throws IOException {
		if(!keyExists(k)) return;
		Runtime.getRuntime().exec(path + " -k " + k + " -w " + value);
	}
	
	public void setFanSpeed(String value) throws IOException {
		System.out.println(value);
		setValue("F0Mx", value);
		setValue("F1Mx", value);
	}
}
