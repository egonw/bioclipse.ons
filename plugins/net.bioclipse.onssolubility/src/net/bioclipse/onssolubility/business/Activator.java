/*******************************************************************************
 * Copyright (c) 2009  Egon Willighagen <egonw@users.sf.net>
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contact: http://www.bioclipse.net/    
 ******************************************************************************/
package net.bioclipse.onssolubility.business;

import net.bioclipse.core.util.LogUtils;

import org.apache.log4j.Logger;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class Activator extends AbstractUIPlugin {

    private static final Logger logger = Logger.getLogger(Activator.class);
    public static final String PLUGIN_ID = "net.bioclipse.onssolubility";

    private static Activator myself;
    
    private ServiceTracker javaFinderTracker;
    private ServiceTracker jsFinderTracker;
    
    public Activator() {}

    public void start(BundleContext context) throws Exception {
        super.start(context);
        myself = this;
        
        javaFinderTracker = new ServiceTracker(
            context,
            IJavaSolubilityManager.class.getName(), 
            null
        );
        javaFinderTracker.open();
        jsFinderTracker = new ServiceTracker(
            context,
            IJavaScriptSolubilityManager.class.getName(), 
            null
        );
        jsFinderTracker.open();
    }

    public ISolubilityManager getJavaManager() {
        ISolubilityManager manager = null;
        try {
            manager = (ISolubilityManager)
                javaFinderTracker.waitForService(1000*10);
        } catch (InterruptedException e) {
            LogUtils.debugTrace(logger, e);
        }
        if(manager == null) {
            throw new IllegalStateException(
                "Could not get the solubility Java manager"
            );
        }
        return manager;
    }

    public ISolubilityManager getJavaScriptManager() {
        ISolubilityManager manager = null;
        try {
            manager = (ISolubilityManager)
                jsFinderTracker.waitForService(1000*10);
        } catch (InterruptedException e) {
            LogUtils.debugTrace(logger, e);
        }
        if(manager == null) {
            throw new IllegalStateException(
                "Could not get the solubility JavaScript manager"
            );
        }
        return manager;
    }

    public void stop(BundleContext context) throws Exception {
        myself = null;
        super.stop(context);
    }

    public static Activator getDefault() {
        return myself;
    }
}
