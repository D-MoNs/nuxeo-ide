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
package org.nuxeo.ide.common.forms;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 * 
 */
public class BindingContext {

    protected Form form;

    protected LayoutManager layout;

    public BindingContext(Form loader) {
        this.form = loader;
    }

    public Form getForm() {
        return form;
    }

    public UIObject<?> getBinding(String id) {
        return form.getBinding(id);
    }

    public LayoutManager findLayoutManager(String layout) {
        return form.getLayoutManager(layout);
    }

    public LayoutManager getLayout() {
        return layout;
    }

    public void setLayout(LayoutManager layout) {
        this.layout = layout;
    }

    public UIObject<?> getBinding(Element element) {
        return form.getBinding(element);
    }

}
