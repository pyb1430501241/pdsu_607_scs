package com.pdsu.scs.test;


import com.pdsu.scs.bean.SystemNotification;
import com.pdsu.scs.service.SystemNotificationService;
import com.pdsu.scs.utils.SimpleUtils;
import org.apache.shiro.SecurityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
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

import java.io.FileInputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@SpringJUnitConfig(locations = {"classpath:spring/springmvc.xml", "classpath:spring/spring.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HandlerTest {

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
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/index")
					)
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
		t.add("code", list.get(list.indexOf("vicode") + 1));
		t.add("flag", "1");
		result = mvc.perform(MockMvcRequestBuilders.post("/user/login").params(t)).andReturn();
		response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testBlobList() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/blob/getwebindex")
				)
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}
	
	/**
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
			t.add("code", list.get(list.indexOf("vicode") + 1));
			result = mvc.perform(MockMvcRequestBuilders.post("/user/login").params(t)).andReturn();
			response = result.getResponse();
			String [] login = response.getContentAsString().replaceAll("[{}:,]", "").split("\"");
			List<String> loginlist = new ArrayList<String>(Arrays.asList(login));
			loginlist.removeAll(Arrays.asList(""));
			loginlist.remove("json");
			result = mvc.perform(MockMvcRequestBuilders.post("/blob/contribution")
					.header("Authorization", loginlist.get(loginlist.indexOf("AccessToken") + 1))
					.param("contype", "1")
					.param("webDataString", "System.out.println(\"Hello World\");\nSystem.out.println(\"Hello World\");\nSystem.out.println(\"Hello World\");"
							+ "\nSystem.out.println(\"Hello World\");")
					).andReturn();
			response = result.getResponse();
			response.setCharacterEncoding("UTF-8");
			System.out.println(response.getContentAsString());
	}
	
	/**
	 * ???????
	 * @throws Exception
	 */
	@Test
	public void testUpdate() throws Exception {
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
		t.add("code", list.get(list.indexOf("vicode") + 1));
		result = mvc.perform(MockMvcRequestBuilders.post("/user/login").params(t)).andReturn();
		response = result.getResponse();
		String [] login = response.getContentAsString().replaceAll("[{}:,]", "").split("\"");
		List<String> loginlist = new ArrayList<String>(Arrays.asList(login));
		loginlist.removeAll(Arrays.asList(""));
		loginlist.remove("json");
		result = mvc.perform(MockMvcRequestBuilders.post("/blob/update")
				.header("Authorization", loginlist.get(loginlist.indexOf("AccessToken") + 1))
				.param("id", "38")
				.param("title", "ssssssssssssssssssssssssssssssssssssssssssssssssss")
				.param("contype", "1")
				.param("webDataString", "????????")).andReturn();
		response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}

	/**
	 * ???????
	 * @throws Exception
	 */
	@Test
	public void testBlobDelete() throws Exception {
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
		t.add("code", list.get(list.indexOf("vicode") + 1));
		result = mvc.perform(MockMvcRequestBuilders.post("/user/login").params(t)).andReturn();
		response = result.getResponse();
		String [] login = response.getContentAsString().replaceAll("[{}:,]", "").split("\"");
		List<String> loginlist = new ArrayList<String>(Arrays.asList(login));
		loginlist.removeAll(Arrays.asList(""));
		loginlist.remove("json");
		result = mvc.perform(MockMvcRequestBuilders.post("/blob/delete")
				.header("Authorization", loginlist.get(loginlist.indexOf("AccessToken") + 1))
				.param("webid", "84")).andReturn();
		response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testgetAuthor() throws Exception {
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
		t.add("code", list.get(list.indexOf("vicode") + 1));
		result = mvc.perform(MockMvcRequestBuilders.post("/user/login").params(t)).andReturn();
		response = result.getResponse();
		String [] login = response.getContentAsString().replaceAll("[{}:,]", "").split("\"");
		List<String> loginlist = new ArrayList<String>(Arrays.asList(login));
		loginlist.removeAll(Arrays.asList(""));
		loginlist.remove("json");
		result = mvc.perform(MockMvcRequestBuilders.get("/blob/getauthor")
				.header("Authorization", loginlist.get(loginlist.indexOf("AccessToken") + 1))
				).andReturn();
		response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void testComment() throws Exception {
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
		t.add("code", list.get(list.indexOf("vicode") + 1));
		result = mvc.perform(MockMvcRequestBuilders.post("/user/login").params(t)).andReturn();
		response = result.getResponse();
		String [] login = response.getContentAsString().replaceAll("[{}:,]", "").split("\"");
		List<String> loginlist = new ArrayList<String>(Arrays.asList(login));
		loginlist.removeAll(Arrays.asList(""));
		loginlist.remove("json");
		result = mvc.perform(MockMvcRequestBuilders.post("/blob/comment")
				.header("Authorization", loginlist.get(loginlist.indexOf("AccessToken") + 1))
				.param("webid", "38")
				.param("content", "!!!!!")).andReturn();
		response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void testCommentReply() throws Exception {
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
		t.add("code", list.get(list.indexOf("vicode") + 1));
		result = mvc.perform(MockMvcRequestBuilders.post("/user/login").params(t)).andReturn();
		response = result.getResponse();
		String [] login = response.getContentAsString().replaceAll("[{}:,]", "").split("\"");
		List<String> loginlist = new ArrayList<String>(Arrays.asList(login));
		loginlist.removeAll(Arrays.asList(""));
		loginlist.remove("json");
		result = mvc.perform(MockMvcRequestBuilders.post("/blob/commentreply")
				.header("Authorization", loginlist.get(loginlist.indexOf("AccessToken") + 1))
				.param("webid", "38")
				.param("cid", "200")
				.param("bid", "181360226")
				.param("content", "Hello, World!")).andReturn();
		response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testBlob() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/blob/xx"))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testGetBlobs() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/user/getblobs")
				.param("uid", "181360226"))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testGetFans() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/user/getfans")
				.param("uid", "181360226"))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testGetIcons() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/user/geticons")
				.param("uid", "181360226"))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}
	
	/**
	 * MockHttpServletRequestBuilder
	 * @throws Exception
	 */
	@Test
	public void testPostBlobImg() throws Exception {
		FileInputStream fis = new FileInputStream("E:/装机/桌面/桌面背景/9.png");
		MvcResult result = mvc.perform(MockMvcRequestBuilders.multipart("/blob/blobimg")
				.file(new MockMultipartFile("img", "9.png", "image/png", fis)))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testGetOneSelfBlobs() throws Exception {
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
		t.add("code", list.get(list.indexOf("vicode") + 1));
		result = mvc.perform(MockMvcRequestBuilders.post("/user/login").params(t)).andReturn();
		response = result.getResponse();
		String [] login = response.getContentAsString().replaceAll("[{}:,]", "").split("\"");
		List<String> loginlist = new ArrayList<String>(Arrays.asList(login));
		loginlist.removeAll(Arrays.asList(""));
		loginlist.remove("json");
		result = mvc.perform(MockMvcRequestBuilders.get("/user/getoneselfblobs")
				.header("Authorization", loginlist.get(loginlist.indexOf("AccessToken") + 1))
				).andReturn();
		response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testLoginEmail() throws Exception {
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
		t.add("code", list.get(list.indexOf("vicode") + 1));
		result = mvc.perform(MockMvcRequestBuilders.post("/user/login").params(t)).andReturn();
		response = result.getResponse();
		String [] login = response.getContentAsString().replaceAll("[{}:,]", "").split("\"");
		List<String> loginlist = new ArrayList<String>(Arrays.asList(login));
		loginlist.removeAll(Arrays.asList(""));
		loginlist.remove("json");
		result = mvc.perform(MockMvcRequestBuilders.get("/user/loginemail")
				.header("Authorization", loginlist.get(loginlist.indexOf("AccessToken") + 1))
				).andReturn();
		response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testdataCheck() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/user/datacheck")
				.param("data", "???123zs@qq.com")
				.param("type", "ss"))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}
	
	
	@Test
	public void testGetCollection() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/user/getcollection")
				.param("uid", "181360226"))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testUpdateInfor() throws Exception {
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
		t.add("code", list.get(list.indexOf("vicode") + 1));
		result = mvc.perform(MockMvcRequestBuilders.post("/user/login").params(t)).andReturn();
		response = result.getResponse();
		String [] login = response.getContentAsString().replaceAll("[{}:,]", "").split("\"");
		List<String> loginlist = new ArrayList<String>(Arrays.asList(login));
		loginlist.removeAll(Arrays.asList(""));
		loginlist.remove("json");
		result = mvc.perform(MockMvcRequestBuilders.post("/user/changeinfor")
				.header("Authorization", loginlist.get(loginlist.indexOf("AccessToken") + 1))
				.param("username", "??")).andReturn();
		response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testGetLabel() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/blob/getlabel"))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testGetContype() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/blob/getcontype"))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testFileList() throws Exception {
			MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/file/getfileindex")
				)
				.andReturn();
			MockHttpServletResponse response = result.getResponse();
			response.setCharacterEncoding("UTF-8");
			System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testTime() {
		Instant start = Instant.now();
		System.out.println(SimpleUtils.getSimpleDateDifferenceFormat("2019-08-12 13:56:28"));
		Instant end = Instant.now();
		System.out.println("??: " + Duration.between(start, end).toMillis());
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testGetOneSelfBrowsingRecord() throws Exception {
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
		t.add("code", list.get(list.indexOf("vicode") + 1));
		result = mvc.perform(MockMvcRequestBuilders.post("/user/login").params(t)).andReturn();
		response = result.getResponse();
		String [] login = response.getContentAsString().replaceAll("[{}:,]", "").split("\"");
		List<String> loginlist = new ArrayList<String>(Arrays.asList(login));
		loginlist.removeAll(Arrays.asList(""));
		loginlist.remove("json");
		result = mvc.perform(MockMvcRequestBuilders.get("/user/getoneselfbrowsingrecord")
				.header("Authorization", loginlist.get(loginlist.indexOf("AccessToken") + 1))
		).andReturn();
		response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}


	@Autowired
	private SystemNotificationService systemNotificationService;

	/**
	 * @throws Exception
	 */
	@Test
	public void testGetOneselfNotification() throws Exception {
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
		t.add("code", list.get(list.indexOf("vicode") + 1));
		result = mvc.perform(MockMvcRequestBuilders.post("/user/login").params(t)).andReturn();
		response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
		String [] login = response.getContentAsString().replaceAll("[{}:,]", "").split("\"");
		List<String> loginlist = new ArrayList<String>(Arrays.asList(login));
		loginlist.removeAll(Arrays.asList(""));
		loginlist.remove("json");
		result = mvc.perform(MockMvcRequestBuilders.get("/user/getnotification")
				.header("Authorization", loginlist.get(loginlist.indexOf("AccessToken") + 1))
		).andReturn();
		response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
		new Thread(() -> {
			for(Integer i = 0; i < 20; i++) {
				systemNotificationService.insert(Arrays.asList(
					new SystemNotification(181360226, "????", 181360241, 1, SimpleUtils.getSimpleDateSecond())
				));
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
				}
			}
		}).start();
		for( Integer i = 0; i < 100; i++) {
			Thread.sleep(5000);
			result = mvc.perform(MockMvcRequestBuilders.get("/user/loginstatus")
					.header("Authorization", loginlist.get(loginlist.indexOf("AccessToken") + 1))
			).andReturn();
			response = result.getResponse();
			response.setCharacterEncoding("UTF-8");
			System.out.println(response.getContentAsString());
		}
	}

	@Test
	public void test() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/user/getcodeforlogin"))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		String[] split = response.getContentAsString().replaceAll("[{}:,]", "").split("\"");
		List<String> list = new ArrayList<>(Arrays.asList(split));
		list.removeAll(Arrays.asList(""));
		list.remove("json");
		MultiValueMap<String, String> t = new LinkedMultiValueMap<String, String>();
		t.add("uid", "181360241");
		t.add("password", "123456");
		t.add("hit", list.get(list.indexOf("token") + 1));
		t.add("code", list.get(list.indexOf("vicode") + 1));
		result = mvc.perform(MockMvcRequestBuilders.post("/user/login").params(t)).andReturn();
		response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
		String [] login = response.getContentAsString().replaceAll("[{}:,]", "").split("\"");
		List<String> loginlist = new ArrayList<String>(Arrays.asList(login));
		loginlist.removeAll(Arrays.asList(""));
		loginlist.remove("json");
		result = mvc.perform(MockMvcRequestBuilders.get("/admin/getuserinformationlist")
				.header("Authorization", loginlist.get(loginlist.indexOf("AccessToken") + 1))
		).andReturn();
		response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}

	@Test
	public void testPostUserImg() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/user/getcodeforlogin"))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		String[] split = response.getContentAsString().replaceAll("[{}:,]", "").split("\"");
		List<String> list = new ArrayList<>(Arrays.asList(split));
		list.removeAll(Arrays.asList(""));
		list.remove("json");
		MultiValueMap<String, String> t = new LinkedMultiValueMap<String, String>();
		t.add("uid", "181360241");
		t.add("password", "123456");
		t.add("hit", list.get(list.indexOf("token") + 1));
		t.add("code", list.get(list.indexOf("vicode") + 1));
		result = mvc.perform(MockMvcRequestBuilders.post("/user/login").params(t)).andReturn();
		response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
		String [] login = response.getContentAsString().replaceAll("[{}:,]", "").split("\"");
		List<String> loginlist = new ArrayList<String>(Arrays.asList(login));
		loginlist.removeAll(Arrays.asList(""));
		loginlist.remove("json");
		FileInputStream fis = new FileInputStream("E:/装机/桌面/桌面背景/11.png");
		result = mvc.perform(MockMvcRequestBuilders.multipart("/user/changeavatar")
				.file(new MockMultipartFile("img", "11.png", "image/png", fis))
				.header("Authorization", loginlist.get(loginlist.indexOf("AccessToken") + 1))
			).andReturn();
		response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}

	@Test
	public void testCollection() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/user/getcodeforlogin"))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		String[] split = response.getContentAsString().replaceAll("[{}:,]", "").split("\"");
		List<String> list = new ArrayList<>(Arrays.asList(split));
		list.removeAll(Arrays.asList(""));
		list.remove("json");
		MultiValueMap<String, String> t = new LinkedMultiValueMap<String, String>();
		t.add("uid", "181360241");
		t.add("password", "123456");
		t.add("hit", list.get(list.indexOf("token") + 1));
		t.add("code", list.get(list.indexOf("vicode") + 1));
		result = mvc.perform(MockMvcRequestBuilders.post("/user/login").params(t)).andReturn();
		response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		String [] login = response.getContentAsString().replaceAll("[{}:,]", "").split("\"");
		List<String> loginlist = new ArrayList<String>(Arrays.asList(login));
		loginlist.removeAll(Arrays.asList(""));
		loginlist.remove("json");
		result = mvc.perform(MockMvcRequestBuilders.post("/blob/collection")
				.param("bid", "181360226")
				.param("webid", "38")
				.header("Authorization", loginlist.get(loginlist.indexOf("AccessToken") + 1)))
				.andReturn();
		response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}

	@Test
	public void testCollectionStatus() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/blob/collectionstatuts")
		).andReturn();
		MockHttpServletResponse response = result.getResponse();
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}

}

