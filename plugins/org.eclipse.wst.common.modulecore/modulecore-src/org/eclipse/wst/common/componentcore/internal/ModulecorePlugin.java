/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.common.componentcore.internal;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.common.componentcore.ArtifactEdit;
import org.eclipse.wst.common.componentcore.internal.impl.PlatformURLModuleConnection;
import org.eclipse.wst.common.componentcore.internal.impl.WTPModulesInit;
import org.eclipse.wst.common.componentcore.internal.util.ArtifactEditAdapterFactory;
import org.eclipse.wst.common.componentcore.internal.util.ModuleCoreEclipseAdapterFactory;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.osgi.framework.BundleContext;
import java.lang.Throwable;
import org.eclipse.core.runtime.CoreException;

/**
 * The main plugin class to be used in the desktop.
 */
public class ModulecorePlugin extends Plugin {
	//The shared instance.
	private static ModulecorePlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	//plugin id
	public static final String PLUGIN_ID = "org.eclipse.wst.common.modulecore"; //$NON-NLS-1$
	
	/**
	 * The constructor.
	 */
	public ModulecorePlugin() {
		super();
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public synchronized void start(BundleContext context) throws Exception {
		
		super.start(context);
		IAdapterManager manager = Platform.getAdapterManager();
		
		manager.registerAdapters(new ModuleCoreEclipseAdapterFactory(), ModuleStructuralModel.class);
		manager.registerAdapters(new ArtifactEditAdapterFactory(), ArtifactEditModel.class);
		manager.registerAdapters(new ArtifactEditAdapterFactory(), ArtifactEdit.class);
		manager.registerAdapters(new ArtifactEditAdapterFactory(), IVirtualComponent.class);
		manager.registerAdapters(new ModuleCoreEclipseAdapterFactory(), IResource.class);
		
		PlatformURLModuleConnection.startup();
		WTPModulesInit.init();
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
		resourceBundle = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static ModulecorePlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = ModulecorePlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		try {
			if (resourceBundle == null)
				resourceBundle = ResourceBundle.getBundle("modulecore"); //$NON-NLS-1$
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		return resourceBundle;
	}
	

	/**
	 * Record an error against this plugin's log. 
	 * 
	 * @param aCode
	 * @param aMessage
	 * @param anException
	 */
	public static void logError(int aCode, String aMessage,
			Throwable anException) {
		getDefault().getLog().log(
				createErrorStatus(aCode, aMessage, anException));
	}

	/**
	 * 
	 * Record a message against this plugin's log. 
	 * 
	 * @param severity
	 * @param aCode
	 * @param aMessage
	 * @param exception
	 */
	public static void log(int severity, int aCode, String aMessage,
			Throwable exception) {
		log(createStatus(severity, aCode, aMessage, exception));
	}

	/**
	 * 
	 * Record a status against this plugin's log. 
	 * 
	 * @param aStatus
	 */
	public static void log(IStatus aStatus) {
		getDefault().getLog().log(aStatus);
	}

	/**
	 * Create a status associated with this plugin.
	 *  
	 * @param severity
	 * @param aCode
	 * @param aMessage
	 * @param exception
	 * @return A status configured with this plugin's id and the given parameters.
	 */
	public static IStatus createStatus(int severity, int aCode,
			String aMessage, Throwable exception) {
		return new Status(severity, PLUGIN_ID, aCode,
				aMessage != null ? aMessage : "No message.", exception); //$NON-NLS-1$
	}

	/**
	 * 
	 * @param aCode
	 * @param aMessage
	 * @param exception
	 * @return A status configured with this plugin's id and the given parameters.
	 */
	public static IStatus createErrorStatus(int aCode, String aMessage,
			Throwable exception) {
		return createStatus(IStatus.ERROR, aCode, aMessage, exception);
	}

	public static IStatus createStatus(int severity, String message, Throwable exception) {
		return new Status(severity, PLUGIN_ID, message, exception);
	}

	public static IStatus createStatus(int severity, String message) {
		return createStatus(severity, message, null);
	}

	public static void logError(Throwable exception) {
		Platform.getLog(Platform.getBundle(PLUGIN_ID)).log( createStatus(IStatus.ERROR, exception.getMessage(), exception));
	}

	public static void logError(CoreException exception) {
		Platform.getLog(Platform.getBundle(PLUGIN_ID)).log( exception.getStatus() );
	}
}
