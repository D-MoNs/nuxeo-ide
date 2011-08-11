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
package org.nuxeo.ide.sdk.features;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.nuxeo.ide.common.wizards.AbstractWizard;
import org.nuxeo.ide.sdk.features.CreateFeatureFromTemplate;
import org.nuxeo.ide.sdk.features.FeatureTemplateContext;
import org.nuxeo.ide.sdk.ui.NuxeoNature;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 * 
 */
public abstract class FeatureCreationWizard extends
        AbstractWizard<FeatureTemplateContext> {

    protected String templateName;

    protected IJavaElement selectedElement;

    public FeatureCreationWizard(String templateName) {
        this.templateName = templateName;
    }

    public IJavaProject getSelectedProject() {
        return selectedElement != null ? selectedElement.getJavaProject()
                : null;
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
        super.init(workbench, currentSelection);
        selectedElement = getInitialJavaElement(currentSelection);
        if (selectedElement == null && !currentSelection.isEmpty()) {
            // TODO if not a java resource we still need to select the current
            // project
        }
    }

    public IJavaProject getSelectedNuxeoProject() {
        IJavaProject project = getSelectedProject();
        try {
            if (project != null
                    && project.getProject().isNatureEnabled(NuxeoNature.ID)) {
                return project;
            }
        } catch (CoreException e) {
            e.printStackTrace(); // TODO
        }
        return null;
    }

    public IPackageFragment getSelectedPackageFragment() {
        if (selectedElement == null) {
            return null;
        }
        IJavaElement parent = selectedElement;
        while (parent != null) {
            if (parent.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
                return (IPackageFragment) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }

    public IJavaElement getSelectedElement() {
        return selectedElement;
    }

    @Override
    protected FeatureTemplateContext createExecutionContext() {
        return new FeatureTemplateContext();
    }

    @Override
    protected boolean execute(FeatureTemplateContext ctx) {
        ctx.setTemplate(templateName);
        CreateFeatureFromTemplate op = new CreateFeatureFromTemplate(ctx);
        return CreateFeatureFromTemplate.run(getShell(), getContainer(), op);
    }

    /**
     * Utility method to inspect a selection to find a Java element.
     * 
     * @param selection the selection to be inspected
     * @return a Java element to be used as the initial selection, or
     *         <code>null</code>, if no Java element exists in the given
     *         selection
     */
    protected IJavaElement getInitialJavaElement(IStructuredSelection selection) {
        IJavaElement jelem = null;
        if (selection != null && !selection.isEmpty()) {
            Object selectedElement = selection.getFirstElement();
            if (selectedElement instanceof IAdaptable) {
                IAdaptable adaptable = (IAdaptable) selectedElement;

                jelem = (IJavaElement) adaptable.getAdapter(IJavaElement.class);
                if (jelem == null || !jelem.exists()) {
                    jelem = null;
                    IResource resource = (IResource) adaptable.getAdapter(IResource.class);
                    if (resource != null
                            && resource.getType() != IResource.ROOT) {
                        while (jelem == null
                                && resource.getType() != IResource.PROJECT) {
                            resource = resource.getParent();
                            jelem = (IJavaElement) resource.getAdapter(IJavaElement.class);
                        }
                        if (jelem == null) {
                            jelem = JavaCore.create(resource); // java project
                        }
                    }
                }
            }
        }
        // if (jelem == null) {
        // IWorkbenchPart part = JavaPlugin.getActivePage().getActivePart();
        // if (part instanceof ContentOutline) {
        // part = JavaPlugin.getActivePage().getActiveEditor();
        // }
        //
        // if (part instanceof IViewPartInputProvider) {
        // Object elem = ((IViewPartInputProvider) part).getViewPartInput();
        // if (elem instanceof IJavaElement) {
        // jelem = (IJavaElement) elem;
        // }
        // }
        // }
        //
        // if (jelem == null || jelem.getElementType() ==
        // IJavaElement.JAVA_MODEL) {
        // try {
        // IJavaProject[] projects =
        // JavaCore.create(getWorkspaceRoot()).getJavaProjects();
        // if (projects.length == 1) {
        // IClasspathEntry[] rawClasspath = projects[0].getRawClasspath();
        // for (int i = 0; i < rawClasspath.length; i++) {
        // if (rawClasspath[i].getEntryKind() == IClasspathEntry.CPE_SOURCE) {
        // jelem = projects[0];
        // break;
        // }
        // }
        // }
        // } catch (JavaModelException e) {
        // e.printStackTrace(); // TODO
        // }
        // }
        return jelem;
    }

    public IWorkspaceRoot getWorkspaceRoot() {
        return ResourcesPlugin.getWorkspace().getRoot();
    }

}
