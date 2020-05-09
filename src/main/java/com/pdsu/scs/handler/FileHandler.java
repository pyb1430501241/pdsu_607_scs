package com.pdsu.scs.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
import com.pdsu.scs.service.WebFileService;
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
		log.info("用户: " + uid + "上传资源: " + title + " 开始" + ", 描述为:" + description);
		try {
			System.out.println(file);
			byte [] s = file.getBytes();
			String name = UUID.randomUUID().toString() + SimpleUtils.getSuffixName(file.getOriginalFilename());
			log.info("文件名为: " + name);
			FileUtils.writeByteArrayToFile(new File(path + name), s);
			log.info("文件写入成功, 开始在服务器保存地址");
			boolean b = webFileService.insert(new WebFile(uid, name, SimpleUtils.getSimpleDateSecond()));
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
}
