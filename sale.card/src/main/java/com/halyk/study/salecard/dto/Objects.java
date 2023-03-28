package com.halyk.study.salecard.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Objects {

    @JacksonXmlElementWrapper(
            useWrapping = false
    )
    private List<Object> data;

}
