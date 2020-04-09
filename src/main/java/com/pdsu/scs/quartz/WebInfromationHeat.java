package com.pdsu.scs.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 该任务用于计算一篇文章的热度
 * @author Admin
 *
 */
public class WebInfromationHeat implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
	}

}
