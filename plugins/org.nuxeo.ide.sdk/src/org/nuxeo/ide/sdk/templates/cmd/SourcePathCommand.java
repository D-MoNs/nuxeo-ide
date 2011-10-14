/*
 * (C) Copyright 2006-2011 Nuxeo SAS (http://nuxeo.com/) and contributors.
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
 *     slacoin
 */
package org.nuxeo.ide.sdk.templates.cmd;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.nuxeo.ide.common.UI;
import org.nuxeo.ide.sdk.java.ClasspathEditor;
import org.nuxeo.ide.sdk.model.PomModel;
import org.nuxeo.ide.sdk.templates.TemplateContext;
import org.osgi.framework.Bundle;
import org.w3c.dom.Element;

/**
 * Add new source folder to project
 * 
 * 
 */
public class SourcePathCommand implements Command, PostCreateCommand {

    protected String name;
    
    @Override
    public void init(Element element) {
        name = element.getAttribute("name");
    }

    @Override
    public void execute(TemplateContext ctx, Bundle bundle, File projectDir)
            throws Exception {
              File pomFile = new File(projectDir, "pom.xml");
        PomModel pom = new PomModel(pomFile);
        pom.addBuildHelperSource(name);
        pom.write(pomFile);  
    }

    @Override
    public void execute(IProject project, TemplateContext ctx) throws Exception {
        try {
            ClasspathEditor editor = new ClasspathEditor(project);
            editor.extendClasspath(name);
            editor.flush();
        } catch (JavaModelException e) {
            UI.showError("Cannot extend classpath of " + project.getName(), e);
        }

    }

}
