package de.fraunhofer.sit.harvester.loggingpoints;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteOpenMode;
import org.sqlite.SQLiteConfig.Encoding;

/**
 * This class can be used to read and
 * load HARVESTER result files from a sqlite database.
 * @author Marc Miltenberger
 */
public final class SQLiteExtractionResultFile {

	/**
	 * Reads the logging point results from a file. If the reading process 
	 * succeeds, the stream will be closed
	 * @return the logging point results
	 * @throws IOException an error occurs
	 * @throws SQLException 
	 */
	public static ExtractionResults readLoggingPointResults(File file) throws SQLException {
		if (file == null || !file.exists())
			throw new IllegalArgumentException("File does not exist");
	    Connection connection = openSqliteRead(file);
	    ExtractionResults res = new ExtractionResults();
	    Statement stmt = connection.createStatement();
	    res.tookAnalysisMs = Integer.valueOf(ExtractionResults.readMetadata(stmt, ExtractionResults.TOOK_ANALYSIS_MS, "0"));
		res.analysisDate = ExtractionResults.readMetadata(stmt, ExtractionResults.ANALYSIS_DATE, ExtractionResults.NOVALUE);
		res.md5Sum = ExtractionResults.readMetadata(stmt, ExtractionResults.MD5,ExtractionResults.NOVALUE);
		res.numberOfPathes = Integer.parseInt(ExtractionResults.readMetadata(stmt, ExtractionResults.NUMBER_OF_PATHES, "0"));
		res.runTime = Integer.parseInt(ExtractionResults.readMetadata(stmt, ExtractionResults.RUNTIME, "0"));
		res.startupTime = Integer.parseInt(ExtractionResults.readMetadata(stmt, ExtractionResults.STARTUP_TIME, "0"));
		NumberFormat nf = NumberFormat.getInstance(Locale.US);
		try {
			res.avgExecutorVariables = nf.parse(ExtractionResults.readMetadata(stmt, ExtractionResults.AVGEXECUTORVARIABLES, "-1")).doubleValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		res.numberOfFilledParameters = Integer.parseInt(ExtractionResults.readMetadata(stmt, ExtractionResults.NUMBER_OF_FILLED_PARAMS, "-1"));
		//res.numberofStaticValuesNeeded = Integer.parseInt(readMetadata(stmt, NUMBER_OF_STATIC_VALUES_NEEDED, "-1"));
		res.parameters = ExtractionResults.readMetadata(stmt,ExtractionResults.PARAMETER, "");
		ResultSet allLoggingPoints = stmt.executeQuery("SELECT * FROM LoggingPoints;");
		while (allLoggingPoints.next()) {
			int id = allLoggingPoints.getInt("loggingPointId");
			String methodSig = allLoggingPoints.getString("methodSignature");
			String statement = allLoggingPoints.getString("stmt");
			String additionalInformation = allLoggingPoints.getString("additionalInformation");
			int codeLine = allLoggingPoints.getInt("codeLine");
			int codeCol = allLoggingPoints.getInt("codeCol");
			String params = allLoggingPoints.getString("params");
			
			LoggingPointInfo loggingPointInfo = new LoggingPointInfo(id, methodSig, statement, additionalInformation, codeLine, codeCol, ExtractionResults.split(params));
			res.allLoggingPoints.put(id, loggingPointInfo);
		}
		Map<Integer, ArrayList<LoggingPointResult>> resultsPerId = res.resultsPerId ;
		ResultSet loggingPointResults = stmt.executeQuery("SELECT * FROM LoggingPointResults;");
		while (loggingPointResults.next()) {
			String resultingValues = loggingPointResults.getString("resultingValues");
			int path = loggingPointResults.getInt("path");
			int loggingPointId = loggingPointResults.getInt("loggingPointId");
			long triggerDate = loggingPointResults.getLong("triggerDate");
			
			ArrayList<LoggingPointResult> result = resultsPerId.get(loggingPointId);
			if (result == null)
			{
				result = new ArrayList<LoggingPointResult>();
				resultsPerId.put(loggingPointId, result);
			}
			
			LoggingPointResult currentResult = new LoggingPointResult(res.allLoggingPoints.get(loggingPointId), path, ExtractionResults.split(resultingValues), triggerDate);
			result.add(currentResult);
			res.allResults.add(currentResult);
		}

		ResultSet loggingPointExceptions = stmt.executeQuery("SELECT * FROM LoggingPointException;");
		while (loggingPointExceptions.next()) {
			String stackTrace = loggingPointResults.getString("stackTrace");
			String message = loggingPointResults.getString("message");
			int path = loggingPointResults.getInt("path");
			int loggingPointId = loggingPointResults.getInt("loggingPointId");
			
			ExceptionResult currentResult = new ExceptionResult(res.allLoggingPoints.get(loggingPointId), path, message, stackTrace);
			res.resultsExceptions.add(currentResult);
		}
		ResultSet dynamicRuntime = stmt.executeQuery("SELECT * FROM DynamicRuntime ORDER BY loggingPointId;");
		while (dynamicRuntime.next()) {
			int ms = loggingPointResults.getInt("ms");
			int path = loggingPointResults.getInt("path");
			int loggingPointId = loggingPointResults.getInt("loggingPointId");
			boolean timeoutHitted = loggingPointResults.getBoolean("timeout");
			
			res.runtimeInformation.put(new LoggingPointAndPathCombination(res.allLoggingPoints.get(loggingPointId), path), new RuntimeInformation(ms, timeoutHitted));
		}
		
		stmt.close();
		connection.close();
		return res;
		
	}
	

	public static void writeLoggingPointResults(ExtractionResults res, File file) throws SQLException {
		if (file.exists())
			file.delete();
	    Connection connection = openSqliteWrite(file);
	    Statement stmt = connection.createStatement();
    	NumberFormat nf = NumberFormat.getInstance(Locale.US);
	    stmt.execute("CREATE TABLE IF NOT EXISTS Metadata(name TEXT PRIMARY KEY, value TEXT);");
	    stmt.execute("CREATE TABLE IF NOT EXISTS DynamicRuntime(loggingPointId INT, path INT, ms INT, timeout BOOLEAN, PRIMARY KEY (loggingPointId, path));");
	    stmt.execute("CREATE TABLE IF NOT EXISTS LoggingPoints(loggingPointId INT PRIMARY KEY, methodSignature TEXT, stmt TEXT, additionalInformation TEXT, codeLine INT, codeCol INT, params TEXT);");
	    stmt.execute("CREATE TABLE IF NOT EXISTS LoggingPointResults(resultingValues TEXT, loggingPointId INT, path INT, triggerDate INT, PRIMARY KEY (resultingValues, loggingPointId));");
	    stmt.execute("CREATE TABLE IF NOT EXISTS LoggingPointException(stackTrace TEXT, loggingPointId INT, message TEXT, path INT, PRIMARY KEY (stackTrace, path));");

	    stmt.execute("INSERT OR REPLACE INTO Metadata (Name, Value) VALUES ('TookAnalysisMs', '" + res.tookAnalysisMs + "')");
	    stmt.execute("INSERT OR REPLACE INTO Metadata (Name, Value) VALUES ('" + ExtractionResults.RUNTIME + "', '" + res.runTime + "')");
	    stmt.execute("INSERT OR REPLACE INTO Metadata (Name, Value) VALUES ('AnalysisDate', '" + res.analysisDate + "')");
	    stmt.execute("INSERT OR REPLACE INTO Metadata (Name, Value) VALUES ('md5', '" + res.md5Sum + "')");
    	stmt.execute("INSERT OR REPLACE INTO Metadata (Name, Value) VALUES ('NumberOfPathes', '" + res.numberOfPathes + "')");
    	stmt.execute("INSERT OR REPLACE INTO Metadata (Name, Value) VALUES ('AvgExecutorVariables', '" + nf.format(res.avgExecutorVariables) + "')");
    	stmt.execute("INSERT OR REPLACE INTO Metadata (Name, Value) VALUES ('NumberOfFilledParams', '" + res.numberOfFilledParameters + "')");
    	stmt.execute("INSERT OR REPLACE INTO Metadata (Name, Value) VALUES ('Parameter', '" + res.parameters + "')");
    	stmt.execute("INSERT OR REPLACE INTO Metadata (Name, Value) VALUES ('StartupTime', '" + res.startupTime + "')");

    	connection.setAutoCommit(false);
    	PreparedStatement prepInsertLp = connection.prepareStatement("INSERT INTO LoggingPoints (loggingPointId, methodSignature, stmt, additionalInformation, codeLine, codeCol, params) VALUES (?, ?, ?, ?, ?, ?, ?)");
    	for (LoggingPointInfo info : res.getAllLoggingPoints()) {
    		prepInsertLp.setInt(1, info.id);
    		prepInsertLp.setString(2, info.methodSignature);
    		prepInsertLp.setString(3, info.stmt);
    		prepInsertLp.setString(4, info.additionalInformation);
    		prepInsertLp.setInt(5, info.row);
    		prepInsertLp.setInt(6, info.col);
    		prepInsertLp.setString(7, LoggingUtils.encode((Object[])info.loggedValues));
    		prepInsertLp.executeUpdate();
    	}
    	prepInsertLp.close();


    	PreparedStatement prepInsertLpResult = connection.prepareStatement("INSERT INTO LoggingPointResults (loggingPointId, path, resultingValues, triggerDate) VALUES (?, ?, ?, ?)");
    	for (LoggingPointResult r : res.getResults()) {
    		prepInsertLpResult.setInt(1, r.id);
    		prepInsertLpResult.setInt(2, r.path);
    		Object[] o = r.variables.values().toArray();
    		prepInsertLpResult.setString(3, LoggingUtils.encode(o));
    		prepInsertLpResult.setLong(4, r.dateTriggered);
    		prepInsertLpResult.executeUpdate();
    	}
    	prepInsertLpResult.close();

    	PreparedStatement prepInsertLpExc = connection.prepareStatement("INSERT INTO LoggingPointException (loggingPointId, message, stackTrace, path) VALUES (?, ?, ?, ?)");
    	for (ExceptionResult r : res.getExceptionResults()) {
    		try {
	    		prepInsertLpExc.setInt(1, r.id);
	    		prepInsertLpExc.setString(2, r.getMessage());
	    		prepInsertLpExc.setString(3, r.getStackTrace());
	    		prepInsertLpExc.setInt(4, r.getPath());
	    		prepInsertLpExc.executeUpdate();
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    	prepInsertLpExc.close();
    	

    	PreparedStatement prepInsertRuntime = connection.prepareStatement("INSERT INTO DynamicRuntime (loggingPointId, path, ms, timeout) VALUES (?, ?, ?, ?)");
    	for (Entry<LoggingPointAndPathCombination, RuntimeInformation> r : res.getRuntimeInformation().entrySet()) {
    		try {
	    		prepInsertRuntime.setInt(1, r.getKey().getLoggingPoint().id);
	    		prepInsertRuntime.setInt(2, r.getKey().getPath());
	    		prepInsertRuntime.setInt(3, r.getValue().getMsTook());
	    		prepInsertRuntime.setBoolean(4, r.getValue().isTimeoutHitted());
	    		prepInsertRuntime.executeUpdate();
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    	connection.commit();
    	prepInsertRuntime.close();
    	stmt.close();
    	connection.close();
	}

	private static Connection openSqliteRead(File file) throws SQLException {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		}
		
		Connection connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
		return connection;
	}

	private static Connection openSqliteWrite(File file) throws SQLException {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		}
		try {
			file.getParentFile().mkdirs();
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
			return connection;
		} catch (Exception e) {
			throw e;
		}
	}
	
}
