package com.pdsu.scs.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pdsu.scs.bean.WebInformation;
import com.pdsu.scs.service.WebInformationService;
import com.pdsu.scs.utils.SimpleDateUtils;


@SpringJUnitConfig(locations = {"classpath:spring/spring.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class WebInformationTest {

	@Autowired
	private WebInformationService webInformationService;
	
	/*
	 * 测试,根据主键获取对应的WebInformation
	 */
	@Test
	public void getWebInformationByIdtest() {
		
		System.out.println(webInformationService.selectById(3));
		
	}
	
	@Test
	public void getWebInformationOrderByTimetest() {
		
		System.out.println(webInformationService.selectWebInformationOrderByTimetest());
		
	}
	
	@Test
	public void addWebInformationTest() {
		WebInformation information = new WebInformation();
		information.setSubTime(SimpleDateUtils.getSimpleDate());
		information.setTitle("记录颜色");
		information.setUid(181360226);
		information.setWebData(("Let the Balloon Rise\r\n" + 
				"		这道题需要去判断输入的颜色是否已经存在，如果存在则让其出现的次数加一，反之则把此颜色储存到容器内。可以借助向量容器去解决。\r\n" + 
				"	\r\n" + 
				"\r\n" + 
				"	#include <iostream>\r\n" + 
				"	#include <vector>\r\n" + 
				"	#include <cstring>\r\n" + 
				"	using namespace std;\r\n" + 
				"	int b[100];\r\n" + 
				"	void fin()  //初始化函数\r\n" + 
				"	{\r\n" + 
				"	    for(int i=0;i<100;i++)\r\n" + 
				"	        b[i]=1;   \r\n" + 
				"	}\r\n" + 
				"	int main()\r\n" + 
				"	{\r\n" + 
				"	    int n;\r\n" + 
				"	    while(cin >> n,n!=0)  //n=0时结束循环\r\n" + 
				"	    {\r\n" + 
				"	        vector<string> a;  //定义一个字符串类型的向量容器\r\n" + 
				"	        fin();   //初始化\r\n" + 
				"	        int i=0,j,d=n;\r\n" + 
				"	        string s;\r\n" + 
				"	        while(d--)\r\n" + 
				"	       {\r\n" + 
				"	           int flag=1;  //旗帜，起判断作用。\r\n" + 
				"	            cin >> s;\r\n" + 
				"	            if(i==0)  //输入第一个颜色时直接存入\r\n" + 
				"	            {\r\n" + 
				"	                a.push_back(s);\r\n" + 
				"	                i++;\r\n" + 
				"	                continue;\r\n" + 
				"	            }\r\n" + 
				"	            for(j=0;j<i;j++)\r\n" + 
				"	            {\r\n" + 
				"	                int t=a[j].compare(s);  //把输入的颜色与向量里存储的比较\r\n" + 
				"	                if(t==0)   //返回值为0代表该颜色已被包含\r\n" + 
				"	                {\r\n" + 
				"	                    flag=0;\r\n" + 
				"	                    break;\r\n" + 
				"	                }\r\n" + 
				"	            }\r\n" + 
				"	            if(flag==0)   //如果已存在此颜色\r\n" + 
				"	            {\r\n" + 
				"	                b[j]++;   //该颜色所出现的次数加以\r\n" + 
				"	            }\r\n" + 
				"	            else    //反之\r\n" + 
				"	            {\r\n" + 
				"	                a.push_back(s);\r\n" + 
				"	                    i++;\r\n" + 
				"	            }\r\n" + 
				"	       }\r\n" + 
				"	       int Max=b[0],k=0;   \r\n" + 
				"	       for(j=1;j<i;j++)   //寻找出现次数最多的颜色\r\n" + 
				"	       {\r\n" + 
				"	         if(Max<b[j]) \r\n" + 
				"	          {\r\n" + 
				"	             Max=b[j];\r\n" + 
				"	             k=j;   //记录此颜色下标\r\n" + 
				"	          }\r\n" + 
				"	       }\r\n" + 
				"	       cout << a[k] << endl;\r\n" + 
				"	    }\r\n" + 
				"	    return 0;\r\n" + 
				"	}\r\n" + 
				"\r\n" + 
				"是不是感觉很麻烦？有一个更简单的方法，即使用map映照容器。\r\n" + 
				"	\r\n" + 
				"\r\n" + 
				"	#include <iostream>\r\n" + 
				"	#include <map>\r\n" + 
				"	#include <string.h>\r\n" + 
				"	using namespace std;\r\n" + 
				"	map<string,int> m; //创建映照容器为string,int型\r\n" + 
				"	string s;\r\n" + 
				"	string y;\r\n" + 
				"	int main()\r\n" + 
				"	{\r\n" + 
				"	    int n;\r\n" + 
				"	    while(cin >> n,n!=0)\r\n" + 
				"	    {\r\n" + 
				"	        int Max=0;\r\n" + 
				"	        m.clear();\r\n" + 
				"	        for(int i=0;i<n;i++)\r\n" + 
				"	        {\r\n" + 
				"	            cin >> s;\r\n" + 
				"	            m[s]++;   //对应颜色的数量加一\r\n" + 
				"	            if(Max<m[s])  //找出现次数最大的颜色\r\n" + 
				"	            {\r\n" + 
				"	                Max=m[s];\r\n" + 
				"	                y=s;     //记录此颜色\r\n" + 
				"	            }\r\n" + 
				"	        }\r\n" + 
				"	        cout << y << endl;\r\n" + 
				"	    }\r\n" + 
				"	    return 0;\r\n" + 
				"	}\r\n" + 
				"\r\n").getBytes());
		webInformationService.insert(information);
		
	}
	
}
