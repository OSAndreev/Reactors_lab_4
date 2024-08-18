package Reactors;

import java.util.HashMap;

public class Reactor {
    private String name;
    private String type;
    private  String country;
    private  String region;
    private String operator;
    private int burnup;
    private int thermalCapacity;
    private int firstGridConnection;
    private HashMap<Integer, Double> loadFactorPerYear;

    private HashMap<Integer, Double> consumptionPerYear;

    public Reactor(
            String reactorName, String type, String country, String region,
            String operator, int burnup, int thermalCapacity, int firstGridConnection,
            HashMap<Integer, Double> loadFactorPerYear
    ) {
        this.name = reactorName;
        this.type = type;
        this.country = country;
        this.region = region;
        this.operator = operator;
        this.burnup = burnup;
        this.thermalCapacity = thermalCapacity;
        this.firstGridConnection = firstGridConnection;
        this.loadFactorPerYear = loadFactorPerYear;
        this.consumptionPerYear = new HashMap<Integer, Double>();
    }


    public String getDetailedDescription() {
        return "Имя реактора: " + name + "\n" +
                "Выгорание: " + burnup + "\n" +
                "Тип: " + type + "\n" +
                "Тепловая мощность: " + thermalCapacity + "\n"
               ;
    }
}
