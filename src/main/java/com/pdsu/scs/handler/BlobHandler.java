package com.pdsu.scs.handler;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pdsu.scs.bean.MyImage;
import com.pdsu.scs.bean.Result;
import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.bean.WebInformation;
import com.pdsu.scs.service.MyImageService;
import com.pdsu.scs.service.UserInformationService;
import com.pdsu.scs.service.WebInformationService;
import com.pdsu.scs.service.WebThumbsService;

@RequestMapping("/blob")
@Controller
public class BlobHandler {

	@Autowired
	private WebInformationService webInformationService;
	
	@Autowired
	private UserInformationService userInformationService;
	
	@Autowired
	private WebThumbsService webThubmsService;
	
	@Autowired
	private MyImageService myInageService;
	
	/**
	 * 转发到网站首页
	 * @return
	 */
	@RequestMapping("/index")
	public String index() {
		return "blob/index";
	}
	
	/**
	 * 转发到博客页面
	 * @param request
	 * @return
	 */
	@RequestMapping("{id}")
	public String blob(HttpServletRequest request) {
		String servletPath = request.getServletPath();
		request.setAttribute("id", Integer.parseInt(
				servletPath.substring(servletPath.lastIndexOf("/")+2, 
				servletPath.length())));
		return "blob/blob";
	}
	
	/**
	 * 获取首页的数据
	 * @return
	 */
	@RequestMapping("/getWebindex")
	@ResponseBody
	public Result getWebForIndex() {
		try {
			//获取按时间排序的投稿
			List<WebInformation> webList = webInformationService.selectWebInformationOrderByTimetest();
			//根据投稿的投稿人uid获取这些投稿人的信息
			List<Integer> uids = new ArrayList<Integer>();
			List<Integer> webids = new ArrayList<Integer>();
			for(WebInformation w : webList) {
				uids.add(w.getUid());
				webids.add(w.getId());
			}
			List<UserInformation> userList = userInformationService.selectUsersByUids(uids);
			List<MyImage> imgpaths = myInageService.selectImagePathByUids(uids);
			//密码置为 null, 并添加头像地址
			for(UserInformation user : userList) {
				UserInformation t  = user;
				t.setPassword(null);
				for(MyImage m : imgpaths) {
					if(user.getUid().equals(m.getUid())) {
						t.setImgpath(m.getImagePath());
					}
				}
			}
			//获取文章的点赞数
			List<Integer> thumbsList = webThubmsService.selectThumbssForWebId(webids);
			//获取文章阅读量
			
			return Result.success().add("webList", webList).add("userList", userList)
				   .add("thumbsList", thumbsList);
		}catch (Exception e) {
			return Result.fail();
		}
	}
	
	/**
	 * 获取博客的相关信息
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/blob")
	public Result toblob(@RequestParam(value = "id", required = false)Integer id) {
		try {
			WebInformation web = webInformationService.selectById(id);
			Integer uid = web.getUid();
			UserInformation author = userInformationService.selectByUid(uid);
			List<WebInformation> webList = webInformationService.selectWebInformationsByUid(uid);
			web.setWebDataString(new String(web.getWebData(),"utf-8"));
			return Result.success().add("web", web).add("webList", webList)
				   .add("author", author);
		}catch (Exception e) {
			return Result.fail();
		}
	}
	
	/**
	 * 处理收藏请求, 作者的学号和网页id由前端获取, 收藏人学号从session里获取
	 * 
	 * @param uid  作者的学号
	 * @param webid 网页id
	 * @return
	 */
	public Result collection(Integer uid, Integer webid) {
		try {
			return Result.success();
		}catch (Exception e) {
			return Result.fail();
		}
	}
	
	/**
	 * 处理关注请求, 作者的学由前端获取, 关注人学号从session里获取
	 * 
	 * @param uid  作者的学号
	 * @return
	 */
	public Result like(Integer uid) {
		try {
			return Result.success();
		}catch (Exception e) {
			return Result.fail();
		}
	}
}
