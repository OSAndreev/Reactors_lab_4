package Reactors;

public class Reactor {
    private String type;
    private String reactorClass;
    private Double burnup;
    private Double electricalCapacity;
    private Double enrichment;
    private Double firstLoad;
    private Double efficiency;
    private Integer lifeTime;
    private Double thermalCapacity;
    private String source;

    public Reactor(
            String type, String reactorClass, Double burnup,
            Double efficiency, Double enrichment, Double thermalCapacity,
            Double electricalCapacity, Integer lifeTime, Double firstLoad,
            String source
    ) {
        this.type = type;
        this.reactorClass = reactorClass;
        this.burnup = burnup;
        this.electricalCapacity = electricalCapacity;
        this.enrichment = enrichment;
        this.firstLoad = firstLoad;
        this.efficiency = efficiency;
        this.lifeTime = lifeTime;
        this.thermalCapacity = thermalCapacity;
        this.source = source;
    }

    @Override
    public String toString() {
        return reactorClass;
    }

    public String getDetailedDescription() {
        return "Класс реактора: " + reactorClass + "\n" +
                "Выгорание: " + burnup + "\n" +
                "Эффективность: " + efficiency + "\n" +
                "Обогащение: " + enrichment + "\n" +
                "Тепловая мощность: " + thermalCapacity + "\n" +
                "Электрическая мощность: " + electricalCapacity + "\n" +
                "Срок службы: " + lifeTime + "\n" +
                "Первая загрузка: " + firstLoad + "\n" +
                "Источник: " + source;
    }
}
