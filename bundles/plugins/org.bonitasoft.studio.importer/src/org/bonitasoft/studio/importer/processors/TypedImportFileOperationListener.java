package org.bonitasoft.studio.importer.processors;

/**
 * Base class meant to be extended. It allows the children class to access
 * a processor casted in the implementation, not the interface.
 * @author Adrien AUBERT
 *
 * @param <T> The class of the processor that is used.
 */
public abstract class TypedImportFileOperationListener<T extends ToProcProcessor> implements ImportFileOperationListener {
    private Class<T> clazz;

    /**
     * Default Constructor.
     * @param clazz The class of the processor.
     */
    protected TypedImportFileOperationListener(final Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public final void diagramCreated(ToProcProcessor processor) {
        if (this.clazz.isInstance(processor)) {
            this.doDiagramCreated(this.clazz.cast(processor));
        }
    }

    /**
     * @see ImportFileOperationListener#diagramCreated(ToProcProcessor)
     */
    protected abstract void doDiagramCreated(T processor);

    public final void importCompleted(ToProcProcessor processor) {
        if (this.clazz.isInstance(processor)) {
            this.doImportCompleted(this.clazz.cast(processor));
        }
    }

    /**
     * @see ImportFileOperationListener#doImportCompleted(ToProcProcessor)
     */
    protected abstract void doImportCompleted(T processor);
}
