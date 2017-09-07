package groovy

import groovy.time.TimeCategory
import loader.Util
import org.apache.commons.lang.StringUtils

import javax.mail.*
import javax.mail.search.ComparisonTerm
import javax.mail.search.FlagTerm
import javax.mail.search.ReceivedDateTerm

class AttachmentLoader {

    public static final String host = "imap.gmail.com"
    public static final String port = "993"

    Properties props = new Properties()
    String username
    String password
    String dir
    Session session
    Store store
    FetchProfile fetchProfile
    Flags seen
    FlagTerm unseenFlagTerm
    Date currentDate = new Date()
    Date sinceDate
    List<File> attachments
    List<String> folders = Util.getMailFolders()
    Map map = [:]

    AttachmentLoader() {
        props.setProperty("mail.store.protocol", "imaps")
        props.setProperty("mail.imap.host", host)
        props.setProperty("mail.imap.port", port)
        props.setProperty("mail.imap.ssl.enable", "true")

        this.username = Util.getUserNameMail()
        this.password = Util.getPasswordMail()
        this.dir = Util.getMainFolder()
        this.session = Session.getDefaultInstance(props, null)

        this.store = session.getStore("imaps")

        store.connect(host, username, password)

        this.fetchProfile = new FetchProfile()
        this.seen = new Flags(Flags.Flag.SEEN)
        this.unseenFlagTerm = new FlagTerm(seen, false)

        fetchProfile.add(FetchProfile.Item.ENVELOPE)
        this.sinceDate = use(TimeCategory) {
            sinceDate = currentDate.clone() - 1.month
        }
        sinceDate;
        this.attachments = new ArrayList<File>()
    }

    public List<String> uploadAttachment() {
        for (int folderIndex = 0; folderIndex < folders.size(); folderIndex++) {
            Folder folder = store.getFolder(folders.get(folderIndex))
            folder.open(Folder.READ_WRITE)
            Message[] messagesUnsorted = folder.search(unseenFlagTerm)
            Message[] messages = folder.search(new ReceivedDateTerm(ComparisonTerm.GE, sinceDate), messagesUnsorted)
            folder.fetch(messages, fetchProfile)

            Boolean hasAttach = false

            for (int messageIndex = 0; messageIndex < messages.length - 1; messageIndex++) {
                messages[messageIndex].setFlag(Flags.Flag.SEEN, true)
            }
            int lastMessageIndex = messages.length - 1
            if (lastMessageIndex >= 0) {
                println "***************************************************"
                println "***************************************************"
                String date = messages[lastMessageIndex].sentDate.format("ddMMYYYY")
                println "${messages[lastMessageIndex].receivedDate}"
                println "${messages[lastMessageIndex].sender}"
                println "${messages[lastMessageIndex].from}"
                println "${messages[lastMessageIndex].subject}"
                println "${messages[lastMessageIndex].allRecipients}"
                println "${messages[lastMessageIndex].getHeader("Message-ID")}"

                Multipart multipart = (Multipart) messages[lastMessageIndex].getContent();

                for (int partIndex = 0; partIndex < multipart.getCount(); partIndex++) {
                    def bodyPart = multipart.getBodyPart(partIndex)
                    if (!(Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) || StringUtils.isNotBlank(bodyPart.getFileName()))) {
                        messages[lastMessageIndex].setFlag(Flags.Flag.SEEN, true)
                        continue; // dealing with attachments only
                    }
                    hasAttach = true
                    Util.checkDirectory(dir + "\\" + folders[folderIndex] + "\\unprocessed")
                    Util.checkDirectory(dir + "\\" + folders[folderIndex] + "\\processed")

                    InputStream is = bodyPart.getInputStream()
                    File f = new File(dir + "\\" + folders[folderIndex] + "\\unprocessed\\", folders[folderIndex] + "_" + date + '.csv')

                    FileOutputStream fos = new FileOutputStream(f)
                    byte[] buf = new byte[4096]
                    int bytesRead
                    while ((bytesRead = is.read(buf)) != -1) {
                        fos.write(buf, 0, bytesRead)
                    }
                    fos.close()
                    attachments.add(f)
                }

                if (hasAttach) {
                    messages[lastMessageIndex].setFlag(Flags.Flag.SEEN, false)

                } else {
                    messages[lastMessageIndex].setFlag(Flags.Flag.SEEN, true)
                }
                println hasAttach
                println "***************************************************"
                println "***************************************************"
                map.put(folders[folderIndex], hasAttach)
            }
        }
        map.findAll { it.value == true }

        return map.findAll { it.value == true }
                .keySet() as String[];
    }
}