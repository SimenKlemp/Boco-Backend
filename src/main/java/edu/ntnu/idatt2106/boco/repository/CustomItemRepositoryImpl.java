package edu.ntnu.idatt2106.boco.repository;

import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.payload.request.SearchRequest;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
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
    public List<Item> search(SearchRequest request)
    {
        try
        {
            Search.getFullTextEntityManager(entityManager).createIndexer().startAndWait();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        BooleanJunction junction = getQueryBuilder().bool();

        junction = shouldMatchText(junction, request.getText());
        junction = mustHavePriceInRange(junction, request.getMinPrice(), request.getMaxPrice());
        if (request.isMustBePickupable()) junction = mustBePickupable(junction);
        if (request.isMustBeDeliverable()) junction = mustBeDeliverable(junction);

        Query keywordQuery = junction.createQuery();
        logger.debug(keywordQuery.toString());

        FullTextQuery fullTextQuery = getJpaQuery(keywordQuery);
        if (request.getSortField() != SearchRequest.SortField.RELEVANCE)
        {
            SortField sortField = new SortField(request.getSortField().toString(), request.getSortField().getType(), !request.isAscending());
            Sort sort = new Sort(sortField);
            fullTextQuery.setSort(sort);
        }

        fullTextQuery.setFirstResult(request.getPage() * request.getPageSize());
        fullTextQuery.setMaxResults(request.getPageSize());

        logger.debug(fullTextQuery.toString());

        List items = fullTextQuery.getResultList();
        logger.debug(String.valueOf(items.size()));

        return items;
    }

    private BooleanJunction shouldMatchText (BooleanJunction junction, String text)
    {
        return junction.should(getQueryBuilder()
                .keyword()
                .fuzzy()
                .withEditDistanceUpTo(2)
                .withPrefixLength(0)
                .onFields("title", "description", "category")
                .matching(text)
                .createQuery()
        );
    }

    private BooleanJunction mustHavePriceInRange (BooleanJunction junction, float min, float max)
    {
        return junction
                .must(getQueryBuilder()
                    .range()
                    .onField("price")
                    .above(min)
                    .createQuery()
            )
            .must(getQueryBuilder()
                    .range()
                    .onField("price")
                    .below(max)
                    .createQuery()
            );
    }

    private BooleanJunction mustBePickupable(BooleanJunction junction)
    {
        return junction.must(getQueryBuilder()
                .keyword()
                .onField("isPickupable")
                .matching(Boolean.TRUE)
                .createQuery()
        );
    }

    private BooleanJunction mustBeDeliverable (BooleanJunction junction)
    {
        return junction.must(getQueryBuilder()
            .keyword()
            .onField("isDeliverable")
            .matching(Boolean.TRUE)
            .createQuery()
        );
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
