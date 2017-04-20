package org.fao.bluebridge;

import java.util.HashMap;
import java.util.Map;

import net.fusejna.FuseException;

public class Main {

	private final static String ENDPOINT = "https://workspace-repository.d4science.org/home-library-webapp/";
	private final String USERNAME = "enrico.anello";
	private final String TOKEN = "ae2d0528-4d19-4fb3-bd50-b1dd61c77e14-843339462";
	
	public static void main(String... args) {
		Map<String, String> arguments = new HashMap<String, String>();
		for (String arg : args) {
			if (arg.contains("=")) {
				arguments.put(arg.split("=")[0].toLowerCase(), arg.split("=")[1]);
			} else if (arg.toLowerCase().equals("-h") || arg.toLowerCase().equals("--help")) {
				printHelp();
				System.exit(0);
			}
		}
		Boolean error = false;
		if (!arguments.containsKey("mountpoint")) {
			System.out.println("Error: mount point must be defined");
			error = true;
		}
		if (!arguments.containsKey("username")) {
			System.out.println("Error: iMarine username must be defined");
			error = true;
		}
		if (!arguments.containsKey("token")) {
			System.out.println("Error: iMarine token must be defined");
			error = true;
		}
		if (error) {
			System.out.println("");
			printHelp();
			System.exit(0);
		}
		String endpoint = ENDPOINT;
		if (arguments.containsKey("endpoint")) {
			endpoint = arguments.get("endpoint");
		}
		WorkspaceFs workspaceFs_ = new WorkspaceFs(arguments.get("username"), arguments.get("token"), endpoint);
		try {
			workspaceFs_.mount(arguments.get("mountpoint"));
		} catch (FuseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void printHelp() {
		System.out.println("Usage:");
		System.out.println("  mountpoint=<mount point path> {mandatory}");
		System.out.println("  username=<iMarine username> {mandatory}");
		System.out.println("  token=<iMarine token> {mandatory}");
		System.out.println("  endpoint=<iMarine workspace webservice endpoint> {optional}");
	}

}
