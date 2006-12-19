// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.repository.ui.wizards;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class RepositoryWizard extends Wizard {

    protected static Logger log = Logger.getLogger(RepositoryWizard.class);

    protected static final String PID = RepositoryPlugin.PLUGIN_ID;

    private IWorkbench workbench;

    protected ISelection selection;

    protected IRepositoryObject repositoryObject = null;

    protected String[] existingNames;

    protected IPath pathToSave = null;

    protected boolean creation;

    private boolean repositoryObjectEditable = true;

    public RepositoryWizard(IWorkbench workbench, boolean creation) {
        super();
        this.workbench = workbench;
        this.creation = creation;
    }

    protected void selectAndReveal(ISelection selection) {
        // PTODO OCA : replace this by a code on the class createAction (sample : CreateConnectionAction)
        // the refrech of the repositoryView must be manage by the actions and not by the Wizard.
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();

        IWorkbenchPage page = window.getActivePage();
        if (page == null) {
            return;
        }

        Object obj = ((IStructuredSelection) selection).getFirstElement();
        RepositoryNode node = (RepositoryNode) obj;
        if (page.getActivePart() instanceof IRepositoryView) {
            IRepositoryView viewPart = (IRepositoryView) page.getActivePart();
            viewPart.refresh(node);
        }
    }

    /**
     * Getter for workbench.
     * 
     * @return the workbench
     */
    public IWorkbench getWorkbench() {
        return this.workbench;
    }

    /**
     * Sets the workbench.
     * 
     * @param workbench the workbench to set
     */
    public void setWorkbench(IWorkbench workbench) {
        this.workbench = workbench;
    }
    
    /**
     * DOC ocarbone Comment method "performCancel". Unlock the IRepositoryObject before the close of the wizard.
     * 
     * @param IRepositoryObject
     */
    public boolean performCancel() {
        reload();
        closeLockStrategy();
        return true;
    }

    /**
     * DOC mhelleboid Comment method "reload".
     */
    private void reload() {
        if (repositoryObject != null) {
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            Property property = factory.reload(repositoryObject.getProperty());
            repositoryObject.setProperty(property);
        }
    }

    /**
     * Sets the repositoryObject.
     * 
     * @param repositoryObject the repositoryObject to set
     */
    public void setRepositoryObject(IRepositoryObject repositoryObject) {
        this.repositoryObject = repositoryObject;
        calculateRepositoryObjectEditable();
    }

    /**
     * DOC ocarbone Comment method "isRepositoryObjectEditable". Check the RepositoryObject (is locked or on recycle
     * bin) and return a boolean represent if the RepositoryObject is editable (true) or readOnly (false).
     * 
     * @return boolean
     */
    public boolean isRepositoryObjectEditable() {
        return repositoryObjectEditable;
    }

    /**
     * DOC matthieu Comment method "calculateObjectEditable".
     * 
     * @return
     */
    private void calculateRepositoryObjectEditable() {
        if (repositoryObject != null) {
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            repositoryObjectEditable = factory.isEditableAndLockIfPossible(repositoryObject);
        }
    }

    /**
     * DOC ocarbone Comment method "initLockStrategy". If needed, lock the repositoryObject.
     * 
     */
    public void initLockStrategy() {
        // The lock strategy is useless when the repositoryObject isn't yet created
        if (repositoryObject != null) {
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                factory.lock(repositoryObject);
            } catch (PersistenceException e1) {
                String detailError = e1.toString();
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("CommonWizard.persistenceException"),
                        detailError);
                log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError);
            }
        }
    }

    /**
     * DOC ocarbone Comment method "performCancel". Unlock the IRepositoryObject before the close of the wizard.
     * 
     * @param IRepositoryObject
     */
    public void closeLockStrategy() {
        // The lock strategy is useless when the repositoryObject isn't created
        if (repositoryObject != null) {
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                factory.unlock(repositoryObject);
            } catch (PersistenceException e) {
                String detailError = e.toString();
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("CommonWizard.persistenceException"),
                        detailError);
                log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError);
            }
        }
    }
}
