package org.bonitasoft.studio.importer.processors;

import java.util.Objects;

public class ExportToolInfo {

    private String name;
    private String version;

    public ExportToolInfo(String name) {
        this(name, null);
    }
    
    public ExportToolInfo(String name, String version) {
        this.name = Objects.requireNonNull(name);
        this.version = version;
    }
    
    
    public String getName() {
        return this.name;
    }
    
    public String getVersion() {
        return this.version;
    }
    
    public String toString() {
        return this.version != null ? this.name + " " + this.version : this.name;
    }
}
