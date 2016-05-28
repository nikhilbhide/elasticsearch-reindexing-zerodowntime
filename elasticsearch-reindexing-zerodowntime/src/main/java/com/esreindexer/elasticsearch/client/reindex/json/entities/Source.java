package com.esreindexer.elasticsearch.client.reindex.json.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO class for Source Object. Used for _reindex api.
 * 
 * @author nikhil.bhide
 * @since 1.0
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Source {
	@JsonProperty(required=true)
    private String index;

    public String getIndex () {
        return index;
    }

    public void setIndex (String index) {
        this.index = index;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [index = "+index+"]";
    }
}