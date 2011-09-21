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
package org.nuxeo.ide.sdk.server.ui.strace;

import org.eclipse.swt.custom.StyleRange;
import org.nuxeo.ide.sdk.server.ui.widgets.ConsoleText;
import org.nuxeo.ide.sdk.server.ui.widgets.HyperlinkStyleFactory;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 * 
 */
public class ErrorHyperlinkFactory extends HyperlinkStyleFactory {

    public ErrorHyperlinkFactory() {
        super("\\w[^\\(\\s]+Error[\\s|:]");
    }

    @Override
    public void onClick(ConsoleText widget, StyleRange style, String text) {
        if (text.endsWith(":")) {
            text = text.substring(0, text.length() - 1);
        }
        new ExceptionHyperLink(text).onClick();
    }

}
