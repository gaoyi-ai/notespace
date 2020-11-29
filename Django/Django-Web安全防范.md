@[toc]
# sql注入攻击

在直接采用sql语句或者不通过re验证
比如在用户登陆中，输入username为' OR 1=1#
那位整个sql语句为true，即可拿到所有的用户信息

django的ORM即可以对输入字段进行转义验证


# 跨站脚本攻击xss(Cross Site Scripting)

正常用户假设向服务器发送请求，而这个请求会暴露在url
比如`http://www.bank.com/product/list/?name='iphone6'`

而黑客会在这段请求中插入一段js代码
比如`http://www.bank.com/product/list/?name=<script>x=document.cookie;alert(x);</script>`
取出用户的cookie，这样黑客就会向受害者发送这段代码，这样黑客就可以拿到用户的cookie发送到远程服务器，这样黑客就会伪装

首先代码里对用户输入的地方和变量都需要仔细检查长度和对"<"">"";"""等字符做过滤；

避免直接在cookie中泄露用户隐私，例如email、密码等等通过使cookie和系统ip绑定来降低cookie泄露后的危险

尽量采用POST 而非GET 提交表单

# CSRF跨站请求伪造(Cross Site Request Forgery)

 1. 用户信任并登陆A
 2. A返回sessionid
 3. 用户每次请求都会带上sessionid
 4. 用户没有登出的情况下访问B
 5. B要求用户访问A的一个url，url是用户看不见的

cookie方法下，假设当用户没有关闭标签页A，这是就会在用户不知情的情况下访问url
比如`<img src=http://www.mybank.com/Transfer/toBankld=11&money=1000>`这样B中加载图片就会向某账户转账

就算使用POST方法，黑客照样可以在链接中插入一个新的表单

django在网页表单中csrf_token是一次的，每次提交form表单时都会带上{% csrf_token %}
