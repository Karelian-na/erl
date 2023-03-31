package cn.karelian.erl.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.karelian.erl.ErlApplication;
import cn.karelian.erl.Exceptions.NullRequestException;

public class Utils {
	public enum FileCategory {
		IMAGE,
		ENCLOSURE,
		MATER,
		FILE,
	}

	public static HttpServletRequest getRequest() throws NullRequestException {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		if (null == attributes) {
			throw new NullRequestException();
		}
		return ((ServletRequestAttributes) attributes).getRequest();
	}

	public static String CopyTempFileToSpecifiedCategory(FileCategory category, String fileName) {
		return CopyTempFileToSpecifiedCategory(category, new StringBuffer(fileName));
	}

	public static String CopyTempFileToSpecifiedCategory(FileCategory category, StringBuffer fileName) {
		if (fileName != null) {
			Path old = Path.of(ErlApplication.TempPath, fileName.toString());
			if (Files.exists(old)) {
				String localPathRoot = null;
				String urlPrefix = null;
				switch (category) {
					case IMAGE:
						localPathRoot = ErlApplication.LocalImagePath;
						urlPrefix = ErlApplication.ImageUrlPrefix;
						break;
					case ENCLOSURE:
						localPathRoot = ErlApplication.LocalEnclosurePath;
						urlPrefix = ErlApplication.EnclosureUrlPrefix;
						break;
					case MATER:
						localPathRoot = ErlApplication.LocalMaterPath;
						urlPrefix = ErlApplication.MaterUrlPrefix;
						break;
					default:
						localPathRoot = ErlApplication.LocalOthersFilePath;
						urlPrefix = ErlApplication.FileUrlPrefix;
						break;
				}

				String date = "";
				Path new_ = Path.of(localPathRoot, fileName.toString());
				while (Files.exists(new_)) {
					date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
					new_ = Path.of(localPathRoot, date + fileName.toString());
				}
				fileName.insert(0, date);

				try {
					Files.copy(old, new_);
				} catch (Exception e) {
					return null;
				}
				return urlPrefix + fileName;
			}
		}
		return null;
	}

}
