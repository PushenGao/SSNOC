package edu.cmu.sv.ws.ssnoc.rest;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.common.utils.MemoryMeasurementTask;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.MemoryCrumbPO;
import edu.cmu.sv.ws.ssnoc.dto.MemoryCrumb;

@Path("/memory")
public class MemoryService extends BaseService {

	static final int DEFAULT_INTERVAL = 1;
	static Timer time;

	@POST
	@Path("/start")
	public Response start() {
		DAOFactory.getInstance().getMemoryCrumbDAO().clear();
		time = new Timer();
        time.schedule(new MemoryMeasurementTask(), 0, 1000 * 60);
		return ok();
	}


	@POST
	@Path("/stop")
	public Response stop() {
		time.cancel();
		return ok();
	}

	@DELETE
	public Response delete() {
		DAOFactory.getInstance().getMemoryCrumbDAO().clear();
		return ok();
	}

	@GET
	public List<MemoryCrumb> get() {
		return getTimeWindow(DEFAULT_INTERVAL);
	}

	@GET
	@Path("/interval/{timeWindowInHours}")
	public List<MemoryCrumb> getTimeWindow(@PathParam("timeWindowInHours") int hours) {
			List<MemoryCrumbPO> po = new ArrayList<MemoryCrumbPO>();
			List<MemoryCrumb> dto = new ArrayList<MemoryCrumb>();
			try {
				po = DAOFactory.getInstance().getMemoryCrumbDAO().findByInterval(hours);
				for(MemoryCrumbPO mpo : po){
					MemoryCrumb mdto = ConverterUtils.convert(mpo);
					dto.add(mdto);
				}
			} catch (Exception e) {
				handleException(e);
			} finally {
				Log.exit(dto);
			}
			return dto;
	}
}
