package com.pdsu.scs.quartz;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.pdsu.scs.service.WebInformationService;

/**
 * 该任务用于计算一篇文章的热度
 * 热度计算规则权重, 投稿时间差占1, 收藏占4, 点赞占3, 浏览量占2
 * @author Admin
 *
 */
public class WebInfromationHeat implements Job{
	
	@Autowired
	private WebInformationService webInformationService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
//		List<WebInformation> webs = webInformationService.selectWebInformationOrderByTimetest();
//		for(WebInformation web : webs) {
//			String start = web.getSubTime();
//			String end = SimpleDateUtils.getSimpleDateSecond();
//		}
	}

}
