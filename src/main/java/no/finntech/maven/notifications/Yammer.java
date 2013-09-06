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
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import no.finntech.yammer.YammerClient;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;


/**
 * Goal which sends notifications to yammer.
 */
@Mojo(name = "yammer", requiresOnline = true, threadSafe = true)
public final class Yammer extends AbstractNotificationMojo {

    /** name or id of the yammer group */
    @Parameter(defaultValue = "", property = "yammer.groupId", required = false)
    private String yammerGroupId;

    /**
     * The "announcement" file. Contents are sent as a yammer message.
     */
    @Parameter(
            defaultValue = "${project.build.directory}/announcement/announcement.vm",
            property = "yammer.announcementFile",
            required = true)
    private File yammerAnnouncement;

    /**
     * The key of the application registered with Yammer.
     * See http://www.yammer.com/client_applications/new
     */
    @Parameter(property = "yammer.applicationKey", required = true)
    private String yammerApplicationKey;

    /**
     * The secret of the application registered with Yammer.
     * See http://www.yammer.com/client_applications/new
     */
    @Parameter(property = "yammer.applicationSecret", required = true)
    private String yammerApplicationSecret;

    /**
     * project artifactId
     */
    @Parameter(defaultValue = "${project.artifactId}", property = "yammer.artifactId", required = false)
    private String artifactId;

    /**
     * The Yammer user's username, needed for authentication
     */
    @Parameter(property = "yammer.username", required = true)
    private String yammerUsername;

    /**
     * The Yammer user's password, needed for authentication
     */
    @Parameter(property = "yammer.password", required = true)
    private String yammerPassword;

    @Override
    protected void executeImpl() throws MojoExecutionException {

        if(!yammerAnnouncement.exists()) {
            throw new MojoExecutionException('\n' + yammerAnnouncement.getAbsolutePath() + " doesn't exist.");
        }

        try (YammerClient client = new YammerClient(
                yammerApplicationKey,
                yammerUsername,
                yammerPassword,
                yammerApplicationSecret)) {

            getLog().info("Posting announcement to yammer");
            client.sendMessage(
                    yammerGroupId,
                    new String(Files.readAllBytes(yammerAnnouncement.toPath()), Charset.forName("UTF-8")),
                    "release",
                    artifactId);
        } catch (IOException ex) {
            throw new MojoExecutionException("failed… ", ex);
        }
    }
}
