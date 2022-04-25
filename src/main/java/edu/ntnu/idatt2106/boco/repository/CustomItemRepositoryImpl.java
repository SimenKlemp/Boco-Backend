package edu.ntnu.idatt2106.boco.repository;

import edu.ntnu.idatt2106.boco.models.Item;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CustomItemRepositoryImpl implements CustomItemRepository
{
    @PersistenceContext
    private EntityManager entityManager;

    private final Logger logger = LoggerFactory.getLogger(CustomItemRepositoryImpl.class);

    @Override
    public List<Item> search(String text)
    {
        try
        {
            Search.getFullTextEntityManager(entityManager).createIndexer().startAndWait();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        Query keywordQuery = getQueryBuilder()
                .keyword()
                .fuzzy()
                .withEditDistanceUpTo(2)
                .withPrefixLength(0)
                .onFields("title", "description", "category")
                .matching(text)
                .createQuery();

        logger.debug(keywordQuery.toString());

        FullTextQuery fullTextQuery = getJpaQuery(keywordQuery);

        logger.debug(fullTextQuery.toString());

        List items = fullTextQuery.getResultList();

        logger.debug(String.valueOf(items.size()));

        return items;
    }

    private FullTextQuery getJpaQuery(Query luceneQuery) {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        return fullTextEntityManager.createFullTextQuery(luceneQuery, Item.class);
    }

    private QueryBuilder getQueryBuilder() {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        return fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Item.class)
                .get();
    }
}
