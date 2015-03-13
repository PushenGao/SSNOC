package edu.cmu.sv.ws.ssnoc.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IStatusCrumbDAO;
import edu.cmu.sv.ws.ssnoc.data.dao.IUserDAO;
import edu.cmu.sv.ws.ssnoc.data.po.StatusCrumbPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.StatusCrumb;

@Path("/status")
public class StatusService extends BaseService {
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/{userName}")
	public Response updateStatus(@PathParam("userName") String userName, StatusCrumb statusCrumb) {
		Log.enter(statusCrumb.getStatusCode());
		StatusCrumb resp = new StatusCrumb();
		IUserDAO userDao = DAOFactory.getInstance().getUserDAO();
		UserPO existingUser = userDao.findByName(userName);
		if (existingUser != null) {
			statusCrumb.setUserName(userName);
			userDao.updateUserStatus(statusCrumb.getStatusCode(), userName);
			StatusCrumbPO po = ConverterUtils.convert(statusCrumb);
			IStatusCrumbDAO scDao = DAOFactory.getInstance().getStatusCrumbDAO();
			scDao.save(po);
			resp = ConverterUtils.convert(po);
		} else {
			Log.warn("Username doesn't exist.");
			return ok(resp);
		}
		return created(resp);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/{crumbID}")
	public StatusCrumb loadCrumb(@PathParam("crumbID") long crumbId) {
		Log.enter(crumbId);

		StatusCrumb statusCrumb = null;
		try {
			StatusCrumbPO statusCrumbPO = DAOFactory.getInstance().getStatusCrumbDAO().findById(crumbId);
			statusCrumb = ConverterUtils.convert(statusCrumbPO);
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(statusCrumb);
		}

		return statusCrumb;
	}
}