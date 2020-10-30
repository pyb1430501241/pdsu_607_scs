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
 * @create 2020
 *	1. ES 服务器过于垃圾！查询日常不可用！甚至影响了账号申请和博客发布！
 *	故无论在申请或发布文章中，插入 ES 索引是否成功，只要插入数据库成
 *	功，均按成功，可能导致的结果为搜索中找不到这个 人/文章。
 *	2. 查询时关键字标红貌似有bug，但暂未知晓bug的解决方案
 *优化方案：
 * 	1. 换个好的 ES服务器集群！
 * 	2. 优化处理逻辑，在 ES 插入失败后抛出的异常里进行再次插入处理
 * 		（题外话：插入都出异常了，尝试几遍都不太可能成功吧？？？
 *  3. 开启线程模式，直到插入成功为止....
 *  	抛弃的方案，第一过于浪费资源，第二同 2
 *  4. 别急着优化了，听歌放松下
 *  	歌曲名：桜咲く (樱花开)，歌手名：ClariS (クラリス)，专辑名：STEP
 *  by 庞亚彬 2020-10-30
 */
@Controller
public class SearchHandler extends ParentHandler {
	
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
