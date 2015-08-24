package database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.jdbc.jdbc2.optional.*;
import giper.ImageProc;
import giper.User;
//import ru.aviacons.balance.calculation.Position;
//import ru.aviacons.balance.calculation.Angle;

/**
 * Created with IntelliJ IDEA.
 * User: amarinin
 * Date: 3/31/14
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class JDBCUtilities {

    private static Logger log = Logger.getLogger(JDBCUtilities.class.getName());
    public String dbms;
    public String jarFile;
    public String dbName;
    public String userName;
    public String password;
    public String urlString;

    private String driver;
    private String serverName;
    private int portNumber;
    private Properties prop;
    private static JDBCUtilities jdbcUtilities = null;
    //public MysqlDataSource dataSource;

    /**
     *
     * Шаблон "сиглетон"
     * @return возвращает объект класс JDBCUtilities
     */
    public static JDBCUtilities getJBDCUtilities(){
        if(jdbcUtilities == null)
            jdbcUtilities = new JDBCUtilities();
        return jdbcUtilities;
    }

    private JDBCUtilities(){
        this.setDefaultProperties();
        //dataSource = new MysqlDataSource();
        //dataSource.setUser(this.userName);
        //dataSource.setPassword(this.password);
        //dataSource.setUrl("jdbc:mysql://" + this.serverName + ":" + this.portNumber + "/" + this.dbName);

    }

    private JDBCUtilities(String propertiesFileName) throws FileNotFoundException,
            IOException, InvalidPropertiesFormatException {
        //super();
        this.setProperties(propertiesFileName);
    }

    /**
     * Функция устанавливает параметры соединения с базой данных по умолчанию
     */
    private void setDefaultProperties(){
        prop = new Properties();

        this.dbms = "mysql";
        this.jarFile = "com.mysql.jdbc.Driver";
        this.driver = "com.mysql.jdbc.Driver";
        this.dbName = "resttest";
        this.userName = "root";
        this.password = "root";
        this.serverName = "localhost";
        this.portNumber = 3306;

        prop.put("user", this.userName);
        prop.put("password", this.password);

        StringBuilder logStr = new StringBuilder();
        logStr.append("Set the following properties:\n");
        logStr.append("dbms: " + dbms + "\n");
        logStr.append("driver: " + driver + "\n");
        logStr.append("dbName: " + dbName + "\n");
        logStr.append("userName: " + userName + "\n");
        logStr.append("serverName: " + serverName + "\n");
        logStr.append("portNumber: " + portNumber + "\n");
        log.config(logStr.toString());
    }

    /**
     * Функция считывает параметры соединения с базой данных из файла
     * @param fileName имя файла содержащего параметры для подключения к базе данных
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     * @throws java.util.InvalidPropertiesFormatException
     */
    private void setProperties(String fileName) throws FileNotFoundException,
            IOException,
            InvalidPropertiesFormatException {
        this.prop = new Properties();
        FileInputStream fis = new FileInputStream(fileName);
        prop.loadFromXML(fis);

        this.dbms = this.prop.getProperty("dbms");
        this.jarFile = this.prop.getProperty("jar_file");
        this.driver = this.prop.getProperty("driver");
        this.dbName = this.prop.getProperty("database_name");
        this.userName = this.prop.getProperty("user_name");
        this.password = this.prop.getProperty("password");
        this.serverName = this.prop.getProperty("server_name");
        this.portNumber = Integer.parseInt(this.prop.getProperty("port_number"));

        System.out.println("Set the following properties:");
        System.out.println("dbms: " + dbms);
        System.out.println("driver: " + driver);
        System.out.println("dbName: " + dbName);
        System.out.println("userName: " + userName);
        System.out.println("serverName: " + serverName);
        System.out.println("portNumber: " + portNumber);

    }

    /**
     * Функция возвращает соединение с базой
     * @return Connection
     * @throws java.sql.SQLException
     */
    public Connection getConnection() throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);

        String currentUrlString = null;

        if (this.dbms.equals("mysql")) {
            currentUrlString = "jdbc:" + this.dbms + "://" + this.serverName +
                    ":" + this.portNumber + "/";
            conn =
                    DriverManager.getConnection(currentUrlString,
                            connectionProps);

            this.urlString = currentUrlString + this.dbName;
            conn.setCatalog(this.dbName);
        } else if (this.dbms.equals("derby")) {
            this.urlString = "jdbc:" + this.dbms + ":" + this.dbName;

            conn =
                    DriverManager.getConnection(this.urlString +
                            ";create=true", connectionProps);

        }
        log.info("Connected to database");
        return conn;
    }

    public static void closeConnection(Connection connArg) {
        log.info("Releasing all open resources ...");
        try {
            if (connArg != null) {
                connArg.close();
                connArg = null;
            }
        } catch (SQLException sqle) {
            printSQLException(sqle);
        }
    }

    public static void closeStatement(Statement statement){
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
        }
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                if (ignoreSQLException(((SQLException)e).getSQLState()) == false) {
                    e.printStackTrace(System.err);
                    log.log(Level.SEVERE, "Exception", e);
                    StringBuilder logStr = new StringBuilder();
                    logStr.append("SQLState: " + ((SQLException)e).getSQLState() + "\n");
                    logStr.append("Error Code: " + ((SQLException) e).getErrorCode() + "\n");
                    logStr.append("Message: " + e.getMessage() + "\n");
                    Throwable t = ex.getCause();
                    while (t != null) {
                        logStr.append("Cause: " + t + "\n");
                        t = t.getCause();
                    }
                    log.log(Level.SEVERE, logStr.toString());
                }
            }
        }
    }

    public static void getWarningsFromResultSet(ResultSet rs) throws SQLException {
        JDBCUtilities.printWarnings(rs.getWarnings());
    }

    public static void getWarningsFromStatement(Statement stmt) throws SQLException {
        JDBCUtilities.printWarnings(stmt.getWarnings());
    }

    public static void printWarnings(SQLWarning warning) throws SQLException {
        if (warning != null) {
            StringBuilder logStr = new StringBuilder();
            logStr.append("\n---Warning---\n");
            while (warning != null) {
                logStr.append("Message: " + warning.getMessage() + "\n");
                logStr.append("SQLState: " + warning.getSQLState() + "\n");
                logStr.append("Vendor error code: " + "\n");
                logStr.append(warning.getErrorCode() + "\n");
                warning = warning.getNextWarning();
            }
            log.severe(logStr.toString());
        }
    }

    public static boolean ignoreSQLException(String sqlState) {
        if (sqlState == null) {
            log.info("The SQL state is not defined!");
            return false;
        }
        // X0Y32: Jar file already exists in schema
        if (sqlState.equalsIgnoreCase("X0Y32"))
            return true;
        // 42Y55: Table already exists in schema
        if (sqlState.equalsIgnoreCase("42Y55"))
            return true;
        return false;
    }

    public static void main(String[] args){
    //    System.out.println(getJBDCUtilities().getWeightingsMaxId());


        System.out.println(getJBDCUtilities().getUser(1));
    }

    public String addImage(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        String out = null;

        try{
            conn = getConnection();
            conn.setAutoCommit(false);

            String query = "INSERT INTO image () " +
                    "VALUE ();";
            log.info(query);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            //проверяем наличие предупреждений
            //getWarningsFromResultSet(resultSet);
            getWarningsFromStatement(stmt);

            query = "SELECT MAX(id) FROM image;";
            stmt.executeQuery(query);
            resultSet = stmt.getResultSet();
            resultSet.next();
            out = resultSet.getString(1);
            //проверяем наличие предупреждений
            getWarningsFromResultSet(resultSet);
            getWarningsFromStatement(stmt);

            conn.commit();
        }catch(SQLException e){
            printSQLException(e);
        }finally {
            closeStatement(stmt);
            closeConnection(conn);
        }
        return out;
    }

    public int createUser(User user){
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        int out = 0;

        try{
            conn = getConnection();
            conn.setAutoCommit(false);

            String query = "INSERT INTO rest_user (name, family, image_id) " +
                    "VALUE ('" + user.getName() + "', '" + user.getFamily() + "', "+ user.getIntImageURL() +");";
            log.info(query);
            System.out.println(query);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            //проверяем наличие предупреждений
            //getWarningsFromResultSet(resultSet);
            getWarningsFromStatement(stmt);

            query = "SELECT MAX(id) FROM rest_user;";
            stmt.executeQuery(query);
            resultSet = stmt.getResultSet();
            resultSet.next();
            out = resultSet.getInt(1);
            //проверяем наличие предупреждений
            getWarningsFromResultSet(resultSet);
            getWarningsFromStatement(stmt);

            conn.commit();
        }catch(SQLException e){
            printSQLException(e);
        }finally {
            closeStatement(stmt);
            closeConnection(conn);
        }
        return out;
    }

    public User getUser(int id){
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        User user = null;

        try{
            conn = getConnection();

            String query = "SELECT * FROM rest_user WHERE id = " + id + ";" ;
            log.info(query);
            stmt = conn.createStatement();
            stmt.executeQuery(query);
            resultSet = stmt.getResultSet();
            resultSet.next();
            user = new User();
            user.setName(resultSet.getString("name"));
            user.setFamily(resultSet.getString("family"));
            user.setStatus((resultSet.getInt("status") == 1));
            user.setImageURL(ImageProc.serverPath + resultSet.getInt("image_id") + ".jpg");
            user.setAge(resultSet.getInt("age"));
            //проверяем наличие предупреждений
            getWarningsFromResultSet(resultSet);
            getWarningsFromStatement(stmt);

        }catch(SQLException e){
            printSQLException(e);
        }finally {
            closeStatement(stmt);
            closeConnection(conn);
        }
        return user;
    }

    public Boolean updateStatus(int id, Boolean newStatus){
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        Boolean oldStatus = null;

        try{
            conn = getConnection();
            conn.setAutoCommit(false);

            String query = "SELECT status FROM rest_user WHERE id = " + id +";";
            log.info(query);
            stmt = conn.createStatement();
            stmt.executeQuery(query);
            resultSet = stmt.getResultSet();
            resultSet.next();
            oldStatus = resultSet.getBoolean(1);
            //проверяем наличие предупреждений
            getWarningsFromResultSet(resultSet);
            getWarningsFromStatement(stmt);

            if(newStatus)
                query = "UPDATE rest_user SET status = 1 WHERE id = " + id + ";";
            else
                query = "UPDATE rest_user SET status = 0 WHERE id = " + id + ";";
            stmt.executeUpdate(query);
            
            //проверяем наличие предупреждений
            //getWarningsFromResultSet(resultSet);
            getWarningsFromStatement(stmt);

            conn.commit();
        }catch(SQLException e){
            printSQLException(e);
        }finally {
            closeStatement(stmt);
            closeConnection(conn);
        }
        return oldStatus;
    }


    /**
     * Функция получает показания датчиков из базы данных для одного положения ракеты
     * @param id номер измерения
     * @param angle положение ракеты
     * @return double[]
     */
    /*public double [] getCalibrations(int id, Angle angle){
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        [] output = new double [3];

        try{
            conn = getConnection();

            String query = "SELECT f0, f1, f2 " +
                    "FROM calibration " +
                    "WHERE id = " + id + " AND angle = " + angle.ordinal();
            log.info(query);
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
            resultSet.next();
            output[0] = resultSet.getDouble("f0");
            output[1] = resultSet.getDouble("f1");
            output[2] = resultSet.getDouble("f2");
            log.info("Результаты запроса");
            log.info("f0 = " + output[0] + " f1 = " + output[1] + " f2 = " + output[2]);

            //проверяем наличие предупреждений
            getWarningsFromResultSet(resultSet);
            getWarningsFromStatement(stmt);
        }catch(SQLException e){
            printSQLException(e);
        }finally {
            closeStatement(stmt);
            closeConnection(conn);
        }

        return output;
    }

    /**
     * Функция возвращает максимальное значени id в таблице calibration
     * @return
     */
    /*public int getCalibrationsMaxId(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        int output = 0;

        try{
            conn = getConnection();

            String query = "SELECT MAX(id) AS max_id " +
                    "FROM calibration ";
            log.info(query);
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
            resultSet.next();
            output = resultSet.getInt("max_id");
            log.info("Результаты запроса");
            log.info("max_id = " + output);

            //проверяем наличие предупреждений
            getWarningsFromResultSet(resultSet);
            getWarningsFromStatement(stmt);
        }catch(SQLException e){
            printSQLException(e);
        }finally {
            closeStatement(stmt);
            closeConnection(conn);
        }

        return output;
    }

    /**
     * Функция получает значения калибровочных показаний датчика для разных положений ракеты
     * @param id номер калибровки
     * @param position положение датчика (номер датчика)
     * @return double[]
     */
    /*public double [] getCalibrations(int id, Position position){
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        double[] output = new double [3];

        try{
            conn = getConnection();

            String query = "SELECT angle," + position.name().toLowerCase() +
                    " FROM calibration " +
                    "WHERE id = " + id + " AND angle IN (" + Angle.LEFT.ordinal() + ", " +
                    Angle.AFLAT.ordinal() + ", " +
                    Angle.RIGHT.ordinal() + ");";
            log.info(query);
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
            resultSet.next();
            output[resultSet.getInt("angle")] = resultSet.getDouble(position.name().toLowerCase());
            resultSet.next();
            output[resultSet.getInt("angle")] = resultSet.getDouble(position.name().toLowerCase());
            resultSet.next();
            output[resultSet.getInt("angle")] = resultSet.getDouble(position.name().toLowerCase());
            log.info("Результаты запроса" + "\n" +
                    Angle.LEFT.name() + "=" + output[0] +
                    Angle.AFLAT.name() + "=" + output[1] +
                    Angle.RIGHT.name() + "=" + output[2]);

            //проверяем наличие предупреждений
            getWarningsFromResultSet(resultSet);
            getWarningsFromStatement(stmt);
        }catch(SQLException e){
            printSQLException(e);
        }finally {
            closeStatement(stmt);
            closeConnection(conn);
        }

        return output;
    }

    /**
     * Функция возвращает углы наклона из базы данных измеренные инклинометром
     * @param id калибровки
     * @param angle уловое пожение поворотной платформы
     * @return массив содержажий angle_x и angle_z
     */
    /*public double [] getCalibrationAngles(int id, Angle angle){
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        double[] output = new double [2];

        try{
            conn = getConnection();

            String query = "SELECT angle_x, angle_z " +
                    " FROM calibration " +
                    "WHERE id = " + id + " AND angle = " + angle.number() + ";";
            log.info(query);
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
            resultSet.next();
            output[0] = resultSet.getDouble("angle_x");
            output[1] = resultSet.getDouble("angle_z");

            System.out.println("Результаты запроса" + "\n" +
                    angle.name() + "=" + angle.number() +
                    "angle_x = " + output[0] +
                    "angle_y = " + output[1]);

            //проверяем наличие предупреждений
            getWarningsFromResultSet(resultSet);
            getWarningsFromStatement(stmt);
        }catch(SQLException e){
            printSQLException(e);
        }finally {
            closeStatement(stmt);
            closeConnection(conn);
        }

        return output;
    }

    /**
     * Функция добавляет в базу данных измеренные массы и углы
     * @param id измерения
     * @param angle угловое положение поворотной части стенда
     * @param angles показания инклинометра
     * @param f показания тензодатчиков
     */
    /*public void addWeightings(int id, Angle angle, double[] angles, double[] f){

        Connection conn = null;
        Statement stmt = null;
        //ResultSet resultSet = null;
        try{
            conn = getConnection();

            String query = "INSERT INTO weighting " +
                    "VALUE (" + id + ", " + angle.ordinal() + ", " + angles[0] + ", " + angles[1] +
                    ", " + f[0] + ", " + f[1] + ", " + f[2] + ")";

            log.info(query);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            //проверяем наличие предупреждений
            //getWarningsFromResultSet(resultSet);
            getWarningsFromStatement(stmt);
        }catch(SQLException e){
            printSQLException(e);
        }finally {
            closeStatement(stmt);
            closeConnection(conn);
        }
    }

    /**
     * Функция получает значения датчиков из базы данных для одного положения ракеты
     * @param id номер измерения
     * @param angle положение ракеты
     * @return double[]
     */
    /*public double [] getWeightings(int id, Angle angle){
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        double[] output = new double [3];

        try{
            conn = getConnection();

            String query = "SELECT f0, f1, f2 " +
                    "FROM weighting " +
                    "WHERE id = " + id + " AND angle = " + angle.ordinal();
            log.info(query);
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
            resultSet.next();
            output[0] = resultSet.getDouble("f0");
            output[1] = resultSet.getDouble("f1");
            output[2] = resultSet.getDouble("f2");
            log.info("Результаты запроса" + "\n" +
                    "f0 = " + output[0] + " f1 = " + output[1] + " f2 = " + output[2]);

            //проверяем наличие предупреждений
            getWarningsFromResultSet(resultSet);
            getWarningsFromStatement(stmt);
        }catch(SQLException e){
            printSQLException(e);
        }finally {
            closeStatement(stmt);
            closeConnection(conn);
        }

        return output;
    }

    /**
     * Функция фозвращает максимально id содержащиеся в таблице
     * @return
     */
    /*public int getWeightingsMaxId(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        int output = 0;

        try{
            conn = getConnection();

            String query = "SELECT MAX(id) AS max_id " +
                    "FROM weighting ";
            log.info(query);
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
            resultSet.next();
            output = resultSet.getInt("max_id");
            log.info("Результаты запроса" + "\n" +
                    "max_id = " + output);

            //проверяем наличие предупреждений
            getWarningsFromResultSet(resultSet);
            getWarningsFromStatement(stmt);
        }catch(SQLException e){
            printSQLException(e);
        }finally {
            closeStatement(stmt);
            closeConnection(conn);
        }

        return output;
    }

    /**
     * Функция получает показания датчика из базы данных для терх положений ракеты
     * @param id номер измерения
     * @param position номер датчика
     * @return double[]
     */
    /*public double [] getWeightings(int id, Position position){
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        double[] output = new double [3];

        try{
            conn = getConnection();

            String query = "SELECT angle," + position.name().toLowerCase() +
                    " FROM weighting " +
                    "WHERE id = " + id + " AND angle IN (" + Angle.LEFT.ordinal() + ", " +
                                                             Angle.AFLAT.ordinal() + ", " +
                                                             Angle.RIGHT.ordinal() + ");";
            log.info(query);
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
            resultSet.next();
            output[resultSet.getInt("angle")] = resultSet.getDouble(position.name().toLowerCase());
            resultSet.next();
            output[resultSet.getInt("angle")] = resultSet.getDouble(position.name().toLowerCase());
            resultSet.next();
            output[resultSet.getInt("angle")] = resultSet.getDouble(position.name().toLowerCase());
            log.info("Результаты запроса" + "\n" +
                               Angle.LEFT.name() + "=" + output[0] +
                               Angle.AFLAT.name() + "=" + output[1] +
                               Angle.RIGHT.name() + "=" + output[2]);

            //проверяем наличие предупреждений
            getWarningsFromResultSet(resultSet);
            getWarningsFromStatement(stmt);
        }catch(SQLException e){
            printSQLException(e);
        }finally {
            closeStatement(stmt);
            closeConnection(conn);
        }

        return output;
    }

    /**
     * Функция возвращает углы наклона из базы данных измеренные инклинометром
     * @param id калибровки
     * @param angle уловое пожение поворотной платформы
     * @return массив содержажий angle_x и angle_z
     */
    /*public double [] getWeightsAngles(int id, Angle angle){
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        double[] output = new double [2];

        try{
            conn = getConnection();

            String query = "SELECT angle_x, angle_z " +
                    " FROM weighting " +
                    "WHERE id = " + id + " AND angle = " + angle.number() + ";";
            log.info(query);
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
            resultSet.next();
            output[0] = resultSet.getDouble("angle_x");
            output[1] = resultSet.getDouble("angle_z");

            log.info("Результаты запроса" + "\n" +
                    angle.name() + "=" + angle.number() +
                    "angle_x = " + output[0] +
                    "angle_y = " + output[1]);

            //проверяем наличие предупреждений
            getWarningsFromResultSet(resultSet);
            getWarningsFromStatement(stmt);
        }catch(SQLException e){
            printSQLException(e);
        }finally {
            closeStatement(stmt);
            closeConnection(conn);
        }

        return output;
    } */

}
