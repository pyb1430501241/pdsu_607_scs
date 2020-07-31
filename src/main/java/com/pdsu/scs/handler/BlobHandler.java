package com.pdsu.scs.handler;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
import com.pdsu.scs.bean.WebThumbs;
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
import com.pdsu.scs.utils.HashUtils;
import com.pdsu.scs.utils.RandomUtils;
import com.pdsu.scs.utils.ShiroUtils;
import com.pdsu.scs.utils.SimpleUtils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

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
	
	private static final String FILEPATH = "/pdsu/web/blob/img/";
	
	private static final String SUFFIX = ".jpg";
	
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
			log.info("开始获取博客页面信息");
			WebInformation web = webInformationService.selectById(id);
			Integer uid = web.getUid();
			log.info("获取作者其余文章");
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
			web.setWebDataString(new String(web.getWebData(),"utf-8"));
			web.setWebData(null);
			UserInformation user = ShiroUtils.getUserInformation();
			if(user == null) {
				user = new UserInformation(181360226);
			}
			log.info("添加访问信息");
			visitInformationService.insert(new VisitInformation(null, user.getUid(), uid, web.getId()));
			log.info("用户: " + user.getUid() + ", 访问了文章: " + web.getId() + ", 作者为: " + uid);
			log.info("获取网页访问量");
			Integer visits = visitInformationService.selectvisitByWebId(web.getId());
			log.info("获取文章点赞数");
			Integer thubms = webThubmsService.selectThumbsForWebId(web.getId());
			log.info("获取文章收藏量");
			Integer collections = myConllectionService.selectCollectionsByWebId(web.getId());
			log.info("获取文章评论");
			List<WebComment> commentList = webCommentService.selectCommentsByWebId(id);
			List<WebCommentReply> commentReplyList = webCommentReplyService.selectCommentReplysByWebComments(commentList);
			List<Integer> uids = new ArrayList<>();
			for(WebComment webComment : commentList) {
				uids.add(webComment.getUid());
			}
			for(WebCommentReply reply : commentReplyList) {
				uids.add(reply.getUid());
			}
			log.info("获取评论者信息");
			List<UserInformation> userList = userInformationService.selectUsersByUids(uids);
			log.info("获取评论者头像信息");
			List<MyImage> imageList = myInageService.selectImagePathByUids(uids);
			for(MyImage img : imageList) {
				for (UserInformation us : userList) {
					if(img.getUid().equals(us.getUid())) {
						us.setImgpath(img.getImagePath());
						break;
					}
				}
			}
			for(WebComment webComment : commentList) {
				WebComment webc = webComment;
				for(UserInformation us : userList) {
					if(webc.getUid().equals(us.getUid())) {
						webc.setUsername(us.getUsername());
						webc.setImgpath(us.getImgpath());
						break;
					}
				}
			}
			for(WebCommentReply reply : commentReplyList) {
				WebCommentReply webc = reply;
				for(UserInformation us : userList) {
					if(webc.getUid().equals(us.getUid())) {
						webc.setUsername(us.getUsername());
						webc.setImgpath(us.getImgpath());
						break;
					}
				}
			}
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
	
	/**
	 * 处理评论请求
	 * @param webid
	 * @param content
	 * @return
	 */
	@RequestMapping(value = "/comment", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result postComment(Integer webid, String content) {
		UserInformation user = null;
		try {
			user =ShiroUtils.getUserInformation();
			log.info("用户: " + user.getUid() + "在博客: " + webid + "发布评论, 内容为: " + content);
			boolean b = webCommentService.insert(new WebComment(null, webid, user.getUid(), 
					content, 0, SimpleUtils.getSimpleDateSecond(), 0));
			if(b) {
				log.info("用户发布评论成功");
				return Result.success().add("username", user.getUsername())
						.add("createtime", SimpleUtils.getSimpleDateSecond())
						.add("imgpath", myInageService.selectImagePathByUid(user.getUid()).getImagePath());
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
	
	/**
	 * 处理回复评论请求
	 * @param cid
	 * @param bid
	 * @param content
	 * @return
	 */
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
				return Result.success().add("username", user.getUsername())
						.add("createtime", SimpleUtils.getSimpleDateSecond())
						.add("imgpath", myInageService.selectImagePathByUid(user.getUid()).getImagePath());
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
	
	/**
	 * 获取作者信息
	 * @param uid
	 * @return
	 */
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
			log.info("获取作者头像信息");
			String imgpath = myInageService.selectImagePathByUid(uid).getImagePath();
			author.setImgpath(imgpath);
			log.info("获取作者粉丝数");
			Integer fans = (int) myLikeService.countByLikeId(uid);
			author.setFans(fans);
			log.info("获取作者文章被点赞数");
			Integer thumbs = webThubmsService.countThumbsByUid(uid);
			author.setThumbs(thumbs);
			log.info("获取作者文章总评论数");
			Integer comment = webCommentService.countByUid(uid);
			author.setComment(comment);
			log.info("获取作者文章总访问量");
			Integer visits = visitInformationService.selectVisitsByVid(uid);
			author.setVisits(visits);
			log.info("获取作者文章总收藏量");
			Integer collection = myConllectionService.countCollectionByUid(uid);
			author.setCollection(collection);
			log.info("获取作者原创数量");
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
	
	/**
	 * 处理点赞请求
	 */
	@RequestMapping(value = "/thumbs", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result thumbs(Integer webid, Integer bid) {
		UserInformation user = null;
		try {
			user = ShiroUtils.getUserInformation();
			log.info("用户: " + user.getUid() + "点赞文章: " + webid + "作者: " + bid);
			boolean b = webThubmsService.insert(new WebThumbs(user.getUid(), bid, webid));
			if(b) {
				log.info("用户: " + user.getUid() + "点赞文章: " + webid + " 成功");
				return Result.success();
			}
			log.warn("用户点赞文章失败, 连接数据库失败");
			return Result.fail().add(EX, "网络连接失败, 请稍候重试");
		} catch (NotFoundBlobIdException e) {
			log.info("用户点赞文章失败, 原因: " + e.getMessage());
			return Result.fail().add(EX, e.getMessage());
		}catch (Exception e) {
			if(user == null) {
				log.info("用户点赞文章失败, 原因: 未登录");
				return Result.fail().add(EX, "用户未登录");
			}
			log.error("用户点赞文章失败, 原因: " + e.getMessage());
			return Result.fail().add(EX, "未定义类型错误");
		}
	}
	
	/**
	 * 处理取消点赞请求
	 */
	@RequestMapping(value = "/dethumbs", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result dethumbs(Integer webid) {
		UserInformation user = null;
		try {
			user = ShiroUtils.getUserInformation();
			log.info("用户: " + user.getUid() + "取消点赞文章: " + webid);
			boolean b = webThubmsService.deletebyWebIdAndUid(webid, user.getUid());
			if(b) {
				log.info("用户: " + user.getUid() + "取消点赞文章: " + webid + " 成功");
				return Result.success();
			}
			log.warn("用户取消点赞文章失败, 连接数据库失败");
			return Result.fail().add(EX, "网络连接失败, 请稍候重试");
		} catch (Exception e) {
			if(user == null) {
				log.info("用户取消点赞文章失败, 原因: 未登录");
				return Result.fail().add(EX, "用户未登录");
			}
			log.error("用户取消点赞文章失败, 原因: " + e.getMessage());
			return Result.fail().add(EX, "未定义类型错误");
		}
	}
	
	/**
	 * 查询用户是否点赞
	 * @param webid
	 * @return
	 */
	@RequestMapping(value = "/thumbsstatus", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public Result thumbsStatus(Integer webid) {
		Integer uid = null;
		try {
			log.info("获取用户是否点赞此篇文章");
			uid = ShiroUtils.getUserInformation().getUid();
			boolean b = webThubmsService.countByWebIdAndUid(webid, uid);
			if(b) {
				log.info("用户已点赞该文章");
				return Result.success();
			}
			log.warn("连接数据库失败!");
		} catch (Exception e) {
			log.error("查询用户是否点赞文章失败, 原因: " + e.getMessage());
		}
		return Result.fail().add(EX, "未点赞");
	}
	
	static {
		File file = new File(FILEPATH);
		if(file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 处理博客页面图片
	 * @param img
	 * @return
	 */
	@PostMapping(value = "/blobimg")
	@ResponseBody
	@CrossOrigin
	public Result postBlobImg(MultipartFile img) {
		try {
			String name = HashUtils.getFileNameForHash(RandomUtils.getUUID()) + SUFFIX;
			log.info("用户博客页面上传图片, 图片名为: " + name);
			InputStream input = img.getInputStream();
			Thumbnails.of(input)
			.scale(1f)
			.outputQuality(0.8f)
			.outputFormat("jpg")
			.toFile(FILEPATH + name);;
			log.info("上传并压缩成功");
			return Result.success().add("img", name);
		} catch (IOException e) {
			log.info("用户博客页面上传图片失败, 原因为: " + e.getMessage());
			return Result.fail().add(EX, "上传图片失败");
		} catch (Exception e) {
			log.error("用户博客页面上传图片失败, 原因: " + e.getMessage());
			return Result.fail().add(EX, "未定义类型错误");
		}
	}
}
