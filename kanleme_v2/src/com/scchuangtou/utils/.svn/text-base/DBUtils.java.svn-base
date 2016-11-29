package com.scchuangtou.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;
import com.scchuangtou.config.AccountConfig;

public final class DBUtils {
	private static final int FREE_MAX_COUNT = 30;
	public static final int VALIDATION_CONNECTION_TIME = 2 * 60 * 1000;
	private static final LinkedList<ConnectionCache> freeConnections = new LinkedList<ConnectionCache>();
	private static boolean checkIsRun = false;

	public static synchronized void checkCacheConnection() {//定时检测连接是否有效
		if (checkIsRun) {
			return;
		}
		checkIsRun = true;
		new Thread() {
			public void run() {
				ConnectionCache cache = null;
				while (checkIsRun) {
					try {
						sleep(VALIDATION_CONNECTION_TIME);
					} catch (Exception e) {
						break;
					}
					int index = 0;
					int size = freeConnections.size();
					while (index < size) {
						synchronized (freeConnections) {
							if (freeConnections.isEmpty()) {
								break;
							} else {
								cache = freeConnections.removeFirst();
							}
						}
						if (cache.isValid()) {
							synchronized (freeConnections) {
								freeConnections.addLast(cache);
							}
						} else {
							System.out.println("connection is not valid");
							cache.close();
						}
						index++;
					}
				}
			};
		}.start();
	}

	@Override
	protected void finalize() throws Throwable {
		checkIsRun = false;
		super.finalize();
	}

	/**
	 * 创建连接
	 * 
	 * @return
	 * @throws SQLException
	 */
	private static Connection createConnection() throws SQLException {
		System.out.println("create a new connection");
		try {
			Class.forName(AccountConfig.DBConfig.DRIVER);
			return DriverManager.getConnection(AccountConfig.DBConfig.URLSTR, AccountConfig.DBConfig.USERNAME,
					AccountConfig.DBConfig.USERPASSWORD);
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * 获取连接
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static ConnectionCache getConnection() throws SQLException {
		ConnectionCache cache = null;
		synchronized (freeConnections) {
			if (freeConnections.size() > 0) {
				cache = freeConnections.removeLast();
			}
		}
		if (cache == null) {
			Connection conn = createConnection();
			if (conn != null) {
				cache = new ConnectionCache(conn);
			}
		}
		if (cache != null) {
			cache.isUse = true;
		}
		return cache;
	}

	/**
	 * 放回连接池中
	 * 
	 * @param conn
	 */
	public static void close(ConnectionCache conn) {
		if (conn == null || !conn.reset()) {
			return;
		}
		synchronized (freeConnections) {
			if (!conn.isUse) {
				throw new IllegalArgumentException("connection is free");
			}
			conn.isUse = false;
			if (freeConnections.size() > FREE_MAX_COUNT) {
				freeConnections.removeFirst().close();
			}
			freeConnections.addLast(conn);
		}
		checkCacheConnection();
	}

	/**
	 * 关闭自动提交,否则效率没有提升
	 * @param conn
	 * @param sql
	 *            eg:insert into tablename
	 * @param datas
	 * @return
	 * @throws SQLException
	 */
	public static int insertBatch(ConnectionCache conn, String sql, List<Map<String, Object>> dataLists) throws SQLException {
		StringBuffer sb = new StringBuffer(sql).append("(");
		StringBuffer valueSql = new StringBuffer();
		
		Map<String, Object> data = dataLists.get(0);
		Set<String> keys = data.keySet();
		Object[] columns = new Object[keys.size()];
		int index = 0;
		for (String key : keys) {
			if (index > 0) {
				sb.append(",");
				valueSql.append(",");
			}
			sb.append(key);
			valueSql.append("?");
			columns[index] = key;
			index++;
		}
		sb.append(") value (").append(valueSql).append(")");
		PreparedStatementCache pstat = null;
		try {
			pstat = conn.prepareStatement(sb.toString());
			for (Map<String, Object> dataList : dataLists) {
				for (int i = 0; i < columns.length; i++) {
					pstat.setObject(i + 1, dataList.get(columns[i]));
				}
				pstat.addBatch();
			}
			int[] rows = pstat.executeBatch();
			if (rows == null) {
				return 0;
			} else {
				int count = 0;
				for (int row : rows) {
					count += row;
				}
				return count >= dataLists.size() ? count : 0;
			}
		} finally {
			close(pstat);
		}
	}

	/**
	 * @param conn
	 * @param sql
	 *            eg:insert into tablename
	 * @param datas
	 *            key:column;value:column's value
	 * @return
	 * @throws SQLException
	 */
	public static int insert(ConnectionCache conn, String sql, Map<String, Object> datas) throws SQLException {
		StringBuffer sb = new StringBuffer();
		sb.append(sql).append("(");
		Set<String> columns = datas.keySet();
		Object[] values = new Object[columns.size()];
		StringBuffer valueSql = new StringBuffer();
		int i = 0;
		for (String column : columns) {
			if (i > 0) {
				sb.append(",");
				valueSql.append(",");
			}
			sb.append(column);
			valueSql.append("?");
			values[i] = datas.get(column);
			i++;
		}
		sb.append(") value (").append(valueSql).append(")");
		return executeUpdate(conn, sb.toString(), values);
	}

	public static int executeUpdate(ConnectionCache conn, String sql, Object[] values) throws SQLException {
		int row = 0;
		if (conn == null) {
			return row;
		}
		PreparedStatementCache pstat = null;
		try {
			pstat = conn.prepareStatement(sql);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					pstat.setObject(i + 1, values[i]);
				}
			}
			row = pstat.executeUpdate();
		} finally {
			close(pstat);
		}
		return row;
	}

	public static void close(Statement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
			}
		}
	}

	public static void close(PreparedStatementCache ps) {
		if (ps != null) {
			try {
				ps.pst.close();
			} catch (SQLException e) {
			}
		}
	}

	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
	}

	public static void beginTransaction(DBUtils.ConnectionCache conn) throws SQLException {
		if (conn != null) {
			try {
				conn.conn.setAutoCommit(false);
			} catch (SQLException e) {
				conn.isClosed = true;
				throw e;
			}
		}
	}

	public static void commitTransaction(DBUtils.ConnectionCache conn) throws SQLException {
		if (conn != null) {
			try {
				conn.conn.commit();
			} catch (SQLException e) {
				conn.isClosed = true;
				throw e;
			}
		}
	}

	public static void rollbackTransaction(DBUtils.ConnectionCache conn) {
		try {
			if (conn != null && !conn.conn.getAutoCommit()) {
				conn.conn.rollback();
			}
		} catch (Exception e) {
			conn.isClosed = true;
		}
	}

	public static class ConnectionCache {
		private Connection conn;
		private boolean isUse = false;
		private boolean isClosed = false;

		public ConnectionCache(Connection conn) {
			this.conn = conn;
		}

		public PreparedStatementCache prepareStatement(String arg0) throws SQLException {
			return new PreparedStatementCache(this, arg0);
		}

		private void close() {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
			isClosed = true;
		}

		private boolean reset() {
			if (conn == null) {
				return false;
			}

			boolean flag = false;
			if (!isClosed) {
				try {
					if (!conn.getAutoCommit()) {
						conn.rollback();
						conn.setAutoCommit(true);
					}
					flag = true;
				} catch (Exception e) {
					flag = false;
				}
			}
			if (!flag) {
				close();
			}
			return flag;
		}

		public boolean isValid() {
			boolean flag = false;
			PreparedStatementCache pstat = null;
			ResultSet rs = null;
			try {
				if (conn.isValid(2)) {
					pstat = prepareStatement("SELECT 1 FROM dual");
					rs = pstat.executeQuery();
					flag = true;
				}
			} catch (Exception e) {
			} finally {
				DBUtils.close(rs);
				DBUtils.close(pstat);
			}
			return flag;
		}
	}

	public static class PreparedStatementCache {
		private ConnectionCache conn;
		private PreparedStatement pst;

		public PreparedStatementCache(ConnectionCache conn, String arg0) throws SQLException {
			this.conn = conn;
			this.pst = conn.conn.prepareStatement(arg0);
		}

		public void addBatch() throws SQLException {
			try {
				this.pst.addBatch();
			} catch (MySQLNonTransientConnectionException e) {
				this.conn.isClosed = true;
				throw e;
			}
		}

		public int[] executeBatch() throws SQLException {
			try {
				return this.pst.executeBatch();
			} catch (MySQLNonTransientConnectionException e) {
				this.conn.isClosed = true;
				throw e;
			}
		}

		public ResultSet executeQuery() throws SQLException {
			try {
				ResultSet rs = pst.executeQuery();
				return rs;
			} catch (MySQLNonTransientConnectionException e) {
				this.conn.isClosed = true;
				throw e;
			}
		}

		public int executeUpdate() throws SQLException {
			try {
				int rs = pst.executeUpdate();
				return rs;
			} catch (MySQLNonTransientConnectionException e) {
				this.conn.isClosed = true;
				throw e;
			}
		}

		public void setObject(int parameterIndex, Object value) throws SQLException {
			pst.setObject(parameterIndex, value);
		}
	}

	public static void main(String[] args) {
		String tableName = "focus";
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = createConnection();
			pst = conn.prepareStatement("select * from " + tableName);
			String sql = "select ";
			String sql2 = "select ";
			ResultSetMetaData rsd = pst.executeQuery().getMetaData();
			for (int i = 0; i < rsd.getColumnCount(); i++) {
				if (i > 0) {
					sql += ",";
					sql2 += ",";
				}
				String name = rsd.getColumnName(i + 1);
				System.out.println("public String " + name + ";");
				sql += name;
				sql2 += tableName + "." + name;
			}
			sql += " from " + tableName;
			sql2 += " from " + tableName;
			System.out.println(sql);
			System.out.println(sql2);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(pst);
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
