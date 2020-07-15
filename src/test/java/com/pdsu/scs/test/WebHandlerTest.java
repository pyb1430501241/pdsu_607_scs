package com.pdsu.scs.test;


import java.lang.reflect.InvocationTargetException;

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
import org.springframework.web.context.WebApplicationContext;

import com.pdsu.scs.bean.EsUserInformation;
import com.pdsu.scs.es.dao.EsDao;
import com.pdsu.scs.exception.web.es.QueryException;
import com.pdsu.scs.utils.SimpleUtils;

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
}

