package org.nuxeo.ide.sdk;

import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.nuxeo.ide.common.forms.PreferencesFormData;
import org.nuxeo.ide.sdk.templates.TemplateManager;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class SDKPlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.nuxeo.ide.sdk"; //$NON-NLS-1$

    // The shared instance
    private static SDKPlugin plugin;

    protected TemplateManager tempMgr;

    protected IConnectProvider connectProvider;

    /**
     * The constructor
     */
    public SDKPlugin() {
    }

    public TemplateManager getTemplateManager() {
        return tempMgr;
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        tempMgr = new TemplateManager();
        tempMgr.loadRegistry(context.getBundle());
        plugin = this;
        connectProvider = SDKRegistry.getConnectProvider();
        NuxeoSDK.initialize();
    }

    public void stop(BundleContext context) throws Exception {
        NuxeoSDK.dispose();
        connectProvider = null;
        plugin = null;
        tempMgr = null;
        super.stop(context);
    }

    public BundleContext getContext() {
        return getBundle().getBundleContext();
    }

    public PreferencesFormData getPreferences() {
        return new PreferencesFormData(new InstanceScope().getNode(PLUGIN_ID));
    }

    public IConnectProvider getConnectProvider() {
        return connectProvider;
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static SDKPlugin getDefault() {
        return plugin;
    }

    public static void log(int status, String message) {
        getDefault().getLog().log(new Status(status, PLUGIN_ID, message));
    }

    public static void log(int status, String message, Throwable cause) {
        getDefault().getLog().log(new Status(status, PLUGIN_ID, message, cause));
    }

    @Override
    protected void initializeImageRegistry(ImageRegistry reg) {
        reg.put("icons/comp/component.gif",
                ImageDescriptor.createFromURL(getBundle().getEntry(
                        "icons/comp/component.gif")));
        reg.put("icons/comp/service.gif",
                ImageDescriptor.createFromURL(getBundle().getEntry(
                        "icons/comp/service.gif")));
        reg.put("icons/comp/xpoint.gif",
                ImageDescriptor.createFromURL(getBundle().getEntry(
                        "icons/comp/xpoint.gif")));
    }

}
