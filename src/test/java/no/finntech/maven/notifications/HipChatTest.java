package no.finntech.maven.notifications;

import static org.junit.Assert.assertEquals;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;

public class HipChatTest {

    @Test
    public void adds_nothing_when_no_announcement_file_specified() throws MojoExecutionException {
        HipChat hipChat = new HipChat();
        StringBuilder msg = new StringBuilder();
        hipChat.addAnnouncementFileIfSet(msg);
        assertEquals(0, msg.length());
    }

    @Test(expected = MojoExecutionException.class)
    public void throws_exception_on_missing_file() throws MojoExecutionException {
        HipChat hipChat = new HipChat();
        StringBuilder msg = new StringBuilder();
        hipChat.setHipchatAnnouncement(new File("/some/path/that/does/not/exist"));
        hipChat.addAnnouncementFileIfSet(msg);
        assertEquals(0, msg.length());
    }

    @Test
    public void adds_announcement_file() throws MojoExecutionException, URISyntaxException {
        HipChat hipChat = new HipChat();
        StringBuilder msg = new StringBuilder();
        hipChat.setHipchatAnnouncement(new File(this.getClass().getClassLoader()
            .getResource("hipchat-announcement").toURI()));
        hipChat.addAnnouncementFileIfSet(msg);
        assertEquals("\ntest success", msg.toString());
    }
}