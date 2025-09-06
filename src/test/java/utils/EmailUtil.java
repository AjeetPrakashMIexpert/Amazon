package utils;

import java.io.File;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class EmailUtil {

	public static void emailExtentReport(String[] toEmail,String subject,String body,String reportPath) {
		// Sender email + app password (use App Passwords, not real Gmail password)
		//final means “cannot be reassigned after initialization.”
		//Using final makes it clear to both the compiler and other developers that these variables shouldn’t change.
		//Thread safety & readability – In multi-threaded code, marking something final ensures 
		//no other thread (or accidental code) reassigns the reference.
		//In Java, a thread is like a lightweight separate path of execution inside a program.
		final String fromEmail=CredentialsManager.getGmailUser();//sender's email address
		final String password=CredentialsManager.getGmailAppPass();
		
		Properties props=new Properties();
		props.put("mail.smtp.host","smtp.gmail.com");
		props.put("mail.smtp.port","587");
		props.put("mail.smtp.auth","true");
		props.put("mail.smtp.starttls.enable","true");
		
		Session session=Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		});
		
		
		try {
			
			//Create message
			Message msg=new MimeMessage(session);
			msg.setFrom(new InternetAddress(fromEmail,"Automation Architect-Ajeet Prakash"));
			
			for(String recipient : toEmail) {
				msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
			}

			msg.setSubject(subject);
			
			//Body part
			BodyPart messageBodyPart=new MimeBodyPart();
			messageBodyPart.setText(body);
			
			//Attach Extent Report
			MimeBodyPart attachmentPart=new MimeBodyPart();
			attachmentPart.attachFile(new File(reportPath));
			
			//Combine Parts
			Multipart multipart=new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			multipart.addBodyPart(attachmentPart);
			
			
			msg.setContent(multipart);
						
			//send email
		    Transport.send(msg);
			/*
			 * since toEmail is an array of strings then below line will
			 * LogHelper.info("Report emailed successfully to " + toEmail); print Report
			 * emailed successfully to [Ljava.lang.String;@481558ce It can be corrected by
			 * below two ways:- LogHelper.info("Report emailed successfully to " +
			 * Arrays.toString(toEmail)); which will print: Report emailed successfully to
			 * [abc@gmail.com, xyz@gmail.com] and another better way is used here:
			 */
		    LogHelper.info("Report emailed successfully to " + String.join(", ", toEmail)); 
		    //it will print report as- Report emailed successfully to abc@gmail.com, xyz@gmail.com
		    
		} catch (Exception e) {
		    LogHelper.error("Failed to send email report", e);
		}




		
	}

}











/*
 * Boilerplate code means the repetitive, standard, or template-like code you
 * need to write again and again in many programs, even if it doesn’t change
 * much. example- WebDriver driver = new ChromeDriver();
 * driver.manage().window().maximize();
 * driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); Every
 * Selenium project has the same setup for WebDriver.
 * 
 * It’s not the test logic → it’s just standard boilerplate.
 */



/*
 * SMTP = Simple Mail Transfer Protocol.
 * 
 * It’s the standard internet protocol used to send emails.
 * 
 * Gmail, Yahoo, Outlook — all use SMTP to deliver outgoing mails.
 * 
 * If IMAP/POP3 are like “reading emails”, then SMTP is like “sending emails.”
 * 
 * Properties props = new Properties();
 * 
 * Creates a Properties object (a map-like structure for key-value pairs, often
 * used in Java configuration).
 * Properties extends Hashtable<Object,Object>
 * .put(key, value) → adds a key-value pair into the Properties object.
 * 
 * props.put("mail.smtp.host", "smtp.gmail.com");
 * 
 * Sets the mail server host.
 * 
 * Gmail’s SMTP server is smtp.gmail.com.
 * 
 * This tells JavaMail “send my email via Gmail.”
 * 
 * props.put("mail.smtp.port", "587");
 * 
 * Sets the port number to connect to Gmail’s SMTP.
 * 
 * Why 587?
 * 
 * SMTP uses different ports depending on security:
 * 
 * 25 → Default SMTP (often blocked by ISPs because spammers misuse it).
 * 
 * 465 → SMTP over SSL (legacy).
 * 
 * 587 → SMTP with STARTTLS (modern + secure, recommended).
 * 
 * Gmail requires 587 for TLS (Transport Layer Security).
 * 
 * 
 * props.put("mail.smtp.auth", "true");
 * 
 * Tells JavaMail that authentication (username + password) is required.
 * 
 * Without this, you can’t log in to Gmail.
 * 
 * props.put("mail.smtp.starttls.enable", "true");
 * 
 * Enables STARTTLS → a way to upgrade a plain text connection to an encrypted
 * (secure) one.
 * 
 * Gmail requires this for security.
 */



/*
 * 1. What is a port?
 * 
 * Every computer/server has one IP address (like a house address).
 * 
 * But inside that house, many different services can run:
 * 
 * Web server (HTTP)
 * 
 * Email server (SMTP, IMAP, POP3)
 * 
 * Database server (MySQL, etc.)
 * 
 * Each service needs its own door so the OS knows where to deliver incoming
 * traffic.
 * 
 * That door number is called a port.
 * 
 * 👉 Example:
 * 
 * House = smtp.gmail.com
 * 
 * Door 587 = Gmail’s SMTP send-mail service
 * 
 * Door 993 = Gmail’s IMAP read-mail service
 * 
 * Door 443 = Gmail’s HTTPS web service
 * 
 * If you knock on the wrong door, the server won’t know what you want.
 * 
 * 🔹 2. Why do we need it in email sending?
 * 
 * When your Java program connects to Gmail:
 * 
 * props.put("mail.smtp.host", "smtp.gmail.com"); props.put("mail.smtp.port",
 * "587");
 * 
 * 
 * Host = which building to go to (Gmail’s server).
 * 
 * Port = which door to knock on (SMTP with STARTTLS).
 * 
 * Without the port, the program would reach Gmail but Gmail wouldn’t know
 * whether you want to:
 * 
 * send mail (SMTP)
 * 
 * read mail (IMAP/POP3)
 * 
 * open a website (HTTPS)
 * 
 * So, specifying the port is like saying:
 * 
 * “I’m here to send an email, not to browse Gmail’s website or fetch my inbox.”
 */




/*
 * WAY A:- Session session = Session.getInstance(props, new Authenticator() {
 * protected PasswordAuthentication getPasswordAuthentication() { return new
 * PasswordAuthentication(fromEmail, password); } });
 * 
 * It’s exactly the same as creating a named class (MyAuthenticator), but it
 * saves you from writing extra boilerplate code.

 *WAY B:-
 * class MyAuthenticator extends Authenticator { 
 * private String user; 
 * privateString pass;
 * 
 * public MyAuthenticator(String user, String pass) 
 * { this.user = user;
 * this.pass = pass; 
 * }
 * 
 * @Override protected PasswordAuthentication getPasswordAuthentication() {
 * return new PasswordAuthentication(user, pass); } 
 * } 
 * 
 * And then:
 * 
 * Session session = Session.getInstance(props, new MyAuthenticator(fromEmail, password)); 
 * 
 */




/*
 * PasswordAuthentication is part of the javax.mail package (JavaMail API).
 * 
 * 🔹 2. What does it do?
 * 
 * It holds two things:
 * 
 * The username (usually your email address)
 * 
 * The password (your app password, not the normal Gmail password)
 * 
 * So instead of passing strings around separately, JavaMail wraps them in a
 * single object.
 * 
 * 🔹 3. Example PasswordAuthentication auth = new
 * PasswordAuthentication("your_email@gmail.com", "your_app_password");
 * 
 * 
 * auth.getUserName() → returns "your_email@gmail.com"
 * 
 * auth.getPassword() → returns "your_app_password"
 * 
 * 🔹 4. Why does JavaMail need it?
 * 
 * When Gmail’s SMTP server says:
 * 
 * “Hey client, authenticate yourself!”
 * 
 * JavaMail calls your Authenticator.getPasswordAuthentication(), which must
 * return a PasswordAuthentication object. That object is then sent as
 * credentials to log in.
 * 
 * 
 * 5. Analogy

Think of it like your ATM card + PIN:

The card alone (username/email) is not enough.

The PIN alone (password) is not enough.

Together → they authenticate you.

PasswordAuthentication bundles both together securely.
 */





/*
 * What is a Session?
 * 
 * In JavaMail, a Session represents a mail session (connection context).
 * 
 * It knows:
 * 
 * Which SMTP server to use (props you configured earlier).
 * 
 * Whether authentication is required.
 * 
 * How to get credentials (username & password).
 * 
 * Think of Session as a setup object that knows all connection details.
 * 
 * 🔹 3. What is Authenticator?
 * 
 * Authenticator is a special JavaMail helper class.
 * 
 * Its job is to provide username & password when the SMTP server (Gmail) asks
 * for authentication.
 * 
 * Normally, if you connect to Gmail SMTP, it says: 👉 “Who are you? Give me
 * your username & password.”
 * 
 * The Authenticator is where you teach JavaMail how to answer that challenge.
 * 
 * 🔹 4. What is getPasswordAuthentication()?
 * 
 * This method is automatically called by JavaMail whenever the server asks for
 * login.
 * 
 * In this code:
 * 
 * return new PasswordAuthentication(fromEmail, password);
 * 
 * 
 * you are returning your Gmail credentials:
 * 
 * fromEmail → your Gmail address.
 * 
 * password → your Gmail App Password (not the normal password).
 * 
 * So effectively: 📤 Whenever Gmail SMTP says “authenticate!” → JavaMail calls
 * getPasswordAuthentication(), which supplies the credentials.
 * 
 * 🔹 5. Putting it all together
 * 
 * Session.getInstance(props, authenticator) does:
 * 
 * Loads your mail properties (props) → which host, which port, TLS, etc.
 * 
 * Stores the authenticator → how to log in.
 * 
 * Creates a Session object that will later be used to build and send your
 * email.
 * 
 * When you do:
 * 
 * Transport.send(message);
 * 
 * 
 * JavaMail:
 * 
 * Uses the Session settings → goes to smtp.gmail.com:587.
 * 
 * Starts TLS.
 * 
 * Calls your Authenticator → gets username/password.
 * 
 * Logs in.
 * 
 * Sends the email.
 */




/*
 * 1. Why Session.getInstance(...) instead of new Session(...)?
 * 
 * The JavaMail API authors made the Session class constructor protected (not
 * public).
 * 
 * This means you cannot directly create a Session object with new Session(...).
 * 
 * 👉 They want you to use factory methods (getInstance() or
 * getDefaultInstance()) instead.
 * 
 * 
 * Analogy
 * 
 * Imagine you want to open a bank account.
 * 
 * You can’t just say new BankAccount(...) because you don’t know the bank’s
 * internal setup.
 * 
 * Instead, you go to the bank’s Account Manager (the factory method).
 * 
 * They make sure the account is created with the right defaults, checks, and
 * links.
 * 
 * Same idea here → JavaMail forces you to use the factory method.
 */













/*
 * Why not only try-catch OR only LogHelper why both is used
 * 
 * 
 * . Two main types of exceptions in Java ✅ Checked exceptions
 * 
 * These are exceptions that must be either handled with try-catch or declared
 * with throws in the method signature.
 * 
 * Compiler checks at compile time → if you don’t handle them, your code won’t
 * compile.
 * 
 * Example:
 * 
 * IOException (when reading/writing files)
 * 
 * SQLException (when dealing with databases)
 * 
 * MessagingException (when dealing with emails via JavaMail API)
 * 
 * 👉 These usually represent problems you expect might happen in the
 * environment (e.g., file missing, server not reachable).
 * 
 * ❌ Unchecked exceptions
 * 
 * Subclasses of RuntimeException.
 * 
 * Compiler doesn’t force you to handle them.
 * 
 * Examples:
 * 
 * NullPointerException
 * 
 * ArrayIndexOutOfBoundsException
 * 
 * IllegalArgumentException
 * 
 * 👉 These usually represent programming errors (like a bug in your code).
 * 
 * 🔹 2. Why is MessagingException checked?
 * 
 * Because when you send email:
 * 
 * The mail server might be down.
 * 
 * The network might be unavailable.
 * 
 * Authentication might fail.
 * 
 * These are environmental issues (not coding bugs). JavaMail designers decided:
 * 👉 “We’ll make this a checked exception so developers are forced to handle it
 * gracefully.”
 * 
 * 🔹 3. Example // Checked exception: must handle or declare try {
 * Transport.send(message); } catch (MessagingException e) {
 * LogHelper.error("❌ Failed to send email", e); }
 * 
 * 
 * If you don’t write try-catch (or throws MessagingException in method
 * signature), 👉 your code won’t compile.
 * 
 * 🔹 4. Analogy
 * 
 * Checked exception: Like a traffic rule → police (compiler) forces you to stop
 * and check.
 * 
 * Unchecked exception: Like slipping on a banana peel → nobody warned you, you
 * just fell.
 */




/*
 * MimeMessage
 * 
 * Think of this as the email itself.
 * 
 * It represents the entire message you will send:
 * 
 * From address
 * 
 * To address
 * 
 * Subject
 * 
 * Content (body, attachments, etc.)
 * 
 * 👉 Analogy: A sealed envelope with everything inside.
 * 
 * 
 * Flow Summary
 * 
 * MimeMessage = the whole email
 * 
 * MimeBodyPart = one piece (text, attachment, image, etc.)
 * 
 * MimeMultipart = a collection of body parts
 * 
 * You put MimeMultipart into MimeMessage, then send it
 */


/*
 * InternetAddress (javax.mail.internet.InternetAddress)
 * 
 * Represents an email address in a structured way.
 * 
 * It validates the format (e.g., xyz@gmail.com).
 * 
 * Example:
 * 
 * new InternetAddress("abc@gmail.com");
 * 
 * 
 * creates an InternetAddress object containing abc@gmail.com. 
 * 
 * 👉 Analogy: InternetAddress = name + postal address written clearly.
 *  Without this, your email might go to an invalid location.
 * 
 * InternetAddress.parse(toEmail)
 * 
 * Converts a string like "abc@gmail.com, def@gmail.com"
 * 
 * Into an array of InternetAddress objects.
 * 
 * So you can send to multiple people.
 * 
 * 👉 Example:
 * 
 * msg.setRecipients(Message.RecipientType.TO,
 * InternetAddress.parse("abc@gmail.com,def@gmail.com"));
 * msg.setRecipients(Message.RecipientType.CC,
 * InternetAddress.parse("manager@gmail.com"));
 */


/*
 * Message.RecipientType.TO → The type of recipient.
 * 
 * TO: Main recipients
 * 
 * CC: Carbon copy
 * 
 * BCC: Blind carbon copy
 */


/*
 * setRecipient() vs addRecipient() in JavaMail
 * 
 * Both are used to define who will receive your email (TO, CC, BCC). But they
 * behave a little differently:
 * 
 * ✅ setRecipient(RecipientType type, Address address)
 * 
 * Sets a single recipient.
 * 
 * If you call it multiple times for the same type, the previous recipient gets
 * replaced.
 * 
 * Example:
 * 
 * msg.setRecipient(Message.RecipientType.TO, new
 * InternetAddress("abc@gmail.com")); msg.setRecipient(Message.RecipientType.TO,
 * new InternetAddress("def@gmail.com"));
 * 
 * 
 * 👉 Result: Only def@gmail.com will get the email. (abc@gmail.com replaced)
 * 
 * ✅ addRecipient(RecipientType type, Address address)
 * 
 * Adds another recipient without replacing the previous ones.
 * 
 * Example:
 * 
 * msg.addRecipient(Message.RecipientType.TO, newInternetAddress("abc@gmail.com")); 
 * msg.addRecipient(Message.RecipientType.TO,new InternetAddress("def@gmail.com"));
 * 
 * 
 * 👉 Result: Both abc@gmail.com and def@gmail.com will receive the email.
 */



/*
 * CC (Carbon Copy)
 * 
 * Others who should be informed, but are not the main recipient.
 * 
 * Example: Team members who need visibility but don’t need to act.
 * 
 * Everyone can see who is CC’d.
 * 
 * BCC (Blind Carbon Copy)
 * 
 * Used when you want to send the email to someone without other recipients
 * knowing.
 * 
 * Recipients in BCC are hidden from TO and CC.
 */



/*
 * InternetAddress
 * 
 * InternetAddress is a JavaMail class that represents an email address.
 * 
 * It can hold:
 * 
 * the email ID itself (fromEmail)
 * 
 * an optional personal name/label ("Automation Reports")
 * 
 * 🔹 What happens here?
 * 
 * fromEmail → "your_email@gmail.com" (the actual sender’s address).
 * 
 * "Automation Reports" → the display name that people see in their inbox.
 * 
 * So instead of seeing:
 * 
 * From: your_email@gmail.com
 * 
 * 
 * the recipient sees:
 * 
 * From: Automation Reports <your_email@gmail.com>
 */




/*
 * Why do we sometimes declare as BodyPart and sometimes MimeBodyPart?
 * 
 * 👉 Line 1:
 * 
 * BodyPart messageBodyPart = new MimeBodyPart();
 * 
 * 
 * Declared as the parent type (BodyPart), but assigned a MimeBodyPart.
 * 
 * This is polymorphism: treat the object as its more general type.
 * 
 * Benefit: Later, if you want to switch to a different BodyPart type, your code
 * doesn’t change.
 * 
 * 👉 Line 2:
 * 
 * MimeBodyPart attachmentPart = new MimeBodyPart();
 * 
 * 
 * Declared as the child type (MimeBodyPart) directly.
 * 
 * This means you want to use methods specific to MimeBodyPart (like
 * attachFile()), which are not defined in the general BodyPart.
 */




/*
 * msg.setContent(multipart) tells the email:
 * 
 * “This message is not just plain text. It has multiple MIME parts (body +
 * attachments).”
 * 
 * Basically:
 * 
 * Without this line → your email would only contain plain text.
 * 
 * With this line → your email becomes a rich MIME email that supports text,
 * attachments, inline images, etc.
 */


/*
 * Transport.send(msg);
 * 
 * This is the final step → it hands the fully built message to the SMTP server.
 * 
 * Transport is the class in JavaMail that knows how to talk to the mail server
 * (smtp.gmail.com in your case).
 * 
 * It uses:
 * 
 * Host (smtp.gmail.com)
 * 
 * Port (587)
 * 
 * Authentication (your fromEmail + appPassword)
 * 
 * 👉 Once called, your email is sent out into the internet via SMTP.
 */