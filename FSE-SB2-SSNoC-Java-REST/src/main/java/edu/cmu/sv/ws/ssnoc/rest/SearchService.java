package edu.cmu.sv.ws.ssnoc.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.Message;
import edu.cmu.sv.ws.ssnoc.dto.User;

@Path("/search")
public class SearchService extends BaseService {
	String stop = "a,able,about,across,after,all,almost,also,am,among,an,and,any,are,as,at,be,because,been,but,by,can,cannot,could,dear,did,do,does,either,else,ever,every,for,from,get,got,had,has,have,he,her,hers,him,his,how,however,i,if,in,into,is,it,its,just,least,let,like,likely,may,me,might,most,must,my,neither,no,nor,not,of,off,often,on,only,or,other,our,own,rather,said,say,says,she,should,since,so,some,than,that,the,their,them,then,there,these,they,this,tis,to,too,twas,us,wants,was,we,were,what,when,where,which,while,who,whom,why,will,with,would,yet,you,your";
	//get the message by id
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/name/{nameword}")//大小写问题
	public List<User>  searchByName(@PathParam("nameword") String name) {
		Log.enter("seach start with names" + name);

		List<UserPO> pos = new ArrayList<UserPO>();
		List<User> dtos = new ArrayList<User>();
		name = "%" + name + "%";
		try {
			pos = DAOFactory.getInstance().getUserDAO().searchByName(name);
			for(UserPO po: pos){
				User dto = ConverterUtils.convert(po);
				dtos.add(dto);
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(dtos);
		}
		return dtos;
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/status/{statuscode}")//大小写问题
	public List<User>  searchByStatus(@PathParam("statuscode") String status) {
		Log.enter("seach start with names" + status);

		List<UserPO> pos = new ArrayList<UserPO>();
		List<User> dtos = new ArrayList<User>();
		try {
			pos = DAOFactory.getInstance().getUserDAO().searchByStatus(status);
			for(UserPO po: pos){
				User dto = ConverterUtils.convert(po);
				dtos.add(dto);
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(dtos);
		}
		return dtos;
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/wall/{keyword}")//大小写问题
	public List<Message>  searchOnWall(@PathParam("keyword") String keyword) {
		Log.enter("seach start with keyword" + keyword);
		int flag = 0;
		List<MessagePO> pos = new ArrayList<MessagePO>();
		List<Message> dtos = new ArrayList<Message>();

		String[] words = keyword.split("\\W");
		String[] stops = stop.split(",");
		List<String> stopwords = new ArrayList<String>();
		for(int i = 0; i < stops.length; i++)
			stopwords.add(stops[i]);
		for(String word: words){
			if(!stopwords.contains(word)){
				flag = 1;
				break;
			}
		}
		if(flag == 0)
			return dtos;
		keyword = "%" + keyword + "%";
		try {
			pos = DAOFactory.getInstance().getSearchDAO().searchMessageOnWall(keyword);
			for(MessagePO po: pos){
				Message dto = ConverterUtils.convert(po);
				dtos.add(dto);
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(dtos);
		}
		return dtos;
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/chat/{username}/{keyword}")//大小写问题
	public List<Message>  searchOnChat(@PathParam("username") String username, @PathParam("keyword") String keyword) {
		Log.enter("seach start with username" + username);
		Log.enter("seach start with keyword" + keyword);
		int flag = 0;
		List<MessagePO> pos = new ArrayList<MessagePO>();
		List<Message> dtos = new ArrayList<Message>();
		String[] words = keyword.split("\\W");
		String[] stops = stop.split(",");
		List<String> stopwords = new ArrayList<String>();
		for(int i = 0; i < stops.length; i++)
			stopwords.add(stops[i]);
		for(String word: words){
			if(!stopwords.contains(word)){
				flag = 1;
				break;
			}
		}
		keyword = "%" + keyword + "%";
		if(flag == 0)
			return dtos;
		try {
			pos = DAOFactory.getInstance().getSearchDAO().searchMessageChat(username, keyword);
			for(MessagePO po: pos){
				Message dto = ConverterUtils.convert(po);
				dtos.add(dto);
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(dtos);
		}
		return dtos;
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/announcement/{keyword}")//大小写问题
	public List<Message>  searchAnnouncement(@PathParam("keyword") String keyword) {
		Log.enter("seach start with keyword" + keyword);
		int flag = 0;
		List<MessagePO> pos = new ArrayList<MessagePO>();
		List<Message> dtos = new ArrayList<Message>();

		String[] words = keyword.split("\\W");
		String[] stops = stop.split(",");
		List<String> stopwords = new ArrayList<String>();
		for(int i = 0; i < stops.length; i++)
			stopwords.add(stops[i]);
		for(String word: words){
			if(!stopwords.contains(word)){
				flag = 1;
				break;
			}
		}
		if(flag == 0)
			return dtos;
		keyword = "%" + keyword + "%";
		try {
			pos = DAOFactory.getInstance().getSearchDAO().searchMessageAnnouncement(keyword);
			for(MessagePO po: pos){
				Message dto = ConverterUtils.convert(po);
				dtos.add(dto);
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(dtos);
		}
		return dtos;
	}
}
