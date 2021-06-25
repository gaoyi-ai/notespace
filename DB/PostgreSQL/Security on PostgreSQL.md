---
title: Security on PostgreSQL
categories:
- DB
- PostgreSQL
tags:
- SQL
- Security
date: 2021/6/25
---



> [goteleport.com](https://goteleport.com/blog/securing-postgres-postgresql)

Introduction
------------

Databases are the Holy Grail for hackers, and as such, must be protected with utmost care. This is the first in a series of articles in which we’ll give an overview of best practices for securing your databases. We’re starting with one of the most popular open-source databases, PostgreSQL, and will go over several levels of security you’d need to think about:

*   Network-level security
*   Transport-level security
*   Database-level security

Network-Level Security for PostgreSQL
-------------------------------------

### Firewalls

In the ideal world, your PostgreSQL server would be completely isolated and not allow any inbound connections, SSH or psql. Unfortunately, this sort of air-gapped setup is not something PostgreSQL supports out-of-the-box.
理想情况下，PostgreSQL 服务器应当是完全隔离，不允许任何入站申请、SSH 或 psql 的。然而，PostgreSQL 没有对这类网闸设置提供开箱即用的支持。

The next best thing you can do to improve the security of your database server is to lock down the port-level access to the node where the database is running with a firewall. By default, PostgreSQL listens on a TCP port 5432. Depending on the operating system, there may be different ways to block other ports. But using Linux’s most widely available `iptables` firewall utility, the following will do the trick:
我们最多也只能通过设置防火墙，锁定数据库所在节点的端口级访问来提升数据库服务器的安全性。默认情况下，PostgreSQL 监听 TCP 端口 5432。而根据操作系统的不同，锁定其他端口的方式也会有所不同。以 Linux 最常用的防火墙`iptables`为例，下面这几行代码就可轻松完成任务：

```
# Make sure not to drop established connections.
iptables -A INPUT -m state --state ESTABLISHED,RELATED -j ACCEPT

# Allow SSH.
iptables -A INPUT -p tcp -m state --state NEW --dport 22 -j ACCEPT

# Allow PostgreSQL.
iptables -A INPUT -p tcp -m state --state NEW --dport 5432 -j ACCEPT

# Allow all outbound, drop everything else inbound.
iptables -A OUTPUT -j ACCEPT
iptables -A INPUT -j DROP
iptables -A FORWARD -j DROP
```

Note: When updating the iptables rules, it is a good idea to use the [iptables-apply](https://man7.org/linux/man-pages/man8/iptables-apply.8.html) tool which automatically rolls back the changes in case you lock yourself out.
在更新 iptables 的规则时，建议使用 iptables-apply 工具。这样，哪怕你不小心把自己也锁在外面了，它也能自动回滚更改。

The PostgreSQL rule above will allow anyone to connect to port 5432. You could make it more strict by only accepting connections from certain IP addresses or subnets:
这条 PostgreSQL 规则会允许所有人连接到端口 5432 上。当然，你也可以将其修改为只接受特定 IP 地址或子网让限制更加严格：

```
# Only allow access to PostgreSQL port from the local subnet.
iptables -A INPUT -p tcp -m state --state NEW --dport 5432 -s 192.168.1.0/24 -j ACCEPT
```

Going back to our ideal scenario, being able to entirely prevent inbound connections to port 5432 would require a some sort of a local agent that maintains a persistent outbound connection to the client node(-s) and has the ability to proxy traffic to the local PostgreSQL instance.
继续讨论我们的理想情况。想要完全限制到 5432 端口的所有入站连接需要一个某种类型的本地代理，由它维持一个到客户端节点的持久出站连接，并且能将流量代理到本地 PostgreSQL 实例。

This technique is called “reverse tunneling” and can be demonstrated using SSH remote port forwarding feature. You can open up a reverse tunnel by running the following command from the node where your PostgreSQL database is running:
这种代理被称作是 “反向通道”。具体使用方法可以通过 SSH 远程端口转发的功能进行演示。运行下列指令可以在 PostgreSQL 数据库运行的节点上打开一个反向通道：

```
ssh -f -N -T -R 5432:localhost:5432 user@<client-host>
```

Of course, the `<client-host>` should be accessible from the PostgreSQL node and have the SSH daemon running. The command will forward the port 5432 on the database server to the port 5432 on the client machine, and you will be able to connect to the database over the tunnel:
PostgreSQL 的节点需要能访问，其上的 SSH 守护进程（daemon）也要处于运行状态。下列指令会将数据库服务器上端口 5432 转发到客户端机器的端口 5432 上，这样你就可以通过这个通道连接数据库了：  

```
psql "host=localhost port=5432 user=postgres db
```

### PostgreSQL Listen Addresses

It is a good practice to restrict addresses on which the server is listening for client connections using the `listen_addresses` configuration file directive. If the node PostgreSQL is running on has multiple network interfaces, use it to make sure the server is only listening on the interface(-s) over which the clients will be connecting to it:
通过配置文件指令`listen_addresses`来限制服务器监听客户端连接的地址是个好习惯。如果运行 PostgreSQL 的节点上有多个网络接口，`listen_addresses`可以确保服务器只会监听客户端所连接的一个或多个接口：

```
listen_addresses = 'localhost, 192.168.0.1'
```

If the clients connecting to the database always reside on the same node (or, say, co-located in the same Kubernetes pod with PostgreSQL running as a side-car container), disabling TCP socket listening can completely eliminate network from the picture. Setting listen addresses to an empty string makes the server accept only Unix-domain socket connections:
如果连接到数据库的客户端总是驻留在同一节点上，或是与数据库共同驻留在同一个 Kubernetes pod 上，PostgreSQL 作为 sidecar 容器运行，禁用套接字监听（socket）可以完全消除网络的影响。将监听地址设置为空字符串可以使服务器只接受 Unix 域的套接字连接：

```
listen_addresses = ''
```

PostgreSQL Transport-Level Security
-----------------------------------

With the majority of the world’s web moving to HTTPs, there’s little excuse for not using a strong transport encryption for database connections as well. PostgreSQL supports TLS (which is still referred to as SSL in the documentation, configuration and CLI for legacy reasons) natively and provides ways to use it for both server and client authentication.
当世界上大部分的网络都转投向 HTTP 的怀抱时，为数据库连接选择强传输加密也变成了一个必备项目。PostgreSQL 本身即支持 TLS（因为历史遗留问题，在文档、配置文件，以及 CLI 中仍被称作是 SSL），我们也可以使用它进行服务器端和客户端认证。

### Server TLS

For server authentication, you first need to obtain a certificate the server will present to the connecting clients. Let’s Encrypt makes it really easy to get free X.509 certificates, for example using the [certbot](https://certbot.eff.org/) CLI tool:
对于服务器的认证，我们首先需要为服务器准备一份用于和相连接的客户端认证的证书。在 Let's Encrypt 上，我们可以找到免费提供的 X.509 证书，具体使用方法以 certbot 的命令行工具为例：

```
certbot certonly --standalone -d postgres.example.com
```

Keep in mind that by default certbot uses [HTTP-01](https://letsencrypt.org/docs/challenge-types/#http-01-challenge) ACME challenge to validate the certificate request which requires a valid DNS for the requested domain pointing to the node and port 80 to be open.
需要注意的是，certbot 默认使用 ACME 规范的 HTTP-01 挑战来验证证书请求，这里我们就需要确保请求域指向节点的 DNS 有效，并且端口 80 处于开放状态。

If you can’t use Let’s Encrypt for some reason and want to generate all secrets locally, you can do it using openssl CLI tool:
除了 Let's Encrypt 之外，如果想在本地生成所有的信息，我们还可以选择 openssl 命令行工具：

```
# Make a self-signed server CA.
openssl req -sha256 -new -x509 -days 365 -nodes \
    -out server-ca.crt \
    -keyout server-ca.key

# Generate server CSR. Put the hostname you will be using to connect to
# the database in the CN field.
openssl req -sha256 -new -nodes \
    -subj "/CN=postgres.example.com" \
    -out server.csr \
    -keyout server.key

# Sign a server certificate.
openssl x509 -req -sha256 -days 365 \
    -in server.csr \
    -CA server-ca.crt \
    -CAkey server-ca.key \
    -CAcreateserial \
    -out server.crt
```

Of course, in the production environment you’d want to make sure that these certificates are updated prior to their expiration date.
在生产环境中记得在证书过期前更新。

### Client TLS

Client certificate authentication allows the server to verify the identity of a connecting client by validating that the X.509 certificate presented by the client is signed by a trusted certificate authority.
通过验证客户端提供的 X.509 证书是由可信的证书颁发机构（CA）签名，服务器便可以验证连接客户端的身份。

It’s a good idea to use different certificate authorities to issue client and server certificates, so let’s create a client CA and use it to sign a client certificate:
建议使用不同的 CA 来分别给客户端和服务器端发证书。下列代码创建了一个客户端 CA，并用它来给客户签证书：

```
# Make a self-signed client CA.
openssl req -sha256 -new -x509 -days 365 -nodes \
    -out client-ca.crt \
    -keyout client-ca.key

# Generate client CSR. CN must contain the name of the database role you
# will be using to connect to the database.
openssl req -sha256 -new -nodes \
    -subj "/CN=alice" \
    -out client.csr \
    -keyout server.key

# Sign a client certificate.
openssl x509 -req -sha256 -days 365 \
    -in client.csr \
    -CA client-ca.crt \
    -CAkey client-ca.key \
    -CAcreateserial \
    -out client.crt
```

Note that the CommonName (CN) field of the client certificate must contain the name of the database account the client is connecting to. PostgreSQL server will use it to establish the identity of the client.
这里需要注意，客户端证书里的 CommonName（CN）字段必须要包含用于连接的数据库账号。PostgreSQL 服务器将用 CN 来创建客户端的身份。

### TLS Configuration

Getting all the pieces together, you can now configure the PostgreSQL server to accept TLS connections:

```
ssl = on
ssl_cert_file = '/path/to/server.crt'
ssl_key_file = '/path/to/server.key'
ssl_ca_file = '/path/to/client-ca.crt'

# This setting is on by default but it’s always a good idea to
# be explicit when it comes to security.
ssl_prefer_server_ciphers = on

# TLS 1.3 will give the strongest security and is advised when
# controlling both server and clients.
ssl_min_protocol_version = 'TLSv1.3'
```

One last remaining bit of configuration is to update the PostgreSQL server host-based authentication file (`pg_hba.conf`) to require TLS for all connections and authenticate the clients using X.509 certificates:
我们还需要再配置的就只剩下用于更新 PostgreSQL 服务器的基于主机的认证文件（`pg_hba.conf`）了。它可以要求所有的连接使用 TLS，并通过 X.509 证书对客户端进行认证。

```
# TYPE  DATABASE        USER            ADDRESS                 METHOD
hostssl all             all             ::/0                    cert
hostssl all             all             0.0.0.0/0               cert
```

Now, clients connecting to the database server will have to present a valid certificate signed by the client certificate authority:
现在，连接到数据库服务器的客户端就需要提供由客户端 CA 签名的有效证书了：

```
psql "host=postgres.example.com \
      user=alice \
      dbname=postgres \
      sslmode=verify-full \
      sslrootcert=/path/to/server-ca.crt \
      sslcert=/path/to/client.crt \
      sslkey=/path/to/client.key"
```

Note that by default psql will not perform the server certificate validation so “sslmode” must be set to `verify-full` or `verify-ca`, depending on whether you’re connecting to the PostgreSQL server using the same hostname as encoded in its X.509 certificate’s CN field.
需要注意的是，默认情况下，psql 不会进行服务器证书认证，所以我们需要将 “sslmode” 设置为“`verify-full`” 或者 “`verify-ca`”。具体设置要看你是否使用了编码后的 X.509 中 CN 字段的主机名对服务器进行连接。

To reduce the command verbosity and not have to enter the paths to TLS secrets every time you want to connect to a database, you can use a PostgreSQL connection service file. It allows you to group connection parameters into “services” which can then be referred to in the connection string via a “service” parameter.
为了简化指令，并且避免在每次连接到数据库时都要重新输入一遍到 TLS 的路径，可以通过使用 PostgreSQL 的连接服务文件来解决。它可以将连接参数分组到 “services” 中，通过连接字符串中的 “service” 参数进行引用。

Create `~/.pg_service.conf` with the following content:

```
[example]
host=postgres.example.com
user=alice
sslmode=verify-full
sslrootcert=/path/to/server-ca.crt
sslcert=/path/to/client.crt
sslkey=/path/to/client.key
```

Now, when connecting to a database, you’d only need to specify the service name and the name of the database you want to connect to:
现在，当连接到数据库时，我们只需要指定服务名和想要连接的数据库名即可：

```
psql "service=example db
```

Database-Level Security
-----------------------

### Roles Overview

So far we have explored how to protect the PostgreSQL database server from unauthorized network connections, use strong transport encryption and make sure that server and clients can trust each other’s identities with mutual TLS authentication. Another piece of the puzzle is to figure out what users can do and what they have access to once they’ve connected to the database and had their identity verified. This is usually referred to as authorization.
目前为止，我们已经探讨的内容有以下几点：如何从未认证的网络连接中保护 PostgreSQL 数据库，如何使用强加密进行数据传输，以及如何通过共同的 TLS 认证让服务器与客户互相信任对方身份。接下来，我们将继续分析用户在这里能做什么，连接到数据库后他们可以访问什么，以及如何验证他们的身份。这一步通常被称作是 “授权”。

PostgreSQL has a comprehensive user permissions system that is built around the concept of roles. In modern PostgreSQL versions (8.1 and newer) a “role” is synonymous with “user” so any database account name you use, say, with psql (e.g. “user=alice”) is actually a role with a `LOGIN` attribute that lets it connect to a database. In fact, the following SQL commands are equivalent:
PostgreSQL 有一套完整的、依据角色（role）建立的用户权限系统。在现代的 PostgreSQL（8.1 及以上版本）中，“角色”是 “用户” 的同义词。无论你用什么样的数据库账户名，比如 psql 中的 "user=alice"，它其实都是一个可以连接数据库的、拥有 LOGIN 属性的角色。也就是说，下面两条指令其实效果一样：

```
CREATE USER alice;
CREATE ROLE alice LOGIN;
```

Besides the ability to log in, roles can have other attributes that allow them to bypass all permission checks (`SUPERUSER`), create databases (`CREATEDB`), create other roles (`CREATEROLE`), and others.
除了登入的权限外，角色还可以拥有其他属性：可以通过所有的权限检查的`SUPERUSER`，可以创建数据库的`CREATEDB`，可以创建其他角色的`CREATEROLE`，等等。

In addition to attributes, roles can be granted permissions which can be split in two categories: membership in other roles and database object privileges. Let’s take a look at how these work in action.
除了属性外，角色被授予的权限可以分为两大类：一是其他角色的成员身份（membership），二是数据库对象的权限。下面我们将介绍这两类都是如何工作的。

### Granting Role Permissions

For our imaginary example, we will be tracking the server inventory:

```
CREATE TABLE server_inventory (
    id            int PRIMARY KEY,
    description   text,
    ip_address    text,
    environment   text,
    owner         text,
);
```

By default, PostgreSQL installation includes a superuser role (usually called “postgres”) used to bootstrap the database. Using this role for all database operations would be equivalent to always using “root” login on Linux, which is never a good idea. Instead, let’s create an unprivileged role and assign permissions to it as needed following the principle of least privilege.
默认情况下，PostgreSQL 安装会包含一个用于引导数据库的超级用户角色，通常被称作是 “postgres”。使用这一角色进行所有的数据库操作相当于在 Linux 系统中经常使用“root” 登入，永远不是个好主意。所以，我们要创建一个无权限角色，根据需要为其授予最小权限。

Rather than assigning privileges to each new user/role individually, you can create a “group role” and grant other roles (mapping onto individual users) membership in this group. Say, you want to allow your developers, Alice and Bob, to view the server inventory but not modify it:
通过创建一个 “组角色” 并授权其他角色（角色与用户一一对应）为组内成员，可以避免为每一个用户或角色单独分配权限的麻烦。假如说，我们想要授予开发者 Alice 和 Bob 查看服务器清单，但不允许其修改的权限：

```
-- Create a group role that doesn't have ability to login by itself and
-- grant it SELECT privileged on the server inventory table.
CREATE ROLE developer;
GRANT SELECT ON server_inventory TO developer;

-- Create two user accounts which will inherit "developer" permissions upon
-- logging into the database.
CREATE ROLE alice LOGIN INHERIT;
CREATE ROLE bob LOGIN INHERIT;

-- Assign both user account to the "developer" group role.
GRANT developer TO alice, bob;
```

Now, when connected to the database, both Alice and Bob will inherit privileges of the “developer” group role and be able to run queries on the server inventory.
现在，当连接到数据库时，Alice 和 Bob 都会继承 “developer” 组角色中的权限，并且可以查询数据库清单表。

The `SELECT` privilege applies to all table columns by default, though it doesn’t have to. Say, you only wanted to allow your interns to view the general server inventory information without letting them connect by hiding the IP address:
`SELECT`权限默认对表的所有列有效，但这一点也是可以更改的。假设我们只想让实习生查看服务器的大致信息但又不想让他们连接到服务器，那么就可以选择隐藏 IP 地址：

```
CREATE ROLE intern;
GRANT SELECT(id, description) ON server_inventory TO intern;
CREATE ROLE charlie LOGIN INHERIT;
GRANT intern TO charlie;
```

Other most commonly used database object privileges are `INSERT`, `UPDATE`, `DELETE` and `TRUNCATE` that correspond to the respective SQL statements, but you can also assign privileges for connecting to specific databases, creating new schemas or objects within the schema, executing functions and so on. Take a look at the [Privileges](https://www.postgresql.org/docs/13/ddl-priv.html) section of PostgreSQL documentation to see the whole list.

### Row-Level Security

One of the more advanced features of PostgreSQL privilege system is row-level security, which allows you to grant privileges to a subset of rows in a table. This includes both rows that can be queried with the `SELECT` statement, as well as rows that can be `INSERT`ed, `UPDATE`d and `DELETE`d.
PostgreSQL 权限系统的更高级的玩法是行级安全策略（RLS），它允许你为表中的部分行分配权限。这既包括可以用`SELECT`查询的行，也包括可以`INSERT`、`UPDATE`以及`DELETE`的行。

To start using row-level security, you need two things: enable it for a table and define a policy that will control row-level access.
想要使用行级安全，我们需要准备两件事情：在表中启用 RLS，为其定义一个用于控制行级访问的策略。

Building on our previous example, let’s say that you want to allow users to update only their own servers. First, enable RLS on the table:
继续之前的例子，假设我们只想允许用户更新他们自己的服务器。那么，第一步，启用表中的 RLS：

```
ALTER TABLE server_inventory ENABLE ROW LEVEL SECURITY;
```

Without any policy defined, PostgreSQL defaults to the “deny” policy which means no role (other than the table owner which is typically the role that created the table) has any access to it.
如果没有定义任何策略的话，PostgreSQL 会默认到 “拒绝” 策略上，这就意味着除了表的创建者 / 所有者之外，没有任何角色能够访问。

A row security policy is a Boolean expression that PostgreSQL will evaluate for each row that is supposed to be returned or updated. The rows returned by `SELECT` statements are checked against the expression specified with the `USING` clause, while the rows updated by `INSERT`, `UPDATE` or `DELETE` statements are checked against the `WITH CHECK` expression.
行安全策略是一个布尔表达式，是 PostgreSQL 用于判定所有需要返回或更新的行的条件。SELECT 语句返回的行将对照 USING 子语句所指定的表达式进行检查，而通过 INSERT，UPDATE 或 DELETE 语句更新的行将对照 WITH CHECK 表达式进行检查。

Let’s define a couple of policies that allow users to see all servers but only update their own, as determined by the “owner” field of the table:
首先，让我们定义几个策略，允许用户查看所有服务器，但只能更新他们自己服务器，这个 “自己” 是由表中的 “owner” 字段决定的。

```
CREATE POLICY select_all_servers
    ON server_inventory FOR SELECT
    USING (true);

CREATE POLICY update_own_servers
    ON server_inventory FOR UPDATE
    USING (current_user = owner)
    WITH CHECK (current_user = owner);
```

Note that only the owner of the table can create or update row security policies for it.

### Auditing

So far we have mostly talked about preemptive security measures. Following one of the cornerstone security principles, defense in depth, we have explored how they layer on top of each other to help slow down a hypothetical attacker’s progression through the system.
到目前为止，我们主要都在讨论先手防御的安全措施。根据其中一项基本的安全原则——深度防御，我们分析了这些措施是如何通过互相叠加，帮助减缓（假想中的）攻击者进攻系统的进程。

Keeping an accurate and detailed audit trail is one of the security properties of the system that is often overlooked. Monitoring the network-level or node-level access for your database server is out of scope of this post, but let’s take a look at what options we have when it comes to PostgreSQL server itself.
留存准确且详细的审计追踪记录是系统安全属性中常常被忽视的一点。监控数据库服务器端的网络层或节点层访问并不在本文的讨论范围内，但当涉及到 PostgreSQL 服务器本身时，不妨先来看看我们有什么可选项。

The most basic thing you can do to enhance visibility into what’s happening within the database is to enable verbose logging. Add the following directives to the server configuration file to turn on logging of all connection attempts and all executed SQL statements:
想要更清晰地观测数据库内情况时，最常用的方法是启用详细日志记录。在服务器配置文件中添加以下指令，开启对所有连接尝试以及已执行 SQL 的日志记录。

```
; Log successful and unsuccessful connection attempts.
log_connections = on

; Log terminated sessions.
log_disconnections = on

; Log all executed SQL statements.
log_statement = all
```

Unfortunately, this is pretty much the extent of what you can do with the standard self-hosted PostgreSQL installation out-of-the-box. It is better than nothing, of course, but it doesn’t scale well beyond a handful of database servers and simple “grepping”.
不幸的是，对于标准的自托管 PostgreSQL，这几乎是在不安装其他插件的情况下你能做到的全部了。虽然总比没有强，但在少数数据库服务器和简单的 “grep” 之外，它的延展性并不好。

For a more advanced PostgreSQL auditing solution, you can use a 3rd party extension such as [pgAudit](https://github.com/pgaudit/pgaudit). You will have to install the extension manually if you’re using a self-hosted PostgreSQL instance. Some hosted versions such as AWS RDS support it out-of-the-box, so you just need to enable it.
如果想要更高级的 PostgreSQL 审计解决方案，诸如 pgAudit 这样的第三方插件是个不错的选择。自托管的 PostgreSQL 需要手动安装，但对于一些托管版本的 PostgreSQL，比如 AWS RDS 这些，本身就有，我们只需启用即可。

pgAudit brings more structure and granularity to the logged statements. However, keep in mind that it is still logs-based, which makes it challenging to use if you want to ship your audit logs in structured format to an external SIEM system for detailed analysis.
对于记录的语句，pgAudit 提供了更多的结构和粒度。但要记住，它并没有脱离日志的范围，也就是说，如果需要将审计日志以结构化的格式传输到外部 SIEM 系统以作更详细的分析时，恐怕会很难。

Certificate-based Access to PostgreSQL
--------------------------------------

[Teleport for Database Access](https://goteleport.com/blog/introducing-database-access/) is the open source project we built with the goal of helping you implement the best practices for securing your PostgreSQL (and other) databases that we discussed in this post.

*   Users can access the databases through the single sign-on flow and use short-lived X.509 certificates for authentication instead of regular credentials.
*   Databases do not need to be exposed on the public Internet and can safely operate in air-gapped environments using Teleport’s built-in reverse tunnel subsystem.
*   Administrators and auditors can see the database activity such as sessions and SQL statements tied to a particular user’s identity in the audit log, and optionally ship it to an external system.

If you’re interested, you can get started with Teleport Database Access by watching the [demo](https://www.youtube.com/watch?v=wNCpYIDG1qM), reading the [docs](https://goteleport.com/docs/database-access/), downloading the [open-source version](https://goteleport.com/teleport/download), and exploring the code on [Github](https://github.com/gravitational/teleport).

Conclusion
----------

As with any system designed with security in mind, properly guarding access to your database instance requires taking protective measures on multiple levels of the stack.

In this article we’ve taken a look at the best practices in protecting your PostgreSQL database access on multiple levels, starting with the network and transport security, and explored how to use PostgreSQL flexible user privilege system.

**Related Posts**

*   [Preventing SSRF Attacks](https://goteleport.com/blog/ssrf-attacks/)
*   [What is a CSRF attack and what are the mitigation examples?](https://goteleport.com/blog/csrf-attacks/)
*   [In Search for a Perfect Access Control System](https://goteleport.com/blog/access-controls/)

