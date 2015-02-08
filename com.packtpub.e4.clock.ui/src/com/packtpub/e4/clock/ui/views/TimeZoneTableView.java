package com.packtpub.e4.clock.ui.views;

import java.util.TimeZone;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.packtpub.e4.clock.ui.internal.TimeZoneDisplayNameColumn;
import com.packtpub.e4.clock.ui.internal.TimeZoneIDColumn;
import com.packtpub.e4.clock.ui.internal.TimeZoneOffsetColumn;
import com.packtpub.e4.clock.ui.internal.TimeZoneSelectionListener;
import com.packtpub.e4.clock.ui.internal.TimeZoneSummerTimeColumn;

public class TimeZoneTableView extends ViewPart {
	private TableViewer tableViewer;
	
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
		tableViewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		String[] ids = TimeZone.getAvailableIDs();
		TimeZone[] timeZones = new TimeZone[ids.length];
		for (int i = 0; i < ids.length; i++) {
			timeZones[i] = TimeZone.getTimeZone(ids[i]);
		}
		
		new TimeZoneIDColumn().addColumnTo(tableViewer);
		new TimeZoneOffsetColumn().addColumnTo(tableViewer);
		new TimeZoneDisplayNameColumn().addColumnTo(tableViewer);
		new TimeZoneSummerTimeColumn().addColumnTo(tableViewer);
		
		tableViewer.setInput(timeZones);
		
		getSite().setSelectionProvider(tableViewer);
		
		selectionListener = new TimeZoneSelectionListener(tableViewer, getSite().getPart());
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(selectionListener);
	}

	public void setFocus() {
		tableViewer.getControl().setFocus();
	}
}
