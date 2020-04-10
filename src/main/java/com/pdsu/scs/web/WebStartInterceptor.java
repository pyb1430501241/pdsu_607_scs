package com.pdsu.scs.web;


import javax.servlet.ServletContext;
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

import com.pdsu.scs.quartz.WebInfromationHeat;

public class WebStartInterceptor implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//将web应用的名称保存到application范围中
		ServletContext application = sce.getServletContext();
		application.setAttribute("APP_PATH", application.getContextPath());
		webInfromationHeatJob();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
	
	/**
	 * 任务调度, 每日零点计算一次所有文章的热度
	 */
	private void webInfromationHeatJob() {
		// 1. 调度器(Scheduler), 从工厂中获取调度的实例, 默认是实例化了 StdSchedulerFactory
		Scheduler scheduler=null;
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		/*
		 * 2. 任务实例(JobDetail)
		 *	2.1 withIdentity 第一个参数为任务的名称(唯一实例), 第二个参数表示任务组的名称
		 *	加载我们的任务类, 与 HelloJob 进行任务绑定, 要求 HelloJob实现 Job 接口
		 */
		JobDetail jobDetail = JobBuilder.newJob(WebInfromationHeat.class)
			.withIdentity("job1", "group1")
			.usingJobData("message", "打印日志") // 传递参数  实质为传递了一个 Map
			.build();
		//任务实例可以获取很多参数
//		System.out.println(jobDetail.getKey().getName());
//		System.out.println(jobDetail.getKey().getGroup());
//		System.out.println(jobDetail.getJobClass().getName());
		SimpleScheduleBuilder.simpleSchedule();
		// 3. 触发器(Trigger)
		// withIdentity 第一个参数为任务的名称(唯一实例), 第二个参数表示任务组的名称
		Trigger trigger = TriggerBuilder.newTrigger()
			.withIdentity("trigger1", "group1")
			.startNow() // 马上启动
			.withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(0, 0))//每5秒执行一次
			.usingJobData("message", "simple触发器")
			.build();
		// 让调度器关联任务和触发器, 保证按器定义的条件执行任务
		try {
			scheduler.scheduleJob(jobDetail,trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//启动触发器
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
