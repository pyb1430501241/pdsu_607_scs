package com.pdsu.scs.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

public class MbgUtils {
	
	public static void main(String[] args) throws Exception{
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		File file = new File("mbg.xml");
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration conf = cp.parseConfiguration(file);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator batisGenerator = new MyBatisGenerator(conf, callback, warnings);
		batisGenerator.generate(null);
	}
	
}
