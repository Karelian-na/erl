-- 数据库视图
	CREATE DATABASE electronicresourcelibrary;
	
	USE electronicresourcelibrary;
	
	CREATE USER `erl_user`@`%` IDENTIFIED WITH mysql_native_password BY '12345678';

	GRANT ALL ON `electronicresourcelibrary`.* TO `erl_user`@`%`;

-- 视图信息表
	DROP TABLE IF EXISTS views_info;
	CREATE TABLE views_info(
		view_name VARCHAR(30) PRIMARY KEY COMMENT '视图名称',
		comment VARCHAR(100) DEFAULT NULL COMMENT '备注',
		update_time DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
		update_user VARCHAR(20) COMMENT '更新人'
	);
	
	INSERT INTO views_info(view_name, comment) VALUES('book_reservations_view', '管理预定申请的视图, 用于展示给管理者');
	INSERT INTO views_info(view_name, comment) VALUES('declarations_view', '管理各种申报的视图, 例如奖项申报、论文申报等等，用于展示给管理者');
	INSERT INTO views_info(view_name, comment) VALUES('internship_apps_view', '管理实习申请的视图, 用于展示给管理者');
	INSERT INTO views_info(view_name, comment) VALUES('laboratory_reservations_view', '管理实验室预约的视图, 用于展示给管理者');
	INSERT INTO views_info(view_name, comment) VALUES('maters_view', '展示各种文件的视图, 例如教学资料, 规章制度等等');
	INSERT INTO views_info(view_name, comment) VALUES('papers_view', '展示论文详细信息的视图');
	INSERT INTO views_info(view_name, comment) VALUES('psotmsgs_view', '展示论研究生名录的视图');
	INSERT INTO views_info(view_name, comment) VALUES('stumsgs_view', '展示所有学生的视图, 包括研究生, 博士生');
	INSERT INTO views_info(view_name, comment) VALUES('teacmsgs_view', '展示所有教师的视图, 包括导师');
	INSERT INTO views_info(view_name, comment) VALUES('tutmsgs_view', '展示所有导师的视图');
	INSERT INTO views_info(view_name, comment) VALUES('usermsgs_view', '展示用户信息的视图');

-- 视图信息视图
	CREATE OR REPLACE VIEW views AS
		SELECT 
			TABLES.TABLE_NAME AS view_name,
			views_info.`comment`,
			views_info.update_time,
			views_info.update_user
		FROM information_schema.`TABLES` TABLES
		LEFT JOIN views_info ON TABLES.TABLE_NAME = views_info.view_name
		WHERE 
			TABLE_SCHEMA = 'electronicresourcelibrary' 
			AND TABLE_NAME NOT IN ("table_fields_info", "fields_info_view", "views_info", "views", "role_perm_assoc", "user_perm_assoc", "user_role_assoc")
-- 			AND TABLE_TYPE = "VIEW"
		;
		

-- 字段信息表
	DROP TABLE IF EXISTS table_fields_info;
	CREATE TABLE table_fields_info (
		table_name VARCHAR(50) NOT NULL COMMENT '表名或视图名',
		field_name VARCHAR(50) NOT NULL COMMENT '字段名',
		display_name VARCHAR(50) DEFAULT NULL COMMENT '展示名',
		display_order TINYINT UNSIGNED DEFAULT 0 COMMENT '次序',
		display BOOL DEFAULT FALSE COMMENT '是否展示',
		searchable BOOL DEFAULT FALSE COMMENT '是否可检索',
		editable BOOL DEFAULT FALSE COMMENT '是否可编辑',
		PRIMARY KEY(table_name, field_name)
	);

	INSERT INTO table_fields_info VALUES('views', 'view_name', '视图名称', 0, 1, 1, 1);
	INSERT INTO table_fields_info VALUES('views', 'comment', '备注', 1, 1, 1, 1);
	INSERT INTO table_fields_info VALUES('views', 'update_time', '更新时间', 2, 1, 1, 1);
	INSERT INTO table_fields_info VALUES('views', 'update_user', '更新人', 3, 1, 1, 1);
	
-- 字段信息视图
	CREATE OR REPLACE VIEW fields_info_view AS
		SELECT
			COLUMNS.TABLE_NAME as table_name,
			COLUMNS.COLUMN_NAME as field_name,
			(CASE 
				WHEN table_fields_info.display_name IS NULL
					THEN COLUMNS.COLUMN_COMMENT
				ELSE
					table_fields_info.display_name
				END
			) AS display_name,
			table_fields_info.display_order,
			table_fields_info.display,
			table_fields_info.searchable,
			table_fields_info.editable
		FROM information_schema.`COLUMNS` COLUMNS
		LEFT JOIN views ON COLUMNS.TABLE_NAME = views.view_name
		LEFT JOIN table_fields_info ON CONCAT(COLUMNS.TABLE_NAME, COLUMNS.COLUMN_NAME) = CONCAT(table_fields_info.table_name, table_fields_info.field_name)
		WHERE
			COLUMNS.TABLE_SCHEMA = 'electronicresourcelibrary' AND
			COLUMNS.TABLE_NAME NOT IN ("fields_info_view", "tables", "views_info") AND
			COLUMNS.COLUMN_NAME <> "deleted"
		ORDER BY COLUMNS.TABLE_NAME;
	
-- 用户表
	DROP TABLE IF EXISTS users;
	CREATE TABLE users (
		id BIGINT(12) UNSIGNED PRIMARY KEY COMMENT 'ID',
		pwd VARCHAR(64) NOT NULL DEFAULT '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92' COMMENT '密码', -- 123456
		last_login_ip VARCHAR(15) DEFAULT NULL COMMENT '最后登录IP',
		last_login_time DATETIME DEFAULT NULL COMMENT '最后登录时间',
		add_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
		bind_email VARCHAR(30) DEFAULT NULL COMMENT '绑定邮箱',
		bind_phone VARCHAR(11) DEFAULT NULL COMMENT '绑定手机',
		is_init BOOL NOT NULL DEFAULT FALSE COMMENT '是否初始化'
	) COMMENT '管理用户的表';
	
	INSERT INTO users(id) VALUES(103501);
	INSERT INTO users(id) VALUES(103510);

-- 角色表
	DROP TABLE IF EXISTS roles;
	CREATE TABLE roles (
		id TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
		name VARCHAR(20) NOT NULL COMMENT '角色名称',
		add_user VARCHAR(20) NOT NULL DEFAULT '系统管理员' COMMENT '创建人',
		level TINYINT UNSIGNED COMMENT '角色级别',
		add_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
		update_time DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
		descrip VARCHAR(100) DEFAULT NULL COMMENT '备注',
		
		deleted TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除'
	) COMMENT '管理角色的表';
	
	INSERT INTO roles(name, level) VALUES('超级管理员', 1);
	INSERT INTO roles(name, level) VALUES('管理员', 2);

-- 用户角色关联表
	DROP TABLE IF EXISTS user_role_assoc;
	CREATE TABLE user_role_assoc (
		uid BIGINT(12) UNSIGNED,
		rid TINYINT UNSIGNED,
		PRIMARY KEY(uid, rid),
		FOREIGN KEY (`uid`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
		FOREIGN KEY (`rid`) REFERENCES `roles` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
	) COMMENT '用户角色关联表';

	INSERT INTO user_role_assoc VALUES(103501, 1);
	INSERT INTO user_role_assoc VALUES(103510, 2);

-- 用户信息表
	DROP TABLE IF EXISTS usermsgs;
	CREATE TABLE usermsgs (
		id BIGINT(12) UNSIGNED PRIMARY KEY COMMENT 'ID',
		name VARCHAR(20) NOT NULL COMMENT '姓名',
		gender TINYINT UNSIGNED DEFAULT NULL COMMENT '性别', -- 1-男; 2-女
		age TINYINT UNSIGNED DEFAULT NULL COMMENT '年龄',
		avatar VARCHAR(255) DEFAULT 'http://erl.wust.edu.cn:8181/images/2a952fddb374b962cbb0c60b3e79245d.png' COMMENT '头像',
		email VARCHAR(50) DEFAULT NULL COMMENT '电子邮箱',
		phone VARCHAR(11) DEFAULT NULL COMMENT '联系方式',
		political_status TINYINT UNSIGNED DEFAULT NULL COMMENT '政治面貌',
		clan TINYINT UNSIGNED DEFAULT NULL COMMENT '民族',
		profile VARCHAR(100) DEFAULT NULL COMMENT '个人简介',
		
		FOREIGN KEY(id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
		CHECK(gender BETWEEN 1 AND 2),
		CHECK(political_status BETWEEN 1 AND 13),
		CHECK(clan BETWEEN 1 AND 56)
	) COMMENT '管理用户基本信息的表';
	
	INSERT INTO usermsgs(id, name) VALUES(103501, '李春华');
	INSERT INTO usermsgs(id, name) VALUES(103510, '李强');
	
-- 用户信息视图
	CREATE OR REPLACE VIEW usermsgs_view AS
		SELECT
			usermsgs.*,
			GROUP_CONCAT(roles.name SEPARATOR ',') 'roles',
			users.add_time,
			REPLACE(users.bind_email, SUBSTR(users.bind_email, 4, 6), '*****') AS bind_email,
			REPLACE(users.bind_phone, SUBSTR(users.bind_phone, 4, 4), '****') AS bind_phone
		FROM users
		JOIN usermsgs ON users.id = usermsgs.id
		JOIN user_role_assoc ON usermsgs.id = user_role_assoc.uid
		JOIN roles ON user_role_assoc.rid = roles.id
		GROUP BY users.id;
		
-- 日志表
	DROP TABLE IF EXISTS logs;
	CREATE TABLE logs (
		id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		uid BIGINT(12) UNSIGNED COMMENT '请求用户ID',
		type ENUM('POST', 'GET', 'PUT', 'DELETE') NOT NULL COMMENT '请求类型',
		title VARCHAR(20) NOT NULL COMMENT '名称',
		url VARCHAR(255) NOT NULL COMMENT '地址',
		params JSON COMMENT '参数',
		ip VARCHAR(15) COMMENT '请求IP',
		date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '请求时间',
		
		FOREIGN KEY(uid) REFERENCES users(id) ON UPDATE CASCADE ON DELETE RESTRICT
	);
		
-- 权限表
	DROP TABLE IF EXISTS permissions;
	CREATE TABLE permissions (
		id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		name VARCHAR(50) NOT NULL COMMENT '名称',
		icon VARCHAR(30) DEFAULT NULL COMMENT '图标',
		type TINYINT UNSIGNED NOT NULL COMMENT '类型', -- 1-菜单; 2-选项; 3-标签; 4-操作;
		status BOOL DEFAULT TRUE COMMENT '状态',
		url VARCHAR(255) NOT NULL DEFAULT '#' COMMENT '地址',
		descrip VARCHAR(100) DEFAULT NULL COMMENT '备注',
		pid SMALLINT UNSIGNED DEFAULT NULL COMMENT '父权限ID',
		oper_type TINYINT UNSIGNED DEFAULT NULL COMMENT '操作方式', -- 仅对type=oper时有效, 1代表只能批量操作, 2代表只能单一操作, 3代表既能批量也能单一操作, 
		add_user VARCHAR(20) NOT NULL DEFAULT '系统管理员' COMMENT '创建人',
		add_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
		update_time DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
		
		FOREIGN KEY(pid) REFERENCES permissions(id) ON UPDATE CASCADE ON DELETE CASCADE,
		CHECK(type BETWEEN 1 AND 4),
		CHECK(oper_type BETWEEN 1 AND 3),
		INDEX(url)
	) COMMENT '管理权限目录的表';

-- 角色、权限关联表
	DROP TABLE IF EXISTS role_perm_assoc;
	CREATE TABLE role_perm_assoc (
		rid TINYINT UNSIGNED,
		pid SMALLINT UNSIGNED,
		PRIMARY KEY(rid, pid),
		FOREIGN KEY(rid) REFERENCES roles(id) ON DELETE CASCADE ON UPDATE CASCADE,
		FOREIGN KEY(pid) REFERENCES permissions(id) ON DELETE CASCADE ON UPDATE CASCADE
	) COMMENT '角色权限关联表';
	
	CREATE TRIGGER `on_permission_insert` AFTER INSERT ON `permissions` 
	FOR EACH ROW
	INSERT INTO role_perm_assoc VALUES(1, NEW.id);
	
-- 用户权限关联表
	DROP TABLE IF EXISTS user_perm_assoc;
	CREATE TABLE user_perm_assoc (
		uid BIGINT(12) UNSIGNED,
		pid SMALLINT UNSIGNED,
		authorize BOOL DEFAULT TRUE NOT NULL,
		PRIMARY KEY(uid, pid),
		FOREIGN KEY(uid) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
		FOREIGN KEY(pid) REFERENCES permissions(id) ON DELETE CASCADE ON UPDATE CASCADE
	) COMMENT '用户权限关联表';

-- DROP TABLE IF EXISTS departments;
-- CREATE TABLE departments (
	-- dep_id CHAR(10) PRIMARY KEY,
	-- dep_name VARCHAR(50) NOT NULL
-- );

-- 专业表
	DROP TABLE IF EXISTS majors;
	CREATE TABLE majors (
		id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		number VARCHAR(10) UNIQUE NOT NULL COMMENT '专业代码',
		name VARCHAR(20) NOT NULL COMMENT '专业名称',
		descrip VARCHAR(100) DEFAULT NULL COMMENT '描述',
		add_user VARCHAR(20) NOT NULL DEFAULT '系统管理员' COMMENT '添加人',
		add_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间'
		
		-- dep_id CHAR(10),
		-- FOREIGN KEY(dep_id) REFERENCES departments(dep_id),
	) COMMENT '管理专业的表';
	
-- 教师表
 	DROP TABLE IF EXISTS teachers;
	CREATE TABLE teachers (
 		id BIGINT(12) UNSIGNED PRIMARY KEY COMMENT '职工号',
 		level TINYINT UNSIGNED NOT NULL COMMENT '职位', -- 1-教授; 2-副教授; 3-讲师; 4-助教;
		degree TINYINT UNSIGNED COMMENT '学历',-- 1-博士研究生; 2-硕士研究生; 3-本科生; 4-博士研究生毕业; 5-硕士研究生毕业; 6-本科毕业; 7-硕士研究生肄业; 8-硕士研究生结业
		bachelor TINYINT UNSIGNED COMMENT '学位', -- 1-工学博士; 2-工学硕士; 3-工学学士; 4-法学博士; 5-法学硕士; 6-法学学士; 7-哲学博士; 8-哲学硕士; 9-哲学学士; 10-文学博士; 11-文学硕士; 12-文学学士; 13-教育学博士; 14-教育学硕士; 15-教育学学士; 16-管理学博士; 17-管理学硕士; 18-管理学学士; 19-理学博士; 20-理学硕士; 21-理学学士; 22-医学博士; 23-医学硕士; 24-医学学士; 25-农学博士; 26-农学硕士; 27-农学学士; 28-艺术学博士; 29-艺术学硕士; 30-艺术学学士; 31-经济学博士; 32-经济学硕士; 33-经济学学士; 34-历史学博士; 35-历史学硕士; 36-历史学学士; 37-体育硕士专业; 38-工程硕士专业; 39-资产评估硕士专业; 40-公共管理硕士专业; 41-军事学博士; 42-军事学硕士; 43-工商管理硕士专业; 44-翻译硕士专业; 45-应用心理学硕士专业; 46-临床医学博士专业; 47-博士; 48-硕士, 49-学士
		is_tutor BOOLEAN COMMENT '是否是导师',

 		-- dep_id CHAR(10),
 		-- FOREIGN KEY(dep_id) REFERENCES departments(dep_id),
 		
		FOREIGN KEY(id) REFERENCES users(id) ON DELETE RESTRICT ON UPDATE CASCADE,
		CHECK(level BETWEEN 1 AND 4),
		CHECK(degree BETWEEN 1 AND 8),
		CHECK(bachelor BETWEEN 1 AND 49)
 	) COMMENT '管理教师信息的表';
	
		
	
-- 教师视图
	CREATE OR REPLACE VIEW teacmsgs_view AS
		SELECT 
			teachers.id, 
			usermsgs.name, 
			usermsgs.age, 
			usermsgs.gender, 
			usermsgs.email, 
			usermsgs.phone,
			teachers.level,
			teachers.degree,
			teachers.bachelor,
			teachers.is_tutor
		FROM users
		JOIN usermsgs ON users.id = usermsgs.id
		JOIN teachers ON usermsgs.id = teachers.id
		WITH CHECK OPTION;

-- 学生表
	DROP TABLE IF EXISTS students;
	CREATE TABLE students (
		id BIGINT(12) UNSIGNED PRIMARY KEY COMMENT 'ID/学籍号',
		mid SMALLINT UNSIGNED NOT NULL COMMENT '专业ID',
		class_name VARCHAR(10) NOT NULL COMMENT '班级',
		degree TINYINT UNSIGNED NOT NULL COMMENT '学历', -- 1-博士研究生; 2-硕士研究生; 3-本科生;
		session SMALLINT NOT NULL COMMENT '所在年级',
		tid BIGINT(12) UNSIGNED DEFAULT NULL COMMENT '导师ID',
		
		-- dep_id CHAR(10),
		-- FOREIGN KEY(dep_id) REFERENCES departments(dep_id),
		CHECK(degree BETWEEN 0 AND 4),
		FOREIGN KEY(`id`) REFERENCES `users`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
		FOREIGN KEY(`mid`) REFERENCES `majors`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
		FOREIGN KEY(`tid`) REFERENCES `teachers`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE
	) COMMENT '管理学生信息的表';
	
	DROP TRIGGER IF EXISTS `on_before_student_insert`;
	CREATE TRIGGER `on_before_student_insert` BEFORE INSERT ON students
	FOR EACH ROW
	BEGIN
		IF NEW.session < 1949 OR NEW.session > YEAR(CURDATE()) THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'session must between 1949 and cur year';
		END IF;
	END;

-- 学生信息视图
	CREATE OR REPLACE VIEW stumsgs_view AS
		SELECT 
			usermsgs.id, 
			usermsgs.name, 
			usermsgs.gender, 
			usermsgs.age,
			usermsgs.email, 
			usermsgs.phone,
			majors.name AS major, 
			students.class_name AS class,
			students.degree,
			students.session
		FROM usermsgs
		JOIN students ON usermsgs.id = students.id
		JOIN majors ON students.mid = majors.id;
		
-- 研究生信息视图
	CREATE OR REPLACE VIEW postmsgs_view AS
		SELECT
			students.id,
			stu_msg.name user_name,
			stu_msg.age,
			stu_msg.gender,
			stu_msg.email,
			stu_msg.phone,
			majors.name major_name,
			students.session, 
			teac_msg.name tutor_name
		FROM usermsgs stu_msg
		JOIN students ON stu_msg.id = students.id
		JOIN majors ON students.mid = majors.id
		JOIN usermsgs teac_msg ON students.tid = teac_msg.id
		WHERE students.tid is not null
		WITH CHECK OPTION;
		
-- 导师视图
	CREATE OR REPLACE VIEW tutmsgs_view AS 
		SELECT DISTINCT
				teachers.id, 
				usermsgs.name,
				usermsgs.age,
				usermsgs.gender, 
				usermsgs.email, 
				usermsgs.phone,
				teachers.level,
				teachers.degree,
				teachers.bachelor
		FROM usermsgs
		JOIN teachers ON usermsgs.id = teachers.id
		WHERE teachers.is_tutor = 1;

-- 教材表
	DROP TABLE IF EXISTS textbooks;
	CREATE TABLE textbooks (
		id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		number VARCHAR(20) NOT NULL UNIQUE COMMENT '编号',
		name VARCHAR(50) NOT NULL COMMENT '名称',
		cover VARCHAR(255) DEFAULT NULL COMMENT '封面',
		type VARCHAR(20) NOT NULL COMMENT '类别',
		author VARCHAR(20) NOT NULL COMMENT '作者',
		publisher VARCHAR(50) NOT NULL COMMENT '出版社',
		isbn VARCHAR(17) NOT NULL UNIQUE COMMENT 'ISBN',
		price FLOAT UNSIGNED COMMENT '价格',
		add_user VARCHAR(20) NOT NULL DEFAULT '系统管理员' COMMENT '添加人',
		add_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
		deleted TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除'
	);

	DROP TABLE IF EXISTS book_reservations;
	CREATE TABLE book_reservations (
		id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		uid BIGINT(12) UNSIGNED NOT NULL COMMENT '用户ID',
		tid INT UNSIGNED NOT NULL COMMENT '教材ID',
		add_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '预订时间',
		amount TINYINT UNSIGNED NOT NULL COMMENT '预订数量',
		role VARCHAR(20) COMMENT '身份',
		message VARCHAR(100) COMMENT '预定留言',
		audit_status TINYINT NOT NULL DEFAULT 0 COMMENT '预订状态',
		comment VARCHAR(100) COMMENT '审核意见',
		audit_time DATETIME COMMENT '审核时间',
		audit_user VARCHAR(20) COMMENT '审核人',
		deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
		
		FOREIGN KEY (uid) REFERENCES users(id) ON DELETE RESTRICT ON UPDATE CASCADE,
		FOREIGN KEY (tid) REFERENCES textbooks(id) ON DELETE RESTRICT ON UPDATE CASCADE
	);
	
	CREATE OR REPLACE VIEW book_reservations_view AS
		SELECT 
			book_reservations.*,
			usermsgs.name add_user,
			textbooks.name book_name,
			textbooks.cover,
			textbooks.type,
			textbooks.author,
			textbooks.publisher
		FROM book_reservations
		JOIN usermsgs ON usermsgs.id = book_reservations.uid
		JOIN textbooks ON book_reservations.tid = textbooks.id
		WITH CHECK OPTION;

-- 学科建设表
	DROP TABLE IF EXISTS disciplines;
	CREATE TABLE disciplines (
		id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		number VARCHAR(10) UNIQUE NOT NULL COMMENT '学科代码',
		name VARCHAR(20) NOT NULL COMMENT '名称',
		descrip VARCHAR(100) COMMENT '备注',
		add_user VARCHAR(20) NOT NULL DEFAULT '系统管理员' COMMENT '添加人',
		add_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间'
	);

-- 学位点建设
	DROP TABLE IF EXISTS degrees;
	CREATE TABLE degrees (
		id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		number VARCHAR(10) UNIQUE NOT NULL COMMENT '学位点代码',
		name VARCHAR(20) NOT NULL COMMENT '名称',
		descrip VARCHAR(100) COMMENT '描述',
		add_user VARCHAR(20) NOT NULL DEFAULT '系统管理员' COMMENT '添加人',
		add_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间'
	);

-- 课程表
	DROP TABLE IF EXISTS courses;
	CREATE TABLE courses (
		id SMALLINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		number VARCHAR(10) UNIQUE NOT NULL COMMENT '课程代码',
		name VARCHAR(20) NOT NULL COMMENT '名称',
		category TINYINT UNSIGNED NOT NULL COMMENT '类型', -- 1-学科基础平台课程; 2-通识教育平台课程; 3-实践教学模块; 4-专业任选课程; 5-专业核心课程
		unit VARCHAR(20) NOT NULL COMMENT '开课单位',
		credit FLOAT NOT NULL COMMENT '学分',
		
		theo_hours TINYINT UNSIGNED COMMENT '理论学时',
		exp_hours TINYINT UNSIGNED COMMENT '实验学时',
		comp_hours TINYINT UNSIGNED COMMENT '上机学时',
		prac_hours TINYINT UNSIGNED COMMENT '实践学时',
		
		ass_method TINYINT UNSIGNED NOT NULL COMMENT '考核方式', -- 1-考试; 2-考核;
		nature TINYINT UNSIGNED NOT NULL COMMENT '修读性质', -- 1-必修; 2-选修;
		
		add_user VARCHAR(20) NOT NULL DEFAULT '系统管理员' COMMENT '添加人',
		add_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
		
		deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
		
		CHECK(category BETWEEN 1 AND 5),
		CHECK(ass_method BETWEEN 1 AND 2),
		CHECK(nature BETWEEN 1 AND 2),
		CHECK(COALESCE(theo_hours, exp_hours, comp_hours, prac_hours) IS NOT NULL)
	);
	
-- 本科教学资料表
	DROP TABLE IF EXISTS teaching_maters;
	CREATE TABLE teaching_maters (
		id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		`group` VARCHAR(20) NOT NULL COMMENT '分组',
		category VARCHAR(64) NOT NULL COMMENT '类型',
		file_name VARCHAR(255) UNIQUE NOT NULL COMMENT '文件名',
		file_size INT UNSIGNED NOT NULL COMMENT '文件大小',
		file_addr VARCHAR(255) NOT NULL COMMENT '文件地址',
		add_uid BIGINT(12) UNSIGNED NOT NULL COMMENT '上传人',
		add_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
		download_times INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '下载次数',
		
		FOREIGN KEY(add_uid) REFERENCES users(id),
		UNIQUE INDEX(add_uid, file_name)
	) COMMENT '教学资料';
	
	CREATE OR REPLACE VIEW maters_view AS
		SELECT 
			teaching_maters.id,
			`group`,
			category,
			file_name,
			file_size,
			usermsgs.`name` AS upload_user,
			add_time AS upload_time,
			download_times
		FROM teaching_maters
		JOIN usermsgs ON teaching_maters.add_uid = usermsgs.id;

-- 实验室表
	DROP TABLE IF EXISTS laboratories;
	CREATE TABLE laboratories (
		id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		number VARCHAR(20) NOT NULL UNIQUE COMMENT '实验室编号',
		name VARCHAR(50) NOT NULL COMMENT '名称',
		capacity TINYINT NOT NULL COMMENT '容量',
		addr VARCHAR(20) NOT NULL COMMENT '位置',
		commander VARCHAR(20) DEFAULT NULL COMMENT '负责人',
		add_user VARCHAR(20) NOT NULL DEFAULT '系统管理员' COMMENT '添加人',
		add_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
		deleted BOOL NOT NULL DEFAULT FALSE COMMENT '是否删除'
	);
	
	DROP TABLE IF EXISTS laboratory_reservations;
	CREATE TABLE laboratory_reservations(
		id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		lab_id SMALLINT UNSIGNED NOT NULL COMMENT '实验室ID',
		add_uid BIGINT(12) UNSIGNED NOT NULL COMMENT '预约人ID',
		day DATE NOT NULL COMMENT '预约日期',
		start TIME NOT NULL COMMENT '开始时间',
		end TIME NOT NULL COMMENT '结束时间',
		message VARCHAR(100) DEFAULT NULL COMMENT '留言',
		add_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '预约时间',
		audit_status TINYINT NOT NULL DEFAULT 0 COMMENT '审核状态',
		audit_time DATETIME DEFAULT NULL COMMENT '审核时间',
		audit_user VARCHAR(20) DEFAULT NULL COMMENT '审核人',
		comment VARCHAR(100) DEFAULT NULL COMMENT '意见',

		FOREIGN KEY(lab_id) REFERENCES laboratories(id) ON DELETE RESTRICT ON UPDATE CASCADE,
		FOREIGN KEY(add_uid) REFERENCES users(id) ON DELETE RESTRICT ON UPDATE CASCADE
	);

	CREATE OR REPLACE VIEW laboratory_reservations_view AS
		SELECT 
			laboratory_reservations.id,
			laboratories.number,
			laboratories.name,
			laboratories.capacity,
			laboratories.addr,
			laboratories.commander,
			usermsgs.`name` AS add_user,
			laboratory_reservations.day,
			laboratory_reservations.start,
			laboratory_reservations.end,
			laboratory_reservations.message,
			laboratory_reservations.add_time,
			laboratory_reservations.audit_status,
			laboratory_reservations.audit_time,
			laboratory_reservations.audit_user,
			laboratory_reservations.comment
		FROM laboratory_reservations
		JOIN laboratories ON laboratories.id = laboratory_reservations.lab_id
		JOIN usermsgs ON laboratory_reservations.add_uid = usermsgs.id;
	
-- 实习任务表
	DROP TABLE IF EXISTS internships;
	CREATE TABLE internships (
		id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		name VARCHAR(50) NOT NULL COMMENT '名称',
		unit_name VARCHAR(50) NOT NULL COMMENT '实习单位',
		unit_descrip VARCHAR(100) DEFAULT NULL COMMENT '单位简介',
		start_date DATE NOT NULL COMMENT '实习开始时间',
		days SMALLINT UNSIGNED NOT NULL COMMENT '实习时长',
		max_app_num SMALLINT UNSIGNED NOT NULL COMMENT '最大申请人数',
		deadline DATE NOT NULL COMMENT '申请截止时间',
		app_num SMALLINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '已申请人数',
		add_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
		add_user VARCHAR(20) NOT NULL DEFAULT '系统管理员' COMMENT '发布人',
		descrip VARCHAR(100) DEFAULT NULL COMMENT '说明',
		
		status TINYINT NOT NULL DEFAULT 1 COMMENT '状态', -- 招募中, 已开始, 进行中, 已取消
		deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
		
		CHECK(status BETWEEN 1 AND 4)
	);

-- 实习申请表
	DROP TABLE IF EXISTS applications;
	CREATE TABLE applications (
		id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		iid INT UNSIGNED NOT NULL COMMENT '实习任务ID',
		add_uid BIGINT(12) UNSIGNED NOT NULL COMMENT '实习人ID',
		status TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态', -- 已申请, 实习中, 已完成, 已取消, 已撤销
		add_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
		
		oper_time DATETIME DEFAULT NULL COMMENT '操作时间',
		reason VARCHAR(50) DEFAULT NULL COMMENT '原因',
		
		FOREIGN KEY (iid) REFERENCES internships(id) ON DELETE RESTRICT ON UPDATE CASCADE,
		FOREIGN KEY (add_uid) REFERENCES users(id) ON DELETE RESTRICT ON UPDATE CASCADE
	);
	
	CREATE OR REPLACE VIEW internship_apps_view AS
		SELECT
			applications.add_uid,
			internships.id AS iid,
			applications.id,
			usermsgs.name AS add_user,
			applications.`status` AS app_status,
			applications.add_time,
			GROUP_CONCAT(roles.`name` SEPARATOR ',') AS roles,
			applications.oper_time,
			applications.reason
		FROM applications
		JOIN internships ON applications.iid = internships.id
		JOIN usermsgs ON applications.add_uid = usermsgs.id
		JOIN user_role_assoc ON usermsgs.id = user_role_assoc.uid
		JOIN roles ON user_role_assoc.rid = roles.id
		GROUP BY applications.id
	;

-- 申报表
	DROP TABLE IF EXISTS declarations;
	CREATE TABLE declarations (
		id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '申报单号',
		add_uid BIGINT(12) UNSIGNED NOT NULL COMMENT '申报人ID',
		ref_id INT UNSIGNED NOT NULL COMMENT '申报所在表id',
		message VARCHAR(200) DEFAULT NULL COMMENT '说明',
		audit_status TINYINT NOT NULL DEFAULT 0 COMMENT '状态', -- 0-未审核; 1-通过; 2-驳回;
		add_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申报时间',
		`comment` VARCHAR(100) DEFAULT NULL COMMENT '意见',
		audit_time DATETIME DEFAULT NULL COMMENT '审核时间',
		audit_user VARCHAR(20) DEFAULT NULL COMMENT '审核人',
		`group` ENUM('Patent', 'Teaching', 'Competition', 'Teacher', 'Project', 'Paper', 'Conference') COMMENT '分组',
		deleted TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
		
		CHECK(audit_status BETWEEN 0 AND 2),
		FOREIGN KEY(add_uid) REFERENCES users(id) ON DELETE RESTRICT ON UPDATE CASCADE
	) COMMENT '管理各种申报的表';
	
	CREATE OR REPLACE VIEW declarations_view AS
		SELECT 
			usermsgs.`name` as add_user,
			declarations.*
		FROM declarations
		JOIN usermsgs ON declarations.add_uid = usermsgs.id;

-- -- 教学成果奖项表
	DROP TABLE IF EXISTS teaching_awards;
	CREATE TABLE teaching_awards (
		id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'id',
		name VARCHAR(50) NOT NULL COMMENT '成果名称',
		type TINYINT UNSIGNED NOT NULL COMMENT '奖项类型', -- 1-国家级研究生教育教学成果奖; 2-国家级高等教育教学成果奖; 3-国家级基础教育教学成果奖; 4-国家级职业教育教学成果奖; 5-省部级相关奖项;
		level TINYINT UNSIGNED NOT NULL COMMENT '奖项等级', -- 1-国家级特等奖; 2-国家级一等奖; 3-国家级二等奖; 4-国家级三等奖; 5-省部级特等奖; 6-省部级一等奖; 7-省部级二等奖; 8-省部级三等奖;
		author VARCHAR(20) NOT NULL COMMENT '成果完成人',
		unit_sig_order TINYINT UNSIGNED NOT NULL COMMENT '单位署名次序',
		comp_sig_order TINYINT UNSIGNED NOT NULL COMMENT '完成人署名次序',
		enclosures VARCHAR(750) DEFAULT NULL COMMENT '附件',
		date DATE NOT NULL COMMENT '获奖时间',
		deleted TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
		
		CHECK(type BETWEEN 1 AND 5),
		CHECK(level BETWEEN 1 AND 8)
	);
	
	CREATE OR REPLACE VIEW teaching_awards_view AS
		SELECT
			teaching_awards.*
		FROM teaching_awards
		LEFT JOIN declarations ON
			`group` = 'Teaching'
			AND teaching_awards.id = declarations.ref_id
		WHERE
			audit_status = 1
			OR ref_id IS NULL
	;
	
-- 竞赛或作品奖项表
	DROP TABLE IF EXISTS competition_awards;
	CREATE TABLE competition_awards (
		id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		award_name VARCHAR(100) NOT NULL COMMENT '奖项名称',
		work_name VARCHAR(100) NOT NULL COMMENT '获奖作品',
		level VARCHAR(20) NOT NULL COMMENT '获奖等级',
		org_name VARCHAR(100) NOT NULL COMMENT '组织单位名称',
		org_type TINYINT UNSIGNED NOT NULL COMMENT '组织单位类型', -- 1-政府; 2-学会; 3-协会; 4其他;
		date DATE NOT NULL COMMENT '获奖时间',
		enclosures VARCHAR(750) DEFAULT NULL COMMENT '附件',
		author VARCHAR(20) NOT NULL COMMENT '获奖人姓名',
		role TINYINT UNSIGNED NOT NULL COMMENT '获奖人角色', -- 1-研究生; 2-教师 
		deleted TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
		
		CHECK(org_type BETWEEN 1 AND 4),
		CHECK(role BETWEEN 1 AND 2)
	);
	
	CREATE OR REPLACE VIEW competition_awards_view AS
		SELECT 
			competition_awards.*
		FROM competition_awards
		LEFT JOIN declarations ON
			competition_awards.id = declarations.ref_id 
			AND (`group` = 'Competition' OR `group` = 'Teacher')
		WHERE 
			audit_status = 1
			OR ref_id IS NULL
	;

-- 学术会议表
	DROP TABLE IF EXISTS academic_conferences;
	CREATE TABLE academic_conferences (
		id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT "ID",
		parti_name VARCHAR(50) NOT NULL COMMENT '参与人姓名',
		name VARCHAR(50) NOT NULL COMMENT '会议名称',
		title VARCHAR(100) NOT NULL COMMENT '报告题目',
		date DATE NOT NULL COMMENT '报告时间',
		enclosures VARCHAR(750) DEFAULT NULL COMMENT '附件',
		location VARCHAR(100) NOT NULL COMMENT '报告地点',
		deleted TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除'
	) COMMENT '学生参见本领域国内外重要学术会议表(研究生)';
	
	CREATE OR REPLACE VIEW conferences_view AS
		SELECT
			academic_conferences.*
		FROM academic_conferences
		LEFT JOIN declarations ON 
			`group` = 'Conference'
			AND academic_conferences.id = declarations.ref_id
		WHERE
			audit_status = 1
			OR ref_id IS NULL
	;

-- 论文
	DROP TABLE IF EXISTS academic_papers;
	CREATE TABLE academic_papers (
		id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		unique_id CHAR(32) UNIQUE NOT NULL COMMENT '论文标识',
		title VARCHAR(50) NOT NULL COMMENT '论文标题',
		pub_jour VARCHAR(50) NOT NULL COMMENT '发表期刊',
		pub_year YEAR NOT NULL COMMENT '发表年份',
		pub_vol TINYINT UNSIGNED NOT NULL COMMENT '发表卷数',
		pub_term TINYINT UNSIGNED NOT NULL COMMENT '发表期数',
		inc TINYINT UNSIGNED NOT NULL COMMENT '期刊收录情况', -- 1-CSSCI; 2-CSCD; 3-SCI; 4-SSCI; 5-EI; 6-A&HCI; 7-其他;
		disc_id SMALLINT UNSIGNED NOT NULL COMMENT '学科',
		enclosures VARCHAR(750) DEFAULT NULL COMMENT '附件',
		deleted TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
		
		CHECK(inc BETWEEN 0 AND 8),
		FOREIGN KEY(disc_id) REFERENCES disciplines(id) ON DELETE RESTRICT ON UPDATE CASCADE
	) COMMENT '国内外重要期刊发明的代表性论文';
	
	DROP TABLE IF EXISTS paper_author_assoc;
	CREATE TABLE paper_author_assoc (
		id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID' COMMENT 'ID',
		pid INT UNSIGNED NOT NULL COMMENT '论文ID',
		uid BIGINT(12) UNSIGNED DEFAULT NULL COMMENT '用户ID',
		name VARCHAR(20) DEFAULT NULL COMMENT '用户姓名',
		`order` TINYINT UNSIGNED NOT NULL COMMENT '署名次序',
		role TINYINT UNSIGNED NOT NULL COMMENT '获奖人角色', -- 1-研究生; 2-教师;
		is_corresponding BOOL NOT NULL DEFAULT FALSE COMMENT '是否通讯作者',
		
		CHECK(`order` BETWEEN 1 AND 4),
		CHECK(role BETWEEN 1 AND 2),
		FOREIGN KEY(pid) REFERENCES academic_papers(id) ON DELETE RESTRICT ON UPDATE CASCADE,
		FOREIGN KEY(uid) REFERENCES users(id) ON DELETE RESTRICT ON UPDATE CASCADE
	) COMMENT '论文作者关联表';
	
	CREATE OR REPLACE VIEW papers_author_view AS
		SELECT
			academic_papers.id,
			academic_papers.title,
			paper_author_assoc.uid,
			(CASE -- 姓名
				WHEN paper_author_assoc.`name` is NULL THEN
					usermsgs.`name`
				ELSE
					paper_author_assoc.`name`
			END) as author,
			paper_author_assoc.role,
			paper_author_assoc.`order`,
			paper_author_assoc.is_corresponding,
			academic_papers.pub_jour,
			academic_papers.pub_year,
			academic_papers.pub_vol,
			academic_papers.pub_term,
			academic_papers.inc,
			disciplines.name as disp,
			academic_papers.enclosures,
			academic_papers.deleted,
			declarations.audit_status
		FROM academic_papers
		JOIN paper_author_assoc ON 
			academic_papers.id = paper_author_assoc.pid
		JOIN disciplines ON 
			academic_papers.disc_id = disciplines.id
		LEFT JOIN usermsgs ON 
			paper_author_assoc.uid = usermsgs.id
		LEFT JOIN declarations ON 
			`group` = 'Paper'
			AND academic_papers.id = declarations.ref_id
	;
	
	CREATE OR REPLACE VIEW papers_view AS
		SELECT
			papers_author_view.id,
			papers_author_view.title,
			GROUP_CONCAT(
				CONCAT(
					papers_author_view.author,
					(CASE -- 是否通讯作者
						WHEN papers_author_view.is_corresponding = 1 THEN
							'(通讯作者):'
						ELSE
							':'
					END),
					papers_author_view.role
				)
				ORDER BY papers_author_view.`order` SEPARATOR ","
			) AS authors,
			papers_author_view.pub_jour,
			papers_author_view.pub_year,
			papers_author_view.pub_vol,
			papers_author_view.pub_term,
			papers_author_view.inc,
			papers_author_view.disp,
			papers_author_view.enclosures,
			papers_author_view.deleted,
			MIN(papers_author_view.audit_status) as audit_status
		FROM papers_author_view
		GROUP BY papers_author_view.id
	;
	
-- 项目
	DROP TABLE IF EXISTS projects;
	CREATE TABLE projects (
		id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		name VARCHAR(100) NOT NULL COMMENT '项目名称',
		author VARCHAR(50) NOT NULL COMMENT '作者',
		auth_order TINYINT UNSIGNED NOT NULL COMMENT '作者排序',
		source VARCHAR(50) NOT NULL COMMENT '项目来源',
		start_date DATE NOT NULL COMMENT '起始时间',
		end_date DATE NOT NULL COMMENT '截止时间',
		tot_funding INT UNSIGNED NOT NULL COMMENT '项目总金额',
		rec_funding INT UNSIGNED NOT NULL COMMENT '到账金额',
		type VARCHAR(10) NOT NULL COMMENT '项目类型',
		enclosures VARCHAR(750) DEFAULT NULL COMMENT '附件',
		category TINYINT UNSIGNED NOT NULL COMMENT '分组', -- 1-科研项目; 2-教研项目;
		deleted TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
		
		CHECK(auth_order BETWEEN 0 AND 5),
		CHECK(category BETWEEN 0 AND 3)
	);

	CREATE OR REPLACE VIEW projects_view AS
		SELECT
			projects.*
		FROM projects
		LEFT JOIN declarations ON
			`group` = 'Project'
			AND projects.id = declarations.ref_id
		WHERE 
			audit_status = 1
			OR ref_id IS NULL
	;

-- 专利
	DROP TABLE IF EXISTS patents;
	CREATE TABLE patents (
		id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		name VARCHAR(50) COMMENT '名称',
		author VARCHAR(20) NOT NULL COMMENT '作者',
		number VARCHAR(20) NOT NULL COMMENT '专利号',
		pub_date DATE NOT NULL COMMENT '申请时间',
		type TINYINT UNSIGNED NOT NULL COMMENT '类型', -- 1-发明; 2-实用新型; 3-外观设计;
		status TINYINT UNSIGNED NOT NULL COMMENT '状态', -- 1-申请; 2-受理; 3-初审合格; 4-实审; 5-公布; 6-审查意见; 7-授权; 8-下证;
		enclosures VARCHAR(750) DEFAULT NULL COMMENT '附件',
		auth_date DATE DEFAULT NULL COMMENT '授权时间',
		deleted TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
		
		CHECK(type BETWEEN 0 AND 4),
		CHECK(status BETWEEN 0 AND 9)
	);

	CREATE OR REPLACE VIEW patents_view AS
		SELECT
			patents.*
		FROM patents
		LEFT JOIN declarations ON
			`group` = 'Patent'
			AND patents.id = declarations.ref_id
		WHERE 
			audit_status = 1
			OR ref_id IS NULL
	;

-- 删除
-- 	DROP TABLE IF EXISTS logs;
-- 	DROP TABLE IF EXISTS user_role_assoc;
-- 	DROP TABLE IF EXISTS roles;
-- 	DROP TABLE IF EXISTS usermsgs;
-- 	DROP TABLE IF EXISTS users;
-- 	
-- 	DROP VIEW IF EXISTS user_msgs;