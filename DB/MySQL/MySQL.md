---
title: MySQL
categories:
- DB
- MySQL
tags:
- SQL
date: 2020/12/27 20:00:17
updated: 2021/1/7 12:00:17
---

# 通用数据库对象介绍

- 数据data

    指对客观事件进行记录并可以鉴别的符号，在计算机系统中存储为可以被识别的信息

- 数据库database 

    存放具体数据的最大的逻辑对象，通常按需求将同一项目所属的数据统一放在一个数据库中，也可以创建多个数据库 

- 数据库管理系统dbms(database management system)
  
    是一种操纵和管理数据库的大型软件，用于建立、使用和维护数据库，简称DBMS，目前分为关系型和非关系型数据库管理系统两种 

- 实例instance 

    数据库管理软件安装之后在服务器上启动起来就称为启动了一个实例，通常一个服务器上只启动一个数据库实例，但也有启动多个实例的，一个实例可以包含多个数据库 

- 数据库对象database object 

    数据库中创建的用来存储，操作数据的对象，比如表，字段，索引，存储过程等 通用数据库对象中英文介绍 

- 索引index 

    在数据库管理系统中通常为加速数据读取速度而创建的一种数据结构 **(类似目录)**

- 视图view 

    为（字面上）简化部分数据的获取方法而创建的定义 **(类似函数)**

- 存储过程sp(stored procedure) 

    在数据库内部创建的具有一系列数据处理逻辑的方法 

- 触发器trigger 

    当数据库表发生修改操作时自动触发某些数据处理的方法 

- 主键primary key 

    数据表中定义的用来唯一确定表中各行数据的一个或几个字段 

- 外键foreign key 

    当数据表中的某一个或几个字段的值来源于某个父表时，则可以创建两个表之间的数据映射关系 (存在依赖)

- 唯一unique 

    Primary key和unique index都可以作为约束表中这一列或几列的数据不重复 **(相当于加入索引)**

# MySQL Layout 

**Table 2.3 MySQL Installation Layout for Generic Unix/Linux Binary Package**

| Directory       | Contents of Directory                                        |
| :-------------- | :----------------------------------------------------------- |
| `bin`           | [**mysqld**](https://dev.mysql.com/doc/refman/8.0/en/mysqld.html) server, client and utility programs |
| `docs`          | MySQL manual in Info format                                  |
| `man`           | Unix manual pages                                            |
| `include`       | Include (header) files                                       |
| `lib`           | Libraries                                                    |
| `share`         | Error messages, dictionary, and SQL for database installation |
| `support-files` | Miscellaneous support files                                  |

# mysql.server

convention

```shell
# Set some defaults
mysqld_pid_file_path=
if test -z "$basedir"
then
  basedir=/usr/local/mysql
  bindir=/usr/local/mysql/bin
  if test -z "$datadir"
  then
    datadir=/usr/local/mysql/data
  fi
  sbindir=/usr/local/mysql/bin
  libexecdir=/usr/local/mysql/bin
else
  bindir="$basedir/bin"
  if test -z "$datadir"
  then
    datadir="$basedir/data"
  fi
  sbindir="$basedir/sbin"
  libexecdir="$basedir/libexec"
fi
```

通过`start`启动mysql 服务

```shell
case "$mode" in
  'start')
    # Start daemon

    # Safeguard (relative paths, core dumps..)
    cd $basedir

    echo $echo_n "Starting MySQL"
    if test -x $bindir/mysqld_safe
    then
      # Give extra arguments to mysqld with the my.cnf file. This script
      # may be overwritten at next upgrade.
      $bindir/mysqld_safe --datadir="$datadir" --pid-file="$mysqld_pid_file_path" $other_args >/dev/null &
      wait_for_pid created "$!" "$mysqld_pid_file_path"; return_value=$?

      # Make lock for RedHat / SuSE
      if test -w "$lockdir"
      then
        touch "$lock_file_path"
      fi

      exit $return_value
    else
      log_failure_msg "Couldn't find MySQL server ($bindir/mysqld_safe)"
    fi
    ;;
```

## 创建一个用于运行mysqld 的mysql 用户和组

因为用户只需要用于所有权的目的，而不是用于登录的目的，所以useradd命令使用-r和-s /bin/false选项来创建一个对服务器主机没有登录权限的用户。如果您的useradd不支持这些选项，请省略这些选项。

```shell
groupadd mysql
useradd -r -g mysql -s /bin/false mysql
```

## mysql 初始化

1. The [`secure_file_priv`](https://dev.mysql.com/doc/refman/8.0/en/server-system-variables.html#sysvar_secure_file_priv) system variable limits import and export operations to a specific directory. Create a directory whose location can be specified as the value of that variable:

    ```terminal
    mkdir mysql-files
    ```

    Grant directory user and group ownership to the `mysql` user and `mysql` group, and set the directory permissions appropriately:

    ```terminal
    chown mysql:mysql mysql-files
    chmod 750 mysql-files
    ```

2. Use the server to initialize the data directory, including the `mysql` schema containing the initial MySQL grant tables that determine how users are permitted to connect to the server. For example:

    ```terminal
    bin/mysqld --initialize --user=mysql
    ```

On Unix and Unix-like systems, it is important for the database directories and files to be owned by the `mysql` login account so that the server has read and write access to them when you run it later. To ensure this, start [**mysqld**](https://dev.mysql.com/doc/refman/8.0/en/mysqld.html) from the system `root` account and include the [`--user`](https://dev.mysql.com/doc/refman/8.0/en/server-options.html#option_mysqld_user) option as shown here:

```terminal
bin/mysqld --initialize --user=mysql
```

Alternatively, execute [**mysqld**](https://dev.mysql.com/doc/refman/8.0/en/mysqld.html) while logged in as `mysql`, in which case you can omit the [`--user`](https://dev.mysql.com/doc/refman/8.0/en/server-options.html#option_mysqld_user) option from the command.

It might be necessary to specify other options such as [`--basedir`](https://dev.mysql.com/doc/refman/8.0/en/server-system-variables.html#sysvar_basedir) or [`--datadir`](https://dev.mysql.com/doc/refman/8.0/en/server-system-variables.html#sysvar_datadir) if [**mysqld**](https://dev.mysql.com/doc/refman/8.0/en/mysqld.html) cannot identify the correct locations for the installation directory or data directory. For example (enter the command on a single line):

```terminal
bin/mysqld --initialize --user=mysql
  --basedir=/opt/mysql/mysql
  --datadir=/opt/mysql/mysql/data
```

Alternatively, put the relevant option settings in an option file and pass the name of that file to [**mysqld**](https://dev.mysql.com/doc/refman/8.0/en/mysqld.html). For Unix and Unix-like systems, suppose that the option file name is `/opt/mysql/mysql/etc/my.cnf`. Put these lines in the file:

```ini
[mysqld]
basedir=/opt/mysql/mysql
datadir=/opt/mysql/mysql/data
```

Then invoke [**mysqld**](https://dev.mysql.com/doc/refman/8.0/en/mysqld.html) as follows (enter the command on a single line with the [`--defaults-file`](https://dev.mysql.com/doc/refman/8.0/en/option-file-options.html#option_general_defaults-file) option first):

```terminal
bin/mysqld --defaults-file=/opt/mysql/mysql/etc/my.cnf
  --initialize --user=mysql
```

## Server Actions During Data Directory Initialization

• data目录为初始化的数据文件存放路径 
• data目录里为每一个数据库创建了一个文件夹 
• Ibdata1和ib_logfile0/1为三个专为innodb存放数据和日志的共享文件 

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201221213913227.png" alt="image-20201221213913227" style="zoom:80%;" />

When invoked with the [`--initialize`](https://dev.mysql.com/doc/refman/8.0/en/server-options.html#option_mysqld_initialize) option, [**mysqld**](https://dev.mysql.com/doc/refman/8.0/en/mysqld.html) performs the following actions during the data directory initialization sequence:

1. The server checks for the existence of the data directory as follows:

    - If no data directory exists, the server creates it.

    - If the data directory exists but is not empty (that is, it contains files or subdirectories), the server exits after producing an error message:

        ```none
        [ERROR] --initialize specified but the data directory exists. Aborting.
        ```

        In this case, remove or rename the data directory and try again.

        An existing data directory is permitted to be nonempty if every entry has a name that begins with a period (`.`).

2. Within the data directory, the server creates the `mysql` system schema and its tables, including the data dictionary tables, grant tables, time zone tables, and server-side help tables.

3. The server initializes the [system tablespace](https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_system_tablespace) and related data structures needed to manage [`InnoDB`](https://dev.mysql.com/doc/refman/8.0/en/innodb-storage-engine.html) tables.

4. The server creates a `'root'@'localhost'` superuser account and other reserved accounts. Some reserved accounts are locked and cannot be used by clients, but `'root'@'localhost'` is intended for administrative use and you should assign it a password.

5. The server populates the server-side help tables used for the [`HELP`](https://dev.mysql.com/doc/refman/8.0/en/help.html) statement. The server does not populate the time zone tables.

6. If the [`init_file`](https://dev.mysql.com/doc/refman/8.0/en/server-system-variables.html#sysvar_init_file) system variable was given to name a file of SQL statements, the server executes the statements in the file. This option enables you to perform custom bootstrapping sequences.

    When the server operates in bootstrap mode, some functionality is unavailable that limits the statements permitted in the file. These include statements that relate to account management (such as [`CREATE USER`](https://dev.mysql.com/doc/refman/8.0/en/create-user.html) or [`GRANT`](https://dev.mysql.com/doc/refman/8.0/en/grant.html)), replication, and global transaction identifiers.

7. The server exits.

## Start MySQL server

Start the MySQL server like this if your installation includes mysqld_safe:

```shell
bin/mysqld_safe --user=mysql &
```

It is important that the MySQL server be run using an unprivileged (non-root) login account. To ensure this, run mysqld_safe as root and include the --user option as shown. Otherwise, you should execute the program while logged in as mysql, in which case you can omit the --user option from the command.

### 推荐安装下初始化及启动：

```shell
bin/mysqld --initialize --user=mysql --datadir /usr/local/mysql/data ##初始化数据目录 
cp -f support-files/my-default.cnf /etc/my.cnf  ##将默认配置文件复制到指定目录 
bin/mysqld_safe --datadir=/usr/local/mysql/data --user=mysql & ##启动MySQL服务 
cp support-files/mysql.server /etc/init.d/mysql.server ##将MySQL加入到服务自启动 
/etc/init.d/mysql.server start ##通过服务启动MySQL 
```

#### MySQL启动相关参数 

• basedir = /usr/local/mysql ## 代表MySQL安装路径 
• datadir = /usr/local/mysql/data ## 代表MySQL的数据文件路径 
• port = 3306 ## 指定MySQL的侦听端口 
• log-error=/usr/local/mysql/data/M00006.err ## 记录MySQL启动日志和运行错误日志 

• character-set-server(默认是latin1) ## 指定MySQL的字符集 
• collation-server(默认是latin1_swedish_ci)  ## 指定MySQL的排序规则 
• default-storage-engine(默认是InnoDB) ## 指定MySQL的默认存储引擎 
• default-time-zone ## 指定默认时区，如果没有指定则和系统默认时区一致 
• open-files-limit(默认5000) ## 指定Mysqld运行过程中可以打开的文件数，避免出现” Too many open files”报错 
• pid-file=/usr/local/mysql/data/M00006.pid ## 指定Mysqld进程对应的程序ID文件，默认是在数据文件目录里 

• Skip-grant-tables ## 指定避开MySQL内部的权限表启动服务 
• Tmpdir ## 指定临时表文件的存放路径 

## 多实例启动

MySQL实例管理的主要资源是数据目录。每个实例都应该使用不同的数据目录，数据目录的位置用--datadir=dir_name选项指定。

除了使用不同的数据目录外，其他几个选项必须对每个服务器实例有不同的值：

- --port=port_num
    --port controls the port number for TCP/IP connections. Alternatively, if the host has multiple
    network addresses, you can set the bind_address system variable to cause each server to listen to a different address.
- --socket={file_name|pipe_name}
    --socket controls the Unix socket file path on Unix 

一种方法是在Unix上运行多个MySQL实例，就是用不同的默认TCP/IP端口和Unix套接字文件编译不同的服务器，使每个服务器在不同的网络接口上监听。在每个安装的不同基础目录中编译，也会自动导致每个服务器有一个单独的、编译后的数据目录、日志文件和PID文件位置。

Assume that an existing 5.7 server is configured for the default TCP/IP port number (3306) and
Unix socket file (/tmp/mysql.sock). To configure a new 8.0.24 server to have different operating
parameters, use a CMake command something like this:

```shell
shell> cmake . -DMYSQL_TCP_PORT=port_number \
             -DMYSQL_UNIX_ADDR=file_name \
             -DCMAKE_INSTALL_PREFIX=/usr/local/mysql-8.0.24
```

Here, port_number and file_name must be different from the default TCP/IP port number and Unix socket file path name, and the CMAKE_INSTALL_PREFIX value should specify an installation directory different from the one under which the existing MySQL installation is located.
If you have a MySQL server listening on a given port number, you can use the following command to find out what operating parameters it is using for several important configurable variables, including the base directory and Unix socket file name:

```shell
shell> mysqladmin --host=host_name --port=port_number variables
```

With the information displayed by that command, you can tell what option values not to use when configuring an additional server.

If you specify localhost as the host name, mysqladmin defaults to using a Unix socket file rather
than TCP/IP. To explicitly specify the transport protocol, use the --protocol={TCP|SOCKET|PIPE|
MEMORY} option. You need not compile a new MySQL server just to start with a different Unix socket file and TCP/IP port number. It is also possible to use the same server binary and start each invocation of it with different parameter values at runtime. One way to do so is by using command-line options:

```shell
shell> mysqld_safe --socket=file_name --port=port_number
```

To start a second server, provide different --socket and --port option values, and pass a --datadir=dir_name option to mysqld_safe so that the server uses a different data directory.

Alternatively, put the options for each server in a different option file, then start each server using a --defaults-file option that specifies the path to the appropriate option file. For example, if the optionfiles for two server instances are named /usr/local/mysql/my.cnf and /usr/local/mysql/
my.cnf2, start the servers like this: command:

```shell
shell> mysqld_safe --defaults-file=/usr/local/mysql/my.cnf
shell> mysqld_safe --defaults-file=/usr/local/mysql/my.cnf2
```

Another way to achieve a similar effect is to use environment variables to set the Unix socket file name and TCP/IP port number:

```shell
shell> MYSQL_UNIX_PORT=/tmp/mysqld-new.sock
shell> MYSQL_TCP_PORT=3307
shell> export MYSQL_UNIX_PORT MYSQL_TCP_PORT
shell> bin/mysqld --initialize --user=mysql
shell> mysqld_safe --datadir=/path/to/datadir &
```

This is a quick way of starting a second server to use for testing. The nice thing about this method is that the environment variable settings apply to any client programs that you invoke from the same shell. Thus, connections for those clients are automatically directed to the second server.

```mysql
mysql> show variables like '%sock%';
+-----------------------------------------+------------------+
| Variable_name                           | Value            |
+-----------------------------------------+------------------+
| mysqlx_socket                           | /tmp/mysqlx.sock |
| performance_schema_max_socket_classes   | 10               |
| performance_schema_max_socket_instances | -1               |
| socket                                  | /tmp/mysql.sock  |
+-----------------------------------------+------------------+
4 rows in set (0.03 sec)
```

```shell
netstat -an|grep LISTEN

tcp6       0      0 :::3306                 :::*                    LISTEN    
unix  2      [ ACC ]     STREAM     LISTENING     199651   /tmp/mysql.sock

tcp6       0      0 :::33060                :::*                    LISTEN     
unix  2      [ ACC ]     STREAM     LISTENING     199654   /tmp/mysqlx.sock
```

## QA

- 视图view是否负责存储数据？

    视图view是存放数据的一个接口，也可以说是虚拟的表。这些数据可以是从一个或几个基本表的数据，也可以是用户自己定义的数据。

    其实视图里面不存放数据的，数据还是放在基本表里面。基本表里面的数据发生变动时，视图里面的数据随之变动。

- 由于扩容问题需要将数据文件 data/ 转移到另外的磁盘(SS)，应该怎么做？

    假设将数据data转移到/home/data/目录下
    (1)关闭数据库服务
    `/etc/init.d/mysgl.server.stop`
    (2)将/usr/local/mysql/data的数据转移到/home/data/目录下

    `mv /usr/local/data /home/data/`

    (3)修改my.cnf中datadir=/home/data/data
    (4)开启数据库服务
    `/etc/init.d/mysgl.server start`

- 字段，数据库服务器，数据库实例，表，数据库之间是什么逻辑关系？

    字段为表中数据的组成部分；表为数据库对象中的一种
    数据库是存放具体数据的最大逻辑对象
    数据库实例可以包含多个数据库
    数据库服务器可以启动一个数据库实例或者多个数据库实例

# MySQL 授权

• bind-address(默认是*) 

• *代表接受所有来自IPV4、IPV6主机网卡的TCP/IP连接 
• 0.0.0.0代表接受所有来自IPV4主机网卡的TCP/IP的连接 
• 指定的IP如127.0.0.1，代表只接受此地址请求的TCP/IP连接 

```mysql
mysql> show variables like '%bind_address%';
+---------------------+-------+
| Variable_name       | Value |
+---------------------+-------+
| bind_address        | *     |
| mysqlx_bind_address | *     |
+---------------------+-------+
2 rows in set (0.00 sec)
```

- 权限系统的作用是授予来自某个主机的某个用户可以查询、插入、修改、删除等数据库操作的权限

- 不能明确的指定拒绝某个用户的连接 

- 权限控制(授权与回收)的执行语句包括create user, grant, revoke 

- 授权后的权限都会存放在MySQL的内部数据库中（数据库名叫mysql ）,并在数据库启动之后把权限信息复制到内存中

- MySQL用户的认证信息不光包括用户名，还要包含连接发起的主机

    (以下两个joe被认为不是同一个用户) 
    • SHOW GRANTS FOR ‘joe’@‘office.example.com’; 
    • SHOW GRANTS FOR 'joe'@'home.example.com'; 

```mysql
mysql> select user,host from mysql.user;
+------------------+-----------+
| user             | host      |
+------------------+-----------+
| mysql.infoschema | localhost |
| mysql.session    | localhost |
| mysql.sys        | localhost |
| root             | localhost |
+------------------+-----------+
4 rows in set (0.00 sec)
```

## 权限粒度

###### MySQL权限级别

- 全局性的管理权限，作用于整个MySQL实例级别 

- 数据库级别的权限，作用于某个指定的数据库上或者所有的数据库上 

- 数据库对象级别的权限，作用于指定的数据库对象上（表、视图等）或者所有的数据库对象上 

- 仅赋予查询id 这一列权限

    ```mysql
    mysql> grant select(id) on students.course to yi@localhost;
    ```

    

权限存储在mysql库的user, db, tables_priv, columns_priv, procs_priv这几个系统表中，待MySQL实例启动后就加载到内存中 

```mysql
mysql> show grants for 'mysql.sys'@'localhost';
+---------------------------------------------------------------+
| Grants for mysql.sys@localhost                                |
+---------------------------------------------------------------+
| GRANT USAGE ON *.* TO `mysql.sys`@`localhost`                 |
| GRANT TRIGGER ON `sys`.* TO `mysql.sys`@`localhost`           |
| GRANT SELECT ON `sys`.`sys_config` TO `mysql.sys`@`localhost` |
+---------------------------------------------------------------+
3 rows in set (0.00 sec)
```

库级权限
GRANT TRIGGER ON `sys`.* TO `mysql.sys`@`localhost`

表级权限
GRANT SELECT ON `sys`.`sys_config` TO `mysql.sys`@`localhost`

###### 对比root用户在几个权限系统表中的数据 

```mysql
mysql> select * from user where user='root' and host='localhost'; ##都是’Y’ 
mysql> select * from db where user='root' and host='localhost'; ##无记录 
mysql> select * from tables_priv where host='localhost' and user='root'; ##无记录 
mysql> select * from columns_priv where user='root' and host='localhost'; ##无记录 
mysql> select * from procs_priv where user='root' and host='localhost'; ##无记录 
```

## 常用权限

**• All/All Privileges权限代表全局或者全数据库对象级别的所有权限** 
• Alter权限代表允许修改表结构的权限，但必须要求有create和insert权限配合。如果是rename表名，则要求有alter和drop原表，create和insert新表的权限 
• Alter routine权限代表允许修改或者删除存储过程、函数的权限 
• Create权限代表允许创建新的数据库和表的权限 
• Create routine权限代表允许创建存储过程、函数的权限 
• Create tablespace权限代表允许创建、修改、删除表空间和日志组的权限 
**• Create temporary tables权限代表允许创建临时表的权限** 
• Create user权限代表允许创建、修改、删除、重命名user的权限 
• Create view权限代表允许创建视图的权限 

**• Delete权限代表允许删除行数据的权限** 
• Drop权限代表允许删除数据库、表、视图的权限，包括truncate table命令 
• Event权限代表允许查询，创建，修改，删除MySQL事件 
**• Execute权限代表允许执行存储过程和函数的权限** 
• File权限代表允许在MySQL可以访问的目录进行读写磁盘文件操作，可使用的命令包括load data infile,select … into outfile,load file()函数 
• Grant option权限代表是否允许此用户授权或者收回给其他用户你给予的权限 
• Index权限代表是否允许创建和删除索引 
**• Insert权限代表是否允许在表里插入数据，同时在执行analyze table,optimize table,repair table语句的时候也需要insert权限** 
• Lock权限代表允许对拥有select权限的表进行锁定，以防止其他链接对此表的读或写 

• Process权限代表允许查看MySQL中的进程信息，比如执行show processlist, mysqladmin processlist, show engine等命令 
• Reference权限是在5.7.6版本之后引入，代表是否允许创建外键 
• Reload权限代表允许执行flush命令，指明重新加载权限表到系统内存中，refresh命令代表关闭和重新开启日志文件并刷新所有的表 
• Replication client权限代表允许执行show master status,show slave status,show binary logs命令 
• Replication slave权限代表允许slave主机通过此用户连接master以便建立主从复制关系 
**• Select权限代表允许从表中查看数据，某些不查询表数据的select执行则不需要此权限，如Select 1+1，Select PI()+2；而且select权限在执行update/delete语句中含有where条件的情况下也是需要的** 
• Show databases权限代表通过执行show databases命令查看所有的数据库名 
• Show view权限代表通过执行show create view命令查看视图创建的语句 

• Shutdown权限代表允许关闭数据库实例，执行语句包括mysqladmin shutdown 
• Super权限代表允许执行一系列数据库管理命令，包括kill强制关闭某个连接命令，change master to创建复制关系命令，以及create/alter/drop server等命令 
• Trigger权限代表允许创建，删除，执行，显示触发器的权限 
**• Update权限代表允许修改表中的数据的权限** 
• Usage权限是创建一个用户之后的默认权限，其本身代表连接登录权限 

```mysql
mysql> create user abc@localhost; 
mysql> show grants for abc@localhost; 
+-----------------------------------------+ 
| Grants for abc@localhost                | 
+-----------------------------------------+ 
| GRANT USAGE ON *.* TO 'abc'@'localhost' | 
+-----------------------------------------+   
```

## MySQL修改权限的生效

• 执行Grant,revoke,set password,rename user命令修改权限之后，MySQL会**自动**将修改后的权限信息同步加载到系统内存中 

• 如果执行insert/update/delete操作上述的系统权限表之后，则**必须再执行刷新权限命令**才能同步到系统内存中，刷新权限命令包括：flush privileges/mysqladmin flush-privileges/mysqladmin reload 

• 如果是修改tables和columns级别的权限，则客户端的下次操作新权限就会生效 

• 如果是修改database级别的权限，则新权限在客户端执行use database命令后生效 

• 如果是修改global级别的权限，则需要重新创建连接新权限才能生效 

• --skip-grant-tables可以跳过所有系统权限表而允许所有用户登录，只在特殊情况下暂时使用 

```mysql
mysql> create role app_readonly;
Query OK. 0 rows affected (0.08 sec)
mysql> grant select on *.* to app_readonly;
Query OK. 0 rows affected (0.04 sec)
mysql> create user app@localhost identified by "mysql";
Query OK. 0 rows affected (0.10 sec)
mysql> grant app_readonly to app@localhost;
Query OK, 0 rows affected (0.02 sec)
```

# SQL Statement

## create table

- Temporary关键词表示创建的是临时表，临时表仅对本链接可见，另外的数据库链接不可见，当本链接断开时，临时表也自动被drop掉 

    ```mysql
    mysql> create temporary table temp1(id int,name varchar(10));
    Query OK, 0 rows affected (0.00 sec) 
    ```

- Like关键词表示基于另外一个表的定义复制一个新的空表，空表上的字段属性和索引都和原表相同
  
- Create table … as select语句表示创建表的同时将select的查询结果数据插入到表中，但索引和主外键信息都不会同步过来

    Ignore和replace表示在插入数据的过程中如果新表中碰到违反唯一约束的情况下怎么处理，ignore表示不插入，replace表示替换已有的数据，默认两个关键词都不写则碰到违反的情况会报错 

- auto_increment表示字段为整数或者浮点数类型的value+1递增数值，value为当前表中该字段最大的值，默认是从1开始递增；一个表中只容许有一个自增字段，且该字段必须有key属性，不能含有default属性，且插入负值会被当成很大的正数 

- `unique index` vs `primary key` 

    主键字段必须非空，但唯一键可以创建在可空字段上
一个表只能有一个主键，但可以有多个唯一键
    
- 外键的作用是什么？请举例说明

    用来限制子表的数据一定来源于父表，在修改父表或子表的数据时也要遵循这个原则

## insert

- insert…values和insert…set两种语句都是将指定的数据插入到现成的表中

    而insert…select语句是将另外表中数据查出来并插入到现成的表中 

    当目标表和select语句中的表相同时，则会先将select语句的结果存放在临时表中，再插入到目标表中(注意执行顺序) 
    `mysql> insert into students select * from students; `

- Insert…values和insert…select语句的执行结果如下 
    `Records: 100 Duplicates: 0 Warnings: 0`

    Records代表此语句操作了多少行数据，但不一定是多少行被插入的数据，因为如果存在相同的行数据且违反了某个唯一性，则duplicates会显示非0数值，warning代表语句执行过程中的一些警告信息 

    low_priority关键词代表如果有其他链接正在读取目标表数据，则此insert语句需要等待读取完成 

    low_priority和high_priority关键词仅在MyISAM, MEMORY, and MERGE三种存储引擎下才生效 

    Ignore关键词代表insert语句如果违反主键和唯一键的约束条件，则不报错而只产生警告信息，违反的行被丢弃，而不是整个语句回退；在数据类型转换有问题时如果有ignore则只产生警告信息，而不是语句回退 

- 当insert语句中使用on duplicate key update子句时，如果碰到当前插入的数据违反主键或唯一键的唯一性约束，则Insert会转变成update语句修改对应的已经存在表中的这条数据。

    比如如果a字段有唯一性约束且已经含有1这条记录，则以下两条语句的执行结果相同 

    `INSERT INTO table (a,b,c) VALUES (1,2,3) ON DUPLICATE KEY UPDATE c=c+1; `

    `UPDATE table SET c=c+1 WHERE a=1; `

    On duplicate key update子句后面可以跟多个修改，用逗号隔开 

    上述例子中如果b字段也有唯一性约束，则与此语句的执行结果相同，但一般应该避免出现对应多条的情况 

    `UPDATE table SET c=c+1 WHERE a=1 OR b=2 LIMIT 1; `

## update

- 以下语句的col1只会比原值增加1

    `UPDATE t1 SET col1 = col1 + 1;`

    以下语句的col2和col1的结果是一样的 
    `UPDATE t1 SET col1 = col1 + 1, col2 = col1;`

- Order by指定update数据的顺序，在某些情况下可以避免错误的发生，比如t表中的id字段是有唯一约束的，则以下第一个语句执行会报错，而第二个语句执行则正常 

    `UPDATE t SET id = id + 1;`
    `UPDATE t SET id = id + 1 ORDER BY id DESC;`

    ```mysql
    mysql> update students set id=id+1; ##执行报错 
    ## 1+1=2,会与2重复
    ERROR 1062 (23000): Duplicate entry '2' for key 'PRIMARY'
    
    mysql> update students set id=id+1 order by id desc; ##执行成功 
    Query OK, 2 rows affected (0.04 sec) 
    Rows matched: 2  Changed: 2  Warnings: 0 
    ```

- 多表修改举例（表之间通过where条件进行join操作） 
    `UPDATE items,month SET items.price=month.price WHERE items.id=month.id;`

## select

- Select_expr也可以使用MySQL内部的函数，另外字段也可以使用别名 
  
    ```mysql
    SELECT CONCAT(last_name,', ',first_name) AS full_name 
        FROM mytable ORDER BY full_name; 
    • SELECT CONCAT(last_name,', ',first_name) full_name 
        FROM mytable ORDER BY full_name; 
    ```
    
- Where条件中不能使用select_expr中定义的字段别名，因为语句执行顺序是where在select之前，所以where在执行时字段别名未知 

- For update关键词代表将查询的数据行加上写锁，直到本事务提交为止 

- Lock in share mode关键词代表将查询的数据行加上读锁，则其他的链接可以读相同的数据但无法修改加锁的数据 

- **ALL/Distinct关键词代表是否将查询结果中完全重复的行都查询出来，ALL是默认值代表都查询出来，指定distinct代表重复行只显示一次** 

- Straight_join关键词代表强制优化器在表连接操作时按照语句中from子句中的表的顺序执行 

- Sql_big_result/sql_small_result通常是和group by/distinct一起使用，其作用是事先告诉优化器查询结果是大还是小，以便优化器事先准备好将查询结果存放在磁盘临时表或者快速临时表中以便后续操作 

- Sql_buffer_result强制将查询结果存入临时表中 

- Sql_calc_found_rows关键词代表要求查询结果的同时计算结果的行数，以便后续通过SELECT FOUND_ROWS()直接获取行数 

- **Sql_cache/sql_no_cache代表是否直接从query cache中获取查询结果** 

- 当inner join或者表之间用逗号隔开，且没有表之间的关联字段，则代表结果是两者的笛卡尔积 

- ON conditional_expr子句一般代表指定两个表之间的关联条件，而where条件中指定查询结果的筛选条件 

- STRAIGHT_JOIN和Join的用法大致相同，唯一不同是确保左表是先被读取的，以保证优化器的读取顺序 

- union 第一个select语句的column_name会被当做最后查询结果的列名，接下来的每个select语句所一一对应的列应该和第一个语句的列的**数据类型最好保持一致** 

- 默认情况下**union语句会把最终结果中的重复行去掉**，这和增加distinct这个关键词的作用一样，如果使用**union all则代表最终结果中的重复行保留** 

## create view

- Create view语句是指将某个查询数据的定义保留下来，以便随时调用

    比如`create view v1 as select id,name,gender from student;` ,v1中记录的是id,name,gender的一组数据的定义 

    ```mysql
    +-----+------+--------+ 
    | id  | name | gender | 
    +-----+------+--------+ 
    ```

- view本身不存储查询结果，只是一个定义 

- **当视图被创建之后，则其定义就已经固定不会再改变，比如一个视图是由select *创建的，则后续对表增加的字段不会成为视图的一部分，而后续对表删除字段则会导致查询视图失败** 

- Order by子句在创建视图过程中是允许的，但当后续的查询视图的语句中有自己的order by子句时则会被忽略掉 
- 视图在满足特定条件时是可以执行insert/update/delete语句的，条件就是视图中的每一行和视图对应的表中的每行数据都能一一对应起来 

## truncate table

- Truncate table语句用来删除/截断表里的所有数据 
- 和delete删除所有表数据在逻辑上含义相同，但性能更快
- 类似执行了drop table和create table两个语句 

# 常用函数

## 聚合函数

用在存在group by子句的语句中 

- AVG([DISTINCT] expr) 

    计算expr的平均值，distinct关键词表示是否排除重复值 

    ```mysql
    SELECT student_name, AVG(test_score) FROM student GROUP BY student_name; 
    ```
    
- COUNT(expr) 

    计算expr中的个数，如果没有匹配则返回0，注意NULL的区别 
    
    ```mysql
    SELECT student.student_name,COUNT(*) FROM student,course WHERE student.student_id=course.student_id GROUP BY student_name; 
    ```
    
- COUNT(DISTINCT expr,[expr...]) 
    计算有多少个不重复的expr值，注意是计算非NULL的个数 
    

`SELECT COUNT(DISTINCT results) FROM student; `
    
- MAX([DISTINCT] expr),MIN([DISTINCT] expr) 
  • 返回expr中最大或者最小的值 

  ```mysql
  SELECT student_name, MIN(test_score), MAX(test_score) FROM student GROUP BY student_name; 
  ```

- SUM([DISTINCT] expr) 
• 返回expr的求和值 

# procedure function trigger

## procedure & function

• 函数与存储过程最大的区别就是函数调用有返回值，调用存储过程用call语句，而调用函数就直接引用函数名+参数即可 

• IN,OUT,INOUT三个参数前的关键词只适用于存储过程，对函数而言所有的参数默认都是输入参数 

• IN输入参数用于把数值传入到存储过程中；OUT输出参数将数值传递到调用者，初始值是NULL；INOUT输入输出参数把数据传入到存储过程，在存储过程中修改之后再传递到调用者 

### label

• 标签label可以加在begin…end语句以及loop, repeat和while语句 

• 语句中通过iterate和leave来控制流程，iterate表示返回指定标签位置，leave表示跳出标签 

### declare

• Declare语句通常用来声明本地变量、游标、条件或者handler 

• Declare语句只允许出现在begin … end语句中而且必须出现在第一行 

• Declare的顺序也有要求，通常是先声明本地变量，再是游标，然后是条件和handler 

- Declare condition语句命名特定的错误条件，而该特定错误可以在declare…handler中指定处理方法 

    • Condition_value指定特定的错误条件，可以有以下两种形式 
    • `Mysql_err_code`表示MySQL error code的整数 
    • SQLSTATE `sqlstate_value`表示MySQL中用5位字符串表达的语句状态 
    • 比如在MySQL中1051error code表示的是unknown table的错误，如果要对这个错误做特殊处理，可以用三种方法： 

    ```mysql
    DECLARE CONTINUE HANDLER FOR 1051 
      BEGIN 
        -- body of handler 
      END; 
    
    DECLARE no_such_table CONDITION FOR 1051; 
    DECLARE CONTINUE HANDLER FOR no_such_table 
      BEGIN 
        -- body of handler 
      END; 
    
    DECLARE no_such_table CONDITION FOR SQLSTATE '42S02'; 
    DECLARE CONTINUE HANDLER FOR no_such_table 
      BEGIN 
        -- body of handler 
      END; 
    ```

- Declare handler语句用来声明一个handler来处理一个或多个特殊条件，当其中的某个条件满足时则触发其中的statement语句执行 

    • Handler_action子句声明当执行完statement语句之后应该怎么办 
    • Continue代表继续执行该存储过程或函数 
    • Exit代表退出声明此handler的begin…end语句块 
    • Undo参数已经不支持 

    • Condition_value的值有以下几种： 
    • Mysql_err_code表示MySQL error code的整数 
    • SQLSTATE sqlstate_value表示MySQL中用5位字符串表达的语句状态 
    • Condition_name表示之前在declare…condition语句中声明的名字 
    • SQLWARNING表示所有的警告信息，即SQLSTATE中01打头的所有错误 
    • NOT FOUND表示查完或者查不到数据，即SQLSTATE中02打头的所有错误 
    • SQLEXCEPTION表示所有的错误信息 

### cursor

Cursor游标用来声明一个数据集 

- Cursor close语句用来关闭之前打开的游标 

    • 如果关闭一个未打开的游标，则MySQL会报错 
    • 如果在存储过程和函数中未使用此语句关闭已经打开的游标，则游标会在声明的begin…end语句块执行完之后自动关闭 

- Cursor declare语句用来声明一个游标和指定游标对应的数据集合，通常数据集合是一个select语句 

- Cursor fetch语句用来获取游标指定数据集的下一行数据并将各个字段值赋予后面的变量 

    • 数据集中的字段需要和INTO语句中定义的变量一一对应 
    • 数据集中的数据都fetch完之后，则返回NOT FOUND 

## trigger

• create trigger语句用来创建一个触发器，触发器的作用是当表上有对应SQL语句发生时，则触发执行 

• Trigger_time指定触发器的执行时间，BEFORE和AFTER指定触发器在表中的每行数据修改前或者后执行 
• Trigger_event指定触发该触发器的具体事件 
• INSERT当新的一行数据插入表中时触发，比如通过执行insert,load data,replace语句插入新数据 
• UPDATE当表的一行数据被修改时触发，比如执行update语句时 
• DELETE当表的一行数据被删除时触发，比如执行delete,replace语句时 
• 当执行insert into … on duplicate key update语句时，当碰到重复行执行update时，则触发update下的触发器 
• 从5.7.2版本开始，可以创建具有相同trigger_time和trigger_event的同一个表上的多个触发器，默认情况下按照创建的时间依次执行，通过指定FOLLOWS/PRECEDES改变执行顺序，即FOLLOWS时表示新创建的触发器后执行，PRECEDES则表示新触发器先执行 

• Trigger_body表示触发器触发之后要执行的一个或多个语句，在内部可以引用涉及表的字段，`OLD.col_name`表示行数据被修改或删除之前的字段数据，`NEW.col_name`表示行数据被插入或修改之后的字段数据 

# 数据类型

## 整数

`int(6) zerofill;` : 000001，不是限制值的合法范围

## 日期时间类型

- Timestamp和datetime日期时间类型可以被自动初始化和更新为当前的日期时间数据，
    当默认指定current timestamp为默认值，或者指定此数据列为自动更新时 

- 指定默认值是指当插入新的数据而该列没有显视指定数值时，则插入当前日期时间值 
- 指定自动更新是指当行中的其他列被更新时，则此列被自动更新为当前日期时间值 

## 字符类型

• 字符类型包含char, varchar, binary, varbinary, blob, text, enum和set 

• Char和varchar可以通过char(M)和varchar(M)指定可以存储的最大字符数，比如char(30)表示可以存储最长30个字符Char类型的长度一旦指定就固定了，其范围可以是0到255，当被存储时，未达到
指定长度的则在值右边填充空格，而获取数据时则会把右侧的空格去掉 

• Varchar类型是变长的类型，其范围可以是0到65535，当存储是未达到指定长度则不填充空格 

• Varchar类型用来存储可变长字符串，是最常见的字符串数据类型，它比定长类型更节省空间，因为它仅使用必要的空间。 

• 另外varchar需要使用1或2个额外字节记录字符串的长度，如果列的最大长度小于等于255字节时，需要1个字节，否则需要2个字节。比如采用Latin1字符集，varchar(10)的列需要11个字节的存储空间，而varchar(1000)列需要1002个字节的存储空间。 

• varchar节省了存储空间，所以对性能也有帮助。但由于行是变长的，在update时可能使行变得比原来更长，这就导致需要做额外的工作。如果一行占用的空间增长，并且物理数据页内没有更多
空间存储时，MyISAM会将行拆成不同的片段存储，InnoDB需要分列页来讲行放到数据页里。 

• char类型是定长，MySQL总是根据定义的字符串长度分配足够的空间。当查询char值时，MySQL会删除所有的末尾空格 

• char适合存储很短的字符串，或者所有值都接近同一个长度。对于经常变更的数据，char也比varchar更好，因为定长的char类型不容易产生碎片。而且对非常短的字符串，char不需要一个额外的字节记录长度 

• Char类型值右边的空格会被自动剔除，而varchar类型则不会 

## 枚举类型

• Enum枚举类型是字符串类型，其值是从事先指定的一系列值中选出，适用在某列的取值范围已经
固定 
• 主要好处为MySQL在存储此类数据时，直接转化成数字存储而不是字符串，可以节省空间，并且在表的.frm文件中存储“数字-字符串”之间的对应关系 

```mysql
CREATE TABLE shirts ( 
    name VARCHAR(40), 
    size ENUM('x-small', 'small', 'medium', 'large', 'x-large') 
); 
```

• 另外枚举类型的排序规则是按照存储顺序进行而不是按照值本身排序的 

## 如何选择

- MySQL支持的数据类型很多，选择正确的数据类型对获得高性能至关重要 
    更小的通常更好 
    尽量使用可以正确存储数据的最小数据类型。更小的数据类型通常更快，因为它们占用更小的磁盘、内存和CPU缓存，并且处理时需要的CPU时间也更少 

- 简单的数据类型操作通常需要更少的CPU周期。例如整型比字符操作代价更低，因为字符集和排序规则使得字符比较比整型比较更复杂 
    尽量避免NULL 
    通常情况下最好指定列为NOT NULL。因为如果查询中包含可为NULL的列，对MySQL来说更难优化，因为可为NULL的列使得索引、索引统计和值比较都更为复杂。当可为NULL的列被索引时，每个索引记录需要一个额外的字节，所以会使用更多的存储空间  

## 自增长类型字段

• 整型和浮点型字段可以被指定为自增长类型字段，意味着当插入行数据时这列为NULL时，则按照此列最大值+1的方式插入数据 
• 获取插入后的自增长列的值，可以用LAST_INSERT_ID()函数获取 
• 一个表中只能有一个自增长字段，且不能含有默认值 
• 自增长字段的数值从1开始递增，且不能插入负值 

# InnoDB

## ACID

ACID模型是关系型数据库普遍支持的事务模型，用来保证数据的一致性，其中的ACID分别代表： 
A：atomicity原子性：事务是一个不可再分割的工作单位，事务中的操作要么都发生，要么都不发生 
C：consistency一致性：事务开始之前和事务结束以后，数据库的完整性约束没有被破坏。这是说数据库事务不能破坏关系数据的完整性以及业务逻辑上的一致性 
I：isolation独立性：多个事务并发访问时，事务之间是隔离的，一个事务不应该影响其它事务运行效果 
D：durability持续性：在事务完成以后，该事务所对数据库所作的更改便持久的保存在数据库之中，并不会被回滚 

举例来说，比如银行的汇款1000元的操作，简单可以拆分成A账户的余额-1000，B账户的余额+1000，还要分别在A和B的账户流水上记录余额变更日志，这四个操作必须放在一个事务中完成，否则丢失其中的任何一条记录对整个系统来说都是不完整的。 
对上述例子来说，原子性体现在要么四条操作每个都成功，意味着汇款成功，要么其中某一个操作失败，则整个事务中的四条操作都回滚，汇款失败；一致性表示当汇款结束时，A账户和B账户里的余额变化和操作日志记录是可以对应起来的；独立性表示当汇款操作过程中如果有C账户也在往B账户里汇款的话，两个事务相互不影响，即A->B有四个独立操作，C->B有四个独立操作；持久性表示当汇款成功时，A和B的余额就变更了，不管是数据库重启还是什么原因，该数据已经写入到磁盘中作为永久存储，不会再变化，除非有新的事务 

其中事务的隔离性是通过MySQL锁机制实现 
原子性，一致性，持久性则通过MySQL的redo和undo日志记录来完成 

## MVCC

为保证并发操作和回滚操作，InnoDB会将修改前的数据存放在回滚段中。 InnoDB会在数据库的每一行上额外增加三个字段以实现多版本控制，第一个字段是DB_TRX_ID用来存放针对该行最后一次执行insert、update操作的事务ID，而delete操作也会被认为是update，只是会有额外的一位来代
表事务为删除操作；第二个字段是DB_ROLL_PTR指针指向回滚段里对应的undo日志记录；第三个字段是DB_ROW_ID代表每一行的行ID。 回滚段中的undo日志记录只有在事务commit提交之后才会被丢弃，为避免回滚段越来越大，要注意及时执行commit命令 

初始数据行的情况，六个字段的值分别是1,2,3,4,5,6 

![image-20210107204848335](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210107204848335.png)

事务1修改该数据行，将六个字段的值分别*10，并生成回滚日志记录 
事务2读取该数据行 

![image-20210107204914061](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210107204914061.png)

事务2按照自己的事务ID和行数据中的事务ID做对比，并按照事务隔离级别选取事务1修改前的回滚段中的数据返回 

```mysql
在两个数据库链接下实验多版本控制 
链接1：mysql> start transaction;

链接2：mysql> start transaction;

链接1：mysql> update score set score=88 where sid=1;

链接2：mysql> select * from score where sid=1; ###链接1锁数据未释放，链接2也能访问相同数据
+------+-----------+-------+ 
| sid  | course_id | score | 
+------+-----------+-------+ 
|    1 |         1 |    90 | 
|    1 |         2 |    90 | 
|    1 |         3 |    90 | 
|    1 |         4 |    90 | 

链接1：mysql>commit; 

链接2：mysql> select * from score where sid=1; ###链接1锁释放，但链接2访问到的数据依然是之前的数据 
+------+-----------+-------+ 
| sid  | course_id | score | 
+------+-----------+-------+ 
|    1 |         1 |    90 | 
|    1 |         2 |    90 | 
|    1 |         3 |    90 | 
|    1 |         4 |    90 | 

链接2：mysql> commit; 

链接2：mysql> select * from score where sid=1; ###链接2提交之后，再访问到的数据是修改后的数据 
+------+-----------+-------+ 
| sid  | course_id | score | 
+------+-----------+-------+ 
|    1 |         1 |    88 | 
|    1 |         2 |    88 | 
|    1 |         3 |    88 | 
|    1 |         4 |    88 | 
```

