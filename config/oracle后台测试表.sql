drop table mobilestatus;
drop table mobileconfig;
drop table mobileuser;
drop table hardwareinfo;
drop table loginlog;
drop table mobilemenu;
drop table menuroles;
drop table backuser;
drop table message_tip;
drop table softversion;
drop table mobileSysLog;
-- create table 后台登陆表
create table backuser
(
  id        number(20) ,
  loginname varchar2(20) primary key,
  username  varchar2(30) not null,
  pswd      varchar2(128) not null,
  depname   varchar2(30),
  isvalid   number
)
;
-- Create table  硬件信息表
create table hardwareinfo
(
  hardwareid       varchar2(128) primary key,
  sysedidtion      varchar2(20),
  imei             varchar2(64),
  modelnum         varchar2(64),
  serialnum        varchar2(64),
  hardwaremodelnum varchar2(20),--该硬件是iOS、Android
  iccid            varchar2(64)
)
;
alter table hardwareinfo add extend1 varchar2(20);
-- Create table  手机用户表  20130716修改添加
create table mobileuser
(
  userid   varchar2(20) primary key,
  username varchar2(30) not null,
  loginname varchar2(20), --登录名
  organoid      varchar2(20) , --部门id
  organname varchar2(100),--部门名称
  menurolesid    varchar2(30) DEFAULT '默认',       --菜单权限id
  isstop   number  not null,  --是否被停用 0被停用 1可用
  stoptime timestamp
)
;
-- Create table  移动配置表
create table mobileconfig
(
  id      number primary key,
  type    number not null,        --类型，用数字表示 1代表用户数量限制，2代表通讯录版本号
  name    varchar2(30) not null,  --类型名称，通讯录版本号、用户数量限制
  details varchar2(50) not null,  --具体版本号
  remark  varchar2(500),          --备注，主要是用于版本更新
  settime timestamp,          --数据插入时间
  isvalid number default 1 not null --该条数据是否有用，使用0与1标识，默认为1且可用
)
;

-- Create table  移动登录表
create table mobilestatus
(
  id             number primary key,
  userid         varchar2(20) not null,
  hardwareid     varchar2(128) not null, --唯一硬件标识符ID
  softwareid     varchar2(128), --唯一软件标识符ID
  status         number not null,       --1为在线、2为切入后台、3为注销、4为退出
  logintype      number ,       --1为密码登录、2为证书登录
  stopordisable  number default 1 not null,--0为设备禁用、1为设备可用
  uninstall      number default 1 not null  --0为设备已卸载该软件
)
;
alter table mobilestatus add lastlogintime timestamp;   --设备最后登录时间
--Create table loginlog

create table loginlog
(
  id             number primary key,
  userid         varchar2(20) not null,
  hardwareid     varchar2(128) not null, --唯一硬件标识符ID
  begintime      timestamp not null,          --本次登陆开始时间
  endtime        timestamp,            --本次退出或者注销时间
  Processed      varchar2(128),            --本次耗时
  flage          number not null       --是否退出和注销的标示
)
;
create table softversion
(
  id         number primary key,
  type   varchar2(20) not null,    --传入APP为何种类型，目前有iOS和Android
  version    varchar2(20) not null,  --证书的版本
  updatetime  timestamp not null,    --更新时间
  softfile    blob not null,         --大数存储版本文件
  remark       varchar2(100),        --软件更新备注
  available    varchar2(10) not null  --是否可用1为可用 0为不可用
);
--Create table mobileusermenu 20130902
--以前的权限管理设置的太有问题必须推翻从来，按照资源、角色、用户这样的模式来进行新的管理
--mobileusermenu即为权限表
create table menuroles
(
  name           varchar2(30) ,  --角色名称
  menuid         varchar2(20)   --权限id,对应mobilemenu主键
)
;

--Create table mobilemenu 20130902

create table mobilemenu
(
   id             number primary key,  --主键
   itemName       varchar2(30),        --功能名字
   itemNo         varchar2(30),        --功能id
   reorder        number,              --菜单顺序
   itemPic        varchar2(30)       --功能图片名称
)
;

create table message_tip
(
   type         varchar2(16),  --如便签、发文
   context      varchar2(400)        --内容
)
;

--Create table mobileSysLog 20160518
create table mobileSysLog
(
   mobileSysLog_id             number primary key,  --主键
   mobileSysLog_description        varchar2(50),       --描述
   mobileSysLog_method             varchar2(200),         --方法名
   mobileSysLog_type               varchar2(10),        --0，为操作描述、1，为异常
   mobileSysLog_requestIp          varchar2(20),       --访问ip
   mobileSysLog_exceptionCode      varchar2(200),        --异常代码
   mobileSysLog_exceptionDetail    varchar2(2000),        --异常类容
   mobileSysLog_params             varchar2(2000),       --参数
   mobileSysLog_createBy           varchar2(30),       --被谁创建
   mobileSysLog_createDate         timestamp not null   --产生时间
)
;

----------------------------------------------------------------
--create20130823
---------------------数据字典-----------------------------------
--table hardwareinfo
comment on column hardwareinfo.hardwareid is '硬件信息表主键id';
comment on column hardwareinfo.sysedidtion is '系统版本号';
comment on column hardwareinfo.imei is     'imei';
comment on column hardwareinfo.modelnum is '手机型号';
comment on column hardwareinfo.serialnum is '系统序列号';
comment on column hardwareinfo.hardwaremodelnum is '系统类型该硬件是iOS、Android';
comment on column hardwareinfo.iccid is 'iccid号';
--table mobileuser
comment on column mobileuser.userid is '用户id为手机用户表的主键';
comment on column mobileuser.username is '用户名';
comment on column mobileuser.organoid is '部门id';
comment on column mobileuser.organname is '部门名称';
comment on column mobileuser.isstop is '是否被停用 0被停用 1可用';
comment on column mobileuser.stoptime is '用户被停用时间';
--table mobileconfig
comment on column mobileconfig.id is '移动配置表的主键';
comment on column mobileconfig.type is '类型，用数字表示 1代表用户数量限制，2代表通讯录版本号，3代表iOS版本号，4代表Android版本号';
comment on column mobileconfig.name is '类型名称，版本号、通讯录版本号、用户数量限制';
comment on column mobileconfig.details is '具体版本号';
comment on column mobileconfig.remark is '备注，主要是用于版本更新';
comment on column mobileconfig.settime is '数据插入时间';
comment on column mobileconfig.isvalid is '该条数据是否有用，使用0与1标识，默认为1且可用';
--table mobilestatus
comment on column mobilestatus.id is '移动登录表的主键';
comment on column mobilestatus.userid is '用户id';
comment on column mobilestatus.hardwareid is '唯一硬件标识符ID';
comment on column mobilestatus.softwareid is '唯一软件标识符ID';
comment on column mobilestatus.status is '1为在线、2为切入后台、3为注销、4为退出';
comment on column mobilestatus.logintype is '1为密码登录、2为证书登录';
comment on column mobilestatus.stopordisable is '1为设备可用、0为设备禁用';
comment on column mobilestatus.uninstall is '0为设备已卸载该软件';
--table loginlog
comment on column loginlog.id is '登陆日志表的主键';
comment on column loginlog.userid is '用户id';
comment on column loginlog.hardwareid is '唯一硬件标识符ID';
comment on column loginlog.begintime is '本次登陆开始时间';
comment on column loginlog.endtime is '本次退出或者注销时间';
comment on column loginlog.Processed is '本次耗时';
comment on column loginlog.flage is '是否退出或者注销的标识登录进来是为1，一旦推出或者注销计算耗时并且改为2';
--table mobilemenu
comment on column mobilemenu.id is '主键';
comment on column mobilemenu.itemName is '功能名字';
comment on column mobilemenu.itemNo is '功能id';
comment on column mobilemenu.itemPic is '功能图片';
--table softversion
comment on column softversion.id is '软件版本';
comment on column softversion.type is '传入APP为何种类型，目前有iOS和Android';
comment on column softversion.version is '证书的版本';
comment on column softversion.updatetime is '更新时间';
comment on column softversion.softfile is '大数存储版本文件';
comment on column softversion.available is '是否可用1为可用 0为不可用';
comment on column softversion.remark is '软件更新备注';
--table mobileusermenu
comment on column menuroles.name is '角色名称‘主键’';
comment on column menuroles.menuid is '和菜单资源的关联';

----------------------------------------------------------------
drop sequence mobileconfig_seq;
create sequence mobileconfig_seq
minvalue 0
maxvalue 9999999999999999999
start with 0
increment by 1
nocache;
/
drop sequence mobilestatus_seq;
create sequence mobilestatus_seq
minvalue 0
maxvalue 9999999999999999999
start with 0
increment by 1
nocache;
/
drop sequence loginlog_seq;
create sequence loginlog_seq
minvalue 0
maxvalue 9999999999999999999
start with 0
increment by 1
nocache;
/
drop sequence mobilemenu_seq;
create sequence mobilemenu_seq
minvalue 0
maxvalue 9999999999999999999
start with 0
increment by 1
nocache;
/
drop sequence SOFTVERSION_SEQ;
create sequence SOFTVERSION_SEQ
minvalue 0
maxvalue 9999999999999999999
start with 10
increment by 1
nocache;
/
drop sequence mobileSysLog_SEQ;
create sequence mobileSysLog_SEQ
minvalue 0
maxvalue 9999999999999999999
start with 10
increment by 1
nocache;
insert into MESSAGE_TIP (TYPE, CONTEXT)
values ('便签', '你好A，您收到一条B,内容为C。');
insert into MESSAGE_TIP (TYPE, CONTEXT)
values ('收文', '你好A，您收到一条B,内容为C。');
insert into MESSAGE_TIP (TYPE, CONTEXT)
values ('发文', '你好A，您收到一条B,内容为C。');
insert into MESSAGE_TIP (TYPE, CONTEXT)
values ('签报', '你好A，您收到一条B,内容为C。');
insert into MESSAGE_TIP (TYPE, CONTEXT)
values ('后台', '通知：C');
commit;
insert into MOBILECONFIG (ID, TYPE, NAME, DETAILS, REMARK, SETTIME, ISVALID)
values (MOBILECONFIG_SEQ.NEXTVAL, 1, '用户数量限制', '25651a7a37d56f4ea348fed1f5b24bb8', '200', sysdate, 1);
insert into MOBILECONFIG (ID, TYPE, NAME, DETAILS, REMARK, SETTIME, ISVALID)
values (MOBILECONFIG_SEQ.NEXTVAL, 3, 'iOS', '1.0.0', '这个iOS版本更新内容', sysdate, 1);
insert into MOBILECONFIG (ID, TYPE, NAME, DETAILS, REMARK, SETTIME, ISVALID)
values (MOBILECONFIG_SEQ.NEXTVAL, 4, 'Android', '1.0.0', '这个Android版本更新内容', sysdate, 1);
commit;
insert into MOBILEMENU (id, ITEMNAME, ITEMNO, ITEMPIC,reorder)
values (MOBILEMENU_SEQ.nextval, '待办公文', 'index_agency', 'index_agency_1.png', 0);
insert into MOBILEMENU (id, ITEMNAME, ITEMNO, ITEMPIC,reorder)
values (MOBILEMENU_SEQ.nextval, '公文查询', 'documentquery', 'documentquery_1.png', 1);
insert into MOBILEMENU (id, ITEMNAME, ITEMNO, ITEMPIC,reorder)
values (MOBILEMENU_SEQ.nextval, '通讯录', 'index_maillist', 'index_maillist_1.png', 2);
insert into MOBILEMENU (id, ITEMNAME, ITEMNO, ITEMPIC,reorder)
values (MOBILEMENU_SEQ.nextval, '办公便签', 'index_note', 'index_note_1.png', 3);
insert into MOBILEMENU (id, ITEMNAME, ITEMNO, ITEMPIC,reorder)
values (MOBILEMENU_SEQ.nextval, '通知公告', 'index_announcement', 'index_announcement_1.png', 4);
insert into MOBILEMENU (id, ITEMNAME, ITEMNO, ITEMPIC,reorder)
values (MOBILEMENU_SEQ.nextval, '副本查阅', 'index_copy', 'index_copy_1.png', 5);
insert into MOBILEMENU (id, ITEMNAME, ITEMNO, ITEMPIC,reorder)
values (MOBILEMENU_SEQ.nextval, '设置', 'setting', 'setting.png', 6);
commit;
insert into menuroles (name, menuid)
values ('默认', '0');
insert into menuroles (name, menuid)
values ('默认', '1');
insert into menuroles (name, menuid)
values ('默认', '2');
insert into menuroles (name, menuid)
values ('默认', '3');
insert into menuroles (name, menuid)
values ('默认', '4');
insert into menuroles (name, menuid)
values ('默认', '5');
insert into menuroles (name, menuid)
values ('默认', '6');
commit;
insert into BACKUSER (ID, LOGINNAME, USERNAME, PSWD, DEPNAME, ISVALID)
values (1, 'admin', 'admin', 'ab9b5a2bbe7e2f5d083892b263d9c340', '1', 1);
commit;