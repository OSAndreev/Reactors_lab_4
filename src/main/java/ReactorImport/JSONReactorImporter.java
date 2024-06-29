package ReactorImport;

import Reactors.Reactor;
import Reactors.ReactorsOwner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class JSONReactorImporter extends ReactorImporterBase {

    @Override
    public void processFile(File file, ReactorsOwner reactorsOwner) {
        if (file.getName().endsWith(".json")) {
            ObjectMapper mapper = new ObjectMapper();

            JsonNode rootNode;
            try {
                rootNode = mapper.readTree(file);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            for (Iterator<String> it = rootNode.fieldNames(); it.hasNext();) {
                String fieldName = it.next();
                JsonNode reactorNode = rootNode.get(fieldName);
                if (reactorNode != null && reactorNode.isObject()) {
                    Reactor reactor = parseReactor(reactorNode);
                    reactorsOwner.addReactor(fieldName, reactor);
                }
            }
        } else if (successor != null) {
            successor.processFile(file, reactorsOwner);
        } else {
            System.out.println("Не удается распознать формат файла");
        }
    }

    private Reactor parseReactor(JsonNode node) {
        return new Reactor(
                node.has("type") ? node.get("type").asText() : null,
                node.has("class") ? node.get("class").asText() : null,
                node.has("burnup") ? node.get("burnup").asDouble() : 0.0,
                node.has("kpd") ? node.get("kpd").asDouble() : 0.0,
                node.has("enrichment") ? node.get("enrichment").asDouble() : 0.0,
                node.has("termal_capacity") ? node.get("termal_capacity").asDouble() : 0.0,
                node.has("electrical_capacity") ? node.get("electrical_capacity").asDouble() : 0.0,
                node.has("life_time") ? node.get("life_time").asInt() : 0,
                node.has("first_load") ? node.get("first_load").asDouble() : 0.0,
                "JSON"
        );
    }
}
