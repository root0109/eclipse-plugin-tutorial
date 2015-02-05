package com.packtpub.e4.clock.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;

/**
 * A small helper class to determine whether the workspace has been initialized 
 * @author Dell
 *
 */
public class WorkbenchState implements IStartup
{
  private static boolean started = false;
  public void earlyStartup ()
  {
      Display.getDefault ().asyncExec (new Runnable ()
      {
          public void run ()
          {
              started = true;
          }
      });
  }
 
  public static boolean isStarted ()
  {
      return started;
  }
}