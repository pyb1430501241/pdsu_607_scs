package com.pdsu.scs.handler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.pdsu.scs.bean.*;
import com.pdsu.scs.service.*;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pdsu.scs.exception.web.blob.NotFoundBlobIdException;
import com.pdsu.scs.exception.web.blob.RepetitionThumbsException;
import com.pdsu.scs.exception.web.blob.comment.NotFoundCommentIdException;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.exception.web.user.UidAndWebIdRepetitionException;
import com.pdsu.scs.shiro.WebSessionManager;
import com.pdsu.scs.utils.HashUtils;
import com.pdsu.scs.utils.RandomUtils;
import com.pdsu.scs.utils.ShiroUtils;
import com.pdsu.scs.utils.SimpleUtils;

import net.coobird.thumbnailator.Thumbnails;

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
	private MyImageService myImageService;
	
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
	 * 文件相关
	 */
	@Autowired
	private WebFileService webFileService;
	
	/**
	 * 关注相关
	 */
	@Autowired
	private MyLikeService myLikeService;
	
	/**
	 * 标签相关
	 */
	@Autowired
	private WebLabelService webLabelService;
	
	/**
	 * 标签文章对照
	 */
	@Autowired
	private WebLabelControlService webLabelControlService;
	
	/**
	 * 文件下载记录相关
	 */
	@Autowired
	private FileDownloadService fileDownloadService;

	@Autowired
	private UserBrowsingRecordService userBrowsingRecordService;
	
	/**
	 * 文章类型
	 */
	@Autowired
	private ContypeService contypeService;
	
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
	public Result getWebForIndex(@RequestParam(value = "p", defaultValue = "1") Integer p
			, @RequestParam(defaultValue = "0") Integer lid) {
		try {
			//获取按时间排序的投稿
			List<WebInformation> webList = new ArrayList<WebInformation>();
			switch (lid) {
				case 0:
					log.info("获取首页数据");
					PageHelper.startPage(p, 10);
					webList = webInformationService.selectWebInformationOrderByTimetest();
					break;
				case 1: case 2: case 3:
				case 4: case 5: case 6:
				case 7: case 8: case 9:
				case 10: case 11:
					log.info("按标签获取首页数据");
					List<Integer> wids = webLabelControlService.selectWebIdsByLid(lid);
					PageHelper.startPage(p, 10);
					webList = webInformationService.selectWebInformationsByIds(wids, true);
					break;
				default:
					log.warn("无此类标签");
					return Result.fail().add(EX, "无此类标签数据");
			}
			if(webList == null || webList.size() == 0) {
				log.info("首页没有数据");
				return Result.accepted();
			}
			List<Integer> uids = new ArrayList<Integer>();
			List<Integer> webids = new ArrayList<Integer>();
			for(WebInformation w : webList) {
				uids.add(w.getUid());
				webids.add(w.getId());
			}
			log.info("获取博客作者信息");
			List<UserInformation> userList = userInformationService.selectUsersByUids(uids);
			log.info("获取博客作者头像");
			List<MyImage> imgpaths = myImageService.selectImagePathByUids(uids);
			for(UserInformation user : userList) {
				UserInformation t  = user;
				t.setPassword(null);
				for(MyImage m : imgpaths) {
					if(user.getUid().equals(m.getUid())) {
						t.setImgpath(m.getImagePath());
					}
				}
			}
			log.info("获取首页文章点赞量");
			List<Integer> thumbsList = webThubmsService.selectThumbssForWebId(webids);
			log.info("获取首页文章访问量");
			List<Integer> visitList = visitInformationService.selectVisitsByWebIds(webids);
			log.info("获取首页文章收藏量");
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
			blobList.sort((o1, o2) -> {
				Integer hint1 = o1.getCollection()*50000 + o1.getThumbs()*30000 + o1.getVisit()*10000 
						- (int)SimpleUtils.getSimpleDateDifference(o1.getWeb().getSubTime(), SimpleUtils.getSimpleDateSecond());
					Integer hint2 = o2.getCollection()*50000 + o2.getThumbs()*30000 + o2.getVisit()*10000
					- (int)SimpleUtils.getSimpleDateDifference(o2.getWeb().getSubTime(), SimpleUtils.getSimpleDateSecond());
					if(hint1 > hint2) {
						return 1;
					}
					return -1;
			});
			PageInfo<BlobInformation> pageInfo = new PageInfo<BlobInformation>(blobList);
			return Result.success().add("blobList", pageInfo);
		}catch (Exception e) {
			log.error("获取首页数据失败, 原因为: " + e.getMessage());
			return Result.fail().add(EX, "未定义类型错误");
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
			web.setSubTime(SimpleUtils.getSimpleDateDifferenceFormat(web.getSubTime()));
			Integer uid = web.getUid();
			web.setWebDataString(new String(web.getWebData(),"utf-8"));
			web.setWebData(null);
			UserInformation user = ShiroUtils.getUserInformation();
			if(user == null) {
				user = new UserInformation(181360226);
			}
			log.info("添加访问信息");
			visitInformationService.insert(new VisitInformation(null, user.getUid(), uid, web.getId()));
			log.info("添加用户浏览记录");
			if(user.getUsername() != null) {
				userBrowsingRecordService.insert(new UserBrowsingRecord(user.getUid(), web.getId(), 1
					, SimpleUtils.getSimpleDateSecond()));
			}
			log.info("用户: " + user.getUid() + ", 访问了文章: " + web.getId() + ", 作者为: " + uid);
			log.info("获取网页访问量");
			Integer visits = visitInformationService.selectvisitByWebId(web.getId());
			log.info("获取文章点赞数");
			Integer thubms = webThubmsService.selectThumbsForWebId(web.getId());
			log.info("获取文章收藏量");
			Integer collections = myConllectionService.selectCollectionsByWebId(web.getId());
			log.info("获取文章评论");
			List<WebComment> commentList = webCommentService.selectCommentsByWebId(id);
			List<WebCommentReply> commentReplyList = webCommentReplyService.selectCommentReplysByWebId(id);
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
			List<MyImage> imageList = myImageService.selectImagePathByUids(uids);
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
				webc.setCreatetime(SimpleUtils.getSimpleDateDifferenceFormat(webc.getCreatetime()));
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
				webc.setCreatetime(SimpleUtils.getSimpleDateDifferenceFormat(webc.getCreatetime()));
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
			log.info("获取文章标签");
			List<Integer> labelids = webLabelControlService.selectLabelIdByWebId(id);
			List<WebLabel> webLabels = webLabelService.selectByLabelIds(labelids);
			return Result.success().add("web", web)
				   .add("visit", visits)
				   .add("thubms", thubms)
				   .add("collection", collections)
				   .add("commentList", commentList)
				   .add("labels", webLabels);
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
	 * 处理收藏请求
	 * 
	 * @param bid  作者的学号
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
	 * 取消收藏请求
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
	 * 获取收藏状态
	 * @param webid
	 * @return
	 */
	@GetMapping("/collectionstatuts")
	@CrossOrigin
	@ResponseBody
	public Result collectionStatus(Integer webid) {
		Integer uid = null;
		try {
			uid = ShiroUtils.getUserInformation().getUid();
			log.info("查询用户是否已收藏文章");
			boolean b = myConllectionService.countByUidAndWebId(uid, webid);
			log.info("查询成功");
			return Result.success().add("status", b);
		} catch (Exception e) {
			if(uid == null) {
				log.info("查询用户是否收藏文章失败, 原因: 用户未登录");
				return Result.fail().add(EX, "未登录");
			}
			log.error("查询用户是否收藏文章失败, 原因: " + e.getMessage());
			return Result.fail().add(EX, "未定义类型错误");
		}
	}
	
	/**
	 * 处理投稿请求
	 * @param web, labelList
	 * @return
	 */
	@RequestMapping(value = "/contribution", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result insert(WebInformation web, @RequestParam(required = false)List<Integer> labelList) {
		//获取当前登录用户的UID
		if(labelList != null) {
			if(labelList.size() > 3) {
				log.info("发布文章失败, 文章只可添加至多三个标签");
				return Result.fail().add(EX, "文章最多添加三个标签");
			}
		}
		Integer uid = null;
		try {
			uid = ShiroUtils.getUserInformation().getUid();
			log.info("用户: " + uid + "发布文章开始");
			if(web.getTitle().length() >= 30) {
				log.info("发布失败, 原因: 标题过长");
				return Result.fail().add(EX, "文章标题应为30字以内");
			}
			//设置作者UID
			web.setUid(uid);
			//把网页主体内容转化为byte字节
			web.setWebData(web.getWebDataString().getBytes("utf-8"));
			//设置文章投稿时间
			web.setSubTime(SimpleUtils.getSimpleDateSecond());
			//发布文章
			int flag = webInformationService.insert(web);
			if(flag != -1) {
				if(labelList != null) {
					boolean b = webLabelControlService.insert(web.getId(), labelList);
					if(!b) {
						log.info("插入文章标签失败");
					}
				}
				log.info("用户: " + uid + "发布文章成功, 文章标题为: " + web.getTitle());
				return Result.success().add("webid", web.getId());
			}
			log.info("用户: " + uid + "发布文章失败");
			return Result.fail().add(EX, "发布失败");
		} catch (UnsupportedEncodingException e) {
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
			if(!user.getUid().equals(webInformation.getUid())) {
				log.info("用户: " + user.getUid() + " 无权删除文章: " + webid);
				return Result.fail().add(EX, "你无权删除这篇文章");
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
	public Result update(WebInformation web, @RequestParam(required = false)List<Integer> labelList) {
		//获取当前登录用户的信息
		UserInformation user = null;
		if(labelList != null) {
			if(labelList.size() > 3) {
				log.info("发布文章失败, 文章只可添加至多三个标签");
				return Result.fail().add(EX, "文章最多添加三个标签");
			}
		}
		try {
			user = ShiroUtils.getUserInformation();
			log.info("用户: " + user.getUid() + ", 开始更新文章: " + web.getId());
			web.setWebData(web.getWebDataString().getBytes());
			boolean b = webInformationService.updateByWebId(web);
			if(b) {
				log.info("更新文章成功");
				if(labelList != null) {
					webLabelControlService.deleteByWebId(web.getId());
					webLabelControlService.insert(web.getId(), labelList);
				}
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
						.add("imgpath", myImageService.selectImagePathByUid(user.getUid()).getImagePath());
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
	public Result postCommentReply(Integer webid, Integer cid, Integer bid, String content) {
		UserInformation user = null;
		try {
			user = ShiroUtils.getUserInformation();
			log.info("用户: " + user.getUid() + " 回复评论: " + cid + "被回复人: " + bid + ", 内容为:" + content);
			boolean b = webCommentReplyService.insert(new WebCommentReply(webid, cid, user.getUid(), bid, content, 
						0, SimpleUtils.getSimpleDateSecond()));
			if(b) {
				log.info("用户回复评论成功");
				return Result.success().add("username", user.getUsername())
						.add("createtime", SimpleUtils.getSimpleDateSecond())
						.add("imgpath", myImageService.selectImagePathByUid(user.getUid()).getImagePath());
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
	public Result getAuthorByUid(HttpServletRequest request, Integer uid) {
		try {
			log.info("获取作者: " + uid + "信息开始");
			UserInformation user = userInformationService.selectByUid(uid);
			Author author = new Author();
			author.setUid(user.getUid());
			author.setUsername(user.getUsername());
			log.info("获取作者其余文章");
			List<WebInformation> webList = webInformationService.selectWebInformationsByUid(uid);
			List<WebInformation> webs = new ArrayList<WebInformation>();
			List<Integer> webids = new ArrayList<>();
			int i = 0;
			for (WebInformation webInformation : webList) {
				if(i <= 5) {
					webs.add(webInformation);
				}
				webids.add(webInformation.getId());
				i++;
			}
			log.info("获取作者头像信息");
			String imgpath = myImageService.selectImagePathByUid(uid).getImagePath();
			author.setImgpath(imgpath);
			log.info("获取作者粉丝数");
			Integer fans = (int) myLikeService.countByLikeId(uid);
			author.setFans(fans);
			log.info("获取作者文章被点赞数");
			Integer thumbs = webThubmsService.countThumbsByUid(uid);
			author.setThumbs(thumbs);
			log.info("获取作者文章总评论数");
			Integer comment = webCommentService.countByUid(uid);
			Integer commentReply = webCommentReplyService.countByWebsAndUid(webids);
			author.setComment(comment + commentReply);
			log.info("获取作者文章总访问量");
			Integer visits = visitInformationService.selectVisitsByVid(uid);
			author.setVisits(visits);
			log.info("获取作者文章总收藏量");
			Integer collection = myConllectionService.countCollectionByUid(uid);
			author.setCollection(collection);
			log.info("获取作者原创数量");
			Integer original = webInformationService.countOriginalByUidAndContype(uid, 1);
			author.setOriginal(original);
			log.info("获取作者总关注数量");
			Integer attention = (int) myLikeService.countByUid(uid);
			author.setAttention(attention);
			log.info("获取作者文件总数量");
			Integer files = webFileService.countByUid(uid);
			author.setFiles(files);
			log.info("获取作者文件被下载量");
			Integer downloads = fileDownloadService.countByBid(uid);
			author.setDownloads(downloads);
			log.info("获取作者信息成功");
			Result result = Result.success().add("author", author).add("webList", webs);
			if(!StringUtils.isEmpty(WebUtils.toHttp(request).getHeader(WebSessionManager.AUTHORIZATION))) {
				boolean b = true;
				if(!ShiroUtils.getUserInformation().getUid().equals(uid)) {
					b = myLikeService.countByUidAndLikeId(ShiroUtils.getUserInformation().getUid(), uid);
				}
				result.add("islike", b);
			}
			return result;
		} catch (NotFoundUidException e) {
			log.info("获取作者信息失败, 原因: " + e.getMessage());
			return Result.fail().add(EX, e.getMessage());
		} catch (Exception e) {
			log.error("获取作者信息发生未知错误, 原因: " + e.getMessage());
			return Result.fail().add(EX, "未定义类型错误");
		}
	}
	
	/**
	 * 获取用户收藏的文章
	 * @param uid
	 * @return
	 */
	@GetMapping("/getcollection")
	@ResponseBody
	@CrossOrigin
	public Result getCollectionByUid(Integer uid, @RequestParam(value = "p", defaultValue = "1")Integer p) {
		try {
			log.info("获取用户: " + uid + " 收藏的文章");
			PageHelper.startPage(p, 10);
			List<MyCollection> collections = myConllectionService.selectWebIdsByUid(uid);
			List<Integer> webids = new ArrayList<>();
			List<Integer> uids = new ArrayList<>();
			for (MyCollection collection : collections) {
				webids.add(collection.getWid());
				uids.add(collection.getBid());
			}
			log.info("获取文章信息");
			List<WebInformation> webs = webInformationService.selectWebInformationsByIds(webids, false);
			log.info("获取文章访问量");
			List<Integer> visits = visitInformationService.selectVisitsByWebIds(webids);
			log.info("获取文章收藏量");
			List<Integer> collection = myConllectionService.selectCollectionssByWebIds(webids);
			log.info("获取作者信息");
			List<UserInformation> users = userInformationService.selectUsersByUids(uids);
			log.info("获取点赞量");
			List<Integer> thumbs = webThubmsService.selectThumbssForWebId(webids);
			List<BlobInformation> blobInformations = new ArrayList<BlobInformation>();
			for (Integer i = 0; i < webs.size(); i++) {
				BlobInformation blob = new BlobInformation();
				blob.setWeb(webs.get(i));
				blob.setVisit(visits.get(i));
				blob.setThumbs(thumbs.get(i));
				blob.setCollection(collection.get(i));
				for(UserInformation user : users) {
					if(webs.get(i).getUid().equals(user.getUid())) {
						blob.setUser(user);
						break;
					}
				}
				blobInformations.add(blob);
			}
			PageInfo<BlobInformation> bloList = new PageInfo<BlobInformation>(blobInformations);
			log.info("获取成功");
			return Result.success().add("blobList", bloList);
		} catch (Exception e) {
			log.info("获取失败, 原因: " + e.getMessage());
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
		} catch (RepetitionThumbsException e) {
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
		} catch (RepetitionThumbsException e) {
			log.info("用户取消点赞失败, 原因: " + e.getMessage());
			return Result.fail().add(EX, e.getMessage());
		}catch (Exception e) {
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
			.toFile(FILEPATH + name);
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
	
	/**
	 * 获取文章标签
	 * @return
	 */
	@ResponseBody
	@CrossOrigin
	@GetMapping("/getlabel")
	public Result getLabel() {
		try {
			log.info("获取所有标签");
			List<WebLabel> label = webLabelService.selectLabel();
			log.info("获取标签成功");
			return Result.success().add("labelList", label);
		} catch (Exception e) {
			log.error("获取标签失败, 原因: " + e.getMessage());
			return Result.fail().add(EX, "未定义类型错误");
		}
	}
	
	/**
	 * 获取文章类型
	 * @return
	 */
	@ResponseBody
	@GetMapping("/getcontype")
	@CrossOrigin
	public Result getContype() {
		try {
			log.info("获取文章类型列表");
			List<Contype> contypes = contypeService.selectContypes();
			log.info("获取文章类型列表成功");
			return Result.success().add("contypeList", contypes);
		} catch (Exception e) {
			log.error("获取文章类型列表失败, 原因: " + e.getMessage());
			return Result.fail().add(EX, "未定义类型错误");
		}
	}
}
