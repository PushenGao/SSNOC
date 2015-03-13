package edu.cmu.sv.ws.ssnoc.common.utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.TimerTask;

import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.MemoryCrumbPO;
import edu.cmu.sv.ws.ssnoc.dto.MemoryCrumb;

// Create a class extends with TimerTask
public class MemoryMeasurementTask extends TimerTask {

	Date now; // to display current time

	// Add your task here
	@Override
	public void run() {
		String[] cmd = {
				"/bin/sh",
				"-c",
				"ls /etc | grep release"
				};
		cmd[2] = "free -k | grep Mem | awk ' {print $3;} ' ";
		int used_mem = Integer.parseInt(executeCommand(cmd));
		cmd[2] = "free -k | grep Mem | awk ' {print $4;} ' ";
		int free_mem = Integer.parseInt(executeCommand(cmd));
		cmd[2] = "df -k | head -n 2 | tail -n 1 | awk ' {print $3;} ' ";
		int used_pers = Integer.parseInt(executeCommand(cmd));
		cmd[2] = "df -k | head -n 2 | tail -n 1 | awk ' {print $4;} ' ";
		int free_pers = Integer.parseInt(executeCommand(cmd));
		MemoryCrumb memcrumb = new MemoryCrumb();
		memcrumb.setCreatedAt(System.currentTimeMillis()/1000);
		memcrumb.setUsedVolatile(used_mem);
		memcrumb.setRemainingVolatile(free_mem);
		memcrumb.setUsedPersistent(used_pers);
		memcrumb.setRemainingPersistent(free_pers);
		MemoryCrumbPO crumbpo = ConverterUtils.convert(memcrumb);
		DAOFactory.getInstance().getMemoryCrumbDAO().save(crumbpo);
	}

	private String executeCommand(String[] command) {

		Process p;
		String line="";
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader =
                            new BufferedReader(new InputStreamReader(p.getInputStream()));

            line = reader.readLine();
 		} catch (Exception e) {
			e.printStackTrace();
		}

		return line;
	}
}