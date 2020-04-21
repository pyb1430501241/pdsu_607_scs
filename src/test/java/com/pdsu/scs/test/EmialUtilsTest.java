package com.pdsu.scs.test;

import org.apache.commons.mail.EmailException;
import org.junit.Test;

import com.pdsu.scs.utils.EmailUtils;

public class EmialUtilsTest {

	@Test
	public void test() throws EmailException {
		EmailUtils utils = new EmailUtils();
		utils.sendEmailForRetrieve("1398375393@qq.com", "卡罗尔与星期二");
		String text = utils.getText();
		System.out.println(text);
	}
}
