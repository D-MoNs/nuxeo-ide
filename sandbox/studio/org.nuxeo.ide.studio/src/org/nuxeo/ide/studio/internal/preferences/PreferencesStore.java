package org.nuxeo.ide.studio.internal.preferences;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.IJavaProject;
import org.nuxeo.ide.studio.StudioContentProvider;
import org.nuxeo.ide.studio.StudioConstants;
import org.nuxeo.ide.studio.StudioPlugin;
import org.nuxeo.ide.studio.StudioProject;

public class PreferencesStore {

    public static PreferencesStore INSTANCE = new PreferencesStore();


    protected IEclipsePreferences getGlobalPreferences() {
        return new InstanceScope().getNode(StudioConstants.PLUGIN_ID);
    }

    protected IEclipsePreferences getProjectPreferences(IJavaProject ctx) {
        return new ProjectScope(ctx.getProject()).getNode(StudioConstants.PLUGIN_ID);
    }

    public URL getConnectLocation() {
        String text = getGlobalPreferences().get(PreferencesConstants.P_URL, "http://localhost:8080/nuxeo/site/studio");
        try {
            return new URL(text);
        } catch (MalformedURLException e) {
            throw new Error("Cannot convert " + text, e);
        }
    }

    public String getUsername() {
        return "b";
    }

    public String getPassword() {
        return "b";
    }

    public void setConnectLocation(URL location) {
        String text = location.toExternalForm();
        getGlobalPreferences().put(PreferencesConstants.P_URL, text);
    }


    public String getStudioProjectName(IJavaProject ctx) {
        IEclipsePreferences preferences = getProjectPreferences(ctx);
        String name = preferences.get(PreferencesConstants.P_PROJECT, "");
        StudioContentProvider provider = StudioPlugin.getDefault().getProvider();

        return name;
    }

    public void setStudioProject(IJavaProject ctx, String name) {
        IEclipsePreferences prefs = getProjectPreferences(ctx);
        prefs.put(PreferencesConstants.P_PROJECT, name);
    }

}
