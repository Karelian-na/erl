/*
 * @Author: Karelian_na
 */
package cn.karelian.erl;

import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import cn.karelian.erl.mapper.ErlMapper;
import cn.karelian.erl.service.ErlServiceImpl;
import cn.karelian.erl.service.interfaces.IErlService;

import java.util.Collections;

public class Generator {
	public static void main(String[] args) {
		FastAutoGenerator autoGenerator = FastAutoGenerator.create(
				"jdbc:mysql://localhost:3306/electronicresourcelibrary?serverTimeZone=UTC",
				"erl_user", "12345678")
				.globalConfig(builder -> {
					builder
							.outputDir("src\\main\\java")
							.disableOpenDir()
							.author("Karelian_na");
				});

		// generateTable(autoGenerator);
		generateView(autoGenerator);
	}

	public static void generateTable(FastAutoGenerator autoGenerator) {
		autoGenerator.packageConfig(builder -> {
			builder.parent("cn.karelian.erl")
					.service("service.interfaces")
					.serviceImpl("service")
					.pathInfo(Collections.singletonMap(OutputFile.xml,
							"src\\main\\resources\\mappers"));
		}).strategyConfig(builder -> {
			builder.entityBuilder()
					.addTableFills(new Column[] {
							new Column("add_user", FieldFill.INSERT),
							new Column("add_time", FieldFill.INSERT),
							new Column("update_time", FieldFill.UPDATE)
					})
					.logicDeleteColumnName("deleted")
					.columnNaming(NamingStrategy.no_change)
					.enableLombok();

			builder.controllerBuilder()
					.enableHyphenStyle()
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
					"roles",
					// "students",
					// "teachers",
					// "textbooks",
					// "user_perm_assoc",
					// "user_role_assoc",
					// "usermsgs",
					// "users",
					// "table_fields_info",
					// "book_reservations",
					// "teaching_maters",
					// "disciplines",
					// "degrees",
					// "academic_papers",
					// "projects",
					// "patents",
					// "teaching_awards",
					// "competition_awards",
					// "academic_conferences",
					// "declarations",
					// "laboratory_reservations",
					// "paper_author_assoc",
					// "applications",
			});

			builder.serviceBuilder()
					.superServiceClass(IErlService.class)
					.superServiceImplClass(ErlServiceImpl.class)
					.formatServiceFileName("I%sService")
					.formatServiceImplFileName("%sService");

			builder.mapperBuilder()
					.superClass(ErlMapper.class);
		}).execute();
	}

	public static void generateView(FastAutoGenerator autoGenerator) {
		autoGenerator.templateConfig(builder -> {
			builder.disable(TemplateType.CONTROLLER)
					.disable(TemplateType.SERVICE)
					.disable(TemplateType.SERVICE_IMPL);
		}).packageConfig(builder -> {
			builder.parent("cn.karelian.erl")
					.mapper("mapper.view")
					.entity("view")
					.pathInfo(Collections.singletonMap(OutputFile.xml,
							"src\\main\\resources\\mappers\\view"));
		}).strategyConfig(builder -> {
			builder.entityBuilder()
					.columnNaming(NamingStrategy.no_change)
					.enableLombok();
			builder.addInclude(new String[] {
					// "views_info", // table

					// "views",
					// "fields_info_view",
					// "maters_view",
					// "permissions_view",
					// "book_reservations_view",
					// "laboratory_reservations_view",
					// "teacmsgs_view",
					// "stumsgs_view",
					// "postmsgs_view",
					// "usermsgs_view",
					// "tutmsgs_view",
					// "internship_apps_view",
					// "declarations_view",
					// "teaching_awards_view",
					// "competition_awards_view",
					// "conferences_view",
					// "patents_view",
					"papers_author_view",
					// "papers_view",
					// "projects_view",
			});

			builder.mapperBuilder()
					.superClass(ErlMapper.class);
		}).execute();
	}
}