/*
 * org.nrg.config.exceptions.SiteConfigurationFileNotFoundException
 * XNAT http://www.xnat.org
 * Copyright (c) 2014, Washington University School of Medicine
 * All Rights Reserved
 *
 * Released under the Simplified BSD.
 *
 * Last modified 8/26/13 6:15 PM
 */
package org.nrg.config.exceptions;

import com.google.common.base.Joiner;

import java.io.FileNotFoundException;
import java.util.List;


public final class SiteConfigurationFileNotFoundException extends SiteConfigurationException {

    private static final long serialVersionUID = -5608639365180565044L;

    public SiteConfigurationFileNotFoundException(final FileNotFoundException exception) {
        super("Couldn't find a configuration file", exception);
    }

    public SiteConfigurationFileNotFoundException(String configFileName, List<String> configFilesLocations) {
        super(String.format("The file '%s' was not found in any of the following locations:\n%s", configFileName, Joiner.on("\n").join(configFilesLocations)));
    }
}
