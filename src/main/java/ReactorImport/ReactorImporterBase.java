package ReactorImport;

import Reactors.ReactorsOwner;

import java.io.File;

public abstract class ReactorImporterBase {
    protected ReactorImporterBase successor;

    public void setSuccessor(ReactorImporterBase successor) {
        this.successor = successor;
    }

    public abstract void processFile(File file, ReactorsOwner reactorsOwner);
}
