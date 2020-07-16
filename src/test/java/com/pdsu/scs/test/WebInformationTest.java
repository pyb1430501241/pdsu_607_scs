package com.pdsu.scs.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pdsu.scs.bean.WebInformation;
import com.pdsu.scs.exception.web.es.InsertException;
import com.pdsu.scs.service.WebInformationService;
import com.pdsu.scs.utils.SimpleUtils;


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
	public void addWebInformationTest() throws InsertException {
		WebInformation information = new WebInformation();
		information.setSubTime(SimpleUtils.getSimpleDateSecond());
		information.setTitle("Java字符串");
		information.setUid(181360226);
		information.setWebData(("## Java字符串操作\r\n" + 
				"**在Java中，把字符串当作对象处理，主要原因是Java是一种面向对象的程序设计语言。通常可以使用类String和StringBuffer的对象来表示和处理字符串。**\r\n" + 
				"\r\n" + 
				"# 1：创建字符串\r\n" + 
				"(1):在Java中,常量字符串是用双括号括起来的一串字符,如\"Hello,world!\",Java编译器能自动为字符串变量生成String类型的对象。所以,**可以通过一个字符串常量直接初始化一个String类型的对象**。\r\n" + 
				"**String s = new String(\"Hellow,world!\")；**\r\n" + 
				"\r\n" + 
				"(2):通过Java中的String类提供的构造方法可以生成一个空字符串,字符数组,字符串对象,字符串缓冲区对象。\r\n" + 
				"**如 String s = new String()**\r\n" + 
				"Java中String类中有很多其他的构造方法,如String(byte[] bytes)通过byte数组初始化一个新的String对象，String(char[] value): 使用char类型的数组初始化一个新的字符串对象等等。\r\n" + 
				"\r\n" + 
				"(3):StringBuffer类是字符串缓冲区,它支持对原字符串的修改,可以对字符串中任何子字符串的增加，删除，修改，并且可以合并两个字符串而不需要建立新的字符串。主要特点是支持多线程，且具有线程安全性。每一个字符串缓冲区对象都有一定的存储空间，只要字符串长度没有超出容量就无需分配新的内部缓冲区数组，如果内部空间不足则**容量自动增大**。\r\n" + 
				"生成StringBuffer的方法有几种\r\n" + 
				"1.StringBuffer()；不带任何参数,初始容量为16个字符。\r\n" + 
				"2.StringBuffer(int capacity)；指定初始容量。\r\n" + 
				"3.StringBuffer(String str)；通过字符串创建字符串缓冲区对象，该对象的内容为指定字符串内容，初始容量为字符串长度加16个字符。\r\n" + 
				"\r\n" + 
				"# 2：String类操作\r\n" + 
				"**(1):字符串连接**\r\n" + 
				"1.String对象之间可以直接使用\"+\"连接，\r\n" + 
				"2.如果是基本类型则可以直接使用\"+\"与字符串进行连接，\r\n" + 
				"3.如果是类的对象，在Java中所有的类都直接或间接的继承了Object类，其中有一个方法是toString()方法可以讲对象转换为字符串再执行连接操作。\r\n" + 
				"**(2):String类中的主要方法**\r\n" + 
				"1. **public char charAt(int index)** 方法，它的用途是获取字符串第index位置的字符。\r\n" + 
				"	如 ：\r\n" + 
				"	\r\n" + 
				"		String str = new String(\"hello，world!\");\r\n" + 
				"		char c = str.charAt(1)；\r\n" + 
				"		System.out.println(c);\r\n" + 
				"	\r\n" + 
				"	此时输出便为字符串中下标为1的元素，即'e'。\r\n" + 
				"2. **public int length()** 方法，它的用途是返回当前字符串的长度。\r\n" + 
				"\r\n" + 
				"		String str = \"hello，world\";\r\n" + 
				"		int  size = str.length();\r\n" + 
				"		System.out.println(size);\r\n" + 
				"\r\n" + 
				"3. **public int compareTo(String anotherString)** 方法，该方法用于比较字符串的大小，返回值为一个整数或0，字符串相等时返回0，比较依据是比较两个字符串从前到后的每一个字符的大小，使用的是字符的Unicode值。此方法比较字符串时不区分大小写。\r\n" + 
				"4. **public boolean equals(String antherString)** 方法，该方法也是用于字符串的比较返回值为true或false，当且仅当两个字符串长度相同，并且其中对应的每一个字符都相等(区分大小写)时返回结果才会true。\r\n" + 
				"\r\n" + 
				"		String str1 = new String(\"java\");\r\n" + 
				"		String str2 = new String(\"Java\");\r\n" + 
				"		System.out.println(str1.compareTo(str1));\r\n" + 
				"		System.out.println(str1.equals(str2));\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"6. **xxx.parsexxx(String str)** 方法，其中xxx为你想转换为的类型，其中有两点需要注意\r\n" + 
				"(1):当你转换为整型int时xxx要写成Integer。\r\n" + 
				"(2):类型的首字母要大写，例如 **long l = Long.parseLong(\"100\")**。\r\n" + 
				"7. 查找\r\n" + 
				"**int indexOf(int ch)** 方法，该方法返回指定字符第一次在字符串中出现的位置，未出现返回-1\r\n" + 
				"**int indexOf(char ch，int fromIndex)** 方法，返回指定位置之后该字符第一次出现的位置。\r\n" + 
				"同理也可查自定字符串第一次出现的位置 **int indexOf(String str)**\r\n" + 
				"也可以使用 **int lastindexOf(char ch)** 等等去查找指定字符，字符串最后一次出现的位置。\r\n" + 
				"\r\n" + 
				"		String str = new String(\"hello world!\");\r\n" + 
				"		System.out.println(str.indexOf(\"l\"));\r\n" + 
				"		System.out.println(str.lastIndexOf(\"l\"));\r\n" + 
				"		String str1 = new String(\"hello\");\r\n" + 
				"		str = str + str1;\r\n" + 
				"		System.out.println(str.indexOf(str1));\r\n" + 
				"		System.out.println(str.lastIndexOf(str1));\r\n" + 
				"	**注意！，查找字符串时返回的时指定字符串第一个字符出现的位置**\r\n" + 
				"8. 替换或修改\r\n" + 
				"String类表示不变字符串，不能对其修改，但是可以重新生成一个String类的对象同时完成修改。\r\n" + 
				"(1): **String toLowerCase()** ，该方法可以将所有字符转换为小写字符。\r\n" + 
				"(2): **String toUpperCase()** ，该方法可以将所有字符转换为大写字符。\r\n" + 
				"(3): **public String replace(char oldChar,char newChar)** ，该方法可以将oldChar全部替换为newChar。\r\n" + 
				"(4): **public Strubg replaceAll(String regex，String replacement)** ，该方法可以将所有的regex字符串替换为replacement字符串\r\n" + 
				"(5): **public String concat(String str)** ，该方法用于连接字符串。\r\n" + 
				"\r\n" + 
				"		String str = new String(\"heLLlo World!\");\r\n" + 
				"		System.out.println(str);\r\n" + 
				"		//转换为小写\r\n" + 
				"		System.out.println(str.toLowerCase());\r\n" + 
				"		//转换为大写\r\n" + 
				"		System.out.println(str.toUpperCase());\r\n" + 
				"		//把L替换为a\r\n" + 
				"		String str1 = str.replace('L','a');\r\n" + 
				"		System.out.println(str1);\r\n" + 
				"		String str2 = \"hello world !\";\r\n" + 
				"		//把world替换为Java\r\n" + 
				"		System.out.println(str2.replace(\"world\",\"Java\"));\r\n" + 
				"		//连接str和str2\r\n" + 
				"		String str3 = str2.concat(str);\r\n" + 
				"		System.out.println(str3);\r\n" + 
				"		\r\n" + 
				"# StringBuffer类操作\r\n" + 
				"*StringBuffer类可以在本对象中进行增加，删除，修改，并不需要创建新的对象，这是StringBuffer类对象最大的好处，也是与String类最大的区别。String类中的方法大部分在StringBuffer中都可以使用。*\r\n" + 
				"1. StringBuffer类使用append()方法进行添加，添加对象可以为布尔型，字符，字符串，浮点型等等，如果添加的字符超出缓冲区长度，Java将自动扩容。\r\n" + 
				"		\r\n" + 
				"		StringBuffer str = new StringBuffer(\"hello world!\");\r\n" + 
				"		//返回当前已使用缓冲区长度\r\n" + 
				"		System.out.println(str.length());\r\n" + 
				"		//返回当前缓冲区的总容量\r\n" + 
				"		System.out.println(str.capacity());\r\n" + 
				"		//分割线\r\n" + 
				"		System.out.println(\"###############\");\r\n" + 
				"		//字符串添加\r\n" + 
				"		str = str.append(str);\r\n" + 
				"		System.out.println(str.length());\r\n" + 
				"		System.out.println(str.capacity());\r\n" + 
				"		System.out.println(\"###############\");\r\n" + 
				"		str = str.append(str);\r\n" + 
				"		System.out.println(str.length());\r\n" + 
				"		System.out.println(str.capacity());\r\n" + 
				"\r\n" + 
				"2. StringBuffer使用insert()方法进行插入操作，可插入对象和append()方法一样，当插入字符超出缓冲区长度,Java将自动扩容。\r\n" + 
				"\r\n" + 
				"		StringBuffer str = new StringBuffer(\"hello world!\");\r\n" + 
				"		System.out.println(str);\r\n" + 
				"		//返回当前已使用缓冲区长度\r\n" + 
				"		System.out.println(str.length());\r\n" + 
				"		//返回当前缓冲区的总容量\r\n" + 
				"		System.out.println(str.capacity());\r\n" + 
				"		//分割线\r\n" + 
				"		System.out.println(\"###############\");\r\n" + 
				"		//字符串插入指定位置\r\n" + 
				"		str.insert(0,\"Java:\");\r\n" + 
				"		System.out.println(str);\r\n" + 
				"		System.out.println(str.length());\r\n" + 
				"		System.out.println(str.capacity());\r\n" + 
				"		System.out.println(\"###############\");\r\n" + 
				"		\r\n" + 
				"		str = str.insert(11,\"beautiful \");\r\n" + 
				"		System.out.println(str);\r\n" + 
				"		System.out.println(str.length());\r\n" + 
				"		System.out.println(str.capacity());").getBytes());
		webInformationService.insert(information);
		
	}
	
}
