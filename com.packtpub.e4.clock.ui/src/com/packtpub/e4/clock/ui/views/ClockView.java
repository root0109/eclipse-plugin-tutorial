package com.packtpub.e4.clock.ui.views;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;

import com.packtpub.e4.clock.ui.ClockWidget;

public class ClockView extends ViewPart {
	public void createPartControl(Composite parent) {
		
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		parent.setLayout(layout);
		
		final ClockWidget clock  = new ClockWidget(parent, SWT.NONE, new RGB(255,0,0));
		final ClockWidget clock2 = new ClockWidget(parent, SWT.NONE, new RGB(0,255,0));
		final ClockWidget clock3 = new ClockWidget(parent, SWT.NONE, new RGB(0,0,255));
		
		clock.setLayoutData(new RowData(100,100));
		clock2.setLayoutData(new RowData(100,100));
		clock3.setLayoutData(new RowData(100,100));
	}

	public void setFocus() {
	}
}