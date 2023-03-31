package cn.karelian.erl.service;

import cn.karelian.erl.DataTransferObject.BookReservationParam;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.ErlApplication;
import cn.karelian.erl.Result;
import cn.karelian.erl.StatusCode;
import cn.karelian.erl.entity.BookReservations;
import cn.karelian.erl.entity.Textbooks;
import cn.karelian.erl.mapper.TextbooksMapper;
import cn.karelian.erl.service.interfaces.ITextbooksService;
import cn.karelian.erl.utils.EntityUtil;
import cn.karelian.erl.utils.LoginInfomationUtil;
import cn.karelian.erl.utils.Utils;
import cn.karelian.erl.utils.WebPageInfo;
import cn.karelian.erl.utils.Utils.FileCategory;
import cn.karelian.erl.view.FieldsInfoView;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Service
public class TextbooksService extends ErlServiceImpl<TextbooksMapper, Textbooks>
		implements ITextbooksService {
	@Autowired
	private BookReservationsService bookReservationsService;

	private Result checkFields(Textbooks textbook, boolean add) {
		int code = EntityUtil.CheckStringField(textbook.getNumber(), 1, 20, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("number", code);
		}

		code = EntityUtil.CheckStringField(textbook.getName(), 1, 50, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("name", code);
		}

		code = EntityUtil.CheckStringField(textbook.getType(), 1, 20, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("type", code);
		}

		code = EntityUtil.CheckStringField(textbook.getAuthor(), 1, 20, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("author", code);
		}

		code = EntityUtil.CheckStringField(textbook.getPublisher(), 1, 20, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("publisher", code);
		}

		code = EntityUtil.CheckStringField(textbook.getIsbn(), 17, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("isbn", code);
		}

		code = EntityUtil.CheckNumberField(textbook.getPrice(), 0F, 1000F, false);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("price", code);
		}

		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Result resindex(IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		if (params.initPageSize != null) {
			QueryWrapper<Textbooks> qw = new QueryWrapper<>();
			qw.select("id", "number", "name", "cover", "type", "author", "publisher", "isbn", "price");
			Result result = super.index(params, qw);

			QueryWrapper<BookReservations> rqw = new QueryWrapper<>();
			rqw.select("amount", "message");
			List<FieldsInfoView> addFields = bookReservationsService.getFields(rqw);

			WebPageInfo<Textbooks> webPageInfo = WebPageInfo.class.cast(result.getData());
			webPageInfo.fields.forEach(u -> u.setEditable(false));
			addFields.forEach(u -> {
				u.setDisplay(false);
				u.setEditable(true);
				webPageInfo.fields.add(u);
			});
			return result;
		}
		return super.index(params);
	}

	@Override
	public Result reserve(BookReservationParam params) throws NullRequestException {
		if (params.id == null) {
			return Result.paramError("id");
		}

		if (params.amount == null || params.amount <= 0 || params.amount > 100) {
			return Result.paramError("amount");
		}

		if (params.message != null && params.message.length() > 100) {
			return Result.paramError("message");
		}

		BookReservations reservation = new BookReservations();
		reservation.setUid(LoginInfomationUtil.getUserId());
		reservation.setTid(params.id);
		reservation.setAmount(params.amount);
		reservation.setMessage(params.message);

		return new Result(bookReservationsService.save(reservation), reservation);
	}

	@Override
	public Result add(Textbooks textbook) {
		Result result = checkFields(textbook, true);

		if (result != null) {
			return result;
		}

		result = new Result();
		if (super.lambdaQuery().eq(Textbooks::getNumber, textbook.getNumber()).exists()) {
			result.setMsg("编号" + textbook.getNumber() + "的书本已存在!");
			return result;
		}

		String coverName = textbook.getCover();
		if (!ObjectUtils.isEmpty(coverName)) {
			String url = Utils.CopyTempFileToSpecifiedCategory(FileCategory.IMAGE, coverName);
			if (url == null) {
				result.setMsg("封面保存失败!");
				return result;
			}
			textbook.setCover(url);
		}

		result.setSuccess(super.save(textbook));
		if (result.isSuccess()) {
			result.setData(textbook);
		}

		return result;
	}

	@Override
	public Result edit(Textbooks textbook) {
		if (textbook.getId() == null) {
			return Result.emptyError("id");
		}

		Result result = this.checkFields(textbook, false);
		if (result != null) {
			return result;
		}

		result = new Result();
		Textbooks old = super.lambdaQuery()
				.select(Textbooks::getCover)
				.eq(Textbooks::getId, textbook.getId())
				.one();
		if (old == null) {
			result.setMsg("书本不存在!");
			return result;
		}

		File localFile = null;
		File backupFile = null;
		String coverName = textbook.getCover();
		if (!ObjectUtils.isEmpty(coverName)) {
			String url = old.getCover();
			if (url != null && url.length() > ErlApplication.ImageUrlPrefix.length()) {
				localFile = new File(
						ErlApplication.LocalImagePath + url.substring(ErlApplication.ImageUrlPrefix.length()));
				if (localFile.exists()) {
					backupFile = new File(localFile + ".bak");
					localFile.renameTo(backupFile);
				}
			}
			url = Utils.CopyTempFileToSpecifiedCategory(FileCategory.IMAGE, coverName);
			if (url == null) {
				if (backupFile != null) {
					backupFile.renameTo(localFile);
				}
				result.setMsg("封面保存失败!");
				return result;
			}
			textbook.setCover(url);
		}

		textbook.setAdd_time(null);
		textbook.setAdd_user(null);

		result.setSuccess(super.updateById(textbook));
		if (result.isSuccess()) {
			if (backupFile != null) {
				backupFile.delete();
			}
			result.setData(EntityUtil.ToMap(textbook));
		}
		return result;
	}

	@Override
	public Result delete(List<Integer> ids) {
		if (ids.size() != 1) {
			return Result.paramError("ids");
		}

		Textbooks book = super.lambdaQuery().select(Textbooks::getCover).eq(Textbooks::getId, ids.get(0)).one();
		if (book != null) {
			// String url = book.getCover();
			// if (url != null && url.length() > ErlApplication.ImageUrlPrefix.length()) {
			// new File(ErlApplication.LocalImagePath +
			// url.substring(ErlApplication.ImageUrlPrefix.length()))
			// .delete();
			// }
			return new Result(super.removeById(ids.get(0)));
		} else {
			return new Result("书本不存在!");
		}
	}
}
