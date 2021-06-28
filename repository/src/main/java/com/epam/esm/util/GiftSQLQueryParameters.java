package com.epam.esm.util;

import lombok.Data;

@Data
public class GiftSQLQueryParameters {
    private String name;
    private String description;
    private String tagName;
    private SortBy sortBy;
    private SortOrientation sortOrientation;

}
