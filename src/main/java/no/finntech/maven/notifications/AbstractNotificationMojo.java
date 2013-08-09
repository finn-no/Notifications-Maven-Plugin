/** © Copyright 2013 FINN AS
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package no.finntech.maven.notifications;


import java.io.File;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;

abstract class AbstractNotificationMojo extends AbstractMojo {

    @Component
    protected MavenSession session;

    @Parameter(property = "basedir")
    private File basedir;

    /** This will cause the assembly to run only at the top of a given module tree.
     * That is, run in the project contained in the same folder where the mvn execution was launched.
     */
    @Parameter(defaultValue = "true", required = false)
    protected boolean runOnlyAtExecutionRoot = true;

    /** Mojo failure will fail build. If false the any errors are simply logged.
     */
    @Parameter(defaultValue = "true", required = false)
    protected boolean failOnError = true;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if(!runOnlyAtExecutionRoot || isExecutionRoot()) {
            try {
                executeImpl();
            } catch(MojoExecutionException | RuntimeException ex) {
                if(failOnError) {
                    throw ex;
                } else {
                    getLog().error("failed to send notification", ex);
                }
            }
        } else {
            getLog().info("Skipping the notification sending because it's not the Execution Root");
        }
    }

    protected abstract void executeImpl() throws MojoExecutionException;

    private boolean isExecutionRoot() {
        getLog().debug("Root Folder: " + session.getExecutionRootDirectory());
        getLog().debug("Current Folder: "+ basedir );
        return session.getExecutionRootDirectory().equalsIgnoreCase(basedir.toString());
    }
}
