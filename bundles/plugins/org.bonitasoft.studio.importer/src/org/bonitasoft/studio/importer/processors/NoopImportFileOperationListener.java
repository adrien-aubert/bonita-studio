package org.bonitasoft.studio.importer.processors;

/**
 * Listener that does nothing. Used to avoid NPE.
 * @author Adrien AUBERT
 *
 */
public class NoopImportFileOperationListener implements ImportFileOperationListener {

    @Override
    public void diagramCreated(ToProcProcessor processor) {

    }

    @Override
    public void importCompleted(ToProcProcessor processor) {

    }

}
