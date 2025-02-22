// ============================================================================
//
// Copyright (C) 2006-2023 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.editor.nodes;

import org.eclipse.gef.commands.Command;
import org.talend.designer.core.ui.editor.subjobcontainer.ICrossPlatformGroupRequest;
import org.talend.designer.core.ui.editor.subjobcontainer.ICrossPlatformRequest;

public abstract class CrossPlatformComponentEditPolicy extends AbsCrossPlatformEditPolicy {

    public CrossPlatformComponentEditPolicy() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public Command getCommand(ICrossPlatformRequest request) {
        if (REQ_DELETE.equals(request.getType())) {
            return getDeleteCommand((ICrossPlatformGroupRequest) request);
        }
        return null;
    }

    protected Command getDeleteCommand(ICrossPlatformGroupRequest request) {
        return createDeleteCommand(request);
    }

    protected Command createDeleteCommand(ICrossPlatformGroupRequest deleteRequest) {
        return null;
    }

}
