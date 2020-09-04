package com.pdsu.scs.handler;

import com.pdsu.scs.bean.EsBlobInformation;
import com.pdsu.scs.bean.EsFileInformation;
import com.pdsu.scs.bean.EsUserInformation;
import com.pdsu.scs.bean.Result;
import com.pdsu.scs.es.service.EsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 搜索相关
 * @author 半梦
 *
 */
@Controller
public class SearchHandler extends ParentHandler{
	
	@Resource(name = "esUserService")
	private EsService<EsUserInformation> esUserService;
	
	@Resource(name = "esBlobService")
	private EsService<EsBlobInformation> esBlobService;
	
	@Resource(name = "esFileService")
	private EsService<EsFileInformation> esFileService;

	private static final Logger log = LoggerFactory.getLogger(SearchHandler.class);
	
	/**
	 * 根据关键字在 es 中搜索 用户, 博客, 文件
	 * @param text
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public Result searchByText(@RequestParam(value = "p")String text) throws Exception{
		log.info("用户查询: " + text + " 开始");
		List<EsUserInformation> users = esUserService.queryByText(text);
		List<EsBlobInformation> blobs = esBlobService.queryByText(text);
		List<EsFileInformation> files = esFileService.queryByText(text);
		log.info("查询成功");
		return Result.success().add("authorList", users).add("blobList", blobs)
				.add("fileList", files);
	}

}
