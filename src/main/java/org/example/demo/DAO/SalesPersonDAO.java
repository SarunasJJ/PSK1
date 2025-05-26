package org.example.demo.DAO;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.example.demo.Entity.SalesPerson;
import org.example.demo.Mappers.SalesPersonMapper;

import java.io.InputStream;
import java.util.List;

@ApplicationScoped
public class SalesPersonDAO {
    private SqlSessionFactory sqlSessionFactory;

    @PostConstruct
    public void init() {
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize SqlSessionFactory", e);
        }
    }

    public SalesPerson findById(Long id) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            SalesPersonMapper mapper = sqlSession.getMapper(SalesPersonMapper.class);
            return mapper.findById(id);
        }
    }

    public List<SalesPerson> findAll() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            SalesPersonMapper mapper = sqlSession.getMapper(SalesPersonMapper.class);
            return mapper.findAll();
        }
    }

    @Transactional
    public void save(SalesPerson salesPerson) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            SalesPersonMapper mapper = sqlSession.getMapper(SalesPersonMapper.class);
            if (salesPerson.getId() == null) {
                mapper.insert(salesPerson);
            } else {
                mapper.update(salesPerson);
            }
        }
    }

    @Transactional
    public void delete(Long id) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {  // Auto-commit
            SalesPersonMapper mapper = sqlSession.getMapper(SalesPersonMapper.class);
            mapper.delete(id);
        }
    }
}