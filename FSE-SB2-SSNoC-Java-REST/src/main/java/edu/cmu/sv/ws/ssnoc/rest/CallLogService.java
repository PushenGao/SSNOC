package edu.cmu.sv.ws.ssnoc.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.ICallLogDAO;

@Path("/calllog")
public class CallLogService extends BaseService {
	@POST
	@Path("/{user1}/{user2}")
	public Response recordCall(@PathParam("user1") String user1, @PathParam("user2") String user2) {
		ICallLogDAO dao = DAOFactory.getInstance().getCallLogDAO();
		dao.recordCall(user1, user2);
		dao.recordCall(user2, user1);
		return ok();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{user}")
	public Response mostFrequentContact(@PathParam("user") String user) {
		ICallLogDAO dao = DAOFactory.getInstance().getCallLogDAO();
		List<String> list = dao.getMostFrequentContacts(user);
		return ok(new Gson().toJson(list));
	}
}
