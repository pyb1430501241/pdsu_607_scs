package com.pdsu.scs.handler;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pdsu.scs.bean.*;
import com.pdsu.scs.service.FileDownloadService;
import com.pdsu.scs.service.UserInformationService;
import com.pdsu.scs.service.WebFileService;
import com.pdsu.scs.utils.HashUtils;
import com.pdsu.scs.utils.ShiroUtils;
import com.pdsu.scs.utils.SimpleUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *  文件相关API
 * @author 半梦
 *
 */
@Controller
@RequestMapping("/file")
public class FileHandler extends ParentHandler{
	
	/**
	 * 文件操作相关
	 */
	@Autowired
	private WebFileService webFileService;
	
	@Autowired
	private FileDownloadService fileDownloadService;
	
	@Autowired
	private UserInformationService userInformationService;
	
	/**
	 * 日志
	 */
	private static final Logger log = LoggerFactory.getLogger(FileHandler.class);
	
	/**
	 * 
	 * @param file  上传的文件
	 * @param title  文件名称
	 * @param description  文件描述
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result upload(@RequestParam("file")MultipartFile file, @RequestParam String title,
						 @RequestParam String description) throws Exception{
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		Integer uid = user.getUid();
		log.info("用户: " + uid + " 上传文件: " + title + " 开始" + ", 描述为:" + description);
		byte [] s = file.getBytes();
		String name = HashUtils.getFileNameForHash(title) + SimpleUtils.getSuffixName(file.getOriginalFilename());
		log.info("文件名为: " + name);
		FileUtils.writeByteArrayToFile(new File(File_FilePath + name), s);
		log.info("文件写入成功, 开始在服务器保存地址");
		boolean b = webFileService.insert(new WebFile(uid, title, description, name, SimpleUtils.getSimpleDateSecond()));
		if(b) {
			log.info("上传成功");
			return Result.success();
		}
		log.error("上传失败");
		return Result.fail().add(EXCEPTION, NETWORK_BUSY);
	}
	
	/**
	 * 下载文件
	 * @param uid 作者 uid
	 * @param title 文件名
	 * @param response HttpServletResponse
	 */
	@ResponseBody
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	@CrossOrigin
	public void download(@RequestParam Integer uid, @RequestParam String title, HttpServletResponse response) throws Exception{
		OutputStream out = null;
		InputStream in = null;
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		try {
			log.info("开始下载文件, 下载人 UID 为: " + user.getUid());
			log.info("查询文件是否存在");
			WebFile webfile = webFileService.selectFileByUidAndTitle(uid, title);
			String name = webfile.getFilePath();
			String url = File_FilePath + name;
			in = new FileInputStream(url);
			response.setContentType("multipart/form-data");
			String filename = title + "_" + uid + SimpleUtils.getSuffixName(name);
			response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
			out = response.getOutputStream(); 
			out.write(in.readAllBytes()); 
			out.flush();
			fileDownloadService.insert(new FileDownload(webfile.getId(), uid, ShiroUtils.getUserInformation().getUid()));
			log.info("下载成功");
		} finally {
			if(!Objects.isNull(out)) {
				try {
					out.close();
				} catch (IOException e) {
					log.warn("输出流关闭失败");
				}
			}
			if(!Objects.isNull(in)) {
				try {
					in.close();
				} catch (IOException e) {
					log.warn("输入流关闭失败");
				}
			}
		}
	}
	
	/**
	 * 
	 * @param p
	 * @return
	 */
	@ResponseBody
	@GetMapping("/getfileindex")
	@CrossOrigin
	public Result getFileIndex(@RequestParam(defaultValue = "1") Integer p) throws Exception{
		log.info("获取首页文件");
		PageHelper.startPage(p, 15);
		List<WebFile> list = webFileService.selectFilesOrderByTime();
		List<Integer> uids = new ArrayList<Integer>();
		List<Integer> fileids = new ArrayList<Integer>();
		for(WebFile file : list) {
			uids.add(file.getUid());
			fileids.add(file.getId());
		}
		log.info("获取作者信息");
		List<UserInformation> users = userInformationService.selectUsersByUids(uids);
		log.info("获取文件下载量");
		List<Integer> downloads = fileDownloadService.selectDownloadsByFileIds(fileids);
		List<FileInformation> files = new ArrayList<FileInformation>();
		for (int i = 0; i < list.size(); i++) {
			FileInformation fileInformation = new FileInformation();
			fileInformation.setWebfile(list.get(i));
			fileInformation.setDownloads(downloads.get(i));
			for (UserInformation user : users) {
				UserInformation u = user;
				user.setPassword(null);
				if(user.getUid().equals(list.get(i).getUid())) {
					fileInformation.setUser(user);
					break;
				}
			}
			files.add(fileInformation);
		}
		PageInfo<FileInformation> fileList = new PageInfo<FileInformation>(files);
		return Result.success().add("fileList", fileList);

	}

}
