package com.pdsu.scs.test;

import java.util.Enumeration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringJUnitConfig(locations = {"classpath:spring/spring.xml","classpath:spring/springmvc.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class WebHandlerTest {

	@Autowired
	WebApplicationContext context;
	
	MockMvc mvc;
	
	@Before
	public void initMockMvc() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
		
	}
	
	//测试发送验证码
	@Test
	public void test() throws Exception {
		MvcResult ret = mvc.perform(MockMvcRequestBuilders.get("/user/sendEmail").param("email", "1398375393@qq.com")
				.param("name", "H_on")).andReturn();
		MockHttpServletRequest request1 = ret.getRequest();
		Enumeration<String> attributeNames = request1.getAttributeNames();
		System.out.println(attributeNames);
	}
	
	//测试申请账号
	@Test
	public void test1() throws Exception {
		MvcResult ret = mvc.perform(MockMvcRequestBuilders.get("/user/applyNumber")
				.param("uid", "181360251")
				.param("password", "123456789")
				.param("username", "H_on")
				.param("college", "信息工程学院")
				.param("clazz", "18物联网工程二班")
				.param("email", "1398375393@qq.com")
				.param("token", "xxxx")).andReturn();
	}
}
