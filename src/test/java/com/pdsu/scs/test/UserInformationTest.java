package com.pdsu.scs.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.service.UserInformationService;
import com.pdsu.scs.utils.SimpleUtils;

@SpringJUnitConfig(locations = {"classpath:spring/spring.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserInformationTest {

	@Autowired
	private UserInformationService userInformationService;
	@Test
	public void test() {
		userInformationService.inset(new UserInformation(null, 181360241, "pyb***20000112", "半梦",
				"信息工程学院", "18级物联网工程二班", SimpleUtils.getSimpleDate(), 1));
	}

}
