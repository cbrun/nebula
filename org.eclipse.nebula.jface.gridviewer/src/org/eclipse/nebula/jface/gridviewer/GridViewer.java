/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    rmcamara@us.ibm.com - initial API and implementation
 *    tom.schindl@bestsolution.at - various significant contributions
 *******************************************************************************/ 

package org.eclipse.nebula.jface.gridviewer;

import org.eclipse.jface.viewers.AbstractTableViewer;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ViewerRow;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Widget;

/**
 * A concrete viewer based on an Grid control.
 * <p>
 * This class is not intended to be subclassed outside the viewer framework. It
 * is designed to be instantiated with a pre-existing Grid control and
 * configured with a domain-specific content provider, label provider, element
 * filter (optional), and element sorter (optional).
 * <p>
 * Content providers for grid viewers must not implement the
 * {@code ITreeContentProvider} interface.
 * <p>
 */
public class GridViewer extends AbstractTableViewer
{
    /** This viewer's grid control. */
    private Grid grid;
    
    private GridViewerRow cachedRow;

    /**
     * Creates a grid viewer on a newly-created grid control under the given
     * parent. The grid control is created using the SWT style bits
     * <code>MULTI, H_SCROLL, V_SCROLL,</code> and <code>BORDER</code>. The
     * viewer has no input, no content provider, a default label provider, no
     * sorter, and no filters.
     * 
     * @param parent the parent control
     */
    public GridViewer(Composite parent)
    {
        this(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
    }

    /**
     * Creates a grid viewer on a newly-created grid control under the given
     * parent. The grid control is created using the given SWT style bits. The
     * viewer has no input, no content provider, a default label provider, no
     * sorter, and no filters.
     * 
     * @param parent the parent control
     * @param style the SWT style bits used to create the grid.
     */
    public GridViewer(Composite parent, int style)
    {
        this(new Grid(parent, style));
    }

    /**
     * Creates a grid viewer on the given grid control. The viewer has no
     * input, no content provider, a default label provider, no sorter, and no
     * filters.
     * 
     * @param grid the grid control
     */
    public GridViewer(Grid grid)
    {
        this.grid = grid;
        hookControl(grid);
    }
    
    /**
     * Returns the underlying Grid Control. 
     * 
     * @return grid control.
     */
    public Grid getGrid()
    {
        return grid;
    }

    /** {@inheritDoc} */
    protected ViewerRow internalCreateNewRowPart(int style, int rowIndex)
    {
    	GridItem item;

		if (rowIndex >= 0) {
			item = new GridItem(grid, style, rowIndex);
		} else {
			item = new GridItem(grid, style);
		}

		return getViewerRowFromItem(item);
    }

    /** {@inheritDoc} */
    protected ColumnViewerEditor createViewerEditor() 
    {
        return new GridViewerEditor(this,new ColumnViewerEditorActivationStrategy(this),ColumnViewerEditor.DEFAULT);
    }

    /** {@inheritDoc} */
    protected void doClear(int index)
    {
        // TODO Fix when grid supports virtual
    }

    /** {@inheritDoc} */
    protected void doClearAll()
    {
        //TODO Fix when grid supports virtual
    }
    
    /** {@inheritDoc} */
    protected void doSetItemCount(int count)
    {
        //TODO Once grid supports virtual
    }

    /** {@inheritDoc} */
    protected void doDeselectAll()
    {
        grid.deselectAll();
    }

    /** {@inheritDoc} */
    protected Widget doGetColumn(int index)
    {
        return grid.getColumn(index);
    }

    /** {@inheritDoc} */
    protected int doGetColumnCount()
    {
        return grid.getColumnCount();
    }

    /** {@inheritDoc} */
    protected Item doGetItem(int index)
    {
        return grid.getItem(index);
    }

    /** {@inheritDoc} */
    protected int doGetItemCount()
    {
        return grid.getItemCount();
    }

    /** {@inheritDoc} */
    protected Item[] doGetItems()
    {
        return grid.getItems();
    }

    /** {@inheritDoc} */
    protected Item[] doGetSelection()
    {
        return grid.getSelection();
    }

    /** {@inheritDoc} */
    protected int[] doGetSelectionIndices()
    {
        return grid.getSelectionIndices();
    }

    /** {@inheritDoc} */
    protected int doIndexOf(Item item)
    {
        return grid.indexOf((GridItem) item);
    }

    /** {@inheritDoc} */
    protected void doRemove(int[] indices)
    {
        grid.remove(indices);
    }

    /** {@inheritDoc} */
    protected void doRemove(int start, int end)
    {
        grid.remove(start, end);
    }

    /** {@inheritDoc} */
    protected void doRemoveAll()
    {
        grid.removeAll();
    }

    /** {@inheritDoc} */
    protected void doSetSelection(Item[] items)
    {
        GridItem[] items2 = new GridItem[items.length];
        for (int i = 0; i < items.length; i++)
        {
            items2[i] = (GridItem) items[i];
        }
        grid.setSelection(items2);
    }

    /** {@inheritDoc} */
    protected void doSetSelection(int[] indices)
    {
        grid.setSelection(indices);
    }

    /** {@inheritDoc} */
    protected void doShowItem(Item item)
    {
        grid.showItem((GridItem)item);
    }

    /** {@inheritDoc} */
    protected void doShowSelection()
    {
        grid.showSelection();
    }

    /** {@inheritDoc} */
    protected Item getItemAt(Point point)
    {
        return grid.getItem(point);
    }

    /** {@inheritDoc} */
    public Control getControl()
    {
        return grid;
    }
    
    /** {@inheritDoc} */
    protected ViewerRow getViewerRowFromItem(Widget item) 
    {
    	if( cachedRow == null ) {
			cachedRow = new GridViewerRow((GridItem) item);
		} else {
			cachedRow.setItem((GridItem) item);
		}
		
		return cachedRow;
    }

    
    
    /** 
     * {@inheritDoc}
     */
    protected void doResetItem(Item item) {
		GridItem gridItem = (GridItem) item;
		int columnCount = Math.max(1, grid.getColumnCount());
		for (int i = 0; i < columnCount; i++) {
			gridItem.setText(i, ""); //$NON-NLS-1$
			gridItem.setImage(null);
		}
	}

	@Override
	protected void doSelect(int[] indices) {
		grid.select(indices);
	}
}
