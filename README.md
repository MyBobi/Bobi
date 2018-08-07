# MyBatisFirstDemo
搭建MyBatis工作环境以及增删改查基本操作

## 数据库准备
创建一个数据库为mybatus-test,表为user
```
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
insert  into `user`(`id`,`name`,`age`) values 
(1,'李三',18),(2,'张三',25);
```
## 搭建环境
使用idea开发，所有考虑使用maven来管理项目，下面是pom.xml文件
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.Bobi</groupId>
    <artifactId>Bobi</artifactId>
    <version>1.0-SNAPSHOT</version>
<dependencies>
    <!-- mybatis核心包 -->
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.3.0</version>
    </dependency>
    <!-- mysql驱动包 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.29</version>
    </dependency>
    <!-- junit测试包 -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.11</version>
        <scope>test</scope>
    </dependency>
    <!-- 日志文件管理包 -->
    <dependency>
        <groupId>log4j</groupId>
        <artifactId>log4j</artifactId>
        <version>1.2.17</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>1.7.12</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-log4j12</artifactId>
        <version>1.7.12</version>
    </dependency>
</dependencies>
</project>
```
接着再加个日志输出环境配置文件，log4j.properties文件如下
各个元素得具体作用是什么，可以自行了解一下，这里就不多言啦
```
# Global logging configuration
log4j.rootLogger=DEBUG, stdout  
# Console output...
log4j.appender.stdout=org.apache.log4j.ConsoleAppender  
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout  
log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n 
```
为了体现所谓得不硬编码，所有加个数据库配置文件吧，sql.properties
根据自己得用户名和密码改一改
```
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/mybatis_test
jdbc.username=root
jdbc.password=123456
```
前面写了那么多配置文件，下面终于写mybatis的配置文件啦，mybatis-config.xml
注释已经写的很清楚了
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <properties resource="mysql.properties"/>
    <settings>
        <!--全局性设置懒加载。如果设为‘false’，则所有相关联的都会被初始化加载,默认值为false-->
        <setting name="lazyLoadingEnabled" value="true"/>
        <!--当设置为‘true’的时候，懒加载的对象可能被任何懒属性全部加载。否则，每个属性都按需加载。默认值为true-->
        <setting name="aggressiveLazyLoading" value="false"/>
    </settings>
    <typeAliases>
        <!-- 其实就是将bean的替换成一个短的名字-->
        <typeAlias type="com.bobi.po.User" alias="User"/>
    </typeAliases>
    <!--对事务的管理和连接池的配置-->
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"></transactionManager>
            <dataSource type="POOLED"><!--POOLED：使用Mybatis自带的数据库连接池来管理数据库连接-->
                <property name="driver" value="${jdbc.driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>
    <!--mapping文件路径配置-->
    <mappers>
        <mapper resource="mapper/UserMapper.xml"/>
    </mappers>

</configuration>
```
以上的配置文件视人而异吧！大概就是这样子啦！！！！
## 编写sql映射配置文件
数据库中就一张表，现在就拿这张表来普通查询，模糊查询，增加，修改，删除操作
实体类User.java
```
package com.bobi.po;

public class User {

    private int id;
    private String name;
    private int age;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```
映射文件UserMapper.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.bobi.po.User">
    <!--下列的id必须唯一的-->
    <insert id="insertUser" parameterType="com.bobi.po.User" useGeneratedKeys="true">
        INSERT INTO USER(name,age) VALUES(#{name},#{age})
    </insert>

    <update id="updateUsername" parameterType="com.bobi.po.User">
        UPDATE USER SET name=#{name} WHERE id=#{id}
    </update>

    <delete id="deleteUser" parameterType="java.lang.Integer">
        DELETE FROM USER WHERE id=#{id}
    </delete>

    <select id="findById" parameterType="int" resultType="com.bobi.po.User">
        SELECT * FROM User WHERE id=#{id}
    </select>

    <select id="findUserByUsername" parameterType="java.lang.String" resultType="com.bobi.po.User">
        SELECT * FROM USER WHERE name LIKE '%${value}%'
    </select>

</mapper>
```
在这里值得注意一下的是平时可能会遇到如下的错误
![](https://images2018.cnblogs.com/blog/1392149/201808/1392149-20180806231803041-1788736127.png)
如果有遇到如图中的问题，根据图中的提示修改就可以啦

最后一个就是编写w测试类了，在src\main\java中编写一个UserTest.java
```
package com.bobi.test;
import com.bobi.po.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
public class UserTest {

    public static void main(String[] args) {
        String resoure="mybatis-config.xml";
        Reader reader=null;
        try {
            reader= Resources.getResourceAsReader(resoure);
            //创建会话工厂。传入MyBatis配置文件信息
            SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(reader);
            //通过会话工厂创建会话session
            SqlSession session=sqlSessionFactory.openSession();

            //通过id查询用户
            User user=session.selectOne("findById",2);
            System.out.println(user.getName());
            System.out.println(user.getAge());
            //通过模糊查询查含有“三”字的用户，并存于一个list列表中
            List<User> list=session.selectList("findUserByUsername","三");
            for (int i=0;i<list.size();i++){
                User user1=list.get(i);
                System.out.println(user1.getName());
                System.out.println(user1.getAge());
            }
            //新增一个用户
            User user2=new User();
            user2.setName("王五");
            user2.setAge(28);
            session.insert("insertUser",user2);
            //删除一个用户
            session.delete("deleteUser",6);
            //更改一个用户，将id为5的用名字改为孙俪
            User user3=new User();
            user3.setId(5);
            user3.setName("孙俪");
            session.update("updateUsername",user3);
            session.commit();
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

```
OK,基本完成啦！博客地址：https://www.cnblogs.com/Jackic/p/9434283.html
## 欢迎关注公众号
![](https://images2018.cnblogs.com/blog/1392149/201808/1392149-20180807101014450-1449937836.jpg)
### 欢迎投稿分享个人工作、生活、项目经验
### 博主也经常分享一些干货教学视频哦！
