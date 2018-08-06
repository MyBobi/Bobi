package com.bobi.test;


import com.bobi.po.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * Created by 吴俊俏
 * Date: 2018/8/6 0006
 * Time: 10:30
 */
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
