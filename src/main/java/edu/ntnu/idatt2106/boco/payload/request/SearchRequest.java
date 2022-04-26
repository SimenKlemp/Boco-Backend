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
    private int pageSize = 0;

    private SortField sortField = SortField.RELEVANCE;
    private boolean ascending = true;

    private float minPrice = 0;
    private float maxPrice = Float.POSITIVE_INFINITY;

    private boolean mustBePickupable = false;
    private boolean mustBeDeliverable = false;

    public enum SortField
    {
        RELEVANCE,
        PRICE,
        PUBLICITY_DATE;

        public org.apache.lucene.search.SortField.Type getType()
        {
            switch (this)
            {
                case PRICE:
                    return org.apache.lucene.search.SortField.Type.FLOAT;
                case PUBLICITY_DATE:
                    return org.apache.lucene.search.SortField.Type.LONG;
            }
            return null;
        }

        @Override
        public String toString()
        {
            String text =  super.toString().toLowerCase();

            String[] words = text.split("[\\W_]+");

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                if (i == 0) {
                    word = word.isEmpty() ? word : word.toLowerCase();
                } else {
                    word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
                }
                builder.append(word);
            }
            return builder.toString();
        }
    }
}
