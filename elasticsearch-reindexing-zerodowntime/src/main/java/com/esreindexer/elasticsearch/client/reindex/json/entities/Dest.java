package com.esreindexer.elasticsearch.client.reindex.json.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO class for Dest Object. Used for _reindex api.
 * 
 * @author nikhil.bhide
 * @since 1.0
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dest {
	@JsonProperty(required=true)
    private String index;
    private String op_type;

    public String getIndex () {
        return index;
    }

    public void setIndex (String index) {
    	if(index==null || index.isEmpty())
    		throw new IllegalArgumentException();
        this.index = index;
    }

    public String getOp_type () {
        return op_type;
    }

    public void setOp_type (String op_type) {
        this.op_type = op_type;
    }

    @Override
    public String toString() {
        return "ClassPojo [index = "+index+", op_type = "+op_type+"]";
    }
}