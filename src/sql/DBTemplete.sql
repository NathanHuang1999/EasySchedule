/*
用于创建数据库模式的sql脚本，不包含课表
@author huang
@date 2020-05-27
*/
#改变数据库的编码方式以兼容中文输入
#alter database enterprises character set utf8mb4

#创建“班级”表
create table class(
	grade		smallint,
	class_no	smallint,
	constraint chk_grade check (grade >=1 and grade <= 14),
	constraint chk_class_no check (class_no >=1),
	primary key (grade, class_no)
);

#创建“教师”表
create table teacher(
	id		varchar(20),
	name		varchar(20) not null,
	sex		char(1), 
	introduction text,
	constraint chk_sex check (sex in ('F', 'M')),
	primary key (id)
);

#创建“课次”表
create table time_slot(
	day		char(2),
	period		smallint,
	constraint chk_day check (day in ('周一', '周二', '周三', '周四', '周五', '周六', '周日')),
	constraint chk_period check (period >0 and period <=15),
	primary key (day, period)
);

#创建“特殊教室”表
create table special_classroom(
	category	varchar(20),
	room_no		smallint,
	primary key (category, room_no)
);

#创建“课程”表
create table course(
	name		varchar(20),
	category	char(2) not null,
	special_classroom_category	varchar(20),
	special_classroom_no	smallint,
	constraint chk_category check (category in ('主课', '副课')),
	primary key (name),
	foreign key (special_classroom_category, special_classroom_no) references special_classroom(category, room_no)
		on delete set null on update cascade
);

#创建“能够教学”表
create table able_to_teach(
	teacher_id	varchar(20),
	course_name	varchar(20),
	ability		varchar(20),
	primary key (teacher_id, course_name),
	foreign key (teacher_id) references teacher(id)
		on delete cascade on update cascade,
	foreign key (course_name) references course(name)
		on delete cascade on update cascade
);

#创建“教学”表
create table teaches(
	grade		smallint,
	class_no	smallint,
	course_name	varchar(20),
	teacher_id	varchar(20),
	amount_per_week	smallint,
	constraint chk_amount_per_week check (amount_per_week > 0),
	primary key (grade, class_no, course_name, teacher_id),
	foreign key (grade, class_no) references class(grade, class_no)
		on delete cascade on update cascade,
	foreign key (course_name) references course(name)
		on delete cascade on update cascade,
	foreign key (teacher_id) references teacher(id)
		on delete cascade on update cascade
);

#创建“电话号码”表
create table phone_no(
	phone_no	varchar(20),
	teacher_id	varchar(20),
	primary key (phone_no, teacher_id),
	foreign key (teacher_id) references teacher(id)
		on delete cascade on update cascade
);
