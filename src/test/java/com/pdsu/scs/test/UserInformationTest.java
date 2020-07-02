package com.pdsu.scs.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pdsu.scs.exception.web.DeleteInforException;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.service.UserInformationService;

@SpringJUnitConfig(locations = {"classpath:spring/spring.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserInformationTest {

	@Autowired
	private UserInformationService userInformationService;
	
	@Test
	public void test() {
		try {
			System.out.println(userInformationService.deleteByUid(181360226));
		} catch (NotFoundUidException e) {
			System.out.println(e.getMessage());
		} catch (DeleteInforException e) {
			System.out.println(e.getMessage());
		}
	}
	
}
