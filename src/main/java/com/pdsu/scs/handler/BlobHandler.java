package com.pdsu.scs.handler;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pdsu.scs.bean.MyCollection;
import com.pdsu.scs.bean.MyImage;
import com.pdsu.scs.bean.Result;
import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.bean.VisitInformation;
import com.pdsu.scs.bean.WebInformation;
import com.pdsu.scs.exception.web.blob.NotFoundBlobIdException;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.exception.web.user.UidAndWebIdRepetitionException;
import com.pdsu.scs.service.MyCollectionService;
import com.pdsu.scs.service.MyImageService;
import com.pdsu.scs.service.UserInformationService;
import com.pdsu.scs.service.VisitInformationService;
import com.pdsu.scs.service.WebInformationService;
import com.pdsu.scs.service.WebThumbsService;
import com.pdsu.scs.utils.ShiroUtils;
import com.pdsu.scs.utils.SimpleUtils;

/**
 * 
 * @author 半梦
 *
 */
@Controller
@RequestMapping("/blob")
public class BlobHandler {

	/**
	 * 博客相关逻辑处理
	 */
	@Autowired
	private WebInformationService webInformationService;
	
	/**
	 * 用户相关逻辑处理
	 */
	@Autowired
	private UserInformationService userInformationService;
	
	/**
	 * 点赞相关逻辑处理
	 */
	@Autowired
	private WebThumbsService webThubmsService;
	
	/**
	 * 头像相关逻辑处理
	 */
	@Autowired
	private MyImageService myInageService;
	
	/**
	 * 访问相关逻辑处理
	 */
	@Autowired
	private VisitInformationService visitInformationService;
	
	/**
	 * 收藏相关逻辑处理
	 */
	@Autowired
	private MyCollectionService myConllectionService;

	private static final String EX = "exception";
	
	/**
	 * 日志
	 */
	private static final Logger log = LoggerFactory.getLogger(BlobHandler.class);
	
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
	public String blob(@PathVariable("id")String id,Map<String, Object> map) {
		String t = id.substring(1, id.length());
		map.put("id", t);
		return "blob/blob";
	}
	
	/**
	 * 获取首页的数据
	 * @return
	 */
	@RequestMapping("/getwebindex")
	@ResponseBody
	@CrossOrigin
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
			log.error("获取首页数据失败, 原因为: " + e.getMessage());
			return Result.fail();
		}
	}
	
	/**
	 * 获取博客的相关信息
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getblob")
	@CrossOrigin
	public Result toBlob(@RequestParam(value = "webid", required = false)Integer id) {
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
				//默认访问人为 181360226
				user = new UserInformation(181360226);
			}
			//添加一个访问记录
			visitInformationService.insert(new VisitInformation(null, user.getUid(), uid, web.getId()));
			log.info("用户: " + user.getUid() + ", 访问了文章: " + web.getId() + ", 作者为: " + uid);
			//获取网页访问量
			Integer visits = visitInformationService.selectvisitByWebId(web.getId());
			//获取网页点赞数
			Integer thubms = webThubmsService.selectThumbsForWebId(web.getId());
			//获取网页收藏量
			Integer collections = myConllectionService.selectCollectionsByWebId(web.getId());
			return Result.success().add("web", web).add("webList", webList)
				   .add("author", author)
				   .add("visit", visits)
				   .add("thubms", thubms)
				   .add("collection", collections);
		}catch (NotFoundUidException e) {
			log.info("文章: " + id + e.getMessage());
			return Result.fail().add(EX, "获取文章信息失败");
		} catch (UnsupportedEncodingException e) {
			log.error("文章: " + id + "编码转换失败!!!");
			return Result.fail().add(EX, "获取文章信息失败");
		} catch (NotFoundBlobIdException e) {
			log.info("文章: " + id + e.getMessage());
			return Result.fail().add(EX, "文章不见了");
		} catch (Exception e) {
			log.warn("发生未知错误!");
			return Result.fail().add(EX, "未知错误");
		}
	}
	
	/**
	 * 处理收藏请求, 作者的学号和网页id由前端获取, 收藏人学号从session里获取
	 * 
	 * @param uid  作者的学号
	 * @param webid 网页id
	 * @return
	 */
	@RequestMapping("/collection")
	@ResponseBody
	@CrossOrigin
	public Result collection(Integer bid, Integer webid) {
		//获取当前登录用户的UID
		Integer uid = ShiroUtils.getUserInformation().getUid();
		try {
			log.info("用户: " + uid + ", 收藏博客: " + webid + ", 作者为: " + bid + ", 开始");
			//添加一条收藏记录
			boolean flag = myConllectionService.insert(new MyCollection(null, uid, webid, bid));
			if(flag) {
				log.info("用户: " + uid + ", 收藏 " + webid + " 成功");
				return Result.success();
			}
			log.info("收藏失败");
			return Result.fail().add(EX, "网络延迟, 请稍候重试");
		}catch (UidAndWebIdRepetitionException e) {
			return Result.fail().add(EX, e.getMessage());
		}catch (Exception e) {
			log.warn("用户: " + uid + "收藏博客: " + webid + "时失败, 原因为: " + e.getMessage());
			return Result.fail().add(EX, "未知错误");
		}
	}

	/**
	 * 取消收藏请求, 作者学号后端session获取, 网页编号前端传入
	 * @param webid  网页ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/decollection")
	@CrossOrigin
	public Result deCollection(Integer webid) {
		//获取当前登录用户的信息
		UserInformation user = ShiroUtils.getUserInformation();
		log.info("用户: " + user.getUid() + ", 取消收藏博客: " + webid + "开始");
		try {
			//删除一条收藏记录
			if(myConllectionService.delete(user.getUid(), webid)) {
				log.info("取消收藏成功");
				return Result.success();
			}else {
				log.info("取消收藏失败");
				return Result.fail();
			}
		}catch (UidAndWebIdRepetitionException e) {
			log.info("用户: " + user.getUid() + ", 取消收藏失败");
			return Result.fail().add(EX, e.getMessage());
		}catch (Exception e) {
			log.info("用户: " + user.getUid() + "取消收藏博客: " + webid + "失败, 原因为: " + e.getMessage());
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
	@CrossOrigin
	public Result insert(WebInformation web) {
		//获取当前登录用户的UID
		Integer uid = ShiroUtils.getUserInformation().getUid();
		try {
			log.info("用户: " + uid + "发布文章开始");
			//设置作者UID
			web.setUid(uid);
			//把网页主体内容转化为byte字节
			web.setWebData(web.getWebDataString().getBytes("utf-8"));
			//设置文章投稿时间
			web.setSubTime(SimpleUtils.getSimpleDateSecond());
			//发布文章
			boolean flag = webInformationService.insert(web);
			if(flag) {
				log.info("用户: " + uid + "发布文章成功, 文章标题为: " + web.getTitle());
				return Result.success().add(EX, "发布成功");
			}
			log.info("用户: " + uid + "发布文章失败");
			return Result.fail().add(EX, "发布失败");
		}catch (UnsupportedEncodingException e) {
			log.warn("文章: " + web.getUid() + " 转码失败!!!");
			return Result.fail().add(EX, "添加文章信息失败");
		}catch (Exception e) {
			log.error("用户: " + uid + ", 发布文章失败, 原因为: " + e.getMessage());
			return Result.fail().add(EX, "发布失败, 未知原因");
		}
	}
	
	/**
	 * 删除文章
	 * @param webid  文章id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	@CrossOrigin
	public Result delete(Integer webid) {
		//获取当前登录用户的信息
		UserInformation user = ShiroUtils.getUserInformation();
		try {
			log.info("开始删除文章, 文章ID为: " + webid + " 文章作者为: " + user.getUid());
			WebInformation webInformation = webInformationService.selectById(webid);
			if(user.getUid() != webInformation.getUid()) {
				return Result.fail().add(EX, "您无权删除这篇文章");
			}
			boolean b = webInformationService.deleteById(webid);
			if(b) {
				log.info("删除文章成功, 文章ID为: " + webid);
				return Result.success();
			}else {
				log.warn("删除文章失败, 数据库异常");
				return Result.fail().add(EX, "网络异常, 请稍候重试");
			}
		} catch (NotFoundBlobIdException e) {
			log.info("删除文章失败, 文章不存在");
			return Result.fail().add(EX, e.getMessage());
		} catch (Exception e) {
			log.info("删除文章失败, 文章不存在");
			return Result.fail().add(EX, "删除失败, 未知原因");
		}
	}
	
	/**
	 * 更新文章
	 * @param web  更新后的文章
	 * @return
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	@CrossOrigin
	public Result update(WebInformation web) {
		//获取当前登录用户的信息
		UserInformation user = ShiroUtils.getUserInformation();
		log.info("用户: " + user.getUid() + ", 开始更新文章: " + web.getId() + "作者: " + web.getUid());
		try {
			boolean b = webInformationService.updateByWebId(web);
			if(b) {
				log.info("更新文章成功");
				return Result.success();
			}else {
				log.info("更新文章失败");
				return Result.fail().add(EX, "网络异常, 请稍候重试");
			}
		}catch (Exception e) {
			log.info("更新文章失败, 原因: " + e.getMessage());
			return Result.fail().add(EX, "未知原因");
		}
	}
}
