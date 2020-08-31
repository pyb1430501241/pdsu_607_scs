package com.pdsu.scs.quartz;

/**
 * @author 半梦
 */
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 该任务用于计算一篇文章的热度
 * 热度计算规则权重, 投稿时间差占1, 收藏占4, 点赞占3, 浏览量占2
 * @author Admin
 *
 */
public class WebInformationHeat implements Job{


	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
	}

}
