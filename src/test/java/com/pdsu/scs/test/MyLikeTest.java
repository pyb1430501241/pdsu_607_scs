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
		
		//myLikeService.insert(new MyLike(null, 181360241, 181360226));
		
		System.out.println("我关注的人数为: " + myLikeService.countByUid(181360241));
		
		System.out.println("关注我的人数为: " + myLikeService.countByLikeId(181360226));
		
		System.out.println(myLikeService.selectLikeIdByUid(181360241));
		
		System.out.println("我关注的人的信息: " + userInformationService.selectUsersByLikeId(181360241));
	}

}