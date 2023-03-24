package com.ya.pf.product;

import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final SessionFactory sessionFactory;

    @Override
    public Page<ProductEntity> getProducts(String name, int pageNo, int pageSize, String sortBy, String order) {

        Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);

        if (name.isEmpty()) {
            return productRepository.findAll(pageable);
        } else {
            return productRepository.findByNameContaining(name, pageable);
        }
    }

    @Override
    public ProductEntity createProduct(ProductEntity productEntity) {

        return productRepository.save(productEntity);
    }

    @Override
    public ProductEntity updateProduct(ProductEntity productEntity) {

        return productRepository.save(productEntity);
    }

    @Override
    public void deleteProduct(long id) {

        productRepository.deleteById(id);
    }

    @Override
    public List<ProductEntity> searchProduct(String name) {

        return productRepository.findByNameContaining(name);
    }

    @Override
    public double getProductPrice(long id) {

        Session session;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Double> criteria = builder.createQuery(Double.class);
        Root<ProductEntity> root = criteria.from(ProductEntity.class);
        criteria.select(root.get("price"));
        criteria.where(builder.equal(root.get("id"), id));
        TypedQuery<Double> query = session.createQuery(criteria);
        Double result = query.getSingleResult();
        session.close();

        return result;
    }

}
