package ReactorImport;

public class UniversalReactorImporter {

    public static ReactorImporterBase createImporterChain() {
        JSONReactorImporter jsonImporter = new JSONReactorImporter();
        XMLReactorImporter xmlImporter = new XMLReactorImporter();
        YAMLReactorImporter yamlImporter = new YAMLReactorImporter();

        xmlImporter.setSuccessor(yamlImporter);
        jsonImporter.setSuccessor(xmlImporter);

        return jsonImporter;
    }
}