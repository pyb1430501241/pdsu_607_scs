package com.pdsu.scs.handler;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pdsu.scs.bean.Author;
import com.pdsu.scs.bean.BlobInformation;
import com.pdsu.scs.bean.MyCollection;
import com.pdsu.scs.bean.MyImage;
import com.pdsu.scs.bean.Result;
import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.bean.VisitInformation;
import com.pdsu.scs.bean.WebComment;
import com.pdsu.scs.bean.WebCommentReply;
import com.pdsu.scs.bean.WebInformation;
import com.pdsu.scs.exception.web.blob.NotFoundBlobIdException;
import com.pdsu.scs.exception.web.blob.comment.NotFoundCommentIdException;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.exception.web.user.UidAndWebIdRepetitionException;
import com.pdsu.scs.service.MyCollectionService;
import com.pdsu.scs.service.MyImageService;
import com.pdsu.scs.service.MyLikeService;
import com.pdsu.scs.service.UserInformationService;
import com.pdsu.scs.service.VisitInformationService;
import com.pdsu.scs.service.WebCommentReplyService;
import com.pdsu.scs.service.WebCommentService;
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
	
	/**
	 * 评论相关
	 */
	@Autowired
	private WebCommentService webCommentService;
	
	/**
	 * 回复评论相关
	 */
	@Autowired
	private WebCommentReplyService webCommentReplyService;
	
	/**
	 * 关注相关
	 */
	@Autowired
	private MyLikeService myLikeService;
	
	private static final String EX = "exception";
	
	/**
	 * 日志
	 */
	private static final Logger log = LoggerFactory.getLogger(BlobHandler.class);
	
	/**
	 * 获取首页的数据
	 * @return
	 */
	@RequestMapping(value = "/getwebindex", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public Result getWebForIndex(@RequestParam(value = "p", defaultValue = "1") Integer p) {
		try {
			PageHelper.startPage(p, 10);
			//获取按时间排序的投稿
			List<WebInformation> webList = webInformationService.selectWebInformationOrderByTimetest();
			if(webList == null || webList.size() == 0) {
				log.info("首页没有数据");
				return Result.accepted();
			}
			//根据投稿的投稿人 uid 获取这些投稿人的信息
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
			List<BlobInformation> blobList = new ArrayList<BlobInformation>();
			for (int i = 0; i < webList.size(); i++) {
				BlobInformation blobInformation = new BlobInformation(
						webList.get(i), visitList.get(i),
						thumbsList.get(i), collectionList.get(i)
				);
				for(UserInformation user : userList) {
					if(webList.get(i).getUid().equals(user.getUid())) {
						blobInformation.setUser(user);;
						break;
					}
				}
				blobList.add(blobInformation);
			}
			PageInfo<BlobInformation> pageInfo = new PageInfo<BlobInformation>(blobList, 5);
			return Result.success().add("blobList", pageInfo);
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
	@RequestMapping(value = "/{webid}", method = RequestMethod.GET)
	@CrossOrigin
	public Result toBlob(@PathVariable("webid")Integer id) {
		try {
			//获取博客页面信息
			WebInformation web = webInformationService.selectById(id);
			Integer uid = web.getUid();
			//查询作者其余文章
			List<WebInformation> webList = webInformationService.selectWebInformationsByUid(uid);
			List<WebInformation> webs = new ArrayList<WebInformation>();
			int i = 0;
			for (WebInformation webInformation : webList) {
				if(i > 5) {
					break;
				}
				webs.add(webInformation);
				i++;
			}
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
			//获取文章评论
			List<WebComment> commentList = webCommentService.selectCommentsByWebId(id);
			List<WebCommentReply> commentReplyList = webCommentReplyService.selectCommentReplysByWebComments(commentList);
			for(WebComment webcomment : commentList) {
				WebComment b = webcomment;
				List<WebCommentReply> webcommentReplyList = new ArrayList<>();
				for(WebCommentReply reply : commentReplyList) {
					if(reply.getCid().equals(b.getId())) {
						webcommentReplyList.add(reply);
					}
				}
				b.setCommentReplyList(webcommentReplyList);
			}
			return Result.success().add("web", web).add("webList", webs)
				   .add("visit", visits)
				   .add("thubms", thubms)
				   .add("collection", collections)
				   .add("commentList", commentList);
		}catch (NotFoundUidException e) {
			log.info("获取文章信息失败, 原因: " + e.getMessage());
			return Result.fail().add(EX, "文章不见了");
		} catch (UnsupportedEncodingException e) {
			log.error("文章: " + id + "编码转换失败!!!");
			return Result.fail().add(EX, "获取文章信息失败");
		} catch (NotFoundBlobIdException e) {
			log.info("文章: " + id + e.getMessage());
			return Result.fail().add(EX, "文章不见了");
		} catch (Exception e) {
			log.warn("发生未知错误, 原因: " + e.getMessage());
			return Result.fail().add(EX, "未定义类型错误");
		}
	}
	
	/**
	 * 处理收藏请求, 作者的学号和网页id由前端获取, 收藏人学号从session里获取
	 * 
	 * @param uid  作者的学号
	 * @param webid 网页id
	 * @return
	 */
	@RequestMapping(value = "/collection", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result collection(Integer bid, Integer webid) {
		//获取当前登录用户的UID
		Integer uid = null;
		try {
			uid = ShiroUtils.getUserInformation().getUid();
			log.info("用户: " + uid + ", 收藏博客: " + webid + ", 作者为: " + bid + ", 开始");
			//添加一条收藏记录
			boolean flag = myConllectionService.insert(new MyCollection(null, uid, webid, bid));
			if(flag) {
				log.info("用户: " + uid + ", 收藏 " + webid + " 成功");
				return Result.success();
			}
			log.warn("收藏失败, 连接数据库失败");
			return Result.fail().add(EX, "网络延迟, 请稍候重试");
		}catch (UidAndWebIdRepetitionException e) {
			return Result.fail().add(EX, e.getMessage());
		}catch (Exception e) {
			if(uid == null) {
				log.info("用户收藏文章失败, 原因: 用户未登录");
				return Result.fail().add(EX, "未登录");
			}else {
				log.error("用户: " + uid + "收藏博客: " + webid + "时失败, 原因为: " + e.getMessage());
				return Result.fail().add(EX, "未定义类型错误");
			}
		}
	}

	/**
	 * 取消收藏请求, 作者学号后端session获取, 网页编号前端传入
	 * @param webid  网页ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/decollection", method = RequestMethod.POST)
	@CrossOrigin
	public Result deCollection(Integer webid) {
		//获取当前登录用户的信息
		UserInformation user = null;
		try {
			user = ShiroUtils.getUserInformation();
			log.info("用户: " + user.getUid() + ", 取消收藏博客: " + webid + "开始");
			//删除一条收藏记录
			if(myConllectionService.delete(user.getUid(), webid)) {
				log.info("取消收藏成功");
				return Result.success();
			}else {
				log.warn("取消收藏失败, 原因: 连接服务器失败");
				return Result.fail().add(EX, "连接服务器失败, 请稍候重试");
			}
		}catch (UidAndWebIdRepetitionException e) {
			log.info("用户: " + user.getUid() + ", 取消收藏失败");
			return Result.fail().add(EX, e.getMessage());
		}catch (Exception e) {
			if(user == null) {
				log.info("用户收藏文章失败, 原因: 用户未登录");
				return Result.fail().add(EX, "未登录");
			} else {
				log.error("用户: " + user.getUid() + "取消收藏博客失败, 原因: " + e.getMessage());
				return Result.fail().add(EX, "未定义类型错误");
			}
		}
	}
	
	/**
	 * 处理投稿请求
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/contribution", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result insert(WebInformation web) {
		//获取当前登录用户的UID
		Integer uid = null;
		try {
			uid = ShiroUtils.getUserInformation().getUid();
			log.info("用户: " + uid + "发布文章开始");
			//设置作者UID
			web.setUid(uid);
			//把网页主体内容转化为byte字节
			web.setWebData(web.getWebDataString().getBytes("utf-8"));
			//设置文章投稿时间
			web.setSubTime(SimpleUtils.getSimpleDateSecond());
			//发布文章
			int flag = webInformationService.insert(web);
			if(flag != -1) {
				log.info("用户: " + uid + "发布文章成功, 文章标题为: " + web.getTitle());
				return Result.success().add(EX, "发布成功").add("webid", flag);
			}
			log.info("用户: " + uid + "发布文章失败");
			return Result.fail().add(EX, "发布失败");
		}catch (UnsupportedEncodingException e) {
			log.warn("文章: " + web.getUid() + " 转码失败!!!");
			return Result.fail().add(EX, "添加文章信息失败");
		}catch (Exception e) {
			if(uid == null) {
				log.info("用户发布文章失败, 原因: 用户未登录");
				return Result.fail().add(EX, "未登录");
			} else {
				log.error("用户: " + uid + ", 发布文章失败, 原因为: " + e.getMessage());
				return Result.fail().add(EX, "发布失败, 未知原因");
			}
		}
	}
	
	/**
	 * 删除文章
	 * @param webid  文章id
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result delete(Integer webid) {
		//获取当前登录用户的信息
		UserInformation user = null;
		try {
			user = ShiroUtils.getUserInformation();
			log.info("开始删除文章, 文章ID为: " + webid + " 文章作者为: " + user.getUid());
			WebInformation webInformation = webInformationService.selectById(webid);
			if(user.getUid() != webInformation.getUid()) {
				log.info("你: " + user.getUid() + " 无权删除文章: " + webid);
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
			if(user == null) {
				log.info("用户删除文章失败, 原因: 未登录");
				return Result.fail().add(EX, "未登录");
			}
			log.error("删除文章失败, 原因: " + e.getMessage());
			return Result.fail().add(EX, "未定义类型错误");
		}
	}
	
	/**
	 * 更新文章
	 * @param web  更新后的文章
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result update(WebInformation web) {
		//获取当前登录用户的信息
		UserInformation user = null;
		try {
			user = ShiroUtils.getUserInformation();
			log.info("用户: " + user.getUid() + ", 开始更新文章: " + web.getId());
			web.setWebData(web.getWebDataString().getBytes());
			boolean b = webInformationService.updateByWebId(web);
			if(b) {
				log.info("更新文章成功");
				return Result.success();
			}else {
				log.info("更新文章失败");
				return Result.fail().add(EX, "网络异常, 请稍候重试");
			}
		}catch (Exception e) {
			if(user == null) {
				log.info("用户更新文章失败, 原因: 用户未登录");
				return Result.fail().add(EX, "未登录");
			}else {
				log.error("更新文章失败, 原因: " + e.getMessage());
				return Result.fail().add(EX, "未知原因");
			}
		}
	}
	
	@RequestMapping(value = "/comment", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result postComment(Integer webid, String content) {
		UserInformation user = null;
		try {
			user =ShiroUtils.getUserInformation();
			log.info("用户: " + user.getUid() + "在博客: " + webid + "发布评论, 内容为: " + content);
			boolean b = webCommentService.insert(new WebComment(webid, user.getUid(), 
					content, 0, SimpleUtils.getSimpleDateSecond(), 0));
			if(b) {
				log.info("用户发布评论成功");
				return Result.success();
			}
			log.warn("用户发布评论失败, 原因: 插入数据库失败");
			return Result.fail().add(EX, "网络链接失败, 请稍候重试");
		} catch (NotFoundBlobIdException e) {
			log.info("用户发布评论失败, 原因: 该博客已被删除");
			return Result.fail().add(EX, "该博客不存在");
		}catch (Exception e) {
			if(user == null) {
				log.info("用户发布评论失败, 原因: 未登录");
				return Result.fail().add(EX, "用户未登录");
			}else {
				log.error("用户: " + user.getUid() + "在博客: " + webid + "发布评论失败, 内容为: " + content
						+ "错误原因为: " + e.getMessage());
				return Result.fail().add(EX, "未定义类型错误");
			}
		}
	}
	
	@RequestMapping(value = "/commentreply", method = RequestMethod.POST)
	@CrossOrigin
	@ResponseBody
	public Result postCommentReply(Integer cid, Integer bid, String content) {
		UserInformation user = null;
		try {
			user = ShiroUtils.getUserInformation();
			log.info("用户: " + user.getUid() + " 回复评论: " + cid + "被回复人: " + bid + ", 内容为:" + content);
			boolean b = webCommentReplyService.insert(new WebCommentReply(cid, user.getUid(), bid, content, 
						0, SimpleUtils.getSimpleDateSecond()));
			if(b) {
				log.info("用户回复评论成功");
				return Result.success();
			}
			log.warn("用户回复评论失败, 原因: 连接数据库失败");
			return Result.fail().add(EX, "网络连接失败, 请稍候重试");
		} catch (NotFoundBlobIdException e) {
			log.info("用户回复评论失败, 原因: 该博客已被删除");
			return Result.fail().add(EX, "该博客不存在");
		} catch (NotFoundCommentIdException e) {
			log.info("用户回复评论失败, 原因: 该评论已被删除");
			return Result.fail().add(EX, "该评论不存在");
		} catch (Exception e) {
			if(user == null) {
				log.info("用户回复评论失败, 原因: 未登录");
				return Result.fail().add(EX, "用户未登录");
			}
			log.info("用户: " + user.getUid() + " 回复评论: " + cid + " 失败, 被回复人: " + bid + ", 内容为:" + content);
			return Result.fail().add(EX, "未定义类型错误");
		}
	}
	
	
	@RequestMapping(value = "/getauthor", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public Result getAuthorByUid(Integer uid) {
		try {
			log.info("获取作者: " + uid + "信息开始");
			UserInformation user = userInformationService.selectByUid(uid);
			Author author = new Author();
			author.setUid(user.getUid());
			author.setUsername(user.getUsername());
			String imgpath = myInageService.selectImagePathByUid(uid).getImagePath();
			author.setImgpath(imgpath);
			Integer fans = (int) myLikeService.countByLikeId(uid);
			author.setFans(fans);
			Integer thumbs = webThubmsService.countThumbsByUid(uid);
			author.setThumbs(thumbs);
			Integer comment = webCommentService.countByUid(uid);
			author.setComment(comment);
			Integer visits = visitInformationService.selectVisitsByVid(uid);
			author.setVisits(visits);
			Integer collection = myConllectionService.countCollectionByUid(uid);
			author.setCollection(collection);
			Integer original = webInformationService.countOriginalByUidAndContype(uid, 1);
			author.setOriginal(original);
			log.info("获取作者信息成功");
			return Result.success().add("author", author);
		} catch (NotFoundUidException e) {
			log.info("获取作者信息失败, 原因: " + e.getMessage());
			return Result.fail().add(EX, e.getMessage());
		} catch (Exception e) {
			log.error("获取作者信息发生未知错误, 原因: " + e.getMessage());
			return Result.fail().add(EX, "未定义类型错误");
		}
	}
}
