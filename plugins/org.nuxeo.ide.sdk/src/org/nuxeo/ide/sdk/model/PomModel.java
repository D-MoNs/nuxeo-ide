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
package org.nuxeo.ide.sdk.model;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 * 
 */
public class PomModel extends XmlFile {

    public static PomModel getPomModel(IProject project) throws Exception {
        IFile file = project.getFile("pom.xml");
        if (!file.exists()) {
            Document doc = factory.newDocumentBuilder().newDocument();
            Element root = doc.createElement("project");
            doc.appendChild(root);
            Element version = doc.createElement("modelVersion");
            version.setTextContent("4.0.0");
            root.appendChild(version);
            return new PomModel(doc);
        } else {
            return new PomModel(file.getContents(true));
        }
    }

    public PomModel(Document doc) {
        super(doc);
    }

    public PomModel(InputStream in) throws Exception {
        super(in);
    }

    public PomModel(File file) throws Exception {
        super(file);
    }

    public Element getDependenciesElement() {
        Element dep = getFirstElement("dependencies");
        if (dep == null) {
            dep = (Element) doc.getDocumentElement().appendChild(
                    doc.createElement("dependencies"));
        }
        return dep;
    }

    public Element getParentElement() {
        return getFirstElement("parent");
    }

    public String getGroupId() {
        Element g = getFirstElement("groupId");
        if (g == null) {
            Element pe = getParentElement();
            if (pe != null) {
                g = getFirstElement(pe, "groupId");
                if (g != null) {
                    return g.getTextContent().trim();
                }
            }
        } else {
            return g.getTextContent().trim();
        }
        return null;
    }

    public String getArtifactVersion() {
        Element g = getFirstElement("version");
        if (g == null) {
            Element pe = getParentElement();
            if (pe != null) {
                g = getFirstElement(pe, "version");
                if (g != null) {
                    return g.getTextContent().trim();
                }
            }
        } else {
            return g.getTextContent().trim();
        }
        return null;
    }

    public String getArtifactId() {
        Element e = getFirstElement("artifactId");
        if (e != null) {
            return e.getTextContent().trim();
        }
        return null;
    }

    public List<Artifact> getDependencies() {
        ArrayList<Artifact> artifacts = new ArrayList<Artifact>();
        Element root = getDependenciesElement();
        Node node = root.getFirstChild();
        while (node != null) {
            if (node.getNodeType() == Node.ELEMENT_NODE
                    && node.getNodeName().equals("dependency")) {
                Element dep = (Element) node;
                Artifact artifact = readDependency(dep);
                artifacts.add(artifact);
            }
            node = node.getNextSibling();
        }
        return artifacts;
    }

    protected Artifact readDependency(Element element) {
        String groupId = null;
        String artifactId = null;
        String version = null;
        String scope = null;
        Node node = element.getFirstChild();
        while (node != null) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String name = node.getNodeName();
                Element el = (Element) node;
                if (name.equals("groupId")) {
                    groupId = el.getTextContent();
                } else if (name.equals("artifactId")) {
                    artifactId = el.getTextContent();
                } else if (name.equals("version")) {
                    version = el.getTextContent();
                } else if (name.equals("scope")) {
                    scope = el.getTextContent();
                }
            }
            node = node.getNextSibling();
        }
        Artifact artifact = new Artifact(groupId, artifactId);
        artifact.setVersion(version);
        artifact.setScope(scope);
        return artifact;
    }

    public void addDependency(Artifact artifact) {
        Element deps = getDependenciesElement();
        Element el = doc.createElement("dependency");
        deps.appendChild(el);
        Element child = doc.createElement("groupId");
        child.setTextContent(artifact.getGroupId());
        el.appendChild(child);
        child = doc.createElement("artifactId");
        child.setTextContent(artifact.getArtifactId());
        el.appendChild(child);
        // Do not manage versions for now
        // if (artifact.getVersion() != null) {
        // child = doc.createElement("version");
        // child.setTextContent(artifact.getVersion());
        // el.appendChild(child);
        // }
        if (artifact.getScope() != null) {
            child = doc.createElement("scope");
            child.setTextContent(artifact.getScope());
            el.appendChild(child);
        }
    }

    public Element getBuildHelperElement(String name) {
        Element build = getFirstElement("build");
        if (build == null) {
            build = (Element) doc.getDocumentElement().appendChild(
                    doc.createElement("build"));
        }
        Element plugins = getFirstElement(build, "plugins");
        if (plugins == null) {
            plugins = (Element) build.appendChild(doc.createElement("plugins"));
        }
        Element plugin = getFirstElement(plugins, "plugin");
        while (plugin != null) {
            Element groupId = getFirstElement((Element) plugin, "groupId");
            Element artifactId = getFirstElement((Element) plugin, "artifactId");
            if ("org.codehaus.mojo".equals(groupId.getTextContent().trim())
                    && "build-helper-maven-plugin".equals(artifactId.getTextContent().trim())) {
                break;
            }
            plugin = (Element) plugin.getNextSibling();
        }

        if (plugin == null) {
            plugin = createHelperPlugin(plugins);
        }

        Element executions = getFirstElement((Element) plugin, "executions");

        Element execution = getFirstElement(executions, "execution");
        String idTextContent = "add-"+name;
        while (execution != null) {
            Element id = getFirstElement(execution, "id");
            if (idTextContent.equals(id.getTextContent())) {
                break;
            }
            Node node = execution.getNextSibling();
            while(node != null && !(node instanceof Element)) {
                node = node.getNextSibling();
            }
            execution = (Element)node;
        }
        if (execution == null) {
            return createBuildHelperExecution(executions, name);
        }
        Element configuration = getFirstElement(execution, "configuration");
        Element sources = getFirstElement(configuration, name+"s");
        return sources;
    }

    protected Element createHelperPlugin(Element plugins) {
        Element plugin;
        plugin = (Element) plugins.appendChild(doc.createElement("plugin"));
        Element groupId = (Element) plugin.appendChild(doc.createElement("groupId"));
        groupId.setTextContent("org.codehaus.mojo");
        Element artifactId = (Element) plugin.appendChild(doc.createElement("artifactId"));
        artifactId.setTextContent("build-helper-maven-plugin");
        plugin.appendChild(doc.createElement("executions"));
        return plugin;
    }

    protected Element createBuildHelperExecution(Element executions, String kind) {
        Element execution = (Element) executions.appendChild(doc.createElement("execution"));
        Element id = (Element) execution.appendChild(doc.createElement("id"));
        id.setTextContent("add-"+kind);
        Element phase = (Element) execution.appendChild(doc.createElement("phase"));
        phase.setTextContent("generate-"+kind+"s");
        Element goals = (Element) execution.appendChild(doc.createElement("goals"));
        Element goal = (Element) goals.appendChild(doc.createElement("goal"));
        goal.setTextContent("add-"+kind);
        Element configuration = (Element) execution.appendChild(doc.createElement("configuration"));
        Element sources = (Element) configuration.appendChild(doc.createElement(kind+"s"));
        return sources;
    }

    public void addBuildHelperSource(String path) {
        Element sources = getBuildHelperElement("source");
        Node source = getFirstElement(sources, "source");
        while (source != null) {
            if (path.equals(source.getTextContent().trim()) == true) {
                return;
            }
            source = source.getNextSibling();
        }
        source = sources.appendChild(doc.createElement("source"));
        source.setTextContent(path);
    }

    public void addBuildHelperResource(String path) {
        Element resources = getBuildHelperElement("resource");
        Node resource = getFirstElement(resources, "resource");
        while (resource != null) {
            if (path.equals(resource.getTextContent().trim()) == true) {
                return;
            }
            resource = resource.getNextSibling();
        }
        resource = resources.appendChild(doc.createElement("resource"));
        Node directory = resource.appendChild(doc.createElement("directory"));
        directory.setTextContent(path);
    }

}
