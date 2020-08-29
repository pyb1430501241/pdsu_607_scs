/*
 * 权限模型采用 RBAC 模型
 * 总共分为三个等级
 * 3. admin
 * 2. teacher
 * 1. student
 */
package com.pdsu.scs.handler;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pdsu.scs.bean.ClazzInformation;
import com.pdsu.scs.bean.UserClazzInformation;
import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.exception.web.admin.ClazzRepetitionException;
import com.pdsu.scs.service.ClazzInformationService;
import com.pdsu.scs.service.UserClazzInformationService;
import com.pdsu.scs.service.UserInformationService;
import com.pdsu.scs.service.UserRoleService;
import com.pdsu.scs.utils.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.pdsu.scs.bean.Result;

import java.util.List;

/**
 * @author 半梦
 */
@Controller
@RequestMapping("/admin")
public class AdminHandler extends ParentHandler{

	/**
	 * 权限
	 */
	@Autowired
	private UserRoleService userRoleService;

	/**
	 * 用户
	 */
	@Autowired
	private UserInformationService userInformationService;

	/**
	 *班级信息
	 */
	@Autowired
	private ClazzInformationService clazzInformationService;

	/**
	 * 用户班级对应
	 */
	@Autowired
	private UserClazzInformationService userClazzInformationService;


	private static final Logger log = LoggerFactory.getLogger(AdminHandler.class);

	/**
	 * 获取所有用户信息
	 * 需要权限为: admin
	 * @param p
	 * @return
	 */
	@ResponseBody
	@GetMapping("/getuserinformationlist")
	@CrossOrigin
	public Result getUserInformationList(@RequestParam(value = "p", defaultValue = "1") Integer p) {
		UserInformation user = null;
		try {
			user = ShiroUtils.getUserInformation();
			if(!userRoleService.isAdmin(user.getUid())) {
				log.info("用户: " + user.getUid() + " 无权限");
				return Result.permission();
			}
			log.info("管理员: " + user.getUid() + " 获取所有用户信息");
			PageHelper.startPage(p, 15);
			PageInfo<UserInformation> userList = new PageInfo<>(
					userInformationService.selectUserInformations()
			, 5);
			return Result.success().add("userList", userList);
		} catch (Exception e) {
			if(user == null) {
				log.info("获取用户列表失败, 原因: 用户未登录");
				return Result.fail().add(EXCEPTION, NOT_LOGIN);
			}
			log.error("管理员获取用户信息失败, 原因: " + e.getMessage());
			return Result.fail().add(EXCEPTION, DEFAULT_ERROR_PROMPT);
		}
	}

	@ResponseBody
	@CrossOrigin
	@PostMapping("/applyclass")
	public Result createClazz(String clazzName, @RequestParam(required = false) List<Integer> uids) {
		UserInformation user = null;
		try {
			user = ShiroUtils.getUserInformation();
			if (!userRoleService.isTeacher(user.getUid())) {
				log.info("用户: " + user.getUid() + "无权限");
				return Result.permissionByTeacher();
			}
			log.info("教师或管理员: " + user.getUid() + "开始创建班级");
			ClazzInformation clazzInformation = new ClazzInformation(clazzName);
			boolean b = clazzInformationService.insert(clazzInformation);
			if (b) {
				log.info("创建班级成功, 班级ID为: " + clazzInformation.getId());
				if(uids != null && uids.size() > 0) {
					log.info("");
					userClazzInformationService.insertByList(uids, clazzInformation.getId());
				}
				return Result.success().add("clazzId", clazzInformation.getId());
			}
			log.warn("创建班级失败, 原因为: " + "连接数据库失败");
			return Result.fail().add(EXCEPTION, "网络连接超时, 请稍候重试");
		} catch (Exception e) {
			if (user == null) {
				log.info("用户创建班级失败, 原因: 用户未登录");
				return Result.fail().add(EXCEPTION, NOT_LOGIN);
			}
			log.error("用户创建班级发生未知错误, 原因: " + e.getMessage());
			return Result.fail().add(EXCEPTION, DEFAULT_ERROR_PROMPT);
		}
	}

}
