package com.pdsu.scs.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.exception.web.DeleteInforException;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.exception.web.user.UidRepetitionException;
import com.pdsu.scs.service.UserInformationService;
import com.pdsu.scs.utils.HashUtils;
import com.pdsu.scs.utils.SimpleUtils;

@SpringJUnitConfig(locations = {"classpath:spring/spring.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserInformationTest {

	@Autowired
	private UserInformationService userInformationService;
	
	@Test
	public void test() {
		UserInformation user = new UserInformation();
		user.setImgpath("01.png");
		user.setUid(181360222);
		user.setUsername("你好");
		user.setTime(SimpleUtils.getSimpleDate());
		user.setCollege("信息工程学院");
		user.setClazz("18物联二班");
		user.setAccountStatus(1);
		user.setPassword(HashUtils.getPasswordHash(181360222, "123456"));
		try {
			userInformationService.inset(user);
			System.out.println(user.getId());
		} catch (UidRepetitionException e) {
			e.printStackTrace();
		}
	}
	
}
