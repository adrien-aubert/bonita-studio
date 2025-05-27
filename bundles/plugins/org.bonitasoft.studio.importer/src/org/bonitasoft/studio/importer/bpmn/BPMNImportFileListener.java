package org.bonitasoft.studio.importer.bpmn;

import java.util.concurrent.atomic.AtomicReference;

import org.bonitasoft.studio.common.Strings;
import org.bonitasoft.studio.common.log.BonitaStudioLog;
import org.bonitasoft.studio.importer.processors.ExportToolInfo;
import org.bonitasoft.studio.importer.processors.TypedImportFileOperationListener;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;

public class BPMNImportFileListener extends TypedImportFileOperationListener<BPMNToProc> {

    protected BPMNImportFileListener() {
        super(BPMNToProc.class);
    }

    @Override
    public void doDiagramCreated(final BPMNToProc processor) {
        if (processor.getExporterInformation().isEmpty()) {
            this.fillExporterInformation(processor);
        }
    }

    /**
     * Opens a dialog to ask the user to set the name of the tool that exported
     * the file.
     * @param processor
     */
    private void fillExporterInformation(final BPMNToProc processor) {
        final AtomicReference<String> reference = new AtomicReference<String>();
        final var dialog = new SelectBPMNExportToolDialog(Display.getDefault().getActiveShell());
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                final int code = dialog.open();
                if (code == IDialogConstants.OK_ID) {
                    reference.set(dialog.getValue());
                }
            }
        });
        
        final String toolName = reference.get();
        if (Strings.isNullOrEmpty(toolName)) {
            processor.cancel();
            BonitaStudioLog.info("BPMN file import cancelled", BPMNImportFileListener.class);
        } else {
            processor.setExporterInformation(toolName);
            BonitaStudioLog.debug(String.format("Setting file exporter to %s", toolName), BPMNImportFileListener.class);
        }
    }

    @Override
    public void doImportCompleted(final BPMNToProc processor) {
        String exporterName = null;
        String exporterVersion = null;
        if (processor.getExporterInformation().isPresent()) {
            final ExportToolInfo exporter = processor.getExporterInformation().get();
            exporterName = exporter.getName();
            exporterVersion = exporter.getVersion() != null ? exporter.getVersion() : "";
        }
        BonitaStudioLog.info(String.format("BPMN file imported from %s %s", exporterName, exporterVersion), BPMNImportFileListener.class);
    }
}
