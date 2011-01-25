// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.talend.designer.xmlmap.figures.layout.EqualWidthLayout;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.ui.color.ColorInfo;
import org.talend.designer.xmlmap.ui.color.ColorProviderMapper;

/**
 * wchen class global comment. Detailled comment
 */
public class XmlMapDataEditPart extends BaseEditPart {

    private IFigure leftFigure;

    private IFigure centerFigure;

    private IFigure rightFigure;

    @Override
    protected IFigure createFigure() {
        // Figure mainFigure = new FreeformLayer();
        // figure.setBackgroundColor(ColorConstants.red);
        // figure.setLayoutManager(new FreeformLayout());
        Figure mainFigure = new Figure();

        // mainFigure.setLayoutManager(new ToolbarLayout() {
        //
        // @Override
        // public void layout(IFigure parent) {
        // super.layout(parent);
        // List children = parent.getChildren();
        // int numChildren = children.size();
        // Rectangle clientArea = transposer.t(parent.getClientArea());
        // int clientx = clientArea.x;
        // int clienty = clientArea.y;
        // int clientWidth = clientArea.width;
        // for (int i = 0; i < numChildren; i++) {
        // IFigure child = (IFigure) children.get(i);
        // Rectangle newBounds = new Rectangle(clientx, clienty, -1, clientArea.height);
        // int devideWidth = (clientWidth - (numChildren - 1) * spacing) / numChildren;
        // newBounds.width = devideWidth;
        // child.setBounds(transposer.t(newBounds));
        // clientx += newBounds.width + spacing;
        // }
        // }
        // });

        EqualWidthLayout manager = new EqualWidthLayout();
        manager.setUseParentHeight(true);
        mainFigure.setLayoutManager(manager);

        // input
        ScrollPane scrollPane = new ScrollPane();
        leftFigure = new RectangleFigure();
        leftFigure.setBorder(new LineBorder(ColorConstants.darkBlue));
        ToolbarLayout subManager = new ToolbarLayout();
        subManager.setSpacing(20);
        subManager.setVertical(true);
        leftFigure.setLayoutManager(subManager);
        leftFigure.setBorder(new MarginBorder(5));
        scrollPane.getViewport().setContents(leftFigure);
        mainFigure.add(scrollPane);

        // var
        scrollPane = new ScrollPane();
        centerFigure = new RectangleFigure();
        centerFigure.setBorder(new LineBorder(ColorConstants.darkBlue));
        // GridLayout centerLayout = new GridLayout();
        // centerFigure.setLayoutManager(centerLayout);
        subManager = new ToolbarLayout();
        subManager.setSpacing(20);
        subManager.setVertical(true);
        centerFigure.setLayoutManager(subManager);
        centerFigure.setBorder(new MarginBorder(5));

        scrollPane.getViewport().setContents(centerFigure);
        mainFigure.add(scrollPane);

        // output
        scrollPane = new ScrollPane();
        rightFigure = new RectangleFigure();
        rightFigure.setBorder(new LineBorder(ColorConstants.darkBlue));
        subManager = new ToolbarLayout();
        subManager.setSpacing(20);
        subManager.setVertical(true);
        rightFigure.setLayoutManager(subManager);
        rightFigure.setBorder(new MarginBorder(5));
        scrollPane.getViewport().setContents(rightFigure);
        mainFigure.add(scrollPane);

        mainFigure.setOpaque(true);
        mainFigure.setBackgroundColor(ColorProviderMapper.getColor(ColorInfo.COLOR_BACKGROUND_LINKS_ZONE));
        return mainFigure;
    }

    @Override
    protected void addChildVisual(EditPart childEditPart, int index) {
        IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
        if (childEditPart instanceof InputXmlTreeEditPart) {
            /* get first figure to put all input tables figures in */
            leftFigure.add(child);
        }
        if (childEditPart instanceof OutputXmlTreeEditPart) {
            /* get third figure to put all output tables figures in */
            rightFigure.add(child);
        }
        if (childEditPart instanceof VarTableEditPart) {
            /* get third figure to put all output tables figures in */
            // GridData gridData = new GridData(GridData.CENTER);
            // gridData.horizontalIndent = 75;
            // gridData.widthHint = 300;
            // if (child instanceof CenterVarFigure) {
            // GridLayout centerLayout = (GridLayout) centerFigure.getLayoutManager();
            // centerLayout.setConstraint((IFigure) child, gridData);
            // }
            centerFigure.add(child);
        }

        // getContentPane().add(child, index);
    }

    @Override
    protected List getModelChildren() {
        List children = new ArrayList();
        children.addAll(((XmlMapData) getModel()).getInputTrees());
        children.addAll(((XmlMapData) getModel()).getOutputTrees());
        children.addAll(((XmlMapData) getModel()).getVarTables());
        return children;
    }

}
