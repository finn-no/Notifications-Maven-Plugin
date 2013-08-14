Notifications-Maven-Plugin
==========================

Send notifications from maven.

Yammer example
==============


            <plugin>
              <groupId>no.finntech.notifications-maven-plugin</groupId>
              <artifactId>notifications-maven-plugin</artifactId>
              <version>0.3</version>
              <configuration>
                <!-- name or id of the yammer group. -->
                <yammerGroupId>Releases</yammerGroupId>
                <!-- The "announcement" file. Contents are sent as a yammer message -->
                <yammerAnnouncement>${project.build.directory}/announcement/announcement.vm</yammerAnnouncement>
                <!-- The key of the application registered with Yammer. See http://www.yammer.com/client_applications/new -->
                <yammerApplicationKey>abcdefghijklmnopqrstu</yammerApplicationKey>
                <!-- The secret of the application registered with Yammer. See http://www.yammer.com/client_applications/new -->
                <yammerApplicationSecret>zyxwvutsrqponmlkjihgfedcba0123456789</yammerApplicationSecret>
                <!-- The Yammer access token, related to the user, used in getting the access authentication. See http://www.yammer.com/api_oauth.html -->
                <yammerAccessToken>fghijklmnopqrstufghi</yammerAccessToken>
                <!-- don't fail the build if sending the notification fails -->
                <failOnError>false</failOnError>
              </configuration>
            </plugin>



HipChat example
===============


            <plugin>
              <groupId>no.finntech.notifications-maven-plugin</groupId>
              <artifactId>notifications-maven-plugin</artifactId>
              <version>0.3</version>
              <configuration>
                <!-- comma-separated list of rooms to send notifications to -->
                <hipchatRooms>Architecture,Releases</hipchatRooms>
                <!-- name the message is sent from -->
                <hipchatFrom>Maven Release</hipchatFrom>
                <!-- The HipChat API token. See https://www.hipchat.com/docs/api -->
                <hipchatToken>abcdefghijklmnopqrstu</hipchatToken>
                <!-- hipchat message to send -->
                <hipchatMessage>${user.name} has released ${project.artifactId}-${project.version}</hipchatMessage>
              </configuration>
            </plugin>
