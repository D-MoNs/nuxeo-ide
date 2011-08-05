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
package org.nuxeo.ide.common.forms.model;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.nuxeo.ide.common.forms.BindingContext;
import org.nuxeo.ide.common.forms.HasValue;
import org.nuxeo.ide.common.forms.WidgetName;
import org.w3c.dom.Element;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 * 
 */
@WidgetName("checkbox")
public class CheckBoxWidget extends ButtonWidget implements HasValue {

    @Override
    protected Button createControl(Composite parent, Element element,
            BindingContext ctx) {
        return new Button(parent, SWT.CHECK);
    }

    @Override
    public Boolean getValue() {
        return ctrl.getSelection();
    }

    @Override
    public void setValue(Object value) {
        if (value == null) {
            return;
        }
        if (value.getClass() == Boolean.class) {
            ctrl.setSelection((Boolean) value);
        } else {
            ctrl.setSelection(Boolean.parseBoolean(value.toString()));
        }
    }

    @Override
    public String getValueAsString() {
        return getValue().toString();
    }

}
