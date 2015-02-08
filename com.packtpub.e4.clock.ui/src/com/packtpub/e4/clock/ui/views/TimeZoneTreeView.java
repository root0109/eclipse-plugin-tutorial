package com.packtpub.e4.clock.ui.views;

import java.net.URL;
import java.util.TimeZone;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySource;

import com.packtpub.e4.clock.ui.internal.TimeZoneComparator;
import com.packtpub.e4.clock.ui.internal.TimeZoneDialog;
import com.packtpub.e4.clock.ui.internal.TimeZoneSelectionListener;
import com.packtpub.e4.clock.ui.internal.TimeZoneViewerComparator;
import com.packtpub.e4.clock.ui.internal.TimeZoneViewerFilter;

public class TimeZoneTreeView extends ViewPart {

	private TreeViewer treeViewer;
	
	private TimeZoneSelectionListener selectionListener;

	public void dispose() {
		if (selectionListener != null) {
			getSite().getWorkbenchWindow().getSelectionService()
					.removeSelectionListener(selectionListener);
			selectionListener = null;
		}
		super.dispose();
	}

	public void createPartControl(Composite parent) {
		treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		
		ResourceManager rm = JFaceResources.getResources();
		LocalResourceManager lrm = new LocalResourceManager(rm,parent);
		
		ImageRegistry ir = new ImageRegistry(lrm);
		URL sample = getClass().getResource("/icons/sample.gif");
		URL nuke = getClass().getResource("/icons/nuke.gif");
		
		ir.put("sample", ImageDescriptor.createFromURL(sample));
		ir.put("nuke", ImageDescriptor.createFromURL(nuke));
		
		FontRegistry fr = JFaceResources.getFontRegistry();
		
		treeViewer.setLabelProvider(new DelegatingStyledCellLabelProvider(new TimeZoneLabelProvider(ir, fr)));
		treeViewer.setContentProvider(new TimeZoneContentProvider());
		treeViewer.setInput(new Object[]{TimeZoneComparator.getTimeZones()});
		
		//treeViewer.setData("REVERSE",Boolean.TRUE);
		treeViewer.setComparator(new TimeZoneViewerComparator());
		
		treeViewer.setFilters(new ViewerFilter[] {new TimeZoneViewerFilter("GMT")});
		
		treeViewer.setExpandPreCheckFilters(true);
		treeViewer.expandAll();
		
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				Viewer viewer = event.getViewer();
				Shell shell = viewer.getControl().getShell();

				ISelection sel = viewer.getSelection();
				Object selectedValue;
				if (!(sel instanceof IStructuredSelection) || sel.isEmpty()) {
					selectedValue = null;
				} else {
					selectedValue = ((IStructuredSelection) sel).getFirstElement();
				}

				if (selectedValue instanceof TimeZone) {
					TimeZone timeZone = (TimeZone) selectedValue;
					new TimeZoneDialog(shell, timeZone).open();
				}
			}
		});
		
		getSite().setSelectionProvider(treeViewer);
		
		selectionListener = new TimeZoneSelectionListener(
				 treeViewer, getSite().getPart());
				getSite().getWorkbenchWindow().getSelectionService()
				 .addSelectionListener(selectionListener);

	}

	public void setFocus() {
		treeViewer.getControl().setFocus();
	}

}
