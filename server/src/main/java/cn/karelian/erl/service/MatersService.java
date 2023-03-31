package cn.karelian.erl.service;

import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.ErlApplication;
import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.entity.Maters;
import cn.karelian.erl.errors.FieldError;
import cn.karelian.erl.mapper.MatersMapper;
import cn.karelian.erl.mapper.view.MatersViewMapper;
import cn.karelian.erl.service.interfaces.IMatersService;
import cn.karelian.erl.utils.EntityUtil;
import cn.karelian.erl.utils.LoginInfomationUtil;
import cn.karelian.erl.utils.SpringContextUtil;
import cn.karelian.erl.utils.Utils;
import cn.karelian.erl.utils.Utils.FileCategory;
import cn.karelian.erl.view.MatersView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-11-17
 */
@Service
public class MatersService extends ErlServiceImpl<MatersMapper, Maters> implements IMatersService {
	public static final Set<String> HandoutFileTypes = Set.of("ppt", "pptx", "pdf");
	public static final Set<String> SyllabusOrExamFileTypes = Set.of("doc", "docx", "pdf");

	@Autowired
	private MatersViewMapper viewMapper;

	@Override
	public Result index(String group, String category, IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		QueryWrapper<MatersView> qw = new QueryWrapper<>();
		qw.eq("`group`", group);
		qw.eq("`category`", category);

		if (params.one && !ObjectUtils.isEmpty(params.searchKey)) {

			String[] fileNameInfos = params.searchKey.split("\\.");
			if (fileNameInfos.length != 2) {
				return Result.fieldError("searchKey", FieldError.FORMAT);
			}

			params.searchKey = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-"
					+ fileNameInfos[0] + "-"
					+ LoginInfomationUtil.getUserName() + "." + fileNameInfos[1];

			return new Result(true, super.lambdaQuery()
					.eq(Maters::getAdd_uid, LoginInfomationUtil.getUserId())
					.eq(Maters::getFile_name, params.searchKey)
					.exists());
		}

		return super.index(viewMapper, params, qw);
	}

	@Override
	public Result download(String group, String category, List<Integer> ids) {
		String fileName;
		FileSystemResource resource;
		List<Maters> maters = super.lambdaQuery()
				.select(Maters::getId, Maters::getFile_addr, Maters::getDownload_times, Maters::getFile_name)
				.in(Maters::getId, ids).list();

		if (maters.size() == 0) {
			return new Result("指定文件不存在!");
		} else if (maters.size() == 1) {
			Maters mater = maters.get(0);

			resource = new FileSystemResource(mater.getFile_addr());

			if (!resource.exists()) {
				return new Result("指定文件不存在!");
			}

			Maters updateMater = new Maters();
			updateMater.setId(mater.getId());
			updateMater.setDownload_times(mater.getDownload_times() + 1);
			super.updateById(updateMater);

			fileName = mater.getFile_name();
		} else {
			String message = TableInfoHelper.getTableInfo(Maters.class).getTableName() + ": "
					+ Arrays.toString(ids.toArray());

			try {
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				md5.update(message.getBytes());
				fileName = HexUtils.toHexString(md5.digest()) + ".zip";
			} catch (Exception e) {
				return Result.internalError(null);
			}

			resource = new FileSystemResource(ErlApplication.TempPath + fileName);
			if (!resource.exists()) {
				Boolean success = false;
				ZipOutputStream zipOutputStream = null;
				try {
					resource.getFile().createNewFile();
					zipOutputStream = new ZipOutputStream(resource.getOutputStream());
				} catch (Exception e) {
				}

				if (zipOutputStream != null) {
					try {
						for (Maters curMater : maters) {
							File curFile = new File(curMater.getFile_addr());
							if (curFile.exists() && curFile.isFile()) {
								FileInputStream inputStream = new FileInputStream(curFile);
								try {
									zipOutputStream.putNextEntry(new ZipEntry(curMater.getFile_name()));
								} catch (ZipException e) {
									continue;
								}

								int len;
								byte[] buffer = new byte[1024];
								while ((len = inputStream.read(buffer)) != -1) {
									zipOutputStream.write(buffer, 0, len);
								}
								inputStream.close();
							}
						}
						zipOutputStream.close();
						success = true;
					} catch (Exception e) {
					}
				}

				if (!success) {
					if (resource.exists()) {
						resource.getFile().delete();
					}
					return Result.internalError(null);
				}
			}

			LambdaUpdateWrapper<Maters> luw = new LambdaUpdateWrapper<>();
			luw.setSql("download_times = download_times + 1");
			luw.in(Maters::getId, maters.stream().map(t -> t.getId()).toArray());

			super.update(luw);
		}

		byte[] data = null;
		try (InputStream inputStream = resource.getInputStream()) {
			data = inputStream.readAllBytes();
		} catch (Exception e) {
		}

		return new Result(true, Map.of("name", fileName, "content", data));
	}

	@Override
	public Result upload(String group, String category, MultipartFile file) {
		String[] fileNameInfos;
		String fileName = file.getOriginalFilename();
		if (fileName == null || (fileNameInfos = fileName.split("\\.")).length != 2) {
			return Result.fieldError("fileName", FieldError.FORMAT);
		}

		switch (category) {
			case "Syllabus":
			case "Exam":
				if (!MatersService.SyllabusOrExamFileTypes.contains(fileNameInfos[1])) {
					return Result.fieldError("fileName", FieldError.FORMAT);
				}
				break;
			case "Handout":
				if (!MatersService.HandoutFileTypes.contains(fileNameInfos[1])) {
					return Result.fieldError("fileName", FieldError.FORMAT);
				}
				break;
			default:
				break;
		}
		GeneralService generalService = SpringContextUtil.getBean(GeneralService.class);
		Result result = generalService.upload(new MultipartFile[] { file });
		if (!result.isSuccess()) {
			return result;
		}

		String userName = LoginInfomationUtil.getUserName();
		fileName = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
				+ "-" + fileNameInfos[0] + "-" + userName + "." + fileNameInfos[1];

		Long uid = LoginInfomationUtil.getUserId();

		Maters mater = super.lambdaQuery()
				.select(Maters::getId, Maters::getFile_addr)
				.eq(Maters::getAdd_uid, uid)
				.eq(Maters::getFile_name, fileName)
				.one();

		File localFile = null;
		File backupFile = null;
		if (mater != null) {
			localFile = new File(mater.getFile_addr());
			if (localFile.exists()) {
				backupFile = new File(localFile + ".bak");
				localFile.renameTo(backupFile);
			}
			mater.setAdd_time(LocalDateTime.now());
		} else {
			mater = new Maters();
			mater.setCategory(category);
			mater.setGroup(group);
			mater.setFile_name(fileName);
		}

		StringBuffer hashedFileName = new StringBuffer((String) result.getData());
		String url = Utils.CopyTempFileToSpecifiedCategory(FileCategory.MATER, hashedFileName);
		result = new Result();
		if (url == null) {
			if (backupFile != null) {
				backupFile.renameTo(localFile);
			}
			result.setMsg("文件保存失败!");
			return result;
		}

		mater.setFile_addr(ErlApplication.LocalMaterPath + hashedFileName);
		mater.setFile_size(file.getSize());
		mater.setDownload_times(0);

		if (backupFile != null) {
			result.setSuccess(super.updateById(mater));
		} else {
			result.setSuccess(super.save(mater));
		}

		if (result.isSuccess()) {
			MatersView view = new MatersView();
			view.setId(mater.getId());
			view.setDownload_times(0);
			view.setFile_name(fileName);
			view.setUpload_time(LocalDate.now());
			view.setFile_size(mater.getFile_size());
			if (backupFile != null) {
				backupFile.delete();
			} else {
				view.setUpload_user(userName);
			}
			result.setData(EntityUtil.ToMap(view));
		}
		return result;
	}

	@Override
	public Result delete(List<Integer> ids) {
		if (ids.size() == 0) {
			return Result.paramError("ids");
		}

		Boolean success;
		if (ids.size() == 1) {
			String path = super.getObj(
					super.lambdaQuery().select(Maters::getFile_addr).eq(Maters::getId, ids.get(0)).getWrapper(),
					t -> (String) t);

			if (success = super.removeById(ids.get(0))) {
				File file = new File(path);
				if (file.exists()) {
					file.delete();
				}
			}
		} else {
			List<Maters> maters = super.lambdaQuery().select(Maters::getId, Maters::getFile_addr).in(Maters::getId, ids)
					.list();

			if (success = super.removeBatchByIds(maters)) {
				for (Maters mater : maters) {
					File file = new File(mater.getFile_addr());
					if (file.exists()) {
						file.delete();
					}
				}
			}
		}
		return new Result(success);
	}
}
