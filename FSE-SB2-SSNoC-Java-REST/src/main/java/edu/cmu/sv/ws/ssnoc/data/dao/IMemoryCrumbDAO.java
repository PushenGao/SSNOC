package edu.cmu.sv.ws.ssnoc.data.dao;

import java.util.List;

import edu.cmu.sv.ws.ssnoc.data.po.MemoryCrumbPO;

public interface IMemoryCrumbDAO {

	void save(MemoryCrumbPO po);

	List<MemoryCrumbPO> findByInterval(int hours);

	void clear();
}
