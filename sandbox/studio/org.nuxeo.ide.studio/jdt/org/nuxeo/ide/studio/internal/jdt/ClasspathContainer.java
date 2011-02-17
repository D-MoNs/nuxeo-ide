/*
 * (C) Copyright 2006-2010 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     bstefanescu
 */
package org.nuxeo.ide.studio.internal.jdt;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.nuxeo.ide.studio.StudioIDEConstants;
import org.nuxeo.ide.studio.StudioIDEPlugin;
import org.nuxeo.ide.studio.connector.StudioIDEContentProvider;
import org.nuxeo.ide.studio.connector.StudioIDEProject;

/**
 * @author <a href="mailto:slacoin@nuxeo.com">Stephane Lacoin</a>
 *
 */
public class ClasspathContainer implements IClasspathContainer {
            
    protected final IClasspathEntry[] entries;
        
    protected final IJavaProject ctx;
    
    protected ClasspathContainer(IJavaProject ctx) {
        this.ctx = ctx;
        this.entries = buildEntries(ctx);
    }

    protected IClasspathEntry[] buildEntries(IJavaProject ctx) {
        String name = StudioIDEPlugin.getDefault().getPreferences().getStudioProjectName(ctx);
        StudioIDEContentProvider provider = StudioIDEPlugin.getDefault().getProvider();
        String studiopath = getProject(provider, name).getBinaryPath();
        IPath prjpath = provider.find(studiopath);
        IFile lib = ctx.getProject().getFile(prjpath);
        return new IClasspathEntry[] { JavaCore.newLibraryEntry(lib.getLocation(), null, null) };
    }
    
    protected StudioIDEProject getProject(StudioIDEContentProvider provider, String name) {
        if (name.isEmpty()) {
            return provider.getDefaultProject();
        }
        return provider.getProject(name);
    }
   
    
    public IClasspathEntry[] getClasspathEntries() {
        return entries;
    }

    public String getDescription() {
        return "Nuxeo Studio Classpath Container for " + ctx.getProject().getName();
    }

    public int getKind() {
        return K_APPLICATION;
    }

    public IPath getPath() {
        return new Path(StudioIDEConstants.CLASSPATH_CONTAINER_ID);
    }

}