/*
 * (C) Copyright 2013-2014 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Sun Seng David TAN <stan@nuxeo.com>
 *     jcarsique
 */
package org.nuxeo.ide.jdt;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jdt.internal.ui.preferences.PreferencesAccess;
import org.eclipse.jdt.internal.ui.preferences.cleanup.CleanUpConfigurationBlock;
import org.eclipse.jdt.internal.ui.preferences.formatter.IProfileVersioner;
import org.eclipse.jdt.internal.ui.preferences.formatter.ProfileManager;
import org.eclipse.jdt.internal.ui.preferences.formatter.ProfileStore;

/**
 * A custom cleanup configuration block with a method to set Nuxeo cleanup configuration block
 */
@SuppressWarnings("restriction")
public class NuxeoCleanUpConfigurationBlock extends CleanUpConfigurationBlock implements NuxeoProfileConfigurationBlock {

    protected ProfileStore profileStore;

    protected IProfileVersioner profileVersioner;

    protected ProfileManager profileManager;

    public NuxeoCleanUpConfigurationBlock(IProject project, PreferencesAccess access) {
        super(project, access);
    }

    @Override
    protected ProfileStore createProfileStore(IProfileVersioner versioner) {
        // store the private variable at creation time
        profileStore = super.createProfileStore(versioner);
        return profileStore;
    }

    @Override
    protected IProfileVersioner createProfileVersioner() {
        // store the private variable at creation time
        profileVersioner = super.createProfileVersioner();
        return profileVersioner;
    }

    @Override
    protected ProfileManager createProfileManager(List<ProfileManager.Profile> profiles, IScopeContext context,
            PreferencesAccess access, IProfileVersioner aProfileVersioner) {
        // store the private variable at creation time
        profileManager = super.createProfileManager(profiles, context, access, aProfileVersioner);
        return profileManager;
    }

    @Override
    public IProfileVersioner getProfileVersioner() {
        return profileVersioner;
    }

    @Override
    public ProfileManager getProfileManager() {
        return profileManager;
    }
}
