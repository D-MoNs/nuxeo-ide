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
package org.nuxeo.ide.sdk.templates;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 * 
 */
public class TemplateRegistry {

    public static final String DEFAULT_TEMPLATE = "default";

    protected Bundle bundle;

    protected String version;

    protected Map<String, ProjectTemplate> projects;

    protected Map<String, ComponentTemplate> components;

    public TemplateRegistry() {
        this.projects = new HashMap<String, ProjectTemplate>();
        this.components = new HashMap<String, ComponentTemplate>();
    }

    public TemplateRegistry(Bundle bundle) {
        this();
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public ProjectTemplate[] getProjectTemplates() {
        return projects.values().toArray(new ProjectTemplate[projects.size()]);
    }

    public ProjectTemplate getProjectTemplate(String key) {
        if (key == null) {
            key = DEFAULT_TEMPLATE;
        }
        return projects.get(key);
    }

    public void addProjectTemplate(ProjectTemplate temp) {
        projects.put(temp.getId(), temp);
    }

    public void copyProjectTemplate(String id, File projectRoot)
            throws IOException {
        ProjectTemplate temp = getProjectTemplate(id);
        if (temp == null) {
            throw new FileNotFoundException("Template " + id + " was not found");
        }
        temp.copyTo(bundle, projectRoot);
    }

    public void addComponentTemplate(ComponentTemplate temp) {
        components.put(temp.getId(), temp);
    }

    public ComponentTemplate[] getComponentTemplates() {
        return components.values().toArray(
                new ComponentTemplate[components.size()]);
    }

    public ComponentTemplate getComponentTemplate(String id) {
        return components.get(id);
    }

    public void applyComponentTemplate(String id, File projectRoot)
            throws IOException {
        ComponentTemplate temp = getComponentTemplate(id);
        if (temp == null) {
            throw new FileNotFoundException("Template " + id + " was not found");
        }
        temp.apply(projectRoot);
    }

    protected static TemplateRegistry load(Element element) {
        TemplateRegistry registry = new TemplateRegistry();
        String version = DomUtil.getAttribute(element, "version", "0.0.0");
        registry.version = version;
        Node child = element.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) child;
                String tag = child.getNodeName();
                if ("project".equals(tag)) {
                    registry.addProjectTemplate(ProjectTemplate.load(el));
                } else if ("component".equals(tag)) {
                    registry.addComponentTemplate(ComponentTemplate.load(el));
                }
            }
            child = child.getNextSibling();
        }
        return registry;
    }

}
