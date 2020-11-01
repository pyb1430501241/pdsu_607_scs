package com.pdsu.scs.handler;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pdsu.scs.bean.*;
import com.pdsu.scs.exception.web.es.InsertException;
import com.pdsu.scs.service.*;
import com.pdsu.scs.shiro.WebSessionManager;
import com.pdsu.scs.utils.*;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 
 * @author 半梦
 * @create 2020-04-26 11:26
 *
 */
@Controller
@RequestMapping("/blob")
@SuppressWarnings("unchecked")
public class BlobHandler extends ParentHandler {

	/**
	 * 博客相关逻辑处理
	 */
	private WebInformationService webInformationService;

	/**
	 * 用户相关逻辑处理
	 */
	private UserInformationService userInformationService;
	
	/**
	 * 点赞相关逻辑处理
	 */
	private WebThumbsService webThumbsService;
	
	/**
	 * 头像相关逻辑处理
	 */
	private MyImageService myImageService;
	
	/**
	 * 访问相关逻辑处理
	 */
	private VisitInformationService visitInformationService;
	
	/**
	 * 收藏相关逻辑处理
	 */
	private MyCollectionService myCollectionService;
	
	/**
	 * 评论相关
	 */
	private WebCommentService webCommentService;
	
	/**
	 * 回复评论相关
	 */
	private WebCommentReplyService webCommentReplyService;
	
	/**
	 * 文件相关
	 */
	private WebFileService webFileService;
	
	/**
	 * 关注相关
	 */
	private MyLikeService myLikeService;
	
	/**
	 * 标签相关
	 */
	private WebLabelService webLabelService;
	
	/**
	 * 标签文章对照
	 */
	private WebLabelControlService webLabelControlService;
	
	/**
	 * 文件下载记录相关
	 */
	private FileDownloadService fileDownloadService;

	/**
	 * 浏览记录
	 */
	private UserBrowsingRecordService userBrowsingRecordService;
	
	/**
	 * 文章类型
	 */
	private ContypeService contypeService;

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
	public Result getWebIndex(@RequestParam(value = "p", defaultValue = "1") Integer p
			, @RequestParam(defaultValue = "0") Integer lid) throws Exception {
		List<WebInformation> webList;
		if (lid.equals(0)) {
			log.info("获取首页数据");
			PageHelper.startPage(p, 10);
			webList = webInformationService.selectWebInformationOrderByTimetest();
		} else {
			log.info("按标签获取首页数据");
			List<Integer> wids = webLabelControlService.selectWebIdsByLid(lid);
			PageHelper.startPage(p, 10);
			webList = webInformationService.selectWebInformationsByIds(wids, true);
		}
		if(webList == null || webList.size() == 0) {
			log.info("首页没有数据");
			return Result.accepted().add(EXCEPTION, "暂无此类标签的文章");
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
		List<Integer> thumbsList = webThumbsService.selectThumbssForWebId(webids);
		log.info("获取首页文章访问量");
		List<Integer> visitList = visitInformationService.selectVisitsByWebIds(webids);
		log.info("获取首页文章收藏量");
		List<Integer> collectionList = myCollectionService.selectCollectionssByWebIds(webids);
		List<BlobInformation> blobList = new ArrayList<>();
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
		blobList.sort(SortUtils.getBlobComparator());
		PageInfo<WebInformation> page = new PageInfo<>(webList);
		return Result.success().add("blobList", blobList).add(HAS_NEXT_PAGE, page.isHasNextPage());
	}
	
	/**
	 * 获取博客的相关信息
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{webid}", method = RequestMethod.GET)
	@CrossOrigin
	public Result toBlob(@PathVariable("webid")Integer id) throws Exception {
		log.info("开始获取博客页面信息");
		WebInformation web = webInformationService.selectById(id);
		Integer uid = web.getUid();
		if(!Objects.isNull(web.getWebData())) {
			web.setWebDataString(new String(web.getWebData(),"UTF-8"));
			web.setWebData(null);
		}
		UserInformation user = ShiroUtils.getUserInformation();
		// 如果未登录, 默认访问人
		if(Objects.isNull(user)) {
			user = new UserInformation(181360226);
		}
		log.info("添加访问信息");
		visitInformationService.insert(new VisitInformation(null, user.getUid(), uid, web.getId()));
		log.info("添加用户浏览记录");
		if(!Objects.isNull(user.getUsername())) {
			userBrowsingRecordService.insert(new UserBrowsingRecord(user.getUid(), web.getId(), 1
				, SimpleUtils.getSimpleDateSecond()));
		}
		log.info("用户: " + user.getUid() + ", 访问了文章: " + web.getId() + ", 作者为: " + uid);
		log.info("获取网页访问量");
		Integer visits = visitInformationService.selectvisitByWebId(web.getId());
		log.info("获取文章点赞数");
		Integer thubms = webThumbsService.selectThumbsForWebId(web.getId());
		log.info("获取文章收藏量");
		Integer collections = myCollectionService.selectCollectionsByWebId(web.getId());
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
			List<WebCommentReply> webCommentReplyList = new ArrayList<>();
			for(WebCommentReply reply : commentReplyList) {
				if(reply.getCid().equals(b.getId())) {
					webCommentReplyList.add(reply);
				}
			}
			b.setCommentReplyList(webCommentReplyList);
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
	}
	
	/**
	 * 处理收藏请求
	 * @param bid  作者的学号
	 * @param webid 网页id
	 * @return
	 */
	@RequestMapping(value = "/collection", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result collection(@RequestParam Integer bid, @RequestParam Integer webid) throws Exception {
		UserInformation user =ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		log.info("用户: " + user.getUid() + ", 收藏博客: " + webid + ", 作者为: " + bid + ", 开始");
		boolean flag = myCollectionService.insert(new MyCollection(null, user.getUid(), webid, bid));
		if(flag) {
			log.info("用户: " + user.getUid() + ", 收藏 " + webid + " 成功");
			return Result.success();
		}
		log.warn("收藏失败, 连接数据库失败");
		return Result.fail().add(EXCEPTION, NETWORK_BUSY);
	}

	/**
	 * 取消收藏请求
	 * @param webid  网页ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/decollection", method = RequestMethod.POST)
	@CrossOrigin
	public Result deCollection(@RequestParam Integer webid) throws Exception{
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		log.info("用户: " + user.getUid() + ", 取消收藏博客: " + webid + "开始");
		if(myCollectionService.delete(user.getUid(), webid)) {
			log.info("取消收藏成功");
			return Result.success();
		}else {
			log.warn("取消收藏失败, 原因: 连接服务器失败");
			return Result.fail().add(EXCEPTION, NETWORK_BUSY);
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
	public Result collectionStatus(@RequestParam Integer webid) throws Exception {
		System.out.println(webid);
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		log.info("查询用户是否已收藏文章");
		boolean b = myCollectionService.countByUidAndWebId(user.getUid(), webid);
		log.info("查询成功");
		return Result.success().add("collectionStatus", b);
	}

	/**
	 * 处理投稿请求
	 * @param web, labelList
	 * @return
	 */
	@RequestMapping(value = "/contribution", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	@Transactional(rollbackFor = InsertException.class)
	public Result insert(@Valid WebInformation web, @RequestParam(required = false)List<Integer> labelList)
			throws Exception {
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		log.info("用户: " + user.getUid() + "发布文章开始");
		if(!Objects.isNull(labelList)) {
			if(labelList.size() > 5) {
				log.info("发布文章失败, 文章只可添加至多五个标签");
				return Result.fail().add(EXCEPTION, "文章最多添加五个标签");
			}
		}
		//设置作者UID
		web.setUid(user.getUid());
		//把网页主体内容转化为byte字节
		web.setWebData(web.getWebDataString().getBytes("UTF-8"));
		//设置文章投稿时间
		web.setSubTime(SimpleUtils.getSimpleDateSecond());
		//发布文章
		int flag = webInformationService.insert(web);
		if(flag > 0) {
			if(!Objects.isNull(labelList)) {
				log.info("开始插入文章标签");
				webLabelControlService.insert(web.getId(), labelList);
			}
			log.info("用户: " + user.getUid() + "发布文章成功, 文章标题为: " + web.getTitle());
			return Result.success().add("webid", web.getId());
		}
		log.warn("用户: " + user.getUid() + "发布文章失败, 原因: 连接数据库失败");
		return Result.fail().add(EXCEPTION, NETWORK_BUSY);
	}
	
	/**
	 * 删除文章
	 * @param webid  文章id
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result delete(@RequestParam Integer webid) throws Exception {
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		log.info("开始删除文章, 文章ID为: " + webid + " 文章作者为: " + user.getUid());
		WebInformation webInformation = webInformationService.selectById(webid);
		if(!user.getUid().equals(webInformation.getUid())) {
			log.info("用户: " + user.getUid() + " 无权删除文章: " + webid);
			return Result.fail().add(EXCEPTION, INSUFFICIENT_PERMISSION);
		}
		boolean b = webInformationService.deleteById(webid);
		if(b) {
			log.info("删除文章成功, 文章ID为: " + webid);
			return Result.success();
		}
		log.warn("删除文章失败, 连接数据库失败");
		return Result.fail().add(EXCEPTION, NETWORK_BUSY);
	}
	
	/**
	 * 更新文章
	 * @param web  更新后的文章
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result update(@Valid WebInformation web, @RequestParam(required = false)List<Integer> labelList) throws Exception {
		//获取当前登录用户的信息
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		log.info("用户: " + user.getUid() + ", 开始更新文章: " + web.getId());
		if(!Objects.isNull(labelList)) {
			if(labelList.size() > 3) {
				log.info("发布文章失败, 文章只可添加至多三个标签");
				return Result.fail().add(EXCEPTION, "文章最多添加三个标签");
			}
		}
		web.setWebData(web.getWebDataString().getBytes());
		boolean b = webInformationService.updateByWebId(web);
		if(b) {
			log.info("更新文章成功");
			if (!Objects.isNull(labelList)) {
				log.info("开始插入文章标签");
				webLabelControlService.deleteByWebId(web.getId());
				webLabelControlService.insert(web.getId(), labelList);
			}
			return Result.success();
		}
		log.info("更新文章失败");
		return Result.fail().add(EXCEPTION, NETWORK_BUSY);
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
	public Result postComment(@RequestParam Integer webid, @RequestParam String content) throws Exception{
		UserInformation user =ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
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
		return Result.fail().add(EXCEPTION, NETWORK_BUSY);
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
	public Result postCommentReply(@RequestParam Integer webid,
								   @RequestParam Integer cid,
								   @RequestParam Integer bid,
								   @RequestParam String content) throws Exception {
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
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
		return Result.fail().add(EXCEPTION, NETWORK_BUSY);
	}
	
	/**
	 * 获取作者信息
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = "/getauthor", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public Result getAuthorByUid(HttpServletRequest request, @RequestParam Integer uid) throws Exception{
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
		Integer thumbs = webThumbsService.countThumbsByUid(uid);
		author.setThumbs(thumbs);
		log.info("获取作者文章总评论数");
		Integer comment = webCommentService.countByUid(uid);
		Integer commentReply = webCommentReplyService.countByWebsAndUid(webids);
		author.setComment(comment + commentReply);
		log.info("获取作者文章总访问量");
		Integer visits = visitInformationService.selectVisitsByVid(uid);
		author.setVisits(visits);
		log.info("获取作者文章总收藏量");
		Integer collection = myCollectionService.countCollectionByUid(uid);
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
	}
	
	/**
	 * 获取用户收藏的文章
	 * @param uid
	 * @return
	 */
	@GetMapping("/getcollection")
	@ResponseBody
	@CrossOrigin
	public Result getCollectionByUid(@RequestParam Integer uid, @RequestParam(value = "p", defaultValue = "1")Integer p)
			throws Exception {
		log.info("获取用户: " + uid + " 收藏的文章");
		PageHelper.startPage(p, 10);
		List<MyCollection> collections = myCollectionService.selectWebIdsByUid(uid);
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
		List<Integer> collection = myCollectionService.selectCollectionssByWebIds(webids);
		log.info("获取作者信息");
		List<UserInformation> users = userInformationService.selectUsersByUids(uids);
		log.info("获取点赞量");
		List<Integer> thumbs = webThumbsService.selectThumbssForWebId(webids);
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
		PageInfo<MyCollection> bloList = new PageInfo<>(collections);
		log.info("获取成功");
		return Result.success().add("blobList", blobInformations).add(HAS_NEXT_PAGE, bloList.isHasNextPage());
	}
	
	/**
	 * 处理点赞请求
	 */
	@RequestMapping(value = "/thumbs", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result thumbs(@RequestParam Integer webid, @RequestParam Integer bid) throws Exception{
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		log.info("用户: " + user.getUid() + "点赞文章: " + webid + "作者: " + bid);
		boolean b = webThumbsService.insert(new WebThumbs(user.getUid(), bid, webid));
		if(b) {
			log.info("用户: " + user.getUid() + "点赞文章: " + webid + " 成功");
			return Result.success();
		}
		log.warn("用户点赞文章失败, 连接数据库失败");
		return Result.fail().add(EXCEPTION, NETWORK_BUSY);
	}
	
	/**
	 * 处理取消点赞请求
	 */
	@RequestMapping(value = "/dethumbs", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result dethumbs(@RequestParam Integer webid) throws Exception{
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		log.info("用户: " + user.getUid() + "取消点赞文章: " + webid);
		boolean b = webThumbsService.deletebyWebIdAndUid(webid, user.getUid());
		if(b) {
			log.info("用户: " + user.getUid() + "取消点赞文章: " + webid + " 成功");
			return Result.success();
		}
		log.warn("用户取消点赞文章失败, 连接数据库失败");
		return Result.fail().add(EXCEPTION, NETWORK_BUSY);
	}
	
	/**
	 * 查询用户是否点赞
	 * @param webid
	 * @return
	 */
	@RequestMapping(value = "/thumbsstatus", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public Result thumbsStatus(@RequestParam Integer webid) throws Exception {
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		log.info("获取用户是否点赞此篇文章");
		boolean b = webThumbsService.countByWebIdAndUid(webid, user.getUid());
		return Result.success().add("thumbsStatus", b);
	}
	
	/**
	 * 处理博客页面图片
	 * @param img
	 * @return
	 */
	@PostMapping(value = "/blobimg")
	@ResponseBody
	@CrossOrigin
	public Result postBlobImg(@RequestParam MultipartFile img) throws Exception {
		String name = HashUtils.getFileNameForHash(RandomUtils.getUUID()) + Img_Suffix;
		log.info("用户博客页面上传图片, 图片名为: " + name);
		InputStream input = img.getInputStream();
		Thumbnails.of(input)
		.scale(1f)
		.outputQuality(0.8f)
		.outputFormat(Img_Suffix_Except_Point)
		.toFile(Blob_Img_FilePath + name);
		log.info("上传并压缩成功");
		return Result.success().add("img", name);
	}
	
	/**
	 * 获取文章标签
	 * @return
	 */
	@ResponseBody
	@CrossOrigin
	@GetMapping("/getlabel")
	public Result getLabel(@RequestParam(defaultValue = "1") Integer p) throws Exception{
		log.info("获取所有标签");
		PageHelper.startPage(p, 16);
		List<WebLabel> label = webLabelService.selectLabel();
		PageInfo<WebLabel> labs = new PageInfo<>(label);
		log.info("获取标签成功");
		return Result.success().add("labelList", labs);
	}
	
	/**
	 * 获取文章类型
	 * @return
	 */
	@ResponseBody
	@GetMapping("/getcontype")
	@CrossOrigin
	public Result getContype() throws Exception{
		log.info("获取文章类型列表");
		List<Contype> contypes = contypeService.selectContypes();
		log.info("获取文章类型列表成功");
		return Result.success().add("contypeList", contypes);
	}


	@Autowired
	public BlobHandler(WebInformationService webInformationService,
					   UserInformationService userInformationService,
					   WebThumbsService webThumbsService,
					   MyImageService myImageService,
					   VisitInformationService visitInformationService,
					   MyCollectionService myCollectionService,
					   WebCommentService webCommentService,
					   WebCommentReplyService webCommentReplyService,
					   WebFileService webFileService,
					   MyLikeService myLikeService,
					   WebLabelService webLabelService,
					   WebLabelControlService webLabelControlService,
					   FileDownloadService fileDownloadService,
					   ContypeService contypeService,
					   UserBrowsingRecordService userBrowsingRecordService) {
		this.webInformationService = webInformationService;
		this.userInformationService = userInformationService;
		this.webThumbsService = webThumbsService;
		this.myImageService = myImageService;
		this.visitInformationService = visitInformationService;
		this.myCollectionService = myCollectionService;
		this.webCommentService = webCommentService;
		this.webCommentReplyService = webCommentReplyService;
		this.webFileService = webFileService;
		this.myLikeService = myLikeService;
		this.webLabelService = webLabelService;
		this.webLabelControlService = webLabelControlService;
		this.fileDownloadService = fileDownloadService;
		this.contypeService = contypeService;
		this.userBrowsingRecordService = userBrowsingRecordService;
	}
}
