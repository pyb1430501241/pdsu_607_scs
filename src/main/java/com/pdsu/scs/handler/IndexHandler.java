package com.pdsu.scs.handler;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pdsu.scs.bean.*;
import com.pdsu.scs.service.*;
import com.pdsu.scs.utils.SortUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 半梦
 * @create 2020-09-21 20:14
 * 首页相关，前端罢工，举步维艰
 */
@RestController
@CrossOrigin
public class IndexHandler extends ParentHandler {

    /**
     * 博客相关
     */
    private WebInformationService webInformationService;

    /**
     * 文件相关
     */
    private WebFileService webFileService;

    /**
     * 用户相关
     */
    private UserInformationService userInformationService;

    /**
     * 用户头像相关
     */
    private MyImageService myImageService;

    /**
     * 点赞相关
     */
    private WebThumbsService webThumbsService;

    /**
     * 访问量相关
     */
    private VisitInformationService visitInformationService;

    /**
     * 收藏相关
     */
    private MyCollectionService myCollectionService;

    /**
     * 文件下载相关
     */
    private FileDownloadService fileDownloadService;

    public static final Logger log = LoggerFactory.getLogger(IndexHandler.class);

    @GetMapping("/index")
    public Result index() throws Exception {
        PageHelper.startPage(1,6);
        log.info("开始获取首页信息");
        log.info("开始获取博客模块");
        List<WebInformation> webList = webInformationService.selectWebInformationOrderByTimetest();
        List<Integer> uids = new ArrayList<>();
        List<Integer> webids = new ArrayList<>();
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
        blobList.sort(SortUtils.getBlobComparator());
        PageInfo<BlobInformation> pageInfo = new PageInfo<>(blobList);
        log.info("获取博客模块成功");
        log.info("开始获取文件模块");
        PageHelper.startPage(1,6);
        List<WebFile> webFiles = webFileService.selectFilesOrderByTime();
        uids = new ArrayList<Integer>();
        List<Integer> fileids = new ArrayList<>();
        for(WebFile file : webFiles) {
            uids.add(file.getUid());
            fileids.add(file.getId());
        }
        log.info("获取作者信息");
        List<UserInformation> users = userInformationService.selectUsersByUids(uids);
        log.info("获取文件下载量");
        List<Integer> downloads = fileDownloadService.selectDownloadsByFileIds(fileids);
        List<FileInformation> files = new ArrayList<FileInformation>();
        for (int i = 0; i < webFiles.size(); i++) {
            FileInformation fileInformation = new FileInformation();
            fileInformation.setWebfile(webFiles.get(i));
            fileInformation.setDownloads(downloads.get(i));
            for (UserInformation user : users) {
                UserInformation u = user;
                u.setPassword(null);
                if(user.getUid().equals(webFiles.get(i).getUid())) {
                    fileInformation.setUser(user);
                    break;
                }
            }
            files.add(fileInformation);
        }
        PageInfo<FileInformation> fileList = new PageInfo<FileInformation>(files);
        log.info("获取文件模块成功");
        return Result.success().add("blobList", pageInfo).add("fileList", fileList);
    }


    /**
     * 预留： 广告相关
     * @return
     */
    @GetMapping()
    @CrossOrigin
    @ResponseBody
    public Result advertising() {
        return Result.success();
    }


    @Autowired
    public IndexHandler(WebInformationService webInformationService,
                        WebFileService webFileService,
                        UserInformationService userInformationService,
                        MyImageService myImageService,
                        WebThumbsService webThumbsService,
                        VisitInformationService visitInformationService,
                        MyCollectionService myCollectionService,
                        FileDownloadService fileDownloadService) {
        this.webInformationService = webInformationService;
        this.webFileService = webFileService;
        this.userInformationService = userInformationService;
        this.myImageService = myImageService;
        this.webThumbsService = webThumbsService;
        this.visitInformationService = visitInformationService;
        this.myCollectionService = myCollectionService;
        this.fileDownloadService = fileDownloadService;
    }
}
