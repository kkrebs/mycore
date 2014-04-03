/*
 * $Id$
 * $Revision: 5697 $ $Date: Mar 25, 2014 $
 *
 * This file is part of ***  M y C o R e  ***
 * See http://www.mycore.de/ for details.
 *
 * This program is free software; you can use it, redistribute it
 * and / or modify it under the terms of the GNU General Public License
 * (GPL) as published by the Free Software Foundation; either version 2
 * of the License or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program, in a file called gpl.txt or license.txt.
 * If not, write to the Free Software Foundation Inc.,
 * 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */

package org.mycore.common.events;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.mycore.common.config.MCRRuntimeComponentDetector;
import org.mycore.common.xml.MCRURIResolver;

/**
 * @author Thomas Scheffler (yagee)
 *
 */
public class MCRServletContainerInitializer implements ServletContainerInitializer {
    private static final Logger LOGGER = Logger.getLogger(MCRServletContainerInitializer.class);

    /* (non-Javadoc)
     * @see javax.servlet.ServletContainerInitializer#onStartup(java.util.Set, javax.servlet.ServletContext)
     */
    @Override
    public void onStartup(final Set<Class<?>> c, final ServletContext ctx) throws ServletException {
        MCRStartupHandler.startUp(ctx);
        //Make sure logging is configured
        //initialize MCRURIResolver
        MCRURIResolver.init(ctx);
        if (LOGGER.isDebugEnabled()) {
            try {
                Enumeration<URL> resources = this.getClass().getClassLoader().getResources("META-INF/web-fragment.xml");
                while (resources.hasMoreElements()) {
                    LOGGER.debug("Found: " + resources.nextElement().toString());
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            LOGGER.debug("This class is here: " + getSource(this.getClass()));
        }
        MCRShutdownHandler.getInstance().isWebAppRunning = true;
        LOGGER.info("I have these components for you: " + MCRRuntimeComponentDetector.getAllComponents());
        LOGGER.info("I have these mycore components for you: " + MCRRuntimeComponentDetector.getMyCoReComponents());
        LOGGER.info("I have these app modules for you: " + MCRRuntimeComponentDetector.getApplicationModules());
    }

    private static String getSource(final Class<? extends MCRServletContainerInitializer> clazz) {
        if (clazz == null) {
            return null;
        }
        ProtectionDomain protectionDomain = clazz.getProtectionDomain();
        CodeSource codeSource = protectionDomain.getCodeSource();
        if (codeSource == null) {
            LOGGER.warn("Cannot get CodeSource.");
            return null;
        }
        URL location = codeSource.getLocation();
        String fileName = location.getFile();
        File sourceFile = new File(fileName);
        return sourceFile.getName();
    }

}