package edu.cmu.sv.ws.ssnoc.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.po.MemoryCrumbPO;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;

public class MemoryCrumbDAOImpl extends BaseDAOImpl implements IMemoryCrumbDAO {

	private List<MemoryCrumbPO> processResults(PreparedStatement stmt) {
		Log.enter(stmt);

		if (stmt == null) {
			Log.warn("Inside processResults method with NULL statement object.");
			return null;
		}

		Log.debug("Executing stmt = " + stmt);
		List<MemoryCrumbPO> memoryCrumbPOs = new ArrayList<MemoryCrumbPO>();
		try (ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				MemoryCrumbPO po = new MemoryCrumbPO();
				po.setCrumbID(rs.getLong(1));
				po.setCreatedAt(rs.getLong(2));
				po.setUsedVolative(rs.getLong(3));
				po.setRemainingVolative(rs.getLong(4));
				po.setUsedPersistent(rs.getLong(5));
				po.setRemainingPersistent(rs.getLong(6));
				memoryCrumbPOs.add(po);
			}
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit(memoryCrumbPOs);
		}

		return memoryCrumbPOs;
	}

	@Override
	public void save(MemoryCrumbPO po) {
		Log.enter(po);
		if (po == null) {
			Log.warn("Inside save method with messagePO == NULL");
			return;
		}

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL.INSERT_MEMORY_CRUMBS)) {
			stmt.setString(1, Long.toString(po.getCreatedAt()));
			stmt.setString(2, Long.toString(po.getUsedVolative()));
			stmt.setString(3, Long.toString(po.getRemainingVolative()));
			stmt.setString(4, Long.toString(po.getUsedPersistent()));
			stmt.setString(5, Long.toString(po.getRemainingPersistent()));

			int rowCount = stmt.executeUpdate();
			Log.trace("Statement executed, and " + rowCount + " rows inserted.");
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit();
		}
	}

	@Override
	public List<MemoryCrumbPO> findByInterval(int hours) {

		String query = SQL.FIND_MEMORY_CRUMBS_BY_HOURS;

		long start_time = (System.currentTimeMillis()/1000) - (3600 * hours);
		List<MemoryCrumbPO> memoryCrumbPOs = null;
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);) {
			stmt.setString(1, Long.toString(start_time));
			memoryCrumbPOs = processResults(stmt);
		} catch (Exception e) {
			handleException(e);
			Log.exit(memoryCrumbPOs);
		}

		return memoryCrumbPOs;
	}

	@Override
	public void clear() {
		Log.enter();
		try (Connection conn = DBUtils.getConnection(); Statement stmt = conn.createStatement()) {
			stmt.execute(SQL.CLEAR_MEMORY_CRUMBS);
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit();
		}

	}

}
