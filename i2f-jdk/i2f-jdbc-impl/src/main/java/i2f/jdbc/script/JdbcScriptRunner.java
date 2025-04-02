//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package i2f.jdbc.script;

import lombok.Data;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class JdbcScriptRunner {
    public static final String LINE_SEPARATOR = System.lineSeparator();
    public static final String DEFAULT_DELIMITER = ";";
    public static final Pattern DELIMITER_PATTERN = Pattern.compile("^\\s*((--)|(//))?\\s*(//)?\\s*@DELIMITER\\s+([^\\s]+)", Pattern.CASE_INSENSITIVE);
    protected Connection connection;
    protected boolean stopOnError = false;
    protected boolean throwWarning = false;
    protected boolean autoCommit = false;
    protected boolean sendFullScript = false;
    protected boolean removeCRs = false;
    protected boolean escapeProcessing = true;
    protected String delimiter = DEFAULT_DELIMITER;
    protected boolean fullLineDelimiter=false;

    public JdbcScriptRunner(Connection connection) {
        this.connection = connection;
    }

    public void runScript(File file) throws IOException{
        runScript(new FileInputStream(file));
    }

    public void runScript(URL url) throws IOException {
        runScript(url.openStream());
    }

    public void runScript(InputStream is) throws IOException{
        runScript(is,"UTF-8");
    }

    public void runScript(InputStream is,String charset) throws IOException {
        runScript(new InputStreamReader(is,charset));
    }

    public void runScript(String str){
        runScript(new StringReader(str));
    }

    public void runScript(Reader reader) {
        this.setAutoCommit();

        try {
            if (this.sendFullScript) {
                this.executeFullScript(reader);
            } else {
                this.executeLineByLine(reader);
            }
        } finally {
            try {
                reader.close();
            } catch (IOException e) {

            }
            this.rollbackConnection();
        }

    }

    protected void executeFullScript(Reader reader) {
        StringBuilder script = new StringBuilder();


        try {
            BufferedReader lineReader = new BufferedReader(reader);

            String line = null;
            while ((line = lineReader.readLine()) != null) {
                script.append(line);
                script.append(LINE_SEPARATOR);
            }

            String command = script.toString();
            this.print(command);
            this.print("\n");
            this.executeStatement(command);
            this.commitConnection();
        } catch (Exception e) {
            String errMsg = "Error executing: " + script + ".  Cause: " + e.getMessage();
            this.printlnError(errMsg,e);
            throw new IllegalStateException(errMsg, e);
        }finally {
            try {
                reader.close();
            } catch (IOException e) {

            }
        }
    }

    protected void executeLineByLine(Reader reader) {
        StringBuilder command = new StringBuilder();


        try {
            BufferedReader lineReader = new BufferedReader(reader);

            String line = null;
            while ((line = lineReader.readLine()) != null) {
                this.handleLine(command, line);
            }

            this.commitConnection();
            this.checkForMissingLineTerminator(command);
        } catch (Exception e) {
            String errMsg = "Error executing: " + command + ".  Cause: " + e.getMessage();
            this.printlnError(errMsg,e);
            throw new IllegalStateException(errMsg, e);
        }finally {
            try {
                reader.close();
            } catch (IOException e) {

            }
        }
    }

    protected void closeConnection() {
        try {
            this.connection.close();
        } catch (Exception e) {
        }

    }

    protected void setAutoCommit() {
        try {
            if (this.autoCommit != this.connection.getAutoCommit()) {
                this.connection.setAutoCommit(this.autoCommit);
            }

        } catch (Throwable e) {
            throw new IllegalStateException("Could not set AutoCommit to " + this.autoCommit + ". Cause: " + e.getMessage(), e);
        }
    }

    protected void commitConnection() {
        try {
            if (!this.connection.getAutoCommit()) {
                this.connection.commit();
            }

        } catch (Throwable e) {
            throw new IllegalStateException("Could not commit transaction. Cause: " + e.getMessage(), e);
        }
    }

    protected void rollbackConnection() {
        try {
            if (!this.connection.getAutoCommit()) {
                this.connection.rollback();
            }
        } catch (Throwable e) {

        }

    }

    protected void checkForMissingLineTerminator(StringBuilder command) {
        if (command != null && !command.toString().trim().isEmpty()) {
            throw new IllegalArgumentException("Line missing end-of-line terminator (" + this.delimiter + ") => " + command);
        }
    }

    protected void handleLine(StringBuilder command, String line) throws SQLException {
        String trimmedLine = line.trim();
        if (this.lineIsComment(trimmedLine)) {
            Matcher matcher = DELIMITER_PATTERN.matcher(trimmedLine);
            if (matcher.find()) {
                this.delimiter = matcher.group(5);
            }

            this.print(trimmedLine);
            this.print("\n");
        } else if (this.commandReadyToExecute(trimmedLine)) {
            command.append(line, 0, line.lastIndexOf(this.delimiter));
            command.append(LINE_SEPARATOR);
            this.print(command);
            this.print("\n");
            this.executeStatement(command.toString());
            command.setLength(0);
        } else if (!trimmedLine.isEmpty()) {
            command.append(line);
            command.append(LINE_SEPARATOR);
        }

    }

    protected boolean lineIsComment(String trimmedLine) {
        return trimmedLine.startsWith("//") || trimmedLine.startsWith("--");
    }

    protected boolean commandReadyToExecute(String trimmedLine) {
        return !this.fullLineDelimiter && trimmedLine.contains(this.delimiter)
                || this.fullLineDelimiter && trimmedLine.equals(this.delimiter);
    }

    protected void executeStatement(String command) throws SQLException {
        Statement statement = null;

        try {
            statement = this.connection.createStatement();
            statement.setEscapeProcessing(this.escapeProcessing);
            String sql = command;
            if (this.removeCRs) {
                sql = sql.replace("\r\n", "\n");
            }

            try {
                for (boolean hasResults = statement.execute(sql); hasResults || statement.getUpdateCount() != -1; hasResults = statement.getMoreResults()) {
                    this.checkWarnings(statement);
                    this.printResults(statement, hasResults);
                }
            } catch (SQLWarning e) {
                throw e;
            } catch (SQLException e) {
                if (this.stopOnError) {
                    throw e;
                }

                String message = "Error executing: " + command + ".  Cause: " + e.getMessage();
                this.printlnError(message,e);
            }
        } catch (Throwable e) {
            if (statement != null) {
                try {
                    statement.close();
                } catch (Throwable ex) {
                    e.addSuppressed(ex);
                }
            }

            throw e;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }


    }

    protected void checkWarnings(Statement statement) throws SQLException {
        if (this.throwWarning) {
            SQLWarning warning = statement.getWarnings();
            if (warning != null) {
                throw warning;
            }
        }
    }

    protected void printResults(Statement statement, boolean hasResults) {
        if (!hasResults) {
            return;
        }
        try {
            ResultSet rs=null;

            try {
                rs = statement.getResultSet();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                StringBuilder builder=new StringBuilder();
                for (int i = 0; i < columnCount; i++) {
                    String value = metaData.getColumnLabel(i + 1);
                    builder.append(value).append("\t");
                }
                builder.append("\n");
                this.print(builder.toString());

                while(rs.next()){
                    builder.setLength(0);
                    for (int i = 0; i < columnCount; ++i) {
                        String value = rs.getString(i + 1);
                        builder.append(value).append("\n");
                    }
                    builder.append("\n");
                    this.print(builder.toString());
                }
            } catch (Throwable e) {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Throwable ex) {
                        e.addSuppressed(ex);
                    }
                }

                throw e;
            }finally {
                if (rs != null) {
                    rs.close();
                }
            }

        } catch (SQLException e) {
            this.printlnError("Error printing results: " + e.getMessage(),e);
        }


    }

    protected void print(Object o) {
        System.out.println(o);
    }

    protected void printlnError(Object o,Throwable e) {
        System.err.println(o);
        if(e!=null){
            e.printStackTrace(System.err);
        }
    }
}
