package org.bonitasoft.studio.importer.processors;

/**
 * Listener that will be called when processing a file import.
 * @author Adrien Aubert
 *
 */
public interface ImportFileOperationListener {
    /**
     * Called when a diagram has been created from the file.
     * @param processor The processor responsible of the file import.
     */
    public void diagramCreated(ToProcProcessor processor);
    
    /**
     * Called when the file import process has ended successfully.
     * @param processor The processor responsible of the file import.
     */
    public void importCompleted(ToProcProcessor processor);
}
