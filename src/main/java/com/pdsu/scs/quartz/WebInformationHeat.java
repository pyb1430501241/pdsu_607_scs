package com.pdsu.scs.quartz;

/**
 * @author 半梦
 */
import com.pdsu.scs.bean.SystemNotification;
import com.pdsu.scs.service.SystemNotificationService;
import com.pdsu.scs.service.impl.SystemNotificationServiceImpl;
import com.pdsu.scs.utils.SimpleUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.pdsu.scs.service.WebInformationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

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
