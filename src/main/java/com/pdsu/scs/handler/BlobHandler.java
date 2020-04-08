package com.pdsu.scs.handler;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pdsu.scs.bean.Result;
import com.pdsu.scs.bean.WebInformation;
import com.pdsu.scs.service.UserInformationService;
import com.pdsu.scs.service.WebInformationService;

@RequestMapping("/blob")
@Controller
public class BlobHandler {

	@Autowired
	private WebInformationService webInformationService;
	
	@Autowired
	private UserInformationService userInformationService;
	
	@RequestMapping("/index")
	public String index() {
		return "blob/index";
	}
	
	@RequestMapping("{id}")
	public String blob(HttpServletRequest request) {
		String servletPath = request.getServletPath();
		request.setAttribute("id", Integer.parseInt(
				servletPath.substring(servletPath.lastIndexOf("/")+2, 
				servletPath.length())));
		return "blob/blob";
	}
	
	@RequestMapping("/getWebindex")
	@ResponseBody
	public Result getWebForIndex() {
		try {
			//获取按时间排序的投稿
			List<WebInformation> list = webInformationService.selectWebInformationOrderByTimetest();
			//根据投稿的投稿人uid获取这些投稿人的信息
			List<Integer> uids = new ArrayList<Integer>();
			for(WebInformation w : list) {
				uids.add(w.getUid());
			}
			
			return Result.success().add("weblist", list);
		}catch (Exception e) {
			return Result.fail();
		}
	}
	
	@ResponseBody
	@RequestMapping("/blob")
	public Result toblob(@RequestParam(value = "id", required = false)Integer id) {
		System.out.println(id);
		try {
			WebInformation web = webInformationService.selectById(id);
			web.setWebDataString(new String(web.getWebData(),"utf-8"));
			return Result.success().add("web", web);
		}catch (Exception e) {
			return Result.fail();
		}
	}
}
