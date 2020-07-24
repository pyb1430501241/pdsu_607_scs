package com.pdsu.scs.test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;


@SpringJUnitConfig(locations = {"classpath:spring/springmvc.xml", "classpath:spring/spring.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class WebHandlerTest {

	@Autowired
	WebApplicationContext context;
	
	MockMvc mvc;
	
	@Autowired
	org.apache.shiro.mgt.SecurityManager securityManager;
	
	@Before
	public void initMockMvc() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
		SecurityUtils.setSecurityManager(securityManager);
	}

	@Test
	public void testGetLoginStatus() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/user/loginstatus"))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testLike() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/user/like").param("likeId", "181360241")
					.param("uid", "181360226"))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testLogin() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/user/getcodeforlogin"))
			.andReturn();
		MockHttpServletResponse response = result.getResponse();
		String[] split = response.getContentAsString().replaceAll("[{}:,]", "").split("\"");
		List<String> list = new ArrayList<>(Arrays.asList(split));
		list.removeAll(Arrays.asList(""));
		list.remove("json");
		MultiValueMap<String, String> t = new LinkedMultiValueMap<String, String>();
		t.add("uid", "181360226");
		t.add("password", "pyb***20000112");
		t.add("hit", list.get(list.indexOf("token") + 1));
		t.add("code", list.get(list.indexOf("code") + 1));
		result = mvc.perform(MockMvcRequestBuilders.post("/user/login").params(t)).andReturn();
		response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testBlobList() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/blob/getwebindex"))
				.andReturn();
			MockHttpServletResponse response = result.getResponse();
			response.setCharacterEncoding("UTF-8");
			System.out.println(response.getContentAsString());
	}
	
	/**
	 * 别忘了等会删除获取验证码的那句话
	 * @throws Exception
	 */
	@Test
	public void testinsert() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/user/getcodeforlogin"))
				.andReturn();
			MockHttpServletResponse response = result.getResponse();
			String[] split = response.getContentAsString().replaceAll("[{}:,]", "").split("\"");
			List<String> list = new ArrayList<>(Arrays.asList(split));
			list.removeAll(Arrays.asList(""));
			list.remove("json");
			MultiValueMap<String, String> t = new LinkedMultiValueMap<String, String>();
			t.add("uid", "181360226");
			t.add("password", "pyb***20000112");
			t.add("hit", list.get(list.indexOf("token") + 1));
			t.add("code", list.get(list.indexOf("code") + 1));
			result = mvc.perform(MockMvcRequestBuilders.post("/user/login").params(t)).andReturn();
			response = result.getResponse();
			String [] login = response.getContentAsString().replaceAll("[{}:,]", "").split("\"");
			List<String> loginlist = new ArrayList<String>(Arrays.asList(login));
			loginlist.removeAll(Arrays.asList(""));
			loginlist.remove("json");
			result = mvc.perform(MockMvcRequestBuilders.post("/blob/contribution")
					.header("Authorization", loginlist.get(loginlist.indexOf("AccessToken") + 1))
					.param("title", "你好, 世界")
					.param("contype", "1")
					.param("webDataString", "System.out.println(\"Hello World\");\nSystem.out.println(\"Hello World\");\nSystem.out.println(\"Hello World\");"
							+ "\nSystem.out.println(\"Hello World\");")).andReturn();
			response = result.getResponse();
			response.setCharacterEncoding("UTF-8");
			System.out.println(response.getContentAsString());
	}
}

