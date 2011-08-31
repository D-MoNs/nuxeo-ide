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
package org.nuxeo.ide.connect.studio.tree;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.nuxeo.ide.common.BundleImageProvider;
import org.nuxeo.ide.connect.studio.StudioProject;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 * 
 */
public class StudioProjectProvider extends BaseLabelProvider implements
        ITreeContentProvider, ILabelProvider {

    protected BundleImageProvider provider;

    public StudioProjectProvider(BundleImageProvider provider) {
        this.provider = provider;
    }

    @Override
    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof StudioProject) {
            return ((StudioProject) inputElement).getTree().getRoots();
        } else if (inputElement instanceof ProjectTree) {
            return ((ProjectTree) inputElement).getRoots();
        }
        return null;
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof Node) {
            return ((Node<?>) parentElement).getChildren();
        }
        return null;
    }

    @Override
    public Object getParent(Object element) {
        if (element instanceof Node) {
            return ((Node<?>) element).getParent();
        }
        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        if (element instanceof Node) {
            return ((Node<?>) element).hasChildren();
        }
        return false;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    @Override
    public String getText(Object element) {
        return ((Node<?>) element).getLabel();
    }

    @Override
    public Image getImage(Object element) {
        String path = ((Node<?>) element).getIcon();
        if (path != null) {
            return provider.getImage(path);
        }
        return null;
    }

}
