package com.packtpub.e4.clock.ui.views;

import java.util.TimeZone;

import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;

import com.packtpub.e4.clock.ui.ClockWidget;

public class ClockView extends ViewPart {
	private Combo timezones;
	
	public void createPartControl(Composite parent) {
		
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		parent.setLayout(layout);
		
		final ClockWidget clock  = new ClockWidget(parent, SWT.NONE, new RGB(255,0,0));
		final ClockWidget clock2 = new ClockWidget(parent, SWT.NONE, new RGB(0,255,0));
		final ClockWidget clock3 = new ClockWidget(parent, SWT.NONE, new RGB(0,0,255));
		
		clock.setLayoutData(new RowData(100,100));
		clock2.setLayoutData(new RowData(100,100));
		clock3.setLayoutData(new RowData(100,100));
		
		createTimezones(parent);
		
		timezones.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				 String z = timezones.getText();
				 TimeZone tz = (z == null ? null : TimeZone.getTimeZone(z));
				 TimeZone dt = TimeZone.getDefault();
				 int offset = tz == null ? 0 : (
						 tz.getOffset(System.currentTimeMillis()) -
						 dt.getOffset(System.currentTimeMillis())) / 3600000;
						 clock3.setOffset(offset);
						 clock3.redraw();
						 }
			
				 public void widgetDefaultSelected(SelectionEvent e) {
					 clock3.setOffset(0);
					 clock3.redraw();
				 }
		});
	}

	public void createTimezones(Composite parent) {
		String[] ids = TimeZone.getAvailableIDs();
		timezones = new Combo(parent, SWT.DROP_DOWN);
		timezones.setVisibleItemCount(5);
		for (int i = 0; i < ids.length; i++) {
			timezones.add(ids[i]);
		}
	}
	
	public void setFocus() {
		timezones.setFocus();
	}
}