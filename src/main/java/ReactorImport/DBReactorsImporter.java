package ReactorImport;
import Reactors.Reactor;
import Reactors.ReactorHolder;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;

public class DBReactorsImporter {
    private String filePath;
    public DBReactorsImporter(String filePath) {
        this.filePath=filePath;
    }
    private Connection getConnection() throws SQLException, ClassNotFoundException {
        String dbUrl = "jdbc:sqlite:"+this.filePath;
        Connection connection = DriverManager.getConnection(dbUrl);
        return connection;
    }
    public HashMap<String,HashMap<Integer,Double>> getResult(String group) throws SQLException, ClassNotFoundException {
        Connection connection = this.getConnection();
        HashMap<String,HashMap<Integer,Double>> result = new HashMap<>();
        String query = null;
        switch(group) {
            case "Страна":
                query = "SELECT country as Страна, year, " +
                        "SUM((ROUND(((thermalCapacity/burnup) * LoadFactor.loadFactor)/100, 2))) as yearLoadFactor " +
                        "FROM ReactorsPris " +
                        "JOIN LoadFactor ON ReactorsPris.name = LoadFactor.reactor " +
                        "JOIN ReactorsTypes ON ReactorsPris.type = ReactorsTypes.type " +
                        "GROUP BY country, year " +
                        "ORDER BY country, year;";

                break;
            case "Компания":
                query = "SELECT operator as Компания, year, " +
                        "SUM((ROUND(((thermalCapacity/burnup) * LoadFactor.loadFactor)/100,2))) as yearLoadFactor " +
                        "FROM ReactorsPris " +
                        "JOIN LoadFactor ON ReactorsPris.name=LoadFactor.reactor " +
                        "JOIN ReactorsTypes ON ReactorsPris.type=ReactorsTypes.type " +
                        "GROUP BY operator, year " +
                        "ORDER BY operator, year;";
                break;
            case "Регион":
                query = "SELECT region as Регион, year, " +
                        "SUM((ROUND(((thermalCapacity/burnup) * LoadFactor.loadFactor)/100,2))) as yearLoadFactor " +
                        "FROM ReactorsPris " +
                        "JOIN Countries ON ReactorsPris.country = Countries.country " +
                        "JOIN LoadFactor ON ReactorsPris.name = LoadFactor.reactor " +
                        "JOIN ReactorsTypes ON ReactorsPris.type = ReactorsTypes.type " +
                        "GROUP BY region, year " +
                        "ORDER BY region, year;";
                break;
        }
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while(resultSet.next()){
            if (!result.containsKey(resultSet.getString(group))) {
                result.put(resultSet.getString(group), new HashMap<>());
            }
            if (!result.get(resultSet.getString(group)).containsKey(resultSet.getInt("year"))) {
                result.get(resultSet.getString(group)).put(resultSet.getInt("year"), resultSet.getDouble("yearLoadFactor"));
            }
        }
        connection.close();
        return result;
    }

    public void readReactorsInfo(ReactorHolder reactorHolder) throws IOException, SQLException, ClassNotFoundException {
        Connection connection = null;
        try {
            connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ReactorsPris");
            while (resultSet.next()) {
                String reactorName = resultSet.getString("name");
                String type = resultSet.getString("type");
                String country = resultSet.getString("country");
                String region = connection.createStatement().executeQuery("SELECT region FROM Countries WHERE country = \"" + country + "\"").getString("region");
                String operator = resultSet.getString("operator");
                int burnup = connection.createStatement().executeQuery("SELECT burnup FROM ReactorsTypes WHERE type = \"" + type + "\"").getInt("burnup");
                int thermalCapacity = resultSet.getInt("thermalCapacity");
                int firstGridConnection = resultSet.getInt("firstGridConnection");
                HashMap<Integer, Double> loadFactors = getLoadFactors(connection, reactorName);
                Reactor newreactor = new Reactor(reactorName, type, country, region, operator, burnup, thermalCapacity, firstGridConnection, loadFactors);
                reactorHolder.addReactor(newreactor);
            }
        } catch (SQLException e) {
            throw  e;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw e;
            }
        }
    }

    private static HashMap<Integer, Double> getLoadFactors(Connection connection, String reactorName) throws SQLException {
        HashMap<Integer, Double> loadFactors = new HashMap<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT year, loadfactor FROM LoadFactor WHERE reactor = ?");
        preparedStatement.setString(1, reactorName);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int year = resultSet.getInt("year");
            double loadFactor = resultSet.getDouble("loadfactor");
            loadFactors.put(year, loadFactor);
        }
        return loadFactors;
    }

}
