package edu.ntnu.idatt2106.boco.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest
{
    private String text;

    private int page = 0;

    private SortField sortField = SortField.RELEVANCE;
    private boolean ascending = true;

    private float minPrice = 0;
    private float maxPrice = Float.POSITIVE_INFINITY;

    private boolean mustBePickupable = false;
    private boolean mustBeDeliverable = false;

    public enum SortField
    {
        RELEVANCE,
        PRICE;

        public org.apache.lucene.search.SortField.Type getType()
        {
            switch (this)
            {
                case PRICE:
                    return org.apache.lucene.search.SortField.Type.FLOAT;
            }
            return null;
        }

        @Override
        public String toString()
        {
            return super.toString().toLowerCase();
        }
    }
}
