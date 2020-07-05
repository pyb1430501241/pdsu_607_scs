package com.pdsu.scs.handler;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pdsu.scs.bean.EsBlobInformation;
import com.pdsu.scs.bean.EsFileInformation;
import com.pdsu.scs.bean.EsUserInformation;
import com.pdsu.scs.bean.Result;
import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.es.service.EsService;
import com.pdsu.scs.exception.web.es.QueryException;
import com.pdsu.scs.utils.ShiroUtils;

/**
 * 搜索相关
 * @author 半梦
 *
 */
@Controller
public class SearchHandler {
	
	@Resource(name = "esUserService")
	private EsService<EsUserInformation> esUserService;
	
	@Resource(name = "esBlobService")
	private EsService<EsBlobInformation> esBlobService;
	
	@Resource(name = "esFileService")
	private EsService<EsFileInformation> esFileService;

	private static final String EX = "exception";
	
	private static final Logger log = LoggerFactory.getLogger(SearchHandler.class);
	
	@RequestMapping("/search")
	@ResponseBody
	@CrossOrigin
	public Result searchByText(String text) {
		UserInformation user = ShiroUtils.getUserInformation();
		log.info("用户: " + user.getUid() + " 查询: " + text + " 开始");
		try {
			List<EsUserInformation> users = esUserService.queryByText(text);
			List<EsBlobInformation> blobs = esBlobService.queryByText(text);
			List<EsFileInformation> files = esFileService.queryByText(text);
			log.info("查询成功");
			return Result.success().add("authorList", users).add("	", blobs)
					.add("fileList", files);
		} catch (QueryException e) {
			log.error(e.getMessage());
			return Result.fail().add(EX , "网络延迟, 请稍后重试");
		}
	}

}
