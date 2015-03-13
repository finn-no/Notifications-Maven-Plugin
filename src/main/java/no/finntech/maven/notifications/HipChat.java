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

import com.github.hipchat.api.UserId;
import com.github.hipchat.api.messages.Message;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;


/**
 * Goal which sends notifications to hipchat.
 */
@Mojo(name = "hipchat", requiresOnline = true, threadSafe = true)
public final class HipChat extends AbstractNotificationMojo {

    /** comma-separated list of rooms to send notifications to. */
    @Parameter(property = "hipchat.rooms", required = false)
    private String hipchatRooms;

    /** user name the message is sent from. */
    @Parameter(property = "hipchat.from", required = false)
    private String hipchatFrom;

    /**
     * hipchat message to send.
     */
    @Parameter(property = "hipchat.message", required = true)
    private String hipchatMessage;

    /**
     * The HipChat API token
     * See https://www.hipchat.com/docs/api
     */
    @Parameter(property = "hipchat.token", required = true)
    private String hipchatToken;


    /**
     * The "announcement" file. Contents are sent as a yammer message.
     */
    @Parameter(
            defaultValue = "${project.build.directory}/announcement/hipchat-announcement.vm",
            property = "hipchat.announcement",
            required = false)
    private File hipchatAnnouncement;

    @Override
    protected void executeImpl() throws MojoExecutionException {

        if(null == hipchatToken || hipchatToken.isEmpty()) {
            throw new MojoExecutionException("\nhipchatToken isn't defined.");
        }
        if(null == hipchatMessage || hipchatMessage.isEmpty()) {
            throw new MojoExecutionException("\nhipchatMessage isn't defined.");
        }

        final StringBuilder msg = new StringBuilder(hipchatMessage);
        addAnnouncementFileIfSet(msg);

        com.github.hipchat.api.HipChat chat = new com.github.hipchat.api.HipChat(hipchatToken);
        for(String room : hipchatRooms.split(",")) {
            getLog().info("Posting announcement to hipchat (" + room + ')');
            chat.getRoom(room).sendMessage(msg.toString(), UserId.create(hipchatFrom, hipchatFrom), true, Message.Color.PURPLE);
        }
    }

    void addAnnouncementFileIfSet(StringBuilder msg) throws MojoExecutionException {

        if (hipchatAnnouncement != null) {
            if (!hipchatAnnouncement.exists()) {
                throw new MojoExecutionException('\n' + hipchatAnnouncement.getAbsolutePath() + " doesn't exist.");
            }
            try {
                msg.append("\n").append(new String(Files.readAllBytes(hipchatAnnouncement.toPath()), Charset
                    .forName("UTF-8")));
            } catch (IOException e) {
                msg.append("failed to add release announcement. Due to: ").append(e.getMessage());
            }
        }
    }

    void setHipchatAnnouncement(File hipchatAnnouncement) {

        this.hipchatAnnouncement = hipchatAnnouncement;
    }
}
