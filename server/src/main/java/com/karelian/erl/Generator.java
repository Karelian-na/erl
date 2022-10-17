/*
 * @Author: Karelian_na
 */
package com.karelian.erl;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.karelian.erl.controller.BaseController;

import java.util.Collections;

public class Generator {

	public static void main(String[] args) {
		FastAutoGenerator.create(
				"jdbc:mysql://localhost:3306/electronicresourcelibrary?serverTimeZone=UTC",
				"erl_user", "12345678")
				.globalConfig(builder -> {
					builder.outputDir(
							"G:\\electronicresourcelibrary\\server\\src\\main\\java");
				})
				.packageConfig(builder -> {
					builder.parent("com.karelian.erl")
							.moduleName(null)
							.pathInfo(Collections.singletonMap(OutputFile.xml,
									"G:\\electronicresourcelibrary\\server\\src\\main\\resources\\mappers"));
				})
				.strategyConfig(builder -> {
					builder.entityBuilder().enableLombok();
					builder.controllerBuilder().enableHyphenStyle()
							.enableRestStyle();
					builder.addInclude(new String[] {
							// "applications",
							// "courses",
							// "internships",
							// "laboratories",
							// "logs",
							// "majors",
							// "permissions",
							// "role_perm_assoc",
							// "roles",
							// "students",
							// "teachers",
							// "textbooks",
							// "user_perm_assoc",
							// "user_role_assoc",
							"usermsgs",
							// "users",
					});

					builder.serviceBuilder().formatServiceFileName("I%sService")
							.formatServiceImplFileName("%sService");

					builder.controllerBuilder().superClass(BaseController.class);
				})
				.execute();
	}
}