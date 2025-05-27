/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.studio.importer.bpmn;


import org.bonitasoft.studio.common.Strings;
import org.bonitasoft.studio.importer.i18n.Messages;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Dialog that allows to select the tool that exported a BPMN file.
 * @author Adrien Aubert
 *
 */
public class SelectBPMNExportToolDialog extends MessageDialog {

    private static final String[] buttons = new String[] {IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL};
    private final String[] tools;
    private static final String OTHER_ELEMENT = Messages.importBPMNFileSelectExporterOtherOption;
    private Text textInput;
    private ComboViewer toolSelectionInput;
    private Composite questionElement;
    
    private String selectedTool;
    private String manualValue;

    public SelectBPMNExportToolDialog(final Shell parentShell) {
        super(parentShell, Messages.importBPMNFileSelectOriginTitle, null,
                Messages.importBPMNFileSelectOriginMessage, MessageDialog.QUESTION_WITH_CANCEL,
                buttons, 0);
        this.tools = new String[]{"Camunda Modeler", "Signavio", "Bizagi", OTHER_ELEMENT};
    }
    

    @Override
    protected Point getInitialSize() {
        return getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
    }

    @Override
    protected Control createCustomArea(Composite parent) {
        final Composite mainComposite = new Composite(parent, SWT.NONE);
        mainComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
        mainComposite.setLayout(
                GridLayoutFactory.fillDefaults().numColumns(1).margins(0, 0).extendedMargins(10, 10, 10, 0).create());
        this.createToolListComponent(mainComposite);
        this.createOtherToolInput(mainComposite);
        this.toolSelectionInput.addSelectionChangedListener(new ISelectionChangedListener() {
            
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                final IStructuredSelection selection = event.getStructuredSelection();
                if (!selection.isEmpty()) {
                    SelectBPMNExportToolDialog.this.setSelectedTool((String) selection.getFirstElement());
                }
            }
        });
        this.textInput.addKeyListener(new KeyAdapter() {
            
            @Override
            public void keyReleased(KeyEvent e) {
                SelectBPMNExportToolDialog.this.setManualInput(SelectBPMNExportToolDialog.this.textInput.getText());
            }
        });

        return mainComposite;
    }
    
    @Override
    protected Control createButtonBar(Composite parent) {
        final Control control = super.createButtonBar(parent);
        this.getButton(IDialogConstants.OK_ID).setEnabled(false);
        return control;
    }

    protected void setSelectedTool(final String tool) {
        this.selectedTool = tool;
        this.manualValue = null; // Reseting the manual input when the selected tool changes
        this.refreshUi();
    }
    
    protected void setManualInput(final String value) {
        this.manualValue = value;
        this.refreshUi();
    }
    
    /**
     * Refresh the ui state from the model.
     */
    private void refreshUi() {
        final boolean isOtherElementSelected = this.isOtherElementSelected();
        this.questionElement.setVisible(isOtherElementSelected);
        this.questionElement.setData(this.manualValue);
        final boolean canValidate = !Strings.isNullOrEmpty(this.getValue());
        this.getButton(IDialogConstants.OK_ID).setEnabled(canValidate);
    }
    
    private boolean isOtherElementSelected() {
        return OTHER_ELEMENT.equals(this.selectedTool);
    }

    private void createToolListComponent(final Composite parent) {
        final Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).spacing(5, 3).create());
        panel.setLayoutData(GridDataFactory.fillDefaults().grab(false, true).create());
        
        final Label label = new Label(panel, SWT.WRAP);
        label.setText(Messages.importBPMNFileSelectExporterLabel);
        
        this.toolSelectionInput = new ComboViewer(panel, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
        toolSelectionInput.getCombo().setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
        toolSelectionInput.setContentProvider(new ArrayContentProvider());
        toolSelectionInput.setInput(this.tools);
    }
    
    private void createOtherToolInput(final Composite parent) {
        this.questionElement = new Composite(parent, SWT.NONE);
        this.questionElement.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).spacing(5, 3).create());
        this.questionElement.setLayoutData(GridDataFactory.fillDefaults().grab(false, true).create());
        this.questionElement.setVisible(false);
        
        final Label descriptionLabel = new Label(this.questionElement, SWT.WRAP);
        descriptionLabel.setText(Messages.importBPMNFileSelectExporterOtherToolInputLabel);
        
        this.textInput = new Text(this.questionElement, SWT.BORDER);
        this.textInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }
    
    public String getValue() {
        if (this.isOtherElementSelected()) {
            return this.manualValue;
        }
        return this.selectedTool;
    }
}
