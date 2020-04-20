package com.pdsu.scs.handler;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pdsu.scs.bean.MyCollection;
import com.pdsu.scs.bean.MyImage;
import com.pdsu.scs.bean.MyLike;
import com.pdsu.scs.bean.Result;
import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.bean.VisitInformation;
import com.pdsu.scs.bean.WebInformation;
import com.pdsu.scs.service.MyCollectionService;
import com.pdsu.scs.service.MyImageService;
import com.pdsu.scs.service.MyLikeService;
import com.pdsu.scs.service.UserInformationService;
import com.pdsu.scs.service.VisitInformationService;
import com.pdsu.scs.service.WebInformationService;
import com.pdsu.scs.service.WebThumbsService;
import com.pdsu.scs.utils.ShiroUtils;
import com.pdsu.scs.utils.SimpleDateUtils;

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
	
	@Autowired
	private VisitInformationService visitInformationService;
	
	@Autowired
	private MyLikeService myLikeService;
	
	@Autowired
	private MyCollectionService myConllectionService;
	
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
			//获取文章访问量
			List<Integer> visitList = visitInformationService.selectVisitsByWebIds(webids);
			//获取文章收藏量
			List<Integer> collectionList = myConllectionService.selectCollectionssByWebIds(webids);
			return Result.success().add("webList", webList)
					.add("userList", userList).add("visitList", visitList)
					.add("thumbsList", thumbsList)
					.add("collectionList", collectionList);
		}catch (Exception e) {
			e.printStackTrace();
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
			//获取博客页面信息
			WebInformation web = webInformationService.selectById(id);
			Integer uid = web.getUid();
			//查询作者信息
			UserInformation author = userInformationService.selectByUid(uid);
			//获取作者头像
			author.setImgpath(myInageService.selectImagePathByUid(uid).getImagePath());
			//密码置空
			author.setPassword(null);
			//查询作者其余文章
			List<WebInformation> webList = webInformationService.selectWebInformationsByUid(uid);
			//把 byte字节码转化为 String
			web.setWebDataString(new String(web.getWebData(),"utf-8"));
			//byte字节码置空
			web.setWebData(null);
			//从Session里获取当前登录用户
			UserInformation user = ShiroUtils.getUserInformation();
			//如果没有用户登录
			if(user == null) {
				user = new UserInformation();
				//默认访问人为 181360226
				user.setUid(181360226);
			}
			//添加一个访问记录
			visitInformationService.insert(new VisitInformation(null, user.getUid(), uid, web.getId()));
			//获取网页访问量
			Integer visits = visitInformationService.selectvisitByWebId(web.getId());
			//获取网页点赞数
			Integer thubms = webThubmsService.selectThumbsForWebId(web.getId());
			//获取网页收藏量
			Integer collections = myConllectionService.selectCollectionsByWebId(web.getId());
			return Result.success().add("web", web).add("webList", webList)
				   .add("author", author)
				   .add("visits", visits)
				   .add("thubms", thubms)
				   .add("collections", collections);
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
	public Result collection(Integer bid, Integer webid) {
		try {
			Integer uid = ShiroUtils.getUserInformation().getUid();
			boolean flag = myConllectionService.insert(new MyCollection(null, uid, webid, bid));
			if(flag) {
				return Result.success();
			}
			return Result.fail();
		}catch (Exception e) {
			return Result.fail();
		}
	}
	
	/**
	 * 处理关注请求, 作者的学号由前端获取, 关注人学号从session里获取
	 * 
	 * @param uid  作者的学号
	 * @return
	 */
	@RequestMapping("/like")
	@ResponseBody
	public Result like(Integer uid) {
		try {
			//从session里获取当前登录的人的学号
			Integer likeId = ShiroUtils.getUserInformation().getUid();
			//插入记录
			boolean flag = myLikeService.insert(new MyLike(null, likeId, uid));
			if(flag) {
				return Result.success();
			}
			return Result.fail();
		}catch (Exception e) {
			return Result.fail();
		}
	}
	
	@RequestMapping("/add")
	public String in() {
		return "blob/insert";
	}
	
	/**
	 * 处理投稿请求
	 * @param user
	 * @return
	 */
	@RequestMapping("/insert")
	@ResponseBody
	public Result insert(WebInformation web) {
		try {
			web.setUid(ShiroUtils.getUserInformation().getUid());
			web.setWebData(web.getWebDataString().getBytes("utf-8"));
			web.setSubTime(SimpleDateUtils.getSimpleDateSecond());
			boolean flag = webInformationService.insert(web);
			if(flag) {
				return Result.success().add("hint", "发布成功");
			}
			return Result.fail().add("hint", "发布失败");
		}catch (Exception e) {
			e.printStackTrace();
			return Result.fail().add("hint", "发布失败, 未知原因");
		}
	}
}
