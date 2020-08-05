package com.pdsu.scs.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.pdsu.scs.bean.FileDownload;
import com.pdsu.scs.bean.Result;
import com.pdsu.scs.bean.WebFile;
import com.pdsu.scs.exception.web.es.InsertException;
import com.pdsu.scs.exception.web.file.UidAndTItleRepetitionException;
import com.pdsu.scs.service.FileDownloadService;
import com.pdsu.scs.service.WebFileService;
import com.pdsu.scs.utils.HashUtils;
import com.pdsu.scs.utils.ShiroUtils;
import com.pdsu.scs.utils.SimpleUtils;

/**
 *  文件相关API
 * @author 半梦
 *
 */
@Controller
@RequestMapping("/file")
public class FileHandler {
	
	/**
	 * 文件上传地址
	 */
	private static final String FILEPATH = "/pdsu/web/file/";
	
	private static final String EX = "exception";
	
	static {
		File file = new File(FILEPATH);
		if(file.exists()) {
			file.mkdirs();
		}
	}
	
	/**
	 * 文件操作相关
	 */
	@Autowired
	private WebFileService webFileService;
	
	@Autowired
	private FileDownloadService fileDownloadService;
	
	/**
	 * 日志
	 */
	private static final Logger log = LoggerFactory.getLogger(FileHandler.class);
	
	@RequestMapping("/index")
	public String index() {
		return "file/index";
	}
	
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
	public Result sendcomment(@RequestParam("file")MultipartFile file, String title, String description) {
		Integer uid = ShiroUtils.getUserInformation().getUid();
		log.info("用户: " + uid + " 上传文件: " + title + " 开始" + ", 描述为:" + description);
		try {
			byte [] s = file.getBytes();
			String name = HashUtils.getFileNameForHash(title) + SimpleUtils.getSuffixName(file.getOriginalFilename());
			log.info("文件名为: " + name);
			FileUtils.writeByteArrayToFile(new File(FILEPATH + name), s);
			log.info("文件写入成功, 开始在服务器保存地址");
			boolean b = webFileService.insert(new WebFile(uid, title, description, name, SimpleUtils.getSimpleDateSecond()));
			if(b) {
				log.info("上传成功");
				return Result.success();
			} else {
				log.error("上传失败");
				return Result.fail().add(EX, "网络异常, 请稍候重试");
			}
		} catch (UidAndTItleRepetitionException e) {
			log.info("用户: " + uid + ", 上传资源: " + title + " 失败, 原因为: " + e.getMessage());
			return Result.fail().add(EX, "无法上传同名文件, 请修改名称");
		} catch (InsertException e) {
			log.error("上传失败, 原因为: " + e.getMessage());
			return Result.fail().add(EX, "网络异常, 请稍候重试");
		}catch (Exception e) {
			log.error("上传失败, 原因为: " + e.getMessage());
			return Result.fail().add(EX, "未定义类型错误");
		}
	}
	
	/**
	 * 下载文件
	 * @param uid 
	 * @param title
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	@CrossOrigin
	public void getDownload(Integer uid, String title, HttpServletResponse response) {
		OutputStream out = null;
		InputStream in = null;
		try {
			log.info("开始下载文件, 下载人 UID 为: " + ShiroUtils.getUserInformation().getUid());
			log.info("查询文件是否存在");
			WebFile webfile = webFileService.selectFileByUidAndTitle(uid, title);
			String name = webfile.getFilePath();
			String url = FILEPATH + name;
			in = new FileInputStream(url);
			response.setContentType("multipart/form-data");
			String filename = title + SimpleUtils.getSuffixName(name);
			response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
			out = response.getOutputStream(); 
			out.write(in.readAllBytes()); 
			out.flush();
			fileDownloadService.insert(new FileDownload(webfile.getId(), uid, ShiroUtils.getUserInformation().getUid()));
			log.info("下载成功");
		}
		catch (Exception e) {
			log.error("下载遇到未知错误: " + e.getMessage());
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
					log.warn("输出流关闭失败");
				}
			}
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.warn("输入流关闭失败");
				}
			}
		}
	}
	
	@ResponseBody
	@GetMapping("/getfileindex")
	@CrossOrigin
	public Result getFileIndex(@RequestParam(defaultValue = "1") Integer p) {
		try {
			log.info("获取首页文件");
			PageHelper.startPage(p, 15);
			webFileService.selectFilesOrderByTime();
			return Result.success();
		} catch (Exception e) {
			return Result.fail();
		}
	}
}
