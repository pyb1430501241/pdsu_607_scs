package com.pdsu.scs.web;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.pdsu.scs.quartz.WebInformationHeat;

/**
 * 
 * @author 半梦
 *
 */
public class WebStartInterceptor implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
//		//将web应用的名称保存到application范围中
//		ServletContext application = sce.getServletContext();
//		application.setAttribute("APP_PATH", application.getContextPath());
		webInfromationHeatJob();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
	
	/**
	 * 任务调度, 每日零点计算一次所有文章的热度
	 */
	private void webInfromationHeatJob() {
		Scheduler scheduler=null;
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		JobDetail jobDetail = JobBuilder.newJob(WebInformationHeat.class)
			.withIdentity("job1", "group1")
			.usingJobData("message", "打印日志") // 传递参数  实质为传递了一个 Map
			.build();
		SimpleScheduleBuilder.simpleSchedule();
		Trigger trigger = TriggerBuilder.newTrigger()
			.withIdentity("trigger1", "group1")
			.startNow() // 马上启动
			.withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(0, 0))//每5秒执行一次
			.usingJobData("message", "simple触发器")
			.build();
		try {
			scheduler.scheduleJob(jobDetail,trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
	}
}
