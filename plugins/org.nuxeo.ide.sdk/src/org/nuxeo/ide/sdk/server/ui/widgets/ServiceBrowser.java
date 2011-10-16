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
package org.nuxeo.ide.sdk.server.ui.widgets;

import org.eclipse.swt.widgets.Composite;
import org.nuxeo.ide.common.widgets.DefaultTableBrowser;
import org.nuxeo.ide.sdk.NuxeoSDK;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 * 
 */
public class ServiceBrowser extends DefaultTableBrowser {

    public ServiceBrowser(Composite parent) {
        super(parent);
    }

    public ServiceBrowser(Composite parent, int filterStyle, int tableStyle) {
        super(parent, filterStyle, tableStyle);
    }

    public void setDefaultInput() {
        NuxeoSDK sdk = NuxeoSDK.getDefault();
        if (sdk != null) {
            setInput(sdk.getComponentIndex().getServices());
        } else {
            setInput(null);
        }
    }

}
