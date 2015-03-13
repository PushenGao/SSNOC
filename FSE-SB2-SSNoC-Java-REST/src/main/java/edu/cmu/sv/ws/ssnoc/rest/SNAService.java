package edu.cmu.sv.ws.ssnoc.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.BronKerboschCliqueFinder;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import com.google.gson.Gson;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;

@Path("/usergroups")
public class SNAService extends BaseService {

	public void cluster(String start, String end, List<List<String>> list) {
		DAOFactory dao = DAOFactory.getInstance();
		// get the list of user names and add them as vertices
		UndirectedGraph<String, DefaultEdge> graph = new SimpleGraph<>(
				DefaultEdge.class);
		List<UserPO> users = dao.getUserDAO().loadUsers();
		for (UserPO po : users) {
			graph.addVertex(po.getUserName());
		}
		// build the private message graph
		List<MessagePO> messages = dao.getMessageDAO().getPeriodMessage(start,
				end);
		Map<String, Set<String>> pmGraph = new HashMap<String, Set<String>>();
		for (MessagePO po : messages) {
			String author = po.getAuthor();
			String target = po.getTarget();
			if (!pmGraph.containsKey(author)) {
				pmGraph.put(author, new HashSet<String>());
			}
			if (!pmGraph.containsKey(target)) {
				pmGraph.put(target, new HashSet<String>());
			}
			pmGraph.get(author).add(target);
			pmGraph.get(target).add(author);
		}
		// build the silence graph
		for (int i = 0; i < users.size(); i++) {
			for (int j = i + 1; j < users.size(); j++) {
				String u1 = users.get(i).getUserName();
				String u2 = users.get(j).getUserName();
				if (!pmGraph.containsKey(u1) || !pmGraph.get(u1).contains(u2)) {
					graph.addEdge(u1, u2);
				}
			}
		}
		// find the cliques
		BronKerboschCliqueFinder<String, DefaultEdge> cliqueFinder = new BronKerboschCliqueFinder<String, DefaultEdge>(
				graph);
		for (Set<String> clique : cliqueFinder.getAllMaximalCliques()) {
			List<String> cliqueList = new ArrayList<String>();
			cliqueList.addAll(clique);
			list.add(cliqueList);
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/unconnected/{start}/{end}")
	public Response analysisSocialNetwork(@PathParam("start") String start,
			@PathParam("end") String end) {
		Log.enter("SNA from " + start + " to " + end);
		List<List<String>> result = new ArrayList<List<String>>();
		try {
			cluster(start, end, result);
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit();
		}
		return ok(new Gson().toJson(result));
	}

}
