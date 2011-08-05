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
package org.nuxeo.dev.ide.launcher;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.nuxeo.dev.ide.Activator;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class NuxeoDevClassPathContainer implements IClasspathContainer {

    public static final String ID= "org.nuxeo.DEV_TOOLS_LIBRARY";
    
    public IClasspathEntry[] getClasspathEntries() {
        return new IClasspathEntry [] {
                JavaCore.newLibraryEntry(
                        Activator.getDefault().getNuxeoDistributionTools(),
                        //Activator.getDefault().getNuxeoDistributionToolsSources(),
                        null, 
                        null)
                };
    }

    public String getDescription() {
        return "Nuxeo Development Tools";
    }

    public int getKind() {
        return K_APPLICATION;
    }

    public IPath getPath() {
        return new Path(ID);
    }

}
