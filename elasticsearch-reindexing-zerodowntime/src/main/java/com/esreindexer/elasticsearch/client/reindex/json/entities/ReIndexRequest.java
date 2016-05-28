package com.esreindexer.elasticsearch.client.reindex.json.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO class for ReIndexRequest Object. Used for _reindex api.
 * 
 * @author nikhil.bhide
 * @since 1.0
 * 
 */
public class ReIndexRequest {
    private String conflicts;
	@JsonProperty(required=true)
    private Dest dest;
	@JsonProperty(required=true)
    private Source source;
    public String getConflicts () {
        return conflicts;
    }

    public void setConflicts(String conflicts) {
        this.conflicts = conflicts;
    }
    public Dest getDest() {
        return dest;
    }

    public void setDest(Dest dest) {
        this.dest = dest;
    }

    public Source getSource() {
        return source;
    }

    public void setSource (Source source) {
        this.source = source;
    }
}