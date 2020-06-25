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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pdsu.scs.bean.Result;
import com.pdsu.scs.bean.WebFile;
import com.pdsu.scs.exception.web.file.FileException;
import com.pdsu.scs.service.WebFileService;
import com.pdsu.scs.utils.HashUtils;
import com.pdsu.scs.utils.ShiroUtils;
import com.pdsu.scs.utils.SimpleUtils;

/**
 *  文件相关API
 *  只在数据库中储存相应的文件存储地址, 文件的储存由IO流下载到本地
 * @author 半梦
 *
 */
@Controller
@RequestMapping("/file")
public class FileHandler {
	
	/**
	 * 文件上传地址
	 */
	private static final String path = "pdsu/web/file/";
	
	private static final String EX = "exception";
	
	static {
		File file = new File(path);
		if(file.exists()) {
			file.mkdirs();
		}
	}
	
	/**
	 * 文件操作相关
	 */
	@Autowired
	private WebFileService webFileService;
	
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
	@RequestMapping("/upload")
	@ResponseBody
	@CrossOrigin
	public Result sendcomment(@RequestParam("file")MultipartFile file, String title, String description) {
		Integer uid = ShiroUtils.getUserInformation().getUid();
		log.info("用户: " + uid + " 上传资源: " + title + " 开始" + ", 描述为:" + description);
		try {
			byte [] s = file.getBytes();
			String name = HashUtils.getFileNameForHash(title) + SimpleUtils.getSuffixName(file.getOriginalFilename());
			log.info("文件名为: " + name);
			FileUtils.writeByteArrayToFile(new File(path + name), s);
			log.info("文件写入成功, 开始在服务器保存地址");
			boolean b = webFileService.insert(new WebFile(uid, title, name, SimpleUtils.getSimpleDateSecond()));
			if(b) {
				log.info("上传成功");
				return Result.success();
			}else {
				log.error("上传失败");
				return Result.fail();
			}
		}catch (Exception e) {
			log.error("上传失败, 原因为: " + e.getMessage());
			return Result.fail();
		}
	}
	
	@ResponseBody
	@RequestMapping("/download")
	@CrossOrigin
	public void getDownload(Integer uid, String title, HttpServletResponse response) {
		log.info("开始下载文件, 下载人 UID 为: " + ShiroUtils.getUserInformation().getUid());
		OutputStream out = null;
		InputStream in = null;
		try {
			log.info("查询文件是否存在");
			WebFile webfile = webFileService.selectFileByUidAndTitle(uid, title);
			String name = webfile.getFilePath();
			String url = path + name;
			in = new FileInputStream(url);
			response.setContentType("multipart/form-data");
			String filename = title + SimpleUtils.getSuffixName(name);
			response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
			out = response.getOutputStream(); 
			out.write(in.readAllBytes()); 
			out.flush();
			log.info("下载成功");
		}
		catch (Exception e) {
			log.error("下载遇到未知错误: " + e.getMessage());
		}finally {
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
					log.error("输出流关闭失败");
				}
			}
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error("输入流关闭失败");
				}
			}
		}
	}
}
