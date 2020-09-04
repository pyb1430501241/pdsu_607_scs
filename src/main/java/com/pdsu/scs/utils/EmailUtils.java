package com.pdsu.scs.utils;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.lang.NonNull;

/**
 * 
 * @author 半梦
 *
 */
public class EmailUtils {

	private String text;
	
	private static final String HR = "<HR align=center width=300 color=#987cb9 SIZE=1>";
	
	public String getText() {
		return text;
	}

	/**
	 * 发送邮件
	 * @param email
	 * @throws EmailException
	 */
	private void sendEmail(String email, String subject, String msg) throws EmailException {
		HtmlEmail ee = new HtmlEmail();
		ee.setHostName("smtp.qq.com");
		ee.setCharset("UTF-8");
		ee.addTo(email);
		ee.setFrom("1430501241@qq.com", "代码分享平台");
		ee.setAuthentication("1430501241@qq.com", "sdgdmkauacrffhea");
		ee.setSubject(subject);
		ee.setSSLOnConnect(true);
		ee.setStartTLSEnabled(true);
		ee.setStartTLSRequired(true);
		ee.addHeader("X-Mailer", "Microsoft Outlook Express 6.00.2900.2869");
		ee.setMsg(msg);
		ee.send();
	}
	
	/**
	 * 注册
	 * @param email
	 * @param name
	 * @throws EmailException 
	 */
	public void sendEmailForApply(@NonNull final String email, @NonNull final String name) throws EmailException {
		text = RandomUtils.getRandom();
		String subject = "代码分享平台注册验证";
		String msg = "尊敬的用户" + name + "您好!\n" + "  您在代码分享平台上进行注册账号的操作,"
				+ "本次请求的邮件验证码是：<b><h1 style=\"font-size:150%\">                "
				+ "        " + text + "</h1><b>为了确保你的用户安全，请在五分钟之内完成验证。"
				+ "本验证码五分钟之内有效, 请及时输入。"
				+ "\n\n" + "  为确保账号安全，请勿泄露此验证码。"
				+ "\n" + "  祝您在【代码分享平台】有所收获！"
				+ "\n\n" + "  (这是一封自动发送的邮件，请勿直接回复)"
				+ HR;
		sendEmail(email, subject, msg);
	}
	
	/**
	 * 找回密码
	 * @param email 邮箱
	 * @param name  用户名
	 * @throws EmailException 邮箱不存在
	 */
	public void sendEmailForRetrieve(@NonNull final String email, @NonNull final String name) throws EmailException {
		text = RandomUtils.getRandom();
		String subject = "代码分享平台找回密码验证";
		String msg = "尊敬的用户" + name + "您好!\n" + "  您在代码分享平台上进行找回密码的操作,"
				+ "本次请求的邮件验证码是：<b><h1 style=\"font-size:150%\">                "
				+ "        " + text + "</h1><b>为了确保你的用户安全，请在五分钟之内完成验证。"
				+ "本验证码五分钟之内有效, 请及时输入。"
				+ "\n\n" + "  为确保账号安全，请勿泄露此验证码。"
				+ "\n" + "  祝您在【代码分享平台】有所收获！"
				+ "\n\n" + "  (这是一封自动发送的邮件，请勿直接回复)"
				+ HR;
		sendEmail(email, subject, msg);
	}
	
	/**
	 * 换绑手机
	 * @param email 邮箱
	 * @param name  用户名
	 * @throws EmailException 邮箱不存在
	 */
	public void sendEmailForRetrieveIpone(@NonNull final String email, @NonNull final String name) throws EmailException {
		text = RandomUtils.getRandom();
		String subject = "代码分享平台换绑手机号验证";
		String msg = "尊敬的用户" + name + "您好!\n" + "  您在代码分享平台上进行换绑手机号的操作,"
				+ "本次请求的邮件验证码是：<b><h1 style=\"font-size:150%\">                "
				+ "        " + text + "</h1><b>为了确保你的用户安全，请在五分钟之内完成验证。"
				+ "本验证码五分钟之内有效, 请及时输入。"
				+ "\n\n" + "  为确保账号安全，请勿泄露此验证码。"
				+ "\n" + "  祝您在【代码分享平台】有所收获！"
				+ "\n\n" + "  (这是一封自动发送的邮件，请勿直接回复)"
				+ HR;
		sendEmail(email, subject, msg);
	}
	
	/**
	 * 修改密码
	 * @param email 邮箱
	 * @param name  用户名
	 * @throws EmailException 邮箱不存在
	 */
	public void sendEmailForModify(@NonNull final String email, @NonNull final String name) throws EmailException {
		text = RandomUtils.getRandom();
		String subject = "代码分享平台修改密码验证";
		String msg = "尊敬的用户" + name + "您好!\n" + "  您在代码分享平台上进行修改密码的操作,"
				+ "本次请求的邮件验证码是：<b><h1 style=\"font-size:150%\">                "
				+ "        " + text + "</h1><b>为了确保你的用户安全，请在五分钟之内完成验证。"
				+ "本验证码五分钟之内有效, 请及时输入。"
				+ "\n\n" + "  为确保账号安全，请勿泄露此验证码。"
				+ "\n" + "  祝您在【代码分享平台】有所收获！"
				+ "\n\n" + "  (这是一封自动发送的邮件，请勿直接回复)"
				+ HR;
		sendEmail(email, subject, msg);
	}
	
}
