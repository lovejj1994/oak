package cn.xxywithpq.service.rabbitmq;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import cn.xxywithpq.domain.MailInfo;

@Component
public class MessageDelegateHandler implements MessageDelegate {

	private static final Log logger = LogFactory.getLog(MessageDelegateHandler.class);

	private static final String UTF8 = "UTF-8";

	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public void handleRabbitMsgForSendMail(MailInfo mailInfo) {
		logger.info("rabbit--Send active mail to ：" + mailInfo.getSetTo());
		sendmail(mailInfo);
	}

	public void sendmail(MailInfo mailInfo) {

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true, "GBK");
			helper.setTo(mailInfo.getSetTo());
			helper.setSubject("welcome to oak : " + mailInfo.getUserName());

			Resource res = new ClassPathResource("mail/mail.html");
			ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
			resolver.setPrefix("/mail/");
			resolver.setSuffix(".html");
			// resolver.setCharacterEncoding("ISO-8859-1");
			resolver.setCharacterEncoding("GBK");
			resolver.setTemplateMode(TemplateMode.HTML);

			TemplateEngine templateEngine = new TemplateEngine();
			templateEngine.setTemplateResolver(resolver);

			Context context = new Context();
			try {
				SimpleDateFormat format = new SimpleDateFormat(new String("YYYY年MM月dd日".getBytes("GBK"), "GBK"));
				String now = format.format(new Date());
				context.setVariable("href", "https://www.xxywithpq.cn/registSuccess/u/" + mailInfo.getUserName()
						+ "/code/" + mailInfo.getActiveCode());
				// context.setVariable("href",
				// "http://localhost:8080/registSuccess/u/"+mailInfo.getUserName()+"/code/"+mailInfo.getActiveCode());
				// context.setVariable("now", new String(now.getBytes(), UTF8));
				context.setVariable("now", new String(now.getBytes("GBK"), "GBK"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String html = templateEngine.process("mail", context);
			helper.setText(html, true);
			helper.setFrom("455234037@qq.com");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
		javaMailSender.send(message);
		logger.info("mail send success.....");
	}

}
