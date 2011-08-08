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
package org.nuxeo.ide.sdk.server;

import java.io.File;

import org.nuxeo.ide.common.UI;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 * 
 */
public class StartServer extends ProcessRunner {

    protected ServerController ctrl;

    public StartServer(ServerController ctrl) {
        super(new ProcessBuilder(
                new File(ctrl.root, "bin/nuxeoctl").getAbsolutePath(), "start"));
        this.ctrl = ctrl;
    }

    @Override
    protected void started() {
        ctrl.fireServerStarting();
    }

    @Override
    protected void terminated(int status, Throwable e) {
        if (e != null) {
            UI.showError("Failed to start Nuxeo Server" + e.getMessage(), e);
        } else {
            ctrl.fireServerStarted();
        }
    }

}
