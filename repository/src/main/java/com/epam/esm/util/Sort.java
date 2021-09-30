package com.epam.esm.util;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Sort {

    private SortBy sortBy;
    private SortOrientation sortOrientation = SortOrientation.ASC;
}