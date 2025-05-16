package org.example.demo.DAO;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.example.demo.Entity.SalesPerson;
import org.example.demo.Mappers.SalesPersonMapper;

import java.io.InputStream;
import java.util.List;

@RequestScoped
public class SalesPersonDAO {
    private SqlSession sqlSession;

    @PostConstruct
    public void init() {
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            sqlSession = sqlSessionFactory.openSession();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize SqlSession", e);
        }
    }

    @PreDestroy
    public void destroy() {
        if (sqlSession != null) {
            sqlSession.close();
        }
    }

    public SalesPerson findById(Long id) {
        SalesPersonMapper mapper = sqlSession.getMapper(SalesPersonMapper.class);
        return mapper.findById(id);
    }

    public List<SalesPerson> findAll() {
        SalesPersonMapper mapper = sqlSession.getMapper(SalesPersonMapper.class);
        return mapper.findAll();
    }

    @Transactional
    public void save(SalesPerson salesPerson) {
        SalesPersonMapper mapper = sqlSession.getMapper(SalesPersonMapper.class);
        if (salesPerson.getId() == null) {
            mapper.insert(salesPerson);
        } else {
            mapper.update(salesPerson);
        }
    }

    @Transactional
    public void delete(Long id) {
        SalesPersonMapper mapper = sqlSession.getMapper(SalesPersonMapper.class);
        mapper.delete(id);
    }

}
