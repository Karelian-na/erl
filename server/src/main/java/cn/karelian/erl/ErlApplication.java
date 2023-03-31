/*
 * @Author: Karelian_na
 */
package cn.karelian.erl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({ "cn.karelian.erl.mapper.**" })
public class ErlApplication {
	public static final String Host = "http://erl.wust.edu.cn:5173/";

	public static final String TempPath = "data/temp/";

	public static final String LocalImagePath = "data/images/";
	private static final String CommonLocalFilePath = "data/files/";
	public static final String LocalMaterPath = CommonLocalFilePath + "maters/";
	public static final String LocalOthersFilePath = CommonLocalFilePath + "others/";
	public static final String LocalEnclosurePath = CommonLocalFilePath + "enclosures/";


	private static final String LocalHost = "http://erl.wust.edu.cn:8181/";

	public static final String ImageUrlPrefix = LocalHost + "images/";
	private static final String CommonFileUrlPrefix = LocalHost + "files/";
	public static final String FileUrlPrefix = CommonFileUrlPrefix + "others/";
	public static final String MaterUrlPrefix = CommonFileUrlPrefix + "maters/";
	public static final String EnclosureUrlPrefix = CommonFileUrlPrefix + "enclosures/";

	public static void main(String[] args) {
		SpringApplication.run(ErlApplication.class, args);
	}
}
