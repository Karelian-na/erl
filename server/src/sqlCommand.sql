-- 数据库视图
	CREATE OR REPLACE VIEW TABLE_INFO AS
		SELECT *
		FROM information_schema.`TABLES`
		WHERE TABLE_SCHEMA = 'electronicresourcelibrary' AND TABLE_NAME != 'TABLE_INFO' AND TABLE_NAME != "fields";
		
	CREATE OR REPLACE VIEW tables AS
		SELECT TABLE_NAME, CREATE_TIME, UPDATE_TIME, TABLE_COMMENT 
		FROM TABLE_INFO
		WHERE TABLE_TYPE = "BASE TABLE";
		
	CREATE OR REPLACE VIEW views AS
		SELECT TABLE_NAME, CREATE_TIME, UPDATE_TIME, TABLE_COMMENT 
		FROM TABLE_INFO
		WHERE TABLE_TYPE = "VIEW";
		
	CREATE OR REPLACE VIEW fields AS
		SELECT TABLE_NAME, COLUMN_NAME, COLUMN_COMMENT, COLUMN_TYPE, COLUMN_DEFAULT
		FROM information_schema.COLUMNS
		WHERE TABLE_SCHEMA = 'electronicresourcelibrary';
	
-- 模糊查询关键字表
	DROP TABLE IF EXISTS table_query_keywords;
	CREATE TABLE table_query_keywords (
		table_name VARCHAR(50),
		real_name VARCHAR(50),
		PRIMARY KEY(table_name, real_name)
	) COMMENT '管理各表可查询字段的表';
	
	INSERT INTO table_query_keywords VALUES('stu_msgs', 'id');
	INSERT INTO table_query_keywords VALUES('stu_msgs', 'name');
	INSERT INTO table_query_keywords VALUES('stu_msgs', 'email');
	INSERT INTO table_query_keywords VALUES('stu_msgs', 'phone');
	INSERT INTO table_query_keywords VALUES('stu_msgs', 'major');
	INSERT INTO table_query_keywords VALUES('stu_msgs', 'class_name');
	
	INSERT INTO table_query_keywords VALUES('teac_msgs', 'id');
	INSERT INTO table_query_keywords VALUES('teac_msgs', 'name');
	INSERT INTO table_query_keywords VALUES('teac_msgs', 'email');
	INSERT INTO table_query_keywords VALUES('teac_msgs', 'phone');
	
	INSERT INTO table_query_keywords VALUES('permissions', 'id');
	INSERT INTO table_query_keywords VALUES('permissions', 'name');
	INSERT INTO table_query_keywords VALUES('permissions', 'url');
	
	INSERT INTO table_query_keywords VALUES('user_msgs', 'id');
	INSERT INTO table_query_keywords VALUES('user_msgs', 'name');
	INSERT INTO table_query_keywords VALUES('user_msgs', 'phone');
	INSERT INTO table_query_keywords VALUES('user_msgs', 'email');
	
	INSERT INTO table_query_keywords VALUES('syllabus', 'file_name');
	INSERT INTO table_query_keywords VALUES('syllabus', 'upload_time');
	INSERT INTO table_query_keywords VALUES('syllabus', 'upload_user');
	
	INSERT INTO table_query_keywords VALUES('tables', 'TABLE_NAME');
	INSERT INTO table_query_keywords VALUES('tables', 'TABLE_COMMENT');
	
	INSERT INTO table_query_keywords VALUES('fields', 'COLUMN_NAME');
	INSERT INTO table_query_keywords VALUES('fields', 'COLUMN_COMMENT');

	DROP TABLE IF EXISTS icons;
	CREATE TABLE icons (
		id SMALLINT PRIMARY KEY AUTO_INCREMENT COMMENT 'id',
		name VARCHAR(50) COMMENT '名称'
	) COMMENT '图标表';
	
	INSERT INTO icons(name) VALUES('under-graduate');
	INSERT INTO icons(name) VALUES('post-graduate');
	INSERT INTO icons(name) VALUES('practice');
	INSERT INTO icons(name) VALUES('construction');
	INSERT INTO icons(name) VALUES('information');
	INSERT INTO icons(name) VALUES('affair');
	INSERT INTO icons(name) VALUES('comprehensive');

-- 角色表
	DROP TABLE IF EXISTS roles;
	CREATE TABLE roles (
		id TINYINT PRIMARY KEY COMMENT '角色ID',
		name VARCHAR(20) NOT NULL COMMENT '角色名称',
		create_user VARCHAR(20) COMMENT '创建人',
		create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
		update_time DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
		descrip VARCHAR(200) DEFAULT NULL COMMENT '备注'
	) COMMENT '管理角色的表';
	
	INSERT INTO roles(id, name, create_user) VALUES(1, '超级管理员', '李春华');
	INSERT INTO roles(id, name, create_user) VALUES(2, '管理员', '李春华');
	INSERT INTO roles(id, name, create_user) VALUES(3, '教师', '李春华');
	INSERT INTO roles(id, name, create_user) VALUES(4, '研究生', '李春华');
	INSERT INTO roles(id, name, create_user) VALUES(5, '本科生', '李春华');

	
-- 用户表
	DROP TABLE IF EXISTS users;
	CREATE TABLE users (
		id BIGINT(12) UNSIGNED PRIMARY KEY COMMENT 'ID',
		pwd VARCHAR(64) NOT NULL DEFAULT '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92' COMMENT '密码',
		last_login_ip VARCHAR(15) DEFAULT NULL COMMENT '最后登录IP',
		last_login_session VARCHAR(64) DEFAULT NULL COMMENT '最后登录session',
		last_login_date DATETIME DEFAULT NULL COMMENT '最后登录时间',
		reg_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间'
	) COMMENT '管理用户的表';


	INSERT INTO users(id) values(201907119001);
	INSERT INTO users(id) values(201907119002);
	INSERT INTO users(id) values(201907119003);
	INSERT INTO users(id) values(201907119004);
	INSERT INTO users(id) values(201907119005);
	INSERT INTO users(id) values(201907119006);
	INSERT INTO users(id) values(201907119007);
	INSERT INTO users(id) values(201907119008);
	INSERT INTO users(id) values(201907119009);
	INSERT INTO users(id) values(201907119010);
	
	INSERT INTO users(id) values(1000012201);
	INSERT INTO users(id) values(1000012202);
	INSERT INTO users(id) values(1000012203);
	INSERT INTO users(id) values(1000012204);
	INSERT INTO users(id) values(1000012205);
	INSERT INTO users(id) values(1000012206);
	INSERT INTO users(id) values(1000012207);
	INSERT INTO users(id) values(1000012208);
	INSERT INTO users(id) values(1000012209);
	INSERT INTO users(id) values(1000012210);
	
	INSERT INTO users(id) values(10342701);
	INSERT INTO users(id) values(10342702);
	INSERT INTO users(id) values(10342703);
	INSERT INTO users(id) values(10342704);
	INSERT INTO users(id) values(10342705);
	INSERT INTO users(id) values(10342706);
	INSERT INTO users(id) values(10342707);
	INSERT INTO users(id) values(10342708);
	INSERT INTO users(id) values(10342709);
	INSERT INTO users(id) values(10342710);
	
	INSERT INTO users(id) values(103501);
	INSERT INTO users(id) values(103502);
	INSERT INTO users(id) values(103503);
	INSERT INTO users(id) values(103504);
	INSERT INTO users(id) values(103505);
	INSERT INTO users(id) values(103506);
	INSERT INTO users(id) values(103507);
	INSERT INTO users(id) values(103508);
	INSERT INTO users(id) values(103509);
	INSERT INTO users(id) values(103510);

	DROP TABLE IF EXISTS user_role_assoc;
	CREATE TABLE user_role_assoc (
		uid BIGINT(12) UNSIGNED,
		rid TINYINT,
		PRIMARY KEY(uid, rid),
		FOREIGN KEY (`uid`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
		FOREIGN KEY (`rid`) REFERENCES `roles` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
	) COMMENT '用户角色关联表';

	INSERT INTO user_role_assoc VALUES(103501, 1);
	INSERT INTO user_role_assoc VALUES(103502, 1);
	INSERT INTO user_role_assoc VALUES(103503, 1);
	INSERT INTO user_role_assoc VALUES(103504, 1);
	INSERT INTO user_role_assoc VALUES(103505, 1);
	INSERT INTO user_role_assoc VALUES(103506, 1);
	INSERT INTO user_role_assoc VALUES(103507, 1);
	INSERT INTO user_role_assoc VALUES(103508, 1);
	INSERT INTO user_role_assoc VALUES(103509, 1);
	INSERT INTO user_role_assoc VALUES(103510, 1);
	INSERT INTO user_role_assoc VALUES(10342701, 2);
	INSERT INTO user_role_assoc VALUES(10342702, 2);
	INSERT INTO user_role_assoc VALUES(10342703, 2);
	INSERT INTO user_role_assoc VALUES(10342704, 2);
	INSERT INTO user_role_assoc VALUES(10342705, 2);
	INSERT INTO user_role_assoc VALUES(10342706, 2);
	INSERT INTO user_role_assoc VALUES(10342707, 2);
	INSERT INTO user_role_assoc VALUES(10342708, 2);
	INSERT INTO user_role_assoc VALUES(10342709, 2);
	INSERT INTO user_role_assoc VALUES(10342710, 2);
	INSERT INTO user_role_assoc VALUES(1000012201, 3);
	INSERT INTO user_role_assoc VALUES(1000012202, 3);
	INSERT INTO user_role_assoc VALUES(1000012203, 3);
	INSERT INTO user_role_assoc VALUES(1000012204, 3);
	INSERT INTO user_role_assoc VALUES(1000012205, 3);
	INSERT INTO user_role_assoc VALUES(1000012206, 3);
	INSERT INTO user_role_assoc VALUES(1000012207, 3);
	INSERT INTO user_role_assoc VALUES(1000012208, 3);
	INSERT INTO user_role_assoc VALUES(1000012209, 3);
	INSERT INTO user_role_assoc VALUES(1000012210, 3);
	INSERT INTO user_role_assoc VALUES(201907119001, 4);
	INSERT INTO user_role_assoc VALUES(201907119002, 4);
	INSERT INTO user_role_assoc VALUES(201907119003, 4);
	INSERT INTO user_role_assoc VALUES(201907119004, 4);
	INSERT INTO user_role_assoc VALUES(201907119005, 4);
	INSERT INTO user_role_assoc VALUES(201907119006, 4);
	INSERT INTO user_role_assoc VALUES(201907119007, 4);
	INSERT INTO user_role_assoc VALUES(201907119008, 4);
	INSERT INTO user_role_assoc VALUES(201907119009, 4);
	INSERT INTO user_role_assoc VALUES(201907119010, 4);

-- 用户信息表
	DROP TABLE IF EXISTS usermsgs;
	CREATE TABLE usermsgs (
		id BIGINT(12) UNSIGNED PRIMARY KEY COMMENT 'ID',
		name VARCHAR(20) NOT NULL COMMENT '姓名',
		gender enum("男", "女") NOT NULL COMMENT '性别',
		email VARCHAR(50) COMMENT '电子邮箱',
		phone VARCHAR(11)COMMENT '联系方式',
		political_status enum("中共党员", "中共预备党员", "共青团员", "民革党员", "民盟盟员", "民建会员", "民进会员", "农工党党员", "致公党党员", "九三学社社员", "台盟盟员", "无党派人士", "群众") COMMENT '政治面貌',
		clan enum("汉族", "蒙古族", "回族", "藏族", "维吾尔族", "苗族", "彝族", "壮族", "布依族", "朝鲜族", "满族", "侗族", "瑶族", "白族", "土家族", "哈尼族", "哈萨克族", "傣族", "黎族", "僳僳族", "佤族", "畲族", "高山族", "拉祜族", "水族", "东乡族", "纳西族", "景颇族", "柯尔克孜族", "土族", "达斡尔族", "仫佬族", "羌族", "布朗族", "撒拉族", "毛南族", "仡佬族", "锡伯族", "阿昌族", "普米族", "塔吉克族", "怒族", "乌孜别克族", "俄罗斯族", "鄂温克族", "德昂族", "保安族", "裕固族", "京族", "塔塔尔族", "独龙族", "鄂伦春族", "赫哲族", "门巴族", "珞巴族", "基诺族") COMMENT '民族',
		
		FOREIGN KEY(id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
	) COMMENT '管理用户基本信息的表';

-- 本科生
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(201907119001, '仰春荷', 1, '2723022544@netease.com', '17529693415');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(201907119002, '史采柳', 1, '2513185575@sina.com', '13557492424');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(201907119003, '韩新颖', 2, '6641220405@enet.com.cn', '15362702740');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(201907119004, '勾真如', 1, '1048817832@tom.com', '18809144600');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(201907119005, '龙抒怀', 1, '4016238032@163.com', '17111109515');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(201907119006, '茹子宁', 1, '2058700484@sogou@com', '15049446696');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(201907119007, '厍思枫', 2, '6308283681@etang.com', '17225449769');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(201907119008, '阎灵枫', 2, '5230405581@eastday.com', '18415026136');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(201907119009, '连彤雯', 1, '2632027782@eyou.com', '17859135875');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(201907119010, '关珺琦', 2, '0365218662@yeah.net', '13735627443');
	
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(1000012201, '邱月天', 1, '0171203373@sina.com', '13306157623');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(1000012202, '索从霜', 1, '6150711376@eastday.com', '15648878585');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(1000012203, '申晶瑶', 1, '7255758306@126.com', '18038877876');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(1000012204, '符苏迷', 1, '0351438345@eastday.com', '18293404338');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(1000012205, '鱼秋莲', 1, '4313402661@163.com', '18341157272');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(1000012206, '堵菊霞', 1, '0355617333@eyou.com', '18781009200');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(1000012207, '习星晴', 2, '0124657342@265.com', '15097163418');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(1000012208, '傅梓涵', 1, '5613317130@etang.com', '18093523333');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(1000012209, '养余馥', 2, '6314856401@xinhuanet', '16527681467');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(1000012210, '桂忻愉', 1, '5281835736@msn.com', '17248288900');
	
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(10342701, '蒲诗槐', 2, '1566141627@msn.com', '17728458875');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(10342702, '冉安然', 2, '5230816053@xinhuanet', '15100965291');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(10342703, '温韶丽', 2, '1024100615@21cn.com', '15234346259');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(10342704, '家惠琴', 1, '0460707700@126.com', '15839516880');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(10342705, '储问筠', 1, '0280248887@265.com', '18171269008');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(10342706, '贾芊芊', 2, '7354521556@21cn.com', '17725572798');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(10342707, '富艳蕙', 2, '5272835273@265.com', '13614843176');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(10342708, '雍珠玉', 2, '3085165616@21cn.com', '13991741367');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(10342709, '乌敏慧', 1, '5623344543@163.com', '15209044256');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(10342710, '阎吉敏', 1, '1551855350@163.com', '15384721524');
	
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(103501, '李春华', 2, '7984562813@163.com', '13976235781');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(103502, '茹莹琇', 1, '7832148741@msn.com', '13907534238');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(103503, '惠韫素', 1, '6254083588@163.com', '18368722517');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(103504, '濮尔槐', 1, '0416768214@35.com', '17356048741');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(103505, '束小瑜', 1, '2571741414@sohu.com', '14730046569');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(103506, '后柔谨', 2, '6068765288@xinhuanet', '17592105852');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(103507, '冷兰矢', 1, '8771361810@sogou@com', '18484986964');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(103508, '訾春雨', 2, '5085817437@eastday.com', '13522399092');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(103509, '石甘泽', 1, '5718771584@etang.com', '18761584981');
	INSERT INTO usermsgs(id, name, gender, email, phone) VALUES(103510, '吴语雪', 1, '2683613212@163.net', '13320196660');
	
-- 用户信息视图
	CREATE OR REPLACE VIEW user_msgs AS
		SELECT
			usermsgs.*,
			GROUP_CONCAT(roles.name SEPARATOR ',') 'roles',
			users.reg_date
		FROM users
		JOIN usermsgs ON users.id = usermsgs.id
		JOIN user_role_assoc ON usermsgs.id = user_role_assoc.uid
		JOIN roles ON user_role_assoc.rid = roles.id
		GROUP BY users.id;
		
-- 日志表
	DROP TABLE IF EXISTS logs;
	CREATE TABLE logs (
		id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		uid BIGINT(12) UNSIGNED COMMENT '请求用户ID',
		type enum('POST', 'GET') COMMENT '请求类型',
		title VARCHAR(100) COMMENT '名称',
		url VARCHAR(255) COMMENT '地址',
		params JSON COMMENT '参数',
		ip VARCHAR(15) COMMENT '请求IP',
		date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间'
	);
		
-- 权限表
	DROP TABLE IF EXISTS permissions;
	CREATE TABLE permissions (
		id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		name VARCHAR(100) COMMENT '名称',
		icon VARCHAR(50) COMMENT '图标',
		type enum("菜单", "选项", "标签", "操作") COMMENT '类型',
		status BOOL DEFAULT TRUE COMMENT '状态',
		url VARCHAR(255) COMMENT '地址' DEFAULT '/layouts/errors/404.html',
		descrip VARCHAR(200) COMMENT '备注',
		pid INT COMMENT '父权限ID',
		create_user VARCHAR(20) COMMENT '创建人',
		create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
		update_time DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
		FOREIGN KEY(pid) REFERENCES permissions(id) ON UPDATE CASCADE ON DELETE CASCADE,
		INDEX(url)
	) COMMENT '管理权限目录的表';

-- 角色、权限关联表
	DROP TABLE IF EXISTS role_perm_assoc;
	CREATE TABLE role_perm_assoc (
		rid TINYINT,
		pid INT,
		PRIMARY KEY(rid, pid),
		FOREIGN KEY(rid) REFERENCES roles(id) ON DELETE CASCADE ON UPDATE CASCADE,
		FOREIGN KEY(pid) REFERENCES permissions(id) ON DELETE CASCADE ON UPDATE CASCADE
	) COMMENT '角色权限关联表';
	
	CREATE TRIGGER `on_permission_insert` AFTER INSERT ON `permissions` 
	FOR EACH ROW
	INSERT INTO role_perm_assoc VALUES(1, NEW.id);
	
	INSERT INTO `permissions` VALUES (1, '本科教学', 'under-graduate', 1, 1, '#', NULL, NULL, '李春华', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (2, '高等数学', NULL, 1, 1, '/maters/AM/', NULL, 1, '李春华', '2022-07-03 17:28:58', '2022-07-08 09:23:24');
	INSERT INTO `permissions` VALUES (3, '列表', NULL, 4, 1, '/maters/AM/index', NULL, 2, '茹莹琇', '2022-07-03 17:28:58', '2022-07-08 09:24:16');
	INSERT INTO `permissions` VALUES (4, '上传', NULL, 4, 1, '/maters/AM/upload', NULL, 2, '茹莹琇', '2022-07-03 17:28:58', '2022-07-08 09:24:09');
	INSERT INTO `permissions` VALUES (5, '删除', NULL, 4, 1, '/maters/AM/delete', NULL, 2, '茹莹琇', '2022-07-03 17:28:58', '2022-07-08 09:24:04');
	INSERT INTO `permissions` VALUES (7, '教学大纲及教学进程表', NULL, 2, 1, '/maters/AM/Syllabus/', NULL, 2, '李春华', '2022-07-03 17:28:58', '2022-07-08 09:23:34');
	INSERT INTO `permissions` VALUES (8, '教学讲义及ppt', NULL, 2, 1, '/maters/AM/Handouts/', NULL, 2, '李春华', '2022-07-03 17:28:58', '2022-07-08 09:23:40');
	INSERT INTO `permissions` VALUES (9, '考试A/B试卷及解答', NULL, 2, 1, '/maters/AM/Exams/', NULL, 2, '李春华', '2022-07-03 17:28:58', '2022-07-08 09:23:47');
	INSERT INTO `permissions` VALUES (10, '工程数学', NULL, 1, 1, '/maters/EM/', NULL, 1, '李春华', '2022-07-03 17:28:58', '2022-07-08 09:25:36');
	INSERT INTO `permissions` VALUES (11, '列表', NULL, 4, 1, '/maters/EM/index', NULL, 10, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (12, '上传', NULL, 4, 1, '/maters/EM/upload', NULL, 10, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (13, '删除', NULL, 4, 1, '/maters/EM/delete', NULL, 10, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (15, '教学大纲及教学进程表', NULL, 2, 1, '/maters/EM/Syllabus/', NULL, 10, '李春华', '2022-07-03 17:28:58', '2022-07-08 09:25:44');
	INSERT INTO `permissions` VALUES (16, '教学讲义及ppt', NULL, 2, 1, '/maters/EM/Handouts/', NULL, 10, '李春华', '2022-07-03 17:28:58', '2022-07-08 09:25:50');
	INSERT INTO `permissions` VALUES (17, '考试A/B试卷及解答', NULL, 2, 1, '/maters/EM/Exams/', NULL, 10, '李春华', '2022-07-03 17:28:58', '2022-07-08 09:25:57');
	INSERT INTO `permissions` VALUES (18, '信息与计算科学', NULL, 1, 1, '/maters/IACS/', NULL, 1, '李春华', '2022-07-03 17:28:58', '2022-07-08 09:29:50');
	INSERT INTO `permissions` VALUES (19, '列表', NULL, 4, 1, '/maters/IACS/index', NULL, 18, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (20, '上传', NULL, 4, 1, '/maters/IACS/upload', NULL, 18, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (21, '删除', NULL, 4, 1, '/maters/IACS/delete', NULL, 18, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (23, '教学大纲及教学进程表', NULL, 2, 1, '/maters/IACS/Syllabus/', NULL, 18, '李春华', '2022-07-03 17:28:58', '2022-07-08 09:30:00');
	INSERT INTO `permissions` VALUES (24, '教学讲义及ppt', NULL, 2, 1, '/maters/IACS/Handouts/', NULL, 18, '李春华', '2022-07-03 17:28:58', '2022-07-08 09:30:06');
	INSERT INTO `permissions` VALUES (25, '考试A/B试卷及解答', NULL, 2, 1, '/maters/IACS/Exams/', NULL, 18, '李春华', '2022-07-03 17:28:58', '2022-07-08 09:30:12');
	INSERT INTO `permissions` VALUES (26, '毕业论文及实习报告', NULL, 2, 1, '/maters/IACS/GTAIPs/', NULL, 18, '李春华', '2022-07-03 17:28:58', '2022-07-08 09:30:17');
	INSERT INTO `permissions` VALUES (27, '统计学', NULL, 1, 1, '/maters/ST/', NULL, 1, '李春华', '2022-07-03 17:28:58', '2022-07-08 09:30:55');
	INSERT INTO `permissions` VALUES (28, '列表', NULL, 4, 1, '/maters/ST/index', NULL, 27, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (29, '上传', NULL, 4, 1, '/maters/ST/upload', NULL, 27, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (30, '删除', NULL, 4, 1, '/maters/ST/delete', NULL, 27, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (32, '教学大纲及教学进程表', NULL, 2, 1, '/maters/ST/Syllabus/', NULL, 27, '李春华', '2022-07-03 17:28:58', '2022-07-08 09:31:02');
	INSERT INTO `permissions` VALUES (33, '教学讲义及ppt', NULL, 2, 1, '/maters/ST/Handouts/', NULL, 27, '李春华', '2022-07-03 17:28:58', '2022-07-08 09:31:07');
	INSERT INTO `permissions` VALUES (34, '考试A/B试卷及解答', NULL, 2, 1, '/maters/ST/Exams/', NULL, 27, '李春华', '2022-07-03 17:28:58', '2022-07-08 09:31:14');
	INSERT INTO `permissions` VALUES (35, '毕业论文及实习报告', NULL, 2, 1, '/maters/ST/GTAIPs/', NULL, 27, '李春华', '2022-07-03 17:28:58', '2022-07-08 09:31:23');
	INSERT INTO `permissions` VALUES (36, '本科教学安排及教材预定', NULL, 1, 1, '#', NULL, 1, '李春华', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (37, '教学安排', NULL, 2, 1, '/maters/UT/TeacArrs/', NULL, 36, '李春华', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (38, '教材预定', NULL, 2, 1, '#', NULL, 36, '李春华', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (39, '研究生教学', 'post-graduate', 1, 1, '#', NULL, NULL, '李春华', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (40, '导师、研究生名录', NULL, 2, 1, '#', NULL, 39, '李春华', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (41, '导师名录', NULL, 3, 1, '/tutors/', NULL, 40, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (42, '列表', NULL, 4, 1, '/tutors/index', NULL, 41, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (43, '编辑', NULL, 4, 1, '/tutors/edit', NULL, 41, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (44, '研究生名录', NULL, 3, 1, '/posts/', NULL, 40, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (45, '列表', NULL, 4, 1, '/posts/index', NULL, 44, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (46, '添加', NULL, 4, 1, '/posts/add', NULL, 44, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (47, '编辑', NULL, 4, 1, '/posts/edit', NULL, 44, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (48, '删除', NULL, 4, 1, '/posts/delete', NULL, 44, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (50, '研究生教学安排、开题与答辩', NULL, 2, 1, '#', NULL, 39, '李春华', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (51, '教学安排', NULL, 3, 1, '/maters/PT/TeacArrs/', NULL, 50, '李春华', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (52, '开题与答辩', NULL, 3, 1, '/qAA/', NULL, 50, '李春华', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (53, '论文发表、学位论文及获奖', NULL, 2, 1, '#', NULL, 39, '李春华', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (54, '论文发表', NULL, 3, 1, '/papers/add', NULL, 53, '茹莹琇', '2022-07-03 17:28:58', '2022-07-07 23:11:39');
	INSERT INTO `permissions` VALUES (55, '学位论文获奖查询', NULL, 3, 1, '/awards/', NULL, 53, '李春华', '2022-07-03 17:28:58', '2022-07-07 22:56:30');
	INSERT INTO `permissions` VALUES (56, '实践教学', 'practice', 1, 1, '#', NULL, NULL, '李春华', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (57, '实验室建设与管理', NULL, 2, 1, '/laboratories/', NULL, 56, '李春华', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (58, '实验教学', NULL, 2, 1, '#', NULL, 56, '李春华', '2022-07-03 17:28:58', '2022-07-10 15:34:23');
	INSERT INTO `permissions` VALUES (59, '实习', NULL, 2, 1, '#', NULL, 56, '李春华', '2022-07-03 17:28:58', '2022-07-10 11:44:31');
	INSERT INTO `permissions` VALUES (60, '各类建设及报奖', 'construction', 1, 1, '#', NULL, NULL, '李春华', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (61, '教材建设', NULL, 2, 1, '/books/', NULL, 60, '李春华', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (62, '列表', NULL, 4, 1, '/books/index', NULL, 61, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (63, '添加', NULL, 4, 1, '/books/add', NULL, 61, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (64, '编辑', NULL, 4, 1, '/books/edit', NULL, 61, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (65, '删除', NULL, 4, 1, '/books/delete', NULL, 61, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (67, '学科建设、学位点建设', NULL, 2, 1, '#', NULL, 60, '李春华', '2022-07-03 17:28:58', '2022-07-10 17:54:44');
	INSERT INTO `permissions` VALUES (69, '列表', NULL, 4, 1, '/discips/index', NULL, 207, '茹莹琇', '2022-07-03 17:28:58', '2022-07-10 17:52:49');
	INSERT INTO `permissions` VALUES (70, '添加', NULL, 4, 1, '/discips/add', NULL, 207, '茹莹琇', '2022-07-03 17:28:58', '2022-07-10 17:53:00');
	INSERT INTO `permissions` VALUES (71, '编辑', NULL, 4, 1, '/discips/edit', NULL, 207, '茹莹琇', '2022-07-03 17:28:58', '2022-07-10 17:53:12');
	INSERT INTO `permissions` VALUES (72, '删除', NULL, 4, 1, '/discips/delete', NULL, 207, '茹莹琇', '2022-07-03 17:28:58', '2022-07-10 17:53:20');
	INSERT INTO `permissions` VALUES (74, '学位点建设', NULL, 3, 1, '/degrees/', NULL, 67, '茹莹琇', '2022-07-03 17:28:58', '2022-07-10 17:54:53');
	INSERT INTO `permissions` VALUES (75, '列表', NULL, 4, 1, '/degrees/index', NULL, 74, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (76, '添加', NULL, 4, 1, '/degrees/add', NULL, 74, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (77, '编辑', NULL, 4, 1, '/degrees/edit', NULL, 74, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (78, '删除', NULL, 4, 1, '/degrees/delete', NULL, 74, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (80, '专业建设、课程建设', NULL, 2, 1, '#', NULL, 60, '李春华', '2022-07-03 17:28:58', '2022-07-10 17:48:36');
	INSERT INTO `permissions` VALUES (81, '专业建设', NULL, 3, 1, '/majors/', NULL, 80, '茹莹琇', '2022-07-03 17:28:58', '2022-07-10 17:50:21');
	INSERT INTO `permissions` VALUES (82, '列表', NULL, 4, 1, '/majors/index', NULL, 81, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (83, '添加', NULL, 4, 1, '/majors/add', NULL, 81, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (84, '编辑', NULL, 4, 1, '/majors/edit', NULL, 81, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (85, '删除', NULL, 4, 1, '/majors/delete', NULL, 81, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (88, '列表', NULL, 4, 1, '/courses/index', NULL, 206, '茹莹琇', '2022-07-03 17:28:58', '2022-07-10 17:51:22');
	INSERT INTO `permissions` VALUES (89, '添加', NULL, 4, 1, '/courses/add', NULL, 206, '茹莹琇', '2022-07-03 17:28:58', '2022-07-10 17:51:32');
	INSERT INTO `permissions` VALUES (90, '编辑', NULL, 4, 1, '/courses/edit', NULL, 206, '茹莹琇', '2022-07-03 17:28:58', '2022-07-10 17:51:46');
	INSERT INTO `permissions` VALUES (91, '删除', NULL, 4, 1, '/courses/delete', NULL, 206, '茹莹琇', '2022-07-03 17:28:58', '2022-07-10 17:51:53');
	INSERT INTO `permissions` VALUES (93, '教学与科学研究及报奖', NULL, 2, 1, '/tARAPAS/', NULL, 60, '李春华', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (94, '信息管理', 'information', 1, 1, '#', NULL, NULL, '李春华', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (95, '用户管理', NULL, 2, 1, '#', NULL, 94, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (96, '学生管理', NULL, 3, 1, '/students/', NULL, 95, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (97, '列表', NULL, 4, 1, '/students/index', NULL, 96, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (98, '添加', NULL, 4, 1, '/students/add', NULL, 96, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (99, '编辑', NULL, 4, 1, '/students/edit', NULL, 96, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (100, '删除', NULL, 4, 1, '/students/delete', NULL, 96, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (102, '教师管理', NULL, 3, 1, '/teachers/', NULL, 95, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (103, '列表', NULL, 4, 1, '/teachers/index', NULL, 102, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (104, '添加', NULL, 4, 1, '/teachers/add', NULL, 102, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (105, '编辑', NULL, 4, 1, '/teachers/edit', NULL, 102, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (106, '删除', NULL, 4, 1, '/teachers/delete', NULL, 102, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (108, '用户管理', NULL, 3, 1, '/users/', NULL, 95, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (109, '列表', NULL, 4, 1, '/users/index', NULL, 108, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (110, '添加', NULL, 4, 1, '/users/add', NULL, 108, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (111, '编辑', NULL, 4, 1, '/users/edit', NULL, 108, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (112, '删除', NULL, 4, 1, '/users/delete', NULL, 108, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (114, '重置密码', NULL, 4, 1, '/users/reset', NULL, 108, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (115, '授权', NULL, 4, 1, '/users/authorize', NULL, 108, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (116, '目录管理', NULL, 2, 1, '/permissions/', NULL, 94, '李春华', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (117, '列表', NULL, 4, 1, '/permissions/index', NULL, 116, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (118, '添加', NULL, 4, 1, '/permissions/add', NULL, 116, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (119, '编辑', NULL, 4, 1, '/permissions/edit', NULL, 116, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (120, '删除', NULL, 4, 1, '/permissions/delete', NULL, 116, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (121, '角色管理', NULL, 2, 1, '/roles/', NULL, 94, '李春华', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (122, '列表', NULL, 4, 1, '/roles/index', NULL, 121, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (123, '添加', NULL, 4, 1, '/roles/add', NULL, 121, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (124, '编辑', NULL, 4, 1, '/roles/edit', NULL, 121, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (125, '删除', NULL, 4, 1, '/roles/delete', NULL, 121, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (126, '授权', NULL, 4, 1, '/roles/authorize', NULL, 121, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (127, '数据库管理', NULL, 2, 1, '/databases/', NULL, 94, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (128, '列表', NULL, 4, 1, '/databases/index', NULL, 127, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (129, '管理', NULL, 4, 1, '/databases/edit', NULL, 127, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (130, '日志管理', NULL, 2, 1, '/logs/', NULL, 94, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (131, '列表', NULL, 4, 1, '/logs/index', NULL, 130, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (132, '删除', NULL, 4, 1, '/logs/delete', NULL, 130, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (134, '党务、工会、学生工作', 'affair', 2, 1, '/pATUSW/', NULL, NULL, '李春华', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (135, '综合类', 'comprehensive', 2, 1, '#', NULL, NULL, '李春华', '2022-07-03 17:28:58', '2022-07-08 14:48:52');
	INSERT INTO `permissions` VALUES (136, '下载', NULL, 4, 1, '/maters/AM/download', NULL, 2, '茹莹琇', '2022-07-03 17:28:58', '2022-07-08 09:23:58');
	INSERT INTO `permissions` VALUES (137, '下载', NULL, 4, 1, '/maters/EM/download', NULL, 10, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (138, '下载', NULL, 4, 1, '/maters/IACS/download', NULL, 18, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (139, '下载', NULL, 4, 1, '/maters/ST/download', NULL, 27, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (140, '列表', NULL, 4, 1, '/maters/UT/index', NULL, 37, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (141, '上传', NULL, 4, 1, '/maters/UT/upload', NULL, 37, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (142, '删除', NULL, 4, 1, '/maters/UT/delete', NULL, 37, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (143, '下载', NULL, 4, 1, '/maters/UT/download', NULL, 37, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (145, '个人/集体预定', NULL, 3, 1, '/books/resindex', NULL, 38, '李春华', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (146, '预定管理', NULL, 3, 1, '/reservations/', NULL, 38, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (147, '列表', NULL, 4, 1, '/reservations/index', NULL, 146, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (148, '审核', NULL, 4, 1, '/reservations/audit', NULL, 146, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (149, '删除', NULL, 4, 1, '/reservations/delete', NULL, 146, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (151, '列表', NULL, 4, 1, '/maters/PT/index', NULL, 51, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (152, '上传', NULL, 4, 1, '/maters/PT/upload', NULL, 51, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (153, '删除', NULL, 4, 1, '/maters/PT/delete', NULL, 51, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (154, '下载', NULL, 4, 1, '/maters/PT/download', NULL, 51, '茹莹琇', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (156, '预定', NULL, 4, 1, '/books/reserve', NULL, 145, '李春华', '2022-07-03 17:28:58', NULL);
	INSERT INTO `permissions` VALUES (157, '列表', NULL, 4, 1, '/laboratories/index', NULL, 57, '茹莹琇', '2022-07-06 21:13:28', NULL);
	INSERT INTO `permissions` VALUES (158, '添加', NULL, 4, 1, '/laboratories/add', NULL, 57, '茹莹琇', '2022-07-06 21:13:58', NULL);
	INSERT INTO `permissions` VALUES (159, '删除', NULL, 4, 1, '/laboratories/delete', NULL, 57, '茹莹琇', '2022-07-06 21:14:17', '2022-07-06 21:15:53');
	INSERT INTO `permissions` VALUES (160, '编辑', NULL, 4, 1, '/laboratories/edit', NULL, 57, '茹莹琇', '2022-07-06 21:15:37', NULL);
	INSERT INTO `permissions` VALUES (161, '我的预定', NULL, 3, 1, '/personal/resindex', NULL, 38, '茹莹琇', '2022-07-07 13:45:00', NULL);
	INSERT INTO `permissions` VALUES (162, '论文管理', NULL, 3, 1, '/papers/', NULL, 53, '茹莹琇', '2022-07-07 23:10:04', NULL);
	INSERT INTO `permissions` VALUES (163, '列表', NULL, 4, 1, '/papers/index', NULL, 162, '茹莹琇', '2022-07-07 23:10:45', NULL);
	INSERT INTO `permissions` VALUES (164, '删除', NULL, 4, 1, '/papers/delete', NULL, 162, '茹莹琇', '2022-07-07 23:11:04', NULL);
	INSERT INTO `permissions` VALUES (165, '教学成果奖项查询', NULL, 3, 1, '/awards/teaching/index', NULL, 135, '茹莹琇', '2022-07-08 08:55:45', '2022-07-08 15:39:46');
	INSERT INTO `permissions` VALUES (166, '规章与制度', NULL, 3, 1, '/maters/COMP/rules/', NULL, 135, '茹莹琇', '2022-07-08 08:58:42', '2022-07-08 19:30:01');
	INSERT INTO `permissions` VALUES (167, '综合类材料', NULL, 3, 1, '/maters/COMP/material/', NULL, 135, '茹莹琇', '2022-07-08 09:01:00', '2022-07-09 11:47:03');
	INSERT INTO `permissions` VALUES (168, '竞赛获奖查询(研究生)', NULL, 3, 1, '/awards/competition/index', NULL, 135, '茹莹琇', '2022-07-08 15:39:00', NULL);
	INSERT INTO `permissions` VALUES (169, '教师奖项查询', NULL, 3, 1, '/awards/teacher/index', NULL, 135, '茹莹琇', '2022-07-08 15:41:07', NULL);
	INSERT INTO `permissions` VALUES (170, '列表', NULL, 4, 1, '/maters/COMP/rules/index', NULL, 166, '茹莹琇', '2022-07-08 19:30:42', NULL);
	INSERT INTO `permissions` VALUES (171, '上传', NULL, 4, 1, '/maters/COMP/rules/upload', NULL, 166, '茹莹琇', '2022-07-08 19:31:03', '2022-07-08 19:33:09');
	INSERT INTO `permissions` VALUES (172, '删除', NULL, 4, 1, '/maters/COMP/rules/delete', NULL, 166, '茹莹琇', '2022-07-08 19:32:26', NULL);
	INSERT INTO `permissions` VALUES (173, '下载', NULL, 4, 1, '/maters/COMP/rules/download', NULL, 166, '茹莹琇', '2022-07-08 19:33:30', NULL);
	INSERT INTO `permissions` VALUES (174, '列表', NULL, 4, 1, '/maters/COMP/material/index', NULL, 167, '茹莹琇', '2022-07-09 11:46:53', NULL);
	INSERT INTO `permissions` VALUES (175, '上传', NULL, 4, 1, '/maters/COMP/material/upload', NULL, 167, '茹莹琇', '2022-07-09 11:47:14', '2022-07-09 11:48:24');
	INSERT INTO `permissions` VALUES (176, '删除', NULL, 4, 1, '/maters/COMP/material/delete', NULL, 167, '茹莹琇', '2022-07-09 11:47:29', NULL);
	INSERT INTO `permissions` VALUES (177, '下载', NULL, 4, 1, '/maters/COMP/material/download', NULL, 167, '茹莹琇', '2022-07-09 11:47:53', NULL);
	INSERT INTO `permissions` VALUES (178, '奖项申报', NULL, 3, 0, '/awards/declare/index', NULL, 93, '茹莹琇', '2022-07-09 22:35:27', '2022-07-09 22:53:52');
	INSERT INTO `permissions` VALUES (179, '教师论文', NULL, 3, 1, '/papers/teacher/', NULL, 93, '茹莹琇', '2022-07-09 22:37:54', NULL);
	INSERT INTO `permissions` VALUES (180, '教研项目', NULL, 3, 0, '/projects/teaching', NULL, 93, '茹莹琇', '2022-07-09 22:39:24', '2022-07-09 22:53:39');
	INSERT INTO `permissions` VALUES (181, '专利', NULL, 3, 0, '/patents/', NULL, 93, '茹莹琇', '2022-07-09 22:40:31', '2022-07-09 22:53:33');
	INSERT INTO `permissions` VALUES (182, '科研项目', NULL, 3, 1, '/projects/scientific', NULL, 93, '茹莹琇', '2022-07-09 22:42:56', '2022-07-09 22:53:28');
	INSERT INTO `permissions` VALUES (183, '学术会议', NULL, 3, 0, '/academicConference/', NULL, 93, '茹莹琇', '2022-07-09 22:47:59', '2022-07-09 22:52:55');
	INSERT INTO `permissions` VALUES (184, '实验课程管理', NULL, 3, 1, '/courses/experiment/', NULL, 58, '茹莹琇', '2022-07-10 10:13:19', NULL);
	INSERT INTO `permissions` VALUES (185, '列表', NULL, 4, 1, '/courses/experiment/index', NULL, 184, '茹莹琇', '2022-07-10 11:19:10', NULL);
	INSERT INTO `permissions` VALUES (186, '添加', NULL, 4, 1, '/courses/experiment/add', NULL, 184, '茹莹琇', '2022-07-10 11:20:41', NULL);
	INSERT INTO `permissions` VALUES (187, '删除', NULL, 4, 1, '/courses/experiment/delete', NULL, 184, '茹莹琇', '2022-07-10 11:21:00', NULL);
	INSERT INTO `permissions` VALUES (188, '编辑', NULL, 4, 1, '/courses/experiment/edit', NULL, 184, '茹莹琇', '2022-07-10 11:21:16', NULL);
	INSERT INTO `permissions` VALUES (189, '实习任务管理', NULL, 3, 1, '/internships/', NULL, 59, '茹莹琇', '2022-07-10 11:44:20', '2022-07-10 13:54:46');
	INSERT INTO `permissions` VALUES (190, '列表', NULL, 4, 1, '/internships/index', NULL, 189, '茹莹琇', '2022-07-10 11:45:07', '2022-07-10 13:54:38');
	INSERT INTO `permissions` VALUES (191, '添加', NULL, 4, 1, '/internships/add', NULL, 189, '茹莹琇', '2022-07-10 11:45:20', '2022-07-10 13:54:55');
	INSERT INTO `permissions` VALUES (192, '删除', NULL, 4, 1, '/internships/delete', NULL, 189, '茹莹琇', '2022-07-10 11:45:30', '2022-07-10 13:55:04');
	INSERT INTO `permissions` VALUES (193, '编辑', NULL, 4, 1, '/internships/edit', NULL, 189, '茹莹琇', '2022-07-10 11:45:45', '2022-07-10 13:55:15');
	INSERT INTO `permissions` VALUES (194, '实习申请', NULL, 3, 1, '/internships/appindex', NULL, 59, '茹莹琇', '2022-07-10 11:47:17', '2022-07-10 15:16:21');
	INSERT INTO `permissions` VALUES (195, '申请管理', NULL, 3, 1, '/applications/', NULL, 59, '茹莹琇', '2022-07-10 11:48:30', '2022-07-10 15:21:44');
	INSERT INTO `permissions` VALUES (196, '列表', NULL, 4, 1, '/applications/index', NULL, 195, '茹莹琇', '2022-07-10 12:28:34', NULL);
	INSERT INTO `permissions` VALUES (197, '删除', NULL, 4, 1, '/applications/delete', NULL, 195, '茹莹琇', '2022-07-10 12:29:42', NULL);
	INSERT INTO `permissions` VALUES (198, '审核', NULL, 4, 1, '/applications/audit', NULL, 195, '茹莹琇', '2022-07-10 12:29:57', NULL);
	INSERT INTO `permissions` VALUES (203, '管理培养方案', NULL, 4, 1, '/layouts/errors/404.html', NULL, 74, '茹莹琇', '2022-07-10 16:55:37', '2022-07-10 17:54:06');
	INSERT INTO `permissions` VALUES (205, '管理培养方案', NULL, 4, 1, '/majors/tpmanage', NULL, 81, '茹莹琇', '2022-07-10 17:47:46', NULL);
	INSERT INTO `permissions` VALUES (206, '课程建设', NULL, 3, 1, '/courses/', NULL, 80, '茹莹琇', '2022-07-10 17:51:12', NULL);
	INSERT INTO `permissions` VALUES (207, '学科建设', NULL, 3, 1, '/discips/', NULL, 67, '茹莹琇', '2022-07-10 17:52:34', '2022-07-10 17:55:00');
	INSERT INTO `permissions` VALUES (208, '添加', NULL, 4, 1, '/tutors/add', NULL, 41, '茹莹琇', '2022-07-10 21:37:42', NULL);
	INSERT INTO `permissions` VALUES (209, '删除', NULL, 4, 1, '/tutors/delete', NULL, 41, '茹莹琇', '2022-07-10 21:37:59', NULL);

-- 用户权限关联表
	DROP TABLE IF EXISTS user_perm_assoc;
	CREATE TABLE user_perm_assoc (
		uid BIGINT(12) UNSIGNED,
		pid INT,
		authorize TINYINT DEFAULT TRUE,
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
		id INT PRIMARY KEY COMMENT '专业代码',
		name VARCHAR(50) NOT NULL COMMENT '专业名称',
		descip VARCHAR(500) COMMENT '备注'
		
		-- dep_id CHAR(10),
		-- FOREIGN KEY(dep_id) REFERENCES departments(dep_id),
	) COMMENT '管理专业的表';

	INSERT INTO majors VALUES(070102, '信息与计算科学', '');
	INSERT INTO majors VALUES(071201, '统计学', '');
	
-- 教师表
 	DROP TABLE IF EXISTS teachers;
	CREATE TABLE teachers (
 		id BIGINT(12) UNSIGNED PRIMARY KEY COMMENT '职工号',
 		level enum("教授", "副教授", "讲师", "助教") COMMENT '职位',
		degree enum("博士研究生", "硕士研究生", "本科生", "博士研究生毕业", "硕士研究生毕业", "本科毕业", "硕士研究生肄业", "硕士研究生结业") COMMENT '学历',
		bachelor enum("工学博士学位", "工学硕士学位", "工学学士学位", "法学博士学位", "法学硕士学位", "法学学士学位", "哲学博士学位", "哲学硕士学位", "哲学学士学位", "文学博士学位", "文学硕士学位", "文学学士学位", "教育学博士学位", "教育学硕士学位", "教育学学士学位", "管理学博士学位", "管理学硕士学位", "管理学学士学位", "理学博士学位", "理学硕士学位", "理学学士学位", "医学博士学位", "医学硕士学位", "医学学士学位", "农学博士学位", "农学硕士学位", "农学学士学位", "艺术学博士学位", "艺术学硕士学位", "艺术学学士学位", "经济学博士学位", "经济学硕士学位", "经济学学士学位", "历史学博士学位", "历史学硕士学位", "历史学学士学位", "体育硕士专业学位", "工程硕士专业学位", "资产评估硕士专业学位", "公共管理硕士专业学位", "军事学博士学位", "军事学硕士学位", "工商管理硕士专业学位", "翻译硕士专业学位", "应用心理学硕士专业学位", "临床医学博士专业学位", "博士学位", "硕士学位", "学士学位") COMMENT '学位',
		is_tutor BOOLEAN COMMENT '是否是导师',

 		-- dep_id CHAR(10),
 		-- FOREIGN KEY(dep_id) REFERENCES departments(dep_id),
 		
		FOREIGN KEY(id) REFERENCES users(id)
 	) COMMENT '管理教师信息的表';

	INSERT INTO teachers VALUES(1000012201, 2, 1, 1, 1);
	INSERT INTO teachers VALUES(1000012202, 2, 1, 1, 1);
	INSERT INTO teachers VALUES(1000012203, 2, 1, 1, 1);
	INSERT INTO teachers VALUES(1000012204, 2, 1, 1, 1);
	INSERT INTO teachers VALUES(1000012205, 2, 1, 1, 1);
	INSERT INTO teachers VALUES(1000012206, 2, 1, 1, 1);
	INSERT INTO teachers VALUES(1000012207, 2, 1, 1, 1);
	INSERT INTO teachers VALUES(1000012208, 2, 1, 1, 1);
	INSERT INTO teachers VALUES(1000012209, 2, 1, 1, 1);
	INSERT INTO teachers VALUES(1000012210, 2, 1, 1, 1);
	
-- 教师视图
	CREATE OR REPLACE VIEW teac_msgs AS
		SELECT 
			teachers.id, 
			usermsgs.name, 
			usermsgs.gender, 
			usermsgs.email, 
			usermsgs.phone,
			teachers.level,
			teachers.degree,
			teachers.bachelor
		FROM users
		JOIN usermsgs ON users.id = usermsgs.id
		JOIN teachers ON usermsgs.id = teachers.id
		WITH CHECK OPTION;

-- 学生表
	DROP TABLE IF EXISTS students;
	CREATE TABLE students (
		id BIGINT(12) UNSIGNED PRIMARY KEY COMMENT 'ID/学籍号',
		mid INT COMMENT '专业ID',
		class_name SMALLINT COMMENT '班级',
		session SMALLINT COMMENT '所在年级',
		degree enum("博士研究生", "硕士研究生", "本科生") COMMENT '学历',
		tid BIGINT(12) UNSIGNED COMMENT '导师ID',
		
		-- dep_id CHAR(10),
		-- FOREIGN KEY(dep_id) REFERENCES departments(dep_id),
		FOREIGN KEY(`id`) REFERENCES `users`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
		FOREIGN KEY(`mid`) REFERENCES `majors`(`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
		FOREIGN KEY(`tid`) REFERENCES `teachers`(`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
	) COMMENT '管理学生信息的表';

	INSERT INTO students VALUES(201907119001, 070102, 1901, 2019, 3, NULL);
	INSERT INTO students VALUES(201907119002, 070102, 1901, 2019, 3, NULL);
	INSERT INTO students VALUES(201907119003, 070102, 1901, 2019, 3, NULL);
	INSERT INTO students VALUES(201907119004, 070102, 1901, 2019, 3, NULL);
	INSERT INTO students VALUES(201907119005, 070102, 1901, 2019, 3, NULL);
	INSERT INTO students VALUES(201907119006, 070102, 1901, 2019, 2, 1000012201);
	INSERT INTO students VALUES(201907119007, 070102, 1901, 2019, 2, 1000012201);
	INSERT INTO students VALUES(201907119008, 070102, 1901, 2019, 2, 1000012201);
	INSERT INTO students VALUES(201907119009, 070102, 1901, 2019, 2, 1000012201);
	INSERT INTO students VALUES(201907119010, 070102, 1901, 2019, 2, 1000012201);

-- 学生信息视图
	CREATE OR REPLACE VIEW stu_msgs AS
		SELECT 
			usermsgs.id, 
			usermsgs.name user_name, 
			usermsgs.gender, 
			usermsgs.email, 
			usermsgs.phone,
			majors.name major_name, 
			students.class_name,
			students.degree,
			students.session
		FROM usermsgs
		JOIN students ON usermsgs.id = students.id
		JOIN majors ON students.mid = majors.id;
		
-- 研究生信息视图
	CREATE OR REPLACE VIEW post_grad_msgs AS
		SELECT
			students.id,
			stu_msg.name user_name, 
			stu_msg.gender, 
			majors.name major_name, 
			students.class_name, 
			students.session, 
			teac_msg.name tutor_name
		FROM usermsgs stu_msg
		JOIN students ON stu_msg.id = students.id
		JOIN majors ON students.mid = majors.id
		JOIN usermsgs teac_msg ON students.tid = teac_msg.id
		WHERE students.degree = 0 OR students.degree = 1
		WITH CHECK OPTION;
		
-- 导师视图
	CREATE OR REPLACE VIEW tut_msgs AS 
		SELECT DISTINCT
				teachers.id, 
				usermsgs.name, 
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
		id INT PRIMARY KEY COMMENT 'ID',
		name VARCHAR(50) COMMENT '名称',
		cover VARCHAR(50) COMMENT '封面',
		type VARCHAR(20) COMMENT '类别',
		author VARCHAR(20) COMMENT '作者',
		publisher VARCHAR(50) COMMENT '出版社',
		ISBN VARCHAR(17) COMMENT 'ISBN',
		price INT COMMENT '价格',
		add_user VARCHAR(20) COMMENT '添加人',
		add_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间'
	);


	INSERT INTO textbooks(id, name, cover, type, author, publisher, ISBN, price, add_user) VALUES(102001, '离散数学A'	, '/assets/imgs/book.png',	 '计算机', '索从霜', '武汉科技大学出版社', '978-7-121-41174-8', 30, '李春华');
	INSERT INTO textbooks(id, name, cover, type, author, publisher, ISBN, price, add_user) VALUES(102002, '数学建模'	, '/assets/imgs/book.png', 	 '数学',   '申晶瑶', '武汉科技大学出版社', '978-7-121-41174-8', 30, '李春华');
	INSERT INTO textbooks(id, name, cover, type, author, publisher, ISBN, price, add_user) VALUES(102003, '数学分析'	, '/assets/imgs/book.png', 	 '数学',   '符苏迷', '武汉科技大学出版社', '978-7-121-41174-8', 30, '李春华');
	INSERT INTO textbooks(id, name, cover, type, author, publisher, ISBN, price, add_user) VALUES(102004, '微分几何'	, '/assets/imgs/book.png', 	 '数学',   '鱼秋莲', '武汉科技大学出版社', '978-7-121-41174-8', 30, '李春华');
	INSERT INTO textbooks(id, name, cover, type, author, publisher, ISBN, price, add_user) VALUES(102005, '计算方法'	, '/assets/imgs/book.png', 	 '数学',   '堵菊霞', '武汉科技大学出版社', '978-7-121-41174-8', 30, '李春华');
	INSERT INTO textbooks(id, name, cover, type, author, publisher, ISBN, price, add_user) VALUES(102006, '操作系统'	, '/assets/imgs/book.png', 	 '计算机', '习星晴', '武汉科技大学出版社', '978-7-121-41174-8', 30, '李春华');
	INSERT INTO textbooks(id, name, cover, type, author, publisher, ISBN, price, add_user) VALUES(102007, '高等代数'	, '/assets/imgs/book.png', 	 '数学',   '傅梓涵', '武汉科技大学出版社', '978-7-121-41174-8', 30, '李春华');
	INSERT INTO textbooks(id, name, cover, type, author, publisher, ISBN, price, add_user) VALUES(102008, '数学实验'	, '/assets/imgs/book.png', 	 '数学',   '养余馥', '武汉科技大学出版社', '978-7-121-41174-8', 30, '李春华');
	
	DROP TABLE IF EXISTS book_reservations;
	CREATE TABLE book_reservations (
		id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		uid BIGINT(12) UNSIGNED COMMENT '用户ID',
		tid INT COMMENT '教材ID',
		time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '预订时间',
		amount INT COMMENT '预订数量',
		role VARCHAR(20) COMMENT '身份',
		comment VARCHAR(100) COMMENT '预定留言' DEFAULT '',
		status TINYINT COMMENT '预订状态' DEFAULT 0,
		result VARCHAR(100) COMMENT '审核意见',
		FOREIGN KEY (uid) REFERENCES users(id),
		FOREIGN KEY (tid) REFERENCES textbooks(id)
	);
	
	INSERT INTO book_reservations(uid, tid, amount, role) VALUES(1000012201, 102002, 2, '教师');
	INSERT INTO book_reservations(uid, tid, amount, role) VALUES(1000012202, 102003, 1, '教师');
	INSERT INTO book_reservations(uid, tid, amount, role) VALUES(1000012203, 102004, 5, '教师');
	INSERT INTO book_reservations(uid, tid, amount, role) VALUES(1000012204, 102006, 10, '教师');
	
	CREATE OR REPLACE VIEW book_reservations_view AS
		SELECT 
			book_reservations.id, 
			book_reservations.uid, 
			usermsgs.name user_name,
			textbooks.id tid,
			textbooks.name book_name,
			textbooks.cover,
			textbooks.type,
			textbooks.author,
			textbooks.publisher,
			book_reservations.time,
			book_reservations.amount,
			book_reservations.role,
			book_reservations.comment,
			book_reservations.status,
			book_reservations.result
		FROM book_reservations
		JOIN usermsgs ON usermsgs.id = book_reservations.uid
		JOIN textbooks ON book_reservations.tid = textbooks.id
		WITH CHECK OPTION;


-- 学科建设表
	DROP TABLE IF EXISTS disciplines;
	CREATE TABLE disciplines (
		id INT PRIMARY KEY COMMENT '学科代码',
		name VARCHAR(20) COMMENT '名称',
		descrip VARCHAR(200) COMMENT '备注'
	);

-- 课程表
	DROP TABLE IF EXISTS courses;
	CREATE TABLE courses (
		id INT PRIMARY KEY COMMENT '课程代码',
		name VARCHAR(64) COMMENT '名称',
		unit VARCHAR(20) COMMENT '开课单位',
		credit FLOAT COMMENT '学分',
		total_hours TINYINT COMMENT '总学时',

		ass_method TINYINT COMMENT '考核方式', -- 考试为1, 考核为0
		nature BOOLEAN COMMENT '修读性质',-- 必修为1, 选修为0
		type VARCHAR(64) COMMENT '类型'
		
		-- theo_hours TINYINT COMMENT '理论学时',
		-- exp_hours TINYINT COMMENT '实验学时',
		-- comp_hours TINYINT COMMENT '上机学时',
		-- prac_hours TINYINT COMMENT '实践学时'
	);
	
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702076, '数学与统计系',				4,		64,	1, 1, '学科基础平台课程',	'高等代数与解析几何(一)');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702709, '数学与统计系',				3.5,	56,	1, 1, '学科基础平台课程',	'程序设计基础(一)');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702716, '数学与统计系',				5,		80,	1, 1, '学科基础平台课程',	'数学分析(一)');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (1401840, '大学英语部',				3,		48,	1, 1, '通识教育平台课程',	'大学英语(一)');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (1501882, '公共体育部',				1,		26,	1, 1, '通识教育平台课程',	'体育(一)');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (2502001, '大学生心理健康教育中心',	1,		16,	0, 1, '通识教育平台课程',	'大学生心理健康教育');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (2504003, '军事课教研室',				4,		14,	1, 1, '通识教育平台课程',	'军事课');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (5105001, '思想政治教育系',			3,		48,	1, 1, '通识教育平台课程',	'思想道德修养与法律基础');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (5106001, '形势与政策教研部',			2,		8,	0, 1, '通识教育平台课程',	'形势与政策');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702077, '数学与统计系',				4.5,	72,	1, 1, '学科基础平台课程',	'高等代数与解析几何(二)');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702091, '数学与统计系',				1,		2,	0, 1, '实践教学模块',		'C程序课程设计');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702216, '数学与统计系',				2,		32,	1, 0, '专业任选课程',		'统计计算基础');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702710, '数学与统计系',				5,		80,	1, 1, '学科基础平台课程',	'程序设计基础(二)');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702717, '数学与统计系',				6,		96,	1, 1, '学科基础平台课程',	'数学分析(二)');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0703605, '应用物理系',				2.5,	40,	1, 0, '学科基础平台课程',	'大学物理B(一)');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (1401841, '大学英语部',				3,		48,	1, 1, '通识教育平台课程',	'大学英语(二)');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (1501883, '公共体育部',				1,		34,	1, 1, '通识教育平台课程',	'体育(二)');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (2503002, '毕业生就业指导中心',		1,		16,	0, 1, '通识教育平台课程',	'职业生涯规划与就业创业指导');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (5103001, '中国近现代史系',			3,		48,	1, 1, '通识教育平台课程',	'中国近现代史纲要');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702011, '数学与统计系',				4.5,	72,	1, 1, '专业核心课程',		'离散数学');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702015, '数学与统计系',				5,		80,	1, 1, '专业核心课程',		'数据结构');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702042, '数学与统计系',				1,		1,	0, 1, '实践教学模块',		'数据结构课程设计');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702086, '数学与统计系',				3.5,	56,	1, 1, '专业核心课程',		'C++程序设计');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702718, '数学与统计系',				5.5,	88,	1, 1, '学科基础平台课程',	'数学分析(三)');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702723, '数学与统计系',				3.5,	56,	1, 1, '学科基础平台课程',	'概率论');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702731, '数学与统计系',				1,		2,	0, 1, '实践教学模块',		'C++课程设计');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0703606, '应用物理系',				2,		32,	1, 0, '学科基础平台课程',	'大学物理B(二)');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0703607, '应用物理系',				1.5,	24,	0, 0, '学科基础平台课程',	'大学物理实验B');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (1401842, '大学英语部',				3,		48,	1, 1, '通识教育平台课程',	'大学英语(三)');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (1501884, '公共体育部',				1,		34,	0, 1, '通识教育平台课程',	'体育(三)');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (5102001, '马克思主义原理系',			3,		48,	1, 1, '通识教育平台课程',	'马克思主义基本原理');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702002, '数学与统计系',				3.5,	56,	1, 1, '专业核心课程',		'复变函数');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702017, '数学与统计系',				3.5,	56,	1, 1, '专业核心课程',		'数理统计');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702019, '数学与统计系',				2,		32,	1, 0, '专业任选课程',		'数学建模');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702041, '数学与统计系',				1,		2,	0, 1, '实践教学模块',		'数据库课程设计');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702049, '数学与统计系',				3,		48,	1, 1, '学科基础平台课程',	'常微分方程');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702068, '数学与统计系',				4,		64,	1, 1, '专业核心课程',		'数据库系统概论');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702093, '数学与统计系',				3,		48,	0, 0, '专业任选课程',		'数学实验');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (1401843, '大学英语部',				3,		48,	1, 1, '通识教育平台课程',	'大学英语(四)');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (1501885, '公共体育部',				1,		34,	0, 1, '通识教育平台课程',	'体育(四)');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (2501002, '学工处通识课程教研室',		1,		16,	0, 1, '通识教育平台课程',	'公益劳动');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (5101001, '马克思主义中国化系',		5,		80,	1, 1, '通识教育平台课程',	'毛泽东思想与中国特色社会主义理论体系概论');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702007, '数学与统计系',				4.5,	72,	1, 1, '专业核心课程',		'计算方法');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702025, '数学与统计系',				3,		48,	1, 1, '专业核心课程',		'微分几何');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702032, '数学与统计系',				3,		48,	1, 1, '专业核心课程',		'运筹学');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702050, '数学与统计系',				1,		16,	1, 0, '专业任选课程',		'专业英语');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702056, '数学与统计系',				4,		64,	1, 0, '专业任选课程',		'计算机组成原理');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702058, '数学与统计系',				4.5,	72,	1, 0, '专业任选课程',		'微观经济学');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702061, '数学与统计系',				3,		48,	1, 0, '专业任选课程',		'操作系统');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702205, '数学与统计系',				3,		48,	1, 0, '专业任选课程',		'统计计算与软件');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702724, '数学与统计系',				3,		48,	1, 0, '学科基础平台课程',	'数论及密码应用');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (1601004, '图书馆',					1,		16,	0, 0, '专业任选课程',		'信息检索与利用');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702021, '数学与统计系',				3,		48,	1, 0, '专业任选课程',		'数字信号处理');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702022, '数学与统计系',				3.5,	56,	1, 0, '专业任选课程',		'算法分析与设计');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702031, '数学与统计系',				4,		64,	1, 0, '专业任选课程',		'信息与编码');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702053, '数学与统计系',				2,		32,	1, 0, '专业任选课程',		'计量经济学');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702060, '数学与统计系',				2,		32,	1, 0, '专业任选课程',		'数理金融');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702072, '数学与统计系',				3,		48,	1, 1, '专业核心课程',		'计算机网络');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702080, '数学与统计系',				4,		64,	1, 0, '专业任选课程',		'现代数学选讲');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702088, '数学与统计系',				2,		32,	0, 1, '专业核心课程',		'专业实验与设计(一)');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702730, '数学与统计系',				2,		32,	1, 1, '专业核心课程',		'实变函数');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702044, '数学与统计系',				2,		32,	1, 0, '专业任选课程',		'计算机实践');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702097, '数学与统计系',				4,		4,	0, 1, '实践教学模块',		'毕业实习');
	INSERT INTO courses(id, unit, credit, total_hours, ass_method, nature, type, name) VALUES (0702098, '数学与统计系',				8,		14,	0, 1, '实践教学模块',		'毕业设计(论文)');
	
-- 本科教学资料表
	DROP TABLE IF EXISTS teac_maters;
	CREATE TABLE teac_maters (
		id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		`group` VARCHAR(20) COMMENT '分组',
		category VARCHAR(64) NOT NULL COMMENT '类型',
		file_name VARCHAR(255) COMMENT '文件名',
		file_size INT COMMENT '文件大小',
		file_addr VARCHAR(255) COMMENT '文件地址',
		upload_user VARCHAR(20) COMMENT '上传人',
		upload_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
		download_times INT DEFAULT 0 COMMENT '下载次数'
	);

	INSERT INTO teac_maters(category, `group`, file_name, file_size, file_addr, upload_user) VALUES ('syllabus', 	'AM', '20200901-高等数学教学大纲-申晶瑶.docx', 300, '/datas/', '申晶瑶');
	INSERT INTO teac_maters(category, `group`, file_name, file_size, file_addr, upload_user) VALUES ('syllabus', 	'AM', '20200901-高等数学教学大纲-符苏迷.docx', 300, '/datas/', '符苏迷');
	INSERT INTO teac_maters(category, `group`, file_name, file_size, file_addr, upload_user) VALUES ('syllabus', 	'AM', '20200901-高等数学教学大纲-鱼秋莲.docx', 300, '/datas/', '鱼秋莲');
	INSERT INTO teac_maters(category, `group`, file_name, file_size, file_addr, upload_user) VALUES ('handout', 	'AM', '20200901-高等数学教学讲义-申晶瑶.ppt', 300, '/datas/', '申晶瑶');
	INSERT INTO teac_maters(category, `group`, file_name, file_size, file_addr, upload_user) VALUES ('handout', 	'AM', '20200901-高等数学教学讲义-符苏迷.ppt', 300, '/datas/', '符苏迷');
	INSERT INTO teac_maters(category, `group`, file_name, file_size, file_addr, upload_user) VALUES ('handout', 	'AM', '20200901-高等数学教学讲义-鱼秋莲.ppt', 300, '/datas/', '鱼秋莲');
	INSERT INTO teac_maters(category, `group`, file_name, file_size, file_addr, upload_user) VALUES ('exam',		'AM', '20200901-高等数学考试试卷A卷-申晶瑶.docx', 300, '/datas/', '申晶瑶');
	INSERT INTO teac_maters(category, `group`, file_name, file_size, file_addr, upload_user) VALUES ('exam',		'AM', '20200901-高等数学考试试卷A卷-符苏迷.docx', 300, '/datas/', '符苏迷');
	INSERT INTO teac_maters(category, `group`, file_name, file_size, file_addr, upload_user) VALUES ('exam',		'AM', '20200901-高等数学考试试卷A卷-鱼秋莲.docx', 300, '/datas/', '鱼秋莲');
	
	INSERT INTO teac_maters(category, `group`, file_name, file_size, file_addr, upload_user) VALUES ('schedule', 	'UT', '20190901-本科教学安排.docx', 300, '/datas/', '景俊英');
	INSERT INTO teac_maters(category, `group`, file_name, file_size, file_addr, upload_user) VALUES ('schedule', 	'UT', '20190901-本科教学安排.docx', 300, '/datas/', '景俊英');
	INSERT INTO teac_maters(category, `group`, file_name, file_size, file_addr, upload_user) VALUES ('schedule', 	'UT', '20190901-本科教学安排.docx', 300, '/datas/', '景俊英');

	INSERT INTO teac_maters(category, `group`, file_name, file_size, file_addr, upload_user) VALUES ('schedule', 	'PT', '20190901-研究生教学安排.docx', 300, '/datas/', '景俊英');
	INSERT INTO teac_maters(category, `group`, file_name, file_size, file_addr, upload_user) VALUES ('schedule', 	'PT', '20190901-研究生教学安排.docx', 300, '/datas/', '景俊英');
	INSERT INTO teac_maters(category, `group`, file_name, file_size, file_addr, upload_user) VALUES ('schedule', 	'PT', '20190901-研究生教学安排.docx', 300, '/datas/', '景俊英');

-- 实验室表
	DROP TABLE IF EXISTS laboratories;
	CREATE TABLE laboratories (
		id INT PRIMARY KEY COMMENT 'ID',
		name VARCHAR(50) COMMENT '名称',
		capacity TINYINT COMMENT '容量',
		addr VARCHAR(20) COMMENT '位置',
		principal VARCHAR(20) COMMENT '负责人',
		add_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间'
	);
	
	INSERT INTO laboratories(id, name, capacity, addr, principal) VALUES(22405, '示波器实验室', 4, '理学院二区405', '张三');
	INSERT INTO laboratories(id, name, capacity, addr, principal) VALUES(22406, '牛顿环实验室', 5, '理学院二区406', '李四');
	INSERT INTO laboratories(id, name, capacity, addr, principal) VALUES(22407, '光的偏振实验室', 6, '理学院二区407', '王五');
	
	DROP TABLE IF EXISTS internships;
	CREATE TABLE internships (
		id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		name VARCHAR(50) COMMENT '名称',
		unit_name VARCHAR(50) COMMENT '实习单位名称',
		unit_descrip VARCHAR(100) COMMENT '实习单位简介',
		days TINYINT COMMENT '实习时长',
		max_app_num INT COMMENT '最大申请人数',
		app_num INT COMMENT '已申请人数',
		publish_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
		publish_user VARCHAR(20) COMMENT '发布者',
		deadline DATETIME COMMENT '申请截止时间'
	);

	INSERT INTO internships(name, unit_name, unit_descrip, days, max_app_num, app_num, publish_user, deadline) VALUES('2019届毕业实习', '腾讯', '腾讯是一家知名的互联网公司', 62, 5, 0, '景俊英', '2022-9-5');
	INSERT INTO internships(name, unit_name, unit_descrip, days, max_app_num, app_num, publish_user, deadline) VALUES('2019届毕业实习', '华为', '华为是一家知名的互联网公司', 62, 5, 0, '景俊英', '2022-9-5');

	DROP TABLE IF EXISTS applications;
	CREATE TABLE applications (
		id INT PRIMARY KEY AUTO_INCREMENT COMMENT '申请编号',
		iid INT COMMENT '实习任务ID',
		uid BIGINT(12) UNSIGNED COMMENT '实习人ID',
		status TINYINT COMMENT '状态',
		start_time DATE COMMENT '实习开始时间',
		FOREIGN KEY (iid) REFERENCES internships(id),
		FOREIGN KEY (uid) REFERENCES users(id)
	);

	INSERT INTO applications(iid, uid, status, start_time) VALUES(1, 201907119001, 0, '2022-9-20');
	
	CREATE OR REPLACE VIEW internships_apps_view AS
		SELECT
			applications.id,
			applications.uid,
			usermsgs.name AS user_name,
			internships.name,
			internships.unit_name,
			internships.unit_descrip,
			applications.start_time,
			internships.days,
			applications.status
		FROM applications
		JOIN internships ON applications.iid = internships.id
		JOIN usermsgs ON applications.uid = usermsgs.id
		WITH CHECK OPTION;



-- -- 教学成功奖项表
	DROP TABLE IF EXISTS teac_awas;
	CREATE TABLE teac_awas (
		id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'id',
		name VARCHAR(50) COMMENT '成果名称',
		type VARCHAR(50) COMMENT '奖项类型',
		level VARCHAR(20) COMMENT '奖项等级',
		author VARCHAR(20) COMMENT '成功完成人',
		unit_sig_order VARCHAR(10) COMMENT '单位署名次序',
		comp_sig_order VARCHAR(10) COMMENT '完成人署名次序',
		date DATETIME COMMENT '获奖时间'
	);
	
	INSERT INTO teac_awas VALUES(null, '问题驱动、名师驱动、生师联动——需求侧导向的数学教学改革与多一流建设', null, null, '李德宜', null, null, null)

-- 竞赛或作品奖项表
	DROP TABLE IF EXISTS comp_awas;
	CREATE TABLE comp_awas (
		id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		award_name VARCHAR(100) NOT NULL COMMENT '奖项名称',
		work_name VARCHAR(100) NOT NULL COMMENT '获奖作品',
		level VARCHAR(20) COMMENT '获奖等级',
		org_name VARCHAR(100) COMMENT '组织单位名称',
		org_type enum('政府', '学会', '协会', '其他') COMMENT '组织单位类型',
		date YEAR COMMENT '获奖时间',
		author VARCHAR(20) COMMENT '获奖人姓名',
		role enum(0,1) COMMENT '获奖人角色'
	);

	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家级三等奖',	2021, 	'政府',	'学生',	'李华',		'中国优选法统筹法与经济数学研究会',		'2021年第十一届MathorCup高校数学建模挑战赛', '基于BP、GA算法的三维团簇最优结构预测');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家级二等奖',	2021, 	'政府',	'学生',	'李华',		'中国优选法统筹法与经济数学研究会',		'2020年Mathorcup高校数学建模挑战赛——大数据竞赛', '基于遗传算法和BP神经网络的原子团簇最优结构预测');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家级优秀奖',	2021, 	'政府',	'学生',	'李华',		'中国优选法统筹法与经济数学研究会',		'2020年Mathorcup高校数学建模挑战赛——大数据竞赛', '冲冲冲');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家级三等奖',	2021, 	'政府',	'学生',	'李华',		'美国国家科学基金会等',				  'Interdisciplinary Contest In Modeling', 'D2125427');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家级一等奖',	2021, 	'政府',	'学生',	'李华',		'亚太地区大学生数学建模竞赛组织委员会',	 'APMCM亚太地区大学生数学建模竞赛', '');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家级优秀奖',	2021, 	'政府',	'学生',	'李华',		'中国优选法统筹法与经济数学研究会',		'MathorCup高校数学建模挑战赛——大数据竞赛 研究生组', '王宇豪杨浩顾君');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家级优秀奖',	2021, 	'政府',	'学生',	'李华',		'全国大学生数学建模竞赛组织委员会',		'“泰迪杯”数据挖掘挑战赛', '上市公司财务数据分析');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家级优秀奖',	2021, 	'政府',	'学生',	'李华',		'亚太地区大学生数学建模竞赛组织委员会',	 'Asia and Pacific Mathematical Contest in Modeling', 'Team B 2020170040070');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家级二等奖',	2021, 	'政府',	'学生',	'李华',		'美国国家科学基金会等',				  '美国国际大学生数学建模竞赛', '2021美国国际大学生数学建模竞赛');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('省级二等奖',	2021, 	'政府',	'学生',	'李华',		'湖北工业与应用数学学会',			  '2021第十三届“华中杯”大学生数学建模挑战赛', '马赛克瓷砖选色问题');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家级二等奖',	2021, 	'政府',	'学生',	'李华',		'中国优选法统筹法与经济数学研究会',		'2021年第十一届MathorCup高校数学建模挑战赛', 'mathorcup数学建模挑战赛');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家级一等奖',	2021, 	'政府',	'学生',	'李华',		'美国国家科学基金会等',				  '2021Interdisciplinary Contest In Modeling', 'What for food?');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家级一等奖',	2021, 	'政府',	'学生',	'李华',		'中国优选法统筹法与经济数学研究会',		'2021年第十一届Mathorcup高校数学建模挑战赛', '基于生成对抗网络和模拟退火遗传算法的团簇能量预测模型');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家级一等奖',	2021, 	'政府',	'学生',	'李华',		'亚太地区大学生数学建模竞赛组织委员会',	 'APMCM 亚太地区大学生数学建模竞赛', 'Analysis of the Economic Impact of the US Presidential Candidates on the United States and China');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家级三等奖',	2021, 	'政府',	'学生',	'李华',		'美国国家科学基金会等',				  'Mathematical Contest in Modeling', '2107623');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家级二等奖',	2021, 	'政府',	'学生',	'李华',		'美国国家科学基金会等',				  '美国国际大学生数学建模竞赛', 'National Higher Education System Operation Detection Model and Its Application');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家级三等奖',	2021, 	'政府',	'学生',	'李华',		'全国大学生数学建模竞赛组织委员会',		 '第九届“泰迪杯”数据挖掘挑战赛', '上市公式财务数据分析');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家级三等奖',	2021, 	'政府',	'学生',	'李华',		'中国优选法统筹法与经济数学研究会',		 '2020年MathorCup高校数学建模挑战赛-大数据竞赛', 'ACC');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家级二等奖',	2021, 	'政府',	'学生',	'李华',		'中国优选法统筹法与经济数学研究会',		 'MathorCup 高校数学建模挑战赛', '三维团簇的能量预');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家级二等奖',	2021, 	'政府',	'学生',	'李华',		'中国优选法统筹法与经济数学研究会',		 'MathorCup 高校数学建模挑战赛', '三维团簇的能量预测');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家级二等奖',	2021, 	'政府',	'学生',	'李华',		'亚太地区大学生数学建模竞赛组织委员会',	  '亚太地区大学生数学建模大赛', '理学院数学建模小分队');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家级三等奖',	2021, 	'政府',	'学生',	'李华',		'中国优选法统筹法与经济数学研究会',		 'mathorcup高校数学建模挑战赛', '理学院建模小分队');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('省级二等奖',	2021, 	'政府',	'学生',	'李华',		'湖北工业与应用数学学会',				'华中地区大学生数学建模邀请赛', '马赛克瓷砖选色问题');
	
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家一等奖',	2021, 	'政府',	'教师',	'冯育强',	'',						  '瑞士MDPI出版社(2019 Best Paper Award)', '2019 Best Paper Award');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家二等奖',	2020, 	'政府',	'教师',	'李春丽',	'中国数学会',				'数学建模', 							'');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家二等奖',	2020, 	'政府',	'教师',	'丁咏梅',	'中国数学会',				'数学建模', 							'');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('国家二等奖',	2021, 	'政府',	'教师',	'徐帆',		'中国数学会',				'数学建模', 							'');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('校级二等奖',	2021, 	'其他',	'教师',	'张青',		'武汉科技大学',				'武汉科技大学第二届“课程思政”教学设计大赛', '');
	INSERT INTO comp_awas(level, date, org_type, role, author, org_name, award_name, work_name) VALUES('校级二等奖',	2021, 	'其他',	'教师',	'龚谊承',	'武汉科技大学',				'武汉科技大学第二届“课程思政”教学设计大赛', '');
-- 	
-- -- 学术会议表
-- 	DROP TABLE IF EXISTS acad_semis;
--  CREATE TABLE acad_semis (
-- 		as_id UNSIGNED INT PRIMARY KEY AUTO_INCREMENT,
-- 		as_name VARCHAR(50),
-- 		as_title VARCHAR(100),
-- 		as_date DATETIME,
-- 		as_part_stu_name VARCHAR(50),
-- 		as_location VARCHAR(100),
-- 		
-- 	);
-- 
-- 	INSERT INTO acad_semis VALUES(null, '魏菲', 'ICACI 2020，云南大理', 'Finite-Time Stabilization of Memristor Neural Networks with Time-Varying Delay: Interval Matrix Method', '202009', 'ICACI 2020，云南大理')
-- 	INSERT INTO acad_semis VALUES(null, '陈佳博', 'CSSC 2020，中国青岛', 'Event-triggered Heterogeneous Consensus of Directed Interdependent Networks', '20200920', 'CSSC 2020，中国青岛')
-- 	INSERT INTO acad_semis VALUES(null, '魏菲', 'CSSC 2020，中国青岛', 'Finite-time Synchronization of Memristor Neural Networks via Interval Matrix Method', '20200919', 'CSSC 2020，中国青岛')
-- 	INSERT INTO acad_semis VALUES(null, '郭天姣', 'CSSC 2020，中国青岛', 'Iterative Learning Heterogeneous Trajectory Tracking for Partially Interdependent Networks', '20200919', 'CSSC 2020，中国青岛')
-- 	INSERT INTO acad_semis VALUES(null, '谢俊涛', 'CSSC 2020，中国青岛', 'A Generalization of Ekeland’s Variational principle and its applications', '20200919', 'CSSC 2020，中国青岛')
-- 	INSERT INTO acad_semis VALUES(null, '王晓杰', '智能决策与博弈分会第一届学术会议暨“数据驱动的决策与博弈”论坛,中国南京', '基于蓄水池抽样的智能医保即时风险决策', '20201127', '分会报告')
-- 
-- 论文
	DROP TABLE IF EXISTS aca_papers;
	CREATE TABLE aca_papers (
		id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
		title VARCHAR(50) COMMENT '论文标题',
		author VARCHAR(50) COMMENT '作者',
		author_type enum('第一作者', '通讯作者', '其他') COMMENT '作者类型',
		pub_jour VARCHAR(50) COMMENT '发表期刊',
		pub_year YEAR COMMENT '发表年份',
		pub_vol TINYINT COMMENT '发表卷数',
		pub_term TINYINT COMMENT '发表期数',
		inc enum('CSSCI', 'CSCD', 'SCI', 'SSCI', 'EI', 'A&HCI', '其他') COMMENT '期刊收录情况',
		disp VARCHAR(50) COMMENT '学科'
	) COMMENT '管理论文的表';
	
	
	INSERT INTO aca_papers VALUES(null, 'problem with critical', '罗妙彤', '第一作者', 'Topological Methods in Nonlienar Analysis', 2021, 8, 11, 'SCI', '非线性分析');

	DROP TABLE IF EXISTS teacher_awas
-- 	
-- -- 科研项目
-- 	DROP TABLE IF EXISTS res_projs;
--  CREATE TABLE res_projs (
-- 		rp_id UNSIGNED INT PRIMARY KEY AUTO_INCREMENT,
-- 		rp_name VARCHAR(100),
-- 		rp_author VARCHAR(50),
-- 		rp_auth_order enum('第一', '第二'),
-- 		rp_source VARCHAR(50),
-- 		rp_start_date DATE,
-- 		rp_end_date DATE,
-- 		rp_tot_funding UNSIGNED INT,
-- 		rp_rec_funding
-- 		rp_type VARCHAR(10)
-- 	);
-- 
-- 	DROP TABLE IF EXISTS patents;
--  CREATE TABLE patents (
-- 		p_id UNSIGNED INT PRIMARY KEY AUTO_INCREMENT,
-- 		p_name VARCHAR(50)
-- 		p_pub_date DATE,
-- 		p_type enum,
-- 		p_status enum
-- 	);