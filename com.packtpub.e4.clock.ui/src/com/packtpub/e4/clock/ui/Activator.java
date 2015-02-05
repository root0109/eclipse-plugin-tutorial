package com.packtpub.e4.clock.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.packtpub.e4.clock.ui"; //$NON-NLS-1$

	// The shared instance, singleton
	private static Activator plugin;
	
	private TrayItem trayItem;
	private Image image;
	private Shell shell;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		//a small fix to overcome the IllegalStateException
		
		//we cannot c the getDefault()
		final Display display = getWorkbench().getDisplay();
		
		display.asyncExec( new Runnable() {
			public void run() {
				image = new Image(display, Activator.class.getResourceAsStream("/icons/nuke.gif"));
				Tray tray = display.getSystemTray();
				if (tray != null && image != null) {
					trayItem = new TrayItem(tray, SWT.NONE);
					trayItem.setToolTipText("Nuke me!");
					trayItem.setVisible(true);
					trayItem.setText("Nuke me!");
					trayItem.setImage(new Image(trayItem.getDisplay(),
							Activator.class.getResourceAsStream("/icons/nuke.gif")));
					
					trayItem.addSelectionListener(new SelectionListener() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							if (shell == null) {
								shell = new Shell(trayItem.getDisplay(),SWT.NO_TRIM|SWT.ON_TOP);
								shell.setAlpha(128);
								shell.setLayout(new FillLayout());
								
								final Region region = new Region();
								region.add(circle(25, 25, 25));
								shell.setRegion(region);
								
								new ClockWidget(shell, SWT.NONE, new RGB(255, 0, 255));
								shell.pack();
								
								shell.addDisposeListener(new DisposeListener() {
									 public void widgetDisposed(DisposeEvent e) {
									 if (region != null && !region.isDisposed())
									 region.dispose();
									 }
									});
							}
							shell.open();
						}

						@Override
						public void widgetDefaultSelected(SelectionEvent e) {
							widgetSelected(e);
						}
					});
				}
			}
			});
		
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		
		if (trayItem != null && !trayItem.isDisposed()) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					if (trayItem != null && !trayItem.isDisposed())
					{
						trayItem.dispose();
					}
						
				}
			});
		}
		
		if (image != null && !image.isDisposed()) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					if (image != null && !image.isDisposed())
					{
						image.dispose();
					}
				}
			});
		}
		
		if (shell != null && !shell.isDisposed()) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					if (shell != null && !shell.isDisposed())
						shell.dispose();
				}
			});
		}
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	private static int[] circle(int r, int offsetX, int offsetY) {
		int[] polygon = new int[8 * r + 4];
		// x^2 + y^2 = r^2
		for (int i = 0; i < 2 * r + 1; i++) {
			int x = i - r;
			int y = (int) Math.sqrt(r * r - x * x);
			polygon[2 * i] = offsetX + x;
			polygon[2 * i + 1] = offsetY + y;
			polygon[8 * r - 2 * i - 2] = offsetX + x;
			polygon[8 * r - 2 * i - 1] = offsetY - y;
		}
		return polygon;
	}
}
