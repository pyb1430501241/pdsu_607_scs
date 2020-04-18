package com.pdsu.scs.test;


import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pdsu.scs.bean.MyCollection;
import com.pdsu.scs.service.MyCollectionService;

@SpringJUnitConfig(locations = {"classpath:spring/spring.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class MyCollectionTest {

	@Autowired
	private MyCollectionService myCollectionService;
	
	/**
	 * 测试添加
	 */
	@Test
	public void test() {
		myCollectionService.insert(new MyCollection(null, 181360226, 12, 181360226));
	}
	
	/**
	 * 测试 根据网页id获取此网页的收藏量
	 */
	@Test
	public void test1() {
		System.out.println(myCollectionService.selectCollectionsByWebId(12));
	}
	
	/**
	 * 测试 根据一组网页id获取收藏量集合
	 */
	@Test
	public void test2() {
		System.out.println(myCollectionService.selectCollectionssByWebIds(Arrays.asList(7,12)));
	}
}
