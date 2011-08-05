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
package org.nuxeo.ide.sdk.wizards.templates;

import org.eclipse.swt.widgets.Composite;
import org.nuxeo.ide.common.wizards.FormWizardPage;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 * 
 */
public class NuxeoProjectPage2 extends FormWizardPage<TemplateContext>
        implements Constants {

    public NuxeoProjectPage2() {
        super("nuxeoProjectPage2", "Maven Settings", null);
    }

    public String getGroupId() {
        return form.getWidgetValueAsString(GROUP_ID);
    }

    public String getArtifactId() {
        String v = form.getWidgetValueAsString(ARTIFACT_ID);
        return v == null ? getPreviousPage().getProjectId() : v;
    }

    public String getArtifactVersion() {
        String v = form.getWidgetValueAsString(ARTIFACT_VERSION);
        return v.length() != 0 ? v : null;
    }

    public String getArtifactName() {
        return form.getWidgetValueAsString(ARTIFACT_NAME);
    }

    public String getArtifactDescription() {
        return form.getWidgetValueAsString(ARTIFACT_DESCRIPTION);
    }

    public String getParentGroupId() {
        return form.getWidgetValueAsString(PARENT_GROUP_ID);
    }

    public String getParentArtfactId() {
        return form.getWidgetValueAsString(PARENT_ARTIFACT_ID);
    }

    public String getParentVersion() {
        return form.getWidgetValueAsString(PARENT_VERSION);
    }

    public NuxeoProjectPage1 getPreviousPage() {
        return (NuxeoProjectPage1) super.getPreviousPage();
    }

    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);
    }

    @Override
    public void update(TemplateContext ctx) {
        ctx.setPropertyIfNotNull(form, ARTIFACT_ID);
        ctx.setPropertyIfNotNull(form, ARTIFACT_VERSION, BUNDLE_VERSION);
        ctx.setPropertyIfNotNull(form, GROUP_ID);

        ctx.setPropertyIfNotNull(form, ARTIFACT_NAME);
        ctx.setProperty(form, ARTIFACT_DESCRIPTION);

        ctx.setPropertyIfNotNull(form, PARENT_GROUP_ID);
        ctx.setPropertyIfNotNull(form, PARENT_ARTIFACT_ID);
        ctx.setPropertyIfNotNull(form, PARENT_VERSION);

    }

    @Override
    public void enterPage() {
        String projectId = getPreviousPage().getForm().getWidgetValueAsString(
                PROJECT_ID);
        String projectName = getPreviousPage().getForm().getWidgetValueAsString(
                PROJECT_NAME);
        String rootPackage = getPreviousPage().getForm().getWidgetValueAsString(
                PROJECT_ROOT_PACKAGE);
        String version = getPreviousPage().getForm().getWidgetValueAsString(
                TARGET_VERSION);

        int i = rootPackage.lastIndexOf('.');
        if (i > 0) {
            rootPackage = rootPackage.substring(0, i);
        }

        form.setWidgetValueIfEmpty(GROUP_ID, rootPackage);
        form.setWidgetValueIfEmpty(ARTIFACT_ID, projectId);
        // form.setWidgetValueIfEmpty("maven.artifact.version", version);
        form.setWidgetValueIfEmpty(ARTIFACT_NAME, projectName);
        form.setWidgetValueIfEmpty(PARENT_VERSION, version);
    }
}
