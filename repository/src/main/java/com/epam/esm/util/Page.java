package com.epam.esm.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
public class Page {

    @Min(1)
    private int page;
    @Min(1)
    private int size;

    public static Page getDefaultPage() {
        Page page = new Page();
        page.setPage(1);
        page.setSize(20);

        return page;
    }

}
