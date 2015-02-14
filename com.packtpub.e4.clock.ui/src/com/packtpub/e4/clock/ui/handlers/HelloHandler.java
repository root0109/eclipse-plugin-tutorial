package com.packtpub.e4.clock.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.progress.IProgressConstants2;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.core.runtime.Status;

import com.packtpub.e4.clock.ui.Activator;

public class HelloHandler extends AbstractHandler {

	//using the runUIThread API to execute our task inside the UI thread
	/*@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		UIJob job = new UIJob(Display.getDefault(), "About to say Hi") {
			
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				
				MessageDialog.openInformation(null, "Jou", "C'mon");
				return Status.OK_STATUS;
			}
		};
		job.schedule();
		return null;
	}*/

	@Override
	public Object execute(ExecutionEvent event) {
		Job job = new Job("About to say hello") {
			protected IStatus run(IProgressMonitor monitor) {
				try {
					SubMonitor subMonitor = SubMonitor.convert(monitor, "Preparing", 5000);
					//subMonitor = null;//the bug
					
					for (int i = 0; i < 50 && !subMonitor.isCanceled(); i++) {
						if (i == 10) {
							subMonitor.subTask("Doing something");
						} else if (i == 25) {
							subMonitor.subTask("Doing something else");
						} else if (i == 40) {
							subMonitor.subTask("Nearly there");
						} else if (i == 12) {
							checkDozen(subMonitor.newChild(100));
							continue;
						}
						Thread.sleep(100);
						subMonitor.worked(100);
					}
				} 
				catch (InterruptedException e) {
					
				} 
				catch (NullPointerException e) {
					StatusManager statusManager = StatusManager.getManager();
					Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Programming bug?", e);
					statusManager.handle(status, StatusManager.LOG | StatusManager.SHOW);
				}
				finally {
					if (monitor != null)
						monitor.done();
				}
			
				if(!monitor.isCanceled()) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							MessageDialog.openInformation(null, "Hello", "World");
						}
					});
				}
				else
				{
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							MessageDialog.openInformation(null, "You", "Canceled");
						}
					});
				}
				
				return Status.OK_STATUS;
			}
		};
		ICommandService service = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
		Command command = service == null ? null : service.getCommand("com.packtpub.e4.clock.ui.command.hello");
		if (command != null) {
			job.setProperty(IProgressConstants2.COMMAND_PROPERTY,
					 ParameterizedCommand.generateCommand(command, null));
		}
		
		job.setProperty(IProgressConstants2.ICON_PROPERTY,
				 ImageDescriptor.createFromURL(
				 HelloHandler.class.getResource("/icons/nuke.gif")));
				
		job.schedule();
		return null;
	}
	
	private void checkDozen(IProgressMonitor monitor) {
		if(monitor == null)
			 monitor = new NullProgressMonitor();
		
		try {
			monitor.beginTask("Checking a dozen", 12);
			for (int i = 0; i < 12; i++) {
				Thread.sleep(10);
				monitor.worked(1);
			}
		} catch (InterruptedException e) {
		} finally {
			monitor.done();
		}
	}

}
