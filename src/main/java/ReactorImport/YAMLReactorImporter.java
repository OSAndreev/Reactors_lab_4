package ReactorImport;

import Reactors.Reactor;
import Reactors.ReactorsOwner;
import org.yaml.snakeyaml.Yaml;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

public class YAMLReactorImporter extends ReactorImporterBase {

    @Override
    public void processFile(File file, ReactorsOwner reactorsOwner) {
        if (file.getName().endsWith(".yaml") || file.getName().endsWith(".yml")) {
            try {
                Yaml yaml = new Yaml();
                FileInputStream inputStream = new FileInputStream(file);
                Iterable<Object> objects = yaml.loadAll(inputStream);
                for (Object obj : objects) {
                    Map<String, ?> map = (Map<String, ?>) obj;
                    for (String key : map.keySet()) {
                        Map<?, ?> innerMap = (Map<?, ?>) map.get(key);
                        Reactor reactor = parseReactor(innerMap);
                        reactorsOwner.addReactor(key, reactor);
                    }
                }
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (successor != null) {
            successor.processFile(file, reactorsOwner);
        } else {
            System.out.println("Не удается распознать формат файла");
        }
    }

    private Reactor parseReactor(Map<?, ?> map) {
        return new Reactor(
                (String) map.get("type"),
                (String) map.get("class"),
                ((Number) map.get("burnup")).doubleValue(),
                ((Number) map.get("kpd")).doubleValue(),
                ((Number) map.get("enrichment")).doubleValue(),
                ((Number) map.get("termal_capacity")).doubleValue(),
                ((Number) map.get("electrical_capacity")).doubleValue(),
                (Integer) map.get("life_time"),
                ((Number) map.get("first_load")).doubleValue(),
                "YAML"
        );
    }
}
