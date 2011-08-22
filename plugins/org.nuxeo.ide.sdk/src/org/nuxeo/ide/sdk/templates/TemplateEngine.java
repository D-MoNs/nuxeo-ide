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

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 * 
 */
public interface TemplateEngine {

    /**
     * Process the given template file (src) and write the result to dst file
     * 
     * @param ctx
     * @param root
     * @throws Exception
     */
    public void transform(TemplateContext ctx, File src, File dst)
            throws Exception;

    @Deprecated
    public void expandVars(TemplateContext ctx, File root) throws Exception;

    public String expandVars(TemplateContext ctx, String content)
            throws Exception;

}
