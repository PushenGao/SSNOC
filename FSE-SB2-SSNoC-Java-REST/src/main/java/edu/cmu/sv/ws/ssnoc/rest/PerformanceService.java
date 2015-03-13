package edu.cmu.sv.ws.ssnoc.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;

@Path("/performance")
public class PerformanceService extends BaseService {
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/setup")
	public Response setup() {
		Log.enter();
		try {
			if (!DBUtils.isUsesTestDb()) {
				DBUtils.setUsesTestDb(true);
				DBUtils.clearDatabase();
				DBUtils.initializeDatabase();
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit();
		}
		return ok();
	}

	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/teardown")
	public Response tearDown() {
		Log.enter();
		try {
			DBUtils.setUsesTestDb(false);
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit();
		}
		return ok();
	}
}
