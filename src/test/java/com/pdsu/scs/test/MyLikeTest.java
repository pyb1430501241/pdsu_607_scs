package com.pdsu.scs.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pdsu.scs.service.MyLikeService;
import com.pdsu.scs.service.UserInformationService;

@SpringJUnitConfig(locations = {"classpath:spring/spring.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class MyLikeTest {

	@Autowired
	private MyLikeService myLikeService;
	
	@Autowired
	private UserInformationService userInformationService;
	
	@Test
	public void test() {
		
	}
	
}