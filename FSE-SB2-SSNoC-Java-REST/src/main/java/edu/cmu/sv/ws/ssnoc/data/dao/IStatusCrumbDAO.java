package edu.cmu.sv.ws.ssnoc.data.dao;

import java.util.List;

import edu.cmu.sv.ws.ssnoc.data.po.StatusCrumbPO;

public interface IStatusCrumbDAO {

	void save(StatusCrumbPO statusCrumbPO);

	StatusCrumbPO findById(long CrumbId);

	List<StatusCrumbPO> findByUserName(String userName);

}
