/*
用于向数据库中插入少量数据以便调试程序的sql脚本
@author huang
@date 2020-05-27
*/

#向“班级”表中插入数据
insert into class values (1, 1);
insert into class values (1, 2);
insert into class values (2, 1);

#向“教师”表中插入数据
insert into teacher values('00001', '王老菊', 'M', '未来科技集团优秀教师');
insert into teacher values('00002', '螺主任', 'M', '是优秀的老师，也是优秀的舞见');
insert into teacher values('00003', '努巴尼守望先锋', 'F', '努巴尼真好吃');
insert into teacher values('00004', '王尤姆', 'M', '只有风暴才能击倒大树...fnndp！');

#因为课次表只与课表相关，目前课表还未定义，故先跳过

#向“特殊教室”表中插入数据
insert into special_classroom values ('生化实验室', 1);
insert into special_classroom values ('生化实验室', 2);
insert into special_classroom values ('物理实验室', 1);

#向“课程”表中插入数据
insert into course values ('语文', '主课', null, null);
insert into course values ('数学', '主课', null, null);
insert into course values ('生物', '副课', null, null);
insert into course values ('物理探索实验', '副课', '物理实验室', 1);

#向“能够教学”表中插入数据
insert into able_to_teach values ('00001', '语文', '优秀');
insert into able_to_teach values ('00002', '语文', '优秀');
insert into able_to_teach values ('00003', '数学', '优秀');
insert into able_to_teach values ('00003', '生物', '良好');
insert into able_to_teach values ('00004', '生物', '优秀');
insert into able_to_teach values ('00004', '物理探索实验', '优秀');

#向“教学”表中插入数据
insert into teaches values (1, 1, '语文', '00001', 6);
insert into teaches values (1, 1, '数学', '00003', 5);
insert into teaches values (1, 2, '语文', '00002', 6);
insert into teaches values (1, 2, '物理探索实验', '00004', 2);
insert into teaches values (2, 1, '语文', '00001', 7);
insert into teaches values (2, 1, '生物', '00004', 3);

#向“电话号码”表中插入数据
insert into phone_no values ('61116111', '00001');
insert into phone_no values ('62226222', '00002');
insert into phone_no values ('63336333', '00003');
insert into phone_no values ('63666333', '00003');
insert into phone_no values ('64004404', '00004');
insert into phone_no values ('84124244', '00004');
