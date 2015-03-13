package edu.cmu.sv.ws.ssnoc.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.po.StatusCrumbPO;

public class StatusCrumbDAOImpl extends BaseDAOImpl implements IStatusCrumbDAO {

	private List<StatusCrumbPO> processResults(PreparedStatement stmt) {
		Log.enter(stmt);

		if (stmt == null) {
			Log.warn("Inside processResults method with NULL statement object.");
			return null;
		}

		Log.debug("Executing stmt = " + stmt);
		List<StatusCrumbPO> statusCrumbPOs = new ArrayList<StatusCrumbPO>();
		try (ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				StatusCrumbPO po = new StatusCrumbPO();
				po.setCrumbID(rs.getLong(1));
				po.setUserName(rs.getString(2));
				po.setStatusCode(rs.getString(3));
				po.setCreatedAt(rs.getString(4));

				statusCrumbPOs.add(po);
			}
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit(statusCrumbPOs);
		}

		return statusCrumbPOs;
	}

	@Override
	public void save(StatusCrumbPO statusCrumbPO) {
		Log.enter(statusCrumbPO);
		if (statusCrumbPO == null) {
			Log.warn("Inside save method with statusCrumbPO == NULL");
			return;
		}

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL.INSERT_STATUS_CRUMB)) {
			stmt.setString(1, statusCrumbPO.getUserName());
			stmt.setString(2, statusCrumbPO.getStatusCode());
			stmt.setString(3, statusCrumbPO.getCreatedAt());

			int rowCount = stmt.executeUpdate();
			Log.trace("Statement executed, and " + rowCount + " rows inserted.");
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit();
		}
	}

	@Override
	public StatusCrumbPO findById(long crumbId) {
		Log.enter(crumbId);

		String query = SQL.FIND_STATUS_CRUMB_BY_ID;

		StatusCrumbPO po = null;
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);) {
			stmt.setLong(1, crumbId);

			List<StatusCrumbPO> statusCrumbPOs = processResults(stmt);

			if (statusCrumbPOs.size() == 0) {
				Log.debug("No status crumb exists with crumbId = " + crumbId);
			} else {
				po = statusCrumbPOs.get(0);
			}
		} catch (Exception e) {
			handleException(e);
			Log.exit(po);
		}

		return po;
	}

	@Override
	public List<StatusCrumbPO> findByUserName(String userName) {
		Log.enter(userName);

		String query = SQL.FIND_STATUS_CRUMBS_BY_USER_NAME;

		List<StatusCrumbPO> statusCrumbPOs = null;
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);) {
			stmt.setString(1, userName);
			statusCrumbPOs = processResults(stmt);
		} catch (Exception e) {
			handleException(e);
			Log.exit(statusCrumbPOs);
		}

		return statusCrumbPOs;
	}

}
