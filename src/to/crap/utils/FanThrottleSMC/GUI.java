package to.crap.utils.FanThrottleSMC;

/*
 * FanThrottleSMC
 * @author: Johannes Heck
 * http://crap.to
 */

import java.io.IOException;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class GUI {

	Display display = new Display();
	Image image;
	final Tray tray = display.getSystemTray();
	Shell shell = new Shell(display);
	final TrayItem item = new TrayItem(tray, SWT.NONE);
	String path = "/Applications/smcFanControl.app/Contents/Resources/smc";
	final SMCParser smc = new SMCParser(path);

	public GUI() throws IOException {
		smc.setFanSpeed("20e0");
		image = new Image(display, "FanThrottleSMC.app/Icon.png");
		if(tray == null) {
			System.out.println("system tray not available");
		} else {
			item.addListener(SWT.DefaultSelection, new Listener() {
				public void handleEvent(Event event) {
					try {
						smc.setFanSpeed("25e0");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			final Menu menu = new Menu(shell, SWT.POP_UP);
			String[] s = {"20e0", "25e0", "30e0", "35e0", "40e0", "45e0", "50e0", "55e0", "60e0"};
			for(int i = 0; i < s.length; i++) {
				final String speed = s[i];
				MenuItem mi = new MenuItem(menu, SWT.PUSH);
				mi.setText("Speed: " + speed);
				mi.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						try {
							smc.setFanSpeed(speed);
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				if(i == 0) menu.setDefaultItem(mi);
			}
			MenuItem mi = new MenuItem(menu, SWT.PUSH);
			mi.setText("Exit");
			mi.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					try {
						smc.setFanSpeed("60e0");
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.exit(0);
				}
			});
			item.addListener(SWT.MenuDetect, new Listener() {
				public void handleEvent(Event event) {
					menu.setVisible(true);
				}
			});
			item.setImage(image);
		}

		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) display.sleep();
		}
		image.dispose();
		display.dispose();
	}

	public void setToolTip(String s) {
		item.setToolTipText(s);
	}
}
