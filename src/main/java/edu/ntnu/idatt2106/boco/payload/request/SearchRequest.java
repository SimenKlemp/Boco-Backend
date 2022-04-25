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

    private int page;

    private String sort;
    private boolean ascending;

    private float minPrice;
    private float maxPrice;

    private boolean mustBeDeliverable;
    private boolean mustBeRetrievable;
}
