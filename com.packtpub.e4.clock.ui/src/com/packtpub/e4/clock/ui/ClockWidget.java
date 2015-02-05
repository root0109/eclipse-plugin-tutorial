package com.packtpub.e4.clock.ui;

import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class ClockWidget extends Canvas {

	private final Color color;
	private int offset;
	
	public ClockWidget(Composite parent, int style, RGB rgb) {
		super(parent, style);

		this.color = new Color(parent.getDisplay(),rgb);
		
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				ClockWidget.this.paintControl(e);
			}
		});
		
		addDisposeListener(new DisposeListener(){
			@Override
			public void widgetDisposed(DisposeEvent e) {
				disposeResource(color);
			}
			
			public void disposeResource(Resource res)
			{
				if(res != null && !res.isDisposed())
				{
					res.dispose();
				}
			}
		});
		
		
		new Thread("TickTock") {
			public void run() {
				while (!ClockWidget.this.isDisposed()) {
					ClockWidget.this.getDisplay().asyncExec(new Runnable() {
						public void run() {
							if (!ClockWidget.this.isDisposed())
								ClockWidget.this.redraw();
						}
					});
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		}.start();
	}
	
	public void paintControl(PaintEvent e) {		
		Calendar calendar = Calendar.getInstance();

		int seconds = calendar.get(Calendar.SECOND);
		int arc = (15 - seconds) * 6 % 360;

		e.gc.setBackground(color);
		
		//draw the second hand
		e.gc.drawArc(e.x,e.y,e.width-1,e.height-1,0,360);
		e.gc.fillArc(e.x, e.y, e.width - 1, e.height - 1, arc - 1, 2);
		
		//draw the hour hand
		e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_BLACK));
		int hours = calendar.get(Calendar.HOUR) + offset;
		arc = (3 - hours) * 30 % 360;
		e.gc.fillArc(e.x, e.y, e.width-1, e.height-1, arc - 5, 10);
		
		Color white = e.display.getSystemColor(SWT.COLOR_WHITE);
		e.gc.setBackground(white);
		
		//draw the time in the center of the clock
		e.gc.drawText(""+calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND), (e.x+e.width)/4, (e.y+e.height)/2);
	}
	
	public void setFont(FontData fdata)
	{
		Font font = new Font(getDisplay(), fdata);
		setFont(font);
	}
	
	@Override
	public Point computeSize(int w, int h, boolean changed) {
		int size;
		if (w == SWT.DEFAULT) {
			size = h;
		} else if (h == SWT.DEFAULT) {
			size = w;
		} else {
			size = Math.min(w, h);
		}
		if (size == SWT.DEFAULT)
			size = 50;
		return new Point(size, size);
	}
	
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public Color getColor() {
		return color;
	}

}
