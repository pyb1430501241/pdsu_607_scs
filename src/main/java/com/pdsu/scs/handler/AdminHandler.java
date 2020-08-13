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
import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.service.UserInformationService;
import com.pdsu.scs.service.UserRoleService;
import com.pdsu.scs.utils.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.pdsu.scs.bean.Result;

/**
 * @author 半梦
 */
@Controller
@RequestMapping("/admin")
public class AdminHandler {

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private UserInformationService userInformationService;

	private static final Logger log = LoggerFactory.getLogger(AdminHandler.class);

	private static final String EX = "exception";

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
			);
			return Result.success().add("userList", userList);
		} catch (Exception e) {
			if(user == null) {
				log.info("用户未登录");
				return Result.fail().add(EX, "未登录");
			}
			log.error("管理员获取用户信息失败, 原因: " + e.getMessage());
			return Result.fail().add(EX, "未定义类型错误");
		}
	}
	
}
